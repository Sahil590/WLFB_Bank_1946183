//The code used was provided in the lab. Action Server



import java.io.*;
import java.net.*;

public class Client2 {
    public static void main(String[] args) throws IOException {

        // Set up the socket, in and out variables

        Socket BankingClientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        int BankingSocketNumber = 4545;
        String WLFB_ServerName = "localhost";
        String BankingClientID = "Client2";

        try {
            BankingClientSocket = new Socket(WLFB_ServerName, BankingSocketNumber);
            out = new PrintWriter(BankingClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(BankingClientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "+ BankingSocketNumber);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        System.out.println("Initialised " + BankingClientID + " client and IO connections");

        System.out.println("Menu");
        System.out.println("Please type the letter option you would like");
        System.out.println("A Deposit");
        System.out.println("B Withdraw");
        System.out.println("C Transfer");
        System.out.println("D Balance");


        // This is modified as it's the client that speaks first

        while (true) {

            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println(BankingClientID + " sending " + fromUser + " to WLFB_Server");
                out.println(fromUser);
            }
            fromServer = in.readLine();
            System.out.println(BankingClientID + " received " + fromServer + " from WLFB_Server");
        }

    }
}
