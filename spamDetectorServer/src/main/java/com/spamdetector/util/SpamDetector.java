package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.text.DecimalFormat;
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
        //Count the number of files in ham and ham2 and add them to the total number of ham files
//        System.out.println(hamFile.listFiles().length + ham2File.listFiles().length);
//
//        //Print out the top 10 most common words in spam and insure they are alphanumeric
//
//        getCommonWords(spamWordCount, "spam");
//        getCommonWords(hamWordCount, "ham");
//
//        System.out.println(getOccurance(spamWordCount, "insurance"));
//        System.out.println(getProbabilityWS(spamWordCount, "insurance"));
//        System.out.println(getProbabilityWH(hamWordCount, "insurance"));
//
//        System.out.println(getProbability(spamWordCount, "insurance"));

        //Read the test files
        File testFile = new File("E:\\Desktop\\assspam\\csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\test\\spam\\00014.13574737e55e51fe6737a475b88b5052");
        Map<String, Integer> testWordSet = readTestFile(testFile, stopwords);
        TreeMap<String, Double> probabilities = storeProbabilities(testWordSet, spamWordCount, hamWordCount);
        double n = getN(probabilities);

        //Round the probability to 2 decimal places
        //DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("The probability of this file being spam is: " + (getProbabilitySF(n)));

        System.out.println(spamWordCount.get("half"));

    }



    public static double getProbabilitySF(double n) {
        return 1 / (1 + Math.exp(n));
    }

    private static TreeMap<String, Double> storeProbabilities(Map<String, Integer> wordSet, Map<String, Integer> spamWordCount, Map<String, Integer> hamWordCount) {
        //For each word in the wordSet, calculate the probability of it being spam
        TreeMap<String, Double> probabilityMap = new TreeMap<>();
        for(String word : wordSet.keySet()) {
            double probability = getProbability(spamWordCount, hamWordCount, word);
            probabilityMap.put(word, probability);
        }

        return probabilityMap;
    }

    private static Map<String, Integer> readTestFile(File testFile, Set<String> stopwords) {
        Set<String> wordsSeen = new HashSet<>();
        Map<String, Integer> wordSet = new HashMap<>();
        try (Scanner scanner = new Scanner(testFile)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (!stopwords.contains(word) && !wordsSeen.contains(word) && word.matches("[a-zA-Z]+")) {
                    wordsSeen.add(word);
                    if (wordSet.containsKey(word)) {
                        wordSet.put(word, wordSet.get(word) + 1);
                    } else {
                        wordSet.put(word, 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return wordSet;
    }

    //Pass a TreeMap
    private static double getN(TreeMap<String, Double> probabilityMap) {
        double sum = 0;
        for (double prSW : probabilityMap.values()) {
            sum += Math.log(1 - prSW) - Math.log(prSW);
        }
        return sum;
    }
    private static double getProbability(Map<String, Integer> spamWordCount, Map<String,Integer> hamWordCount, String word) {
        System.out.println(getProbabilityWS(spamWordCount, word) / (getProbabilityWS(spamWordCount, word) + getProbabilityWH(hamWordCount, word)));
        return getProbabilityWS(spamWordCount, word) / (getProbabilityWS(spamWordCount, word) + getProbabilityWH(hamWordCount, word));
    }

    private static double getProbabilityWS(Map<String, Integer> spamWordCount, String word) {

        return getOccurance(spamWordCount, word) / 501.0;
    }

    private static double getProbabilityWH(Map<String, Integer> hamWordCount, String word) {

        return getOccurance(hamWordCount, word) / 2752.0;
    }

    private static double getOccurance(Map<String, Integer> set, String word) {
        //Get the number of times a word occurs in a set
        try {
            return set.get(word);
        } catch (NullPointerException e) {
            return 1;
        }

    }

    private static Map<String, Integer> parseWords(Map<String, Integer> wordSet, File file, Set<String> stopwords) {

        for (File words : file.listFiles()) {
            try (Scanner scanner = new Scanner(words)) {
                Set<String> wordsSeen = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!stopwords.contains(word) && !wordsSeen.contains(word) && word.matches("[a-zA-Z]+")) {
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