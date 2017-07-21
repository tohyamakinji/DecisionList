package decisionlist.gui.loader;

import decisionlist.dataprocess.JSONReader;
import decisionlist.gui.main.MainProgram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class TableLoader {

    // Debug purpose
    private boolean debugMode = true;

    public TableLoader(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void decisionListTableLoader(MainProgram program) {
        DecisionListLoader loader = new DecisionListLoader(program);
        loader.execute();
    }

    private class DecisionListLoader extends SwingWorker<Void, Void> {

        private final byte classID = 6;
        private MainProgram program;

        DecisionListLoader(MainProgram program) {
            this.program = program;
        }

        @Override
        protected Void doInBackground() throws Exception {
            JSONReader reader = new JSONReader(classID);
            reader.setDebugMode(debugMode);
            JSONObject data, weightObject;
            JSONArray weightArray;
            String POS;
            int cData;
            ((DefaultTableModel) program.getTableDecisionList().getModel()).setRowCount(0);
            for (Object o : reader.getDecisionArray()) {
                data = (JSONObject) o;
                POS = (String) data.get("POS");
                weightArray = (JSONArray) data.get("weight");
                cData = 0;
                for (Object o1 : weightArray) {
                    weightObject = (JSONObject) o1;
                    if (cData <= 200) {
                        ((DefaultTableModel) program.getTableDecisionList().getModel()).addRow(new Object[]{POS, weightObject.get("collocation"), weightObject.get("sense"), Double.toString((Double) weightObject.get("LogL"))});
                        cData++;
                    } else break;
                }
            }
            reader.clear();
            reader.close();
            return null;
        }
    }

    public void coOccurrenceTableLoader(MainProgram program) {
        CoOccurrenceLoader loaderTask = new CoOccurrenceLoader(program);
        loaderTask.execute();
    }

    private class CoOccurrenceLoader extends SwingWorker<Void, Void> {

        private final byte classID = 5;
        MainProgram program;

        CoOccurrenceLoader(MainProgram program) {
            this.program = program;
        }

        @Override
        protected Void doInBackground() throws Exception {
            JSONReader reader = new JSONReader(classID);
            reader.setDebugMode(debugMode);
            JSONObject data, senseObject, features;
            JSONArray senseArray;
            String POS, SENSE;
            int cData;
            ((DefaultTableModel) program.getTableCoOccurrence().getModel()).setRowCount(0);
            for (Object o : reader.getFeatureArray()) {
                data = (JSONObject) o;
                POS = (String) data.get("POS");
                senseArray = (JSONArray) data.get("sense");
                for (Object o1 : senseArray) {
                    senseObject = (JSONObject) o1;
                    SENSE = (String) senseObject.get("senseID");
                    features = (JSONObject) senseObject.get("feature");
                    cData = 0;
                    for (Object key : features.keySet()) {
                        if (cData <= 100) {
                            setProgress(1);
                            ((DefaultTableModel) program.getTableCoOccurrence().getModel()).addRow(new Object[]{POS, SENSE, key, Long.toString((long) features.get(key))});
                            cData++;
                        } else break;
                    }
                }
            }
            reader.clear();
            reader.close();
            return null;
        }
    }

}