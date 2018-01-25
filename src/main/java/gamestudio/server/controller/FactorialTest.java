package gamestudio.server.controller;

public class FactorialTest {

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//		run(1);
//		run(3);
//		run(5);
//		runFac5();
//
//	}
	
	public static void run(int number) {
		int result = 1;
		
		for (int i=1; i<=number; i++) {
			result *= number;
		}
		
		System.out.println(String.format("%d!=%d", number, result));
	}

	public static void runFac5() {
		int result = 1;
		int number = 5;
		
		for (int i=1; i<=number; i++) {
			result *= number;
		}
		
		System.out.println(String.format("%d!=%d", number, result));
	}

}
