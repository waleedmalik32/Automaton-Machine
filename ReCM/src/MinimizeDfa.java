public class MinimizeDfa {
	/*
	 * Theory 
	 * Delete state if : 
	 * 1.State is not an accepting state 
	 * 2.State does not have any transitions to any other different state.
	 */
/*
	public void Find()
	{
		NfaNode obj = new NfaNode();

		for(int i=0; i<obj.AllLiterals.size()-1; ++i){
			String strInChar = (String)obj.AllLiterals.get(i);

	        for each active state s
	        {
	            if there is transition from s on c
	            {
	                go to the next state t
	                if t is accepting state
	                {
	                    record the pattern 
	                }
	                mark t as active
	            }
	            mark s as inactive
	        }
	        
	        if there is transition from starting state on c
	        {
	            go to the next state s
	            if s is accepting state
	            {
	                record the pattern
	            }
	            mark s as active
	        }
		}
	}
	*/
}
