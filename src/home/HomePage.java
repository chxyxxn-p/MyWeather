package home;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import location.LocationPage;
import main.MainDrive;
import main.Page;
import recommend.Recommend;
import recommend.RecommendPage;
import weather.Weather;
import weather.WeatherPage;

public class HomePage extends Page {
	
	Map<Long, Weather> ncstTodayWeatherMap;
	ArrayList<Long> ncstTodayKeyList;
	
	
	WeatherPage wp;
	LocationPage lp;
	
	JPanel nowPanel;
	Image nowImg = new ImageIcon(mainDrive.getTransparentImgPath()).getImage();
	JPanel nowImgPn;
	JTextArea nowInfoTa;
	
	JPanel locationPanel;
	JComboBox<String> firstSepCb;
	JComboBox<String> secondSepCb;
	JComboBox<String> thirdSepCb;
	JButton locationBt;
	
	JPanel recommendPanel;
	JLabel recommendLabel;
	String[] recommendMsg = {" 어떠세요?", " 추천해요!"};
	
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
		 wp = ((WeatherPage)mainDrive.getPages()[1]);
		 
		 nowPanel = new JPanel();
		 nowImgPn = new JPanel() {
			 @Override
			 public void paint(Graphics g) {
				 g.drawImage(nowImg, 0, 0, width/4-40, width/4-40, mainDrive);
			 }
		 };
		 nowInfoTa = new JTextArea();
		 
		 nowInfoTa.setEditable(false);
		 nowInfoTa.setFont(mainDrive.getFont(18));
		 
//			nowPanel.setBackground(Color.cyan);
			nowPanel.setBackground(new Color(255,255,255,120));
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
			
//			nowInfoLb 건드릴 때마다 새로 그리기
			nowInfoTa.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mainDrive.getMainPn().repaint();
					nowPanel.repaint();
				}
			});
			
			
//			[location]
			lp = ((LocationPage)mainDrive.getPages()[3]);
			
			locationPanel = new JPanel();
			firstSepCb = new JComboBox<String>();
			secondSepCb = new JComboBox<String>();
			thirdSepCb = new JComboBox<String>();
			locationBt = new JButton("search");
			
			firstSepCb.setFont(mainDrive.getFont(12));
			secondSepCb.setFont(mainDrive.getFont(12));
			thirdSepCb.setFont(mainDrive.getFont(12));
			locationBt.setFont(mainDrive.getFont(10));
			
//			locationPanel.setBackground(Color.yellow);
			locationPanel.setBackground(new Color(255,255,255,120));
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

			locationPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mainDrive.changePage(3);
				}
			});
			
			locationBt.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					((LocationPage)mainDrive.getPages()[3]).synchronizeSelectedItems(firstSepCb, secondSepCb, thirdSepCb);
//					user가 고른 위치에 해당하는 nx, ny가져와서 mainDrive의 searchNx, searchNy로 대입하고
					((LocationPage)mainDrive.getPages()[3]).getSelectedLocationInfo();
//					스레드로 새로 데이터 불러오기
					mainDrive.runApi();
				}
			});
			
			
//			[recommend]
			recommendPanel = new JPanel();
			recommendLabel = new JLabel("", JLabel.CENTER);
			
//			recommendPanel.setBackground(Color.magenta);
			recommendPanel.setBackground(new Color(255,255,255,120));
			
			recommendLabel.setFont(mainDrive.getFont(50));
			
			recommendLabel.setPreferredSize(new Dimension(recommendPanelWidth, recommendPanelHeight));
			
			recommendPanel.add(recommendLabel);

			recommendPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mainDrive.changePage(4);
				}
			});
			
			
//			[diary]
			diaryPanel = new JPanel();
			
//			diaryPanel.setBackground(Color.green);		
			diaryPanel.setBackground(new Color(255,255,255,120));
			
			diaryPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mainDrive.changePage(2);
				}
			});

			
//			[toDoList]
			todoListPanel = new JPanel();
			
//			todoListPanel.setBackground(Color.blue);		
			todoListPanel.setBackground(new Color(255,255,255,120));

			todoListPanel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mainDrive.changePage(2);
				}
			});
			
			
//			[HomePage]
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
	
	@Override
	public void afterConnectApi() {
		super.afterConnectApi();
		
		nowImg = wp.getNowImg();
		nowInfoTa.setText(wp.getNowInfoTaText());
	}
	
	public void changeRecommendMsg(){
		ArrayList<Recommend> list = ((RecommendPage)mainDrive.getPages()[4]).getRecommendList();
		
		if(list.size() > 0) {
			int listRandom = (int)(Math.random()*list.size());
			int msgRandom = (int)(Math.random()*recommendMsg.length);
			recommendLabel.setText(mainDrive.getLoginUserName()+"님 "
					+list.get(listRandom).getName()
					+recommendMsg[msgRandom]);
			
			System.out.println("listRandom : " + listRandom + " msgRandom : " + msgRandom);
		} else {
			recommendLabel.setText(mainDrive.getLoginUserName()+"님 안녕하세요?");
		}
		this.updateUI();
	}
	
	public void setComboBox(String f, String s, String t) {
		firstSepCb.addItem(f);
		secondSepCb.addItem(s);
		thirdSepCb.addItem(t);

		this.updateUI();
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

	public JLabel getRecommendLabel() {
		return recommendLabel;
	}

	
	
	
	
	
}