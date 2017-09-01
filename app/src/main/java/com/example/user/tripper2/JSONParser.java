package com.example.user.tripper2;

        import org.apache.http.client.methods.HttpGet;
        import org.json.JSONObject;
        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.json.JSONException;
        import android.util.Log;

public class JSONParser {

    // WHEN WE PARSE JSON FILES THIS IS THE CLASS THAT WE USE
    // WE SEND TO THE FUNCTION getJSONFromUrl the url of the JSON, and we got JSONObject of the JSON

    final String TAG = "JsonParser.java"; // ERROR CHECKING TAG IN THE CONSOLE

    static InputStream is = null; // GET THE INPUT
    static JSONObject jObj = null; // THE RETURNED JSON OBJECT
    static String json = "";      // STRING OF JSON

    public JSONObject getJSONFromUrl(String url) {

        // make HTTP request
        try {
            //CONNECT TO THE AMADEUS URL
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //FETCHING THE DATA FROM THE URL
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString(); // NOW WE HAVE IN json VARIABLE ALL THE JSON STRING
        } catch (Exception e) {
            Log.e(TAG, "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json); // INSERT THE JSON INTO JSONOBJECT
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing data " + e.toString());
        }

        // return JSON OBJECT
        return jObj;
    }
}