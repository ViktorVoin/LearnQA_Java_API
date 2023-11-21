import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.cookie.Cookie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TheThirdLesson {
    @Test
    public void assertLengthOfString() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn().jsonPath();
        String answer = response.getJsonObject("messages[1]").toString();
        assertTrue(answer.length() > 15,"expected length of String 15 or more");
        System.out.println(answer);
    }
    @Test
    public void assertCookie() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        String cookie = response.getCookie("HomeWork");
        assertTrue(cookie.equals("hw_value"), "cookie is not valid");
    }
    @Test
    public void assertHeader() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        String header = response.getHeader("x-secret-homework-header");
        assertTrue(header.equals("Some secret value"), "header is not valid");
    }
}
