package com.example.mytbrgr.tries;

/**
 * Created by mytbrgr on 25/05/2016.
 */
import java.util.ArrayList;
import java.util.List;


public class CityWithKeys {
       private ArrayList<String> keyWords;
       private String name;
       private String imgURL;
        public CityWithKeys(){
            name = "no set yet";
        }

    public void setName(String name) {
        this.name = name;
    } // on initiation

    public String getName() {
        return name;
    } // for view

    public void addKeyWord(String key) {
        keyWords.add(key);
    } // only in singleton initiate

    public ArrayList<String> getKeyWords() {
        return keyWords;
    } // only for viewing

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    } //only in singleton initiate

    public String getImgURL() {
        return imgURL;
    } // only for viewing
}
