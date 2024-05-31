package edu.inpt.tomor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAOImpl implements UserDAO {
	static Connection connection = null;
	static ResultSet rs = null;

	@Override
	public User getUserById(String Id) {
		Statement statement = null;
		User user = new User();
		String searchQuery = "SELECT * FROM users WHERE id='" + Id + "'";

		try {
			connection = DBSingletonConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(searchQuery);
			boolean more = rs.next();

			if (!more) {
				user.setValid(false);
			} else {
				user.setValid(true);
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String username = rs.getString("username");
				String email = rs.getString("email");
				String birthdate = rs.getString("birthdate");
				String address = rs.getString("address");
				String city = rs.getString("city");
				String country = rs.getString("country");
				String phone_number = rs.getString("phone_number");

				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setId(Id);
				user.setAddress(address);
				user.setCity(city);
				user.setCountry(country);
				user.setEmail(email);
				user.setPhone_number(phone_number);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					rs = null;
				}
			}
		}
		return user;
	}

	public static User login(User bean) {
		Statement statement = null;
		String email = bean.getEmail();
		String password = bean.getPassword();

		String password_hash = hashPasswordMD5(password);

		String searchQuery = "SELECT * FROM users WHERE email='";
		searchQuery += email;
		searchQuery += "' AND password_hash='";
		searchQuery += password_hash;
		searchQuery += "'";

		System.out.println(searchQuery);

		try {
			connection = DBSingletonConnection.getConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(searchQuery);
			boolean more = rs.next();

			if (!more) {
				bean.setValid(false);
			} else {
				bean.setValid(true);
				String firstName = rs.getString("firstname");
				String lastName = rs.getString("lastname");
				String id = rs.getString("id");

				bean.setFirstName(firstName);
				bean.setLastName(lastName);
				bean.setId(id);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception ex) {
					rs = null;
				}
			}
		}

		return bean;
	}

	private static String hashPasswordMD5(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(password.getBytes());

			StringBuilder result = new StringBuilder();
			for (byte b : bytes) {
				result.append(String.format("%02x", b));
			}

			return result.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

}
