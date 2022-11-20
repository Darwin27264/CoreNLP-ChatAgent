package isla.core;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.Random;

public class Alma extends Application {

    static Thread loadingThread = new Thread();

    static TextArea output = new TextArea();
    static BorderPane root = new BorderPane();
    //Array Library for Services
    private static final String[] CurrentSer = {"Weather", "Time", "NLP", "Sentiment", "Dictionary"};


    public void start(Stage primaryStage) throws Exception {

        // Create a Runnable
        Runnable task = () -> {
            // Loading essential libraries
            LibraryLoaderModule.dataLoad();

            // Loading memories
            MemoryLoaderModule.dataLoad();

        };

        // Run the task in a background thread
        loadingThread = new Thread(task);
        // Terminate the running thread if the application exits
        loadingThread.setDaemon(true);
        loadingThread.start();

        Parent pane = new Pane();
        Scene loading = new Scene(pane, 250, 100);
        primaryStage.setTitle("Loading ISLA...");
        primaryStage.setResizable(false);
        primaryStage.setScene(loading);
        primaryStage.show();

        //wait 5 seconds for all NLP to initialize and load in data from library and services
        Thread.sleep(7000);

        TextField input = new TextField();
        input.setAlignment(Pos.CENTER_LEFT);

        output.prefHeight(600);
        output.setWrapText(true);
        output.setEditable(false);

        MenuBar mb = new MenuBar();
        Menu m1 = new Menu("Services");
        Menu Cur = new Menu("Currently");

        //Adding library to the library list in the menu bar
        for (String element : CurrentSer) {
            MenuItem files = new MenuItem(element);
            m1.getItems().add(files);
        }

        Menu allon = new Menu("SystemStat");

        // Need to implement a better system to check statues of services
        //MenuItem sysstat = new MenuItem(SystemStat); ***

        //ISLA Version Systems
        //B- for beta
        //A- for alpha/final
        MenuItem sysVersion = new MenuItem("Version: B-v0.2");

        //allon.getItems().add(sysstat); ***
        allon.getItems().add(sysVersion);

        MenuItem date = new MenuItem(TimeModule.dateOut());
        MenuItem wea = new MenuItem(weatherTag + ", " + weatherData.get(0) + "°C");
        Cur.getItems().add(date);
        Cur.getItems().add(wea);

        //Adding library to the library list in the menu bar
        mb.getMenus().addAll(m1, Cur, allon);

        root.setTop(mb);
        root.setCenter(output);
        BorderPane.setMargin(output, new Insets(10));
        root.setBottom(input);
        BorderPane.setMargin(input, new Insets(10));

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Isla");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

        //Initial greetings with some info
        String hour = TimeModule.timeOut().split(":")[0];
        int Hour = Integer.parseInt(hour);
        String timedy = "";

        if(6 <= Hour && Hour <= 12) {
            timedy = "Good morning";
        } else if(12 <= Hour && Hour <= 18) {
            timedy = "Good afternoon";
        } else if(18 <= Hour && Hour <= 24) {
            timedy = "Good evening";
        } else {
            timedy = "Hey there";
        }

        // preferredName is a list containing all nicknames loaded from
        // the MemoryLoaderModule
        Random rand = new Random();
        String userName = perferredName.get(rand.nextInt(perferredName.size()));

        output.setText("-->Isla: " + timedy + ", " + userName +
                ". Isla here. （‐＾▽＾‐）ﾉ \n");

        input.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER -> {
                    //Refreshes the user's name
                    userName = perferredName.get(rand.nextInt(perferredName.size()));

                    String UserQ = input.getText();
                    input.setText("");
                    time();
                    NlpModule.initialize();

                    //Putting the input to Dialog area
                    output.appendText("-->You: " + UserQ + "\n");

                    try {
                        MainPro();
                    } catch (ParseException | IOException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        Logger();
                    } catch (IOException e1) {
                        System.out.println("Error occurred when trying to create log! (╯•﹏•╰)");
                    }

                    //Eva responds
                    try {
                        Response();
                    } catch (IOException e1) {
                        System.out.println("Error occurred when trying to generate response! (╯•﹏•╰)");
                    }
                }

                //using stacks for storing previous conversations for thread tracking

                //

                //do something
                case SHIFT -> {
                }

                default -> {

                }
            }
        });

    }

    public static void main(String[] args) throws InterruptedException{launch(args);}

}
