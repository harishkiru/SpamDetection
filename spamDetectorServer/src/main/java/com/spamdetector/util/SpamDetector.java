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
        Map<String, Integer> hamCount = new HashMap<>();
        Set<String> stopwords = getStopwords();

        File spamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\spam");
        File hamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham");
        File ham2File = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham2");

        for (File file : spamFile.listFiles()) {
            try (Scanner scanner = new Scanner(file)) {
                Set<String> wordsSeen = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!stopwords.contains(word) && !wordsSeen.contains(word)) {
                        wordsSeen.add(word);
                        if (spamWordCount.containsKey(word)) {
                            spamWordCount.put(word, spamWordCount.get(word) + 1);
                        } else {
                            spamWordCount.put(word, 1);
                        }
                    }
                }
            }
        }

        for (File file : hamFile.listFiles()) {
            try (Scanner scanner = new Scanner(file)) {
                Set<String> wordsSeen = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!stopwords.contains(word) && !wordsSeen.contains(word)) {
                        wordsSeen.add(word);
                        if (hamCount.containsKey(word)) {
                            hamCount.put(word, hamCount.get(word) + 1);
                        } else {
                            hamCount.put(word, 1);
                        }
                    }
                }
            }
        }

        System.out.println(spamWordCount.get("wrinkles"));

        //Print out the top 10 most common words in spam
        List<Map.Entry<String, Integer>> spam = new ArrayList<>(spamWordCount.entrySet());
        spam.sort(Map.Entry.comparingByValue());
        Collections.reverse(spam);
        for (int i = 0; i < spam.size(); i++) {
            if (spam.get(i).getValue() < 25 || spam.get(i).getValue() > 45) {
                spam.remove(i);
                i--;
            }
        }
        System.out.println("Top 10 most common words in spam:");
        for (int i = 0; i < 10; i++) {
            System.out.println(spam.get(i));
        }

        List<Map.Entry<String, Integer>> ham = new ArrayList<>(hamCount.entrySet());
        ham.sort(Map.Entry.comparingByValue());
        Collections.reverse(ham);
        //Remove words that appear less than 10 times and 100 times
        for (int i = 0; i < ham.size(); i++) {
            if (ham.get(i).getValue() < 10 || ham.get(i).getValue() > 250) {
                ham.remove(i);
                i--;
            }
        }
        System.out.println("Top 10 most common words in ham:");
        for (int i = 0; i < 10; i++) {
            System.out.println(ham.get(i));
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
