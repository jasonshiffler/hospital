package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;

public interface MedicalTestService {

    void orderMedicalTest(MedicalTest medicalTest);
    MedicalTest generateRandomMedicalTest();

}
