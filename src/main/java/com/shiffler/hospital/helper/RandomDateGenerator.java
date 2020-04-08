package com.shiffler.hospital.helper;

import java.time.LocalDate;

public interface RandomDateGenerator {

    public LocalDate generateDateBetween(Integer earliestYear, Integer latestYear);

}
