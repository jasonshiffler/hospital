/*
Contains functionality for interacting with patient data
*/

package com.shiffler.hospital.service;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.helper.NameGenerator;
import com.shiffler.hospital.helper.RandomDateGenerator;
import com.shiffler.hospital.repository.PatientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final MedicalTestService medicalTestService;

    private final NameGenerator nameGenerator;
    private final RandomDateGenerator randomDateGenerator;

    @Value("${earliest.birthyear}")
    private int earliestYear;

    @Value("${latest.birthyear}")
    private int latestYear;

    @Value("${test.probability}")
    float testProbability; // the probability that a patient will need a test. should be between 0 and 1

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository,
                              MedicalTestService medicalTestService,
                              NameGenerator nameGenerator,
                              RandomDateGenerator randomDateGenerator) {

        this.patientRepository = patientRepository;
        this.medicalTestService = medicalTestService;
        this.nameGenerator = nameGenerator;
        this.randomDateGenerator = randomDateGenerator;
    }

    /**
     * Saves a patient to the repository
     * @param patient - The patient object that needs to be saved to the repository
     */
    @Override
    public void savePatient(Patient patient) {

        log.info("Saving patient {}", patient.toString());
        patientRepository.save(patient);

    }

    /**
     * Add a new medical test to an existing patient
     * @param patient - the patient that the test should be added to.
     * @param medicalTest - the medical test that will be added.
     */
    @Override
    public void addMedicalTestToPatient(Patient patient, MedicalTest medicalTest) {

        log.info("Adding Medical Test {} to patient {}", medicalTest.toString(), patient.toString());
        List<MedicalTest> test = patient.getMedicalTests();
        test.add(medicalTest);
        patientRepository.save(patient);
    }

    /**
     * Return a list of all of the patients in the repository
     * @return The List of all of the patients.
     */
    @Override
    public Iterable<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Takes a  patients and randomly determines for each one if they need a medical test.
     * @param patient - The patient that will be checked to see if a medical test is necessary
     */
    @Override
    public void assignRandomTestToPatient(Patient patient) {

        log.info("Random determination if Patient will be assigned a test {} " ,
                patient.getFirstName() + " " + patient.getLastName());
        if (Math.random() < this.testProbability) {
            addMedicalTestToPatient(patient, medicalTestService.generateRandomMedicalTest());
        }

    } //close method

    /**
     * Builds a Patient with a random name and a random birthday
     * @return
     */
    @Override
    public Patient generateRandomPatient() {

        //If its true generate a female patient otherwise generate a male

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
    } //close method
}
