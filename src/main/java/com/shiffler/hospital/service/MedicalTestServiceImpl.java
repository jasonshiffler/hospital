/*
This class is the layer to access management of Medical Test Data.
 */

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

    @Value("${testingcenter.uname}")
    private String username;

    @Value("${testingcenter.pword}")
    private String password;

    //holds the list of possible Medical Tests
    private List<MedicalTest> medicalTestList = new ArrayList<>();

    //The Path of the API to access
    private final String MEDICAL_TEST_PATH ="/api/v1/medicaltestorders";

    //This is used to retrieve information on the Medical Test
    private RestTemplate restTemplate;

    //We'll use this to build the REST template
    private final RestTemplateBuilder restTemplateBuilder;

    //Allows us to map between the DTO and the Entity
    private final MedicalTestMapper medicalTestMapper;

    //Allows access to the database with all of the tests.
    private final MedicalTestRepository medicalTestRepository;



    @Autowired
    public MedicalTestServiceImpl(RestTemplateBuilder restTemplateBuilder,
                                  MedicalTestMapper medicalTestMapper,
                                  MedicalTestRepository medicalTestRepository) {


        this.restTemplateBuilder = restTemplateBuilder;
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
        log.info("Updating Test Status for Pending Tests");

        //Grab the pending medical tests from the DB
        List <MedicalTest> medicalTestList = medicalTestRepository
                .findByMedicalTestMultipleStatus(MedicalTestOrderStatusEnum.ORDER_PLACED,
                        MedicalTestOrderStatusEnum.ORDER_PLACED_ONHOLD,
                        MedicalTestOrderStatusEnum.TEST_IN_PROCESS);


        //Ask the Medical Center for status updates on each test
        medicalTestList.stream().forEach(s -> getMedicalTestStatusFromTestCenter(s));

        log.info("Test Status Updates Complete");
    }

    /**
     * Get the status of a particular Medical Test from the testing center.
     * @param medicalTest
     */
    public void getMedicalTestStatusFromTestCenter(MedicalTest medicalTest) {

        boolean updateEntity = false; //Track the need to update the entity in the database
        log.info("#############################################");
        log.info("Retrieving test status for test: {}", medicalTest);


        MedicalTestDto medicalTestDto = new MedicalTestDto();

        //Retreive the DTO object from the test center
        try {

            medicalTestDto = restTemplate
                    .getForObject(this.apiHost
                            + MEDICAL_TEST_PATH
                            + "/"
                            + medicalTest.getOrderNumber()
                            , MedicalTestDto.class);

            //Update the order status of the medical test if it has changed and isn't null
            if (medicalTest.getTestOrderStatusEnum() != medicalTestDto.getTestOrderStatusEnum()
                    && (medicalTestDto.getTestOrderStatusEnum() != null)) {
                medicalTest.setTestOrderStatusEnum(medicalTestDto.getTestOrderStatusEnum());
                log.info("Medical Test order status updated to {} ", medicalTest.getTestOrderStatusEnum());
                updateEntity= true;
            }

            //Update the result status of the medical test if it has changed and isn't null

            if (medicalTest.getMedicalTestResultEnum() != medicalTestDto.getMedicalTestResultEnum()
                    && (medicalTestDto.getMedicalTestResultEnum() != null)) {
                medicalTest.setMedicalTestResultEnum(medicalTestDto.getMedicalTestResultEnum());
                log.info("Medical Test result status updated to {} ", medicalTest.getMedicalTestResultEnum());
                updateEntity= true;
            }

            if (updateEntity == true){
                log.info("Updating status of medical test saved");
                medicalTestRepository.save(medicalTest);
            } else if(updateEntity == false){
                log.info("No updates for medical test");
            }
            log.info("#############################################");

        }catch (Exception e){
            log.error("Error getting Status update for Medical Test: " + e.getMessage() + medicalTestDto.toString() );
        }
    } //close method

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

            //The result looks like http://localhost:8081//api/v1/medicaltestorders/100000298 . We want to split off the just the id and save it.
            result = url.toString().split("/");

            //check to make sure the id is valid
            if (result[6] == null ||result[6].length() != 9 || (NumberUtils.isParsable(result[6]) == false)){
                log.error(" Invalid location received for Medical Test {} ",medicalTestDto.toString());
            }
            else{
                //if the test is valid change the order status and save the order number
                medicalTest.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.ORDER_PLACED);
                medicalTest.setOrderNumber(Long.parseLong(result[6]));
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
     * @return
     */
    public MedicalTest generateRandomMedicalTest() {

        MedicalTest medicalTest = medicalTestList.get(new Random().nextInt(medicalTestList.size()));
        medicalTest.setTestOrderStatusEnum(MedicalTestOrderStatusEnum.NOT_SUBMITTED);
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

        //This has to be done after the constructor since anything with @Value(our username and password) is
        //done after the constructor if finished.

        restTemplate = restTemplateBuilder.basicAuthentication(username,password).build();

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
