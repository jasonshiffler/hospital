package com.shiffler.hostpital.helper;

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
public class NameGeneratorImpl implements NameGenerator {

   private List<String> maleFirstNameList = new ArrayList<>();
   private List<String> femaleFirstNameList = new ArrayList<>();
   private List<String> lastNameList = new ArrayList<>();
   private List<String> middleInitialList = new ArrayList<>();

    //These Strings specify the source of the names.

    @Value("${male.firstname.srcfile}")
    String maleFirstNameSource;

    @Value("${female.firstname.srcfile}")
    String femaleFirstNameSource;

    @Value("${lastname.srcfile}")
    String lastNameSource;

    @Value("${middleinitial.srcfile}")
    String middleInitialSource;


    @Override
    public String generateMaleFirstName() {
        return maleFirstNameList.get(new Random().nextInt(maleFirstNameList.size()));
    }

    @Override
    public String generateFemaleFirstName() {
        return femaleFirstNameList.get(new Random().nextInt(femaleFirstNameList.size()));
    }

    @Override
    public String generateLastName() {
        return lastNameList.get(new Random().nextInt(lastNameList.size()));
    }

    @Override
    public String generateMiddleInitial() {
        return middleInitialList.get(new Random().nextInt(middleInitialList.size()));
    }

    /**
     * Populate the name lists so they can be used by the name generator methods.
     * @throws IOException
     */
    @PostConstruct
    public void init() throws IOException {


        createNameList(maleFirstNameSource)
                .stream().parallel()
                .forEach(s -> maleFirstNameList.add(s));

        createNameList(femaleFirstNameSource)
                .stream().parallel()
                .forEach(s -> femaleFirstNameList.add(s));

        createNameList(lastNameSource)
                .stream().parallel()
                .forEach(s -> lastNameList.add(s));

        createNameList(middleInitialSource)
                .stream().parallel()
                .forEach(s -> middleInitialList.add(s));
    }

    /**
     * Creates a List of Strings based on data read from a file.
     * Data is normalized so that the first letter in the string is capitalized and
     * and subequent letters are lowercase
     * @param filename - The file that is the source of the names
     * @throws IOException -
     */
    private List<String> createNameList(String filename) throws IOException {

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



}
