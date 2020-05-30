package weather;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MainDrive;

public class FcstPanel  extends JPanel {
	
	MainDrive mainDrive;
	String[] imageName = {"./res/ball_yellow.png"};
	Image weatherImg;
	JPanel imgPn;
	JLabel infoLb;
	
	int width;
	int height;
	
	public FcstPanel(MainDrive mainDrive, int width, int height, int imgNum, String info) {
		this.mainDrive = mainDrive;
		
		this.width = width;
		this.height = height;
		
		this.weatherImg = new ImageIcon(imageName[imgNum]).getImage();
		this.imgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(weatherImg, 0, 0, height-10, height-10, mainDrive);
			}
		};
		this.infoLb = new JLabel(info);
		this.infoLb.setFont(mainDrive.mainFont);
		
		imgPn.setPreferredSize(new Dimension(height-10, height-10));
		infoLb.setPreferredSize(new Dimension(width - height - 10, height));
		
		this.setBackground(Color.white);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		this.add(imgPn);
		this.add(infoLb);
		
		this.setVisible(true);
	}
	

}
