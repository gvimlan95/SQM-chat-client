package sample;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * Created by vimlang on 04/12/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerUnitTest {

    private Server server;
    private Controller controller;

    @Test
    public void serverInitialized() throws Exception {
        server = new Server("localhost",9000,controller);
        server.serverStart();
        assertEquals(true,server!=null);

        server.registerUser("Username");
        assertEquals(true,server!=null);

        server.disconnectUser();
        assertEquals(true,server!=null);

    }
//
//    @Test
//    public void userRegistered(){
//        server.registerUser("Username");
//        assertEquals(true,server!=null);
//    }
//
//    @Test
//    public void userDisconnect(){
//        server.disconnectUser();
//        assertEquals(false,server!=null);
//    }

}