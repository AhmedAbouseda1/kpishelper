package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StaffTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Staff getStaffSample1() {
        return new Staff().id(1L).number_of_staff(1);
    }

    public static Staff getStaffSample2() {
        return new Staff().id(2L).number_of_staff(2);
    }

    public static Staff getStaffRandomSampleGenerator() {
        return new Staff().id(longCount.incrementAndGet()).number_of_staff(intCount.incrementAndGet());
    }
}
