package com.shiffler.hostpital.helper;

import java.time.LocalDate;

public interface RandomDateGenerator {

    public LocalDate generateDateBetween(Integer earliestYear, Integer latestYear);

}
