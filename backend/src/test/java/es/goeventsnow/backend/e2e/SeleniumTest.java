package es.goeventsnow.backend.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.openqa.selenium.chrome.ChromeOptions;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class SeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp(){
        if (driver==null){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        }
    }

    @AfterEach
    void teardown(){
        if (driver!=null){
            driver.quit();
            driver = null;
        }
    }

    @Test
    void testSelenium() {
        driver.get("http://localhost:4200");

        wait.until(presenceOfElementLocated(By.id("event-list")));
        wait.until(presenceOfElementLocated(By.id("event-title-1")));
        wait.until(presenceOfElementLocated(By.id("event-title-3")));

        WebElement webElement1 = driver.findElement(By.id("event-title-1"));
        WebElement webElement2 = driver.findElement(By.id("event-title-3"));

        String title1= webElement1.getText();
        String title3 = webElement2.getText();


        assertEquals(title1,"Taller de Spring Boot 4.0");
        assertEquals(title3,"Torneo de Baloncesto");
    }
    
}
