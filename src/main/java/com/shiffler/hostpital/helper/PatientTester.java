package com.shiffler.hostpital.helper;

import com.shiffler.hostpital.entity.Patient;

import java.util.List;

public interface PatientTester {

    /**
     * Takes a list of patients and randomly determines if they need a test.
     * If they need a test one will be created for them.
     * @param patientList - A List of patients
     */
    public void assignTests(List<Patient> patientList);
}
