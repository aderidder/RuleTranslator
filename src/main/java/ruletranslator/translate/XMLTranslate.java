package ruletranslator.translate;

import javax.swing.JTextArea;
import ruletranslator.shared.LogfileWriter;

// the actual translator
public class XMLTranslate {
    public XMLTranslate(String ruleFile, String crfVersionFile, JTextArea log){
		this.ruleFile = ruleFile.replace(".xml", "");
		this.crfVersionFile = crfVersionFile;
		this.log = log;
		this.logFileName = this.ruleFile+"_log.txt";
        this.translatedFileName = this.ruleFile+"_t.xml";
	}
	

	public void runTranslate() throws Exception{
        // create logfile
		LogfileWriter.openFile(logFileName);
        // parse the version html file to create the translation tables
        CRFVersionFileParser crfVersionFileParser = new CRFVersionFileParser(crfVersionFile, log);
        crfVersionFileParser.parseInFile();
		// translate the rule file
        RuleFileParser ruleFileParser = new RuleFileParser(ruleFile,translatedFileName, log, crfVersionFileParser);
		ruleFileParser.translateFile();
        // close logfile
		LogfileWriter.closeFile();
	}

	public String getLogFileName(){
		return logFileName;
	}

    public String getTranslatedFileName(){
        return translatedFileName;
    }

    private final String ruleFile;
    private final String translatedFileName;
    private final String logFileName;
    private final String crfVersionFile;
    private final JTextArea log;

}
