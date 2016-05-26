
import java.util.*;

public class Transition {

	public String transChar;

	// List of states to which it transfers in character input
	public ArrayList stateArray = new ArrayList();

	// Default constructor
	public Transition(){
	}

	public Transition(String chr){
		transChar	= chr;
	}
}