package com.example.notebook;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        List<List<List<String>>> data = readData("data.txt");
        //showData(data);
        //System.out.println(data);
        GridPane gridPaneStructure = new GridPane();
        gridPaneStructure.setPadding(new Insets(6.0));
        gridPaneStructure.setHgap(10);
        gridPaneStructure.setVgap(10);

        Label structureNameLabel = new Label(" name");
        TextField structureNameText = new TextField("");
        structureNameText.setPrefWidth(250);
        Button addStructureButton = new Button("Add");
        ChoiceBox choiceStructure = new ChoiceBox();
        choiceStructure.setValue("Select structure");
        for (int i = 0; i < data.size(); i++) {
            choiceStructure.getItems().add(data.get(i).get(0).get(0));
        }
        ChoiceBox choiceStructure1 = new ChoiceBox();
        choiceStructure1.setValue("Select structure");
        for (int i = 0; i < data.size(); i++) {
            choiceStructure1.getItems().add(data.get(i).get(0).get(0));
        }
        ChoiceBox choiceStructure2 = new ChoiceBox();
        choiceStructure2.setValue("Select structure");
        for (int i = 0; i < data.size(); i++) {
            choiceStructure2.getItems().add(data.get(i).get(0).get(0));
        }
        ChoiceBox choiceChapter = new ChoiceBox();
        choiceChapter.setValue("Select chapter");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).get(0).get(0) == choiceStructure1.getValue()) {
                for (int j = 1; j < data.get(i).size(); j++) {
                    choiceChapter.getItems().add(data.get(i).get(j).get(0));
                }
            }
        }
        choiceChapter.setPrefWidth(100);

        TextArea structureTextArea = new TextArea();
        Button saveStructureButton = new Button("Save Structure");
        structureTextArea.setPrefHeight(300);

        TextArea notesTextArea = new TextArea();
        Button saveNotesButton = new Button("Save Notes");
        notesTextArea.setPrefHeight(300);

        TextArea allNotesTextArea = new TextArea();
        Button saveInFileButton = new Button("Save All in File");
        allNotesTextArea.setPrefHeight(300);

        choiceStructure.setOnAction(event -> structureTextArea.setText(getChapters(data, choiceStructure.getValue().toString())));
        addStructureButton.setOnAction(event -> {
            if (structureNameText.getText().isEmpty() || structureNameText.getText().matches("[\\s]+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enter correct structure name!", ButtonType.CANCEL);
                alert.showAndWait();
                return;
            }
            data.add(new ArrayList<List<String>>());
            data.get(data.size()-1).add(new ArrayList<String>());
            data.get(data.size()-1).get(0).add(structureNameText.getText().strip());
            choiceStructure.getItems().add(data.get(data.size()-1).get(0).get(0));
            choiceStructure1.getItems().add(data.get(data.size()-1).get(0).get(0));
            choiceStructure2.getItems().add(data.get(data.size()-1).get(0).get(0));
        });
        choiceStructure1.setOnAction(event -> {
            choiceChapter.getItems().remove(0, choiceChapter.getItems().size());
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).get(0) == choiceStructure1.getValue()) {
                    for (int j = 1; j < data.get(i).size(); j++) {
                        choiceChapter.getItems().add(data.get(i).get(j).get(0));
                    }
                }
            }
            choiceChapter.setValue("Select chapter");
        });
        choiceChapter.setOnAction(event -> {
            if (choiceChapter.getValue() != null ) {
                notesTextArea.setText(getNotesFromChapter(data, choiceStructure1.getValue().toString(), choiceChapter.getValue().toString()));
            }
        });
        choiceStructure2.setOnAction(event -> {
            allNotesTextArea.setText(getAllNotes(data, choiceStructure2.getValue().toString()));
        });

        saveStructureButton.setOnAction(event -> {
            if (choiceStructure.getValue().toString() == "Select structure") {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select structure", ButtonType.CANCEL);
                alert.showAndWait();
                return;
            }
            int structureIndex = -1;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).get(0) == choiceStructure.getValue().toString()) {
                    structureIndex = i;
                }
            }
            Scanner s = new Scanner(structureTextArea.getText()).useDelimiter("\\s+");
            data.get(structureIndex).clear();
            //data.add(new ArrayList<List<String>>());
            data.get(structureIndex).add(new ArrayList<String>());
            data.get(structureIndex).get(0).add(choiceStructure.getValue().toString());
            while (s.hasNext()) {
                data.get(structureIndex).add(new ArrayList<String>());
                data.get(structureIndex).get(data.get(structureIndex).size()-1).add(s.nextLine().trim());
            }
            //showData(data);
            //System.out.println(data);
        });

        saveNotesButton.setOnAction(event -> {
            if (choiceStructure1.getValue().toString() == "Select structure") {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select structure", ButtonType.CANCEL);
                alert.showAndWait();
                return;
            }
            if (choiceChapter.getValue().toString() == "Select chapter") {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select chapter", ButtonType.CANCEL);
                alert.showAndWait();
                return;
            }
            int structureIndex = -1;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).get(0).get(0) == choiceStructure1.getValue().toString()) {
                    structureIndex = i;
                }
            }
            int chapterIndex = -1;
            for (int i = 0; i < data.get(structureIndex).size(); i++) {
                if (data.get(structureIndex).get(i).get(0) == choiceChapter.getValue().toString()) {
                    chapterIndex = i;
                }
            }
            Scanner s = new Scanner(notesTextArea.getText()).useDelimiter("\\s+");
            data.get(structureIndex).get(chapterIndex).clear();
            data.get(structureIndex).get(chapterIndex).add(choiceChapter.getValue().toString());
            while (s.hasNext()) {
                //data.get(structureIndex).add(new ArrayList<String>());
                data.get(structureIndex).get(chapterIndex).add(s.nextLine().trim());
            }
            //showData(data);
            //System.out.println(data);
        });
        saveInFileButton.setOnAction(event -> {
            saveInFile(data, "data.txt");
        });

        gridPaneStructure.add(structureNameLabel, 0, 0, 1, 1);
        gridPaneStructure.add(structureNameText, 1, 0, 1, 1);
        gridPaneStructure.add(addStructureButton, 2, 0, 1, 1);
        gridPaneStructure.add(choiceStructure, 0, 1, 3, 1);
        gridPaneStructure.add(structureTextArea, 0, 2, 3, 1);
        gridPaneStructure.add(saveStructureButton, 0, 3, 3, 1);


        GridPane gridPaneNotes = new GridPane();
        gridPaneNotes.setPadding(new Insets(6.0));
        gridPaneNotes.setHgap(10);
        gridPaneNotes.setVgap(10);
        gridPaneNotes.add(choiceStructure1, 0, 0, 1, 1);
        gridPaneNotes.add(choiceChapter, 1, 0, 1, 1);
        gridPaneNotes.add(notesTextArea, 0, 1, 3, 1);
        gridPaneNotes.add(saveNotesButton, 0, 2, 3, 1);


        GridPane gridPaneShow = new GridPane();
        gridPaneShow.setPadding(new Insets(6.0));
        gridPaneShow.setHgap(10);
        gridPaneShow.setVgap(10);
        gridPaneShow.add(choiceStructure2, 0, 0, 1, 1);
        gridPaneShow.add(allNotesTextArea, 0, 1, 1, 1);
        gridPaneShow.add(saveInFileButton, 0, 2, 1, 1);


        TabPane tabPane = new TabPane();
        Tab tab1 = new Tab("Create structure", gridPaneStructure);
        Tab tab2 = new Tab("Create notes"  , gridPaneNotes);
        Tab tab3 = new Tab("Show notes" , gridPaneShow);
        tab1.setClosable(false);
        tab2.setClosable(false);
        tab3.setClosable(false);
        tabPane.getTabs().add(tab1);
        tabPane.getTabs().add(tab2);
        tabPane.getTabs().add(tab3);

        VBox vBoxRoot = new VBox(tabPane);
        startScene(stage, vBoxRoot);
    }

    private void startScene(Stage stage, Pane pane) {
        Scene scene = new Scene(pane, 400, 400, Color.KHAKI);
        stage.setTitle("Notebook");
        stage.setMinHeight(400);
        stage.setMinWidth(400);
        stage.setMaxHeight(400);
        stage.setMaxWidth(400);
        stage.setScene(scene);
        stage.show();
    }

    private List<List<List<String>>> readData(String path) {
        List<List<List<String>>> result = new ArrayList<List<List<String>>>();
        int structureIndex = -1;
        int chapterIndex = 0;
        try {
            Scanner s = new Scanner(new File(path)).useDelimiter("\\s+");
            while (s.hasNext()) {
                String line = s.nextLine();
                if (Character.toString(line.charAt(0)).equals(" ") && Character.toString(line.charAt(1)).equals(" ")) {
                    result.get(structureIndex).get(chapterIndex).add(line);
                } else if (Character.toString(line.charAt(0)).equals(" ")) {
                    chapterIndex++;
                    result.get(structureIndex).add(new ArrayList<String>());
                    result.get(structureIndex).get(chapterIndex).add(line);
                } else {
                    structureIndex++;
                    chapterIndex = 0;
                    result.add(new ArrayList<List<String>>());
                    result.get(structureIndex).add(new ArrayList<String>());
                    result.get(structureIndex).get(chapterIndex).add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        return result;
    }

    private String getChapters(List<List<List<String>>> data, String workName) {
        String result = "";
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(0).get(0) == workName) {
                for (int j = 1; j < data.get(i).size(); j++) {
                    result += data.get(i).get(j).get(0).trim() + "\n";
                }
            }
        }
        return result;
    }

    private String getNotesFromChapter(List<List<List<String>>> data, String workName, String chapterName) {
        String result = "";
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(0).get(0) == workName) {
                for (int j = 1; j < data.get(i).size(); j++) {
                    if (data.get(i).get(j).get(0) == chapterName) {
                        for (int k = 1; k < data.get(i).get(j).size(); k++) {
                            result += data.get(i).get(j).get(k).trim() + "\n";
                        }
                    }
                }
            }
        }
        return result;
    }

    private String getAllNotes(List<List<List<String>>> data, String workName) {
        String result = "";
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).get(0).get(0) == workName) {
                for (int j = 1; j < data.get(i).size(); j++) {
                    for (int k = 0; k < data.get(i).get(j).size(); k++) {
                        if (k == 0) {
                            result += data.get(i).get(j).get(k).trim() + "\n";
                            continue;
                        }
                        result += "  " + data.get(i).get(j).get(k).trim() + "\n";
                    }
                }
            }
        }
        return result;
    }

    private void showData(List<List<List<String>>> data) {
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                for (int k = 0; k < data.get(i).get(j).size(); k++) {
                    System.out.println(data.get(i).get(j).get(k));
                }
            }
        }
    }

    private void saveInFile(List<List<List<String>>> data, String path) {
        String allData = "";
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                for (int k = 0; k < data.get(i).get(j).size(); k++) {
                    if (j == 0) {
                        allData += data.get(i).get(j).get(k).strip() + "\n";
                        continue;
                    }
                    if (k == 0) {
                        allData += " " + data.get(i).get(j).get(k).strip() + "\n";
                        continue;
                    }
                    allData += "  " + data.get(i).get(j).get(k).strip() + "\n";
                }
            }
        }
        File file = new File(path);
        saveTextToFile(allData, file);
    }

    private void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}