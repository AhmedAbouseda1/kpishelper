package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrainingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Training getTrainingSample1() {
        return new Training().id(1L).total_courses(1).total_participants(1);
    }

    public static Training getTrainingSample2() {
        return new Training().id(2L).total_courses(2).total_participants(2);
    }

    public static Training getTrainingRandomSampleGenerator() {
        return new Training()
            .id(longCount.incrementAndGet())
            .total_courses(intCount.incrementAndGet())
            .total_participants(intCount.incrementAndGet());
    }
}
