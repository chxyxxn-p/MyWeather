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
//		DB연결해서 아이디, 비번 같은지 검사
		if(idTf.getText().equals("ID")&&pwTf.getText().equals("PW")) {		//DB연결 전 임시로 ID, Pw 체크
			loginCheckFlag = true;	//아이디, 비번이 DB에 있으면
			mainDrive.loginUserName = "cy";	//DB에서 이름 받아오기 전 임시로 이름 설정
		} else {
		loginCheckFlag = false;	//아이디, 비번이 DB에 없으면
		}
		
//		login 성공 하면 mainDrive의 loginFlag = true로, 로그아웃페이지로 전환
//		login 실패 하면 다시 입력하세요 뜨고 idTf, pwTf setText("");
		
		if(loginCheckFlag) {
			JOptionPane.showMessageDialog(mainDrive, "로그인 되었습니다");
			mainDrive.loginFlag = true;	//로그인 성공 표시
			((LogoutPage)mainDrive.pages[5]).greetingMsg(mainDrive.loginUserName);	//로그아웃 페이지 라벨 텍스트 설정

			mainDrive.changePage(0);	//홈페이지로 이동
			
		} else {
			JOptionPane.showMessageDialog(mainDrive, "아이디, 비밀번호가 올바르지 않습니다");
			idTf.setText("");
			pwTf.setText("");	//text field 내용 지워주기
		}
	}
}