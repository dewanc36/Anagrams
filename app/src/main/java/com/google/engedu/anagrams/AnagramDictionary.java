package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static ArrayList<String> wordList = new ArrayList<String>();
    public static HashSet<String> wordSet = new HashSet<>();
    public  static HashMap<String, ArrayList<String>> letterToWord = new HashMap<String, ArrayList<String>>();
    public  static HashMap<Integer, ArrayList<String>> sizeToWord = new HashMap<Integer, ArrayList<String>>();
    private static String starterWord;
    private static String starterKey;
    private static int wordLength=DEFAULT_WORD_LENGTH;

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));

        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if(letterToWord.get(sortedWord)==null){
                letterToWord.put(sortedWord, new ArrayList<String>());
            }
            letterToWord.get(sortedWord).add(word);

            int key = word.length();
            if(sizeToWord.get(key)==null){
                sizeToWord.put(key, new ArrayList<String>());
            }
            sizeToWord.get(key).add(word);
        }
        //String sizeo = Integer.toString(sizeToWord.size());
        //Log.d("2 nd word", sizeo);



    }
    public String sortLetters(String theWord){
        char[] chars = theWord.toCharArray();
        Arrays.sort(chars);
        String newWord = new String(chars);

        return newWord;
    }


    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)){
            return false;
        }
        if(word.contains(base)){
            return false;
        }

        return true;
    }

   /* public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedLetter = sortLetters(targetWord);
        //result.add(targetWord);

        result = letterToWord.get(sortedLetter);
        ArrayList<String> listOfOneMoreLetter = getAnagramsWithOneMoreLetter(targetWord);
        result.addAll(listOfOneMoreLetter);


        return result;
    }*/



    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String key = sortLetters(word);
        //result=letterToWord.get(key);

        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(int i=0;i<26;i++){
            String longerKey=key+alphabet[i];
            String newKey=sortLetters(longerKey);
            if(letterToWord.containsKey(newKey)) {
                ArrayList<String> listForNewKey = letterToWord.get(newKey);

                for (String newWord : listForNewKey) {
                    if (isGoodWord(newWord, word)) {
                        result.add(newWord);
                    }
                }
            }
        }


        return result;
    }

    public String pickGoodStarterWord() {
       // int wordLength=DEFAULT_WORD_LENGTH;
        Random var = new Random();
        int numOfWordsOfLength = sizeToWord.get(wordLength).size();//size of the arraylist at the key
        int rand = var.nextInt(numOfWordsOfLength-1)+0;
        int wordIndex = rand;
        starterWord = sizeToWord.get(wordLength).get(wordIndex);
        starterKey = sortLetters(starterWord);
        while(starterWord.length()> wordLength || letterToWord.get(starterKey).size()<MIN_NUM_ANAGRAMS){
            if(wordIndex==numOfWordsOfLength-1){
                wordIndex=0;
            }
            wordIndex++;
            starterWord= sizeToWord.get(wordLength).get(wordIndex);
            starterKey = sortLetters(starterWord);
        }
        if(wordLength<MAX_WORD_LENGTH){
            wordLength++;
        }
        return starterWord;

    }
}
