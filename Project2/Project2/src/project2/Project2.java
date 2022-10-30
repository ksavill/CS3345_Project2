/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project2;

import java.awt.event.ActionListener;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author kev
 */
public class Project2 extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        //initialize the GridPane and the horizontal and veritcal gap components
        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(10);
        menuGrid.setVgap(12);
        
        //create the RadioButton group for algorithm type to run
        ToggleGroup inputMethodSelection = new ToggleGroup();
        //Insertion sort radio button
        RadioButton rb1 = new RadioButton("Text File");
        rb1.setToggleGroup(inputMethodSelection);
        rb1.setSelected(true);      
        //Selection sort radio  button
        RadioButton rb2 = new RadioButton("Pasted Text");
        rb2.setToggleGroup(inputMethodSelection);        
        
        Label inputMethodLabel = new Label("Input Method: ");
        
        Label textFileLocationLabel = new Label("Textfile directory: ");
        TextField textFileLocation = new TextField();
        
        // set the default value of the input textfield
        
        Label bookPasteLabel = new Label ("Paste text content here: ");
        TextField bookPaste = new TextField();
        
        
        Label startButtonLabel = new Label("Start OperationsL ");
        Button startButton = new Button("Start");
//        startList.setStyle("-fx-font-size: 12pt;");

        Label outputConsoleLabel = new Label("Output: ");
        TextField outputConsole = new TextField();
        
        //create Vertical box for algorithm selection radio buttons
        VBox radioButtons1 = new VBox();
        radioButtons1.getChildren().addAll(rb1,rb2);
        
        
        

        
        
        
        
        
        //(x,y) for grid arrangement. Can use this to arrange the radio buttons and other elements
        //contents for getting input type
        menuGrid.add(inputMethodLabel,0,0);
        menuGrid.add(radioButtons1,1,0);
        
        //content for inputting textfile location
        menuGrid.add(textFileLocationLabel,0,2);
        menuGrid.add(textFileLocation,1,2);
        
        //content for inputting text content
        menuGrid.add(bookPasteLabel,0,3);
        menuGrid.add(bookPaste,1,3);
        
        //start gui items
        menuGrid.add(startButtonLabel,0,4);
        menuGrid.add(startButton,1,4);
        
        //experiement results
        menuGrid.add(outputConsoleLabel,0,5);
        menuGrid.add(outputConsole,1,5);
        
        
        Scene scene = new Scene(menuGrid, 700, 850);
        
        primaryStage.setTitle("Project 2 - Kevin Savill");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                String sizeInput;
                String arrayType;
                String algorithmType;
//                sizeInput = listSize.getText();
//                arrayType = createArrayType.getSelectedToggle().toString();
//                algorithmType = algorithmSelection.getSelectedToggle().toString();
                System.out.println("Variables initialized and declared, calling beginProcess function");
                //call the starting function
//                beginProcess(sizeInput, arrayType, algorithmType);
                
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
}
    



// note: should add option to generate a new array each time the start button is pressed or just used the existing generated array.
// I could have an array made on program start by default to have size of [0] so that if an array is not made, the function won't hard crash.

