/**
 * 
 */
package Model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

/**
 * @author thusitha
 *
 */
public class Bid {

	/**
	 * Collection<User> user
	 */
	private Collection<User> users;
	
	/**
	 * int idProduct
	 */
	private int idProduct;
	
	/**
	 * String userName
	 */
	private String userName;
	
	/**
	 * String date
	 */
	private Timestamp date;
	
	/**
	 * String price
	 */
	private Float price;

	/**
	 * Contrstructor
	 */
	public Bid() {
		super();
	}

	/**
	 * @return the users
	 */
	public Collection<User> getUsers() {
		return users;
	}

	/**
	 * @param users the Collection<User> to set
	 */
	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	/**
	 * @return the idProduct
	 */
	public int getIdProduct() {
		return idProduct;
	}

	/**
	 * @param idProduct the idProduct to set
	 */
	public void setIdProduct(int idProduct) {
		this.idProduct = idProduct;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Timestamp date) {
		this.date = date;
	}

	/**
	 * @return the price
	 */
	public Float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Float price) {
		this.price = price;
	}
}