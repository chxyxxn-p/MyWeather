package login;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.MainDrive;
import main.Page;

public class LoginPage extends Page {
	
	JLabel idLb;
	JLabel pwLb;
	JTextField idTf;
	JPasswordField pwTf;
	JButton loginBt;
	
	String id;
	String pw;
	
	boolean loginCheckFlag;
			
	public LoginPage(MainDrive mainDrive, String title, int width, int height, String bgImgPath, boolean showFlag) {
		
		super(mainDrive, title, width, height, bgImgPath, showFlag);
		
		this.setBackground(Color.blue);
		
		idLb = new JLabel("ID");
		pwLb = new JLabel("PW");
		idTf = new JTextField();
		pwTf = new JPasswordField();
		loginBt = new JButton("LOGIN");
		
		idLb.setFont(mainDrive.mainFont);
		pwLb.setFont(mainDrive.mainFont);
		idTf.setFont(mainDrive.mainFont);
		pwTf.setFont(mainDrive.mainFont);
		loginBt.setFont(mainDrive.mainFont);
		
		idTf.setBackground(Color.WHITE);
		pwTf.setBackground(Color.WHITE);
		loginBt.setBackground(Color.WHITE);
		
		this.setLayout(null);		
		
		idLb.setBounds(width/2 - width/5, height/8, width/5/2, height/8);
		idTf.setBounds(width/2 - width/5/2, height/8, width/5/2*3, height/8);
		
		pwLb.setBounds(width/2 - width/5, height/8*3, width/5/2, height/8);
		pwTf.setBounds(width/2 - width/5/2, height/8*3, width/5/2*3, height/8);
		
		loginBt.setBounds((width - width/5)/2, height/2 + (height/2-height/8)/2, width/5, height/8);
		
		this.add(idLb);
		this.add(pwLb);
		this.add(idTf);
		this.add(pwTf);
		this.add(loginBt);
		
		loginBt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}
		});
	}
	
	public void login() {
//		DB�����ؼ� ���̵�, ��� ������ �˻�
		connectDatabase();
		
//		login ���� �ϸ� mainDrive�� loginFlag = true��, �α׾ƿ��������� ��ȯ
//		login ���� �ϸ� �ٽ� �Է��ϼ��� �߰� idTf, pwTf setText("");
		
		if(loginCheckFlag) {
			JLabel tempLb = new JLabel("�α��� �Ǿ����ϴ�");
			tempLb.setFont(mainDrive.mainFont);
			JOptionPane.showMessageDialog(mainDrive, tempLb);
			mainDrive.loginFlag = true;	//�α��� ���� ǥ��
			((LogoutPage)mainDrive.pages[5]).greetingMsg(mainDrive.loginUserName);	//�α׾ƿ� ������ �� �ؽ�Ʈ ����

			mainDrive.changePage(5);	//�α׾ƿ��������� �̵�
			
		} else {
			JOptionPane.showMessageDialog(mainDrive, "���̵�, ��й�ȣ�� �ùٸ��� �ʽ��ϴ�");
			idTf.setText("");
			pwTf.setText("");	//text field ���� �����ֱ�
		}
	}
	
	public void connectDatabase() {
		if(idTf.getText().equals("ID") && new String(pwTf.getPassword()).equals("PW")) {		//DB���� �� �ӽ÷� ID, Pw üũ
			loginCheckFlag = true;	//���̵�, ����� DB�� ������
			mainDrive.loginUserName = "�ʿ�";	//DB���� �̸� �޾ƿ��� �� �ӽ÷� �̸� ����
			
		} else {
			loginCheckFlag = false;	//���̵�, ����� DB�� ������
		}
	}
}