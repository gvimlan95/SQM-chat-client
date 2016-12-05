package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import static org.junit.Assert.*;
import static org.loadui.testfx.Assertions.verifyThat;
import static org.loadui.testfx.GuiTest.find;
import static org.loadui.testfx.controls.Commons.hasText;

public class ControllerUITest extends GuiTest {

    protected Parent getRootNode() {
        Parent parent = null;
        try{
            parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
            return parent;
        }catch(IOException e){}
        return parent;
    }

    @org.junit.Test
    public void checkHostTextFieldAndPortTextField(){
        TextField hostField = find("#hostTextField");
        hostField.setText("localhost");
        verifyThat("#hostTextField",hasText("localhost"));

        TextField portField = find("#portTextField");
        verifyThat("#portTextField",hasText(String.valueOf(9000)));

        Button connectBtn = find("#connectBtn");
        click(connectBtn);
    }

    @org.junit.Test
    public void checkLogin(){
        Button connectBtn = find("#connectBtn");
        click(connectBtn);

        TextField usernameTextField = find("#usernameTextField");
        usernameTextField.setText("username");
        verifyThat("#usernameTextField",hasText("username"));

        Button registerBtn = find("#registerBtn");
        click(registerBtn);
    }

    @org.junit.Test
    public void enterMessage(){
        Button connectBtn = find("#connectBtn");
        click(connectBtn);

        Button registerBtn = find("#registerBtn");
        click(registerBtn);

        TextField inputMessageTextField = find("#inputMessageTextField");
        inputMessageTextField.setText("HAIL hi");
        verifyThat("#inputMessageTextField",hasText("HAIL hi"));

        Button sendBtn = find("#sendBtn");
        click(sendBtn);
    }

    @org.junit.Test
    public void disconnectServer(){
        Button connectBtn = find("#connectBtn");
        click(connectBtn);

        Button registerBtn = find("#registerBtn");
        click(registerBtn);

        Button disconnectBtn = find("#disconnectBtn");
        click(disconnectBtn);

    }

}

