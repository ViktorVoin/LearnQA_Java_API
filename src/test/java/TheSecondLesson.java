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
            Response response = RestAssured
                    .given()
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
}