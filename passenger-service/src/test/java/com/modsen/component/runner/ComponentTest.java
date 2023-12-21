package com.modsen.component.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/passengers.feature", glue = "com.modsen.component.step.definitions")
public class ComponentTest {
}