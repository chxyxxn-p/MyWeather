package weather;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.MainDrive;

public class FcstPanel  extends JPanel {
	
	MainDrive mainDrive;
	Image weatherImg;
	JPanel imgPn;
	JTextArea infoTa;
	
	int width;
	int height;
	
	public FcstPanel(MainDrive mainDrive, int width, int height, int imgNum, String info) {
		this.mainDrive = mainDrive;
		
		this.width = width;
		this.height = height;
		
		this.weatherImg = new ImageIcon(mainDrive.getImageName()[imgNum]).getImage();
		this.imgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(weatherImg, 0, 0, height-10, height-10, mainDrive);
			}
		};
		this.infoTa = new JTextArea(info);
		
		this.infoTa.setEditable(false);
//		this.infoTa.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
//		this.infoTa.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		this.infoTa.setFont(mainDrive.getFont(14));
		
		imgPn.setPreferredSize(new Dimension(height-10, height-10));
		infoTa.setPreferredSize(new Dimension(width - height - 10, height));
		
		this.infoTa.setBackground(new Color(0,0,0,0));
		this.setBackground(new Color(255,255,255,120));
//		this.setBackground(Color.white);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		this.add(imgPn);
		this.add(infoTa);
		
		this.setVisible(true);
	}
	

}
