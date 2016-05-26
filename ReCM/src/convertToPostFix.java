import java.io.IOException;
import java.util.Stack;

public class convertToPostFix {
   private Stack st;
   private String input;
   public static char[] postfixArray = new char[50];
   public static String toPost(String str){
		final Stack<Character> stack = new Stack<Character>();
		int size = str.length();
		char[] inputArray = str.toCharArray();
		// post fix conversion
		int i = 0, j = 0, count = 0;
		System.out.println(inputArray);
		int counter = size;
		while (counter != 0) {
			if (inputArray[i] == '+' || inputArray[i] == '|') {
				stack.push(inputArray[i]);
				count++;
	
			} else if (inputArray[i] == ')') {
				postfixArray[j] = stack.pop();
				count--;
				j++;
			} else if (inputArray[i] == '(') {
			} else {
				char a = inputArray[i];
				postfixArray[j] = a;
				j++;
			}
			i++;
			counter--;
		}
		while (count != 0) {
			postfixArray[j] = stack.pop();
			j++;
			count--;
		}
		// to display post fix
		i = 0;
		String result = new String();
		while (postfixArray[i] != 0) {
			result+=postfixArray[i];
			i++;
		}
		return result;
}
}