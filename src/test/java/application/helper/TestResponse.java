package application.helper;

import com.google.common.net.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static application.helper.JsonUtil.toJson;

public class TestResponse {

    public final String body;
    public final int status;

    public TestResponse(int status, String body) {
        this.status = status;
        this.body = body;
    }

    public Map json() {
        return (Map) JsonUtil.fromJson(body, HashMap.class);
    }

    public static TestResponse request(String method, String path, Map<String, Object> params) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:4567" + path))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .method(method.toUpperCase(), HttpRequest.BodyPublishers.ofString(toJson(params)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new TestResponse(response.statusCode(), response.body());
    }
}