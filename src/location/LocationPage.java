package location;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import home.HomePage;
import main.MainDrive;
import main.Page;

public class LocationPage extends Page {
	
	ArrayList<Location> locationList = new ArrayList<Location>();

	JPanel locationPanel;
	JComboBox<String> firstSepCb;
	JComboBox<String> secondSepCb;
	JComboBox<String> thirdSepCb;
	JButton locationBt;
	
	int locationPanelWidth;
	int locationPanelHeight;
	
	public LocationPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		locationPanelWidth = width - 20;
		locationPanelHeight = height / 5;
		
		locationPanel = new JPanel();
		firstSepCb = new JComboBox<String>();
		secondSepCb = new JComboBox<String>();
		thirdSepCb = new JComboBox<String>();
		locationBt = new JButton("search");
		
		firstSepCb.setFont(mainDrive.getFont(14));
		secondSepCb.setFont(mainDrive.getFont(14));
		thirdSepCb.setFont(mainDrive.getFont(14));
		locationBt.setFont(mainDrive.getFont(12));
		
//		locationPanel.setBackground(Color.yellow);
		locationPanel.setBackground(new Color(0,0,0,0));
		firstSepCb.setBackground(Color.white);
		secondSepCb.setBackground(Color.white);
		thirdSepCb.setBackground(Color.white);
		locationBt.setBackground(Color.white);
		
		locationPanel.setLayout(null);
		
		firstSepCb.setBounds(10, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		secondSepCb.setBounds(20 + (locationPanelWidth-50)/10*3, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		thirdSepCb.setBounds(30 + (locationPanelWidth-50)/10*6, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		locationBt.setBounds(40 + (locationPanelWidth-50)/10*9, 10, (locationPanelWidth-50)/10*1, locationPanelHeight-20);
		
		locationPanel.add(firstSepCb);
		locationPanel.add(secondSepCb);
		locationPanel.add(thirdSepCb);
		locationPanel.add(locationBt);
		
		this.setLayout(null);
		
		locationPanel.setBounds(10, 10, locationPanelWidth, locationPanelHeight);

		this.add(locationPanel);

		locationBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				������ �� �����۵�� mainDrive�� ������ ����				
				mainDrive.setSearchFirstSep((String)firstSepCb.getSelectedItem());
				mainDrive.setSearchSecondSep((String)secondSepCb.getSelectedItem());
				mainDrive.setSearchThirdSep((String)thirdSepCb.getSelectedItem());

//				�ٲ� mainDrive�� ������ homePage, locationPage�� combobox ���� ���߱�
				synchronizeSelectedLocation();
				
//				mainDrive�� ����(user�� �� ��ġ)�� �ش��ϴ� nx, ny�����ͼ� mainDrive�� searchNx, searchNy�� ����
				getSelectedLocationNxNy();
				
//				������� ���� ������ �ҷ�����
				mainDrive.runApi();
			}
		});
		
		firstSepCb.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				connectDatabaseSecondSep(firstSepCb, secondSepCb, thirdSepCb);
			}
		});
		
		secondSepCb.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				connectDatabaseThirdSep(firstSepCb, secondSepCb, thirdSepCb);
			}
		});
	}
	
	public void connectDatabaseFirstSep(JComboBox<String> f) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		String sql = "select distinct first_sep from location order by first_sep asc";
		
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String value = rs.getString("first_sep");
				f.addItem(value);
			}
			
			this.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(rs);
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}
	
	public void connectDatabaseSecondSep(JComboBox<String> f, JComboBox<String> s, JComboBox<String>t) {

		s.removeAllItems();
		t.removeAllItems();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		String sql = "select distinct second_sep from location where first_sep=? order by second_sep asc";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, (String)f.getSelectedItem());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String value = rs.getString("second_sep");
				s.addItem(value);
			}
			
			this.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(rs);
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}
	
	public void connectDatabaseThirdSep(JComboBox<String> f, JComboBox<String> s, JComboBox<String>t) {

		t.removeAllItems();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		String sql = "select distinct third_sep from location where first_sep=? and second_sep=? order by third_sep asc";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, (String)f.getSelectedItem());
			pstmt.setString(2, (String)s.getSelectedItem());
			rs = pstmt.executeQuery();
						
			while(rs.next()) {
				String value = rs.getString("third_sep");
				t.addItem(value);
			}
			
			this.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(rs);
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}

public void synchronizeSelectedLocation() {
		
	((LocationPage)mainDrive.getPages()[3]).getFirstSepCb().setSelectedItem(mainDrive.getSearchFirstSep());
	((LocationPage)mainDrive.getPages()[3]).getSecondSepCb().setSelectedItem(mainDrive.getSearchSecondSep());
	((LocationPage)mainDrive.getPages()[3]).getThirdSepCb().setSelectedItem(mainDrive.getSearchThirdSep());
	
	((HomePage)mainDrive.getPages()[0]).getFirstSepCb().setSelectedItem(mainDrive.getSearchFirstSep());
	((HomePage)mainDrive.getPages()[0]).getSecondSepCb().setSelectedItem(mainDrive.getSearchSecondSep());
	((HomePage)mainDrive.getPages()[0]).getThirdSepCb().setSelectedItem(mainDrive.getSearchThirdSep());
		
		mainDrive.getPages()[0].updateUI();
		mainDrive.getPages()[3].updateUI();
	}
	
	public void getSelectedLocationNxNy() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		String sql = "select nx, ny from location where first_sep=? and second_sep=? and third_sep=?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, mainDrive.getSearchFirstSep());
			pstmt.setString(2, mainDrive.getSearchSecondSep());
			pstmt.setString(3, mainDrive.getSearchThirdSep());
			rs = pstmt.executeQuery();
						
			while(rs.next()) {
				mainDrive.setSearchNx(rs.getString("nx"));
				mainDrive.setSearchNy(rs.getString("ny"));
			}
			
			this.updateUI();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(rs);
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}
	

	public void setLocationList(ArrayList<Location> locationList) {
		this.locationList = locationList;
	}

	public JComboBox<String> getFirstSepCb() {
		return firstSepCb;
	}

	public JComboBox<String> getSecondSepCb() {
		return secondSepCb;
	}

	public JComboBox<String> getThirdSepCb() {
		return thirdSepCb;
	}
}