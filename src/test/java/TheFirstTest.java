import io.restassured.response.Response;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

public class TheFirstTest {
    @Test
    public void FirstTest(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();
        response.prettyPrint();
    }
}
