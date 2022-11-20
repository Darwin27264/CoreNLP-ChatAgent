package isla.core;

import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogModule {
    //Original path below
    //private static String LogPath = "D:\\All\\CodeBase\\ChatBot\\zLog\\";
    static final private String LogPath = "D:\\All\\CodeBase\\NozomuFolder\\Nozomu\\zLog\\";
    static final private String logName = "IslaLog" + TimeModule.TimeDate().get("date");
    //Log creator
    public static File InitializerLog() throws IOException {
        File log = new File(LogPath + logName + ".txt");

        if(!log.exists()) {
            log.createNewFile();
        }

        return log;
    }

    public static void Logger(String UserQ, int intentF, double emotionResult) throws IOException {
        File log = InitializerLog();
        String logg = "";

        if(intentF == 0) {
            logg = "statement";
        } else if(intentF == 1) {
            logg = "question";
        }

        FileWriter Log = new FileWriter(log, true);
        BufferedWriter logging = new BufferedWriter(Log);
        logging.write(TimeModule.TimeDate().get("time") +
                ": User---<" + UserQ + ">" + "\n" +
                "EmotionP gives a rating of: " + emotionResult + "\n" +
                "Sentence type is calculated to be: " + logg + "\n");
        //For console view
        System.out.println(TimeModule.TimeDate().get("time") +
                ": User---<" + UserQ + ">" + "\n" +
                "EmotionP gives a rating of: " + emotionResult + "\n" +
                "Sentence type is calculated to be: " + logg + "\n");

        logging.close();
    }

    public static void LoggerResponse(String IslaRes) throws IOException {
        File Log = InitializerLog();

        FileWriter log = new FileWriter(Log, true);
        BufferedWriter logging = new BufferedWriter(log);
        logging.write(TimeModule.TimeDate().get("time") +
                ": Isla---<" + IslaRes + ">" + "\n");
        logging.close();
        //Needs to implement this into the program
    }

    // test main
    public static void main(String[] args) throws IOException {
        Logger("test", 1, 75.0);
    }
    //
}
