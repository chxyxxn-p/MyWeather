package location;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import home.HomePage;
import main.MainDrive;
import main.Page;

public class LocationPage extends Page {
	
	ArrayList<Location> locationList = new ArrayList<Location>();

	JPanel locationPanel;
	JComboBox<String> firstSepCb;
	JComboBox<String> secondSepCb;
	JComboBox<String> thirdSepCb;
	JButton locationBt;
	
	int locationPanelWidth;
	int locationPanelHeight;
	
	public LocationPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		locationPanelWidth = width - 20;
		locationPanelHeight = height / 5;
		
		locationPanel = new JPanel();
		firstSepCb = new JComboBox<String>();
		secondSepCb = new JComboBox<String>();
		thirdSepCb = new JComboBox<String>();
		locationBt = new JButton("search");
		
		firstSepCb.setFont(mainDrive.getFont(14));
		secondSepCb.setFont(mainDrive.getFont(14));
		thirdSepCb.setFont(mainDrive.getFont(14));
		locationBt.setFont(mainDrive.getFont(12));
		
//		locationPanel.setBackground(Color.yellow);
		locationPanel.setBackground(new Color(0,0,0,0));
		firstSepCb.setBackground(Color.white);
		secondSepCb.setBackground(Color.white);
		thirdSepCb.setBackground(Color.white);
		locationBt.setBackground(Color.white);
		
		locationPanel.setLayout(null);
		
		firstSepCb.setBounds(10, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		secondSepCb.setBounds(20 + (locationPanelWidth-50)/10*3, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		thirdSepCb.setBounds(30 + (locationPanelWidth-50)/10*6, 10, (locationPanelWidth-50)/10*3, locationPanelHeight-20);
		locationBt.setBounds(40 + (locationPanelWidth-50)/10*9, 10, (locationPanelWidth-50)/10*1, locationPanelHeight-20);
		
		locationPanel.add(firstSepCb);
		locationPanel.add(secondSepCb);
		locationPanel.add(thirdSepCb);
		locationPanel.add(locationBt);
		
		this.setLayout(null);
		
		locationPanel.setBounds(10, 10, locationPanelWidth, locationPanelHeight);

		this.add(locationPanel);

		locationBt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				user�� �� ��ġ�� �ش��ϴ� nx, ny�����ͼ� mainDrive�� searchNx, searchNy�� �����ϰ�, ������� ���� ������ �ҷ�����
				mainDrive.runApi();
			}
		});
		
	}
	
	public void setComboBox() {
		for(int i = 0 ; i < locationList.size() ; i++) {
			Location location = locationList.get(i);
			
			String f = location.getFirstSep();
			String s = location.getSecondSep();
			String t = location.getThirdSep();
			
			firstSepCb.addItem(f);
			secondSepCb.addItem(s);
			thirdSepCb.addItem(t);
			
			((HomePage)mainDrive.getPages()[0]).setComboBox(f, s, t);
		}
		
		this.updateUI();
		
		
	}

	public void setLocationList(ArrayList<Location> locationList) {
		this.locationList = locationList;
	}

	public JComboBox<String> getFirstSepCb() {
		return firstSepCb;
	}

	public JComboBox<String> getSecondSepCb() {
		return secondSepCb;
	}

	public JComboBox<String> getThirdSepCb() {
		return thirdSepCb;
	}
}