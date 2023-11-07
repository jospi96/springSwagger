
public class StupidClass {
	public int multiply(int a, int b) { 
		return a*b;
	}
	
	public int divide(int x, int y) {
	    if (x > 999) {
	      throw new IllegalArgumentException("X should be less than 1000");
	    }
	    return x / y;
	}
	
}
