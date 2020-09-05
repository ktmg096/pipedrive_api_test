package org.connectors.pipedrive;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import java.util.ArrayList;

public class PipedriveIncrementalTest {
    private int start;
    private int limit;
    private String currentTime;
    private String API;

    public PipedriveIncrementalTest(int start, int limit, String currentTime) {
        this.start = start;
        this.limit = limit;
        this.currentTime = currentTime;
        this.API = "https://api.pipedrive.com/v1/recents";
    }

    public ArrayList<String> incrementalFetch() {
        ArrayList<String> results = new ArrayList<String>();
        HttpResponse<JsonNode> response;
        Object responseObject;
        JSONArray responseArray;
        int responseLength;
        String timestamp = this.currentTime;

        try {
            while (true) {
                response = Unirest.get(this.API)
                                .queryString("api_token", PipedriveAPITest.API_TOKEN)
                                .queryString("since_timestamp", timestamp)
                                .queryString("start", this.start)
                                .queryString("limit", this.limit)
                                .asJson();

                responseObject = response.getBody().getObject().get("data");
                if(responseObject.equals(null)) {
                    this.start = 0;
                    break;
                }

                responseArray = (JSONArray) responseObject;
                responseLength = responseArray.length();
                for(int i=0; i<responseLength; ++i) {
                    results.add(responseArray.get(i).toString());
                }

                this.start += responseLength;
                this.currentTime = response.getBody().getObject().getJSONObject("additional_data").get("last_timestamp_on_page").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void main(String[] args) throws InterruptedException {
        PipedriveIncrementalTest pipedriveIncrementalTest = new PipedriveIncrementalTest(0, 2, "2020-09-05 17:56:00");
        while (true) {
            ArrayList<String> strings = pipedriveIncrementalTest.incrementalFetch();
            for(String s: strings) System.out.println(s);
            System.out.println("Done Parsing. Current Offset: " + pipedriveIncrementalTest.currentTime);
            Thread.sleep(5000);
        }
    }
}
