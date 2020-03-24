package com.shiffler.hostpital.helper;

import java.time.LocalDate;

public interface BirthdayGenerator {

    public LocalDate generateDateOfBirth(Integer earliestYear, Integer latestYear);

}
