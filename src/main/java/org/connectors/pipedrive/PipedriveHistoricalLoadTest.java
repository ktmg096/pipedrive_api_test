package org.connectors.pipedrive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PipedriveHistoricalLoadTest {
    public static ArrayList<String> fetchOrganizations() {
        HttpResponse<JsonNode> response;
        JSONArray responseData = null;
        String responseJson = null;
        Object responseObject = null;
        int responseLength;
        ArrayList<String> result = new ArrayList<String>();
        int limit = 5;
        int start = 0;

        try {
            while(true) {
                response = Unirest.get(PipedriveAPITest.url)
                                .queryString("api_token", PipedriveAPITest.API_TOKEN)
                                .queryString("start", start)
                                .queryString("limit", limit)
                                .asJson();

                responseObject = response.getBody().getObject().get("data");
                if(responseObject.equals(null))
                    break;

                responseData = response.getBody().getObject().getJSONArray("data");
                responseLength = responseData.length();

                for (int i=0; i<responseLength; ++i) {
                    Map<String, Object> data = new ObjectMapper().readValue(responseData.get(i).toString(), HashMap.class);
                    data.remove("owner_id");

                    responseJson = data.toString();
                    result.add(responseJson);
                    System.out.println(responseJson);
                }
                start += responseLength;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(fetchOrganizations());
    }
}
