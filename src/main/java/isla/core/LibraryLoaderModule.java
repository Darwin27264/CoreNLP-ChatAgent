package isla.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LibraryLoaderModule {

    private static HashMap<String, String> EvaEmo = new HashMap<>();
    private static HashMap<String, String> EvaAna = new HashMap<>();
    private static HashMap<String, List<String>> EzRes = new HashMap<>();
    private static HashMap<String, List<String>> EzResWeather = new HashMap<>();
    private static HashMap<String, List<String>> EvaRes = new HashMap<>();

    /**
     * Main program data loader,loading in all the
     * data necessary for the talking capabilities
     * of ISLA and also hosts the memory module
     */
    public static void dataLoad() {

        //EvaEmo is all the words with an emotional value
        try {
            Scanner Evaemo = new Scanner(new FileReader("EvaEmo.txt"));

            while (Evaemo.hasNextLine()) {
                //Getting the data ready to use
                //Putting all the Emotion and linked words into EvaEmo hashmap
                String Original = Evaemo.nextLine();
                Original = Original.replaceAll("[,' ]", "").replaceAll(":", " ");
                String[] TempWr = Original.split("\\s");
                EvaEmo.put(TempWr[0], TempWr[1]);
            }
        } catch (FileNotFoundException e1) {
            System.out.println("Error loading database EvaEmo");
        }

        //EzRes
        try {
            StringBuilder resultE = new StringBuilder();
            Scanner EzResl = new Scanner(new FileReader("EzRes.txt"));

            while (EzResl.hasNext()) {
                resultE.append(" ").append(EzResl.next());
            }

            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(resultE.toString());
            JSONObject EzResponse = (JSONObject) obj;
            JSONObject timeA = (JSONObject) EzResponse.get("time");
            JSONObject greetA = (JSONObject) EzResponse.get("greet");
            JSONObject weatherA = (JSONObject) EzResponse.get("weather");

            JSONArray time = (JSONArray) timeA.get("time");
            JSONArray date = (JSONArray) timeA.get("date");
            EzRes.put("time", Arrays.asList(Arrays.copyOf(time.toArray(), time.size(),
                    String[].class)));
            EzRes.put("date", Arrays.asList(Arrays.copyOf(date.toArray(), date.size(),
                    String[].class)));
            JSONArray greet = (JSONArray) greetA.get("greet");
            JSONArray greetQA = (JSONArray) greetA.get("greetQA");
            EzRes.put("greet", Arrays.asList(Arrays.copyOf(greet.toArray(), greet.size(),
                    String[].class)));
            EzRes.put("greetQA", Arrays.asList(Arrays.copyOf(greetQA.toArray(),
                    greetQA.size(), String[].class)));
            JSONArray dictionary = (JSONArray) EzResponse.get("dictionary");
            EzRes.put("dictionary", Arrays.asList(Arrays.copyOf(dictionary.toArray(),
                    dictionary.size(), String[].class)));

            JSONArray answer = (JSONArray) weatherA.get("an");
            EzResWeather.put("Answer", Arrays.asList(Arrays.copyOf(answer.toArray(),
                    answer.size(), String[].class)));
            JSONArray snowy = (JSONArray) weatherA.get("Snowy");
            EzResWeather.put("Snowy", Arrays.asList(Arrays.copyOf(snowy.toArray(),
                    snowy.size(), String[].class)));
            JSONArray Rainy = (JSONArray) weatherA.get("Rainy");
            EzResWeather.put("Rainy", Arrays.asList(Arrays.copyOf(Rainy.toArray(),
                    Rainy.size(), String[].class)));
            JSONArray Cloudy = (JSONArray) weatherA.get("Cloudy");
            EzResWeather.put("Cloudy", Arrays.asList(Arrays.copyOf(Cloudy.toArray(),
                    Cloudy.size(), String[].class)));
            JSONArray Windy = (JSONArray) weatherA.get("Windy");
            EzResWeather.put("Windy", Arrays.asList(Arrays.copyOf(Windy.toArray(),
                    Windy.size(), String[].class)));
            JSONArray Cold = (JSONArray) weatherA.get("Cold");
            EzResWeather.put("Cold", Arrays.asList(Arrays.copyOf(Cold.toArray(),
                    Cold.size(), String[].class)));
            JSONArray Hot = (JSONArray) weatherA.get("Hot");
            EzResWeather.put("Hot", Arrays.asList(Arrays.copyOf(Hot.toArray(),
                    Hot.size(), String[].class)));
            JSONArray Comfortable = (JSONArray) weatherA.get("Comfortable");
            EzResWeather.put("Comfortable", Arrays.asList(Arrays.copyOf(Comfortable.toArray(),
                    Comfortable.size(), String[].class)));

            EzResl.close();
        } catch (FileNotFoundException e1) {
            System.out.println("Error loading database EzRes, file not found");
        } catch (org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }

        /* EvaRes contains all the example statements and phrases which are
         * used to identify commands and statements
         */
        try {
            String resultE = "";
            Scanner EvaRes1 = new Scanner(new FileReader("EvaRes.txt"));

            while (EvaRes1.hasNext()) {
                resultE = resultE + " " + EvaRes1.next();
            }

            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(resultE);
            JSONObject EvaResponse = (JSONObject) obj;
            JSONObject greetQ = (JSONObject) EvaResponse.get("greet");

            JSONArray greet = (JSONArray) greetQ.get("greet");
            JSONArray greetQA = (JSONArray) greetQ.get("greetQA");
            EvaRes.put("greet", Arrays.asList(Arrays.copyOf(greet.toArray(), greet.size(),
                    String[].class)));
            EvaRes.put("greetQA", Arrays.asList(Arrays.copyOf(greetQA.toArray(),
                    greetQA.size(), String[].class)));

            JSONArray time = (JSONArray) EvaResponse.get("time");
            EvaRes.put("time", Arrays.asList(Arrays.copyOf(time.toArray(), time.size(),
                    String[].class)));

            JSONArray weather = (JSONArray) EvaResponse.get("weather");
            EvaRes.put("weather", Arrays.asList(Arrays.copyOf(weather.toArray(),
                    weather.size(), String[].class)));

            EvaRes1.close();
        } catch (FileNotFoundException | org.json.simple.parser.ParseException e1) {
            System.out.println("Error loading database EvaRes");
        }

        /* EmoAna is categorizing all the values from EvaEmo into
         * even more generalized categories
         */
        try {
            Scanner Emoana = new Scanner(new FileReader("EmoAna.txt"));

            while (Emoana.hasNextLine()) {
                //Getting the data ready to use
                //Putting all the Emotion and linked words into EvaEmo hashmap
                String Original = Emoana.nextLine().replaceAll(" ", "");
                String[] TempWr = Original.split(",");
                EvaAna.put(TempWr[0], TempWr[1]);

            }
        } catch (FileNotFoundException e1) {
            System.out.println("Error loading database EvaAna");

        }

        //Memory Loader


        //
    }

    /**
     * GetRes is a function made to facilitate the access of imported of memory
     *
     * @param JObj
     * @param Key
     * @return
     */
    public static List<String> GetRes(JSONObject JObj, String Key) {
        List<String> Value = Arrays.asList(JObj.get(Key).toString().split(","));
        Value.replaceAll(s -> s.replaceAll("[^a-zA-Z0-9-_ ]", ""));
        return Value;
    }

    public static HashMap<String, HashMap<String, List<String>>> OutputLists(){
        HashMap<String, HashMap<String, List<String>>> out = new HashMap<>();
        out.put("EzRes", EzRes);
        out.put("EzResWeather", EzResWeather);
        out.put("EvaRes", EvaRes);

        return out;
    }

    public static HashMap<String, HashMap<String, String>> OutputHashMaps(){
        HashMap<String, HashMap<String, String>> out = new HashMap<>();
        out.put("EvaEmo", EvaEmo);
        out.put("EvaAna", EvaAna);

        return out;
    }

    /* test main
    public static void main(String[] args) throws ParseException {
        dataLoad();

        HashMap<String, HashMap<String, List<String>>> lists = OutputLists();
        HashMap<String, HashMap<String, String>> strings = OutputStrings();

        System.out.println((lists.get("EzRes").get("greetQA")).size());
    }
    */
}
