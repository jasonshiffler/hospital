package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.MedicalTestOrderStatusEnum;
import com.shiffler.hospital.entity.MedicalTestResultEnum;
import com.shiffler.hospital.mappers.MedicalTestMapper;
import com.shiffler.hospital.repository.MedicalTestRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@Data
public class MedicalTestServiceImpl implements MedicalTestService {

    //The hostname of the Medical Testing Center
    @Value("${api.host}")
    private String apiHost;

    //The path to the file that holds the possible tests
    @Value("${medtests.srcfile}")
    private String testSource;

    //holds the list of possible Medical Tests
    private List<MedicalTest> medicalTestList = new ArrayList<>();

    //The Path of the API to access
    private final String MEDICAL_TEST_PATH ="/api/v1/medicaltestorder";

    private final RestTemplate restTemplate;
    private final MedicalTestMapper medicalTestMapper;
    private final MedicalTestRepository medicalTestRepository;

    @Autowired
    public MedicalTestServiceImpl(RestTemplateBuilder restTemplateBuilder,
                                  MedicalTestMapper medicalTestMapper,
                                  MedicalTestRepository medicalTestRepository) {

        this.restTemplate = restTemplateBuilder.build();
        this.medicalTestMapper = medicalTestMapper;
        this.medicalTestRepository = medicalTestRepository;
    }


    /**
     * Look through the Medical Test Repository for all tests that have a test status of NOT_SUBMITTED and place an order for them.
     *
     */
    public void orderUnSubmittedTests(){

        log.info("Ordering Medical Tests in NOT_SUBMITTED status");

        List <MedicalTest> medicalTestList = medicalTestRepository
                .findByMedicalTestOrderStatus(MedicalTestOrderStatusEnum.NOT_SUBMITTED);
        log.info ("Found {} Medical Tests in NOT_SUBMITTED status", medicalTestList.size());

        medicalTestList.stream().parallel().forEach(s -> orderMedicalTest(s));
    }

    /**
     * Retreive a List of Tests that are in either ORDER_PLACED,ORDER_PLACED_ONHOLD, or TEST_IN_PROCESS
     * status and get an update on their status
     */
    public void updateStatusForPendingTests(){
        log.info("Updating Test Status");

        List <MedicalTest> medicalTestList = medicalTestRepository
                .findByMedicalTestOrderStatus(MedicalTestOrderStatusEnum.ORDER_PLACED);

        medicalTestList.stream().parallel().forEach(s -> getTestStatusFromTestCenter(s));
    }

    public void getTestStatusFromTestCenter(MedicalTest medicalTest) {

        MedicalTestDto medicalTestDto = new MedicalTestDto();

        try {



            String thing = restTemplate.getForObject(this.apiHost
                                    + MEDICAL_TEST_PATH
                                    + "/"
                                    + medicalTest.getOrderNumber()
                            , String.class);

          /*  medicalTestDto = restTemplate
                    .getForObject(this.apiHost
                            + MEDICAL_TEST_PATH
                            + "/"
                            + medicalTest.getOrderNumber()
                            , MedicalTestDto.class);*/

            log.info(thing.toString());

        }catch (Exception e){
            log.error("Error getting Status update for Medical Test: " + e.getMessage() + medicalTestDto.toString() );
        }

    }


    /**
     * Orders a medical test from the Medical Test provider
     * @param medicalTest - The medical test we need to order
     */
    public void orderMedicalTest(MedicalTest medicalTest) {

        URI uri = null;
        String[] result;

        MedicalTestDto medicalTestDto = medicalTestMapper.medicalTestToMedicalTestDto(medicalTest);

        try {
            uri = restTemplate.postForLocation(this.apiHost + MEDICAL_TEST_PATH, medicalTestDto);
            String url  = uri.toString();
            log.info("Location recieved for Medical Test Order {} " , url.toString());

            //The result looks like http://localhost:8081/100000298 . We want to split off the just the id and save it.
            result = url.toString().split("/");

            //check to make sure the id is valid
            if (result[3] == null ||result[3].length() != 9 || (NumberUtils.isParsable(result[3]) == false)){
                log.error("Location received for Medical Test {} + is invalid",medicalTestDto.toString());
            }
            else{
                //if the test is valid change the order status and save the order number
                medicalTest.setTestStatus(MedicalTestOrderStatusEnum.ORDER_PLACED);
                medicalTest.setOrderNumber(Long.parseLong(result[3]));
                medicalTestRepository.save(medicalTest);
            }


        } catch (Exception e){
            log.error("Error creating API order for Medical Test: " + e.getMessage() + medicalTestDto.toString() );
        }
        finally {
            if (uri != null &&  uri.toString().contains(apiHost+MEDICAL_TEST_PATH)){
                medicalTestDto.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
            }
        }

    } // close method


    /**
     * Creates a random Medical test
     *
     * @return
     */
    public MedicalTest generateRandomMedicalTest() {

        MedicalTest medicalTest = medicalTestList.get(new Random().nextInt(medicalTestList.size()));
        medicalTest.setTestStatus(MedicalTestOrderStatusEnum.NOT_SUBMITTED);
        medicalTest.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
        return medicalTest;

    }

    /**
     * Populates the MedicalTestList at startup.
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {

        log.info("*** Generating Medical Tests ***");

        this.createMedicalTestList(this.testSource);

        log.info("*** Medical Tests Generated ***");

        medicalTestList.stream().forEach(s -> log.info(s.toString()));
    }


    /**
     * Create a list of all the medical tests that are available
     *
     * @param filename - The file that contains the list of available tests
     * @return
     * @throws IOException
     */
    private void createMedicalTestList(String filename) throws IOException {

        String line;
        List<String> nameList = new ArrayList<>();
        String[] values = new String[2];
        String testName;
        String testCode;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while ((line = reader.readLine()) != null) {

            values = line.split(",");
            log.info("Test is {}", line);

            testCode = values[0];
            testName = values[1];

            this.medicalTestList.add(
                    MedicalTest.builder()
                            .testCode(testCode)
                            .testName(testName)
                            .build()
            );

        }
    } //close method


} //close class
