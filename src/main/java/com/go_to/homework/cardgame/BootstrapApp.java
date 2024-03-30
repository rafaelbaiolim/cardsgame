package com.go_to.homework.cardgame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("development")
@ComponentScan
public class BootstrapApp implements CommandLineRunner {
    @Autowired
    private ApplicationContext ctx;

    @Override
    public void run(String... args) throws Exception {
        //to be implemented
    }
}
