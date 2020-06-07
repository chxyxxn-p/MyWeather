package calendar.todolist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableColumn;

import home.HomePage;
import main.MainDrive;

public class TodolistPanel extends JPanel {
	
	
	JPanel registAreaPn;
	JPanel contentPn;

	JLabel titleLb;
	JTextField writeTf; // ���� �Է�
	JButton registBt; // ��� ��ư
	JButton editBt; // ���� ��ư
	JButton deleteBt; // ���� ��ư
	JButton allTodolistBt; // ��� todolist ��ȸ

	JTable table;
	JScrollPane scroll;

	JComboBox<String> statusBox; // todolist�� ���¸� �˷��ִ� �޺��ڽ�

	TodolistModel model;
	AllTodolistFrame allTodolistFrame;

	int todolistNo; // todolist �� ��ȣ
	int row;
	int col;
	
	MainDrive mainDrive;
	String dueDate;
	int memberNo;
	
	int width;
	int height;
	
	int titleLbWidth;
	int titleLbHeight;
	int btWidth;
	int btHeight;
	int writeTfWidth;
	int writeTfHeight;
		
	public TodolistPanel(MainDrive mainDrive, String dueDate, int memberNo, int width, int height) {
		this.mainDrive = mainDrive;
		this.dueDate = dueDate;
		this.memberNo = memberNo;
		this.width = width;
		this.height = height;
		
		btWidth = width/7;
		writeTfHeight = btHeight = btWidth/3;
		writeTfWidth = width - btWidth - 30;
		titleLbWidth = width - 20;
		titleLbHeight = height/3 - writeTfHeight - btHeight - 30;

		registAreaPn = new JPanel();
		contentPn = new JPanel();

		titleLb = new JLabel(dueDate.substring(0,4)+"��"+dueDate.substring(4,6)+"��"+dueDate.substring(6,8)+"��" + "�� �� ��");
		allTodolistBt = new JButton("��ü ���");
		editBt = new JButton("����");
		deleteBt = new JButton("����");
		writeTf = new JTextField(50);
		registBt = new JButton("���");
		table = new JTable(model = new TodolistModel());
		scroll = new JScrollPane(table);

		TableColumn comm = table.getColumnModel().getColumn(1);
		statusBox = new JComboBox<String>();
		statusBox.addItem("�� ��");
		statusBox.addItem("���� ��");
		statusBox.addItem("�Ϸ�");
		comm.setCellEditor(new DefaultCellEditor(statusBox));
		
		titleLb.setFont(mainDrive.getFont(23));
		editBt.setFont(mainDrive.getFont(15));
		deleteBt.setFont(mainDrive.getFont(15));
		allTodolistBt.setFont(mainDrive.getFont(15));
		writeTf.setFont(mainDrive.getFont(15));
		registBt.setFont(mainDrive.getFont(15));
		table.setFont(mainDrive.getFont(10));
		statusBox.setFont(mainDrive.getFont(10));

		titleLb.setHorizontalAlignment(JLabel.CENTER);
		table.setRowHeight(30);
		
		this.setBackground(new Color(0,0,0,0));
		registAreaPn.setBackground(new Color(0,0,0,0));
		contentPn.setBackground(new Color(0,0,0,0));
		table.setBackground(Color.WHITE);
		editBt.setBackground(Color.WHITE);
		deleteBt.setBackground(Color.WHITE);
		allTodolistBt.setBackground(Color.WHITE);
		registBt.setBackground(Color.WHITE);
		
		registAreaPn.setPreferredSize(new Dimension(width, height/3));
		contentPn.setPreferredSize(new Dimension(width, height/3*2));
		
		titleLb.setBounds(10, 10, titleLbWidth, titleLbHeight);
		
		allTodolistBt.setBounds(10, height/3-20-writeTfHeight-btHeight, btWidth, btHeight);
		
		editBt.setBounds(width-20-btWidth*2, height/3-20-writeTfHeight-btHeight, btWidth, btHeight);
		deleteBt.setBounds(width-10-btWidth, height/3-20-writeTfHeight-btHeight, btWidth, btHeight);
		
		writeTf.setBounds(10, height/3-10-writeTfHeight, writeTfWidth, writeTfHeight);
		
		registBt.setBounds(width-10-btWidth, height/3-10-btHeight, btWidth, btHeight);
		
		registAreaPn.setLayout(null);
		
		registAreaPn.add(titleLb);
		registAreaPn.add(allTodolistBt);
		registAreaPn.add(editBt);
		registAreaPn.add(deleteBt);
		registAreaPn.add(writeTf);
		registAreaPn.add(registBt);
		
		table.setPreferredSize(new Dimension(width, height/3*2));
		scroll.setPreferredSize(new Dimension(width, height/3*2));
		
		table.getColumnModel().getColumn(0).setPreferredWidth(20);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(50);

		contentPn.setLayout(new BorderLayout());
		contentPn.add(scroll);

		setLayout(new BorderLayout());
		add(registAreaPn, BorderLayout.NORTH);
		add(contentPn);
		
		load();
		
	
		// todolist ���
		registBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (writeTf.getText().equals("")) {
					JLabel opLb = new JLabel("������ �Է����ּ���");
					opLb.setFont(mainDrive.getFont(13));
					JOptionPane.showMessageDialog(TodolistPanel.this, opLb);
				} else {
					Thread insertThread = new Thread() {
						@Override
						public void run() {
							insert();
						}
					};
					insertThread.start();
				}
			}
		});

		// todolist ����
		editBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread editThread = new Thread() {
					@Override
					public void run() {
						edit();
					}
				};
				editThread.start();
			}
		});

		// todolist ����
		deleteBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread deleteThread = new Thread() {
					@Override
					public void run() {
						delete();
					}
				};
				deleteThread.start();
			}
		});

		// ���� ��� todolist ��ȸ
		allTodolistBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allTodolistFrame = new AllTodolistFrame(mainDrive, dueDate, memberNo, width, height);
			}
		});

		// table�� ���콺������ ����
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				row = table.getSelectedRow();
				col = table.getSelectedColumn();
				String value = (String) table.getValueAt(row, 0);
				todolistNo = Integer.parseInt(value);// Ŭ���ø��� ������ todolist_no�� ������
			}
		});
	}
	
	public void load() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select todolist.todolist_no, status.status");
		sql.append(", todolist.content, todolist.duedate");
		sql.append(" from todolist, status");
		sql.append(" where todolist.status_no=status.status_no");
		sql.append(" and todolist.member_no=?");
		sql.append(" and todolist.duedate=?");
		
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, memberNo);
			pstmt.setString(2, dueDate);
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

				list.add(todolist); // ����Ʈ�� �μ� �� �� ���
			}
			model.list = list;
			table.updateUI();
			
			if(dueDate.equals(DayPanel.getDueDate(LocalDate.now()))) {
				((HomePage)mainDrive.getPages()[0]).getModel().list = list;
				((HomePage)mainDrive.getPages()[0]).getTable().updateUI();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(rs);
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}
	
	public void insert() {
		Connection con = null;
		PreparedStatement pstmt = null;
		
		con = mainDrive.getConnectionManager().getConnection();
		
		String sql = "insert into todolist(todolist_no, member_no, status_no, content, duedate)"
						+ " values(seq_todolist.nextval,?,?,?,?)";		
		try {
			pstmt = con.prepareStatement(sql);
			
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, memberNo);
			pstmt.setInt(2, 1); // todolist ���´� "����"

			// �ؽ�Ʈ�ʵ尡 ����ִµ� ����Ϸ��� ��� ��â ����
			pstmt.setString(3, writeTf.getText());
			
			pstmt.setString(4, dueDate);

			int result = pstmt.executeUpdate();
			con.commit(); // Ŀ��
			
			if (result == 0) {
				JLabel opLb = new JLabel("��� ����");
				opLb.setFont(mainDrive.getFont(13));
				JOptionPane.showMessageDialog(this, opLb);
			} else {
				JLabel opLb = new JLabel("��� ����");
				opLb.setFont(mainDrive.getFont(13));
				JOptionPane.showMessageDialog(this, opLb);
				
				load();
			}
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			mainDrive.getConnectionManager().closeDB(pstmt);
			mainDrive.getConnectionManager().closeDB(con);
		}
	}
	
	public void edit() {

		if (todolistNo == 0) {
			JLabel opLb = new JLabel("�����Ͻ� ���ڵ带 �����ϼ���");
			opLb.setFont(mainDrive.getFont(13));
			JOptionPane.showMessageDialog(this, opLb);
			return;
		} else {
			JLabel opLb = new JLabel("�����Ͻðڽ��ϱ�?");
			opLb.setFont(mainDrive.getFont(13));
			int result = JOptionPane.showConfirmDialog(this, opLb);
			if (result == JOptionPane.OK_OPTION) {
				System.out.println("Todolist\tedit");

				Connection con = null;
				PreparedStatement pstmt = null;

				con = mainDrive.getConnectionManager().getConnection();

				String sql = "update todolist set status_no=(select status_no from status where status.status=?), content=?, duedate=? where todolist_no=" + todolistNo;

				int success = 0;
				
				try {
					con.setAutoCommit(false);
					// TodoListModel todoListModel=(TodoListModel)model;
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, (String) table.getValueAt(row, 1));
					pstmt.setString(2, (String) table.getValueAt(row, 2));
					pstmt.setString(3, (String) table.getValueAt(row, 3));
					success = pstmt.executeUpdate();// DML(insert,delete,update)
					con.commit();
					if (success == 0) {
						JLabel op2Lb = new JLabel("���� ����");
						op2Lb.setFont(mainDrive.getFont(13));
						JOptionPane.showMessageDialog(this, op2Lb);
					} else {
						JLabel op3Lb = new JLabel("���� ����");
						op3Lb.setFont(mainDrive.getFont(13));
						JOptionPane.showMessageDialog(this, op3Lb);
						
						load();
					}

				} catch (SQLException e) {
					try {
						con.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} finally {
					mainDrive.getConnectionManager().closeDB(pstmt);
					mainDrive.getConnectionManager().closeDB(con);
				}
			}
		}
	}

	public void delete() {
		if (todolistNo == 0) {
			JLabel opLb = new JLabel("�����Ͻ� ���ڵ带 �����ϼ���");
			opLb.setFont(mainDrive.getFont(13));
			JOptionPane.showMessageDialog(this, opLb);
			return;
		} else {
			JLabel opLb = new JLabel("�����Ͻðڽ��ϱ�?");
			opLb.setFont(mainDrive.getFont(13));
			int result = JOptionPane.showConfirmDialog(this, opLb);
			if (result == JOptionPane.OK_OPTION) {
				System.out.println("Todolist\tdelete");

				Connection con = null;
				PreparedStatement pstmt = null;

				con = mainDrive.getConnectionManager().getConnection();

				String sql = "delete from todolist where todolist_no=" + todolistNo;

				int success = 0;
				
				try {
					con.setAutoCommit(false);
					pstmt = con.prepareStatement(sql);
					success = pstmt.executeUpdate();
					con.commit();
					if (success == 0) {
						JLabel op2Lb = new JLabel("���� ����");
						op2Lb.setFont(mainDrive.getFont(13));
						JOptionPane.showMessageDialog(this, op2Lb);
					} else {
						JLabel op3Lb = new JLabel("���� ����");
						op3Lb.setFont(mainDrive.getFont(13));
						JOptionPane.showMessageDialog(this, op3Lb);
						
						load();
					}

				} catch (SQLException e) {
					try {
						con.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				} finally {
					mainDrive.getConnectionManager().closeDB(pstmt);
					mainDrive.getConnectionManager().closeDB(con);
				}
			}
		}
	}

	public JButton getAllTodolistBt() {
		return allTodolistBt;
	}

	public JTable getTable() {
		return table;
	}
	
	
	
}
