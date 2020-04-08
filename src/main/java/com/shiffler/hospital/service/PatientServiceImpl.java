package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Saves a patient to the repository
     * @param patient - The patient object that needs to be saved to the repository
     */
    @Override
    public void savePatient(Patient patient) {

        log.info("Saving patient {}", patient.toString());

        patientRepository.save(patient);

    }

    /**
     * Add a new medical test to an existing patient
     * @param patient - the patient that the test should be added to.
     * @param medicalTest - the medical test that will be added.
     */
    @Override
    public void addMedicalTest(Patient patient, MedicalTest medicalTest) {

        log.info("Adding Medical Test {} to patient {}", medicalTest.toString(), patient.toString());
        List<MedicalTest> test = patient.getMedicalTests();
        test.add(medicalTest);
        patientRepository.save(patient);

    }

    /**
     * Return a list of all of the patients in the repository
     * @return The List of all of the patients.
     */
    @Override
    public Iterable<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
