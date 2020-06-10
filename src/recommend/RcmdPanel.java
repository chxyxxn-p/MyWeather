package recommend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.MainDrive;

public class RcmdPanel  extends JPanel {
	
	MainDrive mainDrive;
	Image storeImg;
	JPanel imgPn;
	JLabel nameLb;
	JLabel addressLb;
	JLabel phoneLb;
	
	int width;
	int height;
	
	int pnWidth;
	int pnHeight;
	int taWidth;
	int taHeight;
	
	String name;
	String address;
	String phone;
	String image;
	
	URL imageUrl;
	
	public RcmdPanel(MainDrive mainDrive, int width, int height, String name, String address, String phone, String image) {
		this.mainDrive = mainDrive;
		
		this.width = width;
		this.height = height;
		
		this.taWidth = this.pnHeight = this.pnWidth = width - 20;
		this.taHeight = (height-width-30)/3;
		
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.image = image;
		
		try {
			this.imageUrl = new URL(image);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		try {
			this.storeImg = ImageIO.read(imageUrl).getScaledInstance(width-10, width-10, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.imgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
//				super.paint(g);
				g.drawImage(storeImg, 0, 0, width-10, width-10, this);
			}
		};

		this.nameLb = new JLabel(name, JLabel.CENTER);
		this.addressLb = new JLabel(address, JLabel.CENTER);
		this.phoneLb = new JLabel(phone, JLabel.CENTER);

		this.nameLb.setFont(mainDrive.getFont(20));
		this.addressLb.setFont(mainDrive.getFont(20));
		this.phoneLb.setFont(mainDrive.getFont(20));
		
		imgPn.setPreferredSize(new Dimension(width-20, width-20));
		nameLb.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
		addressLb.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
		phoneLb.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
	
		this.nameLb.setBackground(new Color(0,0,0,0));
		this.addressLb.setBackground(new Color(0,0,0,0));
		this.phoneLb.setBackground(new Color(0,0,0,0));
		
		this.setBackground(new Color(255,255,255,120));
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		this.add(imgPn);
		this.add(nameLb);
		this.add(addressLb);
		this.add(phoneLb);

		this.setPreferredSize(new Dimension(width, height));
				
		this.setVisible(true);
		
	}
	

}
