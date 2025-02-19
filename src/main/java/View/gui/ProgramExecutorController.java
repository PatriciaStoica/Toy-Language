package View.gui;

import Controller.Controller;
import Exceptions.InterpreterException;
import Model.Collections.ISemaphoreTable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import Model.ProgramState.ProgramState;
import Model.Statements.IStatement;
import Model.Collections.IDictionary;
import Model.Collections.IHeap;
import Model.Collections.ILockTable;
import Model.Value.IValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Pair<T1, T2> {
    T1 first;
    T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
}

public class ProgramExecutorController {
    private Controller controller;

    @FXML
    private TextField numberOfProgramStatesTextField;

    @FXML
    private TableView<Pair<Integer, IValue>> heapTableView;

    @FXML
    private TableColumn<Pair<Integer, IValue>, Integer> addressColumn;

    @FXML
    private TableColumn<Pair<Integer, IValue>, String> valueColumn;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private ListView<Integer> programStateIdentifiersListView;

    @FXML
    private TableView<Pair<String, IValue>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, IValue>, String> variableNameColumn;

    @FXML
    private TableColumn<Pair<String, IValue>, String> variableValueColumn;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private Button runOneStepButton;

    @FXML
    private TableView<Pair<Integer, Integer>> lockTableView;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> locationColumn;

    @FXML
    private TableColumn<Pair<Integer, Integer>, String> lockValueColumn;

    @FXML
    private TableView<Pair<Integer, Pair<Integer, List<Integer>>>> semaphoreTableView;

    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> semaphoreIndexColumn;

    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> semaphoreValueColumn;
    @FXML
    private TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, String> semaphoreListColumn;


    public void setController(Controller controller) {
        this.controller = controller;
        populate();
    }

    @FXML
    public void initialize() {
        programStateIdentifiersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        locationColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().first.toString()));
        lockValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.toString()));
        semaphoreIndexColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().first).asObject());
        semaphoreValueColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().second.first).asObject());
        semaphoreListColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().second.second.toString()));
    }

    private ProgramState getCurrentProgramState() {
        if (controller.getProgramStates().size() == 0)
            return null;
        else {
            int currentId = programStateIdentifiersListView.getSelectionModel().getSelectedIndex();
            if (currentId == -1)
                return controller.getProgramStates().get(0);
            else
                return controller.getProgramStates().get(currentId);
        }
    }

    private void populate() {
        populateHeapTableView();
        populateOutputListView();
        populateFileTableListView();
        populateProgramStateIdentifiersListView();
        populateSymbolTableView();
        populateExecutionStackListView();
        populateLockTableView();
        populateSemaphoreTableView();
    }

    @FXML
    private void changeProgramState(MouseEvent event) {
        populateExecutionStackListView();
        populateSymbolTableView();
    }

    private void populateNumberOfProgramStatesTextField() {
        List<ProgramState> programStates = controller.getProgramStates();
        numberOfProgramStatesTextField.setText(String.valueOf(programStates.size()));
    }

    private void populateHeapTableView() {
        ProgramState programState = getCurrentProgramState();
        IHeap heap = Objects.requireNonNull(programState).getHeap();
        ArrayList<Pair<Integer, IValue>> heapEntries = new ArrayList<>();
        for(Map.Entry<Integer, IValue> entry: heap.getContent().entrySet()) {
            heapEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        heapTableView.setItems(FXCollections.observableArrayList(heapEntries));
    }

    private void populateOutputListView() {
        ProgramState programState = getCurrentProgramState();
        List<String> output = new ArrayList<>();
        List<IValue> outputList = Objects.requireNonNull(programState).getOut().getList();
        int index;
        for (index = 0; index < outputList.size(); index++){
            output.add(outputList.get(index).toString());
        }
        outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView() {
        ProgramState programState = getCurrentProgramState();
        List<String> files = new ArrayList<>(Objects.requireNonNull(programState).getFileTable().getContent().keySet());
        fileTableListView.setItems(FXCollections.observableList(files));
    }

    private void populateProgramStateIdentifiersListView() {
        List<ProgramState> programStates = controller.getProgramStates();
        List<Integer> idList = programStates.stream().map(ProgramState::getId).collect(Collectors.toList());
        programStateIdentifiersListView.setItems(FXCollections.observableList(idList));
        populateNumberOfProgramStatesTextField();
    }

    private void populateSymbolTableView() {
        ProgramState programState = getCurrentProgramState();
        IDictionary<String, IValue> symbolTable = Objects.requireNonNull(programState).getSymTable();
        ArrayList<Pair<String, IValue>> symbolTableEntries = new ArrayList<>();
        for (Map.Entry<String, IValue> entry: symbolTable.getContent().entrySet()) {
            symbolTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateExecutionStackListView() {
        ProgramState programState = getCurrentProgramState();
        List<String> executionStackToString = new ArrayList<>();
        if (programState != null)
            for (IStatement statement: programState.getExeStack().getReversed()) {
                executionStackToString.add(statement.toString());
            }
        executionStackListView.setItems(FXCollections.observableList(executionStackToString));
    }

    private void populateLockTableView() {
        ProgramState programState = getCurrentProgramState();
        ILockTable lockTable = Objects.requireNonNull(programState).getLockTable();
        ArrayList<Pair<Integer, Integer>> lockTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: lockTable.getContent().entrySet()) {
            lockTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        lockTableView.setItems(FXCollections.observableArrayList(lockTableEntries));
    }

    private void populateSemaphoreTableView() {
        ProgramState programState = getCurrentProgramState();
        ISemaphoreTable semaphoreTable = Objects.requireNonNull(programState).getSemaphoreTable();
        ArrayList<Pair<Integer, Pair<Integer, List<Integer>>>> semaphoreTableEntries = new ArrayList<>();
        for(Map.Entry<Integer, javafx.util.Pair<Integer, List<Integer>>> entry: semaphoreTable.getContent().entrySet()) {
            semaphoreTableEntries.add(new Pair<>(entry.getKey(), new Pair<>(entry.getValue().getKey(), entry.getValue().getValue())));
        }
        semaphoreTableView.setItems(FXCollections.observableArrayList(semaphoreTableEntries));
    }

    @FXML
    private void runOneStep(MouseEvent mouseEvent) {
        if (controller != null) {
            try {
                List<ProgramState> programStates = Objects.requireNonNull(controller.getProgramStates());
                if (programStates.size() > 0) {
                    controller.oneStep();
                    populate();
                    programStates = controller.removeCompletedPrg(controller.getProgramStates());
                    controller.setProgramStates(programStates);
                    populateProgramStateIdentifiersListView();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error!");
                    alert.setHeaderText("An error has occured!");
                    alert.setContentText("There is nothing left to execute!");
                    alert.showAndWait();
                }
            } catch (InterpreterException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Execution error!");
                alert.setHeaderText("An execution error has occured!");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
            alert.setHeaderText("An error has occured!");
            alert.setContentText("No program selected!");
            alert.showAndWait();
        }
    }
}