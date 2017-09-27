package decisionlist.gui.main;

import decisionlist.algorithm.DecisionList;
import decisionlist.algorithm.Evaluation;
import decisionlist.analysis.TestingSet;
import decisionlist.analysis.TrainingSet;
import decisionlist.preprocess.FeatureExtraction;
import jsastrawi.morphology.DefaultLemmatizer;
import jsastrawi.morphology.Lemmatizer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class DecisionListNonGUI {

    private DecisionListNonGUI(byte testID) throws Exception {
        switch (testID) {
            case 1 :
                TrainingTesting();
                break;
            case 2 :
                FeatureExtractionTest();
                break;
            case 3 :
                DecisionListTest();
                break;
            case 4 :
                LogBaseTwoTest();
                break;
            case 5 :
                DisambiguateTest();
                break;
            case 6 :
                StemmingTest();
                break;
            case 7 :
                EvaluationTest();
                break;
        }
    }

    private void EvaluationTest() throws Exception {
        Evaluation evaluation = new Evaluation();
        System.out.println(evaluation.doEvaluate("lapangan-3"));
        System.out.println(evaluation.doEvaluate("all"));
        evaluation.clear();
        evaluation.close();
    }

    private void StemmingTest() throws Exception {
        /* STEMMING TESTING */
        Set<String> dictionary = new HashSet<>();
        InputStream inputStream = Lemmatizer.class.getResourceAsStream("/root-words.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            dictionary.add(line);
        }
        Lemmatizer lemmatizer = new DefaultLemmatizer(dictionary);
        System.out.println(lemmatizer.lemmatize("berbulan"));
        bufferedReader.close();
        inputStream.close();
        dictionary.clear();
    }

    private void DisambiguateTest() throws Exception {
        /* DISAMBIGUATE TESTING */
        String[] strings1 = {"benar", "tiga", "empat", "pundak", "lembaga", "panggul"};
        List<String> list1 = Arrays.asList(strings1);
        DecisionList decisionList = new DecisionList();
        System.out.println(decisionList.disambiguate(list1, "bintang"));
    }

    private void LogBaseTwoTest() {
        /* LOG BASE 2 TESTING */
        double result = Math.log(2135) / Math.log(2);
        System.out.println(result);
    }

    private void DecisionListTest() throws Exception {
        /* DECISION LIST TABLE TESTING */
        DecisionList decisionList = new DecisionList();
        decisionList.updateDecisionTable();
        decisionList.clear();
        decisionList.close();
    }

    private void FeatureExtractionTest() throws Exception {
        /* FEATURE EXTRACTION CO-OCCURRENCE */
        String[] strings = {"panggul", "tiga", "empat", "polri", "didik", "pundak", "lembaga", "kepala", "mantan"};
        String[] strings1 = {"benar", "tiga", "empat", "pundak", "lembaga", "panggul"};
        List<String> list = Arrays.asList(strings);
        List<String> list1 = Arrays.asList(strings1);
        FeatureExtraction extraction = new FeatureExtraction();
        extraction.coOccurrence(list, "bintang","bintang-1");
        extraction.coOccurrence(list1, "bintang","bintang-3");
        extraction.writeFeatures();
        extraction.clear();
        extraction.close();
    }

    private void TrainingTesting() throws Exception {
        /* TRAINING SET CLASS TESTING */
        TrainingSet set = new TrainingSet();
        set.process((byte) 1);
        TestingSet testingSet = new TestingSet();
        testingSet.process((byte) 1);
    }

    public static void main(String[] args) {
        try {
            new DecisionListNonGUI((byte) 6);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}