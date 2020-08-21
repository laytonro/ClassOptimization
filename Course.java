import java.util.ArrayList;

public class Course {
	public String name;
	public String term; 
	public String teacher;
	public ArrayList<Student> students;
	public double genderDiversity;
	
	
	public Course(Course c) {
		this.name = c.name;
		this.term = c.term;
		this.teacher = c.teacher;
		this.students =  new ArrayList<Student>();
		this.genderDiversity = 0;
		
	}
	
	public Course(String name, String term, String teacher) {
		this.name = name;
		this.term = term;
		this.teacher = teacher;
		ArrayList<Student> students = new ArrayList<Student>();
		this.students = students;
		this.genderDiversity = 0;
		
	}
	
	


}
