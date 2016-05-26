import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

///     Theory    /////////
/*
 The start state of DFA is created by taking the Epsilon closure of the start state of the NFA
 For each new DFA state, perform the following for each input character:
 Perform move to the newly created state
 Create new state by taking the Epsilon closure of the result (i). Note that here we could get a state, which is already present in our set. This will result in a set of states, which will form the new DFA state. Note that here from one or many NFA states, we are constructing a single DFA state.
 For each newly created state, perform step 2.
 Accepting states of DFA are all those states, which contain at least one of the accepting states from NFA. Keep in mind that we are here constructing a single DFA state from one or many NFA states.

 Refrence : http://binarysculpting.com/2012/02/15/converting-dfa-to-nfa-by-subset-construction-regular-expressions-part-2/
 */
///     Theory    /////////

public class ParseToDfa {
	public TreeMap nfaTree = new TreeMap();
	public TreeMap dfaTree = new TreeMap();
	static public String NFAstart;
	static public String DFAstart;

	ParseToDfa() {
	}

	public boolean nfaToDfa() {

		ArrayList toProcessList = new ArrayList();
		TreeSet processedTree = new TreeSet();

		State startState = (State) nfaTree.get(NFAstart);
		if (startState == null) {
			return false;
		}

		ArrayList stateSet = Closure(startState);
		startState = new State(stateSet);

		DFAstart = startState.strName;
		dfaTree.put(DFAstart, startState);
		toProcessList.add(startState);
		while (!toProcessList.isEmpty()) {
			State DFAState = (State) toProcessList.get(0);
			dfaTree.put(DFAState.strName, DFAState);
			toProcessList.remove(0);

			if (processedTree.contains(DFAState))
				continue;

			processedTree.add(DFAState);

			// for each possible character
			NfaNode obj = new NfaNode();
			for (int i = 0; i < obj.AllLiterals.size() - 1; ++i) {

				TreeSet moveSet = new TreeSet();
				for (int j = 0; j < DFAState.NFAStates.size(); ++j) {
					ArrayList toMove = Move((State) DFAState.NFAStates.get(j),
							(String) obj.AllLiterals.get(i));
					if (toMove != null)
						moveSet.addAll(toMove); // trick to eliminate duplicates
				}

				TreeSet eClosureSet = new TreeSet();
				Iterator iter = moveSet.iterator();
				while (iter.hasNext()) {
					State tmpState = (State) iter.next();
					ArrayList closureArray = Closure(tmpState);
					eClosureSet.addAll(closureArray);
				}

				if (eClosureSet.size() > 0) {
					State toAdd = new State(new ArrayList(eClosureSet));

					if (!dfaTree.containsKey(toAdd.strName))
						dfaTree.put(toAdd.strName, toAdd);
					else
						toAdd = (State) dfaTree.get(toAdd.strName);

					toProcessList.add(toAdd);

					Transition trans = new Transition(
							(String) obj.AllLiterals.get(i));
					trans.stateArray.add(toAdd);
					DFAState.arrayTransitions.add(trans);
				}
			}
		}

		return true;
	}

	private ArrayList Move(State state, String character) {
		ArrayList Resultant = new ArrayList();

		character.trim();
		if (character.length() == 0) {
			return null;
		}
		for (int i = 0; i < state.arrayTransitions.size(); ++i) {
			Transition transition = (Transition) state.arrayTransitions.get(i);
			if (transition.transChar.compareTo(character) == 0) {
				Resultant.addAll(transition.stateArray);
				return Resultant;
			}
		}
		return null;
	}

	public ArrayList Closure(State toProcess) {
		Stack toProcessStack = new Stack();
		toProcessStack.push(toProcess);
		TreeSet processedTree = new TreeSet();
		TreeSet result = new TreeSet();
		result.add(toProcess);

		while (!toProcessStack.empty()) {
			State currentState = (State) toProcessStack.pop();
			processedTree.add(currentState);
			// for each epsilon transition of this state add that state to the
			// result
			for (int i = 0; i < currentState.arrayTransitions.size(); ++i) {
				Transition t = (Transition) currentState.arrayTransitions
						.get(i);
				// check each transition is it epsilon
				if (t.transChar.compareToIgnoreCase("epsilon") == 0) {
					for (int j = 0; j < t.stateArray.size(); ++j) {
						State s = (State) t.stateArray.get(j);
						result.add(s); // will eliminate duplicates
										// automatically

						if (!processedTree.contains(s))
							toProcessStack.push(s);
					}
				}
			}
		}
		ArrayList resultant = new ArrayList();
		resultant.addAll(result);
		return (resultant);
	}

	public void loadDfa(List<List<NfaNode>> listOfLists) {// ERROR
															// readNFATable()

		for (int x = 0; x < listOfLists.size(); x++) {
			ArrayList<NfaNode> singleList = new ArrayList<NfaNode>();
			singleList = (ArrayList<NfaNode>) listOfLists.get(x);
			State state = new State(singleList.get(0).stateNo);
			if (x == 0) {
				NFAstart = singleList.get(0).stateNo;
			}
			addNFAState(state); // ERROR
		}

		for (int x = 0; x < listOfLists.size(); x++) {
			ArrayList<NfaNode> singleList = new ArrayList<NfaNode>();
			singleList = (ArrayList<NfaNode>) listOfLists.get(x);
			for (int z = 1; z < singleList.size(); z++) {
				Transition transition = new Transition(singleList.get(z).value);
				State state2 = getNFAState(singleList.get(z).stateNo);
				if (state2 != null) {// ERROR
					transition.stateArray.add(state2);
				} else if (state2 == null) {// ERROR
					System.out.println("State2  null");
				}

				state2 = getNFAState(singleList.get(0).stateNo);
				if (state2 != null) {// ERROR
					state2.arrayTransitions.add(transition);
					State tmpState = (State) transition.stateArray
							.get(transition.stateArray.size() - 1);
					System.out.println(state2.strName + " (on character: "
							+ transition.transChar + ") tmp : "
							+ tmpState.strName);
				}
			}
			System.out.println("");
		}
	}

	public boolean addNFAState(State state) {
		// check if the state name exists already
		if (nfaTree.containsKey(state.strName))
			return false;

		// ok, add the state to the map
		try {
			nfaTree.put(state.strName, state);
		} catch (ClassCastException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

		return true;
	}

	public State getNFAState(String name) {
		// Retrieve the state object
		State state = null;
		try {
			state = (State) nfaTree.get(name);
		} catch (ClassCastException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
		return state;
	}
}
