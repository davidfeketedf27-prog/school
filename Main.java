import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Classes.Student;
import Classes.Janitor;
import Classes.Teacher;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Main {
    private static final List<Student> students = new ArrayList<>();
    private static final List<Janitor> janitors = new ArrayList<>();
    private static final List<Teacher> teachers = new ArrayList<>();

    private static JFrame frame;
    private static final Path DATA_BASE = Paths.get("data");

    static void main() {
        // Load initial data from text files
        loadData();

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("High School Platform");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setIconImage(new ImageIcon("icon.png").getImage());
            // Center window on screen
            frame.setLocationRelativeTo(null);
            MenuManager.showMainMenu(frame);
            frame.setVisible(true);
        });
    }



    public static void studentPanel() {
        String name = JOptionPane.showInputDialog(frame, "Enter student name:");
        Student s = findStudentByName(name);
        if (s != null) {
            String msg = s.getGrades().isEmpty()
                    ? s.getName() + " has no grades yet."
                    : s.getName() + "'s grades: " + s.getGrades();
            JOptionPane.showMessageDialog(frame, msg);
        } else {
            JOptionPane.showMessageDialog(frame, "Student not found.");
        }
    }

    public static void workerPanel() {
        String name = JOptionPane.showInputDialog(frame, "Enter janitor name (Chad, Baba, Jaja):");
        for (Janitor j : janitors) {
            if (j.getName().equalsIgnoreCase(name)) {
                // Classes.Janitor.askForSalary() shows its own dialog
                j.askForSalary();
                // persist salary change
                saveJanitors();
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Janitor not found.");
    }

    public static void teacherLogin() {
        String name = JOptionPane.showInputDialog(frame, "Enter teacher name (Lori, Bela, Akos):");
        for (Teacher t : teachers) {
            if (t.getName().equalsIgnoreCase(name)) {
                TeacherPanelManager.showTeacherPanel(t, frame);
                return;
            }
        }
        JOptionPane.showMessageDialog(frame, "Teacher not found.");
    }

    public static Student findStudentByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    // ===== File loading logic =====
    private static void loadData() {
        loadStudents(DATA_BASE.resolve("students.txt"));
        loadJanitors(DATA_BASE.resolve("janitors.txt"));
        loadTeachers(DATA_BASE.resolve("teachers.txt"));
    }

    // students.txt format per line:
    // name|grade1,grade2,grade3
    private static void loadStudents(Path file) {
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String[] parts = trimmed.split("\\|", -1);
                String name = parts[0].trim();
                if (name.isEmpty()) continue;
                Student s = new Student(name);
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    for (String g : parts[1].split(",")) {
                        try {
                            float val = Float.parseFloat(g.trim());
                            if (val > 0 && val <= 10) s.addGrade(val);
                        } catch (NumberFormatException ignored) {}
                    }
                }
                students.add(s);
            }
        } catch (IOException e) {
            showLoadError("students", e);
        }
    }

    // janitors.txt format per line:
    // name|salary
    private static void loadJanitors(Path file) {
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String[] parts = trimmed.split("\\|", -1);
                if (parts.length < 2) continue;
                String name = parts[0].trim();
                String salaryStr = parts[1].trim();
                if (name.isEmpty() || salaryStr.isEmpty()) continue;
                try {
                    float salary = Float.parseFloat(salaryStr);
                    janitors.add(new Janitor(name, salary));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            showLoadError("janitors", e);
        }
    }

    // teachers.txt format per line:
    // name|salary|classroom|subject1,subject2
    private static void loadTeachers(Path file) {
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file)) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) continue;
                String[] parts = trimmed.split("\\|", -1);
                if (parts.length < 2) continue;
                String name = parts[0].trim();
                String salaryStr = parts[1].trim();
                if (name.isEmpty() || salaryStr.isEmpty()) continue;
                try {
                    float salary = Float.parseFloat(salaryStr);
                    Teacher t = new Teacher(name, salary);
                    if (parts.length > 2 && !parts[2].trim().isEmpty()) {
                        t.setClassroom(parts[2].trim());
                    }
                    if (parts.length > 3 && !parts[3].trim().isEmpty()) {
                        for (String s : parts[3].split(",")) {
                            String subj = s.trim();
                            if (!subj.isEmpty()) t.addSubject(subj);
                        }
                    }
                    teachers.add(t);
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            showLoadError("teachers", e);
        }
    }

    private static void showLoadError(String which, Exception e) {
        System.err.println("Failed to load " + which + ": " + e.getMessage());
    }

    // ===== Save logic =====
    public static void saveStudents() {
        Path file = DATA_BASE.resolve("students.txt");
        List<String> lines = new ArrayList<>();
        lines.add("# students.txt");
        lines.add("# Format: name|grade1,grade2,grade3");
        for (Student s : students) {
            StringBuilder sb = new StringBuilder();
            sb.append(s.getName()).append('|');
            List<Float> gs = s.getGrades();
            for (int i = 0; i < gs.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(gs.get(i));
            }
            lines.add(sb.toString());
        }
        try {
            if (!Files.exists(DATA_BASE)) Files.createDirectories(DATA_BASE);
            Files.write(file, lines);
        } catch (IOException e) {
            System.err.println("Failed to save students: " + e.getMessage());
        }
    }

    public static void saveJanitors() {
        Path file = DATA_BASE.resolve("janitors.txt");
        List<String> lines = new ArrayList<>();
        lines.add("# janitors.txt");
        lines.add("# Format: name|salary");
        for (Janitor j : janitors) {
            lines.add(j.getName() + "|" + j.getSalary());
        }
        try {
            if (!Files.exists(DATA_BASE)) Files.createDirectories(DATA_BASE);
            Files.write(file, lines);
        } catch (IOException e) {
            System.err.println("Failed to save janitors: " + e.getMessage());
        }
    }

    public static void saveTeachers() {
        Path file = DATA_BASE.resolve("teachers.txt");
        List<String> lines = new ArrayList<>();
        lines.add("# teachers.txt");
        lines.add("# Format: name|salary|classroom|subject1,subject2");
        for (Teacher t : teachers) {
            StringBuilder sb = new StringBuilder();
            sb.append(t.getName()).append('|')
              .append(t.getSalary()).append('|')
              .append(t.getClassroomRaw() == null ? "" : t.getClassroomRaw()).append('|');
            List<String> subs = t.getSubjects();
            for (int i = 0; i < subs.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(subs.get(i));
            }
            lines.add(sb.toString());
        }
        try {
            if (!Files.exists(DATA_BASE)) Files.createDirectories(DATA_BASE);
            Files.write(file, lines);
        } catch (IOException e) {
            System.err.println("Failed to save teachers: " + e.getMessage());
        }
    }
}
