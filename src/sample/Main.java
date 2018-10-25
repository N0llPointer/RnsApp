package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class Main extends Application {

    javafx.scene.control.TextField textField;

    private int[] modules = {41,59,67};
    private int[] basis;

    long numberToEncrypt;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(20);
        root.setPadding(new Insets(25, 25, 25, 25));
        initializeInsertScreen(root);

        findBasis();

        for (int i : basis) {
            System.out.println(i);
        }

        System.out.println();

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private void findBasis(){
        basis = new int[modules.length];

        int total = 1;
        for(int module: modules)
            total *= module;

        for(int i=0;i<basis.length;i++){
            int Pi = total / modules[i];
            int d = Pi % modules[i];
            int m = 1;
            while((d * m % modules[i]) != 1)
                m++;
            basis[i] = m * Pi;
        }
    }

    private void initializeInsertScreen(GridPane pane) {
        textField = new TextField();
        pane.add(textField, 0, 0, 2, 1);

        ToggleButton toggleButton = new ToggleButton("Number");

        toggleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (toggleButton.isSelected())
                    toggleButton.setText("Text");
                else
                    toggleButton.setText("Number");
            }
        });

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(toggleButton);
        pane.add(hBox, 0, 1, 2, 1);

        Button button = new Button("Encrypt");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Clicked");
                Stage stage = createLogStage(textField.getText(), toggleButton.isSelected());
                stage.show();
            }
        });


        HBox hBox1 = new HBox(10);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(button);
        //pane.add(hBox1,0,1,2,1);
        pane.add(hBox1, 0, 2, 2, 1);

        HBox hBox2 = new HBox(10);
        hBox.setAlignment(Pos.CENTER);

        Button generateRandomNumber = new Button("Generate");
        generateRandomNumber.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Random random = new Random(System.currentTimeMillis());
                long number = random.nextLong();
                textField.setText(Long.toString(number));
            }
        });

        hBox2.getChildren().add(generateRandomNumber);
        pane.add(hBox2, 0, 3, 2, 1);

    }

    public static void main(String[] args) {
        launch(args);
    }

    private Stage createLogStage(String text, boolean isText) {

        Stage stage = new Stage();
        //root = FXMLLoader.load(getClass().getClassLoader().getResource("path/to/other/view.fxml"), resources);
        VBox root = new VBox(10);

        if(isText)
            encryptText();
        else
            encryptNumber(Long.parseLong(text),root);

        root.setFillWidth(true);
        stage.setTitle("My New Stage Title");
        stage.setScene(new Scene(root));
        stage.show();
        // Hide this current window (if this is what you want)
        //((Node)(event.getSource())).getScene().getWindow().hide();

        return stage;
    }

    private void encryptNumber(long number, VBox root){
        HBox visualizer = new HBox(10);
        Text text = new Text("Начальное число: " + Long.toString(number));
        text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        root.getChildren().add(visualizer);


        short[] segments = divideNumberInSegments(number);

        visualizer = new HBox(10);
        text = new Text("Сегменты:");
        //text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        //Text infoText = new Text();
        String segmentsText = "Сегменты:\n";
        for(short segment: segments){
            segmentsText += segment + "\n";
        }
        text.setText(segmentsText);

        root.getChildren().add(visualizer);


        String[] messages = encodeNumberSegmentsIntoMessages(segments);

        visualizer = new HBox(10);
        text = new Text("Сегменты:");
        //text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        //Text infoText = new Text();
        String messagesText = "Сообщения:\n";
        for(String message: messages){
            messagesText += message + "\n";
        }
        text.setText(messagesText);

        root.getChildren().add(visualizer);

        String[] residue = decodeMessagesIntoResidueSequences(messages);

        visualizer = new HBox(10);
        text = new Text("Остатки:");
        //text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        //Text infoText = new Text();
        messagesText = "Остатки:\n";
        for(String message: residue){
            messagesText += message + "\n";
        }
        text.setText(messagesText);

        root.getChildren().add(visualizer);


        short[] postDecodeSegments = decodeResidueSequencesIntoNumberSegments(residue);

        visualizer = new HBox(10);
        text = new Text("Сегменты после Декода:");
        //text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        //Text infoText = new Text();
        segmentsText = "Сегменты после Декода:\n";
        for(short segment: segments){
            segmentsText += segment + "\n";
        }
        text.setText(segmentsText);

        root.getChildren().add(visualizer);


        visualizer = new HBox(10);
        text = new Text("Число после расшифровки: " + Long.toString(number));
        text.setFont(Font.font(20));
        visualizer.getChildren().add(text);
        root.getChildren().add(visualizer);
    }

    private void encryptText(){

    }

    private short[] divideNumberInSegments(long number){

        short[] segments = new short[4];

        for(int i=0;i<segments.length;i++)
            segments[i] = (short) (((number << 16 * i) >> 48) & 0xffff);

        return segments;
    }

    private String[] encodeNumberSegmentsIntoMessages(short[] segments){
        String[] messages = new String[modules.length];
        String[] additional = new String[modules.length];
        long[] additionalNumbers = new long[modules.length];

        Arrays.fill(additionalNumbers,0);

        int[] bitsCount = new int[modules.length];
        for(int i=0;i<bitsCount.length;i++){
            bitsCount[i] =(int) Math.ceil( Math.log(modules[i])/Math.log(2));
        }

        Arrays.fill(messages,"");
        int size = 2;
        for(int i=0;i<segments.length;i++){
            int number = segments[i] & 0xffff;
            for(int j=0;j<modules.length;j++){
                int n = number % modules[j];
                if(n < 10)
                    messages[j] +="0" + n;
                else
                    messages[j] += n;
                additionalNumbers[j] = (additionalNumbers[j] << bitsCount[j]) | n;
            }
        }

        for(int i=0;i<additional.length;i++){
            additional[i] = Long.toString(additionalNumbers[i]);
        }

        return messages;
    }

    private String[] decodeMessagesIntoResidueSequences(String[] messages){
        int size = messages[0].length()/2; //TODO потом отдебажить
        String[] residueSequences = new String[size];
        Arrays.fill(residueSequences,"");
        for(int i=0;i<residueSequences.length;i++){
            for(int j=0;j<messages.length;j++){
                residueSequences[i] += messages[j].substring(i*2,i*2+2) + ",";
            }
        }
        for(int i=0;i<residueSequences.length;i++) {
            System.out.println(residueSequences[i]);
            residueSequences[i] = residueSequences[i].substring(0, residueSequences[i].length() - 1);

        }
        return residueSequences;
    }

    private short[] decodeResidueSequencesIntoNumberSegments(String[] residues){

        int[] segments = new int[residues.length];

        Arrays.fill(segments, 0);

        int total = 1;
        for(int module: modules)
            total *= module;

        for(String text: residues){
            String[] res = text.split(",");
            for (int i = 0; i < res.length; i++) {
                System.out.println(Integer.parseInt(res[i]));
                segments[i] += Integer.parseInt(res[i]) * basis[i];
            }
        }

        short[] segs = new short[residues.length];
        for (int i = 0; i < segs.length; i++) {
            segs[i] = ((short) ((segments[i] % total) & 0xffff));
        }

        return segs;
    }

}
