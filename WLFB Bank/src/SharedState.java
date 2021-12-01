//The code used was provided in the lab. Action Server


public class SharedState {
    private double Client1Acc, Client2Acc, Client3Acc;
    private boolean accessing = false; // true a thread has a lock, false otherwise
    private int threadsWaiting = 0; // number of waiting writers
    private String[] outputs = new String[4];

// Constructor

    SharedState(double[] transaction) {
        Client1Acc = transaction[0];
        Client2Acc = transaction[1];
        Client3Acc = transaction[2];
    }

//Attempt to aquire a lock

    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName() + " is attempting to acquire a lock!");
        ++threadsWaiting;
        while (accessing) {  // while someone else is accessing or threadsWaiting > 0
            System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
            //wait for the lock to be released - see releaseLock() below
            wait();
        }
        --threadsWaiting;                                                                              // nobody has got a lock so get one
        accessing = true;
        System.out.println(me.getName() + " got a lock!");
    }

    // Releases a lock to when a thread is finished

    public synchronized void releaseLock() {
        //release the lock and tell everyone
        accessing = false;
        notifyAll();
        Thread me = Thread.currentThread(); // get a ref to the current thread
        System.out.println(me.getName() + " released a lock!");
    }


    /* The processInput method */

    public synchronized String processInput(String myThreadName, String theInput) {
        System.out.println("Thread " + myThreadName + " wants to do action " + theInput);
        //String theOutput = null;
        String theOutput = "Something went wrong";
        int transfer = 50;
        //Deposit Funds into any account
        // Check what the client said
        if (theInput.equalsIgnoreCase("A")) {
            //Correct request
            if (myThreadName.equals("BankingThread1")) {
                Client1Acc += 50;
                System.out.println("Client" + myThreadName + " has added 50 units to their account the new balance is " + Client1Acc);
                outputs[0] = "50 units have been deposited. Client1 account has been updated = " + Client1Acc;
                theOutput = "Client " + myThreadName + "'s account has been updated = " + Client1Acc;
            } else if (myThreadName.equals("BankingThread2")) {

                Client2Acc += 50;
                System.out.println("Client" + myThreadName + " has added 50 to their account the new balance is " + Client2Acc);
                outputs[0] = "50 units have been deposited. Client2 account has been updated = " + Client2Acc;

            } else if (myThreadName.equals("BankingThread3")) {

                Client3Acc +=  50;
                System.out.println("Client" + myThreadName + " has added 50 to their account the new balance is " + Client3Acc);
                outputs[0] = "50 units have been deposited. Client3 account has been updated = " + Client3Acc;


            }  else {
                System.out.println("Error - thread call not recognised.");
            }
        } else { //incorrect request
            outputs[0] = myThreadName + " received incorrect request - only understand \"Do my action!\"";

        }
//Withdraw funds from any account
        if (theInput.equalsIgnoreCase("B")) {
            if (myThreadName.equals("BankingThread1")) {

                Client1Acc -=  50;
                System.out.println("Client" + myThreadName + " has withdrew 50 from their account. The new balance is " + Client1Acc);
                outputs[1] = "50 untis have been withdrawn from their account. Client1's account has been updated = " + Client1Acc;

            } else if (myThreadName.equals("BankingThread2")) {

                Client2Acc -=  50;
                System.out.println("Client" + myThreadName + " has withdrew 50 from their account. The new balance is " + Client2Acc);
                outputs[1] = "50 untis have been withdrawn from their account. Client2's account has been updated = " + Client2Acc;

            } else if (myThreadName.equals("BankingThread3")) {

                Client3Acc -=  50;
                System.out.println("Client" + myThreadName + " has withdrew 50 from their account. The new balance is " + Client3Acc);
                outputs[1] = "50 untis have been withdrawn from their account. Client3's account has been updated = " + Client3Acc;

            }  else {
                System.out.println("Error - thread call not recognised.");
            }
        }
//Transfer Funds to any account
        if (theInput.equalsIgnoreCase("C")) {
            if (myThreadName.equals("BankingThread1")) {
                System.out.println("client 1 -> "+ Client1Acc);
                System.out.println("client 2 -> "+ Client2Acc);
                Client1Acc -= transfer;
                Client2Acc += transfer;
                System.out.println("client 1 -> "+ Client1Acc);
                System.out.println("client 2 -> "+ Client2Acc);


               // outputs[2] =  myThreadName + "'s has sent 20 units to client 2. Client 1 new balacnce is  = " + Client1Acc;
                outputs[2] = "A transfer of 50 units sent to client 2. Client 1 now has " + Client1Acc + " units in his account";

            }

            if (myThreadName.equalsIgnoreCase("BankingThread2")) {
                System.out.println("client 2 -> "+ Client2Acc);
                System.out.println("client 1 -> "+ Client1Acc);
                Client2Acc -= transfer;
                Client1Acc += transfer;
                System.out.println("client 2 -> "+ Client2Acc);
                System.out.println("client 1 -> "+ Client1Acc);

                outputs[2] = "A transfer of 50 units sent to client 2. Client 1 now has " + Client2Acc + " units in his account";
                // outputs[2] = "Client 1 now has " + Client1Acc;
                // theOutput="i transferred";

            }
            if (myThreadName.equals("BankingThread3")) {

                System.out.println("client 3 -> "+ Client3Acc);
                System.out.println("client 2 -> "+ Client2Acc);
                Client3Acc -= transfer;
                Client2Acc += transfer;
                System.out.println("client 3 -> "+ Client3Acc);
                System.out.println("client 2 -> "+ Client2Acc);

                outputs[2] = "A transfer of 50 units sent to client 2. Client 1 now has " + Client3Acc + " units in his account";
                //outputs[2] = "Client 1 now has " + Client2Acc;
            }
        }
//Check your balance here
        if(theInput.equalsIgnoreCase("D")){
            if(myThreadName.equals("BankingThread1")){
                System.out.println("Client " + myThreadName + "'s account has been updated = " + Client1Acc);
                outputs[3] = myThreadName + ": Your balance is " + Client1Acc;
            }
            else if(myThreadName.equals("BankingThread2")){
                outputs[3] = myThreadName + ": Your balance is " + Client2Acc;
            }
            else if(myThreadName.equals("BankingThread3")){
                outputs[3] = myThreadName + ": Your balance is " + Client3Acc;
            }
        }
        //Return the output message to the Banking WLFB_Server
        //System.out.println(theOutput);

        if(theInput.equalsIgnoreCase("A")){ return outputs[0];}
        if(theInput.equalsIgnoreCase("B")){ return outputs[1];}
        if(theInput.equalsIgnoreCase("C")){ return outputs[2];}
        if(theInput.equalsIgnoreCase("D")){ return outputs[3];}

        return theOutput;
    }
}
