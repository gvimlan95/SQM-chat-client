package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML private TextField hostTextField;
    @FXML private TextField portTextField;
    @FXML private Button connectBtn;
    @FXML private Button disconnectBtn;
    @FXML private Button registerBtn;
    @FXML private TextField usernameTextField;
    @FXML private TextArea messageTextArea;
    @FXML private TextArea onlineUsersTextArea;
    @FXML private TextField inputMessageTextField;
    @FXML private Button sendBtn;
    @FXML private ComboBox myCombobox;
    @FXML private Button userListRefreshBtn;

    private Server server;


    public void refreshOnlineUserList(){
        server.sendMessage("LIST");
    }

    public void connectBtnClicked(){
        server = new Server(hostTextField.getText(), Integer.parseInt(portTextField.getText()),this);
        server.serverStart();
    }

    public void disconnectBtnClicked(){
        server.disconnectUser();
        usernameTextField.setDisable(false);
//        }
    }

    public void sendButtonClicked(){
        server.sendMessage(inputMessageTextField.getText());
        inputMessageTextField.setText("");
    }

    public void registerUser(){
        server.registerUser(usernameTextField.getText());
    }

    public void updateMessageArea(String text){
        messageTextArea.appendText(text);
    }

    public void activateGUIBtn(boolean disabled){
        disconnectBtn.setDisable(disabled);
        connectBtn.setDisable(!disabled);
        registerBtn.setDisable(disabled);
        onlineUsersTextArea.setDisable(disabled);
        myCombobox.setDisable(disabled);
        sendBtn.setDisable(disabled);
        userListRefreshBtn.setDisable(disabled);
    }

    public void activateRegisterGUIBtn(boolean isDisabled){
        inputMessageTextField.setDisable(!isDisabled);
        onlineUsersTextArea.setDisable(!isDisabled);
        registerBtn.setDisable(isDisabled);
        usernameTextField.setDisable(isDisabled);
    }

//    private void populateUserDropdownMessage(String[] data){
//        myCombobox.getItems().clear();
////        myCombobox.getItems().add("HAIL");
//        myCombobox.getItems().addAll(data);
//    }
//
    public void populateOnlineUserList(String[] data){
        onlineUsersTextArea.clear();
        for (String aData : data) {
            onlineUsersTextArea.appendText(aData + "\n");
        }
    }
}
