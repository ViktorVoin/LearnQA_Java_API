import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

public class TheSecondLesson {
    @Test
    public void getJsonMessage(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn().jsonPath();
        String answer = response.getJsonObject("messages[1]").toString();
        System.out.println(answer);
    }
}