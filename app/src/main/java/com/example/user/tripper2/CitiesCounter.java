package com.example.user.tripper2;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mytbrgr on 25/05/2016.
 */

// singleton
public class CitiesCounter {
    private static int[] score;
    private static CityWithKeys[] cities; //intiate by cities
    private static CitiesCounter  citires_counter;
    /* A private Constructor prevents any other
    * class from instantiating.
    */
    private CitiesCounter(){
        score = new int[86]; //all 0
        cities = new CityWithKeys[86];
        //set names to all the cities by YAPQ *************
    }

    public static CitiesCounter getInstance(){
        if(citires_counter==null)
            citires_counter= new CitiesCounter();
        return citires_counter;
    }

    public static boolean addScore(int indexOfCity){
        if(indexOfCity<0 || indexOfCity>=86){
            return false;
        }
        ++score[indexOfCity];
        return true;
    }

    public static int getMaxScore(){
        int max=0;
        int maxIndex=-1;
        for(int i=0;i<86;i++){
            Log.d("List.java",Integer.toString(score[i]));
            if(max<score[i]){
                max=score[i];
                maxIndex = i;
            }
        }
        Log.d("Main.java","maxIndex:  *" + maxIndex);
        return maxIndex;
    }

    public CityWithKeys getCityObject(int index)
    {
        return cities[index];
    }
    private static boolean zeroIndex(int indexToZero){  // for protection the func is private
        if(indexToZero<0 || indexToZero>=86){
            return false;
        }
        score[indexToZero]=0;
        return true;
    }

    public int getCityIndexByName(String city)
    {
        for(int i=0; i<cities.length;i++)
            if(cities[i].getName().equals(city))
                return i;
        return 0; // change 0 !!! REMINDER
    }
    public static void initScore(){
        for(int i=0;i<86;i++)
            score[i] = 0;
    }

    public static CityWithKeys getCityByIndex(int c_index){
        return cities[c_index];
    }

    public boolean addKeyToCity(String keyWord,int index){ //adding specific key to city
        if(index < 0 || index >= 86){
            return false;
        }
        cities[index].addKeyWord(keyWord);
        return true;
    } // on initiation!! only

    public boolean addKeysToCity(CityWithKeys c){ //adding specific key to city
        if( c.getId() < 0 || c.getId() >= 86){
            return false;
        }
        cities[c.getId()] = new CityWithKeys(c);
        //cities[c.getId()].addKeysWord(c.getKeyWords());
        return true;
    } // on initiation!! only


    public static void printScore(){
        for(int i=0;i<86;++i){
            Log.d("Hey", "printScores: i= "+Integer.toString(i)+" score "+score[i]);
        }
    }

    //what ido wanted all the list already sorted by score from highest
    public static ArrayList<CityWithKeys> getAllCitiesToShow(){
        int maxNow = getMaxScore(); //index
        Log.d("Main.java", "MAX INDEX: " + Integer.toString(getMaxScore()));
        ArrayList<CityWithKeys> names= new ArrayList<CityWithKeys>();
        while(maxNow > 0){
            names.add(getCityByIndex(maxNow));
            zeroIndex(maxNow);
            maxNow = getMaxScore(); // return the index
        }
        initScore();
        return names;
    }
    @Override
    public String toString()
    {
        String all_countries = "";
        for(int i=0; i<cities.length; i++)
        {
            if(cities[i]!=null)
            all_countries = all_countries + "," + cities[i].getName();
        }
        return all_countries;
    }
}
