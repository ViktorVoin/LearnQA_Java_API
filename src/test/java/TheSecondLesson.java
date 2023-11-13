import groovy.json.JsonToken;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TheSecondLesson {
    @Test
    public void getJsonMessage() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn().jsonPath();
        String answer = response.getJsonObject("messages[1]").toString();
        System.out.println(answer);
    }

    @Test
    public void getRedirectAddress() {
        Response response = given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String redirect = response.header("location").toString();
        System.out.println(redirect);
    }

    @Test
    public void getLongRedirectAddress() {
        int statusCode;
        int countRedirect = 0;
        String redirectUrl = "https://playground.learnqa.ru/api/long_redirect";
        do {
            Response response = given()
                    .redirects()
                    .follow(false)
                    .get(redirectUrl)
                    .andReturn();
            statusCode = response.statusCode();
            redirectUrl = response.header("location");
            countRedirect++;
        } while (statusCode != 200);
        System.out.println(countRedirect);
    }

    @Test
    public void tokenValidation() throws InterruptedException {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn();
        String token = response.jsonPath().get("token");
        int time = response.jsonPath().get("seconds");
        given()
                .params("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .assertThat()
                .body("status", equalTo("Job is NOT ready"));
        Thread.sleep(time * 1000);
        given()
                .params("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .then()
                .assertThat()
                .body("status", equalTo("Job is ready"), "result", notNullValue());
    }

    @Test
    public void brutTokenData() throws IOException {
        Scanner sc = new Scanner(new File("pass.txt"));
        String brut = null;
        String result = null;
        while (!Objects.equals(result, "You are authorized")) {
            brut = sc.nextLine();
            String getCookie = given()
                    .params("login", "super_admin")
                    .params("password", brut)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .getCookie("auth_cookie");
            Response authData = given()
                    .cookie("auth_cookie", getCookie)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie");
            result = authData.body().asString();
        }
        System.out.println("Pass: " + brut + "\n" + "Server response body: " + result);
    }
}