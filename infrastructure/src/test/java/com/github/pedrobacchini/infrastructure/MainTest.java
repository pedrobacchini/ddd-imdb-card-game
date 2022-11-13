package com.github.pedrobacchini.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testeMain() {
        assertNotNull(new Main());
        Main.main(new String[]{});
    }

}