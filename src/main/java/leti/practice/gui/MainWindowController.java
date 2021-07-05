package leti.practice.gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainWindowController {
    private MainWindow mainWindow;
    private ViewType viewType = ViewType.ORIGINAL_GRAPH;
    private boolean intermediateMessagesEnabled = true;

    @FXML
    private TextArea console;

    @FXML
    private void initialize() {

    }

    @FXML
    private void buttonOpenPressed() {
        printMessageToConsole("Open graph button pressed!");
    }

    @FXML
    private void buttonSavePressed() {
        printMessageToConsole("Save graph button pressed!");
    }

    @FXML
    private void buttonExitPressed() {
        printMessageToConsole("Closing application...");
        mainWindow.endApplication();
    }

    @FXML
    private void radiobuttonOriginalGraphSelected() {
        if (viewType != ViewType.ORIGINAL_GRAPH) {
            viewType = ViewType.ORIGINAL_GRAPH;
            printMessageToConsole("Original graph view select!");
        }
    }

    @FXML
    private void radiobuttonResidualNetworkSelected() {
        if (viewType != ViewType.RESIDUAL_NETWORK) {
            viewType = ViewType.RESIDUAL_NETWORK;
            printMessageToConsole("Residual network view select!");
        }
    }

    @FXML
    private void radiobuttonHeightFunctionSelected() {
        if (viewType != ViewType.HEIGHT_FUNCTION){
            viewType = ViewType.HEIGHT_FUNCTION;
            printMessageToConsole("Height function view select!");
        }
    }

    @FXML
    private void checkboxIntermediateMessagesChecked() {
        intermediateMessagesEnabled = !intermediateMessagesEnabled;

        if (intermediateMessagesEnabled) {
            printMessageToConsole("Intermediate messages enabled!");
        } else {
            printMessageToConsole("Intermediate messages disabled!");
        }
    }

    @FXML
    private void buttonHelpPressed() {
        printMessageToConsole("Help button pressed!");
        mainWindow.showDialog(Alert.AlertType.INFORMATION, "Help", "Help", "Help description");
    }

    @FXML
    private void buttonAboutPressed() {
        printMessageToConsole("About button pressed!");
        String about = """
                The program provides the user with functionality for using the Golberg algorithm for various networks with the possibility of a detailed study of the algorithm's operation.

                Developers: Nikita Shakhin, Rodion Kolovanov, Irina Andrukh""";
        mainWindow.showDialog(Alert.AlertType.INFORMATION, "About", "About", about);
    }

    @FXML
    private void buttonStepBackwardPressed() {
        printMessageToConsole("Step backward button pressed!");
    }

    @FXML
    private void buttonStepForwardPressed() {
        printMessageToConsole("Step forward button pressed!");
    }

    @FXML
    private void buttonRunAlgorithmPressed() {
        printMessageToConsole("Run algorithm button pressed!");
    }

    @FXML
    private void buttonAddEdgePressed() {
        printMessageToConsole("Add edge button pressed!");
    }

    @FXML
    private void buttonRemoveEdgePressed() {
        printMessageToConsole("Remove edge button pressed!");
    }

    @FXML
    private void buttonClearGraphPressed() {
        printMessageToConsole("Clear graph button pressed!");
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void printMessageToConsole(String text) {
        console.appendText(text + "\n");
    }
}
