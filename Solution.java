
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.HashMap;

public class Solution {
	public ArrayList<Course> solutionCourseL = new ArrayList<Course>();
	public HashMap<String,Course> solutionCourseM = new HashMap<String,Course>();
	public ArrayList<Student> studentList = new ArrayList<Student>();
	public HashMap<String, Student> studentMap = new HashMap<String, Student>();
	public int maxCourseSize;
	public int avCourseSize;
	public int evaluationScore;
	public int numStudentsNeverIn1st;
	public int prefScore;
	public int numStudentsWithSameTeacher;
	public long timeSearching;
	
	
	
	//solution class for hill climbing
	//replicates a Solution, but with new courses and students (in the same place but new objects)
	private Solution(Solution s) {
		this.prefScore = 0;
		this.maxCourseSize = s.maxCourseSize;
		this.avCourseSize = s.avCourseSize;
		this.evaluationScore = 0;
		this.numStudentsNeverIn1st = 0;
		this.numStudentsWithSameTeacher = 0;
		this.timeSearching = 0;
		
		ArrayList<Student> newStudentList = new ArrayList<Student>();
		HashMap<String,Student> studentMap = new HashMap<String,Student>();
		
		//create new student
		for (Student student: s.studentList) {
			Student newS = new Student(student);
			newStudentList.add(newS);
			studentMap.put(newS.name, newS);
		}
		
		//update student list and student map
		this.studentList = newStudentList;
		this.studentMap = studentMap;
		
		
		//make new courses and add new students to new courses
		ArrayList<Course> newCourseList = new ArrayList<Course>();
		for (Course course: s.solutionCourseL) {
			Course newCourse = new Course(course);
			for (Student studentInClass: course.students) {
				newCourse.students.add(studentMap.get(studentInClass.name));	
			}
			newCourseList.add(newCourse);
				
		}
		this.solutionCourseL = newCourseList;
		
		for (Course c: solutionCourseL) {
			this.solutionCourseM.put(c.name, c);
		}
		
		
		//update student preferences using new courses
		for (Student oldStudent : s.studentList) {
			Student newStudent = studentMap.get(oldStudent.name);
			for (Course choice: oldStudent.fallChoices) {
				newStudent.fallChoices.add(solutionCourseM.get(choice.name));
			}
			for (Course choice: oldStudent.springChoices) {
				newStudent.springChoices.add(solutionCourseM.get(choice.name));
			}
		}

	}
	

	private Solution() {
		
	}
	

	/*
	 * "pick the best improvement"
Stop picking neighbors when you reach a solution that can't be improved.
If the solution is the best one found so far, then display statistics about it and save the solution.
Repeat until you get tired of letting the program run.
	 */
	
	public static Solution findBestNeighbor(Solution startingSolution) {

		//the current best solution is the best solution from the last "round" of climbing
		Solution bestSolution = startingSolution;
		
		//for each student, move them into a course they are not in and evaluate it. 
		//if better than best solution, make  the best solution = to the  new solution
		for (Student s: startingSolution.studentList) {
			Course currentFallCourse = s.fallChoices.get(s.fallPref);
			Course currentSpringCourse = s.springChoices.get(s.springPref);

			for (Course c: startingSolution.solutionCourseL) {
				if (c.students.contains(s) != true && c.term.equals("F")) {

					Solution newSolution = new Solution(startingSolution);
					
					Student newStudent = newSolution.studentMap.get(s.name);
					
					newSolution.solutionCourseM.get(c.name).students.add(newStudent);
					
					newSolution.solutionCourseM.get(currentFallCourse.name).students.remove(newStudent);
										
					newStudent.fallPref = newStudent.fallChoices.indexOf(newSolution.solutionCourseM.get(currentFallCourse.name));
					
					
					
					newSolution.evaluate();
					
					if (newSolution.evaluationScore< bestSolution.evaluationScore) {
						bestSolution = newSolution;
	
					}
					
				}
				
		
				if (c.students.contains(s) != true && c.term.equals("S")) {

					Solution newSolution = new Solution(startingSolution);
					
					Student newStudent = newSolution.studentMap.get(s.name);
					
					newSolution.solutionCourseM.get(c.name).students.add(newStudent);
					
					newSolution.solutionCourseM.get(currentSpringCourse.name).students.remove(newStudent);
										
					newStudent.springPref = newStudent.springChoices.indexOf(newSolution.solutionCourseM.get(currentSpringCourse.name));
					
					
					
					newSolution.evaluate();
					
					if (newSolution.evaluationScore< bestSolution.evaluationScore) {
						bestSolution = newSolution;
	
					}
					
				}
				
			}
			
		}
		
		//if there was not any improvement, then return null
		if (bestSolution.equals(startingSolution)) {
			return null;
		}
		
		return bestSolution;
		
	}

	//take starting solution, hill climb until reach a peak, then return solution 
	public static Solution hillClimbing(Solution startingSolution) {
		
		Solution s = startingSolution;
		boolean stillClimbing = true;
		
		
		while (stillClimbing == true) {
			s = findBestNeighbor(startingSolution);
		
			
			if (s == null) {
				stillClimbing = false;
			}
			else {
				startingSolution = s;
			//	System.out.println(s.evaluationScore + "  ");
			}
		}
	//	startingSolution.save("currentBestSolution");
		return startingSolution;
		
	}
	
	// hill climbs i times and returns the best solution!
	public static Solution manyHillClimbing(int numClimbs, ArrayList<Course> courseList, ArrayList<Student> studentList, HashMap<String,Course> courseMap) {
		Solution bestSolution = null;
		long startTime = System.currentTimeMillis();


		for (int i=0; i<numClimbs; i++) {
			
			Solution newRandom = randomSolution(courseList, studentList, courseMap);
			newRandom.evaluate();
			
			Solution newSolution = hillClimbing(newRandom);
			
			
			
			if (bestSolution == null || newSolution.evaluationScore < bestSolution.evaluationScore) {
				bestSolution = newSolution;
				bestSolution.save("currentBestManyHCSolution");
				System.out.println("climb #" + i  +  " new best HC solution! " + bestSolution.evaluationScore);
			}
			if (i%10 == 0) {
				System.out.println("try #" + i  + " " + bestSolution.evaluationScore);
			}
			
			
		}
		
		bestSolution.timeSearching = (System.currentTimeMillis() - startTime)/1000;
		System.out.println("done in  " + (System.currentTimeMillis() - startTime)/1000 + " seconds" );
		return bestSolution;
	}
	
	//runs the annealing function i times
	public static Solution manyAnnealing(int numClimbs, ArrayList<Course> courseList, ArrayList<Student> studentList, HashMap<String,Course> courseMap) {
		Solution bestSolution = null;
		long startTime = System.currentTimeMillis();



		for (int i=0; i<numClimbs; i++) {
			System.out.println(" ------ try # " + (i+1)  + " ------ ");
			
			Solution newRandom = randomSolution(courseList, studentList, courseMap);
			newRandom.evaluate();
			
			Solution newSolution = simulatedAnnealing(newRandom);
			
			if (bestSolution == null || newSolution.evaluationScore < bestSolution.evaluationScore) {
				bestSolution = newSolution;
				System.out.println("new best Simulated Annealing solution! " + bestSolution.evaluationScore);
			}
			
		}
		bestSolution.timeSearching = (System.currentTimeMillis() - startTime)/1000;
		return bestSolution;		
	}
	
	//anneals 1 time
	public static Solution simulatedAnnealing(Solution randomStartingSolution) {
		System.out.println("simulated annealing!!!");
		double temperature = 100;
		Solution s = randomStartingSolution;
		
		int counter = 0;
		while (temperature > 1) {
			if (counter % 500 == 0) {
				System.out.println("Counter: " + counter + "  evaluation score: " + s.evaluationScore + "  temperature: " + temperature);
			}
			s = annealNeighbor(s, temperature);
			temperature = temperature*.999;
			counter += 1;
		}
		
		return s;
		
	}
	
//helper function for annealing so selecting F or S is random
	public static Solution annealNeighbor(Solution startingSolution, double temperature) {
		Random rand = new Random();
		Solution s = null;
		
		if (rand.nextBoolean() == true) {
			s = neighborHelper(startingSolution, "F", temperature);
			s = neighborHelper(startingSolution, "S", temperature);
		}
		
		else {
			s= neighborHelper(startingSolution, "S", temperature);
			s= neighborHelper(startingSolution, "F", temperature);
		}
		
		if (s== null) {
			return startingSolution;
		}
		
		return s;

	}
	
	//function that finds neighbor solution for annealing
	public static Solution neighborHelper(Solution startingSolution, String term, double temperature) {
		Random rand = new Random();

		Collections.shuffle(startingSolution.studentList);

		for (Student s: startingSolution.studentList) {
			
			Course currentCourse = null;
			
			if (term.equals("F")){
				currentCourse = s.fallChoices.get(s.fallPref);
			}
			else {
				currentCourse = s.springChoices.get(s.springPref);

			}
			
			for (Course c: startingSolution.solutionCourseL) {
				if (c.students.contains(s) != true && c.term.equals(term)) {

					Solution newSolution = new Solution(startingSolution);

					Student newStudent = newSolution.studentMap.get(s.name);

					newSolution.solutionCourseM.get(c.name).students.add(newStudent);

					newSolution.solutionCourseM.get(currentCourse.name).students.remove(newStudent);

					if (term.equals("F")) {
						newStudent.fallPref = newStudent.fallChoices.indexOf(newSolution.solutionCourseM.get(currentCourse.name));
					}
					if (term.equals("S")) {
						newStudent.springPref = newStudent.springChoices.indexOf(newSolution.solutionCourseM.get(currentCourse.name));
					}
					


					newSolution.evaluate();

					if (newSolution.evaluationScore< startingSolution.evaluationScore) {
						return newSolution;
					}
					else {
						double prob =  Math.pow(Math.E, (startingSolution.evaluationScore-newSolution.evaluationScore)/temperature);
						if (prob > rand.nextDouble()) {
							return newSolution;

						}
					}

				}


			}

		}

		
		return null;
		
	}
	
	
	
	//creates i random samples and returns the best one
	public static Solution randomSampling(int numSolutions, ArrayList<Course> courseList, ArrayList<Student> studentList, HashMap<String,Course> courseMap) {
		System.out.println(" --- starting random sampling --- ");
		long startTime = System.currentTimeMillis();

		
		Solution bestSolution = null;
		int bestScore = 1000000;
		for (int i=0; i <numSolutions; i++) {
			Solution s = randomSolution(courseList, studentList, courseMap);
			s.evaluate();
			//System.out.println(i + "  " + s.evaluationScore);
			if (s.evaluationScore < bestScore) {
				System.out.println(i + " new best score!! " + s.evaluationScore);
				bestSolution = s;
				bestScore = s.evaluationScore;

			}
			
		}	
		long elapsedTime = (System.currentTimeMillis() - startTime)/1000;

		System.out.println("--- done in " + elapsedTime + " seconds---");

		
		bestSolution.timeSearching = (System.currentTimeMillis() - startTime)/1000;

		return bestSolution;
	
	}
	
	
	
	//makes 1 pseudo random solution
	public static Solution randomSolution(ArrayList<Course> courseList, ArrayList<Student> studentList, HashMap<String,Course> courseMap) {
		long startTime = System.currentTimeMillis();

		
		ArrayList<Student> newStudentList = new ArrayList<Student>();
		for (Student student: studentList) {
			newStudentList.add(new Student(student));
		}
		
		ArrayList<Course> newCourseList = new ArrayList<Course>();
		for (Course course: courseList) {
			newCourseList.add(new Course(course));
		}
		Solution s = new Solution();

		for (Course course : newCourseList) {
			s.solutionCourseL.add(course);
			s.solutionCourseM.put(course.name, course);
		}
		
		for (Student nSt: newStudentList) {
			for (Student oSt: studentList) {
				if (nSt.name.equals(oSt.name)) {
			
					for (Course choice: oSt.fallChoices) {
						nSt.fallChoices.add(s.solutionCourseM.get(choice.name));
					}
					for (Course choice: oSt.springChoices) {
						nSt.springChoices.add(s.solutionCourseM.get(choice.name));
					}
				}
			}
		}
		
		s.evaluationScore = 0;
		
		s.prefScore = 0;
		s.studentList = newStudentList;
		s.numStudentsNeverIn1st = 0;
		s.numStudentsWithSameTeacher = 0;
		
		Collections.shuffle(newStudentList);
		
		int coursesPerTerm = newCourseList.size()/2;
		s.avCourseSize = newStudentList.size()/coursesPerTerm;
		s.maxCourseSize = s.avCourseSize + 2;
		
		for (Student student: s.studentList) {
			s.assignStudentToCourse(student, student.fallChoices, s.maxCourseSize, "F");
			s.assignStudentToCourse(student, student.springChoices, s.maxCourseSize, "S");
			
		}
		s.timeSearching = (System.currentTimeMillis() - startTime)/1000;

	
		return s;
		
	}
	//assign student to course helper function
	//start with first choice, but if full, then second, then third, etc
	//if all of their 5 choices are full, they will be placed in the first found option that is not full
	public void assignStudentToCourse(Student student, ArrayList<Course> choices, int courseSize, String term) {
		boolean a = false;
		for (int i = 0; i<5; i++) {
			Course c = solutionCourseM.get(choices.get(i).name);
			if (c.students.size() < courseSize) {
				c.students.add(student);
				if (term.equals("F")) {
					student.fallPref = i;
				}
				else {
					student.springPref = i;
				}
				a =true;
				break;
			}
		}
		if (a== false) {
			System.out.println("student " + student.name + " not assigned");
		}
	}
	
	
	
	
	
	//evaluation function
	//evaluates solution and updates this.evaluationScore
	public int evaluate() {
		int score = 0;
		
		this.evaluationScore = 0;
		this.numStudentsNeverIn1st = 0;
		this.numStudentsWithSameTeacher = 0;		
		
		
		//evaluate student pref and if student does not get first choice either semester
		for (Student st : studentList) {
			for (Course co: solutionCourseL) {
				if (co.students.contains(st)) {
					if (co.term.equals("F")) {
						st.fallPref = st.fallChoices.indexOf(co);
						if (st.fallPref == -1) {
							System.out.println("uh oh");
						}
					}
					if (co.term.equals("S")) {
						st.springPref = st.springChoices.indexOf(co);
						if (st.springPref == -1) {
							System.out.println("uh oh");
						}
					}
				}
			}
			
			score += Math.pow(st.fallPref*3,  3) + Math.pow(st.springPref*3,  3);

			if (st.fallPref != 0 && st.springPref != 0) {
				score += 400;
				numStudentsNeverIn1st += 1;
			}
		}
		
		int minCourseSize = avCourseSize - 2;		

		
		for (Course c: solutionCourseL) {
			// Minimum and maximum course sizes. penalized if too small or too large
			//having too many is WORSE than having too few
			int classSize = c.students.size();
			if (classSize < minCourseSize) {
				score+=Math.pow((minCourseSize-classSize)*5,3);
			}
			else if (classSize > maxCourseSize) {
				score+=Math.pow((classSize-maxCourseSize)*5,3);

			}
			
			
			//gender diversity
			int fCount = 0;
			for (Student s: c.students) {
				if (s.gender.equals("F")){
					fCount += 1;
				}
			}
			c.genderDiversity = (double)(fCount)/(double)(c.students.size());
			score += Math.pow(Math.abs(.5-c.genderDiversity)*20, 3);
		//	System.out.println(.5-c.genderDiversity);
		//	System.out.println(Math.abs(.5-c.genderDiversity));
		//	System.out.println( Math.pow(Math.abs(.5-c.genderDiversity), 3));
			
			
		//student can't have same teacher both semesters
		
			
		}
		
		for (int i =0; i<5; i++) {
			Course fallC = solutionCourseL.get(i);
			for (int j=5; j<10; j++) {
				Course springC = solutionCourseL.get(j);
				if (fallC.teacher.equals(springC.teacher)) {
					for (Student s: fallC.students) {
						if (springC.students.contains(s)) {
							score += 500;
							numStudentsWithSameTeacher += 1;
						}
					}
				}
			
				
			}
			
		}
		this.evaluationScore = score;
		return score;
	}
	
	
	
	//prints the solution
	public void display() {
		System.out.println();
		System.out.println();

		System.out.println("time spent searching: " + timeSearching);
		System.out.println("the evaluation score is: " + evaluationScore);
		
		System.out.println(numStudentsWithSameTeacher + " students have the same teacher both terms");
		
		
		System.out.println(numStudentsNeverIn1st + " students never in 1st choice");
		
		System.out.println();
		
		for (Course c: solutionCourseL) {
			System.out.println(c.name + ", term:" + c.term + ", students: " + c.students.size() + ", %female: " + 100*c.genderDiversity);
			for (Student s: c.students) {
				System.out.print("    " + s.gender + " " + s.name);
				if (c.term.equals("F")){
					System.out.println(", #" + (s.fallPref+1) + " choice");	
				}
				else {
					System.out.println(", #" + (s.springPref+1) + " choice");	
				}
			}
			System.out.println();
			
		}
		
		int[][] preferenceMatrix = createMatrix();
		
		for (int l = 0; l<5; l++) {
			for (int w=0; w<5; w++) {
				System.out.print(preferenceMatrix[l][w] + "  ");
			}
			System.out.println();
		}
		
		
		
	}
	
	//matrix helper function
	public int[][] createMatrix(){
		int[][]preferences = new int[5][5];
		for (Student s: studentList) {
			if (s.fallPref ==-1 | s.springPref==-1) {
				System.out.println("-1");
			}
			else {
				preferences[s.fallPref][s.springPref] += 1;
			}	
		}
		return preferences;
		
	}
	
	
	//saves solution to a csv file
	public void save(String fileName) {
		 SimpleFile file = new SimpleFile(fileName + ".csv");
		 file.startWriting();
		 PrintStream stream = file.getPrintStream();
		 
		 stream.println("time spent searching: " + timeSearching);

		 stream.println("evaluation score," + evaluationScore);
		 stream.println("students with the same teacher both terms," + numStudentsWithSameTeacher);
		 stream.println("students never in 1st choice," + numStudentsNeverIn1st);

		 stream.println();
		 stream.println();

		 
		 stream.println(", Fall 1, Fall 2, Fall 3, Fall 4, Fall 5");
		 int[][] preferenceMatrix = createMatrix();
		 
		 
		for (int l = 0; l<5; l++) {
			stream.print("Spring " + (l+1) + ",");
			for (int w=0; w<5; w++) {
				stream.print(preferenceMatrix[l][w] + ",");
			}
			stream.println();
		}
		 stream.println();
		 stream.println();
		 
		 

		int biggestCourseSize = 0;
		stream.print("Course name,");
		for (Course c: solutionCourseL) {
			stream.print(c.term + " " + c.name + ",");
			if (c.students.size() > biggestCourseSize) {
				biggestCourseSize =c.students.size();
			}
		}
		stream.println();

		stream.print("Teacher,");
		for (Course c: solutionCourseL) {
			stream.print(c.teacher + ",");
		}

		stream.println();
		stream.print("Students in class,");
		
		for (Course c: solutionCourseL) {
			stream.print(c.students.size() + ",");
		}
		
		stream.println();
		stream.print("Gender diversity,");
		
		for (Course c: solutionCourseL) {
			stream.print(c.genderDiversity + ",");
		}
		
		
		stream.println();
		stream.println();

		for (int i=0; i<=biggestCourseSize; i++) {
			stream.print(",");
			for (Course c: solutionCourseL) {
				if (i < c.students.size()) {
					stream.print(c.students.get(i).name + ",");
				}
				else {
					stream.print(",");
				}
			}
			stream.println();
			
		}
	}
	
	
}
