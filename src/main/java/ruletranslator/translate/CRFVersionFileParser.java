package ruletranslator.translate;

import ruletranslator.shared.FileOperations;

import javax.swing.*;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CRFVersionFileParser {
    CRFVersionFileParser(String crfVersionFile, JTextArea log){
        this.crfVersionFile = crfVersionFile;
        this.log = log;
    }


    // parse the html file
    void parseInFile() throws Exception{
        BufferedReader bufferedReader = FileOperations.openFileReader(crfVersionFile);
        String line, prevLine = "";
        boolean groupFound;
        boolean itemFound;

        try{
            // read line until end of file
            while((line=bufferedReader.readLine())!=null){
                // check line for a group identifier
                groupFound = groupIDPatternCheck(line);
                // if a group identifier was found, check the previous line for the group name
                if(groupFound){
                    groupNamePatternCheck(prevLine);
                }
                else{
                    // check line for item name
                    itemFound = itemNamePatternCheck(line);
                    // if itemname found, search for the id
                    if(itemFound){
                        findItemID(bufferedReader);
                    }
                }
                prevLine = line;
            }
        } catch(Exception e){
            throw new Exception("Problem parsing file\nError is: "+e.toString()+newline);
        } finally{
            FileOperations.closeFileReader(bufferedReader);
        }
    }

    // search for the item identifier in a line
    private void findItemID(BufferedReader bufferedReader) throws Exception{
        String line, itemID;
        while((line=bufferedReader.readLine())!=null){
            Matcher matcher = itemIDPattern.matcher(line);
            if(matcher.matches()){
                itemID = matcher.group(1);
                nameToNewIDItem.put(itemName, itemID);
                break;
            }
        }
    }

    // search for the item name in a line
    private boolean itemNamePatternCheck(String line){
        Matcher matcher = itemNamePattern.matcher(line);
        if(matcher.matches()){
            itemName = matcher.group(1);
            return true;
        }
        return false;
    }

    // search for the group identifier in a line
    private boolean groupIDPatternCheck(String line){
        Matcher matcher = groupIDPattern.matcher(line);
        if(matcher.matches()){
            groupID = matcher.group(1);
            return true;
        }
        return false;
    }

    // search for the group name in a line
    private void groupNamePatternCheck(String line){
        Matcher matcher = groupNamePattern.matcher(line);
        String groupName;
        if(matcher.matches()){
            groupName = matcher.group(1);
            nameToNewIDGroup.put(groupName, groupID);
            System.out.println();
            log.append("Adding: "+groupName+" "+groupID+newline);
        }
    }

    String getNewIDItem(String key){
        return nameToNewIDItem.get(key);
    }

    String getNewIDGroup(String key){
        return nameToNewIDGroup.get(key);
    }

    private String groupID, itemName;

    private final String crfVersionFile;
    private final JTextArea log;

    private final Map<String, String> nameToNewIDItem = new HashMap<>();
    private final Map<String, String> nameToNewIDGroup = new HashMap<>();

    // patterns for the HTML file
    // capture the stuff between > and <
    private static final Pattern groupNamePattern = Pattern.compile(".*>(.*)<.*");
    // anything - word boundary - a word that starts with IG_ - non-whitespaces - word boundary - anything
    private static final Pattern groupIDPattern = Pattern.compile(".*\\b(IG_\\S+)\\b<.*");

    private static final Pattern itemNamePattern = Pattern.compile(".*<td class=\"table_cell\"><a href=.*>(.*)</a.*");
    private static final Pattern itemIDPattern = Pattern.compile(".*>(.*)&nbsp.*");

    private static final String newline = System.getProperty("line.separator");
}
