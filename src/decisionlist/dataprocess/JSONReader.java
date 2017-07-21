package decisionlist.dataprocess;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JSONReader {

    private JSONParser parser;

    // TrainingSet.java
    private InputStreamReader dataTrainingStream, forbiddenStream;
    private JSONArray trainingArray, forbiddenArray;

    // FeatureExtraction.java
    private InputStreamReader conjunctionDictionaryStream, featureResultStream;
    private JSONArray conjunctionArray, featureArray;

    // DecisionList.java
    private InputStreamReader decisionListStream;
    private JSONArray decisionArray;

    // TestingSet.java
    private InputStreamReader dataTestingStream;
    private JSONArray testingArray;

    // Evaluation.java
    private InputStreamReader testingResultStream;
    private JSONArray resultArray;

    // SenseLoader.java
    private InputStreamReader senseTagStream;
    private JSONArray senseTagArray;

    // Debug purpose
    private boolean debugMode = true;

    public JSONReader(byte classID) throws IOException, ParseException {
        parser = new JSONParser();
        switch (classID) {
            case 1 : // TrainingSet.java
                initializeTrainingStream();
                initializeForbiddenWordStream();
                break;
            case 2 : // FeatureExtraction.java
                initializeConjunctionStream();
                initializeFeatureResultStream();
                break;
            case 3 : // DecisionList.java
                initializeFeatureResultStream();
                initializeDecisionListStream();
                break;
            case 4 : // TestingSet.java
                initializeTestingStream();
                initializeForbiddenWordStream();
                break;
            case 5 : // TableLoader.java -> CoOccurrenceLoader()
                initializeFeatureResultStream();
                break;
            case 6 : // TableLoader.java -> DecisionListLoader()
                initializeDecisionListStream();
                break;
            case 7 : // Evaluation.java
                initializeTestingResultStream();
                break;
            case 8 : // SenseLoader.java
                initializeSenseTagStream();
                break;
        }
    }

    public void readerInitializer(byte classID) throws IOException, ParseException {
        switch (classID) {
            case 1 : // TrainingSet.java
                initializeConjunctionStream();
                initializeFeatureResultStream();
                break;
            case 4 : // TestingSet.java
                initializeConjunctionStream();
                initializeDecisionListStream();
                break;
        }
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public JSONArray getTrainingArray() {
        return trainingArray;
    }

    public JSONArray getConjunctionArray() {
        return conjunctionArray;
    }

    public JSONArray getFeatureArray() {
        return featureArray;
    }

    public JSONArray getDecisionArray() {
        return decisionArray;
    }

    public JSONArray getTestingArray() {
        return testingArray;
    }

    public JSONArray getResultArray() {
        return resultArray;
    }

    public JSONArray getSenseTagArray() {
        return senseTagArray;
    }

    private void initializeSenseTagStream() throws IOException, ParseException {
        senseTagStream = new InputStreamReader(getClass().getResourceAsStream("/datalibrary/sensetag.json"));
        senseTagArray = (JSONArray) parser.parse(senseTagStream);
    }

    private void initializeTestingResultStream() throws IOException, ParseException {
        testingResultStream = new InputStreamReader(getClass().getResourceAsStream("/result/testingresult.json"));
        resultArray = (JSONArray) parser.parse(testingResultStream);
    }

    private void initializeTestingStream() throws IOException, ParseException {
        dataTestingStream = new InputStreamReader(getClass().getResourceAsStream("/testing/datatesting.json"));
        testingArray = (JSONArray) parser.parse(dataTestingStream);
    }

    private void initializeDecisionListStream() throws IOException, ParseException {
        decisionListStream = new InputStreamReader(getClass().getResourceAsStream("/result/decisiontable.json"));
        decisionArray = (JSONArray) parser.parse(decisionListStream);
    }

    private void initializeForbiddenWordStream() throws IOException, ParseException {
        forbiddenStream = new InputStreamReader(getClass().getResourceAsStream("/datalibrary/forbidden.json"));
        forbiddenArray = (JSONArray) parser.parse(forbiddenStream);
    }

    private void initializeConjunctionStream() throws IOException, ParseException {
        conjunctionDictionaryStream = new InputStreamReader(getClass().getResourceAsStream("/datalibrary/conjunction.json"));
        conjunctionArray = (JSONArray) parser.parse(conjunctionDictionaryStream);
    }

    private void initializeFeatureResultStream() throws IOException, ParseException {
        featureResultStream = new InputStreamReader(getClass().getResourceAsStream("/result/featureresult.json"));
        featureArray = (JSONArray) parser.parse(featureResultStream);
    }

    private void initializeTrainingStream() throws IOException, ParseException {
        dataTrainingStream = new InputStreamReader(getClass().getResourceAsStream("/training/datatraining.json"));
        trainingArray = (JSONArray) parser.parse(dataTrainingStream);
    }

    public boolean isForbidden(String s) {
        for (Object o : forbiddenArray) {
            if (o.toString().equalsIgnoreCase(s)) {
                if (debugMode)
                    System.out.println("DEBUG FORBIDDEN WORD - JSONReader.java - isForbidden() = " + o + " is equals with " + s);
                return true;
            }
        }
        return false;
    }

    public void close() throws IOException {
        if (dataTrainingStream != null)
            dataTrainingStream.close();
        if (forbiddenStream != null)
            forbiddenStream.close();
        if (conjunctionDictionaryStream != null)
            conjunctionDictionaryStream.close();
        if (featureResultStream != null)
            featureResultStream.close();
        if (decisionListStream != null)
            decisionListStream.close();
        if (dataTestingStream != null)
            dataTestingStream.close();
        if (testingResultStream != null)
            testingResultStream.close();
        if (senseTagStream != null)
            senseTagStream.close();
        if (debugMode)
            System.out.println("DEBUG - JSONReader.java - close() = ALL INPUT STREAM CLOSED");
    }

    public void clear() {
        if (trainingArray != null)
            trainingArray.clear();
        if (forbiddenArray != null)
            forbiddenArray.clear();
        if (conjunctionArray != null)
            conjunctionArray.clear();
        if (featureArray != null)
            featureArray.clear();
        if (decisionArray != null)
            decisionArray.clear();
        if (testingArray != null)
            testingArray.clear();
        if (resultArray != null)
            resultArray.clear();
        if (senseTagArray != null)
            senseTagArray.clear();
        if (debugMode)
            System.out.println("DEBUG - JSONReader.java - clear() = ALL JSON ARRAY CLEARED");
    }

}