package ruletranslator.translate;

import junit.framework.TestCase;
import javax.swing.*;

public class XMLTranslateTest extends TestCase {

    public void testGetTranslatedFileName() {
        String crfVersionFile = ""; JTextArea log = new JTextArea();
        String ruleFile = "c:/temp/ruleFile.xml";
        XMLTranslate xmlTranslate = new XMLTranslate(ruleFile, crfVersionFile, log);
        assertTrue("Test if c:/temp/ruleFile.xml's output file is c:/temp/ruleFile_t.xml", xmlTranslate.getTranslatedFileName().equalsIgnoreCase("c:/temp/ruleFile_t.xml"));
    }

    public void testGetLogFileName() {
        String crfVersionFile = ""; JTextArea log = new JTextArea();
        String ruleFile = "c:/temp/ruleFile.xml";
        XMLTranslate xmlTranslate = new XMLTranslate(ruleFile, crfVersionFile, log);
        assertTrue("Test if c:/temp/ruleFile.xml's log file is c:/temp/ruleFile_log.txt", xmlTranslate.getLogFileName().equalsIgnoreCase("c:/temp/ruleFile_log.txt"));
    }
}