package com.browserstack.PercySample.VFCorp.stepdef;

//import com.browserstack.PercySDK;

import browserstack.shaded.org.json.JSONObject;
import com.browserstack.BrowserStackSdk;
import com.browserstack.PercySDK;
import com.browserstack.PercySample.VFCorp.PageObjects.DeviceResult;
import com.browserstack.PercySample.VFCorp.PageObjects.PageResult;
import com.browserstack.PercySample.VFCorp.utils.PercyJsonParser;
import com.browserstack.PercySample.VFCorp.utils.PercyReportGenerator;
import com.browserstack.PercySample.VFCorp.utils.PercySnapshotUtils;
import io.percy.selenium.Percy;

import com.browserstack.PercySample.VFCorp.utils.ExcelUtils;
import com.browserstack.accessibility.AccessibilityUtils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.*;

public class WebAppDemoSteps {

    Percy percy;
    private WebDriver driver;
    String productOnScreenText, productOnCartText;

    @Before
    public void setUp() throws MalformedURLException {
        MutableCapabilities capabilities = new MutableCapabilities();
        HashMap<String, String> bstackOptions = new HashMap<>();
        bstackOptions.putIfAbsent("source", "cucumber-java:appium-sample-main:v1.0");
        capabilities.setCapability("bstack:options", bstackOptions);

        driver = new RemoteWebDriver(
                new URL("https://hub.browserstack.com/wd/hub"), capabilities);
        percy = new Percy(driver);
    }


    @Given("I navigate to {string} website")
    public void I_navigate_to_bstackdemo_website(String url) throws Throwable {
        //driver.get("https://www.bstackdemo.com");
        driver.get(url);
        Assert.assertTrue(driver.getTitle().matches("StackDemo"));
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("fullPage", true);
        options.put("sync", true);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 80)");
        PercySnapshotUtils.takeSapshot(percy,"Home Page", options);
    }


    @Then("I add products to cart")
    public void I_add_products_to_cart() throws Throwable {
        productOnScreenText = driver.findElement(By.xpath("//*[@id=\"1\"]/p")).getText();
        driver.findElement(By.xpath("//*[@id=\"1\"]/div[4]")).click();


    }

    @And("I validate the if the cart is open")
    public void I_validate_the_if_the_cart_is_open() throws Throwable {
        Assert.assertTrue(driver.findElement(By.cssSelector(".float\\-cart__content")).isDisplayed());
            HashMap<String, Object> options = new HashMap<String, Object>();
            options.put("fullPage", true);
            options.put("sync", true);
            PercySnapshotUtils.takeSapshot(percy, "Cart Page", options);
    }

    @And("I validate the product added to cart")
    public void I_validate_the_product_added_to_cart() throws Throwable {
        productOnCartText = driver.findElement(By.xpath("//*[@id=\"__next\"]/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText();
        Assert.assertEquals(productOnScreenText, productOnCartText);
    }


    @Given("I navigate to {string} website and {string}")
    public void I_navigate_to_VFC_website(String brand, String env) throws InterruptedException {
        //driver.get("https://ecom_vf:i7fqsRTL1eCdwzSN@preprod3.vans.com/en-us?cms=production");
        ExcelUtils xlObj= new ExcelUtils();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(15));

        List<Map<String, String>> data = xlObj.readExcelData("src/test/resources/VFC-url-datasheet.xlsx", brand, env);
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("fullPage", true);
        options.put("sync", true);
        for(Map<String, String> item: data)
        {
            driver.get(item.get(env));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            //Commenting just for the demo. The Bot needs to be handled.
            //checkAndHandleCaptcha();
            Thread.sleep(5000);
            //PercySDK.screenshot(driver, brand+"_"+item.get("PAGES"), options);
        }

        Thread.sleep(5000);
    }

    public void checkAndHandleCaptcha()
    {
        try {
            List<WebElement> iframes = driver.findElements(By.id("px-captcha-modal"));
            if (iframes.isEmpty()) {
                System.out.println("Iframe not present in DOM.");
            } else {
                WebElement iframe = iframes.get(0);
                if (iframe.isDisplayed()) {
                    System.out.println("Iframe is present and visible.");
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

                    WebElement captcha = driver.findElement(By.id("px-captcha"));
                    wait.until(ExpectedConditions.visibilityOf(captcha));
                    if (captcha.isDisplayed()) {
                        List<WebElement> nestediframes = driver.findElements(By.xpath("//div[@id=\"px-captcha\"]//iframe[1]"));
                        WebElement nestedFrame = iframes.get(0);
                        if (nestedFrame.isDisplayed()) {
                            WebElement captchaHoldBtn = driver.findElement(By.xpath("//div[@dir='auto']/div[1]"));
                            wait.until(ExpectedConditions.visibilityOf(captchaHoldBtn));
                            Actions actions = new Actions(driver);
                            actions.clickAndHold(captchaHoldBtn)
                                    .pause(5000)  // hold for 5 seconds
                                    .release()
                                    .build()
                                    .perform();
                        }
                    }

                } else {
                    System.out.println("Iframe is present but not visible.");
                }
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    @Then("I take screenshot of the page")
    public void I_take_screenshot_of_the_page()
    {
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("fullPage", true);
        //PercySDK.screenshot(driver, "VFC Home Page", options);
    }

    @After
    public void teardown(Scenario scenario) throws Exception {
        System.out.println("TearDown called");
        if(driver!=null) {
            try {
                driver.quit();
            }finally {
                PercySnapshotUtils.finalizeSnapshots();
            }

        }

    }

}


/*WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        WebElement buyBtn = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector(".buy-btn")));
        buyBtn.click();
        WebElement loginBtn= wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-btn")));
        if(!loginBtn.isDisplayed()) {
            System.out.println("login button not present");
        }else {*/



/*capabilities.setCapability("browserName", "Chrome");
        bstackOptions.put("os", "Windows");
        bstackOptions.put("osVersion", "10");
        bstackOptions.put("browserVersion", "138.0");
        bstackOptions.put("userName", "venkateshraghuna_DNVo9K");
        bstackOptions.put("accessKey", "mZgpti9vW65cJPhmVPWZ");
        bstackOptions.put("consoleLogs", "info");
        capabilities.setCapability("bstack:options", bstackOptions);
        ChromeOptions options = new ChromeOptions();
        //options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.addArguments("--start-maximized", "--kiosk");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);*/


//Commenting local caps
        /*WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--disable-blink-features=AutomationControlled");
        //options.addArguments("--start-maximized", "--kiosk");
        options.addArguments("--window-size=1920,1100");
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);*/

