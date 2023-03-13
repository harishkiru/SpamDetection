package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.util.List;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();


    public SpamResource(){
        //TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");
        this.trainAndTest();
    }

    @GET
    @Path("/")
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object
        List<TestFile> testResults = this.trainAndTest();
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity(testResults)
                .build();
    }


    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
        // return the accuracy of the detector, return in a Response object
        double accuracy = detector.getAccuracy();
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity("{\"accuracy\": " + accuracy + "}")
                .build();
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //      TODO: return the precision of the detector, return in a Response object
        double precision = detector.getPrecision();
        return Response.status(200)
                .header("Access-Control-Allow-Origin", "*")
                .entity("{\"precision\": " + precision + "}")
                .build();
    }

//    @GET
//    public String hello() {
//        return "Hello, World!";
//    }

    private List<TestFile> trainAndTest()  {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
        File mainDirectory = new File(getClass().getClassLoader().getResource("data").getFile());
        //File(getClass().getClassLoader().getResource("data").getFile());
        return this.detector.trainAndTest(mainDirectory);
    }
}