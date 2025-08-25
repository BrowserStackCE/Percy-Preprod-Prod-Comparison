package com.browserstack.PercySample.VFCorp;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/webtest.feature",
        plugin = {"json:src/test/resources/results.json"},
        glue = {"com.browserstack.PercySample.VFCorp.stepdef"})
public class ManualMode extends AbstractTestNGCucumberTests {
}
