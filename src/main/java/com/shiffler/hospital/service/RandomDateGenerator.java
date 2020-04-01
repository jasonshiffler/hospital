package com.shiffler.hospital.service;

import java.time.LocalDate;

public interface RandomDateGenerator {

    public LocalDate generateDateBetween(Integer earliestYear, Integer latestYear);

}
