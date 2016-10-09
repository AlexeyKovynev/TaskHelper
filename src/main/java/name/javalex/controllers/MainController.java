package name.javalex.controllers;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import name.javalex.entities.Process;
import name.javalex.entities.SimplifiedProcess;
import name.javalex.model.Model;
import name.javalex.model.XLSXHandler;
import name.javalex.model.XMLHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    static List<Process> PROCESSES;
    static List<SimplifiedProcess> IMPORTED_PROCESSES;

    private List<SimplifiedProcess> simpleProcesses;
    private Model model = new Model();
    private XMLHandler xml = new XMLHandler();
    private XLSXHandler xlsx = XLSXHandler.getInstance();

    @FXML
    private Button btnCompare;

    @FXML
    private MenuItem btnMenuCompare;

    @FXML
    private TableView<Process> tableTasks;

    @FXML
    private TableColumn<Process, String> nameColumn;

    @FXML
    private TableColumn<Process, String> pidColumn;

    @FXML
    private TableColumn<Process, Long> usedMemoryColumn;

    @FXML
    private void initialize() throws IOException {

        getSystemProcesses();

        // set type and value for the column
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        usedMemoryColumn.setCellValueFactory(new PropertyValueFactory<>("usedMemory"));
        System.out.println(FXCollections.observableArrayList(PROCESSES));
        // fill table with data
        tableTasks.setItems(FXCollections.observableArrayList(PROCESSES));
    }

    private void getSystemProcesses() throws IOException {
        PROCESSES = model.sortByMemory(model.getProcesses());
    }

    @FXML
    public void exportXML() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(tableTasks.getScene().getWindow());

        if (file != null) {
            PROCESSES = model.groupByName(PROCESSES);
            simpleProcesses = model.createSimplifiedProcessList(PROCESSES);
            xml.create(simpleProcesses);
            xml.write(file.getAbsolutePath());
        }
    }

    @FXML
    public void importXML() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(tableTasks.getScene().getWindow());
        if (file != null) {
            IMPORTED_PROCESSES = xml.read(file.getAbsolutePath());
            btnCompare.setDisable(false);
            btnMenuCompare.setDisable(false);
        }
    }

    @FXML
    public void updateSystemProcesses() {
        tableTasks.getItems().clear();
        PROCESSES = model.sortByMemory(model.getProcesses());
        tableTasks.setItems(FXCollections.observableArrayList(PROCESSES));
    }

    @FXML
    public void removeDuplicates() {
        tableTasks.getItems().clear();
        PROCESSES = model.groupByName(PROCESSES);
        tableTasks.setItems(FXCollections.observableArrayList(PROCESSES));
    }

    // Open new stage
    @FXML
    public void compare() throws IOException {
        Stage compareStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/compare-view.fxml"));
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

    @FXML
    public void exportXLSX() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Microsoft Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(tableTasks.getScene().getWindow());

        if (file != null) {
            PROCESSES = model.groupByName(PROCESSES);
            simpleProcesses = model.createSimplifiedProcessList(PROCESSES);
            xlsx.writeStudentsListToExcel(simpleProcesses, file.getAbsolutePath());
        }
    }
}
