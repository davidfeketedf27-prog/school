import javax.swing.*;
import java.awt.*;
import Classes.Teacher;
import Classes.Student;

public class TeacherPanelManager {
    public static void showTeacherPanel(Teacher teacher, JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(245, 248, 250));

        JLabel title = new JLabel(teacher.getName() + "'s Panel", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(33, 37, 41));

        JButton passFailBtn = new JButton("Check Student Pass/Fail");
        JButton gradeBtn = new JButton("Give Grade to Student");
        JButton salaryBtn = new JButton("Ask for Salary");
        JButton infoBtn = new JButton("My Info");
        JButton backBtn = new JButton("Back");

        // Button styles (match MenuManager palette)
        Color primary = new Color(33, 150, 243);   // blue
        Color success = new Color(40, 167, 69);    // green
        Color warning = new Color(255, 193, 7);    // amber
        Color info = new Color(23, 162, 184);      // teal
        Color secondary = new Color(108, 117, 125);// gray

        styleButton(passFailBtn, primary, Color.WHITE);
        styleButton(gradeBtn, warning, Color.BLACK);
        styleButton(salaryBtn, success, Color.WHITE);
        styleButton(infoBtn, info, Color.WHITE);
        styleButton(backBtn, secondary, Color.WHITE);

        passFailBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter student name:");
            Student s = Main.findStudentByName(name);
            if (s != null) {
                JOptionPane.showMessageDialog(frame, teacher.failOrPassStudent(s));
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.");
            }
        });

        gradeBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter student name:");
            Student s = Main.findStudentByName(name);
            if (s != null) {
                String gStr = JOptionPane.showInputDialog(frame, "Enter grade (1–10):");
                try {
                    float grade = Float.parseFloat(gStr);
                    if (grade > 0 && grade <= 10) {
                        teacher.giveGrade(s, grade);
                        JOptionPane.showMessageDialog(frame, "Grade added!");
                        // persist student grade changes
                        Main.saveStudents();
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid grade. Enter between 1–10.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid number.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Student not found.");
            }
        });

        // Teacher inherits askForSalary() from Janitor, which shows its own dialog
        salaryBtn.addActionListener(e -> {
            teacher.askForSalary();
            // persist salary change (teacher is also a janitor)
            Main.saveJanitors();
            Main.saveTeachers();
        });
        infoBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, teacher.toString()));
        backBtn.addActionListener(e -> MenuManager.showMainMenu(frame));

        panel.add(title);
        panel.add(passFailBtn);
        panel.add(gradeBtn);
        panel.add(salaryBtn);
        panel.add(infoBtn);
        panel.add(backBtn);

        frame.setContentPane(panel);
        frame.revalidate();
    }

    private static void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
    }
}
