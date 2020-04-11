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
        patient1 = new Patient();
        patientList = new ArrayList<>();
        patientList.add(patient1);

        medicalTest1 = new MedicalTest();

    }

    @Test
    void savePatient() {

        //Given

        //When
        patientService.savePatient(patient1);

        //Then
        then(patientRepository).should(times(1)).save(any(Patient.class));
        then(patientRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void addMedicalTestToPatient() {

        //Given - Initial condition is that the Patient Medical Test list is empty
        assertThat(patient1.getMedicalTests().size()== 0);

        //When
        patientService.addMedicalTestToPatient(patient1, medicalTest1);

        //Then
        assertThat(patient1.getMedicalTests()).isNotNull();
        then(patientRepository).should(times(1)).save(any(Patient.class));
        then(patientRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void getAllPatients() {

        //Given
        given(patientRepository.findAll()).willReturn(patientList);

        //When
        Iterable<Patient> patientListResults = patientService.getAllPatients();

        //Then
        assertThat(patientListResults.iterator().hasNext() == true);
        then(patientRepository).should(times(1)).findAll();
        then(patientRepository).shouldHaveNoMoreInteractions();


    }

    @Test
    void assignRandomTestToPatient() {
    }
}