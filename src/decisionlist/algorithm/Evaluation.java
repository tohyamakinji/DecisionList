package decisionlist.algorithm;

import decisionlist.dataprocess.JSONReader;
import decisionlist.gui.main.MainProgram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class Evaluation {

    private final byte classID = 7;
    private JSONReader reader;

    public Evaluation() throws IOException, ParseException {
        reader = new JSONReader(classID);
    }

    public Evaluation(boolean debugMode) throws IOException, ParseException {
        this.reader = new JSONReader(classID);
        reader.setDebugMode(debugMode);
    }

    public String doEvaluate(String sense) throws IOException {
        String POS = sense.substring(0, sense.indexOf("-"));
        StringBuilder builder = new StringBuilder();
        JSONObject data, dataTest;
        JSONArray dataArray;
        int TN = 0, TP = 0, FN = 0, FP = 0;
        for (Object o : reader.getResultArray()) {
            data = (JSONObject) o;
            if (POS.equals(data.get("POS"))) {
                dataArray = (JSONArray) data.get("testcase");
                for (Object o1 : dataArray) {
                    dataTest = (JSONObject) o1;
                    if (dataTest.get("sense").equals(sense)) {
                        if (sense.equals(dataTest.get("prediction")))
                            TP++;
                        else FN++;
                    } else {
                        if (sense.equals(dataTest.get("prediction")))
                            FP++;
                        else TN++;
                    }
                }
                break;
            }
        }
        double precision = getPrecision(TP, FP);
        double accuracy = getAccuracy(TP, TN, FP, FN);
        builder.append("EVALUATION ON SENSE ").append(sense.toUpperCase())
                .append(":\n\nTOTAL TRUE POSITIVE : ").append(TP)
                .append("\nTOTAL TRUE NEGATIVE : ").append(TN)
                .append("\nTOTAL FALSE POSITIVE : ").append(FP)
                .append("\nTOTAL FALSE NEGATIVE : ").append(FN)
                .append("\n\nPRECISION : ").append(precision)
                .append("\nACCURACY : ").append(accuracy);
        return builder.toString();
    }

    private double getAccuracy(int tp, int tn, int fp, int fn) {
        return (double) (tp + tn) / (tp + fp + fn + tn);
    }

    private double getPrecision(int tp, int fp) {
        if (tp == 0 && fp == 0)
            return 0;
        else return (double) tp / (tp + fp);
    }

    public void doGUIEvaluate(MainProgram program) {
        EvaluationTask task = new EvaluationTask(program.getSenseBox().getSelectedItem().equals("ALL"), program);
        task.execute();
    }

    private class EvaluationTask extends SwingWorker<Void, Void> {

        boolean isAll;
        MainProgram program;

        EvaluationTask(boolean isAll, MainProgram program) {
            this.isAll = isAll;
            this.program = program;
        }

        @Override
        protected Void doInBackground() throws Exception {
            String sense;
            program.getAreaResult().setText(null);
            if (isAll) {
                for (int i = 0; i < program.getSenseBox().getItemCount(); i++) {
                    sense = program.getSenseBox().getItemAt(i).toLowerCase();
                    if (!sense.equals("all")) {
                        program.getAreaResult().append(doEvaluate(sense));
                        program.getAreaResult().append("\n\n\n");
                    } else program.getAreaResult().append("CREATED BY TOHYAMA KINJI\nFURTHER QUESTION FEEL FREE TO SEND YOUR QUESTION AT : putugitaandika@gmail.com");
                }
            } else {
                program.getAreaResult().append(doEvaluate(((String) program.getSenseBox().getSelectedItem()).toLowerCase()));
                program.getAreaResult().append("\n\n\n");
                program.getAreaResult().append("CREATED BY TOHYAMA KINJI\nFURTHER QUESTION FEEL FREE TO SEND YOUR QUESTION AT : putugitaandika@gmail.com");
            }
            clear();
            close();
            return null;
        }

        @Override
        protected void done() {
            super.done();
            program.getButtonViewResult().setEnabled(true);
        }
    }

    public void clear() {
        reader.clear();
    }

    public void close() throws IOException {
        reader.close();
    }

}