package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;

import java.util.List;

public interface PatientService {

    //Add a new Medical Patient
    public void savePatient(Patient patient);

    //Add a Medical Test to an existing Patient
    public void addMedicalTest(Patient patient, MedicalTest medicalTest);

    //Return a list of all of the patients
    public Iterable<Patient> getAllPatients();
}
