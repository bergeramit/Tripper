package com.example.user.tripper2;

import android.util.Log;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;

import com.example.user.tripper2.JSONParser;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
/**
 * Created by mytbrgr on 02/06/2016.
 */
public class AmadeusJson extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AmadeusJson.java","EEEE");
        String city = "DUB";
        String destination = "LON";
        String date = "2016-06-25";
        new JSONThread().execute("https://api.sandbox.amadeus.com/v1.2/flights/affiliate-search?apikey=eqmDw8Xouiis1Re4lCfimfRI1FVSCLEh&origin="+city.trim().toLowerCase().replace(" ","%20")+"&destination="+destination.trim().toLowerCase().replace(" ","%20")+"&departure_date="+date);
    }
    private class JSONThread extends AsyncTask<String, Void, String>
    {
        ArrayList<Flight> allflights = new ArrayList<Flight>();
        final String TAG = "AmadeusJson.java";
        // set your json string url here
        String yourJsonStringUrl;
        // contacts JSONArray
        JSONArray dataJsonArr = null;
        JSONArray flight = null;
        @Override
        protected String doInBackground(String... params)
        {
            yourJsonStringUrl = params[0];
            try {
                // instantiate our json parser
                JSONParser jParser = new JSONParser();
                // get json string from url
                JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);
                Log.d(TAG,json.toString());
                // get the array of users
                dataJsonArr = json.getJSONArray("results");
                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    // WE PASS THE WHOLE ARRAYLIST OF JSONOBJECTS TO VIEW ALL THE INTEREST PLACES
                    JSONObject outbound = dataJsonArr.getJSONObject(i); // RETRIEVE THE CURRENT INTEREST PLACE IN THE JSON FORM
                    JSONArray flight = outbound.getJSONArray("flights");
                    JSONObject obj1 = flight.getJSONObject(0);
                    String desairport = obj1.getJSONObject("destination").getString("airport");
                    String terminal = obj1.getJSONObject("destination").getString("terminal");
                    JSONObject origin = flight.getJSONObject(1);
                    String fromairport = origin.getString("airport");
                    String timeofdeparte = flight.getJSONObject(5).getString("departs_at");
                    timeofdeparte = timeofdeparte.replace('T',' ');
                    String timeofarrive = flight.getJSONObject(2).getString("arrives_at");
                    timeofarrive = timeofarrive.replace('T',' ');
                    String seatsRemain = flight.getJSONObject(3).getString("seats_remaining");
                    String deeperLink = outbound.getJSONObject("deep_link").getString("deep_link");
                    String pricePerAdult = outbound.getJSONObject("price_per_adult").getString("total_fare");
                    String totalPrice = outbound.getJSONObject("total_price").getString("total_price");
                    allflights.add(new Flight(fromairport,desairport,terminal,timeofdeparte,timeofarrive,seatsRemain,pricePerAdult,totalPrice,deeperLink));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String a) {
            for(int i=0;i<allflights.size();++i){
                Log.d("Hey", "time: " + allflights.get(i).getTimeofdeparte() + " and link: " + allflights.get(i).getDeeperLink());
            }
            //FROM HERE THIS IS THE VIEW STRUCTURE AND THE VIEW HANDLING
        }
    }
    private class Flight{
        private String fromairport;
        private String desairport;
        private String terminal;
        private String timeofdeparte;
        private String timeofarrive;
        private String seatsRemain;
        private String pricePerAdult;
        private String totalPrice;
        private String deeperLink;
        public Flight( String fromairport,String desairport,String terminal,String timeofdeparte,String timeofarrive,String seatsRemain,String pricePerAdult,String totalPrice,String deeperLink){
            this.deeperLink=deeperLink;
            this.desairport=desairport;
            this.fromairport=fromairport;
            this.pricePerAdult=pricePerAdult;
            this.seatsRemain=seatsRemain;
            this.terminal=terminal;
            this.totalPrice=totalPrice;
            this.timeofarrive=timeofarrive;
            this.timeofdeparte=timeofdeparte;
        }
        public String getFromairport() {
            return fromairport;
        }
        public String getDesairport() {
            return desairport;
        }
        public String getTerminal() {
            return terminal;
        }
        public String getTimeofdeparte() {
            return timeofdeparte;
        }
        public String getTimeofarrive() {
            return timeofarrive;
        }
        public String getSeatsRemain() {
            return seatsRemain;
        }
        public String getPricePerAdult() {
            return pricePerAdult;
        }
        public String getTotalPrice() {
            return totalPrice;
        }
        public String getDeeperLink() {
            return deeperLink;
        }
    }
}
