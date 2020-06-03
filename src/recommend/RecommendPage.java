package recommend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.ConnectionManager;
import main.MainDrive;
import main.Page;

public class RecommendPage extends Page {
	
	ArrayList<Recommend> recommendList = new ArrayList<Recommend>();
	
	ConnectionManager connectionManager;
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public RecommendPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		

	}
	
	public void connectDatabase(String weatherName, String nx, String ny) {
		connectionManager = new ConnectionManager();
		con = connectionManager.getConnection();
		
		String sql = "select id, name, address, phone, image from store where store_id = "
				+ "(select store_id from recommend"
				+ " where weather_id = (select id from weather where name = ?"
				+ " and location_id = (select id from location where nx=? and ny=?))";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, weatherName);
			pstmt.setString(2, nx);
			pstmt.setString(3, ny);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Recommend r = new Recommend();
				
				r.setId(rs.getInt("id"));
				r.setName(rs.getString("name"));
				r.setAddress(rs.getString("address"));
				r.setPhone(rs.getString("phone"));
				r.setImage(rs.getString("image"));
				
				recommendList.add(r);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}