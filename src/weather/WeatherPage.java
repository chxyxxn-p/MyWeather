package weather;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.MainDrive;
import main.Page;

public class WeatherPage extends Page {
	
	Map<Long, WeatherValue> weatherMap;
	ArrayList<Long> keyList;
	
	JPanel nowPanel;
	JPanel fcstPanel;
	JScrollPane fcstScroll;
	
	ArrayList<FcstPanel> fcstPanels = new ArrayList<FcstPanel>();
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		this.weatherMap = mainDrive.fcstApi.weatherMap;
		this.keyList = mainDrive.fcstApi.keyList;
		
		nowPanel = new JPanel();
		fcstPanel = new JPanel();
		fcstScroll = new JScrollPane(fcstPanel);

		for(int i = 0 ; i < weatherMap.size() ; i++) {	//예측된 날짜-시간 키 갯수만큼 FcstPanel생성
			WeatherValue wv = weatherMap.get(keyList.get(i));
			System.out.println(keyList.get(i));
			System.out.println(wv.getT3h());
			int imgNum = 0;	//날씨 읽어와서 유형에 맞게 이미지 번호 설정
			
			StringBuilder info = new StringBuilder();
			if(wv.getT3h()!=null) info.append("기온 : " + wv.getT3h() + "℃");
			System.out.println(info.toString());

			FcstPanel f = new FcstPanel(mainDrive, width/3*2-30, height/6, imgNum, info.toString());
			fcstPanel.add(f);
		}
		
		nowPanel.setBackground(Color.magenta);
		fcstPanel.setBackground(Color.cyan);
		
		
		nowPanel.setBounds(0, 0, width/3, height);
		fcstPanel.setBounds(width/3, 0, width/3*2, height);
		fcstScroll.setBounds(width/3, 0, width/3*2, height);
		
		fcstPanel.setLayout(new GridLayout(weatherMap.size(), 1, 0, 10));
		
		this.setLayout(null);
		this.add(nowPanel);
		this.add(fcstScroll);
		
				
	}
}