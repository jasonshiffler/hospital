package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.Patient;

import java.util.List;

public interface PatientTester {

    /**
     * Determine if the patient needs a test.
     * If they need a test one will be created for them.
     * @param patient - The patient who will be check for if a test is needed.
     */
    public void assignTests(Patient patient);
}
