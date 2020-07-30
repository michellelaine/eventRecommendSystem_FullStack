package db.mysql;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import entity.Item;
import entity.Item.ItemBuilder;
import external.TicketMasterAPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MySQLConnection implements DBConnection{
	private Connection conn;
	
	// create a singleton pattern, only create one DB connection
	public MySQLConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		if(conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setFavoriteItems(String userId, List<String> itemIds) {
		if(conn == null) {
			return;
		}
		
		try {
				String sql = "INSERT IGNORE INTO history (user_id, item_id) VALUES(?, ?)";
				PreparedStatement stmt = conn.prepareStatement(sql);
				for(String itemId: itemIds) {
					stmt.setString(1, userId);
					stmt.setString(2, itemId);
					stmt.execute();
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unsetFavoriteItems(String userId, List<String> itemIds) {
		if(conn == null) {
			return;
		}
		
		try {
			String sql = "DELETE FROM history WHERE user_id = ? AND item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(String itemId : itemIds) {
				stmt.setString(1, userId);
				stmt.setString(2, itemId);
				stmt.execute();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> getFavoriteItemIds(String userId) {
		if(conn == null) {
			return new HashSet<>();
		}
		
		Set<String> favoriteItemIds = new HashSet<>();
		
		try {
			String sql = "SELECT item_id from history where user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				String itemId = rs.getString("item_id");
				favoriteItemIds.add(itemId);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return favoriteItemIds;
	}

	@Override
	public Set<Item> getFavoriteItems(String userId) {
		if(conn == null) {
			return new HashSet<>();
		}
		
		Set<Item> favoriteItems = new HashSet<>();
		Set<String> itemIds = getFavoriteItemIds(userId);
		
		try {
			String sql = "SELECT * FROM items WHERE item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for(String itemId : itemIds ) {
				stmt.setString(1, itemId);
				
				ResultSet rs = stmt.executeQuery();
				
				ItemBuilder builder = new ItemBuilder();
				
				while(rs.next()) {
					builder.setItemId(rs.getString("item_id"));
					builder.setName(rs.getString("name"));
					builder.setAddress(rs.getString("address"));
					builder.setImageUrl(rs.getString("image_url"));
					builder.setUrl(rs.getString("url"));
					builder.setCategories(getCategories(itemId));
					builder.setDistance(rs.getDouble("distance"));
					builder.setRating(rs.getDouble("rating"));
					
					favoriteItems.add(builder.build());
					
				}
				
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return favoriteItems;
	}

	@Override
	public Set<String> getCategories(String itemId) {
		if(conn == null) {
			return null;
		}
		
		Set<String> categories = new HashSet<>();
		
		try {
			String sql = "SELECT * FROM categories WHERE item_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, itemId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				categories.add(rs.getString("category"));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return categories;
	}

	@Override
	public List<Item> searchItems(double lat, double lon, String term) {
		TicketMasterAPI tmAPI = new TicketMasterAPI();
		List<Item> items = tmAPI.search(lat,  lon, term);
		for(Item item : items) {
			saveItem(item);
		}
		return items;
	}

	//store event into DB
	@Override
	public void saveItem(Item i) {
		//why to check connection==null, 
		//! conn.saveItem==null.saveItem -> NullException -> app down
		if (conn == null) {
			return;
		}
		try {
			// SQL injection
			// Example:
			// SELECT * FROM users WHERE username = '<username>' AND password = '<password>';
			//
			// sql = "SELECT * FROM users WHERE username = '" + username + "'
			//       AND password = '" + password + "'"
			//
			// username: aoweifjoawefijwaoeifj
			// password: 123456' OR '1' = '1
			//
			// SELECT * FROM users WHERE username = 'aoweifjoawefijwaoeifj' AND password = '123456' OR '1' = '1'
			String sql = "INSERT IGNORE INTO items VALUES(?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,  i.getItemId());
			stmt.setString(2,  i.getName());
			stmt.setDouble(3,  i.getRating());
			stmt.setString(4,  i.getAddress());
			stmt.setString(5,  i.getImageUrl());
			stmt.setString(6,  i.getUrl());
			stmt.setDouble(7,  i.getDistance());
			stmt.execute();
			
			sql = "INSERT IGNORE INTO categories VALUES(?, ?)";
			stmt = conn.prepareStatement(sql);
			for(String category : i.getCategories()) {
				stmt.setString(1, i.getItemId());
				stmt.setString(2, category);
				stmt.execute();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
