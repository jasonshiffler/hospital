package com.shiffler.hospital.service;

import com.shiffler.hospital.dto.MedicalTestDto;
import com.shiffler.hospital.entity.MedicalTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderMedicalTestServiceImplTest {

    @Autowired
    OrderMedicalTestService orderMedicalTestService;


}