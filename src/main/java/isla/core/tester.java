package isla.core;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class tester {

    public static void main(String[] args) throws IOException, ParseException {

        String test = "this is a test phrase";

        NlpModule.initialize();
        System.out.println(NlpModule.nlp(test).get("pos"));
        System.out.println(WeatherModule.WeatherData().get("curTemp"));
        System.out.println(DictionaryModule.Dictionary("joy"));
    }

}
