package decisionlist.analysis;

import decisionlist.algorithm.DecisionList;
import decisionlist.dataprocess.JSONReader;
import decisionlist.gui.main.MainProgram;
import decisionlist.preprocess.FeatureExtraction;
import decisionlist.preprocess.WordLemmatizer;
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

public class TrainingSet {

    private final byte classID = 1;
    private JSONReader reader;
    private WordLemmatizer lemmatizer;
    private FeatureExtraction extraction;

    // Debug purpose
    private boolean debugMode = true;

    public TrainingSet() throws IOException, ParseException, URISyntaxException {
        reader = new JSONReader(classID);
        lemmatizer = new WordLemmatizer();
        extraction = new FeatureExtraction();
    }

    public TrainingSet(boolean debugMode) throws IOException, ParseException {
        reader = new JSONReader(classID);
        reader.readerInitializer(classID);
        lemmatizer = new WordLemmatizer();
        extraction = new FeatureExtraction(reader, classID);
        setDebugMode(debugMode);
    }

    private void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        reader.setDebugMode(debugMode);
        extraction.setDebugMode(debugMode);
    }

    public void process(byte processType) throws IOException, URISyntaxException, ParseException {
        JSONObject data;
        String[] splitText;
        String POS, text, sense;
        List<String> list;
        for (Object o : reader.getTrainingArray()) {
            data = (JSONObject) o;
            POS = (String) data.get("POS");
            text = (String) data.get("text");
            sense = (String) data.get("sense");
            // Start stemming the text.
            text = text.replaceAll("\\W", " ");
            splitText = text.split("\\s+");
            for (int i=0; i < splitText.length; i++) {
                if (!reader.isForbidden(splitText[i]))
                    splitText[i] = lemmatizer.getLemmatizer().lemmatize(splitText[i]);
                else splitText[i] = splitText[i].toLowerCase();
            }
            if (debugMode) {
                System.out.println("DEBUG Training set JSON - TrainingSet.java - process() = " + "POS : " + POS + ", sense : " + sense);
                System.out.println("DEBUG stemmed text - TrainingSet.java - process() = " + Arrays.toString(splitText));
            }
            list = extraction.collocation(Arrays.asList(splitText), POS, processType);
            extraction.coOccurrence(list, POS, sense);
        }
        extraction.writeFeatures();
        extraction.clear();
        extraction.close();
        reader.clear();
        reader.close();
        DecisionList decisionList = new DecisionList();
        if (debugMode)
            decisionList.setDebugMode(debugMode);
        decisionList.updateDecisionTable();
        decisionList.clear();
        decisionList.close();
    }

    public void process(MainProgram program, String processType, int range) {
        if (debugMode)
            System.out.println("DEBUG GUI PROCESS - TrainingSet.java - process() => PROCESS TYPE = " + processType + ", RANGE = " + range);
        extraction.setRange(range);
        program.getTrainingProgressBar().setMinimum(0);
        program.getTrainingProgressBar().setMaximum(reader.getTrainingArray().size() + 10);
        TrainingTask task = new TrainingTask(program, processType);
        task.execute();
    }

    private class TrainingTask extends SwingWorker<Void, Void> {

        MainProgram program;
        byte processType;

        TrainingTask(MainProgram program, String processType) {
            this.program = program;
            this.processType = processType.equals("PARTIAL") ? (byte) 2 : (byte) 1;
        }

        @Override
        protected Void doInBackground() throws Exception {
            JSONObject data;
            String[] splitText;
            String POS, text, sense;
            List<String> list;
            int progress = 0, cData = 0;
            program.getTrainingProgressBar().setValue(progress);
            ((DefaultTableModel) program.getTableCollocation().getModel()).setRowCount(0);
            for (Object o : reader.getTrainingArray()) {
                data = (JSONObject) o;
                POS = (String) data.get("POS");
                text = (String) data.get("text");
                sense = (String) data.get("sense");
                program.getLabelTrainingProgress().setText("TEXT PROCESSED = " + text);
                // Start stemming the text.
                text = text.replaceAll("\\W", " ");
                splitText = text.split("\\s+");
                for (int i=0; i < splitText.length; i++) {
                    if (!reader.isForbidden(splitText[i]))
                        splitText[i] = lemmatizer.getLemmatizer().lemmatize(splitText[i]);
                    else splitText[i] = splitText[i].toLowerCase();
                }
                if (debugMode) {
                    System.out.println("DEBUG Training set JSON - TrainingSet.java - process() = " + "POS : " + POS + ", sense : " + sense);
                    System.out.println("DEBUG stemmed text - TrainingSet.java - process() = " + Arrays.toString(splitText));
                }
                list = extraction.collocation(Arrays.asList(splitText), POS, processType);
                if (cData <= 1000) {
                    ((DefaultTableModel) program.getTableCollocation().getModel()).addRow(new Object[]{Arrays.toString(splitText), Arrays.toString(list.toArray())});
                    cData++;
                }
                extraction.coOccurrence(list, POS, sense);
                progress += 1;
                program.getTrainingProgressBar().setValue(Math.min(progress, program.getTrainingProgressBar().getMaximum()));
            }
            program.getLabelTrainingProgress().setText("WRITING FEATURE INTO DISK");
            extraction.writeFeatures();
            progress += 5;
            program.getTrainingProgressBar().setValue(Math.min(progress, program.getTrainingProgressBar().getMaximum()));
            extraction.clear();
            extraction.close();
            reader.clear();
            reader.close();
            program.getLabelTrainingProgress().setText("WRITING DECISION TABLE INTO DISK");
            DecisionList decisionList = new DecisionList();
            decisionList.setDebugMode(debugMode);
            decisionList.updateDecisionTable();
            decisionList.clear();
            decisionList.close();
            progress += 5;
            program.getTrainingProgressBar().setValue(Math.min(progress, program.getTrainingProgressBar().getMaximum()));
            return null;
        }

        @Override
        protected void done() {
            super.done();
            program.doneTraining();
        }
    }

}