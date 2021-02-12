package test.missionMarsTests.cucumber;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import static io.cucumber.testng.CucumberOptions.SnippetType.CAMELCASE;


@CucumberOptions(snippets = CAMELCASE,
        features = "src/main/resources/features",
        glue = "main.java.cucumber.stepDefinitions",
        plugin = {"pretty", "json:target/cucumber/cucumber.json",
                "html:target/cucumber/cucumber-reports.html"})
public class CucumberTask2TestRunner extends AbstractTestNGCucumberTests {
}
