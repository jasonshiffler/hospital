/*
This Class enables orders for Medical Tests to be placed.
 */

package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.MedicalTestResultEnum;
import com.shiffler.hospital.mappers.MedicalTestMapper;
import com.shiffler.hospital.repository.MedicalTestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class OrderMedicalTestServiceImpl implements OrderMedicalTestService {

    //The hostname of the Medical Testing Center
    @Value("${api.host}")
    private String apiHost;

    private final String MEDICAL_TEST_PATH ="/api/v1/medicaltestorder";
    private final RestTemplate restTemplate;
    private final MedicalTestMapper medicalTestMapper;


    @Autowired
    public OrderMedicalTestServiceImpl(String apiHost, RestTemplateBuilder restTemplateBuilder,
                                       MedicalTestRepository medicalTestRepository, MedicalTestMapper medicalTestMapper) {
        this.apiHost = apiHost;
        this.restTemplate = restTemplateBuilder.build();
        this.medicalTestMapper = medicalTestMapper;
    }

    /**
     * Orders a medical test from the Medical Test provider
     * @param medicalTest - The medical test we need to order
     */
    public void orderMedicalTest(MedicalTest medicalTest) {

        URI uri = null;

        MedicalTestDto medicalTestDto = medicalTestMapper.medicalTestToMedicalTestDto(medicalTest);

        try {
            uri = restTemplate.postForLocation(this.apiHost + MEDICAL_TEST_PATH, medicalTestDto);
            log.info(uri.toString());
        } catch (Exception e){
            log.error("Error creating API order for Medical Test: " + e.getMessage() + medicalTestDto.toString() );
        }
        finally {
            if (uri != null &&  uri.toString().contains(apiHost+MEDICAL_TEST_PATH)){
                medicalTestDto.setMedicalTestResultEnum(MedicalTestResultEnum.WAITING_FOR_RESULT);
            }

        }



    }


}
