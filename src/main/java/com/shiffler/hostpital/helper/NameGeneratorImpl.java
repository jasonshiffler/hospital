package com.shiffler.hostpital.helper;

import com.shiffler.hostpital.entity.FirstName;
import com.shiffler.hostpital.entity.LastName;
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

   private List<FirstName> firstNameList = new ArrayList<>();
   private List<LastName> lastNameList = new ArrayList<>();

    //These Strings specify the source of the names.

    @Value("${male.firstname.srcfile}")
    String maleFirstNameSource;

    @Value("${female.firstname.srcfile}")
    String femaleFirstNameSource;

    @Value("${lastname.srcfile}")
    String lastNameSource;


    @Override
    public String generateFirstName() {
        return firstNameList.get(new Random().nextInt(firstNameList.size())).getFirstName();
    }

    @Override
    public String generateLastName() {
        return lastNameList.get(new Random().nextInt(lastNameList.size())).getLastName();

    }

    @Override
    public String generateMiddleInitial() {
        return null;
    }


    @PostConstruct
    public void init() throws IOException {

        createNameList(maleFirstNameSource)
                .stream()
                .map(s -> FirstName.builder().firstName(s).isMaleName(true).build())
                .forEach(s -> firstNameList.add(s));

        createNameList(femaleFirstNameSource)
                .stream()
                .map(s -> FirstName.builder().firstName(s).isMaleName(false).build())
                .forEach(s -> firstNameList.add(s));

        createNameList(lastNameSource)
                .stream()
                .map(s -> LastName.builder().lastName(s).build())
                .forEach(s -> lastNameList.add(s));
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
