package com.shiffler.hostpital.bootstrap;

import com.shiffler.hostpital.entity.Patient;
import com.shiffler.hostpital.helper.NameGenerator;
import com.shiffler.hostpital.helper.PatientGenerator;
import com.shiffler.hostpital.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LoadNames implements CommandLineRunner {

       PatientGenerator patientGenerator;
       PatientService patientService;

       @Autowired
       public LoadNames(PatientGenerator patientGenerator, PatientService patientService){
           this.patientGenerator = patientGenerator;
           this.patientService = patientService;
       }

    @Override
    public void run(String... args) throws Exception {

        Patient patient;

        log.info("**************** Starting Hospital Application ***************");

           while(true) {
               patient = patientGenerator.randomPatientGenerator();
               log.info(patient.toString());
               patientService.savePatient(patient);
               Thread.sleep(20000);

           }
   }





}
