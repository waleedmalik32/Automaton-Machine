import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class ParseToCfg {
	
	public static String ToCfg(String regex){
		
		String resultant = "S\n";

		for (int i = 1; i < regex.length(); i++) {
			if (regex.charAt(i) == '+' && regex.charAt(i - 1) == regex.charAt(i - 2)) {
				resultant+=regex.charAt(i - 1) + "|"; 
				resultant+="\n";
				//System.out.println(regex.charAt(i - 1) + "|");

			} else if (regex.charAt(i) == '+'&& regex.charAt(i - 1) != regex.charAt(i - 2)) {

				if (regex.charAt(i) != '*') {
					resultant+=regex.charAt(i - 1) + "|" + regex.charAt(i - 2) + "|";
					resultant+="\n";
					//System.out.println(regex.charAt(i - 1) + "|" + regex.charAt(i - 2) + "|");
				}

				else if (regex.charAt(i + 1) == '*') {
					resultant+=regex.charAt(i - 2) + "S" + "|" + regex.charAt(i - 1) + "S" + "|";
					resultant+="\n";
					//System.out.println(regex.charAt(i - 2) + "S" + "|" + regex.charAt(i - 1) + "S" + "|");

				} else {
					resultant+=regex.charAt(i - 1) + "|" + regex.charAt(i - 2) + "|";
					resultant+="\n";		
					//System.out.println(regex.charAt(i - 1) + "|" + regex.charAt(i - 2) + "|");

				}
				//System.out.println("|");
				resultant+="|\n";

			} else if (regex.charAt(i) == '*') {

				if (regex.charAt(i - 1) >= 97 && regex.charAt(i - 1) <= 122) {
					resultant+=regex.charAt(i - 1) + "S";
					resultant+="\n";
					//System.out.println(regex.charAt(i - 1) + "S");

				}
				resultant+="|" + "^";
				resultant+="\n";
				//System.out.println("|" + "^");

			} else if (regex.charAt(i) == '|') {
				resultant+=regex.charAt(i - 2);
				resultant+="\n";
				resultant+=regex.charAt(i - 1) + "|";
				resultant+="\n";
				//System.out.println(regex.charAt(i - 2));
				//System.out.println(regex.charAt(i - 1) + "|");

			}
		}
		for (int j = 0; j < regex.length(); j++) {
			if (regex.charAt(j) == '*') {
				resultant+="^";
				resultant+="\n";
				//System.out.println("^");
				return resultant;
			}
		}
		//System.out.println("\n ");
		resultant+="\n";
		//System.out.println("resultant : \n" + resultant);
		return resultant;

	}
	
	public void cfgToFile(String CFG) {
		try {
			System.out.println(CFG);
			File file = new File("Output/CFG.txt");
			file.getParentFile().mkdirs();
			PrintWriter os = new PrintWriter(file);
			os.println("");
			String delims = "\n";
			String splitString = CFG;
			StringTokenizer st = new StringTokenizer(splitString,delims);
			while (st.hasMoreElements()) {
				os.println(st.nextElement().toString()); 
			}
			os.println("");       
			os.close();
			System.out.println("Done!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
}
