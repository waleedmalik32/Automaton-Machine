import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

public class ParseToNfa {

	private Stack operatorStack = new Stack(); // Operator Stack //opStack
	private Stack literalsStack = new Stack(); // Input Characters Stack
	public TreeSet treeSetInput = new TreeSet(); // All input Tree
	private int NextState = 0; // nNextStateIndex

	public ArrayList regexToNFA(String regex) {

		boolean bPrevTokenConcat = false;

		operatorStack = new Stack();
		literalsStack = new Stack();
		treeSetInput = new TreeSet();
		NextState = 0;

		for (int i = 0; i < regex.length(); ++i) {

			char currentChar = regex.charAt(i);

			if (checkChar(currentChar) == 4) { // Char is a literal(input char)
				if (bPrevTokenConcat) {
					if (operatorStack.empty()) {
						operatorStack.push(new Character('+'));
					} else {
						Character ck = (Character) operatorStack.peek();

						while (compareOperatorPrecedence('+', ck.charValue()) <= 0) {
							opEval();
							if (operatorStack.empty())
								break;
							ck = (Character) operatorStack.peek();
						}
						operatorStack.push(new Character('+'));
					}
				}
				inStackPush(Character.toString(currentChar));
				bPrevTokenConcat = true;

			} else if (operatorStack.empty()) {
				if (bPrevTokenConcat) {
					if (currentChar == '(')
						operatorStack.push(new Character('+'));
				}

				if ((currentChar == '*') || (currentChar == ')'))
					bPrevTokenConcat = true;
				else
					bPrevTokenConcat = false;
				operatorStack.push(new Character(currentChar));

			} else if (checkChar(currentChar) == 3) { // Left bracket
				if (bPrevTokenConcat) {
					Character ck = (Character) operatorStack.peek();

					while (compareOperatorPrecedence('+', ck.charValue()) <= 0) {

						opEval();

						if (operatorStack.empty())
							break;
						ck = (Character) operatorStack.peek();
					}

					operatorStack.push(new Character('+'));
				}

				operatorStack.push(new Character(currentChar));

				bPrevTokenConcat = false;
			} else if (checkChar(currentChar) == 2) {  // Right bracket

				Character ck = (Character) operatorStack.peek();
				while (checkChar(ck.charValue()) != 3) {
					opEval();
					if (operatorStack.empty())
						break;
					ck = (Character) operatorStack.peek();
				}

				operatorStack.pop();

				bPrevTokenConcat = true;
			} 
			else {
				Character ck = (Character) operatorStack.peek();

				while (compareOperatorPrecedence(currentChar,ck.charValue()) <= 0) {
					opEval();
					if (operatorStack.empty())
						break;
					
					ck = (Character) operatorStack.peek();
				}
				operatorStack.push(new Character(currentChar));

				if (currentChar == '*')
					bPrevTokenConcat = true;
				else
					bPrevTokenConcat = false;
			}
		}
		// Evaluate the rest of operators in the order they are popped
		while (!operatorStack.empty())
			opEval();

		return (ArrayList) literalsStack.pop();
	}

	private int checkChar(char ch) {
		if ((ch == '*') || (ch == '|')) {
			return 1;
		} else if ((ch == ')')) {
			return 2;
		} else if ((ch == '(')) {
			return 3;
		} else {
			return 4;
		}
	}

	private int compareOperatorPrecedence(char opFirst, char opSecond) {
		//If both values are equal
		if (opFirst == opSecond)
			return 0;

		// ")" operator has also highest precedence
		// This comparison will only happen with "+" - concatenation
		if (opFirst == ')')
			return 1;
		if (opSecond == ')')
			return -1;

		// "*" operator is after paranthesis
		if (opFirst == '*')
			return 1;
		if (opSecond == '*')
			return -1;

		// Ok ... it is not equal and not "*"
		if (opFirst == '+')
			return 1;
		if (opSecond == '+')
			return -1;

		// "(" operator has lowest precedence
		if (opFirst == '|')
			return 1;
		else
			return -1;
	}

	private void opEval() {

		Character ck = (Character) operatorStack.pop();
		System.out.println("Eval " + ck.toString());
		switch (ck.charValue()) {
		case '*':
			evalStar();
			break;
		case '|':
			evalUnion();
			break;
		case '+':
			evalConcat();
			break;
		}
	}

	private void evalStar() {

		ArrayList expOperand = (ArrayList) literalsStack.pop();

		State stateFirst = new State("s" + Integer.toString(NextState));
		++NextState;
		State stateSecond = new State("s" + Integer.toString(NextState));
		++NextState;

		Transition eT1 = new Transition("epsilon");
		eT1.stateArray.add(expOperand.get(0));
		eT1.stateArray.add(stateSecond);
		stateFirst.arrayTransitions.add(eT1);

		Transition eT2 = new Transition("epsilon");
		eT2.stateArray.add(stateSecond);
		((State) expOperand.get(expOperand.size() - 1)).arrayTransitions.add(eT2);

		Transition eT3 = new Transition("epsilon");
		eT3.stateArray.add(expOperand.get(0));
		((State) expOperand.get(expOperand.size() - 1)).arrayTransitions.add(eT3);

		ArrayList newExp = new ArrayList();
		newExp.add(stateFirst);
		newExp.addAll(expOperand);
		newExp.add(stateSecond);

		literalsStack.push(newExp);

	}

	private void evalUnion() {

		ArrayList expSecond = (ArrayList) literalsStack.pop();
		ArrayList expFirst = (ArrayList) literalsStack.pop();

		
		State stateFirst = new State("s" + Integer.toString(NextState));

		++NextState;
		State stateSecond = new State("s" + Integer.toString(NextState));

		++NextState;

		Transition eT1 = new Transition("epsilon");
		eT1.stateArray.add(expFirst.get(0));
		Transition eT2 = new Transition("epsilon");
		eT2.stateArray.add(expSecond.get(0));

		stateFirst.arrayTransitions.add(eT1);
		stateFirst.arrayTransitions.add(eT2);

		Transition eT3 = new Transition("epsilon");
		eT3.stateArray.add(stateSecond);
		Transition eT4 = new Transition("epsilon");
		eT4.stateArray.add(stateSecond);
		((State) expFirst.get(expFirst.size() - 1)).arrayTransitions.add(eT3);
		((State) expSecond.get(expSecond.size() - 1)).arrayTransitions.add(eT4);

		ArrayList newExp = new ArrayList();
		newExp.add(stateFirst);
		newExp.addAll(expFirst);
		newExp.addAll(expSecond);
		newExp.add(stateSecond);

		literalsStack.push(newExp);
	}

	private void evalConcat() {

		ArrayList expSecond = (ArrayList) literalsStack.pop();
		ArrayList expFirst = (ArrayList) literalsStack.pop();

		Transition t = new Transition("epsilon");

		t.stateArray.add((State) expSecond.get(0));
		State s = (State) expFirst.get(expFirst.size() - 1);
		s.arrayTransitions.add(t);

		ArrayList newExp = new ArrayList();
		newExp.addAll(expFirst);
		newExp.addAll(expSecond);

		literalsStack.push(newExp);
	}

	private void inStackPush(String strInChar) {

		System.out.println("Push " + strInChar);

		ArrayList arrayExpression = new ArrayList();

		String strState1Name = "s" + Integer.toString(NextState);
		State s1 = new State(strState1Name);

		++NextState;

		String strState2Name = "s" + Integer.toString(NextState);
		State s2 = new State(strState2Name);

		Transition t = new Transition(strInChar);
		t.stateArray.add(s2);
		s1.arrayTransitions.add(t);

		arrayExpression.add(s1);
		arrayExpression.add(s2);

		literalsStack.push(arrayExpression);

		++NextState;

		treeSetInput.add(strInChar);
	}

}
