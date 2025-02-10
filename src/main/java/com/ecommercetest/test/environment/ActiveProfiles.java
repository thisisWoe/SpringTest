package com.ecommercetest.test.environment;

import java.util.Arrays;

public class ActiveProfiles {
    private static final String ACTIVE_PROFILES = initActiveProfiles();

    private static String initActiveProfiles() {
        // 1) Provo a leggere la property Java: -Dspring.profiles.active=dev,prod
        String profiles = System.getProperty("spring.profiles.active");

        // 2) Se non esiste, provo a leggere la variabile d'ambiente
        if (profiles == null) {
            profiles = System.getenv("SPRING_PROFILES_ACTIVE");
        }

        // 3) Se ancora null, metto un default
        if (profiles == null) {
            profiles = "default";
        }

        return profiles;
    }

    /**
     * Ritorna i profili attivi come array.
     */
    public static String[] getActiveProfiles() {
        return ACTIVE_PROFILES.split("\\s*,\\s*");
    }

    /**
     * Verifica se il profilo 'dev' è attivo.
     */
    public static boolean isDev() {
        return isActive("dev");
    }

    /**
     * Verifica se il profilo 'test' è attivo.
     */
    public static boolean isTest() {
        return isActive("test");
    }

    /**
     * Verifica se il profilo 'prod' è attivo.
     */
    public static boolean isProd() {
        return isActive("prod");
    }

    /**
     * Metodo generico per verificare se un profilo specifico è attivo.
     */
    private static boolean isActive(String profile) {
        return Arrays.asList(getActiveProfiles()).contains(profile);
    }
}