
public class Main {
	
	public static void main(String[] args) {
		FileReader filereader = new FileReader("Senior English 2018-2019 - Courses.csv", "Senior English 2018-2019 - Students.csv");
	//	FileReader filereader = new FileReader("Senior English 2016-2017 - Courses.csv", "Senior English 2016-2017 - Students.csv");

	//	Solution solution1 = Solution.randomSolution(filereader.courseList, filereader.studentList, filereader.courseMap);
	//	solution1.evaluate();
	//	solution1.display();
	//	solution1.save("s1");
	
		/*
		//RANDOM SAMPLING
		Solution bestRandom = Solution.randomSampling(1000000, filereader.courseList, filereader.studentList, filereader.courseMap);
		bestRandom.save("randomSolution");
		bestRandom.display();
		*/
		
		/*
		//PURE HILL CLIMBING
		Solution hillClimbSolution = Solution.hillClimbing(solution1);
		hillClimbSolution.display();
		hillClimbSolution.save("hillClimbSolution");
		*/
	
		/*
		//PURE HILL CLIMBING WITH RANDOM RESTARTS
		Solution manyHCSolution = Solution.manyHillClimbing(200, filereader.courseList, filereader.studentList, filereader.courseMap);
		manyHCSolution.display();
		manyHCSolution.save("manyHCSolution");
		*/
		
		/*
		//SIMULATED ANNEALING
		Solution annealingSolution = Solution.simulatedAnnealing(solution1);
		annealingSolution.display();
		annealingSolution.save("annealingSolution");
		*/
		
		//SIMULATED ANNEALING WITH RANDOM RESTART
		
		
		Solution manyASolution = Solution.manyAnnealing(10, filereader.courseList, filereader.studentList, filereader.courseMap);
		manyASolution.display();
		manyASolution.save("manyAnnealingSolution");
	
	}

}
