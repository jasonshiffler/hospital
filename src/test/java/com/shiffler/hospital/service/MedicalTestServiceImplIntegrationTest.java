/*
We're using an integration test here so we can confirm the MedicalTest file is read correctly which allows us
to test the private methods as well since they are called.
The other methods will be tested using Mocks.
 */

package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.MedicalTestOrderStatusEnum;
import com.shiffler.hospital.entity.MedicalTestResultEnum;
import com.shiffler.hospital.mappers.MedicalTestMapper;
import com.shiffler.hospital.repository.MedicalTestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;


import static org.junit.jupiter.api.Assertions.*;

//Set the properties file we want to use
@TestPropertySource(locations="classpath:test.properties")
@SpringBootTest(classes = {MedicalTestServiceImpl.class})
public class MedicalTestServiceImplIntegrationTest {

    @Autowired
    MedicalTestServiceImpl medicalTestService;

    @MockBean
    MedicalTestMapper medicalTestMapper;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @MockBean
    MedicalTestRepository medicalTestRepository;

    @Test
    void generateRandomMedicalTest() {
        MedicalTest medicalTest = medicalTestService.generateRandomMedicalTest();
        assertEquals("00000A0001", medicalTest.getTestCode());
        assertEquals("SARS-CoV-2", medicalTest.getTestName());
        assertEquals(MedicalTestResultEnum.WAITING_FOR_RESULT, medicalTest.getMedicalTestResultEnum());
        assertEquals(MedicalTestOrderStatusEnum.NOT_SUBMITTED, medicalTest.getTestOrderStatusEnum());
    }

}