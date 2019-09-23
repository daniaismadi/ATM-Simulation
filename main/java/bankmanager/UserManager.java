package bankmanager;
import account.Account;
import atm.*;

import java.io.*;
import java.util.ArrayList;

/**
 * The User Manager handles all requests related to {@link User} delegated from {@link BankManager}.
 */
class UserManager implements Serializable {

    /**
     * Instance of the ATM being used.
     */
    private final ATM atm;

    /**
     * User Manager Constructor.
     * @param atm instance of {@link ATM} representing the ATM being used
     */
    public UserManager(ATM atm) {
        this.atm = atm;

    }

    /***
     * Creates and returns a new user.
     *
     * @param username the username this user uses to log in
     * @param password the password this user uses to log in
     * @return the new {@link User} created
     */
    public User createUser(String username, String password){
        ArrayList<Account> accounts = new ArrayList<>();
        boolean contains = false;
        for (User parameter : atm.getListOfUsers()) {
            if (parameter.getUsername().equals(username)) {
                contains = true;
            }
        } if (!contains){
            User newUser = new User(username, password, accounts);
            System.out.println("New user: " + username + " created");
            atm.addUserToList(newUser);
            return newUser;
        } else{
            System.out.println("User name already exists, please try a different name");
        }
        return null;
    }

    /**
     * Used in serialization to store the User Manager object.
     * @param oos instance of the ObjectOutputStream class to write the user manager object
     * @throws IOException if an IO error occurs.
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        try {
            oos.defaultWriteObject();
        } catch (IOException e){
            System.out.println("BM writeObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization to restore the user manager's information after the ATM is restarted.
     *
     * @param ois instance of the ObjectInputStream class used to read the user manager object.
     * @throws ClassNotFoundException if the class of the serialized object could not be found.
     * @throws IOException if an IO error occurs
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
        try{
            ois.defaultReadObject();
        } catch (Exception e){
            System.out.println("BM readObject Failed!");
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Used in serialization when class inheritance is not as expected
     * @throws ObjectStreamException when an attempt to deserialize a back-reference fails.
     */
    private void readObjectNoData() throws ObjectStreamException {
        System.out.println("BM readObjectNoData, this should never happen!");
        System.exit(-1);
    }
}
