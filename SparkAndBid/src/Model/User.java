/**
 * 
 */
package Model;

/**
 * @author thusitha
 *
 */
public class User {

	/**
	 * String username
	 */
	private String username;
	
	/**
	 * String password
	 */
	private String password;

	/**
	 * String adresse
	 */
	private String adresse;
	
	/**
	 * String adresseMail
	 */
	
	private String mail;
	
	/**
	 * @param username
	 * @param password
	 * @param adresse
	 */	
	
	public User() {
		super();
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}
	
	/**
	 * @param adresseMail the adresseMail to set
	 */
	public void setAdresseMail(String adresseMail) {
		this.mail = adresseMail;
	}
}
