package com.github.pedrobacchini.infrastructure;

import com.github.pedrobacchini.application.UseCase;

public class Main {

    public static void main(String[] args) {
        System.out.println(new UseCase().execute());
    }
}
