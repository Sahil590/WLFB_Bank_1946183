//The code used was provided in the lab. Action Server



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WLFB_ServerThread extends Thread{

    private Socket BankingActionSocket = null;
    private SharedState mySharedStateObject;
    private String myWLFB_ServerThreadName;
    private double mySharedVariable;

    //Setup the thread
    public WLFB_ServerThread(Socket BankingActionSocket, String WLFB_ServerThreadName, SharedState SharedObject) {

        //super(WLFB_ServerThreadName);
        this.BankingActionSocket = BankingActionSocket;
        mySharedStateObject = SharedObject;
        myWLFB_ServerThreadName = WLFB_ServerThreadName;
    }

    public void run() {
        try {
            System.out.println(myWLFB_ServerThreadName + "initialising.");
            PrintWriter out = new PrintWriter(BankingActionSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(BankingActionSocket.getInputStream()));
            String inputLine, outputLine;

            while ((inputLine = in.readLine()) != null) {
                // Get a lock first
                try {
                    mySharedStateObject.acquireLock();
                    outputLine = mySharedStateObject.processInput(myWLFB_ServerThreadName, inputLine);
                    System.out.println(outputLine);
                    out.println(outputLine);
                    mySharedStateObject.releaseLock();
                }
                catch(InterruptedException e) {
                    System.err.println("Failed to get lock when reading:"+e);
                }
            }

            out.close();
            in.close();
            BankingActionSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
