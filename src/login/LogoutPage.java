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

		greetingLb = new JLabel(mainDrive.getLoginUserName() + "´Ô, ¹Ý°©½À´Ï´Ù!", JLabel.CENTER);
		logoutBt = new JButton("LOGOUT");
		
		greetingLb.setFont(mainDrive.getFont(30));
		logoutBt.setFont(mainDrive.getFont(30));
		
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
		JLabel opLb = new JLabel("·Î±×¾Æ¿ô ÇÏ½Ã°Ú½À´Ï±î?");
		opLb.setFont(mainDrive.getFont(13));
		int result = JOptionPane.showConfirmDialog(mainDrive, opLb);
		if(result == JOptionPane.OK_OPTION) {
			JLabel op2Lb = new JLabel("·Î±×¾Æ¿ô µÇ¾ú½À´Ï´Ù");
			op2Lb.setFont(mainDrive.getFont(13));
			JOptionPane.showMessageDialog(mainDrive, op2Lb);
			this.mainDrive.setLoginFlag(false);
			mainDrive.changePage(6);
		}
	}
	
	public void greetingMsg(String loginUserName) {
		greetingLb.setText(loginUserName + "´Ô, ¹Ý°©½À´Ï´Ù!");
	}
}