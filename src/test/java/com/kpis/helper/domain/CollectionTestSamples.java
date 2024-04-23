package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CollectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Collection getCollectionSample1() {
        return new Collection()
            .id(1L)
            .collection_size(1)
            .number_of_titles(1)
            .stock_for_public_usage(1)
            .titles_availability_for_population(1)
            .titles_availability_for_active_members(1);
    }

    public static Collection getCollectionSample2() {
        return new Collection()
            .id(2L)
            .collection_size(2)
            .number_of_titles(2)
            .stock_for_public_usage(2)
            .titles_availability_for_population(2)
            .titles_availability_for_active_members(2);
    }

    public static Collection getCollectionRandomSampleGenerator() {
        return new Collection()
            .id(longCount.incrementAndGet())
            .collection_size(intCount.incrementAndGet())
            .number_of_titles(intCount.incrementAndGet())
            .stock_for_public_usage(intCount.incrementAndGet())
            .titles_availability_for_population(intCount.incrementAndGet())
            .titles_availability_for_active_members(intCount.incrementAndGet());
    }
}
