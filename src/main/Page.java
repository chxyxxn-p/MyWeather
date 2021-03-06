package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class Page extends JPanel {
	
	protected MainDrive mainDrive;
	
	String title;
	protected int width, height;
	boolean showFlag;

	public Page(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		this.mainDrive = mainDrive;
		
		this.title = title;
		this.width = width;
		this.height = height;
		this.showFlag = showFlag;
		
		this.setBackground(new Color(0, 0, 0, 0));
//		this.setBackground(Color.yellow);


		this.setPreferredSize(new Dimension(width, height));
		
		this.setVisible(showFlag);
	}

	public String getTitle() {
		return title;
	}
	
	public void afterConnectApi() {
		System.out.println("afterConnectApi\trun");
	}
}
