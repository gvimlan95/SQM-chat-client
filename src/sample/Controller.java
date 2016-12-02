package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

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
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Thread t;


    public void connectBtnClicked(ActionEvent event){
        try {
            socket = new Socket(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

//            disconnectBtn.setDisable(false);
//            connectBtn.setDisable(true);
//            registerBtn.setDisable(false);
//            onlineUsersTextArea.setDisable(false);
//            myCombobox.setDisable(false);
//            sendBtn.setDisable(false);
            activateGUIBtn(false);

        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }
        try {
            messageTextArea.appendText(in.readLine()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectBtnClicked(ActionEvent event){
            try{
                if(socket != null) {
                    t.interrupt();
                    out.println("QUIT");
                    in.close();
                    out.close();
                    socket.close();
                }
//                disconnectBtn.setDisable(true);
//                connectBtn.setDisable(false);
                usernameTextField.setDisable(false);
//                onlineUsersTextArea.setDisable(true);
//                myCombobox.setDisable(true);
//                sendBtn.setDisable(true);
                activateGUIBtn(true);
            }
            catch(Exception e) {} // not much else I can do

    }

    public void sendButtonClicked(ActionEvent event){
        out.println(inputMessageTextField.getText());
        inputMessageTextField.setText("");
    }

    public void registerUser(ActionEvent event){
        String username = usernameTextField.getText();
        out.println("IDEN "+username);
        try {
            String uname = in.readLine();
            if(uname.startsWith("REGDONE")){
                messageTextArea.appendText(uname.substring(8)+"\n");
                startServerIncoming();
                out.println("LIST");

                inputMessageTextField.setDisable(false);
                onlineUsersTextArea.setDisable(false);
                registerBtn.setDisable(true);
                usernameTextField.setDisable(true);

            }else{
                messageTextArea.appendText(uname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServerIncoming(){
        t = new Thread(() -> {
            while (!t.isInterrupted() && !socket.isClosed()) {
                try {
//                    String data = in.readLine();
                    validateMessage(in.readLine());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void populateUserDropdownMessage(String[] data){
        myCombobox.getItems().clear();
//        myCombobox.getItems().add("HAIL");
        myCombobox.getItems().addAll(data);
    }

    private void populateOnlineUserList(String[] data){
        onlineUsersTextArea.clear();
        for (String aData : data) {
            onlineUsersTextArea.appendText(aData + "\n");
        }
    }

    private void validateMessage(String data){
        if (data.startsWith("BROADCAST")) {
            messageTextArea.appendText(data.substring(10));
        }else if(data.startsWith("USERLIST")){

            String[] userList = data.substring(9).split(", ");

//            ArrayList<String> userList = new ArrayList<>();
//            for (String ul : ulist) {
//                userList.add(ul);
//            }
////            for(String ul : userList){
////                onlineUsersTextArea.appendText(ul + "\n");
////            }

            populateUserDropdownMessage(userList);
//            populateOnlineUserList(userList);
        } else {
            messageTextArea.appendText(data+"\n");
        }
    }

    public void refreshOnlineUserList(ActionEvent event){
        out.println("LIST");
        System.out.println("Refresh button clicked");
    }

    private void activateGUIBtn(boolean disabled){
        // if the user successfully connected , disabled = false
        disconnectBtn.setDisable(disabled);
        connectBtn.setDisable(!disabled);
        registerBtn.setDisable(disabled);
        onlineUsersTextArea.setDisable(disabled);
        myCombobox.setDisable(disabled);
        sendBtn.setDisable(disabled);
        userListRefreshBtn.setDisable(disabled);

//        // if the user successfully connected, disable = true
//        disconnectBtn.setDisable(isDisabled);
//        connectBtn.setDisable(!isDisabled);
//        registerBtn.setDisable(isDisabled);
//        onlineUsersTextArea.setDisable(!isDisabled);
//        myCombobox.setDisable();
//        sendBtn.setDisable();
//
//        // if the user successfully disconnected, activate = false
//        disconnectBtn.setDisable(true);
//        connectBtn.setDisable(false);
//        usernameTextField.setDisable(false);
//        onlineUsersTextArea.setDisable(true);
//        myCombobox.setDisable(true);
//        sendBtn.setDisable(true);
    }
}
