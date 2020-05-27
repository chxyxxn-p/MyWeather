package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import sun.java2d.pipe.DrawImage;

public class Page extends JPanel {
	
	MainDrive mainDrive;
	
	public String title;
	int width, height;
	String bgImgPath;
	Image backgroundImg;
	boolean showFlag;

	public Page(MainDrive mainDrive, String title, int width, int height, String bgImgPath, boolean showFlag) {
		
		this.mainDrive = mainDrive;
		
		this.title = title;
		this.width = width;
		this.height = height;
		this.bgImgPath = bgImgPath;
		this.showFlag = showFlag;
		
		this.setPreferredSize(new Dimension(width, height));
		
		this.backgroundImg = Toolkit.getDefaultToolkit().createImage(bgImgPath);
		this.repaint();
		
		this.setVisible(showFlag);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, width, height, mainDrive);
	}
}
