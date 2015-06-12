package ruletranslator.shared;
// SharedFileOperations.java
// Class for file operations
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.net.URL;

//import javax.swing.JTextArea;

import ruletranslator.gui.TranslateGUI;

public class FileOperations {
	
	public static String getURLString(String fileName){
		URL url = TranslateGUI.class.getResource("images/"+fileName);
		return url.toString();
	}
	
	public static URL getImageURL(String fileName){
		return TranslateGUI.class.getResource("/images/"+fileName);
	}

	// open file and return buffered reader
	public static BufferedReader openFileReader(String fileName) throws Exception{
		BufferedReader in;
		try{
			in = new BufferedReader(new FileReader(fileName));
		} catch (Exception e){
//			log.append("Error opening file "+fileName+"\nError is: "+e.toString()+"\n");
			throw new Exception ("Error opening file "+fileName+newline+"Error is: "+e.toString()+newline);
		}
		return in;
	}

	// open file and return buffered writer
	public static BufferedWriter openFileWriter(String fileName) throws Exception{
		BufferedWriter out;
		try {
			//Construct the BufferedWriter object
			out = new BufferedWriter(new FileWriter(fileName));
		} catch (Exception e) {
//			e.printStackTrace();
//			log.append("Error opening write file "+fileName+"\nError is: "+e.toString()+"\n");
			throw new Exception("Error opening write file "+fileName+newline+"\nError is: "+e.toString()+newline);
		} 
		return out;
	}

	// close filereader
	public static void closeFileReader(BufferedReader in) throws Exception{
		try{
			in.close();
		} catch (Exception e){
//			System.out.println("Error closing file\nError is: "+e.toString());
//			System.exit(1);
//			log.append("Error closing file\nError is: "+e.toString()+"\n");
			throw new Exception("Error closing file\nError is: "+e.toString()+newline);
			
		}
	}

	// close filewriter
	public static void closeFileWriter(BufferedWriter out) throws Exception{
		try {
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw new Exception("Error closing file"+newline+"Error is: "+e.toString()+newline);
		}
	}

	// print a line to the file
	public static synchronized void writeLine(BufferedWriter out, String line){
			try{
				out.write(line);
				out.newLine();
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// create a directory
	public static synchronized void createDir(String directory) {
		File file = new File (directory);
		if (!file.exists())
            if(!file.mkdir()) System.out.println("Create directory failed");
	}

	public static synchronized void deleteFiles(final String prefixString, String fileDirIn){
		File file = new File(fileDirIn);
		deleteFiles(prefixString, file);
	}

	// delete files based on a string
	public static synchronized void deleteFiles(final String prefixString, File fileDirIn){
//		FilenameFilter filenameFilter = new FilenameFilter(){
//			public boolean accept(File dir, String name) {
//				return name.startsWith(prefixString);
//			}
//		};
        FilenameFilter filenameFilter = (dir, name) -> name.startsWith(prefixString);
		File[] files = fileDirIn.listFiles(filenameFilter);
        for (File file : files) {
            if(!file.delete()) System.out.println("Deleting file failed");
        }
	}

	public static boolean fileExists(String fileName){
		File file = new File(fileName);
		return file.exists();
	}

	private static final String newline = System.getProperty("line.separator");}
