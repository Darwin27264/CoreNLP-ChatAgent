package isla.core;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WeatherModule {

    /**
     * Weather module of ISLA
     *
     * @return HashMap<String, String> - contains the results needed for answer formulating
     * @throws IOException
     * @throws org.json.simple.parser.ParseException
     */
    public static HashMap<String, String> WeatherData() throws IOException, org.json.simple.parser.ParseException {

        String location = "Fredericton";
        //location = "Kingston";

        String API_KEY = "4a515dfc797b53869373dd8e802726cf";
        String part = "minutely";
        String lat = "", lon = "";

        if (location.equals("Fredericton")){
            //Geo Coord of Fredericton
            lat = "45.963589";
            lon = "-66.643112";
        } else if (location.equals("Kingston")){
            //Geo Coord of Kingston
            lat = "44.225276";
            lon = "-76.495083";
        }

        String urlString = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&lon="
                + lon + "&" + "units=metric&exclude=" + part + "&appid=" + API_KEY;

        HashMap<String, String> finalResult = new HashMap<String, String>();

        //read API data, establish url connection
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null){
            result.append(line);
        }
        rd.close();

        //JSON data extracting
        JSONParser jsonParser = new JSONParser();
        Object obj = jsonParser.parse(result.toString());
        JSONObject main = (JSONObject) obj;
        JSONObject current = (JSONObject) main.get("current");
        JSONArray weather = (JSONArray) current.get("weather");
        JSONObject mainW = (JSONObject) weather.get(0);

        double wind = (double) current.get("wind_speed");
        double temp = (double) current.get("temp");
        long curID = (long) mainW.get("id");

        JSONArray hourly = (JSONArray) main.get("hourly");

        List<Integer> tempDay = new ArrayList<>();
        List<Integer> windDay = new ArrayList<>();
        List<Long> id = new ArrayList<>();

        for(int o=0; o<8; o++) {
            JSONObject hour = (JSONObject) hourly.get(o);

            Object t = hour.get("temp");
            double tempD = ((Number) t).doubleValue();
            tempDay.add((int)tempD);

            Object w = hour.get("wind_speed");
            double windD = ((Number) w).doubleValue();
            windDay.add((int)windD);

            JSONArray idWeather = (JSONArray) hour.get("weather");
            JSONObject idA = (JSONObject) idWeather.get(0);
            id.add((Long) idA.get("id"));
        }

        //checking the type of weather using code lables provided by the API
        String weatherNow = "";
        if(curID >= 200 && curID < 250) {
            weatherNow = "rain";
        } else if(curID >= 300 && curID < 350) {
            weatherNow = "rain";
        } else if(curID >= 500 && curID < 550) {
            weatherNow = "rain";
        } else if(curID >= 600 && curID < 650) {
            weatherNow = "snow";
        } else if(curID == 800) {
            weatherNow = "sunny";
        } else if(curID >= 800 && curID < 805){
            weatherNow = "cloudy";
        }
        //***

        //finding the average weather tags of the day to estimate the weather of the day
        int Fhot = 0, Fcold = 0, Frain = 0, Fsnow = 0, Fcloudy = 0, Fwind = 0;
        String weatherPro = "";

        for(int g=0; g<tempDay.size(); g++) {
            if(tempDay.get(g) > 25) {
                Fhot++;
            } else if(tempDay.get(g) < 15) {
                Fcold++;
            }

            if(id.get(g) >= 200 && id.get(g) < 250) {
                Frain++;
            } else if(id.get(g) >= 300 && id.get(g) < 350) {
                Frain++;
            } else if(id.get(g) >= 500 && id.get(g) < 550) {
                Frain++;
            } else if(id.get(g) >= 600 && id.get(g) < 650) {
                Fsnow++;
            } else if(id.get(g) >= 800 && id.get(g) < 805) {
                Fcloudy++;
            }

            if(windDay.get(g) > 50) {
                Fwind++;
            }
        }

        if(Fhot > Fcold) {
            weatherPro = "Hot";
        } else if(Fhot < Fcold){
            weatherPro = "Cold";
        } else {
            weatherPro = "Comfortable";
        }

        HashMap<Integer, String> weathertag = new HashMap<>();

        weathertag.put(Frain, "Rainy");
        weathertag.put(Fsnow, "Snowy");
        weathertag.put(Fwind, "Windy");
        weathertag.put(Fcloudy, "Cloudy");
        List<Integer> comp = new ArrayList<>();
        comp.add(Frain);
        comp.add(Fsnow);
        comp.add(Fcloudy);
        comp.add(Fwind);
        int key = Collections.max(comp);
        String weatherTag = weathertag.get(key);

        //loading final values into map for export
        finalResult.put("curTemp", String.valueOf(Math.round(temp)));
        finalResult.put("curWind", String.valueOf(Math.round(wind)));
        finalResult.put("weatherPro", weatherPro);
        finalResult.put("weatherTag", weatherTag);

        return finalResult;
    }

    /* test main
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(WeatherData().get("curTemp"));
    }
    */
}
