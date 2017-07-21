package decisionlist.algorithm;

import decisionlist.dataprocess.JSONReader;
import decisionlist.dataprocess.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kinji.
 * for more information contact my email at putugitaandika@gmail.com
 */

public class DecisionList {

    private static final byte classID = 3;
    private JSONReader reader;
    private JSONWriter writer;

    // Debug purpose
    private boolean debugMode = true;

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
        reader.setDebugMode(debugMode);
        if (writer != null)
            writer.setDebugMode(debugMode);
    }

    public DecisionList() throws IOException, ParseException {
        reader = new JSONReader(classID);
        writer = new JSONWriter();
    }

    public DecisionList(JSONReader reader) {
        this.reader = reader;
    }

    public String disambiguate(List<String> bagOfWords, String POS) {
        JSONObject data, collocation;
        JSONArray weightArray;
        Map<String, Double> decisionMap = new HashMap<>(bagOfWords.size());
        String collocationSense;
        for (Object o : reader.getDecisionArray()) {
            data = (JSONObject) o;
            if (data.get("POS").equals(POS)) {
                weightArray = (JSONArray) data.get("weight");
                for (String word : bagOfWords) {
                    for (Object o1 : weightArray) {
                        collocation = (JSONObject) o1;
                        if (collocation.get("collocation").equals(word)) {
                            collocationSense = (String) collocation.get("sense");
                            if (decisionMap.containsKey(collocationSense))
                                decisionMap.put(collocationSense, decisionMap.get(collocationSense) + (Double) collocation.get("LogL"));
                            else
                                decisionMap.put(collocationSense, (Double) collocation.get("LogL"));
                            if (debugMode)
                                System.out.println("WEIGHT TOTAL " + collocationSense + " = " + decisionMap.get(collocation.get("sense")));
                            break;
                        }
                    }
                }
                break;
            }
        }
        // FIND THE HIGHEST VALUE
        Map.Entry<String, Double> maxEntry = null;
        for (Map.Entry<String, Double> entry : decisionMap.entrySet()) {
            if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
                maxEntry = entry;
            }
        }
        if (maxEntry == null)
            return null;
        else return maxEntry.getKey();
    }

    @SuppressWarnings("unchecked")
    public void updateDecisionTable() throws IOException, URISyntaxException {
        JSONObject data, objectSense, features, denominatorSense, denominatorFeatures, senseComparator;
        JSONArray arraySense, newArrayRoot = new JSONArray();
        double valueNumerator, valueDenominator, valueComparator;
        boolean isNewCollocation;
        for (Object o : reader.getFeatureArray()) {
            data = (JSONObject) o;
            arraySense = (JSONArray) data.get("sense");
            // Initialize new data
            JSONObject newData = new JSONObject();
            newData.put("POS", data.get("POS"));
            JSONArray newArrayWeight = new JSONArray();
            // End initialize
            for (Object o1 : arraySense) {
                objectSense = (JSONObject) o1;
                features = (JSONObject) objectSense.get("feature");
                for (Object key : features.keySet()) {
                    // FIND NUMERATOR VALUE
                    valueNumerator = probabilityDisambiguation((long) features.get(key), (long) objectSense.get("total"));
                    // FIND DENOMINATOR VALUE
                    valueDenominator = 0;
                    for (Object o2 : arraySense) {
                        denominatorSense = (JSONObject) o2;
                        if (!denominatorSense.get("senseID").equals(objectSense.get("senseID"))) {
                            denominatorFeatures = (JSONObject) denominatorSense.get("feature");
                            if (denominatorFeatures.containsKey(key)) {
                                valueDenominator = valueDenominator + probabilityDisambiguation((long) denominatorFeatures.get(key), (long) denominatorSense.get("total"));
                            }
                        }
                    }
                    if (valueDenominator == 0)
                        valueDenominator = 0.0001;
                    // PREVENT DUPLICATE COLLOCATION
                    isNewCollocation = true;
                    for (Object o2 : newArrayWeight) {
                        senseComparator = (JSONObject) o2;
                        if (senseComparator.containsKey("collocation")) {
                            if (senseComparator.get("collocation").equals(key)) {
                                isNewCollocation = false;
                                valueComparator = Math.log(probabilityDisambiguation(valueNumerator, valueDenominator)) / Math.log(2);
                                if ((double) senseComparator.get("LogL") < valueComparator) {
                                    senseComparator.put("LogL", valueComparator);
                                    senseComparator.put("sense", objectSense.get("senseID"));
                                }
                                break;
                            }
                        }
                    }
                    if (isNewCollocation) {
                        JSONObject newCollocation = new JSONObject();
                        newCollocation.put("LogL", (Math.log(probabilityDisambiguation(valueNumerator, valueDenominator)) / Math.log(2)));
                        newCollocation.put("collocation", key);
                        newCollocation.put("sense", objectSense.get("senseID"));
                        newArrayWeight.add(newCollocation);
                    }
                }
            }
            newArrayWeight.sort((o1, o2) -> -((Double)((JSONObject)o1).get("LogL")).compareTo(((Double)((JSONObject)o2).get("LogL"))));
            newData.put("weight", newArrayWeight);
            newArrayRoot.add(newData);
        }
        writer.writeUpdateTableDecision(newArrayRoot);
    }

    private double probabilityDisambiguation(double numerator, double denominator) {
        return (numerator / denominator);
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

    public void clear() {
        if (reader != null)
            reader.clear();
    }

}