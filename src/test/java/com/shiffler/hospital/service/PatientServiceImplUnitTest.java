/*
Unit Tests for the PatientServiceImpl Class all dependencies will be Mocked.
*/
package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.helper.NameGeneratorImpl;
import com.shiffler.hospital.helper.RandomDateGeneratorImpl;
import com.shiffler.hospital.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplUnitTest {

    @Mock
    PatientRepository patientRepository;

    @Mock
    NameGeneratorImpl nameGenerator;

    @Mock
    MedicalTestServiceImpl medicalTestService;

    @Mock
    RandomDateGeneratorImpl randomDateGenerator;

    @InjectMocks
    PatientServiceImpl patientService;

    Patient patient1;

    MedicalTest medicalTest1;

    List<Patient> patientList;

    @BeforeEach
    void init(){

        //Initialize the Patient, the Patient List, and the Medical Test
        patient1 = new Patient();
        patientList = new ArrayList<>();
        patientList.add(patient1);

        medicalTest1 = new MedicalTest();

    }

    @Test
    void savePatient() {

        //Given - Nothing to setup

        //When- We save the patient
        patientService.savePatient(patient1);

        //Then - The Repository should call its save method on the patient and then should be finished.
        then(patientRepository).should(times(1)).save(any(Patient.class));
        then(patientRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void addMedicalTestToPatient() {

        //Given - Initial condition is that the Patient Medical Test list is empty
        assertThat(patient1.getMedicalTests().size()== 0);

        //When - We add a Medical Test to the Patient
        patientService.addMedicalTestToPatient(patient1, medicalTest1);

        //Then - The Patient should have one Medical Test and the repository should the patient and then be finished
        assertThat(patient1.getMedicalTests().size() == 1);
        then(patientRepository).should(times(1)).save(any(Patient.class));
        then(patientRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAllPatients() {

        //Given - The findAll method  in the repository will return the patientList when called
        given(patientRepository.findAll()).willReturn(patientList);

        //When - The patientService retrieves all of the patients.
        Iterable<Patient> patientListResults = patientService.getAllPatients();

        //Then
        assertThat(patientListResults.iterator().hasNext() == true);
        then(patientRepository).should(times(1)).findAll();
        then(patientRepository).shouldHaveNoMoreInteractions();
    }
/*
    @Test
    void assignRandomTestToPatient() {

        //Given - Initial condition is that the Patient Medical Test list is empty
        assertThat(patient1.getMedicalTests().size()== 0);
        given(medicalTestService.generateRandomMedicalTest()).willReturn(medicalTest1);


        //When - We assign a random test to the patient
        patientService.assignRandomTestToPatient(patient1);

        //Then - The patient should have an associated medical test, the repository should save the new state of the
        //       patient and the repository should be finished.
        then(patientService).should().addMedicalTestToPatient(patient1, medicalTest1);


    }*/
}