package com.shiffler.hostpital.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BirthdayGeneratorImplTest {

    BirthdayGenerator birthdayGenerator;

    @BeforeEach
    public void init(){
        birthdayGenerator = new BirthdayGeneratorImpl();
    }

    @Test
    public void generateDateOfBirth() {
        LocalDate beginDate = LocalDate.of(1909, 12, 31);
        LocalDate endDate = LocalDate.of(2002, 1, 1);

        for(int i = 0; i < 10000000; i++) {
            LocalDate generatedDate = birthdayGenerator.generateDateOfBirth(1910, 2001);
            assertTrue(generatedDate.isAfter(beginDate) && generatedDate.isBefore(endDate),
                    generatedDate.toString());
        }

    }
}