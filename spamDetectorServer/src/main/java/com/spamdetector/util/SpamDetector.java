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

        File spamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\spam");
        File hamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham");
        File ham2File = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham2");

        for (File file : spamFile.listFiles()) {
            try (Scanner scanner = new Scanner(file)) {
                Set<String> wordsSeen = new HashSet<>();
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase();
                    if (!wordsSeen.contains(word)) {
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

        System.out.println(spamWordCount.get("emails"));

    }

}
