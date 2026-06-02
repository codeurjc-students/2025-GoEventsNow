package es.goeventsnow.backend.e2e;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class E2eTestBase {

    protected static final String BASE_URL = "http://localhost:4200";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected void navigateToHome() {
        navigateToPath("");
    }

    protected void navigateToPath(String path) {
        driver.get(BASE_URL + path);
    }

    protected WebElement waitForId(String id) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }

    protected WebElement waitForClickableId(String id) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
    }

    protected void waitForIdToDisappear(String id) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
    }

    protected void waitForPageText(String text) {
        wait.until(driver -> driver.getPageSource().contains(text));
    }

    protected void waitForSelectOption(String id, String visibleText) {
        wait.until(driver -> new Select(waitForId(id)).getOptions().stream()
                .anyMatch(option -> visibleText.equals(option.getDomProperty("textContent").trim())));
    }

    protected void clickId(String id) {
        click(waitForClickableId(id));
    }

    protected void clickCss(String selector) {
        click(wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector))));
    }

    protected void click(WebElement element) {
        scrollToCenter(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException ex) {
            jsClick(element);
        }
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void scrollToCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                element);
    }

    protected void type(String id, String value) {
        WebElement input = waitForId(id);
        input.clear();
        input.sendKeys(value);
    }

    protected void setInputValue(String id, String value) {
        WebElement input = waitForId(id);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                input,
                value);
    }

    protected void selectByVisibleText(String id, String visibleText) {
        new Select(waitForId(id)).selectByVisibleText(visibleText);
    }

    protected void loginAsAdmin() {
        navigateToHome();
        clickId("login-link");
        type("username", "admin");
        type("password", "adminpass");
        clickId("login-btn");
        waitForId("event-list");
    }

    protected void openUserMenu() {
        clickId("userMenu");
    }

    protected void openManageEventsFromMenu() {
        openUserMenu();
        clickId("manage-events-link");
        waitForId("event-manage-title-1");
    }

    protected void openManageParticipantsFromMenu() {
        openUserMenu();
        clickId("manage-participants-link");
        waitForId("participant-manage-name-1");
    }

    protected void loadMoreParticipants() {
        clickCss(".text-center.mt-5 button");
    }

    protected void fillEventForm(String title) {
        type("title", title);
        type("category", "Music");
        type("location", "Madrid");
        selectByVisibleText("participantIds", "Bad Bunny");
        type("date", "2026-06-20");
        type("time", "18:00");
        type("basicPrice", "30");
        type("vipPrice", "80");
        type("availableBasicTickets", "150");
        type("availableVipTickets", "30");
        type("description", "Description of the event");
    }

    protected void submitEventForm() {
        clickId("event-submit-btn");
        waitForId("event-list");
    }

    protected void fillParticipantForm(String name) {
        type("name", name);
        type("biography", "Participant biography");
        type("type", "Participant type");
    }

    protected void submitParticipantForm() {
        clickId("participant-submit-btn");
        waitForId("event-list");
    }

    protected void fillReviewForm(String description, String rating) {
        type("description", description);
        setInputValue("rating", rating);
    }   

    protected void submitReviewForm() {
        clickId("btn-send-review");
        waitForId("reviews-section");
    }
}
