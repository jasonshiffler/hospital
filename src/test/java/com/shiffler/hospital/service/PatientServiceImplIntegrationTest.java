/*
This integration test is for the generateRandomPatient Method
 */

package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.helper.NameGeneratorImpl;
import com.shiffler.hospital.helper.RandomDateGeneratorImpl;
import com.shiffler.hospital.mappers.MedicalTestMapper;
import com.shiffler.hospital.repository.MedicalTestRepository;
import com.shiffler.hospital.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;

@SpringBootTest(classes={NameGeneratorImpl.class,RandomDateGeneratorImpl.class,
        MedicalTestServiceImpl.class, PatientServiceImpl.class})

@TestPropertySource(locations="classpath:test.properties")
public class PatientServiceImplIntegrationTest {

    @Autowired
    NameGeneratorImpl nameGenerator;

    @Autowired
    RandomDateGeneratorImpl randomDateGenerator;

    @Autowired
    MedicalTestServiceImpl medicalTestService;

    @Autowired
    PatientServiceImpl patientService;

    @MockBean
    PatientRepository patientRepository;

    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @MockBean
    MedicalTestMapper medicalTestMapper;

    @MockBean
    MedicalTestRepository medicalTestRepository;

    Patient patient;


    @Test
    void generateRandomPatient() {
        patient = patientService.generateRandomPatient();
        assertEquals("Smith", patient.getLastName());
        assertEquals("A", patient.getMiddleInitial());
        assertThat(patient.getFirstName(), anyOf(containsString("Emma"),containsString("Liam") ));
    }
}