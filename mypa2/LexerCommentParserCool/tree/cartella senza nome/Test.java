package tree;

public class Test {
	public static boolean ParentMatch(String X, int n) {
		char[] chA = X.toCharArray();
		char open = chA[0];
		char close = chA[0];
		ArrayStack<Character> A = new ArrayStack<Character>();

		for (int i = 0; i < chA.length; i++) {
			if (chA[i] == '(' || chA[i] == '[' || chA[i] == '{') 
				A.push(chA[i]);
			else if (chA[i] == ')' || chA[i] == ']' || chA[i] == '}') {
				if (A.isEmpty())
					return false;
				close = chA[i]; //System.out.println("close="+close);
				open = A.pop();
				if ((open == '(' && close != ')') || (open == '[' && close != ']') || (open == '{'&& close != '}'))
					return false;
			}	
		}
		if (A.isEmpty())
			return true;
		else
			return false;
	} 
/**
  * Test our program by performing a series of operations on stacks,
  * printing the operations performed, the returned elements and the
  * contents of the stack involved, after each operation.
  */
  public static void main(String[] args) {
	String s0 = "({hilo}kkkk)[jio]";
	String s1 = "({hilo}kkkk)[jio(kop0])"; 
	System.out.println(ParentMatch(s0, s0.length()));
    System.out.println(ParentMatch(s1, s1.length()));
   /**Object o;
    ArrayStack<Integer> A = new ArrayStack<Integer>();
    A.status("new ArrayStack<Integer> A", null);
    A.push(7);
    A.status("A.push(7)", null);
    o = A.pop();
    A.status("A.pop()", o);
    A.push(9);
    A.status("A.push(9)", null);
    o = A.pop();
    A.status("A.pop()", o);
    ArrayStack<String> B = new ArrayStack<String>();
    B.status("new ArrayStack<String> B", null);
    B.push("Bob");
    B.status("B.push(\"Bob\")", null);
    B.push("Alice");
    B.status("B.push(\"Alice\")", null);
    o = B.pop();
    B.status("B.pop()", o);
    B.push("Eve");
    B.status("B.push(\"Eve\")", null);*/
	

  }
}
