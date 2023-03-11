package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) {
//        TODO: main method of loading the directories and files, training and testing the model


        return new ArrayList<TestFile>();
    }

    public static void main(String[] args) throws FileNotFoundException {

        Map<String, Integer> spamWordCount = new HashMap<>();
        Map<String, Integer> hamWordCount = new HashMap<>();
        Set<String> stopwords = getStopwords();

        File spamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\spam");
        File hamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham");
        File ham2File = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham2");

        spamWordCount = parseWords(spamWordCount, spamFile, stopwords);
        hamWordCount = parseWords(hamWordCount, hamFile, stopwords);
        hamWordCount.putAll(parseWords(hamWordCount, ham2File, stopwords));




        //Print out the top 10 most common words in spam and insure they are alphanumeric

        getCommonWords(spamWordCount, "spam");
        getCommonWords(hamWordCount, "ham");

        System.out.println(getOccurance(spamWordCount, "microsoft"));


    }

    private static int getOccurance(Map<String, Integer> set, String word) {
        return set.get(word);
    }

    private static Map<String, Integer> parseWords(Map<String, Integer> wordSet, File file, Set<String> stopwords) {

        for (File words : file.listFiles()) {
            try (Scanner scanner = new Scanner(words)) {
                Set<String> wordsSeen = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!stopwords.contains(word) && !wordsSeen.contains(word)) {
                        wordsSeen.add(word);
                        if (wordSet.containsKey(word)) {
                            wordSet.put(word, wordSet.get(word) + 1);
                        } else {
                            wordSet.put(word, 1);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.err.println("ERROR >> File not found");
            }
        }
        return wordSet;
    }

    private static void getCommonWords(Map<String, Integer> wordSet, String dataType) {
        List<Map.Entry<String, Integer>> words = new ArrayList<>(wordSet.entrySet());
        words.sort(Map.Entry.comparingByValue());
        Collections.reverse(words);
        //Remove non-alphanumeric words
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getValue() < 25 || words.get(i).getValue() > 500 || !words.get(i).getKey().matches("[a-zA-Z0-9]+")) {
                words.remove(i);
                i--;
            }
        }
        displayCommonWords(words, dataType);
    }

    private static void displayCommonWords(List<Map.Entry<String, Integer>> words, String dataType) {
        System.out.println("Top 10 most common words in " + dataType + ":");
        for (int i = 0; i < 10; i++) {
            System.out.println(words.get(i));
        }
    }

    private static Set<String> getStopwords() {
        // Use a library to get a list of stopwords
        Set<String> stopwords = new HashSet<>();
        try (Scanner scanner = new Scanner(new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\stopword.txt"))) {
            while (scanner.hasNext()) {
                stopwords.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Stopword list not found");
        }
        return stopwords;
    }

}