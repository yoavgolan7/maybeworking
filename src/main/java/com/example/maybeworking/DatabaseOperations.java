package com.example.maybeworking;

import java.sql.*;

public class DatabaseOperations {
    public static void insertTask(String name, String worker, boolean completed) {
        String insertSQL = "INSERT INTO tasks (name, worker, completed) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, worker);
            preparedStatement.setBoolean(3, completed);
            preparedStatement.executeUpdate();
            System.out.println("Task inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getTasks() {
        String selectSQL = "SELECT * FROM tasks";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSQL)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String worker = resultSet.getString("worker");
                boolean completed = resultSet.getBoolean("completed");
                System.out.println("ID: " + id + ", Name: " + name + ", Worker: " + worker + ", Completed: " + completed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTask(int id, String name, String worker, boolean completed) {
        String updateSQL = "UPDATE tasks SET name = ?, worker = ?, completed = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, worker);
            preparedStatement.setBoolean(3, completed);
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
            System.out.println("Task updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(int id) {
        String deleteSQL = "DELETE FROM tasks WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Task deleted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}