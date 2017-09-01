package com.example.user.tripper2;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 28/05/2016.
 */

public class CityWithKeys {
    private int id;
    private ArrayList<String> keyWords;
    private String name;
    private String imgURL;
    private String IATA;
    public CityWithKeys(){
        name = null;
        keyWords = new ArrayList<String>();
        this.imgURL = "no set yet";
        IATA = null;
    }

    public CityWithKeys(String name, ArrayList arrayList, int id){
        this.id = id;
        this.name = name;
        this.keyWords = arrayList;
        this.imgURL = "no set yet";
        IATA = null;
    }
    public CityWithKeys(CityWithKeys c){
        id = c.getId();
        name = c.getName();
        keyWords = c.getKeyWords();
        IATA = null;
    }
    public CityWithKeys(CityWithKeys c,ArrayList e){
        id = c.getId();
        name = c.getName();
        keyWords = e;
        this.imgURL = "no set yet";
        IATA = null;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }


    public void setURL(String url){
        this.imgURL = url;
    }


    public String getURL(){
        if(imgURL == null){
            return null;
        }
        return imgURL;
    }
    public void setName(String name) {
        this.name = name;
    } // on initiation

    public void setId(int id) {
        this.id = id;
    }

    public void setKeyWords(ArrayList<String> keyWords) {
        this.keyWords = keyWords;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    } // for view

    public void addKeyWord(String key) {
        keyWords.add(key);
    } // only in singleton initiate
    public void addKeysWord(ArrayList<String> key) {
        keyWords.addAll(key);
    } // only in singleton initiate

    public ArrayList<String> getKeyWords() {
        return keyWords;
    } // only for viewing

   /* public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    } //only in singleton initiate

    public String getImgURL() {
        return imgURL;
    } // only for viewing*/
    public void printAll()
    {
        for(int i=0;i<keyWords.size(); i++)
            Log.d("Hey", keyWords.get(i));
    }
    public void init()
    {
        keyWords.clear();
        name = "";
    }
    public String toString()
    {
        String keys = "";
        for(int i=0;i<keyWords.size();i++)
        {
            keys = keys + "," + keyWords.get(i);
        }
        return "ID: " + id  + " City: " + name + " **** Keywords: " + keys;
    }
}
