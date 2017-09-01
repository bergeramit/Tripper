package com.example.mytbrgr.tries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mytbrgr on 25/05/2016.
 */

// singleton
public class CitiesCounter {
    private static int[] score;
    private static CityWithKeys[] cities; //intiate by cities
    private static CitiesCounter  citires_counter= new CitiesCounter();
    /* A private Constructor prevents any other
    * class from instantiating.
    */
    private CitiesCounter(){
       score = new int[512]; //all 0
       cities = new CityWithKeys[512];




        //set names to all the cities by YAPQ *************





    }

    public static CitiesCounter getInstance(){
        return citires_counter;
    }

    public static boolean addScore(int indexOfCity){
        if(indexOfCity<0 || indexOfCity>=512){
            return false;
        }
        ++score[indexOfCity];
        return true;
    }

    public static int getMaxScore(){
        int max=0;
        int maxIndex=-1;
        for(int i=0;i<512;i++){
            if(max<score[i]){
                max=score[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }


    private static boolean zeroIndex(int indexToZero){  // for protection the func is private
        if(indexToZero<0 || indexToZero>=512){
            return false;
        }
        score[indexToZero]=0;
        return true;
    }


    public static void initScore(){
        for(int i=0;i<512;i++)
            score[i] = 0;
    }

    private static CityWithKeys getCityByIndex(int c_index){
        return cities[c_index];
    }

    public boolean addKeyToCity(String keyWord,int index){ //adding specific key to city
        if(index < 0 || index >= 512){
            return false;
        }
        cities[index].addKeyWord(keyWord);
        return true;
    } // on initiation!! only



    //what ido wanted all the list already sorted by score from highest
    public static ArrayList<CityWithKeys> getAllCitiesToShow(){
        int maxNow = getMaxScore(); //index
        ArrayList<CityWithKeys> names= new ArrayList<CityWithKeys>();
       while(maxNow != 0){
            names.add(getCityByIndex(maxNow));
            zeroIndex(maxNow);
            maxNow = getMaxScore(); // return the index
        }
        initScore();
        return names;
    }
}
