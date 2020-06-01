package home;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	
	JPanel recommendPanel;
	
	JPanel diaryPanel;
	
	JPanel todoListPanel;
	
	
	public HomePage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);

//		nowWeather
		WeatherPage wp = ((WeatherPage)mainDrive.getPages()[1]);
		
		nowPanel = new JPanel();
		nowImgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(nowImg = wp.getNowImg(), 0, 0, width/4-40, width/4-40, mainDrive);
			}
		};
		nowInfoTa = new JTextArea();
		
		nowInfoTa.setEditable(false);
		
		nowInfoTa.setText(wp.getNowInfoTaText());

		nowInfoTa.setFont(mainDrive.getFont(20));
		
//		nowPanel.setBackground(Color.pink);
		nowPanel.setBackground(new Color(0,0,0,0));
		nowImgPn.setBackground(new Color(0,0,0,0));
		nowInfoTa.setBackground(new Color(0,0,0,0));
		nowInfoTa.setOpaque(true);
		
		nowPanel.setLayout(null);
		
		nowImgPn.setBounds(20, 10, width/4-40, width/4-40);
		nowInfoTa.setBounds(10, 10+width/4-40+10, width/4-20, height - width/4 +40 - 30);
		
		nowPanel.add(nowImgPn);
		nowPanel.add(nowInfoTa);
			
		nowPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(1);
			}
		});
		
//		nowInfoLb 건드릴 때마다 새로 그리기
		nowInfoTa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.getMainPanel().repaint();
				nowPanel.repaint();
			}
		});
			
		
//		recommend
		recommendPanel = new JPanel();
		
		recommendPanel.setBackground(Color.yellow);
//		recommendPanel.setBackground(new Color(0,0,0,0));

		recommendPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(4);
			}
		});
		
		
//		diary
		diaryPanel = new JPanel();
		
		diaryPanel.setBackground(Color.green);		
//		diaryPanel.setBackground(new Color(0,0,0,0));
		
		diaryPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(2);
			}
		});

		
//		toDoList
		todoListPanel = new JPanel();
		
		todoListPanel.setBackground(Color.blue);		
//		todoListPanel.setBackground(new Color(0,0,0,0));

		todoListPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.changePage(2);
			}
		});
		
		
//		page
		this.setLayout(null);
		
		nowPanel.setBounds(0, 0, width/4, height/3*2);
		diaryPanel.setBounds(width/4 + 10, 0, (width - width/4 + 10 + 10)/2, height/3*2);
		todoListPanel.setBounds((width/4 + 10) + ((width - width/4 + 10 + 10)/2 + 10), 0, (width - width/4 + 10 + 10)/2, height/3*2);
		recommendPanel.setBounds(0, height/3*2+10, width, height - (height/3*2+10));
		
		this.add(nowPanel);
		this.add(diaryPanel);
		this.add(todoListPanel);
		this.add(recommendPanel);
	}
}