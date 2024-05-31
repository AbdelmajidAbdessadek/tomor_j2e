package edu.inpt.tomor;

import java.util.ArrayList;

public class Cart {
	private String id;
	private String userId;
	private ArrayList<Product> products = new ArrayList<>();

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void addProduct(Product product) {
		products.add(product);
	}

	public void removeProduct(Product product) {
		products.remove(product);
	}

	public double getTotal() {
		double total = 0;
		for (Product product : products) {
			total += product.getPrice();
		}

		return total;
	}

}