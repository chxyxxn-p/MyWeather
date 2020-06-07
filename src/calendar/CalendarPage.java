package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import main.MainDrive;
import main.Page;

public class CalendarPage extends Page {

	JPanel calendarPanel;
	JPanel[] weekPanels;
	LocalDate today = LocalDate.now();

	JPanel diaryPanel;

	JPanel todolistPanel;

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
		calendarPanel = new JPanel();
		calendarPanel.setBackground(new Color(255, 255, 255, 120));

		weekPanels = new JPanel[7];
		for (int i = 0; i < weekPanels.length; i++) {
			weekPanels[i] = new JPanel();
			LocalDate day = calculateDay(today.getDayOfMonth() - 3 + i);
			
			JTextArea dayTa = new JTextArea(day.getMonthValue()+" / "+day.getDayOfMonth() +" (" + day.getDayOfWeek().toString().substring(0, 3) + ")");
			dayTa.setBackground(new Color(0,0,0,0));
			dayTa.setEditable(false);
			dayTa.setFont(mainDrive.getFont(20));
			
			if(today.getDayOfMonth() == day.getDayOfMonth())
				weekPanels[i].setBackground(Color.white);
			else
				weekPanels[i].setBackground(new Color(0,0,0,0));
			
			weekPanels[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			weekPanels[i].add(dayTa);
			
			weekPanels[i].addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					for(int i = 0 ; i < weekPanels.length ; i++) {
						weekPanels[i].setBackground(new Color(0,0,0,0));
					}
					((JPanel)e.getSource()).setBackground(Color.white);
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

//		 [CalendarPage]
		this.setLayout(null);

		calendarPanel.setBounds(0, 0, calendarPanelWidth, calendarPanelHeight);
		diaryPanel.setBounds(0, calendarPanelHeight + 10, diaryPanelWidth, diaryPanelHeight);
		todolistPanel.setBounds(diaryPanelWidth + 10, calendarPanelHeight + 10, todolistPanelWidth,
				todolistPanelHeight);

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
}