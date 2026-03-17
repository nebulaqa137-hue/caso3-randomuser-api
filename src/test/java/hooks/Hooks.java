package hooks;

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ApiConfigManager;

/**
 * Hooks API - Configura RestAssured antes de cada escenario
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);
    private static final ApiConfigManager config = ApiConfigManager.getInstance();

    @Before
    public void setUp(Scenario scenario) {
        log.info("========================================");
        log.info("INICIO API TEST: " + scenario.getName());
        log.info("========================================");
        RestAssured.baseURI = config.getBaseUrl();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        log.info("RestAssured configurado. Base URI: " + config.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("FIN: " + scenario.getName() + " | " + scenario.getStatus());
        RestAssured.reset();
    }
}
