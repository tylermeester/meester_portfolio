package utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.*;

import static utilities.DBConnection.connection;

public class DynamicTableview {

    public static ObservableList<ObservableList> dynamicTableViewData = FXCollections.observableArrayList();

    /**
     * Populates a TableView with the results of a SQL Query.
     *
     * @param sqlStatement the SQL Query to be executed
     * @param tableView the TableView to be populated with data
     * @throws SQLException when accessing the database
     */
    public static void generateTableView(String sqlStatement, TableView tableView) throws SQLException {

        tableView.getItems().clear();
        tableView.getColumns().clear();
        dynamicTableViewData.clear();


        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

            ResultSet result = preparedStatement.executeQuery();

            //Dynamically adding columns to currentTableView by iterating through the query ResultSet
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;

                //Creating the new TableColumn object and saving the Database column name to it
                TableColumn column = new TableColumn(result.getMetaData().getColumnName(i + 1));


                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty((String) param.getValue().get(j));
                    }});

                tableView.getColumns().addAll(column);
            }

            //Adding data to the TableView
            while (result.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {


                    if(result.getString(i).contains(":")){
                        String localDateTimeString = TimeUtility.convertTimestampToLocalDateTimeString(result.getString(i));
                        row.add(localDateTimeString);
                    }

                    else
                        row.add(result.getString(i));
                }

                dynamicTableViewData.add(row);
            }
            tableView.setItems(dynamicTableViewData);

        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }
    }

    /**
     * Populates a TableView with the results of a Prepared SQL Query.
     *
     * @param preparedStatement the prepared SQL Query to be executed
     * @param tableView the TableView to be populated with data
     */
    public static void generateTableView(PreparedStatement preparedStatement, TableView tableView){

        dynamicTableViewData.clear();
        tableView.getItems().clear();
        tableView.getColumns().clear();


        try {

            ResultSet result = preparedStatement.executeQuery();

            //Dynamically adding columns to currentTableView by iterating through the query ResultSet
            for (int i = 0; i < result.getMetaData().getColumnCount(); i++) {
                final int j = i;

                //Creating the new TableColumn object and saving the Database column name to it
                TableColumn column = new TableColumn(result.getMetaData().getColumnName(i + 1));


                column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty((String) param.getValue().get(j));
                    }});

                tableView.getColumns().addAll(column);
            }

            //Adding data to the TableView
            while (result.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {


                    if(result.getString(i).contains(":")){
                        String localDateTimeString = TimeUtility.convertTimestampToLocalDateTimeString(result.getString(i));
                        row.add(localDateTimeString);
                    }
                    else
                        row.add(result.getString(i));
                }

                dynamicTableViewData.add(row);
            }
            tableView.setItems(dynamicTableViewData);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
        }


    }



}


