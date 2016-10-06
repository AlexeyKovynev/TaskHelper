package name.javalex.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import name.javalex.entities.SimplifiedProcess;
import name.javalex.logic.Model;
import name.javalex.logic.XMLHandler;

import java.io.IOException;
import java.util.List;

public class CompareController {

    private Model model = new Model();
    private XMLHandler xml = new XMLHandler();
    private List<SimplifiedProcess> currentSimpleProcesses;
    private List<SimplifiedProcess> openedSimpleProcesses;


    @FXML
    private Button btnClose;

    @FXML
    private TableView<SimplifiedProcess> openedTasks;

    @FXML
    private TableColumn<SimplifiedProcess, String> openedNameColumn;

    @FXML
    private TableColumn<SimplifiedProcess, Long> openedMemoryColumn;


    @FXML
    private TableView<SimplifiedProcess> currentTasks;

    @FXML
    private TableColumn<SimplifiedProcess, String> currentNameColumn;

    @FXML
    private TableColumn<SimplifiedProcess, Long> currentMemoryColumn;


    @FXML
    private void initialize() throws IOException {

        getCurrentSimplifiedProcesses();
        getOpenedSimplifiedProcesses();

        // set type and value for the column
        openedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        openedMemoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));

        // fill table with data
        openedTasks.setItems(FXCollections.observableArrayList(openedSimpleProcesses));


        // set type and value for the column and fill table with data
        currentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentMemoryColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));
        currentTasks.setItems(FXCollections.observableArrayList(currentSimpleProcesses));

    }

    public void getCurrentSimplifiedProcesses() {
        currentSimpleProcesses = model.createSimplifiedProcessList(MainController.processes);
        System.out.println(currentSimpleProcesses);
    }

    public void getOpenedSimplifiedProcesses() {
        openedSimpleProcesses = xml.read();
        System.out.println(currentSimpleProcesses);
    }

    @FXML
    public void closeCompare() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}
