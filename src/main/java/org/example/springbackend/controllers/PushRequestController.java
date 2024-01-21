package org.example.springbackend.controllers;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@RestController
public class PushRequestController {

    @Value("${xahau.api.user.token}")
    private String xahauIssuedUserToken;

    @Value("${xahau.api.key}")
    private String xahauApiKey;

    @Value("${xahau.api.secret}")
    private String xahauApiSecret;

    @GetMapping("/paymentRequest")
    public void paymentRequest() throws NoSuchAlgorithmException, KeyManagementException {

        System.out.println("Payment request received");


        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        OkHttpClient.Builder newBuilder = new OkHttpClient.Builder();
        newBuilder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
        newBuilder.hostnameVerifier((hostname, session) -> true);

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

        try {

            Response response = client.newCall(request).execute();
        }
        catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}