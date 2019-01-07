/**
 * 
 */
package Model;

/**
 * @author thusitha
 *
 */
public class Product implements Comparable {
	
	/**
	 * Integer id
	 */
	private Integer id;
	
	/**
	 * String stockCode
	 */
	private String stockCode;
	
	/**
	 * String description (used as product name)
	 */
	private String description;
	
	/**
	 * float unitPrice (used as product initial bid price)
	 */
	private float unitPrice;
	
	/**
	 * Bid bid
	 */
	private Bid bid;

	/**
	 * @param id
	 * @param stockCode
	 * @param description
	 * @param unitPrice
	 */
	public Product(Integer id, String stockCode, String description, float unitPrice) {
		super();
		this.id = id;
		this.stockCode = stockCode;
		this.description = description;
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the stockCode
	 */
	public String getStockCode() {
		return stockCode;
	}

	/**
	 * @param stockCode the stockCode to set
	 */
	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unitPrice
	 */
	public float getUnitPrice() {
		return unitPrice;
	}

	/**
	 * @param unitPrice the unitPrice to set
	 */
	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	/**
	 * @return the bid
	 */
	public Bid getBid() {
		return bid;
	}

	/**
	 * @param bid the bid to set
	 */
	public void setBid(Bid bid) {
		this.bid = bid;
	}

	@Override
	public int compareTo(Object o) {
		return ((Product)o).id.compareTo(this.id);
	}
}