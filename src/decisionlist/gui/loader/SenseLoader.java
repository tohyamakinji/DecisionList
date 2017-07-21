package decisionlist.gui.loader;

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

public class SenseLoader {

    private static final byte classID = 8;
    private JSONReader reader;
    private MainProgram program;

    public SenseLoader(MainProgram program, boolean debugMode) throws IOException, ParseException {
        this.program = program;
        reader = new JSONReader(classID);
        reader.setDebugMode(debugMode);
    }

    public void loadSense() {
        SenseLoaderTask task = new SenseLoaderTask();
        task.execute();
    }

    private class SenseLoaderTask extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            JSONObject data, senseData;
            JSONArray senseArray;
            program.getSenseBox().removeAllItems();
            for (Object o : reader.getSenseTagArray()) {
                data = (JSONObject) o;
                senseArray = (JSONArray) data.get("sense");
                for (Object o1 : senseArray) {
                    senseData = (JSONObject) o1;
                    program.getSenseBox().addItem(((String) senseData.get("name")).toUpperCase());
                }
            }
            program.getSenseBox().addItem("ALL");
            reader.clear();
            reader.close();
            return null;
        }
    }

}