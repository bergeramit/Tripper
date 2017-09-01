package com.example.user.tripper2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class ImportJsons extends Activity {

    ArrayList<String> keywords;
    ArrayList<String> citiesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_jsons);
        keywords = new ArrayList<String>();
        citiesList = new ArrayList<String>();


        String cities = "Abu%20Dhabi\n" +
                "Antalya\n" +
                "Atlanta\n" +
                "Auckland\n" +
                "Baku\n" +
                "Bangkok\n" +
                "Barcelona\n" +
                "Beijing\n" +
                "Berlin\n" +
                "Bilbao\n" +
                "Birmingham\n" +
                "Bratislava\n" +
                "Bucharest\n" +
                "Budapest\n" +
                "Cairo\n" +
                "Cape%20Town\n" +
                "Chennai\n" +
                "Chiang%20Mai\n" +
                "Chicago\n" +
                "Christchurch\n" +
                "Copenhagen\n" +
                "Denpasar\n" +
                "Doha\n" +
                "Dubai\n" +
                "Dublin\n" +
                "Edinburgh\n" +
                "Florence\n" +
                "Glasgow\n" +
                "Graz\n" +
                "Guangzhou\n" +
                "Hamburg\n" +
                "Hangzhou\n" +
                "Hanoi\n" +
                "Helsinki\n" +
                "Ho%20Chi%20Minh%20City\n" +
                "Honolulu\n" +
                "Indianapolis\n" +
                "Innsbruck\n" +
                "Iowa%20City\n" +
                "Jaipur\n" +
                "Jakarta\n" +
                "Jerusalem\n" +
                "Kiev\n" +
                "Kolkata\n" +
                "Krakow\n" +
                "Las%20Vegas\n" +
                "Lima\n" +
                "Linz\n" +
                "Lisbon\n" +
                "Liverpool\n" +
                "London\n" +
                "Los%20Angeles\n" +
                "Lyon\n" +
                "Madrid\n" +
                "Manama\n" +
                "Manchester\n" +
                "Manila\n" +
                "Marseille\n" +
                "Mecca\n" +
                "Melbourne\n" +
                "Mexico%20City\n" +
                "Milan\n" +
                "Monaco\n" +
                "Montreal\n" +
                "Moscow\n" +
                "Mumbai\n" +
                "Munich\n" +
                "Nairobi\n" +
                "Nanjing\n" +
                "New%20Delhi\n" +
                "New%20York\n" +
                "Newcastle%20upon%20Tyne\n" +
                "Nice\n" +
                "Orlando\n" +
                "Oslo\n" +
                "Oxford\n" +
                "Paris\n" +
                "Prague\n" +
                "Reims\n" +
                "Reykjavik\n" +
                "Riyadh\n" +
                "Rome\n" +
                "San%20Diego\n" +
                "San%20Francisco\n" +
                "San%20Jose\n" +
                "Seattle\n" +
                "Seoul\n" +
                "Shanghai\n" +
                "Shenzhen\n" +
                "Singapore\n" +
                "Sofia\n" +
                "Stockholm\n" +
                "Sydney\n" +
                "Tallinn\n" +
                "Tel%20Aviv\n" +
                "Toronto\n" +
                "Turku\n" +
                "Vancouver\n" +
                "Vienna\n" +
                "Warsaw\n" +
                "Washington\n" +
                "Zaragoza\n" +
                "Zurich";

        citiesList.addAll(new ArrayList(Arrays.asList(cities.split("\n"))));
            new JSONThread().execute("https://api.sandbox.amadeus.com/v1.2/points-of-interest/yapq-search-text?apikey=eqmDw8Xouiis1Re4lCfimfRI1FVSCLEh&city_name=");

    }

    private class JSONThread extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            final String TAG = "IMPORT_ALL";

            // set your json string url here
            String yourJsonStringUrl;

            // contacts JSONArray
            JSONArray dataJsonArr = null;
            yourJsonStringUrl = params[0];
            try {

                // instantiate our json parser
                JSONParser jParser = new JSONParser();
                int onetime = 0;
                String replace = null;
                for(int j=0; j<citiesList.size();j++) {
                //for(int j=0; j<20;j++){ my func AKA Amit
                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl + citiesList.get(j));
                // get the array of users
                dataJsonArr = json.getJSONArray("points_of_interest");
                // loop through all users

                   for (int i = 0; i < dataJsonArr.length(); i++) {
                        // WE PASS THE WHOLE ARRAYLIST OF JSONOBJECTS TO VIEW ALL THE INTEREST PLACES
                       JSONObject c = dataJsonArr.getJSONObject(i); // RETRIEVE THE CURRENT INTEREST PLACE IN THE JSON FORM
                        String title = c.getString("title");
                        // Storing each json item in variable - NOTICE THE AMADEUS JSON BEFORE UNDERSTANGING THE ATTRIBUTES
                        String cat = c.getString("categories");
                       if(cat.length()>=10 && onetime == 0) {
                           replace = citiesList.get(j).replace("%20"," ");
                           Log.d("IMPORT_ALL",replace+":");
                           onetime=1;
                       }

                       cat = cat.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");
                        // show the values in our logcat
                        ArrayList<String> strings = new ArrayList(Arrays.asList(cat.split(",")));
                        print(strings);
                        keywords.addAll(strings);
                    }
                    onetime=0;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                //Log.d(TAG, e.getMessage());
            }

            return null;
        }
        @Override
        protected void onPostExecute(String a) {

        }


    }
    public void print(ArrayList<String> arrayList) {
        for(int i=0; i<arrayList.size(); i++) {
            Log.d("IMPORT_ALL",arrayList.get(i));
        }
    }
}
