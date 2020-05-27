package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import calendar.CalendarPage;
import home.HomePage;
import location.LocationPage;
import login.LoginPage;
import login.LogoutPage;
import weather.WeatherPage;

public class MainDrive extends JFrame {

//	page
	JPanel contentsPanel;
	Page[] pages = new Page[6];

//	menu
	JPanel menuPanel;
	MenuIcon[] icons = new MenuIcon[6];
	
	String resDir = "C:/Users/tjoeun/Dropbox/Java/Park-choyeon_Project/MyWeather/res/";
	String pageBgImgName = "sky-bg.jpg";
	String iconBgImgName = "ball_yellow.png";
	
	public MainDrive() {
//		메모리 적재
		contentsPanel = new JPanel();
	
		pages[0] = new LoginPage(this, "로그인 페이지 (현재 로그아웃 상태)", 1520, 820, resDir+pageBgImgName, false);
		pages[1] = new LogoutPage(this, "로그아웃 페이지 (현재 로그인 상태)", 1520, 820, resDir+pageBgImgName, false);
		pages[2] = new HomePage(this, "홈페이지", 1520, 820, resDir+pageBgImgName, false);
		pages[3] = new WeatherPage(this,  "날씨 페이지", 1520, 820, resDir+pageBgImgName, false);
		pages[4] = new CalendarPage(this, "달력 페이지", 1520, 820, resDir+pageBgImgName, false);
		pages[5] = new LocationPage(this, "위치 페이지", 1520, 820, resDir+pageBgImgName, false);

		menuPanel = new JPanel();
		
		icons[0] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		icons[1] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		icons[2] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		icons[3] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		icons[4] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		icons[5] = new MenuIcon(this, 50, 50, resDir+iconBgImgName);
		
		changePage(3);	//처음으로 보여줄 페이지	//#나중에 로그인페이지or홈페이지를  시작으로 변경

		
//		System.out.println(MainDrive.class.getResource("").getPath());
				
//		속성
		menuPanel.setPreferredSize(new Dimension(60, 820));
		menuPanel.setBackground(Color.WHITE);
		
		contentsPanel.setPreferredSize(new Dimension(1520, 820));
		contentsPanel.setBackground(Color.YELLOW);
		
//		조립		
		for(int i = 0 ; i < icons.length ; i++) {
			menuPanel.add(icons[i]);
		}
		
		contentsPanel.setLayout(new FlowLayout(0, 0, 0));
		
		for(int i = 0 ; i < pages.length ; i++) {
			contentsPanel.add(pages[i]);
		}
		
		add(menuPanel, BorderLayout.WEST);
		add(contentsPanel);

		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		
//		리스너
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		for(int i = 0 ; i < icons.length ; i++) {		
			int index = i;
			icons[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					changePage(index);
				}
			});
		}
		
	}
	
	public void changePage(int pageIndex) {
		
		this.setTitle(pages[pageIndex].title);

		for(int i = 0 ; i < pages.length ; i++) {
			pages[i].setVisible(i == pageIndex ? true : false);
		}	
	}

	public static void main(String[] args) {
		new MainDrive();
	}
}

