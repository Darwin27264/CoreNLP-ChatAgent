package isla.core;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ResponseModule {

    public static void MatchPro() throws ParseException, IOException {

        if(Userq.contains(",")||Userq.contains("?")||Userq.contains(".")) {
            Userq.remove((Userq.size() - 1));
        }

        Collection<String> inputClean = Userq;

        //code to scan EvaRes for patterns that match
        EvaRes.forEach((k,v) -> {
            for (String element : v) {
                double s, p;

                Collection<String> inWords = new ArrayList<>(Arrays.asList(element.split(" ")));
                Collection<String> similar = new ArrayList<>(inWords);

                similar.retainAll(inputClean);
                s = similar.size();
                p = inputClean.size();

                if(maxMatch < s/p) {
                    maxMatch = (s/p);
                    Obj = k;
                }

                System.out.println(s + " " + p);
                System.out.println(maxMatch);
            }
        }
        );
        ResPro();
    }

    public static void ResPro() throws ParseException, IOException {
        /*ResPro is the main response generation script
         * main functions of response generation. This method
         * will include both the EzRez function, command processing
         * and also the statment processing functions
         */

        MatchIntentRes = "";

        if(allNN.size() > 0) {
            Obj = allNN.get(i - 1);
        }

        //checking to see if dictionary needs to be called
        diction = false; //resetting diction
        if(Obj.equals("meaning")||Obj.equals("definition")) {
            diction = true;
            DictionWord = Userq.get(Userq.size() - 1);
        } else if(Obj.equals("mean")) {
            diction = true;
            DictionWord = Userq.get((Userq.indexOf("mean") - 1));
        }

        Random rand = new Random();

        switch(Obj) {
            case "weather":
                weather();
                MatchIntentRes = WeatherAn;
                break;
            case "time":
                time();
                MatchIntentRes = TimeAn;
                break;
            case "date":
                time();
                MatchIntentRes = DateAn;
                break;
            case "greet":
                MatchIntentRes = EzRes.get("greet").get(rand.nextInt(EzRes.get("greet").size()));

                if(MatchIntentRes.indexOf("%user") != -1) {
                    MatchIntentRes = MatchIntentRes.replace("%user", User);
                }

                break;
            case "greetQA":
                MatchIntentRes = EzRes.get("greetQA").get(rand.nextInt(EzRes.get("greetQA").size()));

                if(MatchIntentRes.indexOf("%user") != -1) {
                    MatchIntentRes = MatchIntentRes.replace("%user", User);
                }

                break;
            default:
                if(diction) {
                    Dictionary();
                    MatchIntentRes = DictionRes;
                } else {
                    //Using ELIZA response generating method

                    //
                }
        }

        if(isMulti) {
            if(i < UserQParts || i < allNN.size()) {

                IslaRes = IslaRes + MatchIntentRes + "\n";
                i++;
            } else {
                IslaRes = IslaRes + MatchIntentRes + "\n";

                LoggerR();
                Logger();
            }
        } else {
            IslaRes = MatchIntentRes;

            LoggerR();
            Logger();
        }
    }

    public static void Response() throws IOException {
        LoggerR();

        if(IslaRes == "") {
            String error = "No valid response could be found and given (╯•﹏•╰)";
            output.appendText("-->Isla: " + error + "\n");
        } else {
            output.appendText("-->Isla: " + IslaRes + "\n");
        }

        IslaRes = "";
        //timer function below, will output if user do not respond in sometime to check for attention

    }
}
