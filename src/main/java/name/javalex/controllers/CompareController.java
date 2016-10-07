package name.javalex.controllers;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import name.javalex.entities.SimplifiedProcess;
import name.javalex.model.Model;

import java.io.IOException;
import java.util.List;

public class CompareController {

    private Model model = new Model();
    private List<SimplifiedProcess> currentSProcesses;

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

        ChangeListener openedTasksTableListener = (observable, oldValue, newValue) -> {
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
        };

        ChangeListener currentTasksTableListener = (observable, oldValue, newValue) -> {
            if (newValue != null) {
                SimplifiedProcess runningProc = currentTasks.getSelectionModel().getSelectedItem();
                lbCurrVal.setText("Yes");
                lbCurrNameVal.setText(runningProc.getName());
                lbCurrMemVal.setText(runningProc.getMemory() + " KB");
                if (MainController.IMPORTED_PROCESSES.contains(runningProc)) {
                    setValuesToOpened(runningProc);
                } else {
                    setOpenedNotAvailable();
                    lbConclusion.setText("Current process was not launched when selected report was made");
                }
            }
        };

        openedTasks.getSelectionModel().selectedItemProperty().addListener(openedTasksTableListener);
        currentTasks.getSelectionModel().selectedItemProperty().addListener(currentTasksTableListener);

        // set type and value for the column
        openedNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        openedMemColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));

        // fill table with data
        openedTasks.setItems(FXCollections.observableArrayList(MainController.IMPORTED_PROCESSES));

        // set type and value for the column and fill table with data
        currentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentMemColumn.setCellValueFactory(new PropertyValueFactory<>("memory"));
        currentTasks.setItems(FXCollections.observableArrayList(currentSProcesses));
    }

    private void getCurrentSimplifiedProcesses() {
        MainController.PROCESSES = model.groupByName(MainController.PROCESSES);
        currentSProcesses = model.createSimplifiedProcessList(MainController.PROCESSES);
        System.out.println(currentSProcesses);
    }


    @FXML
    public void closeCompare() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void setValuesToCurrent(SimplifiedProcess openedProc) {

        long openedMemory = openedProc.getMemory();
        long currentMemory = currentSProcesses.get(currentSProcesses.indexOf(openedProc)).getMemory();
        lbConclusion.setText(model.getMemoryDifferences(openedMemory, currentMemory));
        lbCurrVal.setText("Yes");
        lbCurrNameVal.setText(currentSProcesses.get(currentSProcesses.indexOf(openedProc)).getName());
        lbCurrMemVal.setText(String.valueOf(currentMemory + " KB"));
    }

    private void setValuesToOpened(SimplifiedProcess runningProc) {

        long currentMemory = runningProc.getMemory();
        long openedMemory = MainController.IMPORTED_PROCESSES.get(MainController.IMPORTED_PROCESSES.indexOf(runningProc)).getMemory();
        lbConclusion.setText(model.getMemoryDifferences(openedMemory, currentMemory));
        lbOpenedNameVal.setText(MainController.IMPORTED_PROCESSES.get(MainController.IMPORTED_PROCESSES.indexOf(runningProc)).getName());
        lbOpenedMemVal.setText(String.valueOf(openedMemory + " KB"));
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
