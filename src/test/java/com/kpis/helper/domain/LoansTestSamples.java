package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LoansTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Loans getLoansSample1() {
        return new Loans().id(1L).total_items_borrowed(1).turnover_rate(1L).media_borrowed_at_least_once_percentage(1);
    }

    public static Loans getLoansSample2() {
        return new Loans().id(2L).total_items_borrowed(2).turnover_rate(2L).media_borrowed_at_least_once_percentage(2);
    }

    public static Loans getLoansRandomSampleGenerator() {
        return new Loans()
            .id(longCount.incrementAndGet())
            .total_items_borrowed(intCount.incrementAndGet())
            .turnover_rate(longCount.incrementAndGet())
            .media_borrowed_at_least_once_percentage(intCount.incrementAndGet());
    }
}
