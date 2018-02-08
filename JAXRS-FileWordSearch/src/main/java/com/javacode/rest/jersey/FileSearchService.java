package com.javacode.rest.jersey;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;


@Path("/files")
public class FileSearchService {
	private static final String FILE_FOLDER = "c://tmp/";

@GET
@Path("/search")
@Consumes(MediaType.TEXT_HTML)
public String findFiles(@QueryParam("searchWords") String searchWords) {
	String result = null;
	String htmlResult = null;
	String[] srchWords = searchWords.split(",");
	 try{
		 File directory = new File(FILE_FOLDER);
		 result = listFileFolder(directory,srchWords);
	        
	 }
	 catch(Exception ex) {
         result = "An exception has occurred. Details: " + ex.getMessage();                
     }
	 if (result.indexOf(",") < 0){
		 htmlResult = "<p>No files found that contain following words " +  searchWords + "." + "</p>";
	 }
	 else{
		 String[] listFiles = result.split(",");
		 htmlResult = "<p>Files that contain following words " +  searchWords + " are:" + "</p><ul>";
		 for (String s: listFiles){
			 htmlResult = htmlResult + "<li>" + s + "</li>";
		 }
		 htmlResult = htmlResult + "</ul>";
	 }
	 
     
     return htmlResult;
}
/*
 * Method to go through files in directory specified - c://tmp
 */
private String listFileFolder(File directory, String[] srchWords){
	String result = null;
	File[] fList = directory.listFiles();
    for (File file : fList){
        if (file.isFile()){
            if(foundWords(srchWords,file) && file.getName() != null){
            	result = result + "," + file.getName();
            }
            else if(file.isDirectory()){
            	listFileFolder(file,srchWords);
            }
        }
    }
    return result;
}
/*
 *Search Function 
 */
@SuppressWarnings("finally")
public boolean foundWords (String[] srchWords, File file){

	Boolean flag = false;
	Scanner txtscan = null;
	try{
		txtscan = new Scanner(file);
		while(txtscan.hasNextLine()){
			final String fileline = txtscan.nextLine();
			for (String s: srchWords){
				if (fileline.contains(s)){
					flag = true;
				}
				else {
					flag = false;
				}
			}
		}
	}
	catch (FileNotFoundException ex){
		flag = false;
	}
	
	finally {
		txtscan.close();
		return flag;
	}
	
	}
	
}

