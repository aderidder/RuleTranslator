package ruletranslator.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import ruletranslator.shared.FileOperations;
import ruletranslator.translate.XMLTranslate;

// Graphical User Interface. 
// Contains main

// The GUI is a JPanel
public class TranslateGUI extends JPanel implements ActionListener {
	TranslateGUI(){
		// Setup our panel with a borderlayout
		super(new BorderLayout());
		setup();
	}

	private void setup(){
		//Create a file chooser and filters for xml and html
		xmlFilter = new FileNameExtensionFilter("xml files", "xml");
		htmlFilter = new FileNameExtensionFilter("html files", "htm", "html");

		// create the uneditable log area with a scroll pane
		logArea = new JTextArea(30,150);
		logArea.setEditable(false);
		logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		JScrollPane logScrollPane = new JScrollPane(logArea);

		// Create the top panel and the bottom panel
		JPanel topPanel = topPaneSetup();
		JPanel bottomPanel = bottomPaneSetup();

		// Add the panels to the borderlayout
		add(topPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.PAGE_END);

	}

	// create the menubar
	private JMenuBar createMenuBar(){
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;
		menuBar = new JMenuBar();

		// Build the File menu
		menu = new JMenu("File");
		menuItem = new JMenuItem("Exit");
		menuItem.setActionCommand("Exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		// Build the About menu
		menu = new JMenu("About");
		menuItem = new JMenuItem("Help");
		menuItem.setActionCommand("Help");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("About");
		menuItem.setActionCommand("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);

		// Return the menuBar
		return menuBar;
	}

	// create the bottom panel
	private JPanel bottomPaneSetup(){
		JPanel buttonPanel = new JPanel();
		JButton button;

		// create the Run Button
		button = new JButton("Run");
		button.setPreferredSize(new Dimension(150,30));
		button.setActionCommand("Run");
		button.addActionListener(this);
		buttonPanel.add(button);

		// create the Exit Button
		button = new JButton("Exit");
		button.setPreferredSize(new Dimension(150,30));
		button.setActionCommand("Exit");
		button.addActionListener(this);
		buttonPanel.add(button);

		return buttonPanel;
	}

	// create standard GridBagConstraints
	private static GridBagConstraints getDefaultConstraints(){
		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		// set the xweight for the component. As all components (unless overwritten) have 
		// the same weight, they will all have the same sizes  
		gridBagConstraints.weightx = 0.5;
		// margins
		gridBagConstraints.insets = new Insets(3,3,3,3);
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		return gridBagConstraints;
	}

	// create the top Panel
	private JPanel topPaneSetup(){
		JPanel pane = new JPanel();
		JButton button;
		JLabel label;

		GridBagConstraints gridBagConstraints;

		// set the layout to a gridbaglayout
		pane.setLayout(new GridBagLayout());

		// text label
		label = new JLabel("Rule File");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		pane.add(label, gridBagConstraints);

		// textfield
		ruleFileTextField = new JTextField(20);
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		pane.add(ruleFileTextField, gridBagConstraints);

		// button
		button = new JButton("Browse");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 0.2; // smaller weight
		button.setActionCommand("BrowseRuleFile");
		button.addActionListener(this);
		pane.add(button, gridBagConstraints);

		// text label
		label = new JLabel("ViewCRFVersion File");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		pane.add(label, gridBagConstraints);

		// textfield
		crfVersionFileTextField = new JTextField(20);
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		pane.add(crfVersionFileTextField, gridBagConstraints);

		// button
		button = new JButton("Browse");
		gridBagConstraints = getDefaultConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.weightx = 0.2; // smaller weight
		button.setActionCommand("BrowseViewCRFFile");
		button.addActionListener(this);
		pane.add(button, gridBagConstraints);

		return pane;
	}

	// create and show the GUI
	private static void createAndShowGUI() {
		// create the frame
		JFrame frame = new JFrame("Rule Translator");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// create our panel and add it to the frame
		TranslateGUI translateGUI = new TranslateGUI(); 
		frame.add(translateGUI);
		// add the menubar to the frame
		frame.setJMenuBar(translateGUI.createMenuBar());
		
		// change the icon to a ctmm icon 
		URL url = FileOperations.getImageURL("ctmmSymbol.jpg");

		ImageIcon img = new ImageIcon(url);
		frame.setIconImage(img.getImage());

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	// create a new filechooser based on the directory and the filter
	private JFileChooser getFileChooser(FileNameExtensionFilter filter){
		JFileChooser fileChooser = new JFileChooser(dir);
		fileChooser.setFileFilter(filter);
		return fileChooser;
	}

	// browse file
	private void browseFile(JTextField textField, JFileChooser fileChooser){
		// show the fileChooser
		int returnVal = fileChooser.showOpenDialog(this);

		// if ok is pressed, set the textfield to the selected file and store the directory 
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile(); 
			textField.setText(selectedFile.getAbsolutePath());
			dir = selectedFile.getParent();
		} else {
			logArea.append("Choose command cancelled by user" + newline);
		}
	}

	// check whether both the crfVersionFileTextField and the ruleFileTextField exist 
	private boolean fileExists(String fileName){
		if(!FileOperations.fileExists(fileName)){
			JOptionPane.showMessageDialog(this, "The file "+fileName+" does not exist", "There was a problem", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	// run the translator
	private void runProgram(){
		String ruleFile = ruleFileTextField.getText().trim();
		String crfVersionFile = crfVersionFileTextField.getText().trim();
		// check whether both the crfVersionFileTextField and the ruleFileTextField exist
		if(fileExists(ruleFile)&&fileExists(crfVersionFile)){
			logArea.append("Running ruletranslator..."+newline);
			// ensure the directory variable is set properly 
			dir = ruleFile.substring(0,ruleFile.lastIndexOf(fileSeparator));
            // create the translator object

			try{
                XMLTranslate xmlTranslate = new XMLTranslate(ruleFile, crfVersionFile, logArea);
                // try to translate and display message if successful
				xmlTranslate.runTranslate();
				JOptionPane.showMessageDialog(this, "Rule Translation Finished."+newline+
						"Translated file: "+xmlTranslate.getTranslatedFileName()+newline+
						"LogFile: "+xmlTranslate.getLogFileName());

			} catch(Exception e){
				// show message if not successful
				logArea.append(e.toString());
				JOptionPane.showMessageDialog(this, "The rule translator encountered a problem:"+newline+e.toString(),
						"There was a problem", JOptionPane.ERROR_MESSAGE);
			}
		}
		else{
			logArea.append("Please specify a valid Rules file and a ViewCRFFile" + newline);
		}

	}

	// show about information
	private void showAbout(){
		// create image
		URL url = FileOperations.getImageURL("ctmm.jpg");
		ImageIcon img = new ImageIcon(url);
		// show dialog
		JOptionPane.showMessageDialog(this, 
			"This program was created for CTMM TRACER (WP12 ICT, "+newline+
			"developer: Sander de Ridder, tester: Rinus Voorham, "+newline+
			"WP-leader: Jeroen Beli�n) and is now supported by "+newline+
			"CTMM TraIT."+newline+
			"Please contact Marinel Cavelaars (mn.cavelaars@vumc.nl)"+newline+
			"for questions\n\nProgram version "+version, "About"
			, JOptionPane.INFORMATION_MESSAGE, img);
	}

	// show help
	private void showHelp(){
		Help.showHelp();
	}

	// handle actions
	public void actionPerformed(ActionEvent e) {
		// Handle open browse button for rules file.
		if (e.getActionCommand().equalsIgnoreCase("BrowseRuleFile")) {
			JFileChooser fileChooser = getFileChooser(xmlFilter);
			browseFile(ruleFileTextField, fileChooser);
		}
		// Handle open browse button for crfVersion file.
		else if (e.getActionCommand().equalsIgnoreCase("BrowseViewCRFFile")) {
			JFileChooser fileChooser = getFileChooser(htmlFilter);
			browseFile(crfVersionFileTextField, fileChooser);
		}
		// Handle run
		else if (e.getActionCommand().equalsIgnoreCase("Run")) {
			runProgram();
		}
		// Handle exit
		else if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			System.exit(0);
		}
		// Handle about
		else if (e.getActionCommand().equalsIgnoreCase("About")) {
			showAbout();
		}
		// Handle help
		else if (e.getActionCommand().equalsIgnoreCase("Help")) {
			showHelp();
		}
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}

	// main
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(TranslateGUI::createAndShowGUI);
	}

	private String dir="";
	private JTextField ruleFileTextField, crfVersionFileTextField;
	private JTextArea logArea;
	private FileNameExtensionFilter xmlFilter;
	private FileNameExtensionFilter htmlFilter;

	private static final String newline = System.getProperty("line.separator");
	private static final String fileSeparator = System.getProperty("file.separator");
	private static final long serialVersionUID = -7755429999545798394L;
	private static final double version = 0.9;
}

