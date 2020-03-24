package com.shiffler.hostpital.helper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class BirthdayGeneratorImpl implements BirthdayGenerator {

    /**
     * Returns a random date of birth between the specified years
     * @param earliestYear The earliest year the birthday can be in. i.e. 1900
     * @param latestYear  The latest year the birthday can be in. i.e. 1900
     * @return a random LocalDate
     *
     * https://stackoverflow.com/questions/3985392/generate-random-date-of-birth
     */

    public LocalDate generateDateOfBirth(Integer earliestYear, Integer latestYear){
        Random random = new Random();
        int minDay = (int) LocalDate.of(earliestYear, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(latestYear, 1, 1).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        return LocalDate.ofEpochDay(randomDay);
    }




}