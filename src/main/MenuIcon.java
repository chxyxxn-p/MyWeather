package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MenuIcon extends JPanel {

	MainDrive mainDrive;
	int width, height;
	String bgImgPath;
	Image backgroundImg;

	public MenuIcon(MainDrive mainDrive, int width, int height, String bgImgPath) {
		
		this.mainDrive= mainDrive;

		this.width = width;
		this.height = height;
		this.bgImgPath = bgImgPath;
		
		this.setPreferredSize(new Dimension(width, height));
		
		this.backgroundImg = new ImageIcon(bgImgPath).getImage();
		this.repaint();
		}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, width, height, mainDrive);
	}
}
