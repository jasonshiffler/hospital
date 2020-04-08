package com.shiffler.hospital.mappers;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import org.mapstruct.Mapper;

@Mapper // Signals that this is a Mapstruct Mapper
public interface MedicalTestMapper {
    MedicalTestDto medicalTestToMedicalTestDto(MedicalTest medicalTest);
    MedicalTest medicalTestDtoToMedicalTest (MedicalTestDto medicalTestDto);

}
