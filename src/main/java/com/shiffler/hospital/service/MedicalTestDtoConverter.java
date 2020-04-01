package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;

public class MedicalTestDtoConverter {

    /**
     * Converts the object of type MedicalTest to a type that is useful to the testing center
     * @param medicalTest - The MedicalTest Object that needs to be converted
     * @return - The MedicalTestDto Object that the testing center will accept.
     */
    public static MedicalTestDto MedicalTestToMedicalTestDto(MedicalTest medicalTest){
        MedicalTestDto medicalTestDto = new MedicalTestDto();
        medicalTestDto.setTestCode(medicalTest.getTestCode());
        medicalTestDto.setReferenceUUID(medicalTest.getId());

        return medicalTestDto;

    }

}
