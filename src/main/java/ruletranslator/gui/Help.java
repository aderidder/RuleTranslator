package ruletranslator.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ruletranslator.shared.FileOperations;

// help is also a panel
public class Help extends JPanel implements ListSelectionListener {
	private Help(){
		// panel with borderlayout
		super(new BorderLayout());
		setup();
	}

	private void setup(){
		// setup the help panel
		JSplitPane helpPanel = helpPaneSetup();
		// add the help panel to the center of the borderlayout
		add(helpPanel, BorderLayout.CENTER);
	}

	// setup the help panel, which is a splitpane
	// on the left we will have the topics; on the right the text for the selected topic
	private JSplitPane helpPaneSetup(){
		// create a new list based on the help topics
		// the model is single select and we start by setting the selected index to the first topic
        JList <String> list = new JList<>(helpTopics);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);

		// create a scrollpanel for the list
		JScrollPane listScrollPane = new JScrollPane(list);

		// create an editorpanel which accepts html
		rightArea = new JEditorPane();
		rightArea.setContentType("text/html");
		rightArea.setEditable(false);

		// add the panel to a scroll pane
		JScrollPane rightScrollPane = new JScrollPane(rightArea);

		//Create the split pane with the two scroll panes in it.
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, rightScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane.
		Dimension minimumSize = new Dimension(200, 100);
		listScrollPane.setMinimumSize(minimumSize);
		rightScrollPane.setMinimumSize(minimumSize);

		//Provide a preferred size for the split pane.
		splitPane.setPreferredSize(new Dimension(1000, 400));

		// show the first help topic
		showHelp(list.getSelectedIndex());
		return splitPane;
	}

	// close the window
	private static void closeWindow(){
		if(helpAlreadyShown){
			helpAlreadyShown = false;
			frame.dispose();
		}
	}

	// create and show the GUI
	private static void createAndShowGUI(){
		// used for check to see if a help window is already open
		helpAlreadyShown = true;
		
		// create the frame and set the close operation to do nothing
		frame = new JFrame("Help");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// add the window listener and call closeWindow when a windowclosing event occurs
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				closeWindow();
			}
		});

		// create the help panel and add it to the frame
		Help help = new Help(); 
		frame.add(help);

		// add the ctmm icon to the frame
		URL url = FileOperations.getImageURL("images/ctmmSymbol.jpg");
		ImageIcon img = new ImageIcon(url);
		frame.setIconImage(img.getImage());

		//Display the window.
		frame.pack();
		frame.setVisible(true);

		//		System.out.println(Help.class.getResource("/").getPath());
	}

	// show help, based on the selected index
	private void showHelp(int index){
		rightArea.setText(helpTexts[index]);
		rightArea.setCaretPosition(0);
	}

	// event listener for list
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList)e.getSource();
		showHelp(list.getSelectedIndex());
	}

	// setup the help topics
	private static String [] setupHelpTopics(){
		helpTopics = new String[3];
		helpTopics[0] = "General";
		helpTopics[1] = "Rules file";
		helpTopics[2] = "ViewCRFVersion File";
		return helpTopics;
	}
	
	// setup the help texts
	private static String [] setupHelpTexts(){
		helpTexts = new String[helpTopics.length];
		helpTexts[0] = getText0();
		helpTexts[1] = getText1();
		helpTexts[2] = getText2();
		return helpTexts;
	}

	// first text
	// maybe we should put the text in files and read these?
	private static String getText0(){
		String text="";
		try{
			String file = FileOperations.getURLString("workflow.jpg");
			String file2 = FileOperations.getURLString("ctmmTrait.jpg");
			String file3 = FileOperations.getURLString("ctmmTracer.jpg");

			text="<html><head></head><body>"+
			"<h1>General Introduction</h1>"+
			"If you're familiar with designing your own rules in OpenClinica, you're aware of OpenClinica's policy to "+
			"generate its OIDs randomly. This implies that, when you upload the same CRF to a different server,"+
			"all the items get different OIDs. As a results, you will have to update all the OIDs in your rule files."+
			"This is can be extremely time-consuming and it may well introduce mistakes if you just accidentally make a"+
			"replacement error.<br>"+
			"This program was designed to help when you have to replace all these OIDs. It was originally created<br>"+
			"for CTMM TRACER where it was extremely successful.<br>"+
			"However, for the program to be able to work, there are several requirements:"+
			"<ul>" +
			"<li>The rules file contains rules for only 1 CRF" +
			"<li>For this rules you need to download the new ViewCRFVersion file from you OC server" +
			"<li>The rules file needs to contain a header which contains the mapping" +
			"</ul><br>"+
			"<img border=\"0\" src=\""+file+"\" alt=\"Workflow\"><br>"+
			"What the program does is the following: "+
			"<ul>" +
			"<li>Parse the ViewCRFVersion file to create a mapping between ItemName and New OID" +
			"<li>Parse the header of your rule file to create a mapping between the ItemName and Old OID" +
			"<li>Parse the body of your rule file and change Old OID to New OID via the ItemName" +
			"</ul><br>"+
			"Please check out the other help topics if more information is required.<br><br>"+
			"<img border=\"0\" src=\""+file2+"\" alt=\"traitLogo\">"+
			"<img border=\"0\" src=\""+file3+"\" alt=\"tracerLogo\"><br>"+
			"</body></html>";

		} catch(Exception e){
			System.out.println("couldn't find picture");
			System.out.println(Help.class.getResource("/").getPath());	
		}

		return text;
	}

	// second text
	private static String getText1(){
		String text="";
		try{
			String file = FileOperations.getURLString("header.jpg");
			String file2 = FileOperations.getURLString("body.jpg");

			text="<html><head></head><body>"+
			"<h1>Rules file</h1>"+
			"<h2>Header</h2>"+
			"The rules file has to contain a mapping of the item name to the OID (as specified on the old server).<br>"+
			"For example:<br>"+
			"<img border=\"0\" src=\""+file+"\" alt=\"Example Header\"><br>"+
			"The header (the blue text) is specified between the comment tags <!-- --><br>"+
			"The program then searcher the header for the pattern: <i>\\s+(\\S+)\\s+(\\S+)\\s*</i>, which basically " +
			"means whitespaces - nonwhitespaces - whitespaces - nonwhitespaces - (whitespaces)<br>"+
			"Hence, all the lines are skipped until we reach our first item which matches this pattern, LeeftijdMenoPauze. "+
			"The mapping ItemName to ItemOID is stored by the program and later used to translate the ItemOID to the new "+
			"OID<br><br>"+
            "<b><u>Your identifier lines have to start with a whitespace/tab, followed by the name, followed by a whitespace/tab, followed by the OID</u></b>"+
			"<h2>Body</h2>"+
			"The program parses the body by applying the following pattern: <i>.*\\b((I|IG)_\\w+)\\b.*</i>, which basically means"+
			"(anything) - word boundary - a word that starts with either I_ or IG_ - nonwhitespaces - word boundary - (anything)<br>"+
			"This should capture the ItemOIDs specified in the body of your xml file."+
			"A rule in TRACER look like this:<br>"+
			"<img border=\"0\" src=\""+file2+"\" alt=\"Example Body\"<br>"+
			"</body></html>";

		} catch(Exception e){
			System.out.println("couldn't find picture");
			System.out.println(Help.class.getResource("/").getPath());	
		}
		return text;
	}

	// third text
	private static String getText2(){
		String text="";
		try{

			String file = FileOperations.getURLString("DownloadCRFVersion.jpg");

			text="<html><head></head><body>"+
			"<h1>ViewCRFVersion File</h1>"+
			"The ViewCRFVersion file is required as the ruletranslator needs to know the new OIDs "+
			"for the itemNames for the CRF. The file can be found as follows:<br>" +
			"<ul>" +
			"<li>Go to Manage Case Report Forms screen" +
			"<li>Clicking View on the Original version of your CRF" +
			"<li>Right-click the &lt;..&gt; of the appropriate version and select Save Link As..." +
			"</ul><br>"+
			"<img border=\"0\" src=\""+file+"\" alt=\"Download ViewCRFVersion\"<br>"+
			"</body></html>";
		} catch(Exception e){
			System.out.println("couldn't find picture");
			System.out.println(Help.class.getResource("/").getPath());	
		}

		return text;

	}

	// show help
	public static void showHelp(){
		// check whether a helpwindow already exists
		if(!helpAlreadyShown){
			javax.swing.SwingUtilities.invokeLater(Help::createAndShowGUI);
		}
	}

    private JEditorPane rightArea;

	private static JFrame frame;
	private static boolean helpAlreadyShown = false;
	private static String[] helpTopics = setupHelpTopics();
	private static String[] helpTexts = setupHelpTexts();

	private static final long serialVersionUID = 331623305138507005L;

}

