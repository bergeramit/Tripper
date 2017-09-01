package com.example.user.tripper2;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class List extends AppCompatActivity {

    //******************************
    //SHOW MATCHING CITIES ACTIVITY
    //******************************

    private final String apiKey = "eqmDw8Xouiis1Re4lCfimfRI1FVSCLEh"; //our Amadues key
    private ImageButton cityImage; // the current city image in the screen
    private ImageButton cityImageBackground; // the current city image in the screen
    private Button why;            // button -> change activity to the list of insterest places
    private Button order;          // button -> order page
    private ArrayList<CityWithKeys> cities = SearchVar.results; //list of cities names (Until we have the alg)
    private int currentCity = -1;    // current city index in the arraylists
    private TextView title;         // the title of the current city in the screen
    DisplayMetrics metrics = new DisplayMetrics();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //REMOVE STATUS BAR - SCRIPT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //****************************
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); //connect to activity list xml
        // initiallize views
        cityImage = (ImageButton) findViewById(R.id.imageButton);
        why = (Button) findViewById(R.id.button2);
        order = (Button) findViewById(R.id.button3);
        title = (TextView) findViewById(R.id.textView);
        // initiallize buttons end
        Typeface font = Typeface.createFromAsset(getAssets(),"tc.TTF");                 //font initiallize
        Typeface fontBold = Typeface.createFromAsset(getAssets(),"tcbold.TTF");        //font initiallize
        // DESIGN TEXTS START
        why.setTypeface(font);
        why.setTextSize(18);
        order.setTypeface(font);
        order.setTextSize(18);
        // DESIGN TEXTS END

        viewNextCity(); // call the next city view and update
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Hey","Clicked");
                startActivity(new Intent(getApplicationContext(), AmadeusJson.class));
            }
        });
        cityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewNextCity();
            }
        }); // click on city image event

        why.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), InterestPlaces.class);
                ii.putExtra("city", cities.get(currentCity).getName()); //WE WANT TO PASS THE CURRENT CITY STRING VARIABLE
                ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   // CLAR FLAGS
                startActivity(ii);            }
        });             // START InterestPlaces ACTIVITY
        // click on interest place event

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("List.java","ENTERED");
                startActivity(new Intent(getApplicationContext(),AmadeusJson.class));
            }
        });
    }

    public void viewNextCity() //update and show next city
    {
        currentCity++; //NEXT CITY INDEX UPDATE
        if(currentCity==cities.size()) // if we ended the arraylist get back to the start
            currentCity = 0;
        Log.d("Hey",currentCity + "");
        title.setText(cities.get(currentCity).getName());
        // PICASO - External library that help us load the image from url with simple syntax
        Picasso.with(getApplicationContext())
                .load(cities.get(currentCity).getImgURL())
                .resize(metrics.widthPixels, metrics.heightPixels)
                        .centerCrop()
                        .into(cityImage);
    }
}
