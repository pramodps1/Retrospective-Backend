package com.sports.info.interview;

import org.mockito.ArgumentMatcher;

import java.util.Date;

public class DateMatcher implements ArgumentMatcher<Date> {

    private final Date expectedDate;

    public DateMatcher(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    @Override
    public boolean matches(Date actualDate) {
        // Customize the comparison logic based on your requirements
        return actualDate.toInstant().equals(expectedDate.toInstant());
    }
}