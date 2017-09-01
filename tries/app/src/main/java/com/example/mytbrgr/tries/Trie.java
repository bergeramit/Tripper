package com.example.mytbrgr.tries;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Trie
{
    private TrieNode root;
    private ArrayList<String> allWords;

    /**
     * Constructor
     */
    public Trie()
    {
        root = new TrieNode();
        allWords = new ArrayList<String>();
    }

    /**
     * Adds a word to the Trie
     * @param word
     */
    public void addWord(String word)
    {
        root.addWord(word.toLowerCase());
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
       Log.d("WORDS",lastNode.toString());
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
}