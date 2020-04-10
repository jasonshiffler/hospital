/*
This DTO object presents only the fields that are needed for a particular MedicalTest as there is some
internal information that doesn't need to be exposed.
 */

package com.shiffler.hospital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    //The Medical Test Center refers to this as the id so we use Jackson to map the field name
    @JsonProperty("id")
    private Long orderNumber;

    private String testCode;

    //Allow for Object <--> JSON Serialization/Deserialization since the REST API uses a different name for this value
    @JsonProperty("testOrderStatus")
    private MedicalTestOrderStatusEnum testOrderStatusEnum;

    @JsonProperty("medicalTestResult")
    private MedicalTestResultEnum medicalTestResultEnum;
}
