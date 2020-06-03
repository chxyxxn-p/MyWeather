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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import calendar.CalendarPage;
import database.ConnectionManager;
import home.HomePage;
import location.Location;
import location.LocationPage;
import login.LoginPage;
import login.LogoutPage;
import recommend.RecommendPage;
import weather.GetApi;
import weather.WeatherPage;

public class MainDrive extends JFrame {
	
	GraphicsEnvironment ge;
	int fontIndex;
	
	JPanel pagePn;
	Page[] pages = new Page[7];

	JPanel mainPn;
	
	JPanel menuPn;
	MenuIcon[] icons = new MenuIcon[6];
  
	String mainBgImgPath = "./res/catsky.gif";
	String iconBgImgPath = "./res/ball_darkred.png";
	String[] weatherIconImgPathes = {"./res/ball_yellow.png", "./res/ball_purple.png", "./res/ball_blue.png", "./res/ball_green.png", "./res/ball_orange.png", "./res/ball_red.png"};
	String transparentImgPath = "./res/transparent.png";
	
	GetApi fcstApi;
	GetApi ncstTodayApi;
	GetApi ncstYesterdayApi;

	String searchFcstDate;
	String searchNcstDate;

	String searchFcstTime;
	String searchNcstTime;
	
	String[] weatherCase = {"맑음", "더움", "습함", "비/소나기", "바람", "흐림/안개", "눈"};
	int nowWeatherCaseNum;

	String searchNx = "60";
	String searchNy = "127";
	
	int pageWidth = 1000;
	int pageHeight = 600;

	boolean loginFlag;
	String loginUserName; // LoginPage에서 login되면 설정

	String xlsFilePath = "./res/location.xls";
	
	Thread ncstYesterdayApiThread;
	Thread ncstTodayApiThread;
	Thread fcstApiThread;
	Thread afterApiThread;

	public MainDrive() {
//		폰트 설정
		setFont();

//		현재시간 기준으로 검색할 base time 설정
		setSearchTime();
		
//		api 연결
		setDefaultGUI();
				
//		검색할 위치 고르는 ComboBox의 items -> xls로 불러오기
		getLocationFromXls();
	}
	

	
	public void setFont() {
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		try {
			
//			boolean registFontSuccess = ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/NanumSquareR.ttf")));
			boolean registFontSuccess = ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/210 Gulim OTF 070.otf")));
			
//			System.out.println("font regist : " + registFontSuccess);
			
			fontIndex = 10;
			
			for(int i = 0 ; i < ge.getAvailableFontFamilyNames().length ; i++) {
//				if(ge.getAvailableFontFamilyNames()[i].equals("NanumSquare")||ge.getAvailableFontFamilyNames()[i].equals("나눔스퀘어 Regular")) {
				if(ge.getAvailableFontFamilyNames()[i].equals("210 굴림OTF 070")) {
					fontIndex = i;
					break;
				}
			}
//			System.out.println("registed font index : " + fontIndex + "/" + ge.getAvailableFontFamilyNames()[fontIndex]);
			
			
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

	public void setDefaultGUI() {
		
		System.out.println("main default setting...");
		
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

		pages[1] = new WeatherPage(this, "WEATHER", pageWidth, pageHeight, false);
		pages[2] = new CalendarPage(this, "CALENDAR", pageWidth, pageHeight, false);
		pages[3] = new LocationPage(this, "LOCATION", pageWidth, pageHeight, false);
		pages[4] = new RecommendPage(this, "RECOMMEND", pageWidth, pageHeight, false);
		
		pages[0] = new HomePage(this, "HOME", pageWidth, pageHeight, false);	//다른 페이지의 값을 받아오기때문에 실행 순서 변경
		
		pages[5] = new LogoutPage(this, "LOGOUT", pageWidth, pageHeight, false);
		pages[6] = new LoginPage(this, "LOGIN", pageWidth, pageHeight, false);

		menuPn = new JPanel();

		icons[0] = new MenuIcon(this, 50, 50, iconBgImgPath); // home
		icons[1] = new MenuIcon(this, 50, 50, iconBgImgPath); // weather
		icons[2] = new MenuIcon(this, 50, 50, iconBgImgPath); // calendar
		icons[3] = new MenuIcon(this, 50, 50, iconBgImgPath); // location
		icons[4] = new MenuIcon(this, 50, 50, iconBgImgPath); // recommend
		icons[5] = new MenuIcon(this, 50, 50, iconBgImgPath); // login/out

		changePage(6); // 처음으로 보여줄 페이지 //#나중에 로그인페이지or홈페이지를 시작으로 변경

//		속성
		mainPn.setPreferredSize(new Dimension(pageWidth + 60, pageHeight));

		menuPn.setPreferredSize(new Dimension(60, pageHeight));
		menuPn.setBackground(new Color(0, 0, 0, 0));

//		System.out.println(menuPanel.getWidth()); -> 60출력 예상.. 0 출력됨
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
					if (index != 5) {
						if(loginFlag) {
							changePage(index);
						} else {
							JOptionPane.showMessageDialog(MainDrive.this, "로그인이 필요한 서비스 입니다");
						}
					} else {
						if (loginFlag) {
							changePage(5);
						} else {
							changePage(6);
						}
					}
				}
			});
		}
		
		System.out.println("main default set done!");

	}
	
	public void getLocationFromXls() {
//		엑셀로 불러온 위치 정보를 LocationPage의 list에 저장
		System.out.println("getting location info...");

		ArrayList<Location> locationList = new ArrayList<Location>();

		FileInputStream fis = null;
		
		try {
			fis = new FileInputStream(xlsFilePath);
			
			HSSFSheet sheet = new HSSFWorkbook(fis).getSheet("location");
			
			for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
				Location location = new Location();
				HSSFRow row = sheet.getRow(rowIndex);
				location.setFirstSep(row.getCell(0).getStringCellValue());
				location.setSecondSep(row.getCell(1).getStringCellValue());
				location.setThirdSep(row.getCell(2).getStringCellValue());
				location.setNx((int)row.getCell(3).getNumericCellValue());
				location.setNy((int)row.getCell(4).getNumericCellValue());
				locationList.add(location);
			}
			
			((LocationPage)pages[3]).setLocationList(locationList);
			((LocationPage)pages[3]).setComboBox();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		System.out.println("get location info done!");
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
//				ncstYesterdayApi.printAllWeatherMapValue();				
			};
		};

		ncstTodayApiThread = new Thread() {
			public void run() {
				ncstTodayApi = new GetApi("getUltraSrtNcst", "500", searchNcstDate, searchNcstTime, searchNx, searchNy);
				ncstTodayApi.connectData();
				ncstTodayApi.setWeatherMap();
//				ncstTodayApi.printAllWeatherMapValue();				
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
		
		afterApiThread = new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				for(int i = 1 ; i < pages.length ; i++) {
					pages[i].afterConnectApi();
				}
				pages[0].afterConnectApi();	//homePage는 다른 페이지에서 데이터 불러오기때문에 마지막에 실행

			};
		};
		
		ncstYesterdayApiThread.start();
		ncstTodayApiThread.start();
		fcstApiThread.start();
		afterApiThread.start();
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

	public GetApi getFcstApi() {
		return fcstApi;
	}

	public GetApi getNcstTodayApi() {
		return ncstTodayApi;
	}

	public GetApi getNcstYesterdayApi() {
		return ncstYesterdayApi;
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
	

	public static void main(String[] args) {
		new MainDrive();
	}
}