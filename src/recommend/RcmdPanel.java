package recommend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.MainDrive;

public class RcmdPanel  extends JPanel {
	
	MainDrive mainDrive;
	Image storeImg;
	JPanel imgPn;
	JTextArea nameTa;
	JTextArea addressTa;
	JTextArea phoneTa;
	
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
		
		this.storeImg = new ImageIcon(imageUrl).getImage();
		
		this.imgPn = new JPanel() {
			@Override
			public void paint(Graphics g) {
//				super.paint(g);
				g.drawImage(storeImg, 0, 0, height-10, height-10, mainDrive);
			}
		};
		
		this.nameTa = new JTextArea(name);
		this.addressTa = new JTextArea(address);
		this.phoneTa = new JTextArea(phone);
		
		this.nameTa.setEditable(false);
		this.addressTa.setEditable(false);
		this.phoneTa.setEditable(false);

		this.nameTa.setFont(mainDrive.getFont(20));
		this.addressTa.setFont(mainDrive.getFont(20));
		this.phoneTa.setFont(mainDrive.getFont(20));
		
		imgPn.setPreferredSize(new Dimension(width-20, width-20));
		nameTa.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
		addressTa.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
		phoneTa.setPreferredSize(new Dimension(width-20, (height-width-30)/3));
	
		this.nameTa.setBackground(new Color(0,0,0,0));
		this.addressTa.setBackground(new Color(0,0,0,0));
		this.phoneTa.setBackground(new Color(0,0,0,0));
		
		this.imgPn.setBackground(Color.red);
//		this.nameTa.setBackground(Color.green);
//		this.addressTa.setBackground(Color.cyan);
//		this.phoneTa.setBackground(Color.orange);
		
		this.setBackground(new Color(255,255,255,120));
//		this.setBackground(Color.white);
		
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		this.add(imgPn);
		this.add(nameTa);
		this.add(addressTa);
		this.add(phoneTa);

		this.setPreferredSize(new Dimension(width, height));
				
		this.setVisible(true);
		
	}
	

}
