package isla.core;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class IntEmoProcessModule {

    //Array Library for question POS
    private static final String[] questionw = {"WH", "WDT", "WRB", "WP"};
    //Array Library for question NN
    private static final String[] questionNN = {"VBP", "VBZ"};

    private List<String> QuestionW = new ArrayList<>();
    private List<String> QuestionNN = new ArrayList<>();


    /**
     * IntentPrep processes the sentence first and prep it for IntentDeep
     *
     */
    public static void IntentCaller(List<String> QuestionW, List<String> QuestionNN,
                                    String UserQ, List<String> wordPos) {

        String WorPo = String.join(" ", wordPos);

        //finding comma or period to interpret sentences if needed
        if(UserQ.contains(",") || UserQ.contains(".")) {

            for(int i = 0; i < UserQ.split("[,.]").length; i++) {

                List<String> Poser = new LinkedList<>
                        (List.of(WorPo.split("[,.]")[i].trim()));
                List<String> userInput = new LinkedList<>
                        (List.of(UserQ.split("[,.]")[i].trim()));

                String firstWordPos = Poser.get(0).split(" ")[0];

                //Bait is created to map and allocate value in LinkedLists for better performance
                List<String> baitWordPos = new LinkedList<>(Arrays.asList(Poser.get(0).split(" ")));
                wordPos = baitWordPos;
                List<String> baitUserInput = new LinkedList<>(Arrays.asList(userInput.get(0).split(" ")));
                List<String> rawInputPart = baitUserInput;

                IntentProcess(QuestionW, QuestionNN, wordPos, rawInputPart, firstWordPos);
            }
        } else {
            String firstWordPos = wordPos.get(0);
            List<String> userInput = new LinkedList<>(List.of(UserQ.trim()));

            IntentProcess(QuestionW, QuestionNN, wordPos, userInput, firstWordPos);
        }
    }

    public static void IntentProcess(List<String> QuestionW, List<String> QuestionNN,
                                     List<String> wordPos, List<String> rawInput, String firstWordPos) {

        int Question = 0;
        int Statement= 0;

        String Obj;
        String Intentf;

        if(rawInput.contains("?")||QuestionW.contains(firstWordPos)) {
            Question++;
            Intentf = "a question";
        } else if(rawInput.contains(".")||rawInput.contains("!")){
            Statement++;
            Intentf = "a statement";
        } else {
            Statement++;
            Intentf = "likely a statement";
        }

        if(Question > Statement && wordPos.size() > 2) {
            intentF = 1;

            int indexNN = 0;
            List<Integer> NNAll = new LinkedList<>();
            List<String> allNN = new LinkedList<>();
            Obj = ""; //The object/info the user is asking for in a question
            String NN = wordPos.get(1); //Seeing if the second word is "is" or "are"
            DictionWord = rawInput.get(rawInput.size() - 1);

            if(QuestionNN.contains(NN)) {
                if(wordPos.contains("CC")) {
                    for(int p = 0;p < wordPos.size(); p++) {
                        if(wordPos.get(p).equals("NN")||wordPos.get(p).equals("NNP")||
                                wordPos.get(p).equals("NNS")||wordPos.get(p).equals("JJ")) {
                            NNAll.add(p);
                        }
                    }

                    for (Integer integer : NNAll) {
                        allNN.add(rawInput.get(integer).toLowerCase().replaceAll("[^a-zA-Z0-9]", " "));
                    }

                    System.out.println(allNN);
                    //multiple functions might need to be called
                } else if(wordPos.contains("NN")){
                    indexNN = wordPos.indexOf("NN");
                    Obj = rawInput.get(indexNN).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

                    System.out.println(Obj);
                    ResPro();
                } else if(wordPos.contains("NNS")) {
                    indexNN = wordPos.indexOf("NNS");
                    Obj = rawInput.get(indexNN).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

                    System.out.println(Obj);
                    ResPro();
                } else if(wordPos.contains("NNP")) {
                    indexNN = wordPos.indexOf("NNP");
                    Obj = rawInput.get(indexNN).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

                    System.out.println(Obj);
                    ResPro();
                } else if(wordPos.contains("JJ")) {
                    indexNN = wordPos.indexOf("JJ");
                    Obj = rawInput.get(indexNN).toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

                    System.out.println(Obj);
                    ResPro();
                } else {
                    MatchPro();
                }
            } else {
                MatchPro();
            }
        } else if(Statement > Question){
            intentF = 0;

            MatchPro();
        }
    }

    /**
     * The emotion process so far just scans through the
     * EVAEmo library for emotions found in the input and
     * at the same time working together with the StandfordNLP's
     * sentiment analysis to let Eva have a general idea of how
     * the user is feeling
     */
    public static void EmotionProcess(String userInput, int SentEv) {

        int Ana = 0;
        List<String> ana = new LinkedList<>();
        HashMap<String, Integer> Anaa = new HashMap<>();
        Anaa.put("hate", 0);
        Anaa.put("sad", 1);
        Anaa.put("fear", 2);
        Anaa.put("neutral", 3);
        Anaa.put("desire", 4);
        Anaa.put("happy", 5);

        HashMap<String, String> EvaEmo = LibraryLoaderModule.OutputHashMaps().get("EvaEmo");
        HashMap<String, String> EvaAna = LibraryLoaderModule.OutputHashMaps().get("EvaAna");

        //EmoAna is used to store all the emotion that were found in the input
        List<String> EmoAna = new ArrayList<>();

        //Clearing up the input for analysis
        userInput = userInput.replaceAll("\\W", " ").toLowerCase();
        String[] userInputArray = userInput.split(" ");

        for (String element : userInputArray) {
            boolean EmoSense = EvaEmo.containsKey(element);
            if (EmoSense) {
                EmoAna.add(EvaEmo.get(element));
            }
        }

        //Emoana (list) is used to keep all the emotions once
        //EmoAna is then used to see how often an emotion was found
        List<String> Emoana = new LinkedList<>();
        Emoana = EmoAna.stream().distinct().collect(Collectors.toList());

        for (String s : Emoana) {
            if (EvaAna.containsKey(s)) {
                ana.add(EvaAna.get(s));
            }
            //Test code
            //System.out.println(Emoana.get(i) + " has appeared " +
            //Collections.frequency(EmoAna, Emoana.get(i)) + " times in the user input");
        }

        for (String element : ana) {
            Ana = Ana + Anaa.get(element);
        }

        //SentEv is from StanfordNLP
        if(ana.size() == 0) {
            double EmotionRating = 5 * 10 + SentEv * 12.5;
        } else {
            double EmotionRating = Ana * 10 + SentEv * 12.5;
        }
    }

    public void IntEmo(String rawInput, List<String> wordPos, int sentEv) {
        QuestionW = Arrays.asList(questionw);
        QuestionNN = Arrays.asList(questionNN);

        EmotionProcess(rawInput, sentEv);
        IntentCaller(QuestionW, QuestionNN, rawInput, wordPos);
    }

}
