/**
 * 
 */
package DAO;

import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import Model.Bid;
import Model.Product;
import Model.User;

/**
 * @author thusitha
 *
 */
public class SparkDAO {

	/**
	 * SparkConf SPARKCONF
	 */
	private static SparkConf SPARKCONF;

	/**
	 * SparkContext SPARKCONTEXT
	 */
	private static SparkContext SPARKCONTEXT;

	/**
	 * JavaSparkContext JAVASPARKCONTEXT
	 */
	private static JavaSparkContext JAVASPARKCONTEXT;

	/**
	 * List<Row> list
	 */
	private static List<Row> listProducts;

	/**
	 * Constructor
	 */
	public SparkDAO() {
		super();
	}

	/**
	 * @return the list
	 */
	public static List<Row> getList() {
		return listProducts;
	}

	/**
	 * create a Spark connection and read the CSV DataSet
	 * 
	 * @return JavaSparkContext
	 */
	public void initSparkConnectionAndReadDataSet() {
		SPARKCONF = new SparkConf().setAppName("SparkAndBid").setMaster("local[*]");
		SPARKCONTEXT = new SparkContext(SPARKCONF);
		JAVASPARKCONTEXT = JavaSparkContext.fromSparkContext(SPARKCONTEXT);
		getListOfRows();
	}

	/**
	 * read the CSV DataSet from HDFS and create a List<Row>
	 */
	private void getListOfRows() {
		SparkSession spark = SparkSession.builder().appName("SparkAndBid").getOrCreate();
		Dataset<Row> dfProducts = spark.read().option("mode", "DROPMALFORMED").option("header", true)
				.option("inferSchema", true).option("delimiter", ";")
				.csv("hdfs://127.0.0.1:54310/temp/Online-Retail.csv");

		dfProducts.createOrReplaceTempView("product");

		Dataset<Row> sqlResult = spark.sql("SELECT StockCode, Description, UnitPrice from product");

		// Convert the DataSet to a List
		listProducts = sqlResult.collectAsList();
		spark.stop();
	}

	/**
	 * get the number of rows of the List<Row>
	 * 
	 * @return int
	 */
	public int getNumberOfRows() {
		return listProducts.size();
	}

	/**
	 * create a list of Products from the List<Row>
	 * 
	 * @return TreeSet<Product>
	 */
	public TreeSet<Product> createProductFromListOfRows(int currentPage) {
		int start = currentPage * 12 - 12;
		int len = start + 12;

		if (listProducts.size() < len) {
			len = listProducts.size();
		}

		TreeSet<Product> products = new TreeSet<>();

		// Iterate over the list to create Product Beans
		for (int i = start; i < len; i++) {
			products.add(new Product(i, listProducts.get(i).getString(0), listProducts.get(i).getString(1),
					Float.parseFloat(listProducts.get(i).getString(2).replace(",", "."))));
		}
		return products;
	}

	/**
	 * add a Bid to a Product and save to HDFS
	 * 
	 * @param stockCode
	 * @param bid
	 */
	public void addBidToProduct(int id, Bid bid) {
		SPARKCONF = new SparkConf().setAppName("SparkAndBid").setMaster("local[*]");
		SPARKCONTEXT = new SparkContext(SPARKCONF);
		JAVASPARKCONTEXT = JavaSparkContext.fromSparkContext(SPARKCONTEXT);
		SparkSession spark = SparkSession.builder().appName("SparkAndBid").getOrCreate();

		TreeSet<Product> products = new TreeSet<>();

		// Iterate over the list to create Product Beans
		for (int i = 0; i < listProducts.size(); i++) {
			products.add(new Product(i, listProducts.get(i).getString(0), listProducts.get(i).getString(1),
					Float.parseFloat(listProducts.get(i).getString(2).replace(",", "."))));
		}

		List<String> data = new ArrayList<String>();

		// Iterate over Collection<Product> to add the Bid into List<String> data
		for (Product product : products) {
			if (product.getId().equals(id)) {
				product.setBid(bid);
				for (User user : product.getBid().getUsers()) {
					data.add("" + product.getId() + ", " + user.getUsername() + ", " + product.getBid().getDate() + ", "
							+ product.getBid().getPrice());
				}
			}
		}

		Dataset<Row> df = spark.createDataset(data, Encoders.STRING()).toDF();

		try {
			Configuration conf = new Configuration();
			conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
			FileSystem hdfs = FileSystem.get(URI.create("hdfs://127.0.0.1:54310"), conf);
			if (hdfs.exists(new Path("/temp/bid.csv"))) {
				// Get the existing data of bids
				List<String> existing = JAVASPARKCONTEXT.textFile("hdfs://127.0.0.1:54310/temp/bid.csv").collect();
				List<String> temp = new ArrayList<>();

				// Remove quotes
				for (int i = 0; i < existing.size(); i++) {
					temp.add(existing.get(i).replace("\"", ""));
				}

				Dataset<Row> dfBids = spark.createDataset(temp, Encoders.STRING()).toDF();

				// join the existing and new datasets
				Dataset<Row> finalDataset = df.union(dfBids);

				// remove the existing HDFS file
				hdfs.delete(new Path("/temp/bid.csv"), true);

				// save into HDFS
				finalDataset.repartition(1).write().format("csv").save("hdfs://127.0.0.1:54310/temp/bid.csv");
			} else {
				// save into HDFS
				df.repartition(1).write().format("csv").save("hdfs://127.0.0.1:54310/temp/bid.csv");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		spark.stop();
	}

	/**
	 * get the highest bid by product
	 * 
	 * @param products
	 * @return Map<Integer, Bid>
	 */
	public TreeSet<Product> getBidsByProduct(TreeSet<Product> currentProducts) {
		SPARKCONF = new SparkConf().setAppName("SparkAndBid").setMaster("local[*]");
		SPARKCONTEXT = new SparkContext(SPARKCONF);
		JAVASPARKCONTEXT = JavaSparkContext.fromSparkContext(SPARKCONTEXT);
		SparkSession spark = SparkSession.builder().appName("SparkAndBid").getOrCreate();

		// Get the existing data of bids
		List<String> existing = JAVASPARKCONTEXT.textFile("hdfs://127.0.0.1:54310/temp/bid.csv").collect();
		List<String> temp = new ArrayList<>();

		// Remove quotes
		for (int i = 0; i < existing.size(); i++) {
			temp.add(existing.get(i).replace("\"", ""));
		}

		Dataset<Row> dfBids = spark.createDataset(temp, Encoders.STRING()).toDF();
		Dataset<Row> df = dfBids.selectExpr("split(value, ',')[0] as productId", "split(value, ',')[1] as username",
				"split(value, ',')[2] as date", "split(value, ',')[3] as price");

		List<Row> listBids = df.collectAsList();

		Collection<Bid> tempBids = new HashSet<>();

		String pattern = "yyyy-MM-dd hh:mm:ss.SSS";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);

		for (int i = 0; i < listBids.size(); i++) {

			Bid bid = new Bid();
			try {
				bid.setIdProduct(Integer.parseInt(listBids.get(i).getString(0)));
				bid.setUserName(listBids.get(i).getString(1).trim());
				String stringDate = listBids.get(i).getString(2).trim();
				Date parsedDate = sdf.parse(stringDate);
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				bid.setDate(timestamp);
				bid.setPrice(Float.parseFloat(listBids.get(i).getString(3).replace(",", ".")));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			tempBids.add(bid);
		}

		Map<Integer, Bid> bs = new HashMap<Integer, Bid>();

		for (Product product : currentProducts) {
			for (Bid bid : tempBids) {
				if (product.getId() == bid.getIdProduct()) {
					if (bs.containsKey(product.getId())) {
						Bid inList = bs.get(product.getId());
						if (bid.getPrice() > inList.getPrice()) {
							bs.replace(product.getId(), bid);
						}
					} else {
						bs.put(product.getId(), bid);
					}
				}
			}
		}
		spark.stop();

		TreeSet<Product> withBids = new TreeSet<>();

		for (Product product : currentProducts) {
			for (Integer key : bs.keySet()) {
				if (product.getId() == bs.get(key).getIdProduct()) {
					product.setBid(bs.get(key));
				}
			}
			withBids.add(product);
		}
		return withBids;
	}

	/**
	 * Check if the User exists
	 * @param user
	 * @return Collection<User>
	 */
	public Collection<User> logInUser(User user) {
		SPARKCONF = new SparkConf().setAppName("SparkAndBid").setMaster("local[*]");
		SPARKCONTEXT = new SparkContext(SPARKCONF);
		JAVASPARKCONTEXT = JavaSparkContext.fromSparkContext(SPARKCONTEXT);
		SparkSession spark = SparkSession.builder().appName("Java Spark Session").getOrCreate();

		// Get the existing data of bids
		List<String> existing = JAVASPARKCONTEXT.textFile("hdfs://127.0.0.1:54310/temp/users.csv").collect();
		List<String> temp = new ArrayList<>();

		// Remove quotes
		for (int i = 0; i < existing.size(); i++) {
			temp.add(existing.get(i).replace("\"", ""));
		}

		Dataset<Row> dfUsers = spark.createDataset(temp, Encoders.STRING()).toDF();
		Dataset<Row> df = dfUsers.selectExpr("split(value, ',')[0] as username", "split(value, ',')[1] as password");

		List<Row> listUsers = df.collectAsList();

		Collection<User> tempUsers = new HashSet<>();

		for (int i = 0; i < listUsers.size(); i++) {
			User u = new User();
			u.setUsername(listUsers.get(i).getString(0));
			u.setPassword(listUsers.get(i).getString(1));
			tempUsers.add(u);
		}
		
		Collection<User> exist = new HashSet<>();
		for (User u : tempUsers) {
			if (u.getUsername().equals(user.getUsername()) && u.getPassword().equals(user.getPassword())) {
				exist.add(u);
			}
		}
		spark.close();
		return exist;
	}

	/**
	 * Save the User to HDFS file
	 * @param user
	 */
	public void registerUser(User user) {
		SPARKCONF = new SparkConf().setAppName("SparkAndBid").setMaster("local[*]");
		SPARKCONTEXT = new SparkContext(SPARKCONF);
		JAVASPARKCONTEXT = JavaSparkContext.fromSparkContext(SPARKCONTEXT);
		SparkSession spark = SparkSession.builder().appName("SparkAndBid").getOrCreate();

		List<String> data = new ArrayList<String>();
		data.add("" + user.getUsername() + "," + user.getPassword());

		Dataset<Row> df = spark.createDataset(data, Encoders.STRING()).toDF();

		try {
			Configuration conf = new Configuration();
			conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
			conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
			FileSystem hdfs = FileSystem.get(URI.create("hdfs://127.0.0.1:54310"), conf);
			if (hdfs.exists(new Path("/temp/users.csv"))) {
				// Get the existing data of bids
				List<String> existing = JAVASPARKCONTEXT.textFile("hdfs://127.0.0.1:54310/temp/users.csv").collect();
				List<String> temp = new ArrayList<>();

				// Remove quotes
				for (int i = 0; i < existing.size(); i++) {
					temp.add(existing.get(i).replace("\"", ""));
				}

				Dataset<Row> dfUsers = spark.createDataset(temp, Encoders.STRING()).toDF();

				// join the existing and new datasets
				Dataset<Row> finalDataset = df.union(dfUsers);

				// remove the existing HDFS file
				hdfs.delete(new Path("/temp/users.csv"), true);

				// save into HDFS
				finalDataset.repartition(1).write().format("csv").save("hdfs://127.0.0.1:54310/temp/users.csv");
			} else {
				// save into HDFS
				df.repartition(1).write().format("csv").save("hdfs://127.0.0.1:54310/temp/users.csv");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		spark.stop();
	}
}