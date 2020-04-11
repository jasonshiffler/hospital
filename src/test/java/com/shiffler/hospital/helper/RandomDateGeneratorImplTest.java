package com.shiffler.hospital.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class RandomDateGeneratorImplTest {

    RandomDateGenerator randomDateGenerator;

    @BeforeEach
    public void init(){
        randomDateGenerator = new RandomDateGeneratorImpl();
    }

    /**
     * Runs a test on the date generator. Creates many dates to run the test since we are dealing with randomized data
     * Verifies that all of the dates fall within a certain timeframe.
     */
    @Test
    public void generateDateBetween() {
        LocalDate beginDate = LocalDate.of(1909, 12, 31);
        LocalDate endDate = LocalDate.of(2002, 1, 1);

            IntStream.rangeClosed(0,10000000)
                    .parallel()
                    .forEach( (s) ->
                    {
                        LocalDate generatedDate = randomDateGenerator.
                        generateDateBetween(1910, 2001);
                        assertTrue(generatedDate.isAfter(beginDate) && generatedDate.isBefore(endDate),
                        generatedDate.toString());
                    });

        }

    }
