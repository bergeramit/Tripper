package com.example.user.tripper2;

import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Trie implements Serializable
{
    private TrieNode root;
    private ArrayList<String> allWords;
    private int size;
    private String[] helpWords = {"helpinghand","accessible","disability","transportation","wheels","chair","wheelchair","wheelchairs","wheels","chairs","accessibility","disabled","disable","transportations","access","handicapped","disabilities","assistance"};
    public InputStream input;
    public POSTaggerME tagger;
    /**
     * Constructor
     */
    public Trie()
    {
        size = 1;
        root = new TrieNode();
        allWords = new ArrayList<String>();
    }

    public int getSize() {
        return size;
    }

    /**
     * Adds a word to the Trie
     * @param word
     */
    public void addWord(String word)
    {
        size += word.length();

        root.addWord(word.toLowerCase().trim());
        allWords.add(word);
    }

    //when adding new word remmember to intiate contries !!


    /**
     * Get the words in the Trie with the given
     * prefix
     * @param prefix
     * @return a List containing String objects containing the words in
     *         the Trie with the given prefix.
     */
    public boolean searchAndUpdate(String prefix) //return arr with the indexes of the cities with this key word until u see -1
    {
        //Find the node which represents the last letter of the prefix
        TrieNode lastNode = root;
        for (int i=0; i<prefix.length(); i++)
        {
            lastNode = lastNode.getNode(prefix.charAt(i));
            //If no node matches, then no words exist, return empty list
            if (lastNode == null) return false;
        }
        if(lastNode.toString().equals(prefix)){  // if we found exactly the word return the list of countries
            //add the key word to the struct
            lastNode.UpdateScore(prefix);
            return true;
        }
        return false;
        //Return the words which eminate from the last node
    }



    //adding the words
    public boolean findAndAddCityIndex(String word,int index){

        TrieNode lastNode = root;
        for (int i=0; i<word.length(); i++)
        {
            if(word.charAt(i) >= 'a' && word.charAt(i) <= 'z')
            lastNode = lastNode.getNode(word.charAt(i));
            else
            return false;
            //If no node matches, then no words exist, return empty list
            if (lastNode == null) return false;
        }
        if(lastNode.toString().equals(word)){  // if we found exactly the word return the list of countries
            //add the key word to the struct
            //Log.d("Hey", "findAndAddCityIndex: "+ index);
            lastNode.addCityToKeyWord(index); // adding the city index to the array of the word
            return true;
        }
        return true;
    }





    // every word search and print
    public void printall(){
        for(int i=0;i<allWords.size() ; ++i){
            printOneWord(allWords.get(i));
        }
    }

    // print one word to WORDS
    public void printOneWord(String word){
        TrieNode lastNode = root;
        for (int i=0; i<word.length(); i++)
        {
            lastNode = lastNode.getNode(word.charAt(i));

            //If no node matches, then no words exist, return empty list
            if (lastNode == null) return;
        }
        System.out.println(lastNode.toString());
    }


    public boolean printListOfCityIndex(String word){
        TrieNode lastNode = root;
        //Log.d("Hey", "printListOfCityIndex: FOUND!!!!!!!!!!!!!!!!!!  ::: "+word);
        for (int i=0; i<word.length(); i++)
        {
            lastNode = lastNode.getNode(word.charAt(i));
            //If no node matches, then no words exist, return empty list
            if (lastNode == null) return false;
        }
        if(lastNode.toString().equals(word)){  // if we found exactly the word return the list of countries
            //add the key word to the struct
            //Log.d("Hey", "printListOfCityIndex: FOUND!!!!!!!!!!!!!!!!!! ");
            lastNode.printListOfCitiesIndex();
            return true;
        }
        return false;
    }


    public List getWords(String prefix) // for auto complition!!!
    {
        //Find the node which represents the last letter of the prefix
        TrieNode lastNode = root;
        for (int i=0; i<prefix.length(); i++)
        {
            lastNode = lastNode.getNode(prefix.charAt(i));

            //If no node matches, then no words exist, return empty list
            if (lastNode == null) return new ArrayList();
        }

        //Return the words which eminate from the last node
        return lastNode.getWords();
    }

    public List getWords() // for auto complition!!!
    {
        return root.getWords();
    }
}

