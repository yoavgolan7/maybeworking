package com.example.maybeworking;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {
    @FXML
    private TextField taskNameField;

    @FXML
    private TextField workerNameField;

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private TableColumn<Task, String> nameColumn;

    @FXML
    private TableColumn<Task, String> workerColumn;

    @FXML
    private TableColumn<Task, Boolean> completeColumn;

    private ObservableList<Task> taskList;

    @FXML
    public void initialize() {
        taskList = FXCollections.observableArrayList();
        taskTableView.setItems(taskList);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("worker"));
        completeColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        completeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(completeColumn));

        loadTasksFromDatabase();
    }

    private void loadTasksFromDatabase() {
        try (Connection connection = DatabaseSetup.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM tasks")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String worker = resultSet.getString("worker");
                boolean completed = resultSet.getBoolean("completed");
                Task task = new Task(name, worker, completed);
                taskList.add(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTask() {
        String taskName = taskNameField.getText();
        String workerName = workerNameField.getText();

        if (!taskName.isEmpty() && !workerName.isEmpty()) {
            Task task = new Task(taskName, workerName, false);
            taskList.add(task);

            // Clear the text fields
            taskNameField.clear();
            workerNameField.clear();

            System.out.println("Task added: " + task);
        }
    }

    @FXML
    private void handleSaveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
            for (Task task : taskList) {
                writer.write(task.toString());
                writer.newLine();
            }
            System.out.println("Tasks saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}