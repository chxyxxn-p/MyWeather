package login;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import home.HomePage;
import main.MainDrive;
import main.Page;
import recommend.RecommendPage;

public class LoginPage extends Page {
	
	JLabel idLb;
	JLabel pwLb;
	JTextField idTf;
	JPasswordField pwTf;
	JButton loginBt;
	
	String id;
	String pw;
	
	boolean loginCheckFlag;
			
	public LoginPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {
		
		super(mainDrive, title, width, height, showFlag);
		
		idLb = new JLabel("ID");
		pwLb = new JLabel("PW");
		idTf = new JTextField();
		pwTf = new JPasswordField();
		loginBt = new JButton("LOGIN");
		
		idLb.setFont(mainDrive.getFont(30));
		pwLb.setFont(mainDrive.getFont(30));
		idTf.setFont(mainDrive.getFont(30));
		pwTf.setFont(mainDrive.getFont(30));
		loginBt.setFont(mainDrive.getFont(30));
		
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
			tempLb.setFont(mainDrive.getFont(10));
			JOptionPane.showMessageDialog(mainDrive, tempLb);
			mainDrive.setLoginFlag(true);	//�α��� ���� ǥ��
			((LogoutPage)mainDrive.getPages()[5]).greetingMsg(mainDrive.getLoginUserName());	//�α׾ƿ� ������ �� �ؽ�Ʈ ����

			mainDrive.changePage(5);	//�α׾ƿ��������� �̵�
			
		} else {
			JOptionPane.showMessageDialog(mainDrive, "���̵�, ��й�ȣ�� �ùٸ��� �ʽ��ϴ�");
		}
		
		idTf.setText("");
		pwTf.setText("");	//text field ���� �����ֱ�
	}
	
	public void connectDatabase() {
//		�α��� ��ư ������ -> �α��� �����ͺ��̽� ���� �޼ҵ� ȣ��
		if(idTf.getText().equals("ID") && new String(pwTf.getPassword()).equals("PW")) {		//DB���� �� �ӽ÷� ID, Pw üũ
			loginCheckFlag = true;	//���̵�, ����� DB�� ������
			mainDrive.setLoginUserName("�ʿ�");	//DB���� �̸� �޾ƿ��� �� �ӽ÷� �̸� ����
			
			Thread t = new Thread() {
				public void run() {
//			�α��εǸ� -> ��õ �����ͺ��̽� ���� �޼ҵ� ȣ�� => API ȣ�� �ķ� ����(���� ������ �°� ����ϱ⶧���� ���� ������ �˾ƾ� ��)
//					((RecommendPage)mainDrive.getPages()[4]).connectDatabase(null, null, null);
					
////			�ٲ� �̸����� Ȩ�������� recommnedLabel ���� ���� 
//					HomePage hp = ((HomePage)mainDrive.getPages()[0]);
//					hp.getRecommendLabel().setText(mainDrive.getLoginUserName()+"�� �ȳ��ϼ���?");
//					hp.repaint();
				}
			};
			
			t.start();
		} else {
			loginCheckFlag = false;	//���̵�, ����� DB�� ������
		}
	}
}