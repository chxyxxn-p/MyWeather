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

import home.HomePage;
import main.MainDrive;
import main.Page;
import recommend.Recommend;
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
//		DB연결해서 아이디, 비번 같은지 검사
		connectDatabase();
		
//		login 성공 하면 mainDrive의 loginFlag = true로, 로그아웃페이지로 전환
//		login 실패 하면 다시 입력하세요 뜨고 idTf, pwTf setText("");
		
		if(loginCheckFlag) {
			JLabel tempLb = new JLabel("로그인 되었습니다");
			tempLb.setFont(mainDrive.getFont(10));
			JOptionPane.showMessageDialog(mainDrive, tempLb);
			mainDrive.setLoginFlag(true);	//로그인 성공 표시
			((LogoutPage)mainDrive.getPages()[5]).greetingMsg(mainDrive.getLoginUserName());	//로그아웃 페이지 라벨 텍스트 설정

			mainDrive.changePage(5);	//로그아웃페이지로 이동
			
		} else {
			JOptionPane.showMessageDialog(mainDrive, "아이디, 비밀번호가 올바르지 않습니다");
		}
		
		idTf.setText("");
		pwTf.setText("");	//text field 내용 지워주기
	}
	
	public void connectDatabase() {
//		로그인 버튼 누르면 -> 로그인 데이터베이스 연동 메소드 호출

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			con = mainDrive.getConnectionManager().getConnection();
			
			String sql = "select * from member";
			
			try {
				pstmt = con.prepareStatement(sql);
				rs = pstmt.executeQuery();
				
				while(rs.next()) {
					
					if(idTf.getText().equals(rs.getString("member_id")) && new String(pwTf.getPassword()).equals(rs.getString("member_passwd"))) {		//DB연결 전 임시로 ID, Pw 체크
						mainDrive.setLoginUserName(rs.getString("member_name"));	//DB에서 이름 받아오기 전 임시로 이름 설정
						
						loginCheckFlag = true;	//아이디, 비번이 DB에 있으면
						break;
						
					} else {
						loginCheckFlag = false;	//아이디, 비번이 DB에 없으면
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