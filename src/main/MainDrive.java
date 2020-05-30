package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
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
	
	GraphicsEnvironment ge;
	
//	page
	JPanel pagePanel;
	public Page[] pages = new Page[6];

	JPanel mainPanel;
	
//	menu
	JPanel menuPanel;
	MenuIcon[] icons = new MenuIcon[5];
  
//	String resDir = "C:/Users/tjoeun/Dropbox/Java/Park-choyeon_Project/MyWeather/res/";
//	String mainBgImgName = "sky.jpg";
//	String iconBgImgName = "ball_yellow.png";
	String mainBgImg = "./res/sky.jpg";
	String iconBgImg = "./res/ball_yellow.png";

	public GetApi fcstApi;
	public GetApi ncstTodayApi;
	public GetApi ncstYesterdayApi;
//	GetApi api;

	String searchFcstDate;
	String searchNcstDate;

	String searchFcstTime;
	String searchNcstTime;

	String searchNx = "60";
	String searchNy = "127";
	
	int pageWidth = 1000;
	int pageHeight = 500;

	public boolean loginFlag;
	public String loginUserName; // LoginPage에서 login되면 설정

	Thread ncstYesterdayApiThread;
	Thread ncstTodayApiThread;
	Thread fcstApiThread;

	public MainDrive() {
		
//		폰트 설정
		setFont();

//		현재시간 기준으로 검색할 base time 설정
		searchTimeSetting();

//		api 연결
		runApi();
		
//		defaultSetting();
	}
	
	public void defaultSetting() {
		
		System.out.println("main default setting...");
		
//		메모리 적재
		Image img = new ImageIcon(mainBgImg).getImage();

		mainPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		pagePanel = new JPanel();

		pages[0] = new HomePage(this, "HOME", pageWidth, pageHeight, false);
		pages[1] = new WeatherPage(this, "WEATHER", pageWidth, pageHeight, false);
		pages[2] = new CalendarPage(this, "CALENDAR", pageWidth, pageHeight, false);
		pages[3] = new LocationPage(this, "LOCATION", pageWidth, pageHeight, false);
		pages[4] = new LoginPage(this, "LOGIN", pageWidth, pageHeight, false);
		pages[5] = new LogoutPage(this, "LOGOUT", pageWidth, pageHeight, false);

		menuPanel = new JPanel();

		icons[0] = new MenuIcon(this, 50, 50, iconBgImg); // home
		icons[1] = new MenuIcon(this, 50, 50, iconBgImg); // weather
		icons[2] = new MenuIcon(this, 50, 50, iconBgImg); // calendar
		icons[3] = new MenuIcon(this, 50, 50, iconBgImg); // location
		icons[4] = new MenuIcon(this, 50, 50, iconBgImg); // login/out

		changePage(1); // 처음으로 보여줄 페이지 //#나중에 로그인페이지or홈페이지를 시작으로 변경

//		속성
		mainPanel.setPreferredSize(new Dimension(pageWidth + 60, pageHeight));

		menuPanel.setPreferredSize(new Dimension(60, pageHeight));
		menuPanel.setBackground(new Color(0, 0, 0, 0));

//		System.out.println(menuPanel.getWidth()); -> 60출력 예상.. 0 출력됨
		pagePanel.setPreferredSize(new Dimension(pageWidth, pageHeight));
		pagePanel.setBackground(new Color(0, 0, 0, 0));

//		조립
		for (int i = 0; i < icons.length; i++) {
			menuPanel.add(icons[i]);
		}

		pagePanel.setLayout(new FlowLayout(0, 0, 0));
		for (int i = 0; i < pages.length; i++) {
			pagePanel.add(pages[i]);
		}

		mainPanel.setLayout(new FlowLayout(0, 0, 0));
		mainPanel.add(menuPanel, BorderLayout.WEST);
		mainPanel.add(pagePanel);

		this.add(mainPanel);
		this.setContentPane(mainPanel);

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

		for (int i = 0; i < icons.length; i++) {
			int index = i;
			icons[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (index != 4) {
						changePage(index);
					} else {
						if (loginFlag) {
							changePage(5);
						} else {
							changePage(4);
						}
					}
				}
			});
		}
		
		System.out.println("main default set done...");

	}
	
	public void setFont() {
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(MainDrive.class.getResource(".").getPath() + "/NanumSquareR.ttf")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Font getFont(int size) {
		int index = 0;
		
		for(int i = 0 ; i < ge.getAvailableFontFamilyNames().length ; i++) {
			if(ge.getAvailableFontFamilyNames()[i].equals("나눔스퀘어 Regular")) {
				index = i;
				break;
			}
//			System.out.println(ge.getAvailableFontFamilyNames()[i]);
		}
		
		return new Font(ge.getAvailableFontFamilyNames()[index], Font.PLAIN, size);
//		mainFont = new Font("HY견고딕", Font.PLAIN, 20);
	}

	public void searchTimeSetting() {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeformat = new SimpleDateFormat("HHmm");

		searchNcstDate = searchFcstDate = dateFormat.format(new Date());

		int time = Integer.parseInt(timeformat.format(new Date()));

		int hour = time / 100;
		int minute = time % 100;

//		searchFcstTime
		int startTime = 210;

		for (int i = 0; i < 8; i++) {
			startTime += 300;
			
			if (time < startTime) {
				searchFcstTime = (startTime - 310 < 1000 ? "0" : "") + Integer.toString((startTime - 310));
				
				if (i == 0) {
					searchFcstTime = "2300";
					searchFcstDate = Integer.toString(Integer.parseInt(searchFcstDate) - 1);
				}
				break;
			}
		}
		
//		searchNcstTime
		if (minute <= 40) {
			if (hour == 00) {
				searchNcstTime = "23" + "00";
				searchNcstDate = Integer.toString(Integer.parseInt(searchNcstDate) - 1);
			} else {
				searchNcstTime = (hour - 1 < 10 ? "0" : "") + Integer.toString(hour - 1) + "00";
			}
		} else if (minute <= 59) {
			searchNcstTime = (hour < 10 ? "0" : "") + Integer.toString(hour) + "00";
		}
	}

	public void runApi() {
		
		ncstYesterdayApiThread = new Thread() {
			public void run() {
				String yesterdayNcstDate = Integer.toString(Integer.parseInt(searchNcstDate)-1);
				String yesterdayNcstTime = Integer.toString(Integer.parseInt(searchNcstTime)+200);
				
				if(yesterdayNcstTime == "2500")
					yesterdayNcstTime = "0000";
				
				ncstYesterdayApi = new GetApi("getUltraSrtNcst", "500", yesterdayNcstDate, yesterdayNcstTime, searchNx, searchNy);
				ncstYesterdayApi.connectData();
				ncstYesterdayApi.setWeatherMap();
				ncstYesterdayApi.printAllWeatherMapValue();				
			};
		};

		ncstTodayApiThread = new Thread() {
			public void run() {
				ncstTodayApi = new GetApi("getUltraSrtNcst", "500", searchNcstDate, searchNcstTime, searchNx, searchNy);
				ncstTodayApi.connectData();
				ncstTodayApi.setWeatherMap();
				ncstTodayApi.printAllWeatherMapValue();				
			};
		};

		fcstApiThread = new Thread() {
			public void run() {
				fcstApi = new GetApi("getVilageFcst", "500", searchFcstDate, searchFcstTime, searchNx, searchNy);
				fcstApi.connectData();
				fcstApi.setWeatherMap();
//				fcstApi.printAllWeatherMapValue();
				defaultSetting();
			};
		};
		
//		thread 나누니까 빨라도 데이터 안불러왔을때 default Setting 생성해서 데이터 null뜰때가있다
//		스레드 합치던가, 모든 스레드가 종료된 후 를 알수있는 메소드 있는지 찾아보기

//		ncstYesterdayApiThread.start();
//		ncstTodayApiThread.start();
		fcstApiThread.start();
		
	}

	public void changePage(int pageIndex) {

		this.setTitle(pages[pageIndex].title);

		for (int i = 0; i < pages.length; i++) {
			pages[i].setVisible(pages[i].showFlag = (i == pageIndex ? true : false));
		}
		
		mainPanel.repaint();
		
	}

	public static void main(String[] args) {
		new MainDrive();
	}
}