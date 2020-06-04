package recommend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import database.ConnectionManager;
import home.HomePage;
import main.MainDrive;
import main.Page;

public class RecommendPage extends Page {
	
	JPanel rcmdPanel;
	JScrollPane rcmdScroll;
	
	ArrayList<Recommend> recommendList = new ArrayList<Recommend>();

	
	public RecommendPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		rcmdPanel = new JPanel();
		rcmdScroll = new JScrollPane(rcmdPanel);
		
		rcmdPanel.setBackground(new Color(0,0,0,0));
		rcmdScroll.setBackground(new Color(0,0,0,0));
//		rcmdPanel.setBackground(Color.yellow);
//		rcmdScroll.setBackground(Color.pink);
		
		this.setLayout(new BorderLayout());
		this.add(rcmdScroll);
		
		rcmdScroll.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				mainDrive.getMainPn().repaint();
				rcmdPanel.repaint();
			}
		});

		
	}
	
	@Override
	public void afterConnectApi() {
		super.afterConnectApi();
		
		connectDatabase(mainDrive.getWeatherCase()[mainDrive.getNowWeatherCaseNum()], mainDrive.getSearchFirstSep(), mainDrive.getSearchSecondSep(), mainDrive.getSearchThirdSep());
		
		
	}
	
	public void connectDatabase(String weatherName, String f, String s, String t) {

//		임시로 데이터베이스 연동 전 list에 item 추가
//		for(int i = 0 ; i < 4 ; i++) {
//		
//		Recommend r1 = new Recommend();
//		r1.setId(1);
//		r1.setName("빽다방 종로관철점");
//		r1.setAddress("관철동 42-4");
//		r1.setImage("https://search.pstatic.net/common/?src=http%3A%2F%2Fldb.phinf.naver.net%2F20191019_212%2F1571471269167BYgmN_JPEG%2F0ATu8sY4aIcu1FcfHT1RJJih.jpg");
//		
//		Recommend r2 = new Recommend();
//		r2.setId(1);
//		r2.setName("영풍문고 종로본점");
//		r2.setAddress("서린동 33");
//		r2.setPhone("1522-2776");
//		r2.setImage("https://search.pstatic.net/common/?src=http%3A%2F%2Fldb.phinf.naver.net%2F20160421_31%2F14612224873355dxkh_JPEG%2F176371563826414_1.jpeg");
//		
//		recommendList.add(r1);
//		recommendList.add(r2);
//		}
		
		
		ConnectionManager connectionManager = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		connectionManager = new ConnectionManager();
		con = connectionManager.getConnection();
		
		String sql = "select id, name, address, phone, image from store where id IN "
				+ "(select store_id from recommend"
				+ " where weather_id = (select id from weather where name = ?)"
				+ " and location_id = (select id from location where FIRST_SEP =? AND SECOND_SEP =? AND THIRD_SEP =?))";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, weatherName);
			pstmt.setString(2, f);
			pstmt.setString(3, s);
			pstmt.setString(4, t);
			
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
		} finally {
			connectionManager.closeDB(rs);
			connectionManager.closeDB(pstmt);
			connectionManager.closeDB(con);
		}
				
		for(int i = 0 ; i < recommendList.size() ; i++) {
			Recommend rcmd = recommendList.get(i);
			String name = rcmd.getName();
			String address = rcmd.getAddress();
			String phone = rcmd.getPhone();
			String image = rcmd.getImage();
			
			RcmdPanel r = new RcmdPanel(mainDrive, width/3, height-30, name, address, phone, image);
			rcmdPanel.add(r);
		}
		
		rcmdPanel.setLayout(new GridLayout(1, recommendList.size(), 10, 0));
		
		((HomePage)mainDrive.getPages()[0]).changeRecommendMsg();
		((HomePage)mainDrive.getPages()[0]).updateUI();
		this.updateUI();
		
	}

	public ArrayList<Recommend> getRecommendList() {
		return recommendList;
	}
	
}