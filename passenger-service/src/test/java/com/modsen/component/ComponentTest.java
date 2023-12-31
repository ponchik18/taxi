package com.modsen.component;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/passengers-service-component.feature", glue = "com.modsen.component")
public class ComponentTest {
}