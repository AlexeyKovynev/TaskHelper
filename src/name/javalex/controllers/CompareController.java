package name.javalex.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import name.javalex.entities.SimplifiedProcess;
import name.javalex.logic.Model;
import name.javalex.logic.XMLHandler;

import java.io.IOException;
import java.util.List;

public class CompareController {

    private Model model = new Model();
    private XMLHandler xml = new XMLHandler();
    private List<SimplifiedProcess> currentSProcesses;
    private List<SimplifiedProcess> openedSProcesses;


    @FXML
    private Button btnClose;

    // Bottom labels
    @FXML
    private Label lbOpenedVal;

    @FXML
    private Label lbOpenedNameVal;

    @FXML
    private Label lbOpenedMemVal;

    @FXML
    private Label lbCurrVal;

    @FXML
    private Label lbCurrNameVal;

    @FXML
    private Label lbCurrMemVal;

    @FXML
    private Label lbConclusion;

    // Tables
    @FXML
    private TableView<SimplifiedProcess> openedTasks;

    @FXML
    private TableColumn<SimplifiedProcess, String> openedNameColumn;

    @FXML
    private TableColumn<SimplifiedProcess, Long> openedMemColumn;

    @FXML
    private TableView<SimplifiedProcess> currentTasks;

    @FXML
    private TableColumn<SimplifiedProcess, String> currentNameColumn;

    @FXML
    private TableColumn<SimplifiedProcess, Long> currentMemColumn;

    @FXML
    private void initialize() throws IOException {

        getCurrentSimplifiedProcesses();
        getOpenedSimplifiedProcesses();

        ChangeListener openedTasksTableListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    SimplifiedProcess openedProc = openedTasks.getSelectionModel().getSelectedItem();
                    lbOpenedVal.setText("Yes");
                    lbOpenedNameVal.setText(openedProc.getName());
                    lbOpenedMemVal.setText(openedProc.getMemory() + " KB");
                    if (currentSProcesses.contains(openedProc)) {
                        setValuesToCurrent(openedProc);
                    } else {
                        setCurrentNotAvailable();
                        lbConclusion.setText("Process is not running now");
                    }
                }
            }
        };

        ChangeListener currentTasksTableListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue != null) {
                    SimplifiedProcess runningProc = currentTasks.getSelectionModel().getSelectedItem();
                    lbCurrVal.setText("Yes");
                    lbCurrNameVal.setText(runningProc.getName());
                    lbCurrMemVal.setText(runningProc.getMemory() + " KB");
                    if (openedSProcesses.contains(runningProc)) {
                        setValuesToOpened(runningProc);
                    } else {
                        setOpenedNotAvailable();
                        lbConclusion.setText("Current process was not launched when you save the report");
                    }
                }
            }
        };

        openedTasks.getSelectionModel().selectedItemProperty().addListener(openedTasksTableListener);
        currentTasks.getSelectionModel().selectedItemProperty().addListener(currentTasksTableListener);

        // set type and value for the column
        openedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        openedMemColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));

        // fill table with data
        openedTasks.setItems(FXCollections.observableArrayList(openedSProcesses));

        // set type and value for the column and fill table with data
        currentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentMemColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));
        currentTasks.setItems(FXCollections.observableArrayList(currentSProcesses));
    }

    private void getCurrentSimplifiedProcesses() {
        MainController.processes = model.groupByName(MainController.processes);
        currentSProcesses = model.createSimplifiedProcessList(MainController.processes);
        System.out.println(currentSProcesses);
    }

    private void getOpenedSimplifiedProcesses() {
        openedSProcesses = xml.read();
        System.out.println(currentSProcesses);
    }

    @FXML
    private void closeCompare() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void setValuesToCurrent(SimplifiedProcess openedProc) {

        long openedMemory = openedProc.getMemory();
        long currentMemory = currentSProcesses.get(currentSProcesses.indexOf(openedProc)).getMemory();
        lbConclusion.setText(model.getMemoryDifferences(openedMemory, currentMemory));
        lbCurrVal.setText("Yes");
        lbCurrNameVal.setText(currentSProcesses.get(currentSProcesses.indexOf(openedProc)).getName());
        lbCurrMemVal.setText(String.valueOf(currentMemory));
    }

    private void setValuesToOpened(SimplifiedProcess runningProc) {

        long currentMemory = runningProc.getMemory();
        long openedMemory = openedSProcesses.get(openedSProcesses.indexOf(runningProc)).getMemory();
        lbConclusion.setText(model.getMemoryDifferences(openedMemory, currentMemory));
        lbOpenedNameVal.setText(openedSProcesses.get(openedSProcesses.indexOf(runningProc)).getName());
        lbOpenedMemVal.setText(String.valueOf(openedMemory));
        lbOpenedVal.setText("Yes");

    }

    private void setOpenedNotAvailable() {
        lbOpenedVal.setText("No");
        lbOpenedNameVal.setText("Not available");
        lbOpenedMemVal.setText("Not available");
    }

    private void setCurrentNotAvailable() {
        lbCurrVal.setText("No");
        lbCurrNameVal.setText("Not available");
        lbCurrMemVal.setText("Not available");
    }
}
