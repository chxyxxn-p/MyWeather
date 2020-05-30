package weather;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.MainDrive;
import main.Page;

public class WeatherPage extends Page {
	
	JPanel nowPanel;
	JPanel fcstPanel;
	JScrollPane fcstScroll;
	
	ArrayList<FcstPanel> fcstPanels = new ArrayList<FcstPanel>();
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		nowPanel = new JPanel();
		fcstPanel = new JPanel();
		fcstScroll = new JScrollPane(fcstPanel);
		
		System.out.println(mainDrive.fcstApi.weatherMap.size());
		
		for(int i = 0 ; i < mainDrive.fcstApi.weatherMap.size() ; i++) {	//예측된 날짜-시간 키 갯수만큼 FcstPanel생성
			int imgNum = 0;	//날씨 읽어와서 유형에 맞게 이미지 번호 설정
			String info = "날씨 설명~~";	//날씨 읽어와서 스트링빌더로 추가
			FcstPanel f = new FcstPanel(mainDrive, width/3*2, height/6, imgNum, info);
			fcstPanel.add(f);
			System.out.println("FcstPanel 생성");
		}
		
		nowPanel.setBackground(Color.magenta);
		fcstPanel.setBackground(Color.cyan);
		
		
		nowPanel.setBounds(0, 0, width/3, height);
		fcstPanel.setBounds(width/3, 0, width/3*2, height);
		
		fcstPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
		
		this.setLayout(null);
		this.add(nowPanel);
		this.add(fcstPanel);
		
				
	}
}