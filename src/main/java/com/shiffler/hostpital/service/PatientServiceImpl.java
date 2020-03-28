package com.shiffler.hostpital.service;

import com.shiffler.hostpital.entity.Patient;
import com.shiffler.hostpital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    PatientRepository patientRepository;

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
        patientRepository.save(patient);

    }
}
