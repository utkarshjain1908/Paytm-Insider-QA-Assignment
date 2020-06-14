package assignment;

public class PrimeNumbersAssignment {

	public static void main(String[] args) {
		int count = 1, mult = 0;

		for(int i = 2; count <= 100; i++) {
			for(int j = 1; j < i; j++) {
				if(i%j==0) {
					mult++;
				}
			}
			if(mult<=1) {
				System.out.println(i+"\n");
				count++;
			}
			mult=0;
		}

	}

}
