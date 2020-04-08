package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.MedicalTestResultEnum;
import com.shiffler.hospital.mappers.MedicalTestMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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

    @Value("${medtests.srcfile}")
    private String testSource; //The path to the file that holds the possible tests

    private List<MedicalTest> medicalTestList = new ArrayList<>();


    private final String MEDICAL_TEST_PATH ="/api/v1/medicaltestorder";
    private final RestTemplate restTemplate;
    private final MedicalTestMapper medicalTestMapper;


    @Autowired
    public MedicalTestServiceImpl(RestTemplateBuilder restTemplateBuilder,
                                  MedicalTestMapper medicalTestMapper) {

        this.restTemplate = restTemplateBuilder.build();
        this.medicalTestMapper = medicalTestMapper;
    }

    /**
     * Orders a medical test from the Medical Test provider
     * @param medicalTest - The medical test we need to order
     */
    public void orderMedicalTest(MedicalTest medicalTest) {

        URI uri = null;

        MedicalTestDto medicalTestDto = medicalTestMapper.medicalTestToMedicalTestDto(medicalTest);

        try {
            uri = restTemplate.postForLocation(this.apiHost + MEDICAL_TEST_PATH, medicalTestDto);
            log.info(uri.toString());
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
        return medicalTestList.get(new Random().nextInt(medicalTestList.size()));

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
                    MedicalTest.builder().testCode(testCode).testName(testName)
                            .medicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT).build()
            );

        }
    } //close method


} //close class
