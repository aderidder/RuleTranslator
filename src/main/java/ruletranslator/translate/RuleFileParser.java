package ruletranslator.translate;

import ruletranslator.shared.FileOperations;
import ruletranslator.shared.LogfileWriter;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RuleFileParser {
    RuleFileParser(String ruleFile, String translatedFileName, JTextArea log, CRFVersionFileParser crfVersionFileParser){
        this.ruleFile = ruleFile.replace(".xml", "");
        this.log = log;
        this.translatedFileName = translatedFileName;
        this.crfVersionFileParser = crfVersionFileParser;
    }

    // write feedback to the logfile and to the textarea
    private void giveFeedback(String line){
        LogfileWriter.writeLine(line);
        log.append(line+newline);
    }

    private static boolean isGroupItem(String anID){
        return anID.startsWith("IG_");
    }

    private static boolean isItem(String anID){
        return anID.startsWith("I_");
    }

    private String handleHeaderPatternMatch(Matcher matcher, String line) throws Exception{
        String itemName, itemID;
        // fetch the itemName from the  pattern's first group
        itemName = matcher.group(1);
        // fetch the itemID from the pattern's second group
        itemID = matcher.group(2);

        if(isGroupItem(itemID)){
            oldIDToNameGroup.put(itemID, itemName);
            // replace the identifier by the one stored in the nameToNewID hashtable
            line = line.replace(itemID, crfVersionFileParser.getNewIDGroup(itemName));
            // give feedback to the screen and logfile
            giveFeedback(line+"\nR:\t\t"+itemID+" --> "+crfVersionFileParser.getNewIDGroup(itemName));
        }
        else if(isItem(itemID)){
            oldIDToNameItem.put(itemID, itemName);
            // replace the identifier by the one stored in the nameToNewID hashtable
            line = line.replace(itemID, crfVersionFileParser.getNewIDItem(itemName));
            // give feedback to the screen and logfile
            giveFeedback(line+"\nR:\t\t"+itemID+" --> "+crfVersionFileParser.getNewIDItem(itemName));
        }
        else{
            throw new Exception("Unknown Item type: "+itemID);
        }
        return line;
    }

    // parse the xml header
    private void parseHeader(BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws Exception{
        String line="";
        boolean changed=false;

        try{
            // read until the end of the file
            while((line=bufferedReader.readLine())!=null){
                // if we reach the end of the comment section, break
                if(line.contains("-->")){
                    break;
                }

                // if line contains a normal item (starts with I_) or a group item (starts with IG_) do things
                if(line.contains("I_")||line.contains("IG_")){
                    Matcher matcher = headerPattern.matcher(line);
                    if(matcher.matches()){
                        line = handleHeaderPatternMatch(matcher, line);
                        changed = true;
                    }
                }
                // write the line to our new file
                FileOperations.writeLine(bufferedWriter, line);
                // give feedback to the screen and logfile
                if(!changed) giveFeedback(line);
                changed=false;
            }
            FileOperations.writeLine(bufferedWriter, line);
            giveFeedback(line);
        } catch(Exception e){
            throw new Exception ("Problem while parsing the header.\nline:"+line+"\nMessage is: "+e.toString());
        }
    }

//    private static String getFromTable(Map<String, String> map, String key) throws Exception{
//        if(!map.containsKey(key)) throw new Exception("Key not found: "+key+"\nPlease check spelling and casing of the name!");
//        return map.get(key);
//    }

    private String handleBodyPatternMatch(Matcher matcher, String line) throws Exception{
        String itemName, oldItemID, newItemID;

        // retrieve the oldItemID from the matched group
        oldItemID = matcher.group(1);
        // translate id to name
        if(isGroupItem(oldItemID)){
            itemName = oldIDToNameGroup.get(oldItemID);
            // translate name to new id
            newItemID = crfVersionFileParser.getNewIDGroup(itemName);
        }
        else if(isItem(oldItemID)){
            itemName = oldIDToNameItem.get(oldItemID);
            // translate name to new id
            newItemID = crfVersionFileParser.getNewIDItem(itemName);
        }
        else{
            throw new Exception("Unknown Item Type");
        }
        // replace the first instance of the identifier with the new identifier
        line = line.replaceFirst("\\b"+oldItemID+"\\b", newItemID);

        // give feedback to the screen and logfile
        giveFeedback(line+newline+"R:\t\t"+oldItemID+" --> "+newItemID+newline);
        return line;
    }

    // parse the xml body
    private void parseBody(BufferedReader bufferedReader, BufferedWriter bufferedWriter) throws Exception{
        String line="";
        boolean changed=false;
        Matcher matcher;

        try{
            // read until end of file
            while((line=bufferedReader.readLine())!=null){
                // trim line and split using space
                String [] spaceSplit = line.trim().split(" ");
                for(String substring:spaceSplit){
                    // match the pattern against each substring
                    matcher = bodyPattern.matcher(substring);
                    if(matcher.matches()){
                        line = handleBodyPatternMatch(matcher, line);
                        changed=true;
                    }
                }
                // write the line to our new file
                FileOperations.writeLine(bufferedWriter, line);
                // give feedback to the screen and logfile
                if(!changed) giveFeedback(line);
                changed=false;
            }
        } catch(Exception e){
            throw new Exception ("Problem while parsing the body.\nline:"+line+"\nMessage is: "+e.toString());
        }
    }

    // translate the xml file
    void translateFile() throws Exception{
        // create the output file
        BufferedWriter bufferedWriter = FileOperations.openFileWriter(translatedFileName);
        BufferedReader bufferedReader = FileOperations.openFileReader(ruleFile+".xml");

        try{
            // first parse the xml's header, which contains a description of the name and identifier
            parseHeader(bufferedReader, bufferedWriter);
            // next parse the xml's body, which contains the actual rules
            parseBody(bufferedReader, bufferedWriter);
        } catch(Exception e){
            throw new Exception("Problem parsing file\nError is: "+e.toString()+newline);
        } finally{
            FileOperations.closeFileReader(bufferedReader);
            FileOperations.closeFileWriter(bufferedWriter);
        }
    }


    // Patterns for the xml file
    // whitespaces - nonwhitespaces - whitespaces - nonwhitespaces
    private static final Pattern headerPattern = Pattern.compile("\\s+(\\S+)\\s+(\\S+)\\s*");
    // anything - word boundary - a word that starts with either I_ or IG_ - nonwhitespaces - word boundary - anything
    private static final Pattern bodyPattern = Pattern.compile(".*\\b((I|IG)_\\w+)\\b.*");

    private static final String newline = System.getProperty("line.separator");

    private final String ruleFile;
    private final String translatedFileName;
//    private final String logFileName;

    private final JTextArea log;

    private final Map<String, String> oldIDToNameItem = new HashMap<>();
    private final Map<String, String> oldIDToNameGroup = new HashMap<>();

    private final CRFVersionFileParser crfVersionFileParser;

}
