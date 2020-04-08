package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;

import java.util.List;

public interface PatientService {

    //Add a new Medical Patient
    void savePatient(Patient patient);

    //Add a Medical Test to an existing Patient
    void addMedicalTestToPatient(Patient patient, MedicalTest medicalTest);

    //Return a list of all of the patients
    Iterable<Patient> getAllPatients();

    //Assign a Random MedicalTest to a Patient
    void assignRandomTestToPatient(Patient patient);

    //Generates a Random Patient
    Patient generateRandomPatient();

}
