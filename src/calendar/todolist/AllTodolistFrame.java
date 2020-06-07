package calendar.todolist;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import main.MainDrive;

public class AllTodolistFrame extends JFrame {
	
	TodolistPanel panel;
	
	public AllTodolistFrame(MainDrive mainDrive, String dueDate, int memberNo, int width, int height) {
		
		panel = new TodolistPanel(mainDrive, dueDate, memberNo, width, height);
		
		panel.remove(panel.getAllTodolistBt());
		
		loadAll();
		
		add(panel);
		setBackground(Color.white);
		setSize(width, height);
		setVisible(true);
	}
	
	public void loadAll() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = panel.mainDrive.getConnectionManager().getConnection();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select todolist.todolist_no, status.status");
		sql.append(", todolist.content, todolist.duedate");
		sql.append(" from todolist, status");
		sql.append(" where todolist.status_no=status.status_no");
		sql.append(" and todolist.member_no=?");
		sql.append(" order by todolist.todolist_no asc");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, panel.memberNo);
			rs = pstmt.executeQuery();
			
			List list = new ArrayList();
			
			while(rs.next()) {
				StatusTable statusTable = new StatusTable();
				Todolist todolist = new Todolist();

				todolist.setStatusTable(statusTable);

				todolist.setTodolistNo(rs.getInt("todolist_no"));
				statusTable.setStatus(rs.getString("status"));
				todolist.setContent(rs.getString("content"));
				todolist.setDuedate(rs.getString("duedate"));

				list.add(todolist); // 리스트에 부서 한 개 담기
			}
			panel.model.list = list;
			panel.table.updateUI();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			panel.mainDrive.getConnectionManager().closeDB(rs);
			panel.mainDrive.getConnectionManager().closeDB(pstmt);
			panel.mainDrive.getConnectionManager().closeDB(con);
		}
	}
	
	

}
