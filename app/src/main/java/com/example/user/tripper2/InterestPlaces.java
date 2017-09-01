package com.example.user.tripper2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

public class InterestPlaces extends AppCompatActivity {

    LinearLayout container; // THE LINEARLAYOUT CONTAINER THAT CONTAINES ALL THE LIST OF INTEREST PLACES
    ScrollView scroll; // SCROLLABLE VIEW
    TextView headline; // THE HEADLINE
    Typeface font;      // FONT
    private String city;
    private int cityIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //REMOVE STATUS BAR - SCRIPT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //***************************
        city = getIntent().getExtras().getString("city"); // GET FROM THE LIST ACTIVITY THE CURRENT CITY
        CitiesCounter citiesCounter = CitiesCounter.getInstance();
        cityIndex = citiesCounter.getCityIndexByName(city); // CITY INDEX

        //WE WILL PARSE THE DATA OF THIS CITY ^^
        // REMINDER - WE SEND THE CURRENT CITY FROM List.java In THE BUTTON EVENT

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_why);
        font = Typeface.createFromAsset(getAssets(), "tc.TTF"); //FONT
        container = new LinearLayout(getApplicationContext());      //WE CREATE NEW LINEARLAYOUT CONTAINER (CONTAINER OF THE LIST)
        container.setOrientation(LinearLayout.VERTICAL);            // SET VERTICAL VIEW OF THE LIST
        scroll = (ScrollView) findViewById(R.id.scrollView);        // SCROLL
        headline = (TextView) findViewById(R.id.textView4);         // THE HEADLINE
        headline.setTypeface(font);                                 // SET FONT
        headline.setText(headline.getText().toString().trim() + " in " + city);
        headline.setTextSize(20);
        scroll.addView(container);                                  // ADD TO THE SCROLL THE LIST VIEW CONTAINER


        //HERE WE EXCUTE ASYNC PROCESS, THE ASYNCTASK GET THE URL OF THE JSON
        new JSONThread().execute("http://api.sandbox.amadeus.com/v1.2/points-of-interest/yapq-search-text?apikey=eqmDw8Xouiis1Re4lCfimfRI1FVSCLEh&city_name=" + city.trim().toLowerCase().replace(" ","%20"));

    }

    private class JSONThread extends AsyncTask<String, Void, String>
    {
        final String TAG = "InterestPlaces.java";
        // set your json string url here
        String yourJsonStringUrl;
        // contacts JSONArray
        JSONArray dataJsonArr = null;
        ArrayList<Place> placesToView = null; //ARRAYLIST OF THE PLACES THAT WE GOING TO SHOW (THE PLACE CLASS IS DOWN)
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
                dataJsonArr = json.getJSONArray("points_of_interest");
                placesToView = new ArrayList<Place>();
                // loop through all users
                for (int i = 0; i < dataJsonArr.length(); i++) {
                    // WE PASS THE WHOLE ARRAYLIST OF JSONOBJECTS TO VIEW ALL THE INTEREST PLACES
                    JSONObject c = dataJsonArr.getJSONObject(i); // RETRIEVE THE CURRENT INTEREST PLACE IN THE JSON FORM

                    // Storing each json item in variable - NOTICE THE AMADEUS JSON BEFORE UNDERSTANGING THE ATTRIBUTES
                    String main_image = c.getString("main_image"); // GET MAIN IMAGE ATTRIBUE FROM THE JSON
                    String title = c.getString("title");           // GET MAIN TITLE ATTRIBUE FROM THE JSON
                    JSONObject details = new JSONObject(c.getString("details")); // DETAILS IS A JSON ARRAY
                    String description = details.getString("short_description"); // EXTRACT FROM DETAILS THE DESCRIPTION
                    String longDescription = details.getString("description"); // EXTRACT FROM DETAILS THE DESCRIPTION

                    JSONArray keys = new JSONArray(c.getString("categories"));
                   /* if (keys != null) {
                        for (int k=0;k<keys.length();k++){
                            for(int j=0; j<SearchVar.results.get(cityIndex).getKeyWords().size();j++) // CHECKING FOR MATCHING PLACES WITH SAME KEYS
                                if(keys.get(k).toString().contains(SearchVar.results.get(cityIndex).getKeyWords().get(j))); // WE DONT SHOW ANY PLACE THAT DONT CONNECT TO THE USER TEXT
                                {
                                    placesToView.add(new Place(main_image, title, description, longDescription)); // ADD THE PLACES ARRAY THIS CURRENT PLACE
                                    Log.d(TAG,"Key: " + keys.get(k) + " - City Name: " + city );
                                }
                        }
                    }*/
                    placesToView.add(new Place(main_image, title, description, longDescription)); // ADD THE PLACES ARRAY THIS CURRENT PLACE
                    if(placesToView.isEmpty())
                        return "empty";
                    // show the values in our logcat
                   /* Log.e(TAG, "main_image: " + main_image
                            + ", title: " + title
                            + ", short_description: " + details);*/

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String a) {
            // WHEN WE FINISHED INSERT TO THE ARRAYLIST WHOLE THE INTEREST PLACES IN THE BACKGROUND TASK
            // START TO VIEW ALL THE PLACES

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            for(int i=0;i<placesToView.size();i++)
            {
                //FROM HERE THIS IS THE VIEW STRUCTURE AND THE VIEW HANDLING
                LinearLayout currentPlace = new LinearLayout(getApplicationContext()); //CURRENT LINEARVIEW PLACE LIST OBJECT
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  //SET HIS MARGINS
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(20, 20, 20, 20); //DISTANCE OF 2PX BETWEEN EACH LIST OBJECT
                currentPlace.setGravity(View.TEXT_ALIGNMENT_CENTER);
                currentPlace.setLayoutParams(params); //SET THE MARGIN
                currentPlace.setBackgroundColor(Color.WHITE); // SET BACKGROUND OF LIST OBJECT
                LinearLayout descriptionWithoutImage = new LinearLayout(getApplicationContext());
                descriptionWithoutImage.setOrientation(LinearLayout.VERTICAL);
                final TextView title = new TextView(getApplicationContext()); //HANDLE THE TITLE OF THE PLACE
                final TextView clickForInfo = new TextView(getApplicationContext()); //HANDLE THE TITLE OF THE PLACE
                final TextView longDescription = new TextView(getApplicationContext()); //HANDLE THE TITLE OF THE PLACE
                longDescription.setVisibility(View.GONE);
                longDescription.setTextColor(Color.parseColor("#696969"));
                longDescription.setText(placesToView.get(i).longDescription);
                longDescription.setTextSize(14);

                clickForInfo.setVisibility(View.VISIBLE);
                clickForInfo.setTextColor(Color.parseColor("#95a5a6"));
                clickForInfo.setText("Click for more info...");
                clickForInfo.setTypeface(null, Typeface.BOLD_ITALIC);
                clickForInfo.setTextSize(12);

                title.setTypeface(font);
                final TextView description = new TextView(getApplicationContext());
                final ImageView image = new ImageView(getApplicationContext());
                image.setMaxHeight(metrics.widthPixels - 100);
                image.setMaxWidth(metrics.widthPixels - 40);
                image.setMinimumHeight(metrics.widthPixels - 100);
                image.setMinimumWidth(metrics.widthPixels - 40);
                currentPlace.setMinimumWidth(metrics.widthPixels - 50);
                currentPlace.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
                description.setText(placesToView.get(i).details);
                title.setText(placesToView.get(i).title);
                title.setTextSize(40);
                title.setPadding(50, 20, 50, 20);
                title.setGravity(View.TEXT_ALIGNMENT_CENTER);
                description.setPadding(40, 40, 40, 10);
                longDescription.setPadding(40, 40, 40, 40);
                clickForInfo.setPadding(40, 0, 40, 10);
                longDescription.setBackgroundColor(Color.parseColor("#F5F5F5"));
                longDescription.setTextColor(Color.parseColor("#2c3e50"));
                description.setTextColor(Color.parseColor("#B2B2B2"));
                title.setTextColor(Color.parseColor("#2c3e50"));
                currentPlace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (longDescription.getVisibility() == View.GONE) {
                            longDescription.setVisibility(View.VISIBLE);
                            clickForInfo.setVisibility(View.GONE);
                        } else if (longDescription.getVisibility() == View.VISIBLE) {
                            longDescription.setVisibility(View.GONE);
                            clickForInfo.setVisibility(View.VISIBLE);
                        }
                    }
                });
                // PICASO - External library that help us load the image from url with simple syntax
                    Picasso.with(getApplicationContext())
                            .load(placesToView.get(i).main_image)
                            .resize(metrics.widthPixels - 100, metrics.widthPixels - 100)
                            .centerCrop()
                            .into(image, new Callback()
                            {
                                @Override
                                public void onSuccess()
                                {
                                }

                                @Override
                                public void onError()
                                {
                                    image.setVisibility(View.GONE);
                                    title.setPadding(50, 20, 50, 5);
                                }
                            });
                               //ADDING THE VIEWS TO THE CONTAINERS
                descriptionWithoutImage.addView(title);
                if(image!=null)
                    descriptionWithoutImage.addView(image);
                descriptionWithoutImage.addView(description);
                descriptionWithoutImage.addView(clickForInfo);
                descriptionWithoutImage.addView(longDescription);
/*
                currentPlace.addView(image);
*/
                currentPlace.addView(descriptionWithoutImage);
                container.addView(currentPlace);
            }
        }
    }

    private class Place{ // PRIVATE CLASS FOR HANDLING THE DATA FROM AMADEUS
        private String main_image;
        private String title;
        private String details;
        private String longDescription;

        public Place(String main_image, String title, String details,String longDescription) {
            this.main_image = main_image;
            this.title = title;
            this.details = details;
            this.longDescription = longDescription;
        }
    }
}
