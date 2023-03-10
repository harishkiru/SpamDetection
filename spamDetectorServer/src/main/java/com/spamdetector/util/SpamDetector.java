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

        HashMap<String, Integer> spamWordCount = new HashMap<String, Integer>();
        HashMap<String, Integer> hamWordCount = new HashMap<String, Integer>();

        File spamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\spam");
        File hamFile = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham");
        File ham2File = new File("csci2020u-assignment01\\spamDetectorServer\\src\\main\\resources\\data\\train\\ham2");

//        File[] spamFiles = spamFile.listFiles();
//        File[] hamFiles = hamFile.listFiles();
//        File[] ham2Files = ham2File.listFiles();
//
//        System.out.println("Spam Files: " + spamFiles.length + " Ham Files: " + hamFiles.length + " Ham2 Files: " + ham2Files.length);

        for(int i = 0; i < spamFile.listFiles().length; i++) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(Objects.requireNonNull(spamFile.listFiles())[i]));
                String line = reader.readLine();
                String[] words;
                System.out.println(spamFile.listFiles()[i].getName() + " IM HERE KEKW");

                while((reader.readLine()) != null) {
                    words = line.split(" ");
                    int counter = 0;
                    for (String word : words) {



                        if (spamWordCount.containsKey(word)) {
                            spamWordCount.put(word, spamWordCount.get(word) + 1);
                        } else {
                            spamWordCount.put(word, 1);
                        }


                    }
                    line = reader.readLine();
                }
            }catch (Exception e) {
                System.out.println("Error >> " + e);
            }
        }
        System.out.println(spamWordCount.size());

        System.out.println();

    }

}

