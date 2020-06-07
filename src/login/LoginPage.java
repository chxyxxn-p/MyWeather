package login;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import calendar.CalendarPage;
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
			JLabel opLb = new JLabel("�α��� �Ǿ����ϴ�");
			opLb.setFont(mainDrive.getFont(13));
			JOptionPane.showMessageDialog(mainDrive, opLb);
			mainDrive.setLoginFlag(true);	//�α��� ���� ǥ��
			((LogoutPage)mainDrive.getPages()[5]).greetingMsg(mainDrive.getLoginUserName());	//�α׾ƿ� ������ �� �ؽ�Ʈ ����

			mainDrive.changePage(5);	//�α׾ƿ��������� �̵�
			
		} else {
			JLabel opLb = new JLabel("���̵�, ��й�ȣ�� �ùٸ��� �ʽ��ϴ�");
			opLb.setFont(mainDrive.getFont(13));
			JOptionPane.showMessageDialog(mainDrive, opLb);
		}
		
		idTf.setText("");
		pwTf.setText("");	//text field ���� �����ֱ�
	}
	
	public void connectDatabase() {
//		�α��� ��ư ������ -> �α��� �����ͺ��̽� ���� �޼ҵ� ȣ��

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			con = mainDrive.getConnectionManager().getConnection();
			
			String sql = "select * from member";
			
			try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					if(idTf.getText().equals(rs.getString("member_id")) && new String(pwTf.getPassword()).equals(rs.getString("member_passwd"))) {		//DB���� �� �ӽ÷� ID, Pw üũ
						mainDrive.setLoginUserNo(rs.getInt("member_no"));
						mainDrive.setLoginUserName(rs.getString("member_name"));	//DB���� �̸� �޾ƿ��� �� �ӽ÷� �̸� ����
						loginCheckFlag = true;	//���̵�, ����� DB�� ������
						((CalendarPage)mainDrive.getPages()[2]).setTodolistPanel();

						break;
						
					} else {
						loginCheckFlag = false;	//���̵�, ����� DB�� ������
					}
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				mainDrive.getConnectionManager().closeDB(rs);
				mainDrive.getConnectionManager().closeDB(pstmt);
				mainDrive.getConnectionManager().closeDB(con);
			}
			
	}
}