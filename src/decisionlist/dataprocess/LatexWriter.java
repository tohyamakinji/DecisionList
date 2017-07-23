package decisionlist.dataprocess;

import decisionlist.gui.main.MainProgram;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class LatexWriter {

    private byte classID;
    private MainProgram program;
    private ProgressMonitor monitor;
    private int progress;
    private boolean isBreak;

    public LatexWriter(MainProgram program) {
        this.program = program;
    }

    public void saveTableDecision(boolean debugMode) throws IOException, ParseException {
        progress = 0;
        isBreak = false;
        classID = 10;
        JSONReader reader = new JSONReader(classID);
        reader.setDebugMode(debugMode);
    }

    public void saveTableCoOccurrence(boolean debugMode) throws IOException, ParseException {
        progress = 0;
        isBreak = false;
        classID = 9;
        JSONReader reader = new JSONReader(classID);
        reader.setDebugMode(debugMode);
        monitor = new ProgressMonitor(program.getMainFrame(), "SAVING CO-OCCURRENCE FEATURE", "", 0, getTotalFeature(reader));
        OccurrenceWriterTask task = new OccurrenceWriterTask(reader);
        task.execute();
    }

    private int getTotalFeature(JSONReader reader) {
        int count = 5;
        JSONObject data, senseData, features;
        JSONArray senseArray;
        for (Object o : reader.getFeatureArray()) {
            data = (JSONObject) o;
            senseArray = (JSONArray) data.get("sense");
            for (Object o1 : senseArray) {
                senseData = (JSONObject) o1;
                features = (JSONObject) senseData.get("feature");
                count = count + features.keySet().size();
            }
        }
        return count;
    }

    private class OccurrenceWriterTask extends SwingWorker<Void, Void> {

        JSONReader reader;

        OccurrenceWriterTask(JSONReader reader) {
            this.reader = reader;
        }

        @Override
        protected Void doInBackground() throws Exception {
            monitor.setProgress(progress);
            StringBuilder builder = new StringBuilder();
            builder.append("\\begin{table}[h!]\n")
                    .append("\t\\centering\n")
                    .append("\t\\begin{tabular}{ |c|c|c|c| }\n")
                    .append("\t\t\\hline\n")
                    .append("\t\t\\textbf{POS} & \\textbf{SENSE} & \\textbf{FEATURE} & \\textbf{OCCURRENCE} \\\\\n")
                    .append("\t\t\\hline\n");
            JSONObject data, dataSense, features;
            JSONArray senseArray;
            monitor.setNote("Read data ...");
            // GET DATA
            for (Object o : reader.getFeatureArray()) {
                data = (JSONObject) o;
                senseArray = (JSONArray) data.get("sense");
                for (Object o1 : senseArray) {
                    dataSense = (JSONObject) o1;
                    features = (JSONObject) dataSense.get("feature");
                    for (Object key : features.keySet()) {
                        builder.append("\t\t").append(data.get("POS")).append(" & ")
                                .append(dataSense.get("senseID")).append(" & ")
                                .append(key).append(" & ")
                                .append(Long.toString((long) features.get(key))).append(" \\\\\n").append("\t\t\\hline\n");
                        progress += 1;
                        monitor.setProgress(Math.min(progress, monitor.getMaximum()));
                        if (monitor.isCanceled()) {
                            isBreak = true;
                            break;
                        }
                    }
                    if (isBreak)
                        break;
                }
                if (isBreak)
                    break;
            }
            if (isBreak) {
                reader.clear();
                reader.close();
                return null;
            } else {
                monitor.setNote("Saving file into disk ...");
                builder.append("\t\\end{tabular}\n")
                        .append("\t\\caption{Co-occurrence result}\n")
                        .append("\t\\label{occurrence-result}\n")
                        .append("\\end{table}");
                File file = new File(getClass().getResource("/result/features.txt").toURI());
                OutputStream stream = new FileOutputStream(file, false);
                stream.write(builder.toString().getBytes());
                stream.flush();
                stream.close();
                reader.clear();
                reader.close();
                progress += 5;
                monitor.setProgress(Math.min(progress, monitor.getMaximum()));
                return null;
            }
        }

        @Override
        protected void done() {
            super.done();
            program.doneWriting();
        }
    }

    public void saveTableCollocation() {
        progress = 0;
        isBreak = false;
        monitor = new ProgressMonitor(program.getMainFrame(), "SAVING COLLOCATION FEATURE", "", 0, program.getTableCollocation().getModel().getRowCount() + 15);
        CollocationWriterTask task = new CollocationWriterTask();
        task.execute();
    }

    private class CollocationWriterTask extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            monitor.setProgress(progress);
            int yMax = program.getTableCollocation().getModel().getRowCount(), y = 0, x = 0;
            StringBuilder builder = new StringBuilder();
            builder.append("\\begin{table}[h!]\n")
                    .append("\t\\centering\n")
                    .append("\t\\begin{tabular}{ |c|c| }\n")
                    .append("\t\t\\hline\n");
            progress += 5;
            monitor.setProgress(Math.min(progress, monitor.getMaximum()));
            monitor.setNote("Get column data ...");
            // GET COLUMN
            while (x < 2) {
                switch (x) {
                    case 0 :
                        builder.append("\t\t\\textbf{").append(program.getTableCollocation().getModel().getColumnName(x)).append("} & ");
                        break;
                    case 1 :
                        builder.append("\\textbf{").append(program.getTableCollocation().getModel().getColumnName(x)).append("} \\\\\n").append("\t\t\\hline\n");
                        break;
                }
                x++;
            }
            progress += 5;
            monitor.setProgress(Math.min(progress, monitor.getMaximum()));
            monitor.setNote("Get row data ...");
            // GET DATA
            while (y < yMax) {
                x = 0;
                while (x < 2) {
                    switch (x) {
                        case 0 :
                            builder.append("\t\t").append((String) program.getTableCollocation().getModel().getValueAt(y,x)).append(" & ");
                            break;
                        case 1 :
                            builder.append((String) program.getTableCollocation().getModel().getValueAt(y, x)).append(" \\\\\n").append("\t\t\\hline\n");
                            break;
                    }
                    x++;
                }
                progress += 1;
                monitor.setProgress(Math.min(progress, monitor.getMaximum()));
                if (monitor.isCanceled()) {
                    isBreak = true;
                    break;
                }
                y++;
            }
            // FINISH LINE
            if (isBreak) {
                return null;
            } else {
                monitor.setNote("Saving file into disk ...");
                builder.append("\t\\end{tabular}\n")
                        .append("\t\\caption{Collocation result}\n")
                        .append("\t\\label{collocation-result}\n")
                        .append("\\end{table}");
                File file = new File(getClass().getResource("/result/collocation.txt").toURI());
                OutputStream stream = new FileOutputStream(file, false);
                stream.write(builder.toString().getBytes());
                stream.flush();
                stream.close();
                progress += 5;
                monitor.setProgress(Math.min(progress, monitor.getMaximum()));
                return null;
            }
        }

        @Override
        protected void done() {
            super.done();
            program.doneWriting();
        }
    }

}