import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class finalStep {
	public static void Tester(String regex,String path) throws IOException{
		System.out.println("inputChecker.Tester() : " + path);
		

		File fin = new File(path);
			FileInputStream fis = new FileInputStream(fin);
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line = null;
			while ((line = br.readLine()) != null) {
				
				line.trim();
				boolean status = inputChecker.parseInput(regex,line);
				if(status == true){
					cfgToFile(line);
				}
				else if(status == false){
					
				}
		}
			br.close();
		
	}
	


	public static void cfgToFile(String output_txt) {

	      BufferedWriter bw = null;
	 
	      try {
	    	  System.out.println(output_txt);
	         bw = new BufferedWriter(new FileWriter("Output/Output.txt", true));
		     bw.write(output_txt);
		     bw.newLine();
		     bw.flush();
		     } 
	      catch (IOException ioe) {
		     ioe.printStackTrace();
		     } finally {                       // always close the file
		     if (bw != null) try {
		        bw.close();
		        System.out.println("Done!");
		        } catch (IOException ioe2) 
			     {
			        // just ignore it
			     }
		      } // end try/catch/finally
		 
		   } 
}
