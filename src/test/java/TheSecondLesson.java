import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

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
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false).log().uri()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        String redirect = response.header("location").toString();
        System.out.println(redirect);
    }

    @Test
    public void getLongRedirectAddress() {
        int statusCode = 0;
        int countRedirect = 0;
        while (statusCode != 200) {
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(true)
                    .get("https://playground.learnqa.ru/api/long_redirect")
                    .andReturn();
            statusCode = response.statusCode();
            countRedirect++;
        }
        System.out.println(countRedirect);
    }
}