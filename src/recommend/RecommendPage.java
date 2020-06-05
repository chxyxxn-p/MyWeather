package recommend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
		Thread thread = new Thread() {
			public void run() {
//		기존 패널에 있던 패널 , 리스트 지우기
				Component[] childList = rcmdPanel.getComponents();
				
				int listSize = recommendList.size();
				for (int i = 0; i < listSize ; i++) {
					recommendList.remove(0);
				}
				
				for (int i = 0; i < childList.length; i++) {
					rcmdPanel.remove(childList[i]);
				}
				
//		데이터베이스 연결
				System.out.println(weatherName);
				Connection con = null;
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				
				con = mainDrive.getConnectionManager().getConnection();
				
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
					mainDrive.getConnectionManager().closeDB(rs);
					mainDrive.getConnectionManager().closeDB(pstmt);
					mainDrive.getConnectionManager().closeDB(con);
				}
				
				
//		새로 불러온 리스트만큼 rcmdPanel에 추가
				for (int i = 0; i < recommendList.size(); i++) {
					Recommend rcmd = recommendList.get(i);
					String name = rcmd.getName();
					String address = rcmd.getAddress();
					String phone = rcmd.getPhone();
					String image = rcmd.getImage();
					
					RcmdPanel r = new RcmdPanel(mainDrive, width / 3, height - 30, name, address, phone, image);
					rcmdPanel.add(r);
				}
				
				rcmdPanel.setLayout(new GridLayout(1, recommendList.size(), 10, 0));
				
				((HomePage)mainDrive.getPages()[0]).changeRecommendMsg();
				((HomePage)mainDrive.getPages()[0]).updateUI();
				
				RecommendPage.this.updateUI();
				
			};
		};
		thread.start();
	}

	public ArrayList<Recommend> getRecommendList() {
		return recommendList;
	}
	
}