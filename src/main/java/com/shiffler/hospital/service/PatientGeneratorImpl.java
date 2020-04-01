/*
This class is used to generate random patients so that there is test data available in the repository
 */

package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.Patient;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Setter
public class PatientGeneratorImpl implements PatientGenerator {

    private final NameGenerator nameGenerator;
    private final RandomDateGenerator randomDateGenerator;

    @Value("${earliest.birthyear}")
    private int earliestYear;

    @Value("${latest.birthyear}")
    private int latestYear;

    @Autowired
    public PatientGeneratorImpl(NameGenerator nameGenerator, RandomDateGenerator randomDateGenerator) {
        this.nameGenerator = nameGenerator;
        this.randomDateGenerator = randomDateGenerator;
    }

    /**
     * Builds a Patient with a random name and a random birthday
     * @return
     */
    @Override
    public Patient randomPatientGenerator() {

        if (new Random().nextBoolean()) {

            return Patient.builder().firstName(nameGenerator.generateFemaleFirstName())
                    .middleInitial(nameGenerator.generateMiddleInitial())
                    .lastName(nameGenerator.generateLastName())
                    .dateOfBirth(randomDateGenerator.generateDateBetween(earliestYear,latestYear))
                    .build();
        } else {
            return Patient.builder().firstName(nameGenerator.generateMaleFirstName())
                    .middleInitial(nameGenerator.generateMiddleInitial())
                    .lastName(nameGenerator.generateLastName())
                    .dateOfBirth(randomDateGenerator.generateDateBetween(earliestYear,latestYear))
                    .build();
        }
    }

}
