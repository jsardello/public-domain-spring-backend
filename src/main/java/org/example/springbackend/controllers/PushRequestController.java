package org.example.springbackend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PushRequestController {

    @Value("${xahau.api.user.token}")
    private String xahauIssuedUserToken;

    @Value("${xahau.api.key}")
    private String xahauApiKey;

    @Value("${xahau.api.secret}")
    private String xahauApiSecret;

    @GetMapping("/paymentRequest")
    public void paymentRequest() {

        System.out.println("Payment request received");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String requestBody = "{ \n" +
                "  \"txjson\": {\n" +
                "    \"TransactionType\": \"Payment\",\n" +
                "    \"Destination\": \"rPBNCq9AxjdXGdRMQG51TkQCKhD9bSMEMr\",\n" +
                "    \"Fee\": \"12\",\n" +
                "    \"Account\": \"rBzenNKj1R8kDmi7cRydSmgkD4st6uAu78\",\n" +
                "    \"Amount\": \"10000000\"\n" +
                "  },\n" +
                "  \"user_token\": \"" + xahauIssuedUserToken + "\"\n" +
                "}";

        System.out.println(xahauIssuedUserToken);
        okhttp3.RequestBody body = okhttp3.RequestBody.create(MediaType.parse("application/json"), requestBody);
        Request request = new Request.Builder()
                .url("https://xumm.app/api/v1/platform/payload")
                .method("POST", body)
                .header("Content-Type", "application/json")
                .header("x-api-key", xahauApiKey)
                .header("x-api-secret", xahauApiSecret)
                .build();

//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode json = mapper.readValue(requestBody, ObjectNode.class);

        try {

            Response response = client.newCall(request).execute();
//            JSONParser parser = new JSONParser();
//            json = (JSONObject) parser.parse(response.body().string());
//            System.out.println(json);
        }
        catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
