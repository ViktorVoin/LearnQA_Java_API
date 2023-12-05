package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getHeader(Response response, String name) {
        Headers headers = response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn`t have header with " + name);
        return headers.getValue(name);
    }

    protected String getCookie(Response response, String name) {
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn`t have cookie with " + name);
        return cookies.get(name);
    }

    protected static int getIntFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().getInt(name);
    }

    protected static String getStringFromJson(Response Response, String name) {
        Response.then().assertThat().body("$", hasKey(name));
        return Response.jsonPath().get(name).toString();
    }

}
