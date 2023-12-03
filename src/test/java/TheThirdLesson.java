import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TheThirdLesson {
    @Test
    public void assertLengthOfString() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn().jsonPath();
        String answer = response.getJsonObject("messages[1]").toString();
        assertTrue(answer.length() > 15, "expected length of String 15 or more");
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


    @ParameterizedTest
    @ValueSource(strings = {"android", "ios", "google", "windows", "iphone"})
    public void userAgentTest(String agent) {
        Map<String, String> dataTest = new HashMap<>();
        if (Objects.equals(agent, "android")) {
            dataTest.put("user-agent", "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) " +
                    "AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
            dataTest.put("platform", "Mobile");
            dataTest.put("browser", "No");
            dataTest.put("device", "Android");
        } else if (Objects.equals(agent, "ios")) {
            dataTest.put("user-agent", "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 " +
                    "(KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1");
            dataTest.put("platform", "Mobile");
            dataTest.put("browser", "Chrome");
            dataTest.put("device", "iOS");
        } else if (Objects.equals(agent, "google")) {
            dataTest.put("user-agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
            dataTest.put("platform", "Googlebot");
            dataTest.put("browser", "Unknown");
            dataTest.put("device", "Unknown");
        } else if (Objects.equals(agent, "windows")) {
            dataTest.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0");
            dataTest.put("platform", "Web");
            dataTest.put("browser", "Chrome");
            dataTest.put("device", "No");
        } else if (Objects.equals(agent, "iphone")) {
            dataTest.put("user-agent", "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) " +
                    "AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1");
            dataTest.put("platform", "Mobile");
            dataTest.put("browser", "No");
            dataTest.put("device", "iPhone");
        }
        Response response = RestAssured
                .given()
                .headers("user-agent", dataTest.get("user-agent"))
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();
        String expectedPlatform = response.jsonPath().get("platform").toString();
        String expectedBrowser = response.jsonPath().get("browser").toString();
        String expectedDevice = response.jsonPath().get("device").toString();
        assertTrue(expectedPlatform.equals(dataTest.get("platform")), "platform name not valid in OS: " + agent);
        assertTrue(expectedBrowser.equals(dataTest.get("browser")), "browser name not valid in OS: " + agent);
        assertTrue(expectedDevice.equals(dataTest.get("device")), "device name not valid in OS: " + agent);
    }

}
