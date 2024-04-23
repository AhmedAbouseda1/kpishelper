package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VisitorsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Visitors getVisitorsSample1() {
        return new Visitors().id(1L).total_visitors(1).website_visitors(1);
    }

    public static Visitors getVisitorsSample2() {
        return new Visitors().id(2L).total_visitors(2).website_visitors(2);
    }

    public static Visitors getVisitorsRandomSampleGenerator() {
        return new Visitors()
            .id(longCount.incrementAndGet())
            .total_visitors(intCount.incrementAndGet())
            .website_visitors(intCount.incrementAndGet());
    }
}
