package isla.core;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class NlpModule {
    public static HashMap<String,List<String>> nlp(String txt) {

        org.apache.log4j.BasicConfigurator.configure();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, sentiment");
        props.setProperty("ner.applyFineGrained", "false");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // read some text in the text variable
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(txt);

        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        List<String> wordPos = new ArrayList<>();
        List<String> userQ = new ArrayList<>();
        List<String> nameEn = new ArrayList<>();
        List<String> sentEv = new ArrayList<>();

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ner = token.get(NamedEntityTagAnnotation.class);
                // this sets the tree and output of the Sentiment analysis to int SentEv
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int SentEv = RNNCoreAnnotations.getPredictedClass(tree);

                wordPos.add(pos);
                userQ.add(word.toLowerCase());
                nameEn.add(ner);
                sentEv.add(String.valueOf(SentEv));

            }
        }

        HashMap<String, List<String>> out = new HashMap<String, List<String>>();
        out.put("pos", wordPos);
        out.put("input", userQ);
        out.put("ner", nameEn);
        out.put("sent", sentEv);

        return out;
    }

    public static void initialize(){
        nlp("");
    }

    /* test main
    public static void main(String[] args) throws InterruptedException {
        //Starting NLP
        initialize();

        System.out.println(nlp("more test").get("pos"));
        System.out.println(nlp("more test").get("input"));

    }
    */
}