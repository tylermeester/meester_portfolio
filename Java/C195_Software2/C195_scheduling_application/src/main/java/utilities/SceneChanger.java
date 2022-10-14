package utilities;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import meester.scheduling_application.Main;

import java.io.IOException;

public class SceneChanger {

    /**
     * Changes the current scene.
     *
     * @param fxmlDocument the new scene
     * @param actionEvent the ActionEvent initiating the method
     * @throws IOException when changing scenes
     */
    public static void changeScene(String fxmlDocument, ActionEvent actionEvent) throws IOException {

        Stage stage;

        //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

        //Creates a new FXMLLoader object that references the .fxml document for the next scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlDocument));

        //Creates a new scene using the fxmlLoader object that was just created
        Scene newScene = new Scene(fxmlLoader.load());

        //Changes the scene to 'newScene' which was just created
        stage.setScene(newScene);

        stage.centerOnScreen();

        //Shows the stage with the new scene
        stage.show();
    }

    /**
     * Changes the current scene.
     *
     * @param fxmlDocument the new scene
     * @param keyEvent the keyEvent initiating the method
     * @throws IOException when changing scenes
     */
    public static void changeScene(String fxmlDocument, KeyEvent keyEvent) throws IOException {

        Stage stage;

        //Trying to use this line but for a keyEvent
        stage = (Stage) ((Button) keyEvent.getSource()).getScene().getWindow();

        //Creates a new FXMLLoader object that references the .fxml document for the next scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlDocument));

        //Creates a new scene using the fxmlLoader object that was just created
        Scene newScene = new Scene(fxmlLoader.load());

        //Changes the scene to 'newScene' which was just created
        stage.setScene(newScene);

        //Shows the stage with the new scene
        stage.show();
    }





}
