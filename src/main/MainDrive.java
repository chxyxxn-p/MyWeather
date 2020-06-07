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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import calendar.CalendarPage;
import database.ConnectionManager;
import home.HomePage;
import location.LocationPage;
import login.LoginPage;
import login.LogoutPage;
import recommend.RecommendPage;
import weather.GetFinedustApi;
import weather.GetWeatherApi;
import weather.WeatherPage;

public class MainDrive extends JFrame {
	
	GraphicsEnvironment ge;
	int fontIndex;
	
	JPanel pagePn;
	Page[] pages = new Page[7];

	JPanel mainPn;
	
	JPanel menuPn;
	MenuIcon[] icons = new MenuIcon[6];
  
	String mainBgImgPath = "./res/animationsky.gif";
	String[] iconBgImgPathes = {"./res/i_home.png", "./res/i_weather.png", "./res/i_calendar.png", "./res/i_location.png", "./res/i_recommend.png", "./res/i_login.png"};
	String[] weatherIconImgPathes = {"./res/w_sunny.png", "./res/w_hot.png", "./res/w_hidr.png", "./res/w_rainy.png", "./res/w_windy.png", "./res/w_cloudy.png", "./res/w_snow.png"};
	String transparentImgPath = "./res/transparent.png";
	
	GetWeatherApi fcstApi;
	GetWeatherApi ncstApi;
	
	GetFinedustApi finedustApi;

	String searchFcstDate;
	String searchNcstDate;

	String searchFcstTime;
	String searchNcstTime;
	
	String[] weatherCase = {"맑음", "더움", "습함", "비/소나기", "바람", "흐림/안개", "눈"};
	int nowWeatherCaseNum;

	String searchFirstSep = "서울특별시";
	String searchSecondSep = "종로구";
	String searchThirdSep = "종로1.2.3.4가동";
	
	String searchNx = "60";
	String searchNy = "127";
	
	int pageWidth = 1000;
	int pageHeight = 600;

	boolean loginFlag;
	String loginUserName; // LoginPage에서 login되면 설정

	String xlsFilePath = "./res/location.xls";
	
	ConnectionManager connectionManager;

	public MainDrive() {
//		폰트 설정
		setFont();

//		현재시간 기준으로 검색할 base time 설정
		setSearchTime();
		
//		기본 GUI 그리기
		setDefaultGUI();
		
		connectionManager = new ConnectionManager();
		
//		데이터베이스 연결해서 comboBox items 채우기
		LocationPage lp = (LocationPage)pages[3];
		lp.connectDatabaseFirstSep(lp.getFirstSepCb());	//first->second , second->third 연쇄적으로 실행
		
		HomePage hp = (HomePage)pages[0];
		lp.connectDatabaseFirstSep(hp.getFirstSepCb());	//first->second , second->third 연쇄적으로 실행
		
//		멤버변수의 초기 위치 값을 두 페이지의 combo box의 선택 아이템으로 설정
		lp.synchronizeSelectedLocation();
		
//		mainDrive의 searchFirstSep, searchSecondSep, searchThirdSep의 값으로 searchNx, searchNy를 구한다
		lp.getSelectedLocationNxNy();

//		searchNx, searchNy 값으로 기본위치 api 불러오기
		runApi();
	}
	
	public void setFont() {
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		try {
			
			boolean registFontSuccess = ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/210 Gulim OTF 070.otf")));
			
			
			fontIndex = 10;
			
			for(int i = 0 ; i < ge.getAvailableFontFamilyNames().length ; i++) {

				if(ge.getAvailableFontFamilyNames()[i].equals("210 굴림OTF 070")
						|| ge.getAvailableFontFamilyNames()[i].equals("210 GulimOTF 070")) {
					fontIndex = i;
					break;
				}
			}
			
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Font getFont(int size) {
		return new Font(ge.getAvailableFontFamilyNames()[fontIndex], Font.PLAIN, size);
	}

	public void setSearchTime() {

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
		searchNcstDate = searchFcstDate = dateFormat.format(LocalDate.now());
		
		int hour = LocalTime.now().getHour();
		int minute = LocalTime.now().getMinute();
		
		int time = hour*100 + minute;
		
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

	public void setDefaultGUI() {
		
		System.out.println("defaultGUI\tsetting...");
		
//		메모리 적재
		Image img = new ImageIcon(mainBgImgPath).getImage();

		mainPn = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {

				g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		pagePn = new JPanel();

		pages[1] = new WeatherPage(this, "My Weather :: WEATHER", pageWidth, pageHeight, false);
		pages[2] = new CalendarPage(this, "My Weather :: CALENDAR", pageWidth, pageHeight, false);
		pages[3] = new LocationPage(this, "My Weather :: LOCATION", pageWidth, pageHeight, false);
		pages[4] = new RecommendPage(this, "My Weather :: RECOMMEND", pageWidth, pageHeight, false);
		pages[5] = new LogoutPage(this, "My Weather :: LOGOUT", pageWidth, pageHeight, false);
		pages[6] = new LoginPage(this, "My Weather :: LOGIN", pageWidth, pageHeight, false);
		
		pages[0] = new HomePage(this, "My Weather :: HOME", pageWidth, pageHeight, false);	//다른 페이지의 값을 받아오기때문에 실행 순서 변경
		

		menuPn = new JPanel();

		icons[0] = new MenuIcon(this, 50, 50, iconBgImgPathes[0]); // home
		icons[1] = new MenuIcon(this, 50, 50, iconBgImgPathes[1]); // weather
		icons[2] = new MenuIcon(this, 50, 50, iconBgImgPathes[2]); // calendar
		icons[3] = new MenuIcon(this, 50, 50, iconBgImgPathes[3]); // location
		icons[4] = new MenuIcon(this, 50, 50, iconBgImgPathes[4]); // recommend
		icons[5] = new MenuIcon(this, 50, 50, iconBgImgPathes[5]); // login/out

		changePage(6);

//		속성
		mainPn.setPreferredSize(new Dimension(pageWidth + 60, pageHeight));

		menuPn.setPreferredSize(new Dimension(60, pageHeight));
		menuPn.setBackground(new Color(0, 0, 0, 0));

		pagePn.setPreferredSize(new Dimension(pageWidth, pageHeight));
		pagePn.setBackground(new Color(0, 0, 0, 0));

//		조립
		for (int i = 0; i < icons.length; i++) {
			menuPn.add(icons[i]);
		}

		pagePn.setLayout(new FlowLayout(0, 0, 0));
		for (int i = 0; i < pages.length; i++) {
			pagePn.add(pages[i]);
		}

		mainPn.setLayout(new FlowLayout(0, 0, 0));
		mainPn.add(menuPn, BorderLayout.WEST);
		mainPn.add(pagePn);

		this.add(mainPn);
		this.setContentPane(mainPn);

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
					if (index == 0) {
						if(loginFlag) {
							((HomePage)pages[0]).changeRecommendMsg();
							changePage(index);
						} else {
							JLabel opLb = new JLabel("로그인이 필요한 서비스 입니다");
							opLb.setFont(getFont(13));
							JOptionPane.showMessageDialog(MainDrive.this, opLb);
						}
					} else if(index == 5) {
						if (loginFlag) {
							changePage(5);
						} else {
							changePage(6);
						}
					} else {
						if(loginFlag) {
							changePage(index);
						} else {
							JLabel opLb = new JLabel("로그인이 필요한 서비스 입니다");
							opLb.setFont(getFont(13));
							JOptionPane.showMessageDialog(MainDrive.this, opLb);
						}
					}
				}
			});
		}
		
		System.out.println("defaultGUI\tsetted");

	}

	public void runApi() {

		Thread integratedThread = new Thread() {
			public void run() {
				System.out.println("search location\t"+searchFirstSep+" "+searchSecondSep+" "+searchThirdSep+" / "+searchNx+" "+searchNy);
				
//				ncst
				ncstApi = new GetWeatherApi("getUltraSrtNcst", "500", searchNcstDate, searchNcstTime, searchNx, searchNy);
				ncstApi.connectData();
				ncstApi.setWeatherMap();
				
//				finedust
				finedustApi = new GetFinedustApi(searchFirstSep);
				finedustApi.transformSearchLocationString();
				finedustApi.connectData();
				finedustApi.setFinedustString();
				
//				fcst
				fcstApi = new GetWeatherApi("getVilageFcst", "500", searchFcstDate, searchFcstTime, searchNx, searchNy);
				fcstApi.connectData();
				fcstApi.setWeatherMap();
				
//				after
				for(int i = 1 ; i < pages.length ; i++) {	
					System.out.print("pages["+i+"]\t");
					pages[i].afterConnectApi();
				}
				
				System.out.print("pages[0]\t");
				pages[0].afterConnectApi();	//homePage(index:0) 는 다른 페이지에서 데이터 불러오기때문에 마지막에 실행
			};
		};
		

		integratedThread.start();
	}

	public void changePage(int pageIndex) {

		this.setTitle(pages[pageIndex].title);

		for (int i = 0; i < pages.length; i++) {
			pages[i].setVisible(pages[i].showFlag = (i == pageIndex ? true : false));
		}
		
		mainPn.repaint();
		
	}

	public Page[] getPages() {
		return pages;
	}

	public JPanel getMainPn() {
		return mainPn;
	}

	public String[] getweatherIconImgPathes() {
		return weatherIconImgPathes;
	}

	public String getTransparentImgPath() {
		return transparentImgPath;
	}

	public GetWeatherApi getFcstApi() {
		return fcstApi;
	}

	public GetWeatherApi getNcstApi() {
		return ncstApi;
	}

	public GetFinedustApi getFinedustApi() {
		return finedustApi;
	}

	public int getNowWeatherCaseNum() {
		return nowWeatherCaseNum;
	}

	public void setNowWeatherCaseNum(int nowWeatherCaseNum) {
		this.nowWeatherCaseNum = nowWeatherCaseNum;
	}

	public String[] getWeatherCase() {
		return weatherCase;
	}

	public String getSearchFirstSep() {
		return searchFirstSep;
	}

	public void setSearchFirstSep(String searchFirstSep) {
		this.searchFirstSep = searchFirstSep;
	}

	public String getSearchSecondSep() {
		return searchSecondSep;
	}

	public void setSearchSecondSep(String searchSecondSep) {
		this.searchSecondSep = searchSecondSep;
	}

	public String getSearchThirdSep() {
		return searchThirdSep;
	}

	public void setSearchThirdSep(String searchThirdSep) {
		this.searchThirdSep = searchThirdSep;
	}

	public String getSearchNx() {
		return searchNx;
	}

	public void setSearchNx(String searchNx) {
		this.searchNx = searchNx;
	}

	public String getSearchNy() {
		return searchNy;
	}

	public void setSearchNy(String searchNy) {
		this.searchNy = searchNy;
	}

	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}



	public static void main(String[] args) {
		new MainDrive();
	}
}