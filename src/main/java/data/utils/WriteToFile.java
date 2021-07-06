package data.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class WriteToFile {
	
	//1 48 600 600 2.2

	  public static void main(String[] args) {
		  long start = new Date().getTime();
		  int from = 1;
		  int to = 200;
		  if(args != null && args.length > 0) {
			  to = new Integer(args[0]); 
		  }
		  System.out.println(start + ", from: " + from + ", to: " + to);
		  while(from != to) {
				genOneGig();
				System.out.println("genOneGig: " + from + ", free space: " + new File("/").length());
				from ++;
		  }
		  System.out.println((new Date().getTime() - start)/1000);
	  }
	  
	  private static void genOneGig() {
		  
		  	for(int a = 0; a < 600; a++){
		  		try {
			    	
			    	File myObj = new File("/home/laimonas/.fill/file_" + new Date().getTime());
			    	if (myObj.createNewFile()) {
			    		FileWriter myWriter = new FileWriter(myObj);
				    	for(int i = 0; i < 600; i++){
					    	myWriter.append(text);	
				    	}
				        myWriter.close();
			        }
			      } catch (IOException e) {
			        e.printStackTrace();
			      }
	    	}
	}

	static String text = "text,text,text,text,text,text,text,text,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"
	  		+ "text,text,text,text,texttext,text,text,text,texttext,text,text,text,text"; 
}
