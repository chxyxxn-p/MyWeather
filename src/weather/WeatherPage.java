package weather;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.MainDrive;
import main.Page;

public class WeatherPage extends Page {
	
	Map<Long, WeatherValue> weatherMap;
	ArrayList<Long> keyList;
	
	JPanel nowPanel;
	Image nowImg;
	JPanel nowImgPn;
	String nowImgPath = "./res/ball_yellow.png";
	JLabel nowInfoLb;

	JPanel fcstPanel;
	JScrollPane fcstScroll;
	ArrayList<FcstPanel> fcstPanels = new ArrayList<FcstPanel>();
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		this.weatherMap = mainDrive.fcstApi.weatherMap;
		this.keyList = mainDrive.fcstApi.keyList;
		
		nowPanel = new JPanel();
		nowImg = new ImageIcon(nowImgPath).getImage();
		nowImgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(nowImg, 0, 0, width/3-40, width/3-40, mainDrive);
			}
		};
		
		nowInfoLb = new JLabel("now info");
		
		fcstPanel = new JPanel();
		fcstScroll = new JScrollPane(fcstPanel);

		for(int i = 0 ; i < weatherMap.size() ; i++) {	//������ ��¥-�ð� Ű ������ŭ FcstPanel����
			WeatherValue wv = weatherMap.get(keyList.get(i));

			int imgNum = 0;	//���� �о�ͼ� ������ �°� �̹��� ��ȣ ����
			
//			StringBuilder info = new StringBuilder();
//			if(wv.getT3h()!=null) info.append("��� : " + wv.getT3h() + "��");

			String info = mainDrive.fcstApi.weatherValueToString(wv);
			FcstPanel f = new FcstPanel(mainDrive, width/3*2-30, height/6, imgNum, info.toString());
			fcstPanel.add(f);
		}
		
		nowInfoLb.setFont(mainDrive.getFont(18));
		
		nowImgPn.setBackground(Color.red);
		nowInfoLb.setBackground(Color.pink);
		nowInfoLb.setOpaque(true);
		nowPanel.setBackground(Color.cyan);
		fcstPanel.setBackground(Color.magenta);
		
		nowPanel.setLayout(null);
		
		nowImgPn.setBounds(20, 10, width/3-40, width/3-40);
		nowInfoLb.setBounds(10, 10+width/3-40+10, width/3-20, height - width/3 +40 - 30);
		System.out.println(width+"/"+height);
		System.out.println(10+"/"+ (10+width/3-40+10)+"/"+ (width/3-20)+"/"+ (height - width/3+40 -10-10-10));
		
		nowPanel.add(nowImgPn);
		nowPanel.add(nowInfoLb);
		
		fcstPanel.setLayout(new GridLayout(weatherMap.size(), 1, 0, 10));
		
		nowPanel.setBounds(0, 0, width/3, height);
		
		fcstPanel.setBounds(width/3, 0, width/3*2, height);
		fcstScroll.setBounds(width/3, 0, width/3*2, height);

		this.setLayout(null);
		this.add(nowPanel);
		this.add(fcstScroll);
		
				
	}
}