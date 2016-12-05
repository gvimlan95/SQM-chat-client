package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Server {

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private Thread t;
    private Controller controller;
    public boolean isPortOpen;

    Server(String hostname, int port,Controller controller){
        this.controller = controller;
        try {
            socket = new Socket(hostname, port);
            isPortOpen = true;
        } catch (IOException e) {
            System.out.println("Port is closed");
            isPortOpen = false;
        }
    }

    public void serverStart(){
        if(isPortOpen) {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                startServerIncoming();
            } catch (IOException e) {
                validateMessage("Error: In or Out failed");
            }
        }
    }

     public void registerUser(String username){
         out.println("IDEN " + username);
    }

    public void sendMessage(String message){
        out.println(message);
    }

    private void startServerIncoming(){
        t = new Thread(new Runnable() {
            public void run() {
                while (!t.isInterrupted() && !socket.isClosed()) {
                    try {
                        validateMessage(in.readLine()+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        t.start();
    }

    public void disconnectUser(){
        try {
            if(t != null){
                t.interrupt();
            }
            out.println("QUIT");
            in.close();
            out.close();
            socket.close();
        }catch(IOException e){
            System.out.println("Error");
        }
    }

    private void validateMessage(String data){
        if (data.startsWith("BROADCAST")) {
            controller.updateMessageArea(data);

        }else if(data.startsWith("WCS1")){
            controller.updateMessageArea(data);
            controller.activateGUIBtn(false);

        }else if(data.startsWith("WCS2")){
            controller.updateMessageArea(data);
            controller.activateRegisterGUIBtn(true);

        } else if(data.startsWith("REGDONE")){
            controller.updateMessageArea(data);
            controller.activateRegisterGUIBtn(true);
            out.println("LIST");

        }else if(data.startsWith("QUIT")){
            controller.activateGUIBtn(true);

        }else if(data.startsWith("Error")) {
            controller.updateMessageArea(data);

        }else if(data.startsWith("MESGSENT")){
            controller.updateMessageArea(data);

        }else if(data.startsWith("PM")){
            controller.updateMessageArea(data);
        }else if(data.startsWith("USERLIST")){
            String[] ulist = data.substring(9).split(", ");
            controller.populateOnlineUserList(ulist);
        }
    }
}
