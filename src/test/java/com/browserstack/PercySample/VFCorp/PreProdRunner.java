package com.browserstack.PercySample.VFCorp;

import com.browserstack.PercySample.VFCorp.utils.PercyReportGenerator;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

@CucumberOptions(
        features = "src/test/resources/webtest.feature",
        plugin = {"json:src/test/resources/results.json"},
        glue = {"com.browserstack.PercySample.VFCorp.stepdef"},
        tags = "@BrowserStack-pre-prod")
public class PreProdRunner extends AbstractTestNGCucumberTests {

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        System.out.println(">>> Before Suite setup");
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        System.out.println(">>> After Suite cleanup");
        System.out.println("Finalize Results called");
        PercyReportGenerator.getInstance().summarize();
        PercyReportGenerator.getInstance().generateReport();
    }

}
