package project2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
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
        
        // items for input method fields
        Label inputMethodLabel = new Label("Input Method: ");
        //create the RadioButton group for algorithm type to run
        ToggleGroup inputMethodSelection = new ToggleGroup();
        //Insertion sort radio button
        RadioButton rb1 = new RadioButton("Text File");
        rb1.setToggleGroup(inputMethodSelection);
        rb1.setSelected(true);      
        //Selection sort radio  button
        RadioButton rb2 = new RadioButton("Pasted Text");
        rb2.setToggleGroup(inputMethodSelection);        
        
        Label textFileLocationLabel = new Label("Textfile directory: ");
        TextField textFileLocation = new TextField();
        // set the default value of the input textfield
        textFileLocation.setText("booklist.txt");
        
        
        Label bookPasteLabel = new Label ("Paste text content here: ");
        TextArea bookPaste = new TextArea();
        
        // button for starting operations
        Label startButtonLabel = new Label("Start Operations: ");
        Button startButton = new Button("Start");
//        startList.setStyle("-fx-font-size: 12pt;");


        Label outputResultsLabel = new Label("Results: ");
        Label outputResults = new Label("");
        
        Label importedBooklistLabel = new Label("Imported Booklist: ");
        Label importedBooklist = new Label("");
        
        //create Vertical box for algorithm selection radio buttons
        VBox radioButtons1 = new VBox();
        radioButtons1.getChildren().addAll(rb1,rb2);

        //(x,y) for grid arrangement. Can use this to arrange the radio buttons and other elements
        //contents for getting input type
        
        // input method radiobutton group options (Text File or Pasted Text)
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
        menuGrid.add(outputResultsLabel,0,5);
        menuGrid.add(outputResults,0,6);
        menuGrid.add(importedBooklistLabel,1,5);
        menuGrid.add(importedBooklist,1,6);
        
        
        Scene scene = new Scene(menuGrid, 1000, 950);
        
        primaryStage.setTitle("Project 2 - Kevin Savill");
        primaryStage.setScene(scene);
        primaryStage.show();
        

        // event listender for the start action button
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // initialize needed variables
                String inputMethod;
                String textfileDirectory;
                String pastedText;
                String bookString;
                String results;
                
                // declare inputMethod using current radio selection from gui
                inputMethod = inputMethodSelection.getSelectedToggle().toString();
                // delcare textfileDirectory from filename typed in gui, by default this will be booklist.txt
                textfileDirectory = textFileLocation.getText();
                // get the typed in text from the textArea in gui
                pastedText = bookPaste.getText();
                System.out.println("Variables initialized and declared, calling beginProcess function");
                bookString = startFunctions(inputMethod, textfileDirectory, pastedText);
                System.out.println(bookString);
                // in startFunctions, if there is an issue with getting the text from either the textArea or text file, "Error" will be returned as the string
                // this if statement, will set the result fields in the gui to "Error" and stop any further functions from being called.
                if (bookString == "Error") {
                    outputResults.setText(bookString);
                    importedBooklist.setText("Error");
                    return;
                }
                outputResults.setText("Imported");
                // update the import field in the gui to the list of books
                importedBooklist.setText(bookString);
                System.out.println("Going to pass imported list into book class to create book objects.");
                // pass the bookString to bookToAVL, this function will also handle creating the book objects, and then inserting to AVLTree
                // once done, any actions taken in the AVLTree for balancing will be returned as a long string with \n which will then be displayed to the results field in gui.
                results = bookToAVL(bookString);
                outputResults.setText(results);
                System.out.println("Main Functions complete.");
                
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }
    
    public static String startFunctions(String inputMethod, String textfileDirectory, String pastedText) {
        // initalize bookstring. I will return this and then pass this string along to a book creator class to handle appending the lines to author, booktitle, and isbn properties respectively
        // this function is simply for getting the inputs from the gui and getting all 30 lines (although can be variable amount of lines)
        String bookString;
        bookString = "";
        // input validation and getting book list from the text file method
        if (inputMethod.contains("Text File")) {
            System.out.println("Input method is Text File.");
            if(textfileDirectory.length() < 1) {
                System.out.println("The input method you chose is empty.");
                bookString = "Error";
                return bookString;
            } else {
                try{
                    File bookFile = new File(textfileDirectory);
                    Scanner scanner = new Scanner(bookFile);
                    int i =0; // count lines
                    String x;
                    while (scanner.hasNextLine()) { // goes through all lines in text file
                       x = scanner.nextLine(); // content of line
                       if(i!=0){ // if this is not the first line being imported, append a breakline before the next element
                           bookString = bookString + "\n";
                       }
                       bookString = bookString +  x;
                       i++;
                    }  
                } catch(Exception e) {
                    System.out.println(e);
                    bookString = "Error";
                }
            }
        // input validation and getting book list from the pasted text in gui
        } else {
            System.out.println("Input Method is Pasted Text");
            if(pastedText.length() < 1) {
                System.out.println("The input method you chose is empty.");
                bookString = "Error";
                return bookString;
            } else {
                bookString = pastedText;
            }
        }
        return bookString;
    }
    
    public String bookToAVL(String bookString) {
        String results = "";
        // pass bookString to insertBooks function in Book class to be returned the array of book objects
        results = Book.insertBooks(bookString);
        
        
        
        return results;
    }
    
}
    



// note: should add option to generate a new array each time the start button is pressed or just used the existing generated array.
// I could have an array made on program start by default to have size of [0] so that if an array is not made, the function won't hard crash.

