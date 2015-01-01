package com.corpme.sauron.service;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty" },
        dryRun = false,
        glue = "com.corpme.sauron.service" )
public class TestStepsRunner {
}


