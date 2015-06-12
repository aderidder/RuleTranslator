package ruletranslator.shared;

import java.io.BufferedWriter;

// a static logwriter class
public class LogfileWriter {
	public static void openFile (String fileName) throws Exception{
		bufferedWriter = FileOperations.openFileWriter(fileName);
	}

	public static void closeFile() throws Exception{
		FileOperations.closeFileWriter(bufferedWriter);
	}

	public static synchronized void writeLine() {
		writeLine("");
	}

	// printing a double to the file
	public static synchronized void writeLine(double value) {
		writeLine(Double.toString(value));
	}

	// printing a line to the file
	public static synchronized void writeLine(String line) {
		FileOperations.writeLine(bufferedWriter, line);
	}


	private static BufferedWriter bufferedWriter;
}
