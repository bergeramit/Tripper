package com.example.user.tripper2;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TrieNode implements Serializable
{
    private TrieNode parent;
    private ArrayList<Integer> citiesIndex; // city indexes** = for every key word the cities which have this key word
    private TrieNode[] children;
    private boolean isLeaf;     //Quick way to check if any children exist
    private boolean isWord;     //Does this node represent the last character of a word
    private char character;     //The character this node represents

    /**
     * Constructor for top level root node.
     */
    public TrieNode()
    {
        children = new TrieNode[26];
        isLeaf = true;
        isWord = false;
        citiesIndex = new ArrayList<Integer>();
    }

    /**
     * Constructor for child node.
     */
    public TrieNode(char character)
    {
        this();
        this.character = character;
    }

    /**
     * Adds a word to this node. This method is called recursively and
     * adds child nodes for each successive letter in the word, therefore
     * recursive calls will be made with partial words.
     * @param word the word to add
     */
    protected void addWord(String word)
    {
        isLeaf = false;

        int charPos = word.charAt(0) - 'a';
        if(charPos < 0 || charPos > 26) {
            return ;
        }

        if (children[charPos] == null)
        {
            children[charPos] = new TrieNode(word.charAt(0));
            children[charPos].parent = this;
        }

        if (word.length() > 1)
        {
            children[charPos].addWord(word.substring(1));
        }
        else {
            children[charPos].isWord = true;
        }
    }

    /**
     * Returns the child TrieNode representing the given char,
     * or null if no node exists.
     * @param c
     * @return
     */
    protected TrieNode getNode(char c) {
        return children[c - 'a'];
    }


    //adding index of city with this key word
    protected boolean addCityToKeyWord(int c_index){
        for(int i=0;i<citiesIndex.size();i++){
            if(citiesIndex.get(i) == c_index)
                return true;
        }
        citiesIndex.add(c_index);
        //Log.d("Hey", "index!!!!!!!!!!!!! "+c_index);
        return true;
    }


    //array which have index of the cities with this key word
    protected ArrayList<Integer> getCitiesArray(){
        return citiesIndex;
    }

    public boolean UpdateScore(String keyWord){
        //adding for every city call the set key to array list in citywithkey
        CitiesCounter save = CitiesCounter.getInstance();
       for(int i=0; i<citiesIndex.size();++i){
            //save.addKeyToCity(keyWord,cities[i]);
            save.addScore(citiesIndex.get(i));
        }

        return true;
    }


    /**
     * Returns a List of String objects which are lower in the
     * hierarchy that this node.
     * @return
     */
    protected List getWords()
    {
        //Create a list to return
        List list = new ArrayList();

        //If this node represents a word, add it
        if (isWord)
        {
            list.add(toString());
            //Log.d("Hey", "getWords: "+toString());
        }

        //If any children
        if (!isLeaf)
        {
            //Add any words belonging to any children
            for (int i=0; i<children.length; ++i)
            {
                if (children[i] != null)
                {
                    list.addAll(children[i].getWords());

                }

            }

        }

        return list;

    }



    public void printListOfCitiesIndex(){
        for(int i=0;i<citiesIndex.size();++i){
            Log.d("HeyAmit", Integer.toString(citiesIndex.get(i)));
        }
    }



    /**

     * Gets the String that this node represents.

     * For example, if this node represents the character t, whose parent

     * represents the charater a, whose parent represents the character

     * c, then the String would be "cat".

     * @return

     */

    public String toString()

    {

        if (parent == null)

        {

            return "";

        }

        else

        {

            return parent.toString() + new String(new char[] {character});

        }

    }

}