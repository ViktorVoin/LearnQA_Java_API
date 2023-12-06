package tests;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserGetTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Features(value = {@Feature(value = "User Data")})
    @DisplayName("Something print in console")
    @Description("Get userData with not auth")
    @Step("Starting test 'ID some Number testGetUserDataNotAuth'")
    @Severity(value = SeverityLevel.MINOR)
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

    @Test
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);


        int userId = BaseTestCase.getIntFromJson(responseGetAuth, "user_id");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");


        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);
        responseUserData.prettyPrint();

        String[] expectedFields = {"email", "firstName", "lastName"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);
    }

    @Test
    public void getAnotherUserProfile() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "test12346@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        int userId = BaseTestCase.getIntFromJson(responseGetAuth, "user_id");
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseAuthUser = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + 2, header, cookie);

        String[] notExpectedFields = {"email", "firstName", "lastName", "id"};
        Assertions.assertJsonNotHasFields(responseAuthUser, notExpectedFields);
    }
}
