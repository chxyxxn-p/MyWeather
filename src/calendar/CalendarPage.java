package calendar;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.swing.JPanel;

import calendar.todolist.DayPanel;
import calendar.todolist.TodolistPanel;
import main.MainDrive;
import main.Page;

public class CalendarPage extends Page {

	JPanel calendarPanel;
	JPanel[] weekPanels;
	LocalDate today = LocalDate.now();

	JPanel diaryPanel;

	JPanel todolistPanel;
	String selectedDueDate;

	int calendarPanelWidth;
	int calendarPanelHeight;
	int diaryPanelWidth;
	int diaryPanelHeight;
	int todolistPanelWidth;
	int todolistPanelHeight;

	public CalendarPage(MainDrive mainDrive, String title, int width, int height, boolean showFlag) {

		super(mainDrive, title, width, height, showFlag);

		calendarPanelWidth = width;
		calendarPanelHeight = height/10 - 5;
		diaryPanelWidth = width/3 - 5;
		todolistPanelWidth = width/3*2 - 5;
		todolistPanelHeight = diaryPanelHeight = height - calendarPanelHeight - 5;

//		 [calendar]
		setSelectedDueDate(DayPanel.getDueDate(today));
		
		calendarPanel = new JPanel();
		calendarPanel.setBackground(new Color(255, 255, 255, 120));

		weekPanels = new JPanel[7];
		for (int i = 0; i < weekPanels.length; i++) {
			LocalDate day = calculateDay(today.getDayOfMonth() - 3 + i);
			weekPanels[i] = new DayPanel(mainDrive, today, day, calendarPanelWidth/weekPanels.length, calendarPanelHeight);
			
			
			weekPanels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					DayPanel dp = (DayPanel)e.getSource();
					
					for(int i = 0 ; i < weekPanels.length ; i++) {
						weekPanels[i].setBackground(new Color(0,0,0,0));
					}
					dp.setBackground(Color.white);
					selectedDueDate = DayPanel.getDueDate(dp.getDay());
					setTodolistPanel();
				}
			});

			calendarPanel.add(weekPanels[i]);
		}

		calendarPanel.setLayout(new GridLayout(1, weekPanels.length, 0, 0));

//		 [diary]
		diaryPanel = new JPanel();
		diaryPanel.setBackground(new Color(255, 255, 255, 120));
		
//		 [todolist]
		todolistPanel = new JPanel();
		todolistPanel.setBackground(new Color(255, 255, 255, 120));
		todolistPanel.add(new TodolistPanel(mainDrive, selectedDueDate, mainDrive.getLoginUserNo(), todolistPanelWidth, todolistPanelHeight));

		
//		 [CalendarPage]
		this.setLayout(null);

		calendarPanel.setBounds(0, 0, calendarPanelWidth, calendarPanelHeight);
		diaryPanel.setBounds(0, calendarPanelHeight + 10, diaryPanelWidth, diaryPanelHeight);
		todolistPanel.setBounds(diaryPanelWidth + 10, calendarPanelHeight + 10, todolistPanelWidth, todolistPanelHeight);

		this.add(calendarPanel);
		this.add(diaryPanel);
		this.add(todolistPanel);

	}
	
	public LocalDate calculateDay(int day) {
		int year = today.getYear();
		int month = today.getMonthValue();
		
		if (day < 1) {
			month--;

			if (month < 1) {
				month = 12;
				year--;
			}

			day += LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		}

		return LocalDate.of(year, month, day);
	}

	
	public void setTodolistPanel() {

		todolistPanel.removeAll();
		todolistPanel.add(new TodolistPanel(mainDrive, selectedDueDate, mainDrive.getLoginUserNo(), todolistPanelWidth, todolistPanelHeight));
		todolistPanel.updateUI();
	}


	public void setSelectedDueDate(String selectedDueDate) {
		this.selectedDueDate = selectedDueDate;
	}
	
	
	
	
}