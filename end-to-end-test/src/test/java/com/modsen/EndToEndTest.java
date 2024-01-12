package com.modsen;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/end-to-end-test.feature", glue = "com.modsen")
public class EndToEndTest {
}