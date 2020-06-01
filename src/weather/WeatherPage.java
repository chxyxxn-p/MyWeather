package weather;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.MainDrive;
import main.Page;

public class WeatherPage extends Page {
	
	Map<Long, WeatherValue> fcstWeatherMap;
	ArrayList<Long> fcstKeyList;
	
	Map<Long, WeatherValue> ncstTodayWeatherMap;
	ArrayList<Long> ncstTodayKeyList;
	
	JPanel nowPanel;
	Image nowImg;
	JPanel nowImgPn;
	String nowImgPath = "./res/ball_yellow.png";
	JTextArea nowInfoTa;

	JPanel fcstPanel;
	JScrollPane fcstScroll;
	ArrayList<FcstPanel> fcstPanels = new ArrayList<FcstPanel>();
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		this.fcstWeatherMap = mainDrive.fcstApi.weatherMap;
		this.fcstKeyList = mainDrive.fcstApi.keyList;
		
		this.ncstTodayWeatherMap = mainDrive.ncstTodayApi.weatherMap;
		this.ncstTodayKeyList = mainDrive.ncstTodayApi.keyList;
		
		nowPanel = new JPanel();
		nowImg = new ImageIcon(nowImgPath).getImage();
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
		
		nowInfoTa.setText(mainDrive.ncstTodayApi.weatherValueToString(ncstTodayWeatherMap.get(ncstTodayKeyList.get(0)), "\n"));

		for(int i = 0 ; i < fcstWeatherMap.size() ; i++) {	//예측된 날짜-시간 키 갯수만큼 FcstPanel생성
			WeatherValue wv = fcstWeatherMap.get(fcstKeyList.get(i));

			int imgNum = 0;	//날씨 읽어와서 유형에 맞게 이미지 번호 설정
			
			String info = mainDrive.fcstApi.weatherValueToString(wv, "\t");
			
			FcstPanel f = new FcstPanel(mainDrive, width/3*2-30, height/8, imgNum, info.toString());
			fcstPanel.add(f);
		}
		
		nowInfoTa.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		nowInfoTa.setAlignmentY(JTextArea.CENTER_ALIGNMENT);

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
		
		fcstPanel.setLayout(new GridLayout(fcstWeatherMap.size(), 1, 0, 10));
		
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
				mainDrive.mainPanel.repaint();
				nowPanel.repaint();
			}
		});
		
//		FcstPanel 스크롤 움직일때마다 새로 그리기
		fcstScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				mainDrive.mainPanel.repaint();
				fcstPanel.repaint();

			}
		});
		
	}
}