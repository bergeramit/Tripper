package com.example.mytbrgr.tries;

import java.util.ArrayList;

/**
 * Created by mytbrgr on 25/05/2016.
 */

public class InitiateAll {
    private static String textFromUser;
    private static ArrayList<String> blockOfWords;

    public InitiateAll(String textFromUser){
        this.textFromUser = textFromUser;
        blockOfWords = new ArrayList<String>();
    }

    public static void breakTheText(){
        String [] seperateWordsWithDot = textFromUser.split(" ");

        for(int i=0; i<seperateWordsWithDot.length; ++i){
            while(! negativeWords(seperateWordsWithDot,i-1)){
                blockOfWords.add(seperateWordsWithDot[i]);
            }
        }
        for(int i=0 ; i < blockOfWords.size() ; ++i){
            if(blockOfWords.get(i).charAt(blockOfWords.get(i).length() -1 ) == '.'){
              blockOfWords.get(i).substring(0,blockOfWords.get(i).length() -1);      // cut last charecter
            }
        }
    }

    public static boolean negativeWords(String[] check,int index){
        int j = index;
        if(j==0)
            return false;
        while(j>=0 && check[j].charAt(check[j].length()-1) != '.'){
            if(checktheword(check[j].toLowerCase())){
                return true;
            }
            --j;
        }
        return false;
    }

    public static boolean checktheword(String s){
        return (s.equals("not") || s.equals("doesn't") || s.equals("don't")
        || s.equals("no") || s.equals("doesnt") || s.equals("dont") || s.equals("shoudnt")
        || s.equals("shouldn't") || s.equals("mustn't") || s.equals("mustnt") );
    }

}
