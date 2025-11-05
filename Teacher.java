package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Janitor implements FailPass{

    private String classroom;
    private final List<String> subjects = new ArrayList<>();

    public Teacher(String name, float salary) {
        super(name, salary);
    }

    public String failOrPassStudent(Student student){
        List<Float> grades = student.getGrades();
        if (grades.isEmpty()) {
            return student.getName() + " has no grades.";
        }

        float sum = 0;
        for (Float g : grades) {
            sum += g;
        }

        float finalGrade = sum / grades.size();
        return (finalGrade < 5) ?
                (name + " failed " + student.getName() + " with grade " + finalGrade) :
                (name + " passed " + student.getName() + " with grade " + finalGrade);
    }

    public void setClassroom(String classroom){
        this.classroom = classroom;
    }
    public String getClassroom(){
        return Objects.requireNonNullElse(this.classroom, "No classroom");
    }

    // Raw classroom for persistence (may be null)
    public String getClassroomRaw(){
        return this.classroom;
    }

    public void addSubject(String subject){
        if (!"X".equals(subject)) {
            subjects.add(subject);
        }
    }

    // Subjects getter for persistence and UI
    public List<String> getSubjects(){
        return subjects;
    }

    public void giveGrade(Student student, float grade){
        student.addGrade(grade);
    }

    public String toString(){
        return this.name + "'s details:" +
                "\n \tClassroom: " + this.getClassroom() +
                "\n \tSubject(s): " + this.subjects +
                "\n \tSalary: " + this.getSalary();
    }
}
