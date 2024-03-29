package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;

/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */
public class SpamDetector {
    static double accuracy;
    static double precision;

    public List<TestFile> trainAndTest(File mainDirectory) {
//      ArrayList of TestFile objects to store the results of the test
        ArrayList<TestFile> testResults = new ArrayList<>();
        //store the words and their counts in a map
        Map<String, Integer> spamWordCount = new HashMap<>();
        Map<String, Integer> hamWordCount = new HashMap<>();
        Set<String> stopwords = getStopwords();
        //get the files
        File spamFile = new File(mainDirectory.getAbsolutePath()+"\\train\\spam");
        File hamFile = new File(mainDirectory.getAbsolutePath()+"\\train\\ham");
        File ham2File = new File(mainDirectory.getAbsolutePath()+"\\train\\ham2");
        //parse the files
        spamWordCount = parseWords(spamWordCount, spamFile, stopwords);
        hamWordCount = parseWords(hamWordCount, hamFile, stopwords);
        hamWordCount.putAll(parseWords(hamWordCount, ham2File, stopwords));
        //get the test files
        File testSpamFile = new File(mainDirectory.getAbsolutePath()+"\\test\\spam");
        File[] testSpamFiles = testSpamFile.listFiles();
        File testHamFile = new File(mainDirectory.getAbsolutePath()+"\\test\\ham");
        File[] testHamFiles = testHamFile.listFiles();

        int truePositives = 0;
        int trueNegatives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;


        //status of the file
        String status;
        //test the spam files
        for(File tempFile : testSpamFiles) {
            Map<String, Integer> testWordSet2 = readTestFile(tempFile, stopwords);
            TreeMap<String, Double> probabilities2 = storeProbabilities(testWordSet2, spamWordCount, hamWordCount);
            double n2 = getN(probabilities2);
            double probability = getProbabilitySF(n2);
            //System.out.println("The probability of this file being spam is: " + probability);
            status = checkSpam(tempFile, probability, truePositives, trueNegatives, falsePositives, falseNegatives);
            testResults.add(new TestFile(tempFile.getName(), probability, "spam"));
            switch (status) {
                case "truePositives":
                    truePositives++;
                    break;
                case "trueNegatives":
                    trueNegatives++;
                    break;
                case "falsePositives":
                    falsePositives++;
                    break;
                case "falseNegatives":
                    falseNegatives++;
                    break;
            }
        }
        //test the ham files
        for(File tempFile : testHamFiles) {
            Map<String, Integer> testWordSet2 = readTestFile(tempFile, stopwords);
            TreeMap<String, Double> probabilities2 = storeProbabilities(testWordSet2, spamWordCount, hamWordCount);
            double n2 = getN(probabilities2);
            double probability = getProbabilitySF(n2);
            //System.out.println("The probability of this file being ham is: " + probability);
            status = checkHam(tempFile, probability, truePositives, trueNegatives, falsePositives, falseNegatives);
            testResults.add(new TestFile(tempFile.getName(), probability, "ham"));
            switch (status) {
                case "truePositives":
                    truePositives++;
                    break;
                case "trueNegatives":
                    trueNegatives++;
                    break;
                case "falsePositives":
                    falsePositives++;
                    break;
                case "falseNegatives":
                    falseNegatives++;
                    break;
            }
        }
        //calculate accuracy and precision
        setAccuracy(truePositives, trueNegatives, testSpamFiles.length + testHamFiles.length);
        setPrecision(truePositives, falsePositives);

        return testResults;
    }

    //get Accuracy
    public static double getAccuracy() {
        return accuracy;
    }
    //get Precision
    public static double getPrecision() {
        return precision;
    }

    //calculate accuracy
    private static void setAccuracy(int truePositives, int trueNegatives, int numTestFiles) {
        accuracy = (truePositives + trueNegatives) / (double) numTestFiles;
    }
    //calculate precision
    private static void setPrecision(int truePositives, int falsePositives) {
        precision = truePositives / (double) (truePositives + falsePositives);
    }

    //check if the file is spam or ham
    private String checkSpam(File tempFile, double probability, int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {

        if(isSpam(probability).equals("spam") && tempFile.getPath().contains("spam")) {
            return "truePositives";
        } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("ham")) {
            return "trueNegatives";
        } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("ham")) {
            return "falsePositives";
        } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("spam")) {
            return "falseNegatives";
        }

        return "KEKW U BROKE IT LUL OMEGALUL GIGACHAD KEKW KEKW CAN I GET A POGGERS IN THE CHAT CUZ HOW U MAKE THIS NOT WORK DAWG";
    }
    //check if the file is spam or ham
    public static String checkHam(File tempFile, double probability, int truePositives, int trueNegatives, int falsePositives, int falseNegatives) {


        if(isSpam(probability).equals("ham") && tempFile.getPath().contains("ham")) {
            return "truePositives";
        } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("spam")) {
            return "trueNegatives";
        } else if(isSpam(probability).equals("ham") && tempFile.getPath().contains("spam")) {
            return "falsePositives";
        } else if(isSpam(probability).equals("spam") && tempFile.getPath().contains("ham")) {
            return "falseNegatives";
        }

        return "KEKW U BROKE IT LUL OMEGALUL GIGACHAD KEKW KEKW CAN I GET A POGGERS IN THE CHAT CUZ HOW U MAKE THIS NOT WORK DAWG";
    }
    //returns the probability of the file being spam
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
    //get the probability map
    private static TreeMap<String, Double> storeProbabilities(Map<String, Integer> wordSet, Map<String, Integer> spamWordCount, Map<String, Integer> hamWordCount) {
        //For each word in the wordSet, calculate the probability of it being spam
        TreeMap<String, Double> probabilityMap = new TreeMap<>();
        for(String word : wordSet.keySet()) {
            double probability = getProbability(spamWordCount, hamWordCount, word);
            probabilityMap.put(word, probability);
        }

        return probabilityMap;
    }
    //read the test file and return a map of the words and their counts
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
        //For each word in the wordSet, calculate the probability of it being spam
        for (double prSW : probabilityMap.values()) {
            sum += Math.log(1 - prSW) - Math.log(prSW);
        }
        return sum;
    }
    //get the probability of a word being spam
    private static double getProbability(Map<String, Integer> spamWordCount, Map<String,Integer> hamWordCount, String word) {
        return getProbabilityWS(spamWordCount, word) / (getProbabilityWS(spamWordCount, word) + getProbabilityWH(hamWordCount, word));
    }

    private static double getProbabilityWS(Map<String, Integer> spamWordCount, String word) {

        return getOccurance(spamWordCount, word) / 501.0;
    }

    private static double getProbabilityWH(Map<String, Integer> hamWordCount, String word) {

        return getOccurance(hamWordCount, word) / 2752.0;
    }
    //get the number of times a word occurs in a set
    private static double getOccurance(Map<String, Integer> set, String word) {
        //Get the number of times a word occurs in a set
        try {
            return set.get(word);
        } catch (NullPointerException e) {
            return 1;
        }

    }
    //this method reads the files and returns a map of the words and their counts
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
    //checks the occurances of multiple words in a file and only returns the words that are in the range given
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
    //displays the common words
    private static void displayCommonWords(List<Map.Entry<String, Integer>> words, String dataType) {
        //System.out.println("Top 10 most common words in " + dataType + ":");
        for (int i = 0; i < 10; i++) {
            //System.out.println(words.get(i));
        }
    }
    //get stopwords
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