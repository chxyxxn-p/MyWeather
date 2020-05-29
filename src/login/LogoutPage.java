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

	public LogoutPage(MainDrive mainDrive, String title, int width, int height, String bgImgPath, boolean showFlag) {
		
		super(mainDrive, title, width, height, bgImgPath, showFlag);
		
		this.setBackground(Color.orange);
		
		greetingLb = new JLabel(mainDrive.loginUserName + "��, �ݰ����ϴ�!", JLabel.CENTER);
		logoutBt = new JButton("LOGOUT");
		
		greetingLb.setFont(mainDrive.font);
		logoutBt.setFont(mainDrive.font);
		
		logoutBt.setBackground(Color.white);
		
		
		this.setLayout(null);
		
		greetingLb.setBounds(500, 100, 500, 400);
		logoutBt.setBounds(600, 500, 300, 100);
		
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
		int result = JOptionPane.showConfirmDialog(mainDrive, "�α׾ƿ� �Ͻðڽ��ϱ�?");
		if(result == JOptionPane.OK_OPTION) {
			JOptionPane.showMessageDialog(mainDrive, "�α׾ƿ� �Ǿ����ϴ�");
			this.mainDrive.loginFlag = false;
			mainDrive.changePage(0);
		}
	}
	
	public void greetingMsg(String loginUserName) {
		greetingLb.setText(loginUserName + "��, �ݰ����ϴ�!");
	}
}