package com.shiffler.hospital.dto;

import com.shiffler.hospital.entity.MedicalTestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalTestDto {

    private Long orderNumber;
    private Integer version;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime lastModifiedDateTime;
    private String testCode;
    private MedicalTestStatusEnum testStatus;
    private Boolean isPositive;
    private UUID referenceUUID;
}
