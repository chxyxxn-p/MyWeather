package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;

import calendar.CalendarPage;
import home.HomePage;
import location.LocationPage;
import login.LoginPage;
import login.LogoutPage;
import weather.GetApi;
import weather.WeatherPage;

public class MainDrive extends JFrame {

	public Font font = new Font("맑은 고딕", Font.PLAIN, 20);

//	page
	JPanel contentsPanel;
	public Page[] pages = new Page[6];

//	menu
	JPanel menuPanel;
	MenuIcon[] icons = new MenuIcon[5];
	
	String resDir = "C:/Users/tjoeun/Dropbox/Java/Park-choyeon_Project/MyWeather/res/";
	String pageBgImgName = "sky-bg.jpg";
	String iconBgImgName = "ball_yellow.png";
	
	GetApi fcstApi;
	GetApi ncstApi;
	GetApi api;
	
	String searchFcstDate;
	String searchNcstDate;
	
	String searchFcstTime;
	String searchNcstTime;
	
	String searchNx = "60";
	String searchNy = "127";
	
	 public boolean loginFlag;
	 public String loginUserName;	//LoginPage에서 login되면 설정
	 
	 Thread ncstApiThread;
	 Thread fcstApiThread;

	public MainDrive() {

//		현재시간 기준으로 검색할 base time 설정
		searchTimeSetting();
				
//		api 연결
		runApi();
		
//		메모리 적재
		contentsPanel = new JPanel();
	
		pages[0] = new HomePage(this, "홈페이지", 1520, 820, resDir + pageBgImgName, false);
		pages[1] = new WeatherPage(this, "날씨 페이지", 1520, 820, resDir + pageBgImgName, false);
		pages[2] = new CalendarPage(this, "달력 페이지", 1520, 820, resDir + pageBgImgName, false);
		pages[3] = new LocationPage(this, "위치 페이지", 1520, 820, resDir + pageBgImgName, false);
		pages[4] = new LoginPage(this, "로그인 페이지", 1520, 820, resDir + pageBgImgName, false);
		pages[5] = new LogoutPage(this, "로그아웃 페이지", 1520, 820, resDir + pageBgImgName, false);

		menuPanel = new JPanel();
		
		icons[0] = new MenuIcon(this, 50, 50, resDir + iconBgImgName);	//home
		icons[1] = new MenuIcon(this, 50, 50, resDir + iconBgImgName);	//weather
		icons[2] = new MenuIcon(this, 50, 50, resDir + iconBgImgName);	//calendar
		icons[3] = new MenuIcon(this, 50, 50, resDir + iconBgImgName);	//location
		icons[4] = new MenuIcon(this, 50, 50, resDir + iconBgImgName);	//login/out
		
		changePage(4);	//처음으로 보여줄 페이지	//#나중에 로그인페이지or홈페이지를  시작으로 변경

		
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
					if(index != 4) {
						changePage(index);
					} else {
						if(loginFlag) {
							changePage(5);
						} else {
							changePage(4);
						}
					}
				}
			});
		}
	}
	
public void runApi() {
		
		ncstApiThread = new Thread() {	
			public void run() {
				ncstApi = new GetApi("getUltraSrtNcst", "500", searchNcstDate, searchNcstTime, searchNx, searchNy);
				ncstApi.connectData();
				ncstApi.setWeatherMap();
//				ncstApi.printAllWeatherMapValue();				
			};
		};
		
		fcstApiThread = new Thread() {
			public void run() {			
				fcstApi = new GetApi("getVilageFcst", "500", searchFcstDate, searchFcstTime, searchNx, searchNy);
				fcstApi.connectData();
				fcstApi.setWeatherMap();
//				fcstApi.printAllWeatherMapValue();
			};
		};
		
		ncstApiThread.start();
		fcstApiThread.start();
	}
	
public void searchTimeSetting() {
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat timeformat = new SimpleDateFormat("HHmm");
	
	searchNcstDate = searchFcstDate = dateFormat.format(new Date());
	
	int time = Integer.parseInt(timeformat.format(new Date()));
		
		System.out.println("now : " + searchNcstDate + " " + time);
		
		int hour = time / 100;
		int minute = time % 100;
	
//		searchFcstTime
		int startTime = 210;
		
		for(int i = 0 ; i < 8 ; i++) {
			if(time < (startTime += i * 300)) {
				searchFcstTime = Integer.toString((startTime - 310));
				
				if(startTime < 0) {
					startTime = 2300;
					searchFcstDate = Integer.toString(Integer.parseInt(searchFcstDate) -1);
				}
				break;
			}
		}
		System.out.println("fcst base : " + searchFcstDate + " " + searchFcstTime);
	
//		searchNcstTime
		if(minute <= 40) {
			if(hour == 00) {
				searchNcstTime = "23" + "00";
				searchNcstDate = Integer.toString(Integer.parseInt(searchNcstDate) -1);
			} else {
				searchNcstTime = Integer.toString(hour-1) + "00";
			}
		} else if(minute <= 59) {
			searchNcstTime = Integer.toString(hour) + "00";
		}
		
		System.out.println("ncst base : " + searchNcstDate + " " + searchNcstTime);

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