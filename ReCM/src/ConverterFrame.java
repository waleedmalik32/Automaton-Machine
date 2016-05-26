import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConverterFrame {
	List<List<NfaNode>> NFA = new ArrayList<List<NfaNode>>();

	JFrame frame;
	private JTable table;
	private JTextField regExText;
	private JTable TransitionTable;
	private JButton buttonToDfa;
	private JButton buttonMinDfa;
	private JButton buttonToCfg;
	DefaultTableModel model;
	private JLabel lblInputs;
	
	
	private ParseToNfa regExParser = new ParseToNfa();
	private ParseToDfa nfaParser = new ParseToDfa();
	private MinimizeDfa minimizeDfa = new MinimizeDfa();
	private ParseToCfg regexToCfg = new ParseToCfg();
	private inputChecker InputChecker = new inputChecker();
	private finalStep Final = new finalStep();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConverterFrame window = new ConverterFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	    
	public ConverterFrame() {
		frame = new JFrame("Regular Expression Parser");

		frame.setBounds(100, 100, 664, 475);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		regExText = new JTextField();
		regExText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				regExText.setText("");
			}
		});
		regExText.setBounds(10, 5, 275, 53);
		frame.getContentPane().add(regExText);
		regExText.setColumns(10);
		regExText.setText("  Enter Regular Expression here !!");

		TransitionTable = new JTable(22, 10);
		//
		Color ivory = new Color(255, 255, 208);
		TransitionTable.setOpaque(true);
		TransitionTable.setFillsViewportHeight(true);
		TransitionTable.setBackground(ivory);
		//
		TransitionTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("");
			}
		});
		TransitionTable.setBounds(0, 90, 661, 382);
		frame.getContentPane().add(TransitionTable);
		TransitionTable.validate();

		final JButton btnToNfa = new JButton("To NFA");
		btnToNfa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnToNfa.setEnabled(false);
				String regex = regExText.getText();
				regex = regex.replace("|", "");
				regex = regex.replace("+", "|");
				System.out.println(regex);
				ArrayList arrayNFA = regExParser.regexToNFA(regex);
				if (arrayNFA != null) {
					displayNFA(arrayNFA);
					outputToFile("NFA.txt");
				}

			}
		});

		btnToNfa.setBounds(294, 4, 79, 36);
		frame.getContentPane().add(btnToNfa);

		buttonToDfa = new JButton("To DFA");
		buttonToDfa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				buttonToDfa.setEnabled(false);
				nfaParser.loadDfa(NFA);
				nfaParser.nfaToDfa();
				displayDFA();
				outputToFile("DFA.txt");
			}
		});

		buttonToDfa.setBounds(383, 4, 79, 36);
		frame.getContentPane().add(buttonToDfa);

		buttonMinDfa = new JButton("Minimize");
		buttonMinDfa.setEnabled(false);
		buttonMinDfa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});

		buttonMinDfa.setBounds(467, 4, 95, 36);
		frame.getContentPane().add(buttonMinDfa);

		buttonToCfg = new JButton("To CFG");
		buttonToCfg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				buttonToCfg.setEnabled(false);
				String REGEX = regExText.getText();
				
				convertToPostFix Objc = new convertToPostFix();
				String postREGEX = Objc.toPost(REGEX);
				
				System.out.println("postREGEX : "+ postREGEX);
				String CFG = regexToCfg.ToCfg(postREGEX);
				regexToCfg.cfgToFile(CFG);
				
				//PRINT NEW LAYOUT
				JFrame frame1 = new JFrame("CFG");
				frame1.setBounds(100, 100, 474, 138);
				frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame1.setResizable(false);
				frame1.getContentPane().setLayout(null);
				JTextField tf1;
				JTextField tf2;
				tf1 = new JTextField();
				tf1.setBounds(82, 11, 336, 31);
				frame1.getContentPane().add(tf1);
				tf1.setColumns(10);
				
				tf2 = new JTextField();
				tf2.setColumns(10);
				tf2.setBounds(82, 53, 336, 31);
				frame1.getContentPane().add(tf2);
				
				JLabel lblRegex = new JLabel("Regex : ");
				lblRegex.setBounds(26, 19, 46, 14);
				frame1.getContentPane().add(lblRegex);
				
				JLabel lblCfg = new JLabel("CFG : ");
				lblCfg.setBounds(26, 61, 46, 14);
				frame1.getContentPane().add(lblCfg);
				frame1.setVisible(true);
				
				tf1.setText(REGEX);
				tf2.setText(CFG);
				
				
			}
		});

		buttonToCfg.setBounds(572, 4, 79, 36);
		frame.getContentPane().add(buttonToCfg);

		JLabel lblStates = new JLabel("States      |");
		lblStates.setBounds(10, 73, 79, 14);
		frame.getContentPane().add(lblStates);

		lblInputs = new JLabel("Inputs");
		lblInputs.setBounds(99, 73, 46, 14);
		frame.getContentPane().add(lblInputs);
		
		JButton btnNewButton = new JButton("Analyse Input File");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String regex = regExText.getText();
				regex = regex.replace("|", "");
				regex = regex.replace("+", "|");
				regex = regex.replace("*", "+");
				try {
					Final.Tester(regex,MainClass.path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(294, 46, 354, 33);
		frame.getContentPane().add(btnNewButton);
	}

	private void displayNFA(ArrayList arrayNFA) {
		TransitionTable = new JTable(22, 10);
		//
		Color ivory = new Color(255, 255, 208);
		TransitionTable.setOpaque(true);
		TransitionTable.setFillsViewportHeight(true);
		TransitionTable.setBackground(ivory);
		//
		
		TransitionTable.setBounds(0, 90, 661, 382);

		frame.getContentPane().add(TransitionTable);
		TransitionTable.validate();
		TransitionTable.setValueAt("States", 0, 0);

		int nColumn = 1;
		TreeMap inCharPos = new TreeMap();
		Iterator iter = regExParser.treeSetInput.iterator();
		while (iter.hasNext()) {
			String strInChar = (String) iter.next();
			TransitionTable.setValueAt(strInChar, 0, nColumn);
			inCharPos.put(strInChar, new Integer(nColumn));
			++nColumn;

		}
		// last one is epsilon
		TransitionTable.setValueAt("epsilon", 0, nColumn);
		inCharPos.put("epsilon", new Integer(nColumn));

		// now traverse through the states array and display all transitions for
		// it
		for (int i = 0; i < arrayNFA.size(); ++i) {
			State s = (State) arrayNFA.get(i);

			TransitionTable.setValueAt(s.strName, i + 1, 0);

			for (int j = 0; j < s.arrayTransitions.size(); ++j) {
				Transition t = (Transition) s.arrayTransitions.get(j);

				Integer nCol = (Integer) inCharPos.get(t.transChar);

				String strStates = ((State) t.stateArray.get(0)).strName;
				for (int k = 1; k < t.stateArray.size(); ++k)
					strStates += "," + ((State) t.stateArray.get(k)).strName;

				String strEntry = (String) TransitionTable.getValueAt(i + 1,
						nCol.intValue());
				if (strEntry != null)
					if (strEntry.length() > 0)
						strEntry += "," + strStates;
					else
						strEntry = strStates;
				else
					strEntry = strStates;

				if (strEntry != null)
					TransitionTable
							.setValueAt(strEntry, i + 1, nCol.intValue());
			}
		}
		setNFA();
	}

	public void setNFA() {

		for (int i = 1;; i++) {
			String literal = (String) TransitionTable.getValueAt(0, i);
			if (literal == null) {
				break;
			}
			NfaNode.loadLiterals(literal);
			System.out.println(literal);
		}
		int columns;
		int rows;

		for (int i = 1;; i++) {
			String literal = (String) TransitionTable.getValueAt(0, i);
			if (literal == null) {
				columns = i;
				break;
			}

		}
		for (int i = 1;; i++) {
			String literal = (String) TransitionTable.getValueAt(i, 0);
			if (literal == null) {
				rows = i - 1;
				break;
			}
		}

		String literal;
		NfaNode Obj;
		for (int i = 1; i < rows + 1; i++) {
			ArrayList<NfaNode> NODE = new ArrayList<NfaNode>();
			NODE.clear();
			for (int j = 0; j < columns + 1; j++) {
				literal = (String) TransitionTable.getValueAt(i, j);
				if (j == 0) {
					Obj = new NfaNode();
					Obj.stateNo = (String) TransitionTable.getValueAt(i, 0);
					Obj.value = "0";
					NODE.add(Obj);
				} else if (j != 0 && literal != null && literal.length() > 0) {
					String delims = ",";
					String splitString = (String) TransitionTable.getValueAt(i,
							j);
					String value = (String) TransitionTable.getValueAt(0, j);
					StringTokenizer st = new StringTokenizer(splitString,
							delims);
					while (st.hasMoreElements()) {
						Obj = new NfaNode();
						Obj.value = value;
						Obj.stateNo = st.nextElement().toString();

						NODE.add(Obj);
					}
				}
			}
			NFA.add(NODE);
		}
		Obj = new NfaNode();
		Obj.displayNFA(NFA);
	}
	
	
	public void outputToFile(String name) {
		try {
			name="Output/" + name ;
			File file = new File(name);
			file.getParentFile().mkdirs();
			PrintWriter os = new PrintWriter(file);
			for (int row = 0; row < TransitionTable.getRowCount(); row++) {
				String ToAdd = new String();
				for (int col = 0; col < TransitionTable.getColumnCount(); col++) {
					if(col==1 && row==1){
						os.println("");
					}
					String literal = (String) TransitionTable.getValueAt(row,col);
					if(literal!=null && literal.length() > 0){
						ToAdd+=literal + "        ";
						if(col==0 && row!=0){
							ToAdd+="    ";
						}
					}
					else{
						ToAdd+= "          " ;
						if(col==0 && row!=0){
							ToAdd+="    ";
						}
					}
				}
				os.println(ToAdd);
			}
			os.println("");
            
			os.close();
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void displayDFA(){

		// Get the name of the start state
		String strDFAStartState = nfaParser.DFAstart;
		TreeMap treeMapDFA 		= nfaParser.dfaTree;

		// Create table of the needed size
		TransitionTable = new JTable(22, 10);
		//
		Color ivory = new Color(255, 255, 208);
		TransitionTable.setOpaque(true);
		TransitionTable.setFillsViewportHeight(true);
		TransitionTable.setBackground(ivory);
		//
		
		TransitionTable.setBounds(0, 90, 661, 382);
		
		frame.getContentPane().add(TransitionTable);
		TransitionTable.validate();
		TransitionTable.setValueAt("States", 0, 0);
		

		// Display input characters and record the position of each character
		// in a tree map
		NfaNode obj = new NfaNode();
		int nColumn 		= 1;
		TreeMap charTreeMap	= new TreeMap();
		for(int i=0; i<obj.AllLiterals.size()-1; ++i){
			String strInChar = (String)obj.AllLiterals.get(i);
			TransitionTable.setValueAt(strInChar, 0, nColumn);
			charTreeMap.put(strInChar, new Integer(nColumn));
			++nColumn;
		}
	
		State DFAStartState = (State)treeMapDFA.get(strDFAStartState);
		treeMapDFA.remove(strDFAStartState);

		// Display DFA start state first
		TransitionTable.setValueAt(DFAStartState.strName, 1, 0);
		for(int i=0; i<DFAStartState.arrayTransitions.size(); ++i){
			Transition t = (Transition)DFAStartState.arrayTransitions.get(i);
			Integer nCol = (Integer)charTreeMap.get(t.transChar);
			TransitionTable.setValueAt(((State)t.stateArray.get(0)).strName, 1, nCol.intValue());
		}

		Iterator iter = treeMapDFA.values().iterator();
		int nRow = 2;
		while(iter.hasNext()){
			State tmpState = (State)iter.next();

			TransitionTable.setValueAt(tmpState.strName, nRow, 0);
			for(int j=0; j<tmpState.arrayTransitions.size(); ++j){
				Transition t = (Transition)tmpState.arrayTransitions.get(j);
				Integer nCol = (Integer)charTreeMap.get(t.transChar);
				TransitionTable.setValueAt(((State)t.stateArray.get(0)).strName, nRow, nCol.intValue());
			}
			++nRow;
		}
	}

}
