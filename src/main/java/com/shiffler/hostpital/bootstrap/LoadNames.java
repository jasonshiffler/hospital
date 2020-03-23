package com.shiffler.hostpital.bootstrap;

import com.shiffler.hostpital.helper.NameGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class LoadNames implements CommandLineRunner {

       NameGenerator nameGenerator;

       @Autowired
       public LoadNames(NameGenerator nameGenerator){
           this.nameGenerator = nameGenerator;
       }


    @Override
    public void run(String... args) throws Exception {

           while(true) {
               log.info(nameGenerator.generateMaleFirstName() + " " + nameGenerator.generateLastName());
               log.info(nameGenerator.generateFemaleFirstName() + " " + nameGenerator.generateLastName());
           }
   }





}
