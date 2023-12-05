package tests;

import io.restassured.path.json.JsonPath;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class UserEditTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testEditJustCreatedTest() {
        //User generated
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token", this.getHeader(responseGetAuth, "x-csrf-token"))
                .cookie("auth_sid", this.getCookie(responseGetAuth, "auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        Assertions.assertJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    public void editNotAuthUser() {
        String newName = "ChangedName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Map<String, String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequestCreateUser("https://playground.learnqa.ru/api/user/", userData);

        String userId = BaseTestCase.getStringFromJson(responseCreateAuth, "id");

        Response responseEditUserWithoutAuth = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId, editData);

        newName = "ChangedName123";
        Assertions.assertJsonByName(responseEditUserWithoutAuth, "firstName", newName);
        responseEditUserWithoutAuth.prettyPrint();
    }

    @Test
    public void editAuthWithAnotherUser() {
        //LOGIN
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "test12346@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        //EDIT
        String newName = "ChangedName123";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + 2,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        //LOGIN WITH CORRECT USER
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", "vinkotov@example.com");
        authData1.put("password", "1234");

        Response responseGetAuth1 = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);

        String userId1 = BaseTestCase.getStringFromJson(responseGetAuth, "user_id");
        //CHECK NAME
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId1,
                        this.getHeader(responseGetAuth1, "x-csrf-token"),
                        this.getCookie(responseGetAuth1, "auth_sid"));

        Assertions.assertJsonByNameNotEqual(responseUserData, "firstName", newName);
    }

    @Test
    public void editNotValidEmailWithAuth() {
        //LOGIN WITH CORRECT USER
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", "test12346@example.com");
        authData1.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);

        String userId = BaseTestCase.getStringFromJson(responseGetAuth, "user_id");

        //EDIT
        String email = "test12346example.com";
        Map<String, String> editData = new HashMap<>();
        editData.put("email", email);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }
    @Test
    public void editWithShortNameUser() {
        //LOGIN WITH CORRECT USER
        Map<String, String> authData1 = new HashMap<>();
        authData1.put("email", "test12346@example.com");
        authData1.put("password", "1234");

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData1);

        String userId = BaseTestCase.getStringFromJson(responseGetAuth, "user_id");

        //Short name in userName
        //EDIT
        String firstName = "a";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", firstName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest("https://playground.learnqa.ru/api/user/" + userId,
                        this.getHeader(responseGetAuth, "x-csrf-token"),
                        this.getCookie(responseGetAuth, "auth_sid"), editData);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "{\"error\":\"Too short value for field firstName\"}");
    }
}
