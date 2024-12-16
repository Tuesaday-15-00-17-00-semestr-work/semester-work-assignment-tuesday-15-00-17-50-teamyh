package org.example.frontend;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Запустить начальную страницу (например, логин)
        new LoginPage().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
