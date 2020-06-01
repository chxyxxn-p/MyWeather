package home;

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
import weather.FcstPanel;
import weather.WeatherPage;
import weather.WeatherValue;

public class HomePage extends Page {
	
	Map<Long, WeatherValue> ncstTodayWeatherMap;
	ArrayList<Long> ncstTodayKeyList;
	
	JPanel nowPanel;
	Image nowImg;
	JPanel nowImgPn;
	JTextArea nowInfoTa;
	
	public HomePage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		WeatherPage wp = ((WeatherPage)mainDrive.getPages()[1]);
		
		nowPanel = new JPanel();
		nowImgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(nowImg, 0, 0, width/3-40, width/3-40, mainDrive);
			}
		};
		nowInfoTa = new JTextArea();
		nowInfoTa.setEditable(false);
		
		WeatherValue nwv =  wp.getNwv();
		
		nowImg = wp.getNowImg();

		nowInfoTa.setText(wp.getNowInfoTaText());

//		nowInfoTa.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
//		nowInfoTa.setAlignmentY(JTextArea.CENTER_ALIGNMENT);

		nowInfoTa.setFont(mainDrive.getFont(25));
		
		nowImgPn.setBackground(new Color(0,0,0,0));
		nowInfoTa.setBackground(new Color(0,0,0,0));
		nowInfoTa.setOpaque(true);
		nowPanel.setBackground(new Color(0,0,0,0));
		
		nowPanel.setLayout(null);
		
		nowImgPn.setBounds(20, 10, width/3-40, width/3-40);
		nowInfoTa.setBounds(10, 10+width/3-40+10, width/3-20, height - width/3 +40 - 30);
		
		nowPanel.add(nowImgPn);
		nowPanel.add(nowInfoTa);
		
		nowPanel.setBounds(0, 0, width/3, height);
		

		this.setLayout(null);
		this.add(nowPanel);
		
		
//		nowInfoLb 건드릴 때마다 새로 그리기
		nowInfoTa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.getMainPanel().repaint();
				nowPanel.repaint();
			}
		});
				
	}
}