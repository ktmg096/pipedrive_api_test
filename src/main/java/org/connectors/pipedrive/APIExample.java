package org.connectors.pipedrive;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.HashMap;
import java.util.Map;

public class APIExample {

    public static void main(String[] args) {
        try {
            String url = "http://13.232.62.135";
            HttpResponse<JsonNode> response;

            Map<String, String> headers = new HashMap<>();
            headers.put("accept", "application/json");
            headers.put("Authorization", "Bearer abcd");

            Map<String, Object> fields = new HashMap<>();
            fields.put("name", "sumant");
            fields.put("date", "today");


//            response = Unirest.get(url)
//                        .header("Authorization", "abcd")
//                        .queryString("qpiKey", "123")
//                        .asJson();

            response = Unirest.get(url)
                        .headers(headers)
                        .queryString(fields)
                        .asJson();

            System.out.println(response.getBody().getObject().get("b"));
            System.out.println(response.getHeaders().toString());
            System.out.println(response.getStatus());

            Unirest.shutdown();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}