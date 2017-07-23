package decisionlist.dataprocess;

import org.json.simple.JSONArray;

import java.io.*;
import java.net.URISyntaxException;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class JSONWriter {

    // Debug purpose
    private boolean debugMode = true;

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void writeFeatures(JSONArray array) throws IOException, URISyntaxException {
        File file = new File(getClass().getResource("/result/featureresult.json").toURI());
        OutputStream stream = new FileOutputStream(file, false);
        stream.write(array.toJSONString().getBytes());
        stream.flush();
        stream.close();
        if (debugMode)
            System.out.println("DEBUG WRITE FILE - JSONWriter.java - writeFeatures() = SUCCESS");
    }

    public void writeUpdateTableDecision(JSONArray array) throws URISyntaxException, IOException {
        File file = new File(getClass().getResource("/result/decisiontable.json").toURI());
        OutputStream stream = new FileOutputStream(file, false);
        stream.write(array.toJSONString().getBytes());
        stream.flush();
        stream.close();
        if (debugMode)
            System.out.println("DEBUG WRITE FILE - JSONWriter.java - writeUpdateTableDecision() = SUCCESS");
    }

    public void writeTestingResult(JSONArray array) throws IOException, URISyntaxException {
        File file = new File(getClass().getResource("/result/testingresult.json").toURI());
        OutputStream stream = new FileOutputStream(file, false);
        stream.write(array.toJSONString().getBytes());
        stream.flush();
        stream.close();
        if (debugMode)
            System.out.println("DEBUG WRITE FILE - JSONWriter.java - writeTestingResult() = SUCCESS");
    }

}