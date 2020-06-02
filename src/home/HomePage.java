package home;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.MainDrive;
import main.Page;
import weather.WeatherPage;
import weather.WeatherValue;

public class HomePage extends Page {
	
	Map<Long, WeatherValue> ncstTodayWeatherMap;
	ArrayList<Long> ncstTodayKeyList;
	
	JPanel nowPanel;
	Image nowImg;
	JPanel nowImgPn;
	JTextArea nowInfoTa;
	
	JPanel locationPanel;
	JComboBox<String> sepFristCb;
	JComboBox<String> sepSecondCb;
	JComboBox<String> sepThirdCb;
	JButton locationBt;
	
	JPanel recommendPanel;
	
	JPanel diaryPanel;
	
	JPanel todoListPanel;
	
	
	int nowPanelWidth;
	int nowPanelHeight;
	int locationPanelWidth;
	int locationPanelHeight;
	int diaryPanelWidth;
	int diaryPanelHeight;
	int todoListPanelWidth;
	int todoListPanelHeight;
	int recommendPanelWidth;
	int recommendPanelHeight;
	
	
	public HomePage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		 nowPanelWidth = width/4;
		 nowPanelHeight = height/3*2-5;
		 locationPanelWidth = width - (nowPanelWidth + 10);
		 locationPanelHeight = nowPanelHeight/7;
		 todoListPanelWidth = diaryPanelWidth = (width-nowPanelWidth-10)/2;
		 todoListPanelHeight = diaryPanelHeight = nowPanelHeight - locationPanelHeight - 10;
		 recommendPanelWidth = width;
		 recommendPanelHeight = height/3-5;

//		[nowWeather]
		WeatherPage wp = ((WeatherPage)mainDrive.getPages()[1]);
		
		nowPanel = new JPanel();
		nowImgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(nowImg = wp.getNowImg(), 0, 0, width/4-40, width/4-40, mainDrive);
			}
		};
		nowInfoTa = new JTextArea();
		
		nowInfoTa.setEditable(false);
		
		nowInfoTa.setText(wp.getNowInfoTaText());

		nowInfoTa.setFont(mainDrive.getFont(18));
		
		nowPanel.setBackground(Color.cyan);
//		nowPanel.setBackground(new Color(0,0,0,0));
		nowImgPn.setBackground(new Color(0,0,0,0));
		nowInfoTa.setBackground(new Color(0,0,0,0));
		nowInfoTa.setOpaque(true);
		
		nowPanel.setLayout(null);
		
		nowImgPn.setBounds(20, 10, width/4-40, width/4-40);
		nowInfoTa.setBounds(10, 10+width/4-40+10, width/4-20, height - width/4 +40 - 30);
		
		nowPanel.add(nowImgPn);
		nowPanel.add(nowInfoTa);
			
		nowPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(1);
			}
		});
		
//		nowInfoLb 건드릴 때마다 새로 그리기
		nowInfoTa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.getMainPanel().repaint();
				nowPanel.repaint();
			}
		});
		
		
//		[location]
		locationPanel = new JPanel();
		sepFristCb = new JComboBox<String>();
		sepSecondCb = new JComboBox<String>();
		sepThirdCb = new JComboBox<String>();
		locationBt = new JButton("search");
		
		sepFristCb.setFont(mainDrive.getFont(12));
		sepSecondCb.setFont(mainDrive.getFont(12));
		sepThirdCb.setFont(mainDrive.getFont(12));
		locationBt.setFont(mainDrive.getFont(10));
		
		locationPanel.setBackground(Color.yellow);
//		locationPanel.setBackground(new Color(0,0,0,0));
		sepFristCb.setBackground(Color.white);
		sepSecondCb.setBackground(Color.white);
		sepThirdCb.setBackground(Color.white);
		locationBt.setBackground(Color.white);
		
		locationPanel.setLayout(null);
		
		sepFristCb.setBounds(10, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		sepSecondCb.setBounds(20 + (locationPanelWidth-50)/10*3, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		sepThirdCb.setBounds(30 + (locationPanelWidth-50)/10*6, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		locationBt.setBounds(40 + (locationPanelWidth-50)/10*9, 10, (locationPanelWidth-50)/10*1, locationPanelHeight-20);
		
		locationPanel.add(sepFristCb);
		locationPanel.add(sepSecondCb);
		locationPanel.add(sepThirdCb);
		locationPanel.add(locationBt);

		locationPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(3);
			}
		});
		
		locationBt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
//				user가 고른 위치에 해당하는 nx, ny가져와서 mainDrive의 searchNx, searchNy로 대입하고, 스레드로 새로 데이터 불러오기
				System.out.println("click");
			}
		});
		
		
//		[recommend]
		recommendPanel = new JPanel();
		
		recommendPanel.setBackground(Color.magenta);
//		recommendPanel.setBackground(new Color(0,0,0,0));

		recommendPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(4);
			}
		});
		
		
//		[diary]
		diaryPanel = new JPanel();
		
		diaryPanel.setBackground(Color.green);		
//		diaryPanel.setBackground(new Color(0,0,0,0));
		
		diaryPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(2);
			}
		});

		
//		[toDoList]
		todoListPanel = new JPanel();
		
		todoListPanel.setBackground(Color.blue);		
//		todoListPanel.setBackground(new Color(0,0,0,0));

		todoListPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(2);
			}
		});
		
		
//		[HomePage]
		this.setLayout(null);
		
		nowPanel.setBounds(0, 0, nowPanelWidth, nowPanelHeight);
		locationPanel.setBounds(nowPanelWidth + 10, 0, locationPanelWidth, locationPanelHeight);
		diaryPanel.setBounds(nowPanelWidth + 10, locationPanelHeight + 10, diaryPanelWidth, diaryPanelHeight);
		todoListPanel.setBounds((nowPanelWidth + 10) + (diaryPanelWidth + 10), locationPanelHeight + 10, todoListPanelWidth, todoListPanelHeight);
		recommendPanel.setBounds(0, nowPanelHeight + 10, recommendPanelWidth, recommendPanelHeight);
		
		this.add(nowPanel);
		this.add(locationPanel);
		this.add(diaryPanel);
		this.add(todoListPanel);
		this.add(recommendPanel);
	}
}