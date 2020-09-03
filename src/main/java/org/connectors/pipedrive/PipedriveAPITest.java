package org.connectors.pipedrive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class PipedriveAPITest {

    public static String API_TOKEN = "4b27fc328d0c3fd3292fb19282267b7cbae91f5b";
    public static String url = "https://api.pipedrive.com/v1/organizations";

    public static String scrapeOrganizations() {
        HttpResponse<JsonNode> response;
        String result = "";

        try {

            response = Unirest.get(url)
                    .queryString("api_token", API_TOKEN)
                    .asJson();

//            System.out.println(response.getBody().getObject().getJSONArray("data"));
            Object responseData = response.getBody().getObject().getJSONArray("data").get(0);
//            System.out.println(responseData.toString());

            Map<String, Object> data = new ObjectMapper().readValue(responseData.toString(), HashMap.class);
            data.remove("owner_id");
//            System.out.println(data.toString());
            result = data.toString();

            for(Map.Entry<String, Object> entry: data.entrySet()) {
                if(entry.getValue() == null) System.out.println(entry.getKey() + " = " + entry.getValue() + " (" + "null" + ")");
                else
                System.out.println(entry.getKey() + " = " + entry.getValue() + " (" + entry.getValue().getClass() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(scrapeOrganizations());
    }
}
