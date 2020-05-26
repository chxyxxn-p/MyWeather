package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Page extends JPanel {
	
	MainDrive mainDrive;
	
	public String title;
	int width, height;
	Color color;
	boolean showFlag;

	public Page(MainDrive mainDrive, String title, int width, int height, Color color, boolean showFlag) {
		this.mainDrive = mainDrive;
		
		this.title = title;
		this.width = width;
		this.height = height;
		this.color = color;
		this.showFlag = showFlag;
		
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(color);
		this.setVisible(showFlag);
	}
}
