package com.shiffler.hospital.bootstrap;

import com.shiffler.hospital.entity.MedicalTest;
import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class LoadNames implements CommandLineRunner {


    private final PatientService patientService;
    private final MedicalTestService medicalTestService;

       @Autowired
       public LoadNames(PatientService patientService,
                        MedicalTestService medicalTestService){

           this.patientService = patientService;
           this.medicalTestService = medicalTestService;
       }

    @Override
    public void run(String... args) throws Exception {

        Patient patient;

        log.info("**************** Starting Hospital Application ***************");

           while(true) {
               patient =patientService.generateRandomPatient();
               log.info("****** Generating new patient ******");
               log.info(patient.toString());
               patientService.savePatient(patient);

               log.info("***** Assigning Tests to patients *****");
               assignTests();

               log.info("***** Ordering Medical Tests *****");
               medicalTestService.orderUnSubmittedTests();
               medicalTestService.updateStatusForPendingTests();
               Thread.sleep(10000);



           }
   }


    /**
     * Look through all of the patients in the repository and randomly assign medical tests
     *
     */
   private void assignTests(){
       Iterable<Patient> patients = patientService.getAllPatients();
           for (Patient patient: patients){
               patientService.assignRandomTestToPatient(patient);
           }
   }

}
