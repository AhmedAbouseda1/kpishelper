package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SpaceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Space getSpaceSample1() {
        return new Space().id(1L).square_meters_available(1L);
    }

    public static Space getSpaceSample2() {
        return new Space().id(2L).square_meters_available(2L);
    }

    public static Space getSpaceRandomSampleGenerator() {
        return new Space().id(longCount.incrementAndGet()).square_meters_available(longCount.incrementAndGet());
    }
}
