//The code used was provided in the lab. Action Server


import java.io.IOException;
import java.net.ServerSocket;

public class WLFB_Server {
    public static void main(String[] args) throws IOException {

        ServerSocket WLFB_ServerSocket = null;
        boolean listening = true;
        String WLFB_ServerName = "WLFB_Server";
        int WLFB_ServerNumber = 4545;

        double Client1Acc = 1000;
        double Client2Acc = 1000;
        double Client3Acc = 1000;

        double[] Account = {Client1Acc, Client2Acc, Client3Acc};
        //Create the shared object in the global scope...

        SharedState ourSharedStateObject = new SharedState(Account);



        // Make the server socket

        try {
            WLFB_ServerSocket = new ServerSocket(WLFB_ServerNumber);
        } catch (IOException e) {
            System.err.println("Could not start " + WLFB_ServerName + " specified port.");
            System.exit(-1);
        }
        System.out.println(WLFB_ServerName + " started");

        //Got to do this in the correct order with only three automated clients

        while (listening){
            new WLFB_ServerThread(WLFB_ServerSocket.accept(),
                    "BankingThread1", ourSharedStateObject).start();
            new WLFB_ServerThread(WLFB_ServerSocket.accept(),
                    "BankingThread2", ourSharedStateObject).start();
            new WLFB_ServerThread(WLFB_ServerSocket.accept(),
                    "BankingThread3", ourSharedStateObject).start();
            System.out.println("New " + WLFB_ServerName + " thread started.");
        }

        WLFB_ServerSocket.close();
    }
}
