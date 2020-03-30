/*
This class manages tests for Patients
 */

package com.shiffler.hospital.helper;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@Slf4j
public class PatientTesterImpl implements PatientTester {


    @Value("${test.probability}")
    float testProbability; // the probability that a patient will need a test. should be between 0 and 1

    @Value("${medtests.srcfile}")
    String testSource; //The path to the file that holds the possible tests

    private List<MedicalTest> medicalTestList = new ArrayList<>();

    PatientService patientService;

    @Autowired
    public PatientTesterImpl(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Takes a  patients and randomly determines for each one if they need a medical test.
     *
     * @param patientList - A List of patients
     */
    @Override
    public void assignTests(Patient patient) {

          if (Math.random() < this.testProbability) {
             patientService.addMedicalTest(patient, generateRandomMedicalTest());
          }

    } //close method

    /**
     * Creates a random Medical test
     *
     * @return
     */
    public MedicalTest generateRandomMedicalTest() {
        return medicalTestList.get(new Random().nextInt(medicalTestList.size()));

    }

    /**
     * Populates the MedicalTestList at startup.
     *
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {

        log.info("*** Generating Medical Tests ***");

        this.createMedicalTestList(this.testSource);

        log.info("*** Medical Tests Generated ***");

        medicalTestList.stream().forEach(s -> log.info(s.toString()));
    }


    /**
     * Create a list of all the medical tests that are available
     *
     * @param filename - The file that contains the list of available tests
     * @return
     * @throws IOException
     */
    private void createMedicalTestList(String filename) throws IOException {

        String line;
        List<String> nameList = new ArrayList<>();
        String[] values = new String[2];
        String testName;
        String testCode;

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while ((line = reader.readLine()) != null) {

            values = line.split(",");
            log.info("Test is {}", line);

            testCode = values[0];
            testName = values[1];

            this.medicalTestList.add(
                    MedicalTest.builder().testCode(testCode).testName(testName).isPositive(null).build()
            );

        }
    }


}
