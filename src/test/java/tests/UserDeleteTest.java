package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserDeleteTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void userDelete2() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        Response responseDeleteRequest = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + 2,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));
        Assertions.assertResponseTextEquals(responseDeleteRequest, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        responseDeleteRequest.prettyPrint();
    }

    @Test
    public void userDeleteWithAuth() {
        Map<String, String> userData;
        userData = DataGenerator.getRegistrationData();
        userData.put("email", "test179@test.com");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestCreateUser("https://playground.learnqa.ru/api/user/", userData);
        String userId = BaseTestCase.getStringFromJson((responseCreateAuth), "id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "test179@test.com");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        responseGetAuth.prettyPrint();

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Response responseGetUser = apiCoreRequests
                .makeGetRequestn("https://playground.learnqa.ru/api/user/" + userId);
        responseGetUser.prettyPrint();
        Assertions.assertResponseTextEquals(responseGetUser, "User not found");
    }

    @Test
    public void userDeleteWithAnotherAuth() {
        Map<String, String> userData;
        userData = DataGenerator.getRegistrationData();
        userData.put("email", "test175@test.com");

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestCreateUser("https://playground.learnqa.ru/api/user/", userData);
        String userId = BaseTestCase.getStringFromJson((responseCreateAuth), "id");

        responseCreateAuth.prettyPrint();

        Map<String, String> authData = new HashMap<>();
        authData.put("email", "test175@test.com");
        authData.put("password", "123");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        responseGetAuth.prettyPrint();

        Response responseDeleteUser = apiCoreRequests
                .makeDeleteRequest("https://playground.learnqa.ru/api/user/" + 86943,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"));

        Response responseGetAuthTwice = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        responseGetAuthTwice.prettyPrint();
    }


}
