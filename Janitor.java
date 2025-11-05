package Classes;
import javax.swing.*;
import java.lang.*;
import java.util.concurrent.ThreadLocalRandom;

public class Janitor implements AskSalary{

    String name;
    float salary;

    public Janitor(String name, float salary){
        this.name = name;
        this.salary = salary;
    }

    @Override
    public void askForSalary(){
        int randomNumber = ThreadLocalRandom.current().nextInt(1, 101); // 1–100
        boolean answer;

        answer = randomNumber <= 50;

        //System.out.print(this.name + "'s salary was " + this.salary + ". And was");

        float oldSalary = this.salary;
        if(answer){

            this.salary += 50;
            JOptionPane.showMessageDialog(null, this.name + "'s salary was " + oldSalary + ". And was Increased... \n Current salary is " + this.salary);
        }
        else{
            this.salary -= 50;
            JOptionPane.showMessageDialog(null, this.name + "'s salary was " + oldSalary + ". And was Decreased... \n Current salary is " + this.salary);
        }

    }
    public float getSalary(){
        return this.salary;
    }

    public String getName(){
        return this.name;
    }
}
