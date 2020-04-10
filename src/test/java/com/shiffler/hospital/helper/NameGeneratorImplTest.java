/*
Tests the NameGeneratorImpl class
 */

package com.shiffler.hospital.helper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;

//Set the properties file we want to use
@TestPropertySource(locations="classpath:test.properties")

//We only want to load the NameGeneratorImpl Class
@SpringBootTest(classes={NameGeneratorImpl.class})
class NameGeneratorImplTest {

    @Autowired
    private NameGeneratorImpl nameGenerator;

    @Test
    void generateMaleFirstName() {
       assertEquals("Liam", nameGenerator.generateMaleFirstName());
    }

    @Test
    void generateFemaleFirstName() {
        assertEquals("Emma", nameGenerator.generateFemaleFirstName());
    }

    @Test
    void generateLastName() {
        assertEquals("Smith", nameGenerator.generateLastName());
    }

    @Test
    void generateMiddleInitial() {
        assertEquals("A", nameGenerator.generateMiddleInitial());
    }
}