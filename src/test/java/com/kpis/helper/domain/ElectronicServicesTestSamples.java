package com.kpis.helper.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ElectronicServicesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ElectronicServices getElectronicServicesSample1() {
        return new ElectronicServices().id(1L).total_pcs_with_internet(1).pcs_with_internet_for_clients_only(1);
    }

    public static ElectronicServices getElectronicServicesSample2() {
        return new ElectronicServices().id(2L).total_pcs_with_internet(2).pcs_with_internet_for_clients_only(2);
    }

    public static ElectronicServices getElectronicServicesRandomSampleGenerator() {
        return new ElectronicServices()
            .id(longCount.incrementAndGet())
            .total_pcs_with_internet(intCount.incrementAndGet())
            .pcs_with_internet_for_clients_only(intCount.incrementAndGet());
    }
}
