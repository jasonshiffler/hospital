package com.shiffler.hostpital.bootstrap;

import com.shiffler.hostpital.entity.FirstName;
import com.shiffler.hostpital.entity.LastName;
import com.shiffler.hostpital.repository.FirstNameRepository;
import com.shiffler.hostpital.repository.LastNameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class LoadNames implements CommandLineRunner {

    //These Strings specify the source of the names.

    @Value("${male.firstname.srcfile}")
    String maleFirstNameSource;

    @Value("${female.firstname.srcfile}")
    String femaleFirstNameSource;

    @Value("${lastname.srcfile}")
    String lastNameSource;

    FirstNameRepository firstNameRepository;
    LastNameRepository lastNameRepository;

    @Autowired
    public LoadNames(FirstNameRepository firstNameRepository,
                     LastNameRepository lastNameRepository) {

        this.firstNameRepository = firstNameRepository;
        this.lastNameRepository = lastNameRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        this.clearTables();
        this.populateFirstNameTables(maleFirstNameSource,true);
        this.populateFirstNameTables(femaleFirstNameSource,false);
        this.populateLastNameTable(lastNameSource);
    }

    /**
     * Poplulates Firstnames in the FirstName Table
     * @param srcFilePath - The path of the source file with the first name data to load
     * @param isMaleName - Is this a male name or not
     * @throws IOException
     */
    private void populateFirstNameTables(String srcFilePath, boolean isMaleName) throws IOException {

        List<FirstName> firstNameList = new ArrayList<>();

        createNameListTable(srcFilePath)
                .stream()
                .map(s -> FirstName.builder().firstName(s).isMaleName(isMaleName).build())
                .forEach(s -> firstNameList.add(s));
        firstNameRepository.saveAll(firstNameList);
    }

    /**
     * Poplulates Lastnames in the LastName Table
     * @param srcFilePath  The path of the source file with the first name data to load
     * @throws IOException
     */
    private void populateLastNameTable(String srcFilePath) throws IOException {

        List<LastName> lastNameList = new ArrayList<>();

        createNameListTable(srcFilePath)
                .stream()
                .map(s -> LastName.builder().lastName(s).build())
                .forEach(s -> lastNameList.add(s));
        lastNameRepository.saveAll(lastNameList);
    }

    /**
     * Creates a List of Strings based on data read from a file.
     * Data is normalized so that the first letter in the string is capitalized and
     * and subequent letters are lowercase
     * @param filename - The file that is the source of the names
     * @throws IOException -
     */
    private List<String> createNameListTable(String filename) throws IOException {

        String line;
        List<String> nameList = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        while ((line = reader.readLine()) != null) {

            //Adjust Capitalization so the first name is always Bob and not bob or BOB or bOB
            line = line.toLowerCase().trim();
            line = line.substring(0, 1).toUpperCase() + line.substring(1).toLowerCase();
            nameList.add(line);
        }
        return nameList;
    }

    /**
     * Delete all entries in the name tables
     */
    private void clearTables() {
        firstNameRepository.deleteAll();
    }

}
