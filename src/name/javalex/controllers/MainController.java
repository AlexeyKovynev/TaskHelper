package name.javalex.controllers;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.javalex.entities.Process;
import name.javalex.entities.SimplifiedProcess;
import name.javalex.logic.Model;
import name.javalex.logic.XMLHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    static List<Process> processes;

    private List<SimplifiedProcess> simpleProcesses;
    private Model model = new Model();
    private XMLHandler xml = new XMLHandler();

    @FXML
    private TableView<Process> tableTasks;

    @FXML
    private TableColumn<Process, String> nameColumn;

    @FXML
    private TableColumn<Process, Integer> pidColumn;

    @FXML
    private TableColumn<Process, Long> usedMemoryColumn;

    @FXML
    private void initialize() throws IOException {

        getSystemProcesses();

        // set type and value for the column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        usedMemoryColumn.setCellValueFactory(new PropertyValueFactory<>("usedMemory"));

        // fill table with data
        tableTasks.setItems(FXCollections.observableArrayList(processes));


    }

    private void getSystemProcesses() throws IOException {
        processes = model.sortByMemory(model.getProcesses());
        System.out.println("getSystemProcesses");
        System.out.println(processes);
    }

    @FXML
    private void exportXML(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(tableTasks.getScene().getWindow());

        if(file != null){
            processes = model.groupByName(processes);
            simpleProcesses = model.createSimplifiedProcessList(processes);
            xml.create(simpleProcesses);
            xml.write(file.getAbsolutePath());
            System.out.println("printXMLToConsole");
            System.out.println(processes);
        }
    }

    @FXML
    private void importXml(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Open File");
        fileChooser.showOpenDialog(tableTasks.getScene().getWindow());
    }

    @FXML
    private void updateSystemProcesses() {
        tableTasks.getItems().clear();
        processes = model.sortByMemory(model.getProcesses());
        tableTasks.setItems(FXCollections.observableArrayList(processes));
        System.out.println("updateSystemProcesses");
        System.out.println(processes);
    }

    @FXML
    private void removeDuplicates() {
        tableTasks.getItems().clear();
        processes = model.groupByName(processes);
        tableTasks.setItems(FXCollections.observableArrayList(processes));
        System.out.println("removeDuplicates");
        System.out.println(processes);
    }

    @FXML
    private void test() {
        xml.read();
    }

    @FXML
    private void compare(ActionEvent event) throws IOException{
        Stage compareStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("../views/compare-view.fxml"));
        compareStage.setTitle("TaskHelper");
        compareStage.setResizable(false);
        compareStage.setScene(new Scene(root, 600, 700));
        compareStage.initModality(Modality.WINDOW_MODAL);
        compareStage.initOwner(tableTasks.getScene().getWindow());
        compareStage.show();
    }

    @FXML
    public void closeMain() {
        Stage stage = (Stage) tableTasks.getScene().getWindow();
        stage.close();
    }
}
