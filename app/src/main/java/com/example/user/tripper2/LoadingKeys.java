package com.example.user.tripper2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class LoadingKeys extends Activity {

   // CityWithKeys cityWithKeys[] = new CityWithKeys[121];
    CitiesCounter citiesCounter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_keys);
        Global.keywords = new Trie();
        citiesCounter = CitiesCounter.getInstance();
        new GetKeys().execute("keys.txt");
        new GetImage().execute("Image.txt");
    }

    private class GetKeys extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                StringBuilder buf = new StringBuilder();
                InputStream file = getAssets().open(params[0]);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(file, "UTF-8"));
                String str;
                ArrayList<String> tempkeys = new ArrayList<>();
                CityWithKeys temp = new CityWithKeys();
                String name = null;
                int index = 0;
                while ((str = in.readLine()) != null) {
                    str.trim().toLowerCase();
                    if(str.contains(":"))
                    {
                        if(temp.getName() != null) {
                            tempkeys.removeAll(Arrays.asList("null")); // remove all null and empty places
                            //cityWithKeys[index] = new CityWithKeys(temp,tempkeys); // set the new city with id and keys
                            citiesCounter.addKeysToCity(new CityWithKeys(temp,tempkeys));
                            index++;
                        }
                        //name = str.replace(":", ""); //remove ":"
                        String[] bothNames = str.trim().replace(":", "-").split("-"); //remove ":"
                        temp.setName(bothNames[0]);
                        temp.setIATA(bothNames[1]);
                        temp.setName(name);
                        temp.getKeyWords().clear(); // INSURANCE - empty current obj
                        tempkeys = new ArrayList<>(); // new
                        temp.setId(index); // city id = index
                    }
                    else {
                        for(int i=0; i<Arrays.asList(str.trim().split(" ")).size(); i++) {

                            //Log.d("Hey", Arrays.asList(str.trim().split(" ")).get(i).toLowerCase());
                            if(! isNumeric(Arrays.asList(str.trim().split(" ")).get(i))){
                                Global.keywords.addWord(Arrays.asList(str.trim().split(" ")).get(i).toLowerCase());
                                //Log.d("Hey",Integer.toString(temp.getId()));
                                Global.keywords.findAndAddCityIndex(Arrays.asList(str.trim().split(" ")).get(i).toLowerCase(), temp.getId());
                                 }
                        }
                        tempkeys.addAll(Arrays.asList(str.trim().split(" "))); // make every single word as a different key
                    }
                    buf.append(str);
                }
                in.close();
                index++;
            }
            catch(Exception e)
            {
                Log.d("Hey", "ERROR");
                Log.d("Hey", e.getLocalizedMessage());
                e.getStackTrace();
            }

            return "";
        }


        public  boolean isNumeric(String str)
        {
            for (char c : str.toCharArray())
            {
                if (!Character.isDigit(c)) return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(String a) {
/*
            for(int i=0;i<cityWithKeys.length;i++)
            {
                if(cityWithKeys[i]!=null)
                Log.d("Hey", cityWithKeys[i].toString());
            }*/
            Global.keywords.printListOfCityIndex("properties");
            Global.keywords.searchAndUpdate("properties");
            CitiesCounter check = CitiesCounter.getInstance();
            //check.printScore();
            //Log.d("Hey",citiesCounter.toString() + "**********");
            startActivity(new Intent(getApplicationContext(),Main.class));
        }

    }

    private class GetImage extends AsyncTask<String,Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                StringBuilder buf = new StringBuilder();
                InputStream file = getAssets().open(params[0]);
                BufferedReader in = new BufferedReader(new InputStreamReader(file, "UTF-8"));
                String str;
                String name = null;
                int index = 0;
                while ((str = in.readLine()) != null) {
                        CitiesCounter update = CitiesCounter.getInstance();
                        update.getCityObject(index).setURL(str);
                    buf.append(str);
                    index++;
                }
                in.close();

            }
            catch(Exception e)
            {
                Log.d("Hey", "ERROR2222");
                Log.d("Hey", e.getLocalizedMessage());
                e.getStackTrace();
            }

            return "";
        }
        @Override
        protected void onPostExecute(String a) {
            /*
            for(int i=0;i<cityWithKeys.length;i++)
            {
                if(cityWithKeys[i]!=null)
                Log.d("Hey", cityWithKeys[i].toString());
            }*/
            CitiesCounter check = CitiesCounter.getInstance();
            check.printURL();
            Log.d("Hey",citiesCounter.toString() + "**********");
            startActivity(new Intent(getApplicationContext(),Main.class));
        }
    }
}
