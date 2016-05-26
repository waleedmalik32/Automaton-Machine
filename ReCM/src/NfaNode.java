import java.util.ArrayList;
import java.util.List;


public class NfaNode {
	String value;
	String stateNo;
	static ArrayList<String>  AllLiterals=new ArrayList<String>();
	int initial_Final;
	NfaNode(){
		value=null;
		stateNo=null;
		initial_Final=0;

	}
	void setNfaNode(String stateNo,String value){
		this.value=value;
		this.stateNo=stateNo;
	}
	static void loadLiterals(String literal){
		AllLiterals.add(literal);
	}
	void  displayNFA(List<List<NfaNode>> listOfLists){
		for(int x = 0; x < listOfLists.size(); x++)
		{
			ArrayList<NfaNode> singleList = new ArrayList<NfaNode>();
			singleList= (ArrayList<NfaNode>) listOfLists.get(x);
		    for(int z = 0; z < singleList.size(); z++)
		        {
		        System.out.print(singleList.get(z).value+" -> "+singleList.get(z).stateNo);
		        System.out.print(" ");
		    }
		    System.out.println("");
		}
	}

}
