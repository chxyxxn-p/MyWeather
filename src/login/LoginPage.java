package login;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.MainDrive;
import main.Page;

public class LoginPage extends Page {
		
	JTextField idTf;
	JTextField pwTf;
	JButton loginBt;
	
	String id;
	String pw;
	
	boolean loginCheckFlag;
			
	public LoginPage(MainDrive mainDrive, String title, int width, int height, String bgImgPath, boolean showFlag) {
		
		super(mainDrive, title, width, height, bgImgPath, showFlag);
		
		this.setBackground(Color.red);
		
		idTf = new JTextField("ID");
		pwTf = new JTextField("PW");
		loginBt = new JButton("LOGIN");
		
		idTf.setFont(mainDrive.font);
		pwTf.setFont(mainDrive.font);
		loginBt.setFont(mainDrive.font);
		
		
		idTf.setBackground(Color.WHITE);
		pwTf.setBackground(Color.WHITE);
		loginBt.setBackground(Color.WHITE);
		
		this.setLayout(null);		
		
		idTf.setBounds(500, 100, 500, 100);
		pwTf.setBounds(500, 300, 500, 100);
		loginBt.setBounds(600, 500, 300, 100);
		
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
		if(idTf.getText().equals("ID")&&pwTf.getText().equals("PW")) {		//DB���� �� �ӽ÷� ID, Pw üũ
			loginCheckFlag = true;	//���̵�, ����� DB�� ������
			mainDrive.loginUserName = "cy";	//DB���� �̸� �޾ƿ��� �� �ӽ÷� �̸� ����
		} else {
		loginCheckFlag = false;	//���̵�, ����� DB�� ������
		}
		
//		login ���� �ϸ� mainDrive�� loginFlag = true��, �α׾ƿ��������� ��ȯ
//		login ���� �ϸ� �ٽ� �Է��ϼ��� �߰� idTf, pwTf setText("");
		
		if(loginCheckFlag) {
			JOptionPane.showMessageDialog(mainDrive, "�α��� �Ǿ����ϴ�");
			mainDrive.loginFlag = true;	//�α��� ���� ǥ��
			((LogoutPage)mainDrive.pages[5]).greetingMsg(mainDrive.loginUserName);	//�α׾ƿ� ������ �� �ؽ�Ʈ ����

			mainDrive.changePage(0);	//Ȩ�������� �̵�
			
		} else {
			JOptionPane.showMessageDialog(mainDrive, "���̵�, ��й�ȣ�� �ùٸ��� �ʽ��ϴ�");
			idTf.setText("");
			pwTf.setText("");	//text field ���� �����ֱ�
		}
	}
}