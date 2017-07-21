package decisionlist.preprocess;

import decisionlist.dataprocess.JSONReader;
import decisionlist.dataprocess.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class FeatureExtraction {

    private static final byte classID = 2;
    private List<String> wordBag;
    private int range = 3;
    private List<Integer> posLocation;
    private JSONReader reader;
    private JSONWriter writer;

    // Debug purpose
    private boolean debugMode = true;

    public FeatureExtraction() throws IOException, ParseException, URISyntaxException {
        wordBag = new ArrayList<>();
        posLocation = new ArrayList<>();
        reader = new JSONReader(classID);
        writer = new JSONWriter();
    }

    public FeatureExtraction(JSONReader reader, byte classID) {
        switch (classID) {
            case 1 : // TrainingSet.java
                this.reader = reader;
                wordBag = new ArrayList<>();
                posLocation = new ArrayList<>();
                writer = new JSONWriter();
                break;
            case 4 : // TestingSet.java
                this.reader = reader;
                wordBag = new ArrayList<>();
                posLocation = new ArrayList<>();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void coOccurrence(List<String> bagOfWords, String POS, String sense) {
        JSONObject data, objectSense, feature;
        JSONArray arraySense;
        boolean isNewSense = true, isNewPOS = true;
        for (Object o : reader.getFeatureArray()) {
            data = (JSONObject) o;
            if (data.get("POS").equals(POS)) {
                isNewPOS = false;
                arraySense = (JSONArray) data.get("sense");
                for (Object o1 : arraySense) {
                    objectSense = (JSONObject) o1;
                    if (objectSense.get("senseID").equals(sense)) {
                        isNewSense = false;
                        feature = (JSONObject) objectSense.get("feature");
                        for (String word : bagOfWords) {
                            if (feature.containsKey(word))
                                feature.put(word, (long) feature.get(word) + 1);
                            else feature.put(word, (long) 1);
                            objectSense.put("total", (long) objectSense.get("total") + 1);
                        }
                        break;
                    }
                }
                if (isNewSense) {
                    JSONObject newObjectSense = new JSONObject(), newFeature = new JSONObject();
                    newObjectSense.put("senseID", sense);
                    newObjectSense.put("total", (long) 0);
                    for (String word : bagOfWords) {
                        newFeature.put(word, (long) 1);
                        newObjectSense.put("total", (long) newObjectSense.get("total") + 1);
                    }
                    newObjectSense.put("feature", newFeature);
                    arraySense.add(newObjectSense);
                }
                break;
            }
        }
        if (isNewPOS) {
            JSONObject newPOS = new JSONObject(), newObjectSense = new JSONObject(), newFeature = new JSONObject();
            JSONArray newArraySense = new JSONArray();
            newPOS.put("POS", POS);
            newObjectSense.put("senseID", sense);
            newObjectSense.put("total", (long) 0);
            for (String word : bagOfWords) {
                newFeature.put(word, (long) 1);
                newObjectSense.put("total", (long) newObjectSense.get("total") + 1);
            }
            newObjectSense.put("feature", newFeature);
            newArraySense.add(newObjectSense);
            newPOS.put("sense", newArraySense);
            reader.getFeatureArray().add(newPOS);
        }
    }

    public void writeFeatures() throws IOException, URISyntaxException {
        writer.writeFeatures(reader.getFeatureArray());
    }

    public List<String> collocation(List<String> splittedText, String POS, byte processType) {
        wordBag.clear();
        posLocation.clear();
        switch (processType) {
            case 1 :
                thoroughProcess(splittedText, POS);
                break;
            case 2 :
                partialProcess(splittedText, POS);
                break;
        }
        return wordBag;
    }

    private void partialProcess(List<String> splittedText, String pos) {
        int i = splittedText.indexOf(pos), x = i - 1, y = i + 1, findX = 0, findY = 0;
        boolean isFinishLeft = false, isFinishRight = false;
        while (true) {
            if (x > -1) {
                if (!wordBag.contains(splittedText.get(x))) {
                    if (!isConjunction(splittedText.get(x))) {
                        wordBag.add(splittedText.get(x));
                        findX++;
                    }
                }
                x--;
                if (findX == range)
                    x = -1; // end loop
            } else
                isFinishLeft = true;
            if (y < splittedText.size()) {
                if (!splittedText.get(y).equals(pos) && !wordBag.contains(splittedText.get(y))) {
                    if (!isConjunction(splittedText.get(y))) {
                        wordBag.add(splittedText.get(y));
                        findY++;
                    }
                }
                y++;
                if (findY == range)
                    y = splittedText.size(); // end loop
            } else
                isFinishRight = true;
            if (isFinishLeft && isFinishRight)
                break;
        }
        if (debugMode)
            System.out.println("DEBUG WORDBAG - FeatureExtraction.java - partialProcess() = " + wordBag.toString());
    }

    private void thoroughProcess(List<String> splittedText, String pos) {
        for (int i=0; i < splittedText.size(); i++) {
            if (splittedText.get(i).equals(pos))
                posLocation.add(i);
        }
        int count = 0, x, y, findX, findY;
        boolean isFinishLeft, isFinishRight;
        while (count < posLocation.size()) {
            x = posLocation.get(count) - 1;
            y = posLocation.get(count) + 1;
            findX = 0;
            findY = 0;
            isFinishLeft = false;
            isFinishRight = false;
            while (true) {
                if (x > -1) {
                    if (!splittedText.get(x).equals(pos) && !wordBag.contains(splittedText.get(x))) {
                        if (!isConjunction(splittedText.get(x))) {
                            wordBag.add(splittedText.get(x));
                            findX++;
                        }
                    }
                    x--;
                    if (findX == range)
                        x = -1; // end loop
                } else
                    isFinishLeft = true;
                if (y < splittedText.size()) {
                    if (!splittedText.get(y).equals(pos) && !wordBag.contains(splittedText.get(y))) {
                        if (!isConjunction(splittedText.get(y))) {
                            wordBag.add(splittedText.get(y));
                            findY++;
                        }
                    }
                    y++;
                    if (findY == range)
                        y = splittedText.size(); // end loop
                } else
                    isFinishRight = true;
                if (isFinishLeft && isFinishRight)
                    break;
            }
            count++;
        }
        if (debugMode)
            System.out.println("DEBUG WORDBAG - FeatureExtraction.java - thoroughProcess() = " + wordBag.toString());
    }

    private boolean isConjunction(String s) {
        for (Object o : reader.getConjunctionArray()) {
            if (o.equals(s)) {
                if (debugMode)
                    System.out.println("DEBUG CONJUNCTION - FeatureExtraction.java - isConjunction() = " + s + " equals with " + o);
                return true;
            }
        }
        return false;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        if (writer != null)
            writer.setDebugMode(debugMode);
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

    public void clear() {
        if (wordBag != null)
            wordBag.clear();
        if (posLocation != null)
            posLocation.clear();
        if (reader != null)
            reader.clear();
    }

}