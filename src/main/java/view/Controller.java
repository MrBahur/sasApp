package view;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

public class Controller {

    private static String serverURL = "http://localhost:8080";

    public void login() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            //HttpGet request = new HttpGet(serverURL + "/users");
            HttpPost request = new HttpPost(serverURL + "/users/login");

            JSONObject json = new JSONObject();
            json.put("username", "Kobi");
            json.put("password", "kobi123");

            //create the request
            StringEntity stringEntity = new StringEntity(json.toString());
            request.getRequestLine();
            request.setEntity(stringEntity);
            request.addHeader("Content-Type", "application/json");

            CloseableHttpResponse response = httpClient.execute(request);

            try {

                // Get HttpResponse Status - example for debugging
                System.out.println(response.getStatusLine().getStatusCode());   // 200
                System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                System.out.println(response.getStatusLine().toString());        // HTTP/1.1 200 OK

                //something went wrong - maybe show in UI
                if (response.getStatusLine().getStatusCode() != 200) {
                    //add massage in UI
                }

                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // return it as a String
                    String result = EntityUtils.toString(entity);
                    System.out.println(result);
                }

            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
