package Service;



import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private static final AccountDAO accountDAO = new AccountDAO();

    public static Account registerAccount(String username, String password) {
        // Check if the username is blank or password length is less than four
        if (username == null || username.trim().isEmpty() || password == null || password.length() < 4) {
            return null; // or throw an exception, depending on your design
        }

        // Check if the username is already taken
        if (AccountDAO.isUsernameAvailable(username)) {
            // Register the account
            // You may want to handle the case where the registration fails in your DAO
            boolean registrationSuccessful = AccountDAO.createAccount(username, password);
            
            if (registrationSuccessful) {
                // Return the created account
                AccountDAO daoInstance = new AccountDAO();
                //return AccountDAO.getAccountByUsername(username);
                return daoInstance.getAccountByUsername(username);
            }
        }

        // Return null if registration fails
        return null;
    }

    public static Account login(String username, String password) {
        // Validate login credentials
        if (accountDAO.validateLogin(username, password)) {
            // Return the authenticated account
            return accountDAO.getAccountByUsername(username);
        }
        return null;
    }
    public static boolean isUsernameAvailable(String username) {
        // You can implement this method by calling the corresponding method in AccountDAO
        return AccountDAO.isUsernameAvailable(username);
    }
    public static boolean isUsernameTaken(String username) {
        // Delegate to the AccountDAO to check if the username is taken
         
        return AccountDAO.isUsernameAvailable(username);
    }
}


