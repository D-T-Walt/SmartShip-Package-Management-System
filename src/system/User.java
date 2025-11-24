package system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	protected int userID;
	protected String firstName;
	protected String lastName;
	protected String hashPassword;
	
	public User(int userID, String firstName, String lastName, String hashPassword) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hashPassword = hashPassword;
	}
	
	public User() {
		this.userID = -1;
		this.firstName = "first";
		this.lastName = "last";
		this.hashPassword = "pass";
	}
	
	public User(User obj) {
		this.userID = obj.userID;
		this.firstName = obj.firstName;
		this.lastName = obj.lastName;
		this.hashPassword = obj.hashPassword;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHashPassword() {
		return hashPassword;
	}

	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	@Override
	public String toString() {
		return "User's Info\nUser ID: " + userID + "\nFirst Name: " + firstName + "\nLast Name: " + lastName + "\n";
	}
	
	public static String hashPassword(String input) {
        try {
            //Create a SHA-256 message digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            //Compute the hash bytes
            byte[] hashBytes = digest.digest(input.getBytes());
            
            //Convert bytes to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Unable to hash string", e);
        }
    }
}
