package com.example.maybeworking;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private TableColumn<Task, String> completeColumn;

    @FXML
    private ComboBox<String> filterComboBox;

    private ObservableList<Task> taskList;
    private FilteredList<Task> filteredTaskList;

    @FXML
    public void initialize() {
        taskList = FXCollections.observableArrayList();
        filteredTaskList = new FilteredList<>(taskList, p -> true);
        taskTableView.setItems(filteredTaskList);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        workerColumn.setCellValueFactory(new PropertyValueFactory<>("worker"));
        completeColumn.setCellValueFactory(cellData -> {
            boolean completed = cellData.getValue().isCompleted();
            return new SimpleStringProperty(completed ? "Yes" : "No");
        });

        filterComboBox.setItems(FXCollections.observableArrayList("Show all", "Show complete", "Show incomplete"));
        filterComboBox.setValue("Show all");
        filterComboBox.setOnAction(event -> updateFilter());

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

    private void updateFilter() {
        String selectedFilter = filterComboBox.getValue();
        switch (selectedFilter) {
            case "Show complete":
                filteredTaskList.setPredicate(task -> task.isCompleted());
                break;
            case "Show incomplete":
                filteredTaskList.setPredicate(task -> !task.isCompleted());
                break;
            default:
                filteredTaskList.setPredicate(task -> true);
                break;
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
        String deleteSQL = "DELETE FROM tasks";
        String insertSQL = "INSERT INTO tasks (name, worker, completed) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseSetup.getConnection();
             Statement deleteStatement = connection.createStatement();
             PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {

            // Clear existing data
            deleteStatement.execute(deleteSQL);

            // Insert new data
            for (Task task : taskList) {
                insertStatement.setString(1, task.getName());
                insertStatement.setString(2, task.getWorker());
                insertStatement.setBoolean(3, task.isCompleted());
                insertStatement.addBatch();
            }

            insertStatement.executeBatch();
            System.out.println("Tasks saved to database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeStatus() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            selectedTask.setCompleted(!selectedTask.isCompleted());
            updateTaskCompletion(selectedTask);
            taskTableView.refresh();
        }
    }

    private void updateTaskCompletion(Task task) {
        String updateSQL = "UPDATE tasks SET completed = ? WHERE name = ? AND worker = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setBoolean(1, task.isCompleted());
            preparedStatement.setString(2, task.getName());
            preparedStatement.setString(3, task.getWorker());
            preparedStatement.executeUpdate();
            System.out.println("Task completion updated in database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTask() {
        Task selectedTask = taskTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            taskList.remove(selectedTask);
            deleteTaskFromDatabase(selectedTask);
        }
    }

    private void deleteTaskFromDatabase(Task task) {
        String deleteSQL = "DELETE FROM tasks WHERE name = ? AND worker = ?";

        try (Connection connection = DatabaseSetup.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

            preparedStatement.setString(1, task.getName());
            preparedStatement.setString(2, task.getWorker());
            preparedStatement.executeUpdate();
            System.out.println("Task deleted from database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}