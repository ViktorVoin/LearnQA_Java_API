import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
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
}
