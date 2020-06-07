package calendar.todolist;

import java.awt.Color;
import java.time.LocalDate;

import javax.swing.JPanel;
import javax.swing.JTextField;

import main.MainDrive;

public class DayPanel extends JPanel {
	
	JTextField dayTf;
	LocalDate day;

	public DayPanel(MainDrive mainDrive, LocalDate today, LocalDate day, int width, int height) {
		
		this.day = day;

		dayTf = new JTextField(day.getMonthValue() + " / "
				+ day.getDayOfMonth()
				+ " (" + day.getDayOfWeek().toString().substring(0, 3) + ")");

		dayTf.setBackground(new Color(0, 0, 0, 0));
		dayTf.setEditable(false);
		dayTf.setFont(mainDrive.getFont(20));
		dayTf.setHorizontalAlignment(JTextField.CENTER);

		if (today.getDayOfMonth() == day.getDayOfMonth())
			this.setBackground(Color.white);
		else
			this.setBackground(new Color(0, 0, 0, 0));

		dayTf.setBounds(0, height/4, width, height/2);
		this.setLayout(null);
		
		this.add(dayTf);
	}

	static public String getDueDate(LocalDate day) {
		String year = Integer.toString(day.getYear());
		String month = (day.getMonthValue() < 10 ? "0" : "") + Integer.toString(day.getMonthValue());
		String date = (day.getDayOfMonth() < 10 ? "0" : "") + Integer.toString(day.getDayOfMonth());

		return year+month+date;
	}

	public LocalDate getDay() {
		return day;
	}
	
	
	
}
