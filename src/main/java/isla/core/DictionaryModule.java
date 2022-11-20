package isla.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DictionaryModule {

    /**
     * Dictionary module of ISLA
     *
     * @param word - word to look up the definition for
     * @return rawDefs - empty list if failed
     * @return clean - cleaned dictionary definitions
     */
    public static List<String> Dictionary(String word) {

        word = word.toLowerCase();

        JSONParser jsonParser = new JSONParser();
        List<String> rawDefs = new ArrayList<>();

        final String appKey = "7fb10a55-6df5-47b0-958c-4a56d00a621c";
        try {
            URL url = new URL("https://dictionaryapi.com/api/v3/references/collegiate/json/" + word + "?key=" + appKey);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");

            // read the output from the server
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            String result = stringBuilder.toString();

            Object obj = jsonParser.parse(result);
            JSONArray fullArray = (JSONArray) obj;
            JSONObject all = (JSONObject) fullArray.get(0);

            JSONArray sseqArray = (JSONArray) all.get("def");
            JSONObject sseq = (JSONObject) sseqArray.get(0);

            JSONArray sensesAll = (JSONArray) sseq.get("sseq");

            for (Object def : sensesAll) {
                JSONArray layer1 = (JSONArray) def;
                JSONArray layer2 = (JSONArray) layer1.get(0);
                JSONArray dt = (JSONArray) ((JSONObject)layer2.get(1)).get("dt");

                String response = (((JSONArray)dt.get(0)).get(1)).toString();
                rawDefs.add(response);
            }

            return responseClean(rawDefs);

        } catch (Exception e) {
            e.printStackTrace();
            return rawDefs;
        }
    }

    /**
     * Cleans the raw definition results by taking out any sublayer markings
     * within curly brackets.
     *
     * @param raw - raw list of results
     * @return clean - clean list of results ready for use
     */
    private static List<String> responseClean(List<String> raw){

        List<String> clean = new ArrayList<>();

        for (String i : raw){

            StringBuilder rebuiltDef = new StringBuilder();
            boolean inBracket = false;

            for (Character j : i.toCharArray()){
                if (j == '[' || j == '{') {
                    inBracket = true;
                }

                if (!inBracket){
                    rebuiltDef.append(j);
                }

                if (j == '}' || j == ']'){
                    inBracket = false;
                }
            }
            clean.add(String.valueOf(rebuiltDef));
        }
        return clean;
    }

    /* test main
    public static void main(String[] args) {
        for (String i : Objects.requireNonNull(Dictionary("peach"))){
            System.out.println(i);
        }
    }
     */
}
