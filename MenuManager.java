import javax.swing.*;
import java.awt.*;

public class MenuManager {
    public static void showMainMenu(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.setBackground(new Color(245, 248, 250)); // light background

        JLabel title = new JLabel("=== Highschool Platform ===", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(33, 37, 41));

        JButton studentBtn = new JButton("See Student Grades");
        JButton workerBtn = new JButton("Ask for Salary (Worker)");
        JButton teacherBtn = new JButton("Teacher Panel");
        JButton exitBtn = new JButton("Exit");

        // Button styles
        Color primary = new Color(33, 150, 243);   // blue
        Color success = new Color(40, 167, 69);    // green
        Color warning = new Color(255, 193, 7);    // amber
        Color danger = new Color(220, 53, 69);     // red

        styleButton(studentBtn, primary, Color.WHITE);
        styleButton(workerBtn, success, Color.WHITE);
        styleButton(teacherBtn, warning, Color.BLACK);
        styleButton(exitBtn, danger, Color.WHITE);

        studentBtn.addActionListener(e -> Main.studentPanel());
        workerBtn.addActionListener(e -> Main.workerPanel());
        teacherBtn.addActionListener(e -> Main.teacherLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        panel.add(title);
        panel.add(studentBtn);
        panel.add(workerBtn);
        panel.add(teacherBtn);
        panel.add(exitBtn);

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
