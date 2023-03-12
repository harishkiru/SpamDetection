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

        //Iterate through all the tgest spam files
//        File testSpamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\test\\spam");
//        File[] testSpamFiles = testSpamFile.listFiles();
//        for(File file : testSpamFiles) {
//            Map<String, Integer> testWordSet2 = readTestFile(file, stopwords);
//            TreeMap<String, Double> probabilities2 = storeProbabilities(testWordSet2, spamWordCount, hamWordCount);
//            double n2 = getN(probabilities2);
//            System.out.println("The probability of this file being spam is: " + (getProbabilitySF(n2)));
//        }

        File testSpamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\test\\spam");
        File[] testSpamFiles = testSpamFile.listFiles();
        File testHamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\test\\ham");
        File[] testHamFiles = testHamFile.listFiles();

        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;


        int values[] = checkSpam(testSpamFile, testSpamFiles, spamWordCount, hamWordCount, stopwords, truePositives, trueNegatives, falsePositives, falseNegatives);
        int values2[] = checkHam(testHamFile, testHamFiles, spamWordCount, hamWordCount, stopwords, values[0], values[1], values[2], values[3]);

        System.out.println("True Positives: " + values2[0]);
        System.out.println("True Negatives: " + values2[1]);
        System.out.println("False Positives: " + values2[2]);
        System.out.println("False Negatives: " + values2[3]);
        //Get number of files in test/spam directory

        double accuracy = (values2[0] + values2[1]) / (double) (testSpamFiles.length + testHamFiles.length);
        System.out.println("Accuracy: " + accuracy);

        double precision = values2[0] / (double) (values2[0] + values2[2]);
        System.out.println("Precision: " + precision);

        }

        //Calculate Accuracy



    private static int[] checkSpam(File fileS, File[] files, Map<String, Integer> spamWordCount, Map<String, Integer> hamWordCount, Set<String> stopwords, int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {
        for(File tempFile : files) {
            Map<String, Integer> testWordSet2 = readTestFile(tempFile, stopwords);
            TreeMap<String, Double> probabilities2 = storeProbabilities(testWordSet2, spamWordCount, hamWordCount);
            double n2 = getN(probabilities2);
            double probability = getProbabilitySF(n2);
            System.out.println("The probability of this file being spam is: " + probability);

            if(isSpam(probability).equals("spam") && tempFile.getPath().contains("spam")) {
                truePositives++;
            } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("ham")) {
                trueNegatives++;
            } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("ham")) {
                falsePositives++;
            } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("spam")) {
                falseNegatives++;
            }

        }
        return new int[]{truePositives, trueNegatives, falsePositives, falseNegatives};
    }

    public static int[] checkHam(File fileS, File[] files, Map<String, Integer> spamWordCount, Map<String, Integer> hamWordCount, Set<String> stopwords, int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {
        for(File tempFile : files) {
            Map<String, Integer> testWordSet2 = readTestFile(tempFile, stopwords);
            TreeMap<String, Double> probabilities2 = storeProbabilities(testWordSet2, spamWordCount, hamWordCount);
            double n2 = getN(probabilities2);
            double probability = getProbabilitySF(n2);
            System.out.println("The probability of this file being ham is: " + probability);

            if(isSpam(probability).equals("ham") && tempFile.getPath().contains("ham")) {
                    truePositives++;
                } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("spam")) {
                    trueNegatives++;
                } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("spam")) {
                    falsePositives++;
                } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("ham")) {
                    falseNegatives++;
                }
            }
        return new int[]{truePositives, trueNegatives, falsePositives, falseNegatives};
    }
    private static String isSpam(double n) {
        if(n > 0.5) {
            return "spam";
        } else {
            return "ham";
        }
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