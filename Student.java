import java.util.ArrayList;

public class Student {
	public String name;
	public String gender;
	public ArrayList<Course> fallChoices;
	public ArrayList<Course> springChoices;
	public int springPref;
	public int fallPref;
	
	
	public Student(Student s) {
		this.name = s.name;
		this.gender = s.gender;
		this.springPref = -1;
		this.fallPref =-1;
		this.fallChoices = new ArrayList<Course>();
		this.springChoices = new ArrayList<Course>();


	}
	
	
	public Student(String name, String gender, ArrayList<Course> fallChoices, ArrayList<Course> springChoices) {
		this.name = name;
		this.gender = gender;
		this.fallChoices = fallChoices;
		this.springChoices = springChoices;
		this.fallPref = -1;
		this.springPref =-1;
	}
}
