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
    private BufferedReader in;
    private PrintWriter out;
    Socket socket;
    private volatile boolean running;


    public void connectBtnClicked(ActionEvent event){
        try {
            socket = new Socket(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("in or out failed");
            System.exit(-1);
        }
        try {
            messageTextArea.appendText(in.readLine()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = true;



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String data = in.readLine();
                        if (data.startsWith("BROADCAST")) {
                            messageTextArea.appendText(data.substring(10));
                        }else if(data.startsWith("USERLIST")){
//                            onlineUsersTextArea.appendText(data.split(",")+"\n");
                        } else {
                            messageTextArea.appendText(data+"\n");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                        out.println("LIST");
//                        System.out.println(in.readLine());
//                } catch (IOException e) {
//                }
//            }
//        }).start();
    }

    public void disconnectBtnClicked(ActionEvent event){
        System.out.println("Disconnect btn clicked");
    }

    public void sendButtonClicked(ActionEvent event){
        out.println(inputMessageTextField.getText());
        inputMessageTextField.setText("");
//        try {
//            String input = in.readLine();
//            messageTextArea.appendText(input+"\n");
//            System.out.println(input);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void registerUser(ActionEvent event){
        String username = usernameTextField.getText();
        out.println("IDEN "+username);
        try {
            String uname = in.readLine();
            if(uname.startsWith("REGDONE")){
                messageTextArea.appendText(uname.substring(8)+"\n");
//                System.out.println(uname.substring(8));
                sendBtn.setDisable(false);
                inputMessageTextField.setDisable(false);
            }else{
                messageTextArea.appendText(uname);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
