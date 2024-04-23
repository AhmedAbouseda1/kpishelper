package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PopulationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Population getPopulationSample1() {
        return new Population().id(1L).population(1).active_members(1);
    }

    public static Population getPopulationSample2() {
        return new Population().id(2L).population(2).active_members(2);
    }

    public static Population getPopulationRandomSampleGenerator() {
        return new Population()
            .id(longCount.incrementAndGet())
            .population(intCount.incrementAndGet())
            .active_members(intCount.incrementAndGet());
    }
}
