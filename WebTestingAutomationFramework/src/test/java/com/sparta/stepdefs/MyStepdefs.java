package com.sparta.stepdefs;

import com.sparta.pom.pages.CartPage;
import com.sparta.pom.pages.HomePage;
import com.sparta.pom.pages.ProductsPage;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

public class MyStepdefs {
    private static final String DRIVER_LOCATION = "src/test/resources/chromedriver.exe";
    private static ChromeDriverService service;
    private static WebDriver webDriver;
    private HomePage homePage;
    private ProductsPage productsPage;
    private CartPage cartPage;

    public static ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");
        return chromeOptions;
    }


    @BeforeAll
    public static void beforeAll() throws IOException {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(DRIVER_LOCATION))
                .usingAnyFreePort()
                .build();
        service.start();

    }

    @Before
    public void setup() {
        webDriver = new ChromeDriver(service, getChromeOptions());
    }

    @Given("I am on the automationexercise website homepage")
    public void iAmOnTheAutomationexerciseWebsiteHomepage() {
        homePage = new HomePage(webDriver);
    }

    @When("I click products link")
    public void iClickProductsLink() {
        productsPage = homePage.goToProductsPage();
    }

    @Then("I go to products page")
    public void iGoToProductsPage() {
        Assertions.assertEquals("https://automationexercise.com/products", productsPage.getUrl());
    }

    @Given("I am on the products page")
    public void iAmOnTheProductsPage() {
        productsPage = new HomePage(webDriver).goToProductsPage();
    }
    @When("I type in {string}")
    public void iTypeIn(String searchProduct) {
        productsPage.enterSearchedProduct(searchProduct);
    }

    @And("I click search")
    public void iClickSearch() {
        productsPage.clickSearch();
    }

    @Then("I can see word {string} in the search box")
    public void iCanSeeWordInTheSearchBox(String searchedValue) {
        Assertions.assertEquals(searchedValue, productsPage.getSearchProductValue());
    }

    @Then("I am redirected to the page with searched products")
    public void iAmRedirectedToThePageWithSearchedProducts() {
        Assertions.assertEquals("https://automationexercise.com/products?search=women", productsPage.getUrl());
    }

    @When("I find the product I want")
    public void iFindTheProductIWant() {
        cartPage = new CartPage(webDriver);
        cartPage.chooseProduct();
    }

    @And("I click on the add to cart button")
    public void iClickOnTheAddToCartButton() {
        cartPage.clickAddToCart();
    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String message) {
        Assertions.assertTrue(cartPage.hasMessage(message));
    }




    @When("I click on the cart icon")
    public void iClickOnTheCartIcon() {
        homePage = new HomePage(webDriver);
        cartPage = homePage.gotToCartPage();
    }

    @Then("I should be able to see a summary of the items in my cart")
    public void iShouldBeAbleToSeeASummaryOfTheItemsInMyCart() {
        Assertions.assertEquals("https://automationexercise.com/view_cart", cartPage.getUrl());
    }

    @Given("I am on the cart page")
    public void iAmOnTheCartPage() {
        cartPage = homePage.gotToCartPage();
    }

    @When("I click on the X button")
    public void iClickOnTheXButton() {
        cartPage.removeItem();
    }


    @Then("I will see the message {string}")
    public void iWillSeeTheMessage(String message) {
        Assertions.assertTrue(cartPage.confirmRemove(message));
    }

    @After
    void tearDown() {
        webDriver.close();
    }

    @AfterAll
    static void teardownAll() {
        service.stop();
    }
}
