package com.shiffler.hospital.bootstrap;

import com.shiffler.hospital.entity.Patient;
import com.shiffler.hospital.helper.PatientGenerator;
import com.shiffler.hospital.helper.PatientTester;
import com.shiffler.hospital.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoadNames implements CommandLineRunner {

       PatientGenerator patientGenerator;
       PatientService patientService;
       PatientTester patientTester;

       @Autowired
       public LoadNames(PatientGenerator patientGenerator, PatientService patientService, PatientTester patientTester){
           this.patientGenerator = patientGenerator;
           this.patientService = patientService;
           this.patientTester = patientTester;
       }

    @Override
    public void run(String... args) throws Exception {

        Patient patient;

        log.info("**************** Starting Hospital Application ***************");

           while(true) {
               patient = patientGenerator.randomPatientGenerator();
               log.info("****** Generating new patient ******");
               log.info(patient.toString());
               patientService.savePatient(patient);

               log.info("***** Assigning Tests to patients *****");

               assignTests();
               Thread.sleep(10000);

           }
   }


    /**
     * Look through the whole list of patients and randomly assign medical tests
     *
     */
   private void assignTests(){
       Iterable<Patient> patients = patientService.getAllPatients();
           for (Patient patient: patients){
               patientTester.assignTests(patient);
           }

   }

}
