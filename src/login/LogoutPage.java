package login;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import main.MainDrive;
import main.Page;

public class LogoutPage extends Page {
	
	JLabel greetingLb;
	JButton logoutBt;

	public LogoutPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);

		greetingLb = new JLabel(mainDrive.loginUserName + "´Ô, ¹Ý°©½À´Ï´Ù!", JLabel.CENTER);
		logoutBt = new JButton("LOGOUT");
		
		greetingLb.setFont(mainDrive.mainFont);
		logoutBt.setFont(mainDrive.mainFont);
		
		logoutBt.setBackground(Color.white);
		
		
		this.setLayout(null);
		
		greetingLb.setBounds(width/3, height/8, width/3, height/2);
		logoutBt.setBounds((width - width/5)/2, height/2 + (height/2-height/8)/2, width/5, height/8);
		
		this.add(greetingLb);
		this.add(logoutBt);
		
		logoutBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				logout();
			}
		});
	}
	
	public void logout() {
		int result = JOptionPane.showConfirmDialog(mainDrive, "·Î±×¾Æ¿ô ÇÏ½Ã°Ú½À´Ï±î?");
		if(result == JOptionPane.OK_OPTION) {
			JOptionPane.showMessageDialog(mainDrive, "·Î±×¾Æ¿ô µÇ¾ú½À´Ï´Ù");
			this.mainDrive.loginFlag = false;
			mainDrive.changePage(4);
		}
	}
	
	public void greetingMsg(String loginUserName) {
		greetingLb.setText(loginUserName + "´Ô, ¹Ý°©½À´Ï´Ù!");
	}
}