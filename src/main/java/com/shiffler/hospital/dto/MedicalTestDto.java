package com.shiffler.hospital.dto;

import com.shiffler.hospital.entity.MedicalTestOrderStatusEnum;
import com.shiffler.hospital.entity.MedicalTestResultEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data //This is required for MapStruct to work properly
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestDto {

    private Long orderNumber;
    private String testCode;
    private MedicalTestOrderStatusEnum testStatus;
    private MedicalTestResultEnum medicalTestResultEnum;
}
