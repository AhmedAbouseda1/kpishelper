package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ActivitiesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Activities getActivitiesSample1() {
        return new Activities().id(1L).total_activities(1).total_participants(1);
    }

    public static Activities getActivitiesSample2() {
        return new Activities().id(2L).total_activities(2).total_participants(2);
    }

    public static Activities getActivitiesRandomSampleGenerator() {
        return new Activities()
            .id(longCount.incrementAndGet())
            .total_activities(intCount.incrementAndGet())
            .total_participants(intCount.incrementAndGet());
    }
}
