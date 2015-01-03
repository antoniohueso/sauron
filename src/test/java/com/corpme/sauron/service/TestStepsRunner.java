package com.corpme.sauron.service;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html: html-report" },
        dryRun = false,
        tags = {"@resumenRfcs"},
        glue = "com.corpme.sauron.service" )
public class TestStepsRunner {
}


