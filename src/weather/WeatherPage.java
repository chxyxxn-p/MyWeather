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
	
	WeatherValue nwv;
	int nImgNum;
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		this.fcstWeatherMap = mainDrive.getFcstApi().getWeatherMap();
		this.fcstKeyList = mainDrive.getFcstApi().getKeyList();
		
		this.ncstTodayWeatherMap = mainDrive.getNcstTodayApi().getWeatherMap();
		this.ncstTodayKeyList = mainDrive.getNcstTodayApi().getKeyList();
		
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
		
//		ncst
		nwv = ncstTodayWeatherMap.get(ncstTodayKeyList.get(0));
		
		nImgNum = mainDrive.getNcstTodayApi().weatherImgNum(nwv);
		nowImg = new ImageIcon(mainDrive.getImageName()[nImgNum]).getImage();

		nowInfoTa.setText(mainDrive.getNcstTodayApi().weatherValueToString(nwv, "\n"));

//		fcst
		for(int i = 0 ; i < fcstWeatherMap.size() ; i++) {	//������ ��¥-�ð� Ű ������ŭ FcstPanel����
			WeatherValue fwv = fcstWeatherMap.get(fcstKeyList.get(i));

			int fImgNum = mainDrive.getFcstApi().weatherImgNum(fwv);	//���� �о�ͼ� ������ �°� �̹��� ��ȣ ����
			
			String info = mainDrive.getFcstApi().weatherValueToString(fwv, "\t");
			
			FcstPanel f = new FcstPanel(mainDrive, width/3*2-30, height/8, fImgNum, info.toString());
			fcstPanel.add(f);
		}
		
//		nowInfoTa.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
//		nowInfoTa.setAlignmentY(JTextArea.CENTER_ALIGNMENT);

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
		
		
//		nowInfoLb �ǵ帱 ������ ���� �׸���
		nowInfoTa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainDrive.getMainPanel().repaint();
				nowPanel.repaint();
			}
		});
		
//		FcstPanel ��ũ�� �����϶����� ���� �׸���
		fcstScroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				mainDrive.getMainPanel().repaint();
				fcstPanel.repaint();

			}
		});
		
	}
	
	public String getNowInfoTaText() {
		return nowInfoTa.getText();
	}
	
	public Image getNowImg() {
		return nowImg;
	}
	
	
	
	
	
	
}