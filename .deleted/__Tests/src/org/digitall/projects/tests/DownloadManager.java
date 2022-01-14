package org.digitall.projects.tests;

/**
 * Download Manager
 *
 * Save-Listing1 as Download.java, Listing 2 as DownloadManager.java,
   Listing 3 as DownloadsTableModel.java, Listing 4 as ProgressRenderer.java.
   Then Compile javac DownloadManager.java DownloadsTableModel.java ProgressRenderer.java Download.java
  & then javaw DownloadManager
 */
// This class downloads a file from a URL.
//listing 2
import java.awt.*;
import java.awt.event.*;

import java.io.File;

import java.net.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * Download Manager
 *
 * Save-Listing1 as Download.java, Listing 2 as DownloadManager.java,
   Listing 3 as DownloadsTableModel.java, Listing 4 as ProgressRenderer.java.
   Then Compile javac DownloadManager.java DownloadsTableModel.java ProgressRenderer.java Download.java
  & then javaw DownloadManager
 */
public class
// The Download Manager.
DownloadManager extends JFrame implements Observer {

    // Add download text field.
    private JTextField addTextField;
    // Download table's data model.
    private DownloadsTableModel tableModel;
    // Table showing downloads.
    private JTable table;
    // These are the buttons for managing the selected download.
    private JButton pauseButton, resumeButton;
    private JButton cancelButton, clearButton, md5checksums;
    // Currently selected download.
    private Download selectedDownload;
    // Flag for whether or not table selection is being cleared.
    private boolean clearing;
    // Constructor for Download Manager.

    public DownloadManager() {
	// Set application title.
	setTitle("Download Manager");
	// Set window size.
	setSize(640, 480);
	// Handle window closing events.
	addWindowListener(new WindowAdapter() {

		       public void windowClosing(WindowEvent e) {
			   actionExit();
		       }

		   }
	);
	// Set up file menu.
	JMenuBar menuBar = new JMenuBar();
	JMenu fileMenu = new JMenu("File");
	fileMenu.setMnemonic(KeyEvent.VK_F);
	JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_X);
	fileExitMenuItem.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
					    actionExit();
					}

				    }
	);
	fileMenu.add(fileExitMenuItem);
	menuBar.add(fileMenu);
	setJMenuBar(menuBar);
	// Set up add panel.
	JPanel addPanel = new JPanel();
	addTextField = new JTextField(30);
	addPanel.add(addTextField);
	JButton addButton = new JButton("Add Download");
	addButton.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     actionAdd();
				 }

			     }
	);
	addPanel.add(addButton);
	// Set up Downloads table.
	tableModel = new DownloadsTableModel();
	table = new JTable(tableModel);
	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

							public void valueChanged(ListSelectionEvent e) {
							    tableSelectionChanged();
							}

						    }
	);
	// Allow only one row at a time to be selected.
	table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	// Set up ProgressBar as renderer for progress column.
	ProgressRenderer renderer = new ProgressRenderer(0, 100);
	renderer.setStringPainted(true);
	// show progress text
	table.setDefaultRenderer(JProgressBar.class, renderer);
	// Set table's row height large enough to fit JProgressBar.
	table.setRowHeight((int)renderer.getPreferredSize().getHeight());
	// Set up downloads panel.
	JPanel downloadsPanel = new JPanel();
	downloadsPanel.setBorder(BorderFactory.createTitledBorder("Downloads"));
	downloadsPanel.setLayout(new BorderLayout());
	downloadsPanel.add(new JScrollPane(table), BorderLayout.CENTER);
	// Set up buttons panel.
	JPanel buttonsPanel = new JPanel();
	pauseButton = new JButton("Pause");
	pauseButton.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       actionPause();
				   }

			       }
	);
	pauseButton.setEnabled(false);
	buttonsPanel.add(pauseButton);
	resumeButton = new JButton("Resume");
	resumeButton.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					actionResume();
				    }

				}
	);
	resumeButton.setEnabled(false);
	buttonsPanel.add(resumeButton);
	cancelButton = new JButton("Cancel");
	cancelButton.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					actionCancel();
				    }

				}
	);
	cancelButton.setEnabled(false);
	buttonsPanel.add(cancelButton);
	clearButton = new JButton("Clear");
	clearButton.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       actionClear();
				   }

			       }
	);
	clearButton.setEnabled(false);
	buttonsPanel.add(clearButton);
	md5checksums = new JButton("MD5!");
	md5checksums.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					createMD5Checksums();
				    }

				}
	);
	buttonsPanel.add(md5checksums);
	// Add panels to display.
	getContentPane().setLayout(new BorderLayout());
	getContentPane().add(addPanel, BorderLayout.NORTH);
	getContentPane().add(downloadsPanel, BorderLayout.CENTER);
	getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
	addTextField.setText("http://ftp.gnu.org/gnu/automake/automake-1.10.tar.bz2");
    }
    // Exit this program.

    private void actionExit() {
	System.exit(0);
    }
    // Add a new download.

    private void actionAdd() {
	URL verifiedUrl = verifyUrl(addTextField.getText());
	if (verifiedUrl != null) {
	    tableModel.addDownload(new Download(verifiedUrl));
	    addTextField.setText("");
	    addTextField.setText("http://ftp.gnu.org/gnu/automake/automake-1.10.tar.bz2");
	    // reset add text field
	} else {
	    JOptionPane.showMessageDialog(this, "Invalid Download URL", "Error", JOptionPane.ERROR_MESSAGE);
	}
    }
    // Verify download URL.

    private URL verifyUrl(String url) {
	// Only allow HTTP URLs.
	if (!url.toLowerCase().startsWith("http://"))
	    return null;
	// Verify format of URL.
	URL verifiedUrl = null;
	try {
	    verifiedUrl = new URL(url);
	} catch (Exception e) {
	    return null;
	}
	// Make sure URL specifies a file.
	if (verifiedUrl.getFile().length() < 2)
	    return null;
	return verifiedUrl;
    }
    // Called when table row selection changes.

    private void tableSelectionChanged() {
	/* Unregister from receiving notifications
       from the last selected download. */
	if (selectedDownload != null)
	    selectedDownload.deleteObserver(DownloadManager.this);
	/* If not in the middle of clearing a download,
       set the selected download and register to
       receive notifications from it. */
	if (!clearing && table.getSelectedRow() > -1) {
	    selectedDownload = tableModel.getDownload(table.getSelectedRow());
	    selectedDownload.addObserver(DownloadManager.this);
	    updateButtons();
	}
    }
    // Pause the selected download.

    private void actionPause() {
	selectedDownload.pause();
	updateButtons();
    }
    // Resume the selected download.

    private void actionResume() {
	selectedDownload.resume();
	updateButtons();
    }
    // Cancel the selected download.

    private void actionCancel() {
	selectedDownload.cancel();
	updateButtons();
    }
    // Clear the selected download.

    private void actionClear() {
	clearing = true;
	tableModel.clearDownload(table.getSelectedRow());
	clearing = false;
	selectedDownload = null;
	updateButtons();
    }
    /* Update each button's state based off of the
     currently selected download's status. */

    private void updateButtons() {
	if (selectedDownload != null) {
	    int status = selectedDownload.getStatus();
	    switch (status) {
		case Download.DOWNLOADING :
		    pauseButton.setEnabled(true);
		    resumeButton.setEnabled(false);
		    cancelButton.setEnabled(true);
		    clearButton.setEnabled(false);
		    break;
		case Download.PAUSED :
		    pauseButton.setEnabled(false);
		    resumeButton.setEnabled(true);
		    cancelButton.setEnabled(true);
		    clearButton.setEnabled(false);
		    break;
		case Download.ERROR :
		    pauseButton.setEnabled(false);
		    resumeButton.setEnabled(true);
		    cancelButton.setEnabled(false);
		    clearButton.setEnabled(true);
		    break;
		case Download.INITIATING :
		    pauseButton.setEnabled(false);
		    resumeButton.setEnabled(true);
		    cancelButton.setEnabled(true);
		    clearButton.setEnabled(false);
		    break;
		default :
		    // COMPLETE or CANCELLED
		    pauseButton.setEnabled(false);
		    resumeButton.setEnabled(false);
		    cancelButton.setEnabled(false);
		    clearButton.setEnabled(true);
	    }
	} else {
	    // No download is selected in table.
	    pauseButton.setEnabled(false);
	    resumeButton.setEnabled(false);
	    cancelButton.setEnabled(false);
	    clearButton.setEnabled(false);
	}
    }
    /* Update is called when a Download notifies its
     observers of any changes. */

    public void update(Observable o, Object arg) {
	// Update buttons if the selected download has changed.
	if (selectedDownload != null && selectedDownload.equals(o))
	    updateButtons();
    }
    // Run the Download Manager.

    public static void main(String[] args) {
	DownloadManager manager = new DownloadManager();
	manager.setVisible(true);
    }

    public void createMD5Checksums() {
	String path = "/home/santiago/jdevhome/mywork/DDesktop/.output/";
	String xmlChecksums = "<?xml version=\"1.0\"?>\n";
	xmlChecksums += "<filelist>\n";
	xmlChecksums += createMD5Checksums(path, path);
	xmlChecksums += "</filelist>\n";
	System.out.println(xmlChecksums);
    }

    public String createMD5Checksums(String root, String path) {
	String checksums = "";
	File file = new File(path);
	File[] fileList = file.listFiles();
	for (int i = 0; i < fileList.length; i++) {
	    if (fileList[i].isDirectory()) {
		//System.out.println(fileList[i].getAbsolutePath());
		checksums += createMD5Checksums(root, fileList[i].getAbsolutePath());
	    } else if (fileList[i].isFile()) {
		try {
		    checksums += "  <file>\n";
		    checksums += "    <filename>" + fileList[i].getName() + "</filename>\n";
		    checksums += "    <path>" + fileList[i].getAbsolutePath().replaceAll(root, "") + "</path>\n";
		    checksums += "    <hash>" + MD5Checksum.getMD5Checksum(fileList[i]) + "</hash>\n";
		    checksums += "  </file>\n";
		} catch (Exception e) {
		    // TODO
		    System.out.println("Error creating MD5 checksums for file " + fileList[i].getAbsolutePath());
		}
	    }
	}
	return checksums;
    }
    /*
<?xml version="1.0"?>
<company>
	 <employee>
		 <firstname>Paul</firstname>
		 <lastname>Enderson</lastname>
	 </employee>
</company>

 import java.io.File;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;

 public class XMLReader {

  public static void main(String argv[]) {

   try {
   File file = new File("c:\\MyXMLFile.xml");
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   DocumentBuilder db = dbf.newDocumentBuilder();
   Document doc = db.parse(file);
   doc.getDocumentElement().normalize();
   System.out.println("Root element " + doc.getDocumentElement().getNodeName());
   NodeList nodeLst = doc.getElementsByTagName("employee");
   System.out.println("Information of all employees");

   for (int s = 0; s < nodeLst.getLength(); s++) {

     Node fstNode = nodeLst.item(s);
     
     if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
   
	    Element fstElmnt = (Element) fstNode;
       NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("firstname");
       Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
       NodeList fstNm = fstNmElmnt.getChildNodes();
       System.out.println("First Name : "  + ((Node) fstNm.item(0)).getNodeValue());
       NodeList lstNmElmntLst = fstElmnt.getElementsByTagName("lastname");
       Element lstNmElmnt = (Element) lstNmElmntLst.item(0);
       NodeList lstNm = lstNmElmnt.getChildNodes();
       System.out.println("Last Name : " + ((Node) lstNm.item(0)).getNodeValue());
     }

   }
   } catch (Exception e) {
     e.printStackTrace();
   }
  }
 }
  
 
 */

}
