package com.example.user.tripper2;
        import android.content.ActivityNotFoundException;
        import android.content.Context;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.Locale;

public class Main extends AppCompatActivity {

    private TextView mes;
    private ProgressBar pr;
    private Button match; // CLICK MATCH ON MAIN ACTIVITY
    private EditText request;
    private ArrayList<CityWithKeys> results = null;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //REMOVE STATUS BAR - SCRIPT
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //*********************************

        setContentView(R.layout.activity_main); //connect to activity main

        btnSpeak = (ImageButton) findViewById(R.id.btn);
        pr = (ProgressBar) findViewById(R.id.pr);
        mes = (TextView)   findViewById(R.id.mes);
        match = (Button) findViewById(R.id.button); // connect the button
        request = (EditText) findViewById(R.id.editText);



        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //match button event
                String requsetString = request.getText().toString().trim();
                Log.d("Main.java", "in listener " + requsetString);
                new Alg().execute(requsetString);
                toggleView(pr);

            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

    }
    public void toggleView(View view)
    {
        if(view.getVisibility()==View.GONE)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }
    private class Alg extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            ArrayList<String> text = new ArrayList<>(Arrays.asList(params[0].split(" ")));
            String interesting_words = "";
            ArrayList<String> parsed_text = null;
            try {
                parsed_text = Thesaurus.calculateInterestedWords(getApplicationContext(), text);
            }
            catch (Exception e)
            {
                e.getStackTrace();
                Log.d("Amit",e.getMessage()+"333333333");
            }
            if(parsed_text.isEmpty())
                interesting_words = params[0].toLowerCase();
            else
                for (String word : parsed_text)
                    interesting_words += word + " ";
            Log.d("Amit2", interesting_words);
            InitiateAll init = new InitiateAll(interesting_words.toLowerCase().trim());
            Log.d("Main.java", "1");
            init.breakTheText();
            Log.d("Main.java", "2");
            for (int i = 0; i < init.getBlockOfWords().size(); i++) {
                Log.d("Main.java", "onClick: " + init.getBlockOfWords().get(i));
                Log.d("Main.java",init.getBlockOfWords().get(i));
                try {
                    Global.keywords.searchAndUpdate(init.getBlockOfWords().get(i));
                }
                catch(Exception e)
                {
                    Log.d("Main.java",e.getMessage());
                    return "Search Error";
                }
            }
            Log.d("Main.java", "3");
            results = CitiesCounter.getAllCitiesToShow();
            Log.d("Amit3",Integer.toString(results.size()));
            //Log.d("Amit3",results.get(0).equals(null)+"");
            Log.d("Main.java", Integer.toString(results.size()));
            if (results.size() == 0) {
                return "Not founded, Try Again";
            }
            return "";
        }



        @Override
        protected void onPostExecute(String a) {
            toggleView(pr);
            if(!a.equals("")) {
                mes.setText(a);
                toggleView(mes);
            }
            else {
                SearchVar.results = results;
                Intent i = new Intent(getApplicationContext(), List.class);
                startActivity(i);
            }
        }
        }

    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Describe your vecation!");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"Sorry! Your device doesnt support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try {
                        request.setText(result.get(0));
                    }
                    catch (NullPointerException e)
                    {
                        mes.setText("Google Record Error");
                        toggleView(mes);
                    }
                }
                break;
            }

        }
    }

    }

