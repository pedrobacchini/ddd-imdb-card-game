package com.github.pedrobacchini.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.AbstractEnvironment;

import static org.wildfly.common.Assert.assertNotNull;

public class MainTest {

    @Test
    void testMain() {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "test");
        assertNotNull(new Main());
        Main.main(new String[]{});
    }

}
