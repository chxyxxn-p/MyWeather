package weather;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.MainDrive;
import main.Page;
import recommend.RecommendPage;

public class WeatherPage extends Page {
	
	Map<Long, Weather> fcstWeatherMap;
	ArrayList<Long> fcstKeyList;
	
	Map<Long, Weather> ncstTodayWeatherMap;
	ArrayList<Long> ncstTodayKeyList;
	
	JPanel nowPanel;
	Image nowImg = new ImageIcon(mainDrive.getTransparentImgPath()).getImage();
	JPanel nowImgPn;
	JTextArea nowInfoTa;

	JPanel fcstPanel;
	JScrollPane fcstScroll;
	ArrayList<FcstPanel> fcstPanels = new ArrayList<FcstPanel>();
	
	Weather nwv;
	int nImgNum;
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		nowPanel = new JPanel();
		nowImgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(nowImg, 0, 0, width/3-40, width/3-40, mainDrive);
			}
		};
		nowInfoTa = new JTextArea();
		nowInfoTa.setEditable(false);
		
		fcstPanel = new JPanel();
		fcstScroll = new JScrollPane(fcstPanel);
		
		nowInfoTa.setFont(mainDrive.getFont(25));
		
		nowImgPn.setBackground(new Color(0,0,0,0));
		nowInfoTa.setBackground(new Color(0,0,0,0));
		nowInfoTa.setOpaque(true);
		nowPanel.setBackground(new Color(0,0,0,0));
		fcstPanel.setBackground(new Color(0,0,0,0));
		fcstScroll.setBackground(new Color(0,0,0,0));
		
		nowPanel.setLayout(null);
		
		nowImgPn.setBounds(20, 10, width/3-40, width/3-40);
		nowInfoTa.setBounds(10, 10+width/3-40+10, width/3-20, height - width/3 +40 - 30);
		
		nowPanel.add(nowImgPn);
		nowPanel.add(nowInfoTa);
		
		
		nowPanel.setBounds(0, 0, width/3, height);
		
		fcstPanel.setBounds(width/3, 0, width/3*2, height);
		fcstScroll.setBounds(width/3, 0, width/3*2, height);
		
		this.setLayout(null);
		this.add(nowPanel);
		this.add(fcstScroll);
		
		
//		nowInfoLb 건드릴 때마다 새로 그리기
		nowInfoTa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.getMainPn().repaint();
				nowPanel.repaint();
			}
		});
		
//		FcstPanel 스크롤 움직일때마다 새로 그리기
		fcstScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				mainDrive.getMainPn().repaint();
				fcstPanel.repaint();
				
			}
		});
		
	}
	
	@Override
	public void afterConnectApi() {
		super.afterConnectApi();
		
		this.fcstWeatherMap = mainDrive.getFcstApi().getWeatherMap();
		this.fcstKeyList = mainDrive.getFcstApi().getKeyList();
		
		this.ncstTodayWeatherMap = mainDrive.getNcstTodayApi().getWeatherMap();
		this.ncstTodayKeyList = mainDrive.getNcstTodayApi().getKeyList();
		
//		ncst
		nwv = ncstTodayWeatherMap.get(ncstTodayKeyList.get(0));
		
		nImgNum = mainDrive.getNcstTodayApi().getWeatherImgNum(nwv, "ncst");
		mainDrive.setNowWeatherCaseNum(nImgNum);
		nowImg = new ImageIcon(mainDrive.getweatherIconImgPathes()[nImgNum]).getImage();

		nowInfoTa.setText(mainDrive.getNcstTodayApi().weatherValueToString(nwv, "\n"));
		

//		fcst
		for(int i = 0 ; i < fcstWeatherMap.size() ; i++) {	//예측된 날짜-시간 키 갯수만큼 FcstPanel생성
			Weather fwv = fcstWeatherMap.get(fcstKeyList.get(i));

			int fImgNum = mainDrive.getFcstApi().getWeatherImgNum(fwv, "fcst");	//날씨 읽어와서 유형에 맞게 이미지 번호 설정
		
			String info = mainDrive.getFcstApi().weatherValueToString(fwv, "\t");
			
			FcstPanel f = new FcstPanel(mainDrive, width/3*2-30, height/8, fImgNum, info.toString());
			fcstPanel.add(f);
		}
		
		fcstPanel.setLayout(new GridLayout(fcstWeatherMap.size(), 1, 0, 10));

		this.updateUI();
	}
	
	public String getNowInfoTaText() {
		return nowInfoTa.getText();
	}
	
	public Image getNowImg() {
		return nowImg;
	}
	
	
	
	
	
	
}