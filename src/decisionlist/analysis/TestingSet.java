package decisionlist.analysis;

import decisionlist.algorithm.DecisionList;
import decisionlist.dataprocess.JSONReader;
import decisionlist.dataprocess.JSONWriter;
import decisionlist.gui.main.MainProgram;
import decisionlist.preprocess.FeatureExtraction;
import decisionlist.preprocess.WordLemmatizer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class TestingSet {

    private final byte classID = 4;
    private JSONReader reader;
    private JSONWriter writer;
    private WordLemmatizer lemmatizer;
    private FeatureExtraction extraction;
    private DecisionList decisionList;

    // Debug purpose
    private boolean debugMode = true;

    private void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        reader.setDebugMode(debugMode);
        extraction.setDebugMode(debugMode);
        decisionList.setDebugMode(debugMode);
        writer.setDebugMode(debugMode);
    }

    public TestingSet() throws IOException, ParseException, URISyntaxException {
        reader = new JSONReader(classID);
        lemmatizer = new WordLemmatizer();
        extraction = new FeatureExtraction();
        decisionList = new DecisionList();
        writer = new JSONWriter();
    }

    public TestingSet(boolean debugMode) throws IOException, ParseException {
        reader = new JSONReader(classID);
        reader.readerInitializer(classID);
        lemmatizer = new WordLemmatizer();
        extraction = new FeatureExtraction(reader, classID);
        decisionList = new DecisionList(reader);
        writer = new JSONWriter();
        setDebugMode(debugMode);
    }

    @SuppressWarnings("unchecked")
    public void process(byte processType) throws IOException, URISyntaxException {
        JSONObject data, dataTesting;
        JSONArray testCaseArray;
        String POS, text, prediction;
        String[] splitText;
        List<String> list;
        for (Object o : reader.getTestingArray()) {
            data = (JSONObject) o;
            POS = (String) data.get("POS");
            testCaseArray = (JSONArray) data.get("testcase");
            for (Object o1 : testCaseArray) {
                dataTesting = (JSONObject) o1;
                text = (String) dataTesting.get("text");
                // Start stemming the text.
                text = text.replaceAll("\\W", " ");
                splitText = text.split("\\s+");
                for (int i=0; i < splitText.length; i++) {
                    if (!reader.isForbidden(splitText[i]))
                        splitText[i] = lemmatizer.getLemmatizer().lemmatize(splitText[i]);
                    else splitText[i] = splitText[i].toLowerCase();
                }
                list = extraction.collocation(Arrays.asList(splitText), POS, processType);
                prediction = decisionList.disambiguate(list, POS);
                if (prediction != null)
                    dataTesting.put("prediction", prediction);
                else dataTesting.put("prediction", "UNPREDICTABLE");
                if (debugMode) {
                    System.out.println("DEBUG Training set JSON - TestingSet.java - process() = " + "POS : " + POS);
                    System.out.println("DEBUG stemmed text - TestingSet.java - process() = " + Arrays.toString(splitText));
                    System.out.println("DEBUG Sense predict - TestingSet.java - process() = " + prediction);
                }
            }
        }
        writer.writeTestingResult(reader.getTestingArray());
        extraction.clear();
        extraction.close();
        decisionList.clear();
        decisionList.close();
        reader.clear();
        reader.close();
    }

    public void process(MainProgram program, String processType, int range) {
        if (debugMode)
            System.out.println("DEBUG GUI PROCESS - TrainingSet.java - process() => PROCESS TYPE = " + processType + ", RANGE = " + range);
        extraction.setRange(range);
        program.getLabelTestingProgress().setText("COUNT TOTAL TESTING DATA");
        program.getTestingProgressBar().setMinimum(0);
        program.getTestingProgressBar().setMaximum(getTotalData());
        TestingTask task = new TestingTask(program, processType);
        task.execute();
    }

    private int getTotalData() {
        int count = 5;
        JSONObject data;
        JSONArray testArray;
        for (Object o : reader.getTestingArray()) {
            data = (JSONObject) o;
            testArray = (JSONArray) data.get("testcase");
            count = count + testArray.size();
        }
        return count;
    }

    private class TestingTask extends SwingWorker<Void, Void> {

        MainProgram program;
        byte processType;

        TestingTask(MainProgram program, String processType) {
            this.program = program;
            this.processType = processType.equals("PARTIAL") ? (byte) 2 : (byte) 1;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Void doInBackground() throws Exception {
            JSONObject data, dataTesting;
            JSONArray testCaseArray;
            String POS, text, prediction;
            String[] splitText;
            List<String> list;
            int progress = 0, cData;
            program.getTestingProgressBar().setValue(progress);
            ((DefaultTableModel) program.getTableTestingResult().getModel()).setRowCount(0);
            for (Object o : reader.getTestingArray()) {
                data = (JSONObject) o;
                POS = (String) data.get("POS");
                testCaseArray = (JSONArray) data.get("testcase");
                cData = 0;
                for (Object o1 : testCaseArray) {
                    dataTesting = (JSONObject) o1;
                    text = (String) dataTesting.get("text");
                    program.getLabelTestingProgress().setText("TEXT PROCESSED = " + text);
                    // Start stemming the text.
                    text = text.replaceAll("\\W", " ");
                    splitText = text.split("\\s+");
                    for (int i=0; i < splitText.length; i++) {
                        if (!reader.isForbidden(splitText[i]))
                            splitText[i] = lemmatizer.getLemmatizer().lemmatize(splitText[i]);
                        else splitText[i] = splitText[i].toLowerCase();
                    }
                    list = extraction.collocation(Arrays.asList(splitText), POS, processType);
                    prediction = decisionList.disambiguate(list, POS);
                    if (prediction != null) {
                        dataTesting.put("prediction", prediction);
                        if (cData <= 200) {
                            ((DefaultTableModel) program.getTableTestingResult().getModel()).addRow(new Object[]{POS, dataTesting.get("text"), Arrays.toString(list.toArray()), dataTesting.get("sense"), prediction});
                            cData++;
                        }
                    } else {
                        dataTesting.put("prediction", "UNPREDICTABLE");
                        if (cData <= 200) {
                            ((DefaultTableModel) program.getTableTestingResult().getModel()).addRow(new Object[]{POS, dataTesting.get("text"), Arrays.toString(list.toArray()), dataTesting.get("sense"), "UNPREDICTABLE"});
                            cData++;
                        }
                    }
                    if (debugMode) {
                        System.out.println("DEBUG Training set JSON - TestingSet.java - process() = " + "POS : " + POS);
                        System.out.println("DEBUG stemmed text - TestingSet.java - process() = " + Arrays.toString(splitText));
                        System.out.println("DEBUG Sense predict - TestingSet.java - process() = " + prediction);
                    }
                    progress += 1;
                    program.getTestingProgressBar().setValue(Math.min(progress, program.getTestingProgressBar().getMaximum()));
                }
            }
            program.getLabelTestingProgress().setText("WRITING TESTING RESULT INTO DISK");
            writer.writeTestingResult(reader.getTestingArray());
            extraction.clear();
            extraction.close();
            decisionList.clear();
            decisionList.close();
            reader.clear();
            reader.close();
            progress += 5;
            program.getTestingProgressBar().setValue(Math.min(progress, program.getTestingProgressBar().getMaximum()));
            return null;
        }

        @Override
        protected void done() {
            super.done();
            program.doneTesting();
        }
    }

}