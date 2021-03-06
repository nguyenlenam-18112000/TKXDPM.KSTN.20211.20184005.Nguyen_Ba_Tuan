package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place rush order usecase in our AIMS project
 * 
 * @author PIE
 */
public class PlaceRushOrderController extends BaseController {

	/**
	 * Just for logging purpose
	 */
	private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

	/**
	 * This method checks the avalibility of product when user click PlaceRushOrder
	 * button
	 * 
	 * @throws SQLException
	 */
	public void placeRushOrder() throws SQLException {
		Cart.getCart().checkAvailabilityOfProduct();
	}

	/**
	 * This method creates the new Order based on the Cart
	 * 
	 * @return Order
	 * @throws SQLException
	 */
	public Order createRushOrder() throws SQLException {
		Order order = new Order();
		for (Object object : Cart.getCart().getListMedia()) {
			CartMedia cartMedia = (CartMedia) object;
			OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice(), cartMedia.getRushOrder());
			order.getlstOrderMedia().add(orderMedia);
		}
		return order;
	}

	/**
	 * This method creates the new Invoice based on order
	 * 
	 * @param order
	 * @return Invoice
	 */
	public Invoice createInvoice(Order order) {
		return new Invoice(order);
	}

	/**
	 * This method takes responsibility for processing the shipping info from user
	 * 
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
		LOGGER.info("Process Delivery Info");
		LOGGER.info(info.toString());
		validateDeliveryInfo(info);
	}

	/**
	 * The method validates the info
	 * 
	 * @param info
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {

	}

	public boolean validatePhoneNumber(String phoneNumber) {

		if (phoneNumber.length() != 10)
			return false;
		if (!phoneNumber.startsWith("0"))
			return false;
		try {
			Integer.parseInt(phoneNumber);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public boolean validateName(String name) {
		if (name == null)
			return false;
		if (name.equals("null"))
			return false;
		Pattern p = Pattern.compile("[^A-Za-z]");
		Matcher m = p.matcher(name);
		boolean b = m.find();
		if (b == true)
			return false;
		return true;
	}

	public boolean validateAddress(String address) {
		if (address == null) return false;
    	if (address.equals("null")) return false;
    	if (address.equals("")) return false;
    	boolean valid = true;
    	int i;
    	for (i = 0; i < address.length(); i++) {
    		char ch = address.charAt(i);
    		valid = valid & (((ch >= 'A') & (ch <= 'Z')) | ((ch >= 'a') & (ch <= 'z')) | ((ch >= '0') & (ch <= '9'))| ch == ' ');
    		if (valid==false) {
    			break;
    		}
    	}
    	return valid;
	}

	/**
	 * This method calculates the shipping fees of order
	 * 
	 * @param order
	 * @return shippingFee
	 */
	public int calculateShippingFee(Order order) {
		Random rand = new Random();
		int fees = (int) (((rand.nextFloat() * 10) / 100) * order.getAmount());
		LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
		return fees;
	}
}
