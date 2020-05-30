package weather;

import java.awt.Color;

import javax.swing.JPanel;

import main.MainDrive;
import main.Page;

public class WeatherPage extends Page {
	
	JPanel todayPanel;
	JPanel fcstPanel;
	
	public WeatherPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		todayPanel = new JPanel();
		fcstPanel = new JPanel();
		
		todayPanel.setBackground(Color.magenta);
		fcstPanel.setBackground(Color.white);
		
		this.setLayout(null);
		
		todayPanel.setBounds(0, 0, width/3, height);
		fcstPanel.setBounds(width/3, 0, width/3*2, height);
		
		this.add(todayPanel);
		this.add(fcstPanel);
		
				
	}
}