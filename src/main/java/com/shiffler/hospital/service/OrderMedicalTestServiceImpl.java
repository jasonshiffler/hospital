package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class OrderMedicalTestServiceImpl implements OrderMedicalTestService {


    @Value("${api.host}")
    private String apiHost;

    public final String MEDICAL_TEST_PATH ="/api/v1/medicaltestorder";
    private final RestTemplate restTemplate;


    @Autowired
    public OrderMedicalTestServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void orderMedicalTest(MedicalTest medicalTest) {
        MedicalTestDto medicalTestDto = MedicalTestDtoConverter.MedicalTestToMedicalTestDto(medicalTest);

        URI uri = restTemplate.postForLocation(this.apiHost + MEDICAL_TEST_PATH, medicalTestDto);


    }


}
