package com.modsen.end_to_end.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/passengers-end-to-end.feature", glue = "com.modsen.end_to_end.step.definitions")
public class EndToEndTest {
}