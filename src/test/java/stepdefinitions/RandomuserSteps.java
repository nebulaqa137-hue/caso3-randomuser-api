package stepdefinitions;

import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import utils.ApiConfigManager;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class RandomuserSteps {

    private static final Logger log = LogManager.getLogger(RandomuserSteps.class);
    private static final ApiConfigManager config = ApiConfigManager.getInstance();

    private Response lastResponse;
    private Response seedResponse1;
    private Response seedResponse2;

    @Given("el cliente tiene configurado RestAssured con la URL base de Randomuser")
    public void clienteConfigurado() {
        log.info("RestAssured configurado desde Hooks");
    }

    // ---- GET BASICO ----
    @When("hace GET al endpoint raiz")
    public void getEndpointRaiz() {
        log.info("GET /");
        lastResponse = given().when().get("/").then().extract().response();
        log.info("Status: " + lastResponse.statusCode());
    }

    @Then("el status code debe ser {int}")
    public void validarStatusCode(int expectedStatus) {
        Assert.assertEquals(lastResponse.statusCode(), expectedStatus,
            "Status code esperado: " + expectedStatus);
        log.info("Status code OK: " + expectedStatus);
    }

    @Then("la respuesta debe contener al menos {int} usuario")
    public void respuestaContieneAlMenos(int cantidad) {
        List<?> results = lastResponse.jsonPath().getList("results");
        Assert.assertTrue(results.size() >= cantidad,
            "Debe haber al menos " + cantidad + " usuario(s)");
    }

    // ---- PARAMETRO RESULTS ----
    @When("hace GET con parametro results igual a {int}")
    public void getConResults(int cantidad) {
        log.info("GET ?results=" + cantidad);
        lastResponse = given()
            .queryParam("results", cantidad)
            .when().get("/").then().extract().response();
    }

    @Then("la respuesta debe contener exactamente {int} usuarios")
    public void exactamenteUsuarios(int cantidad) {
        List<?> results = lastResponse.jsonPath().getList("results");
        Assert.assertEquals(results.size(), cantidad,
            "Debe haber exactamente " + cantidad + " usuarios");
        log.info("Cantidad correcta: " + cantidad);
    }

    // ---- FILTRO GENERO ----
    @When("hace GET con parametro gender igual a {string}")
    public void getConGenero(String gender) {
        log.info("GET ?gender=" + gender);
        lastResponse = given()
            .queryParam("gender", gender)
            .queryParam("results", 3)
            .when().get("/").then().extract().response();
    }

    @Then("todos los usuarios deben tener genero {string}")
    public void validarGenero(String gender) {
        List<String> genders = lastResponse.jsonPath().getList("results.gender");
        for (String g : genders) {
            Assert.assertEquals(g, gender, "Genero incorrecto: " + g);
        }
        log.info("Todos los usuarios tienen genero: " + gender);
    }

    // ---- FILTRO NACIONALIDAD ----
    @When("hace GET con parametro nat igual a {string}")
    public void getConNat(String nat) {
        log.info("GET ?nat=" + nat);
        lastResponse = given()
            .queryParam("nat", nat)
            .queryParam("results", 3)
            .when().get("/").then().extract().response();
    }

    @Then("todos los usuarios deben tener nacionalidad {string}")
    public void validarNacionalidad(String nat) {
        List<String> nats = lastResponse.jsonPath().getList("results.nat");
        for (String n : nats) {
            Assert.assertEquals(n, nat, "Nacionalidad incorrecta: " + n);
        }
        log.info("Todos los usuarios tienen nat: " + nat);
    }

    // ---- SEED ----
    @When("hace GET dos veces con el mismo seed")
    public void getDosvecesConSeed() {
        String seed = config.getSeed();
        log.info("GET ?seed=" + seed + " (x2)");
        seedResponse1 = given().queryParam("seed", seed).when().get("/").then().extract().response();
        seedResponse2 = given().queryParam("seed", seed).when().get("/").then().extract().response();
    }

    @Then("ambas respuestas deben tener el mismo email")
    public void mismEmailConSeed() {
        String email1 = seedResponse1.jsonPath().getString("results[0].email");
        String email2 = seedResponse2.jsonPath().getString("results[0].email");
        log.info("Email 1: " + email1 + " | Email 2: " + email2);
        Assert.assertEquals(email1, email2, "Con el mismo seed el email debe ser identico");
    }

    // ---- INC/EXC ----
    @When("hace GET con parametro inc igual a {string}")
    public void getConInc(String inc) {
        log.info("GET ?inc=" + inc);
        lastResponse = given()
            .queryParam("inc", inc)
            .when().get("/").then().extract().response();
    }

    @Then("la respuesta debe contener los campos name email y phone")
    public void camposNameEmailPhone() {
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].name"), "name debe existir");
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].email"), "email debe existir");
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].phone"), "phone debe existir");
        log.info("Campos name, email, phone presentes");
    }

    @Then("la respuesta NO debe contener el campo gender")
    public void sinCampoGender() {
        Object gender = lastResponse.jsonPath().get("results[0].gender");
        Assert.assertNull(gender, "El campo gender NO debe estar presente cuando no se incluye en 'inc'");
        log.info("Campo gender ausente correctamente");
    }

    // ---- VALIDACION EMAIL ----
    @Then("todos los emails deben tener formato valido")
    public void validarFormatoEmail() {
        List<String> emails = lastResponse.jsonPath().getList("results.email");
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        for (String email : emails) {
            Assert.assertTrue(email.matches(regex), "Email con formato invalido: " + email);
            log.info("Email valido: " + email);
        }
    }

    // ---- UBICACION ----
    @Then("la respuesta debe contener campos de ubicacion")
    public void camposUbicacion() {
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].location.city"),    "city debe existir");
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].location.country"), "country debe existir");
        Assert.assertNotNull(lastResponse.jsonPath().get("results[0].location.state"),   "state debe existir");
        log.info("Campos de ubicacion presentes");
    }
}
