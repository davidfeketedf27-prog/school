package Classes;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private final String name;
    private final List<Float> grades = new ArrayList<>();

    public Student(String name){
        this.name = name;
    }

//    public void showGrades(){
//        if (grades.isEmpty()) {
//            System.out.println(name + " has no grades.");
//        } else {
//            System.out.println(name + "'s grades: " + grades);
//        }
//    }

    public String getName(){
        return this.name;
    }

    public void addGrade(float grade) {
        if (grade > 0 && grade <= 10) {
            grades.add(grade);
        }
    }

    public List<Float> getGrades() {
        return grades;
    }
}
