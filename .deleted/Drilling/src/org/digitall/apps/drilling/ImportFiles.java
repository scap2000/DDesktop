package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DecimalFormat;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTable;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.components.buttons.UnAssignButton;
import org.digitall.lib.icons.IconTypes;

//

public class ImportFiles extends BasicDialog {

    private BasicButton btnOpenFile = new BasicButton();
    private BasicTextField tfFileName = new BasicTextField();
    private BasicScrollPane scpFileContent = new BasicScrollPane();
    private DefaultTableModel tableModel = new DefaultTableModel();
    private BasicTable jtFileContent = new BasicTable(tableModel);
    private BasicScrollPane scpFileHeader = new BasicScrollPane(jtFileContent);
    private JTextArea taFileHeader = new JTextArea();
    private Vector fileLines = new Vector();
    private JRadioButton rbTab = new JRadioButton();
    private JRadioButton rbComma = new JRadioButton();
    private JRadioButton rbDotComma = new JRadioButton();
    private JRadioButton rbSpace = new JRadioButton();
    private JRadioButton rbOther = new JRadioButton();
    private ButtonGroup optionsGroup = new ButtonGroup();
    private BasicTextField tfSeparator = new BasicTextField();
    private UnAssignButton addLinesToHeader = new UnAssignButton(true);
    private AssignButton addLinesToContent = new AssignButton(true);
    private final int linesLimit = 15;
    private final int totalLinesLimit = 32768;
    private int linesToHeader = 0;
    private int linesToContent = 0;
    private int totalLines = 0;
    private String separator = "\t";
    private SaveDataButton btnImport = new SaveDataButton();
    private CloseButton btnClose = new CloseButton();
    private File file;
    private JProgressBar pbImportStatus = new JProgressBar();
    private PanelHeader panelHeader;

    public ImportFiles() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(661, 585));
	panelHeader = new PanelHeader(this.getWidth(), "ASL Lab Files Importer", IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	this.setTitle("Lab Importer");
	btnOpenFile.setText("Abrir ASLab CSV");
	btnOpenFile.setBounds(new Rectangle(5, 55, 215, 20));
	btnOpenFile.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnOpenFile_actionPerformed(e);
		    }

		});
	tfFileName.setBounds(new Rectangle(225, 55, 420, 20));
	tfFileName.setEditable(false);
	tfFileName.setBackground(Color.white);
	scpFileContent.setBounds(new Rectangle(5, 300, 640, 225));
	scpFileHeader.setBounds(new Rectangle(5, 85, 640, 155));
	taFileHeader.setEditable(false);
	rbTab.setText("Tabulador:   \"\\t\"");
	rbComma.setText("Coma:   \",\"");
	rbDotComma.setText("Punto y Coma:   \";\"");
	rbSpace.setText("Espacio:  \" \"");
	rbOther.setText("Otros");
	rbTab.setBounds(new Rectangle(30, 250, 135, 20));
	rbComma.setBounds(new Rectangle(30, 270, 135, 20));
	rbComma.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			rbComma_actionPerformed(e);
		    }

		});
	rbDotComma.setBounds(new Rectangle(185, 250, 135, 20));
	rbDotComma.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			rbDotComma_actionPerformed(e);
		    }

		});
	rbSpace.setBounds(new Rectangle(185, 270, 135, 20));
	rbSpace.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			rbSpace_actionPerformed(e);
		    }

		});
	rbOther.setBounds(new Rectangle(340, 250, 135, 20));
	rbOther.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			rbOther_actionPerformed(e);
		    }

		});
	tfSeparator.setBounds(new Rectangle(340, 270, 135, 20));
	tfSeparator.addKeyListener(new KeyAdapter() {

		    public void keyTyped(KeyEvent e) {
			tfSeparator_keyTyped(e);
		    }

		});
	addLinesToHeader.setBounds(new Rectangle(555, 255, 40, 25));
	addLinesToHeader.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			addLinesToHeader_actionPerformed(e);
		    }

		});
	addLinesToContent.setBounds(new Rectangle(605, 255, 40, 25));
	addLinesToContent.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			addLinesToContent_actionPerformed(e);
		    }

		});
	btnImport.setBounds(new Rectangle(5, 530, 40, 25));
	btnImport.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnImport_actionPerformed(e);
		    }

		});
	btnClose.setBounds(new Rectangle(605, 530, 40, 25));
	btnClose.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnClose_actionPerformed(e);
		    }

		});
	pbImportStatus.setBounds(new Rectangle(290, 535, 150, 15));
	optionsGroup.add(rbTab);
	optionsGroup.add(rbComma);
	optionsGroup.add(rbDotComma);
	optionsGroup.add(rbSpace);
	optionsGroup.add(rbOther);
	rbTab.setSelected(true);
	rbTab.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			rbTab_actionPerformed(e);
		    }

		});
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(pbImportStatus, null);
	this.getContentPane().add(btnImport, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(addLinesToContent, null);
	this.getContentPane().add(addLinesToHeader, null);
	this.getContentPane().add(tfSeparator, null);
	this.getContentPane().add(rbOther, null);
	this.getContentPane().add(rbSpace, null);
	this.getContentPane().add(rbDotComma, null);
	this.getContentPane().add(rbComma, null);
	this.getContentPane().add(rbTab, null);
	scpFileHeader.getViewport().add(taFileHeader, null);
	this.getContentPane().add(scpFileHeader, null);
	scpFileContent.getViewport().add(jtFileContent);
	this.getContentPane().add(scpFileContent, null);
	this.getContentPane().add(tfFileName, null);
	this.getContentPane().add(btnOpenFile, null);
	tfSeparator.setEditable(false);
	pbImportStatus.setEnabled(false);
	pbImportStatus.setVisible(false);
	pbImportStatus.setString("");
	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    public File openFile() {
	JFileChooser selector = new JFileChooser();
	selector.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	selector.setFileFilter(new CSVFileFilter());
	int opcion = selector.showOpenDialog(null);
	if (opcion == JFileChooser.APPROVE_OPTION) {
	    tfFileName.setText(selector.getSelectedFile().getPath());
	    return selector.getSelectedFile();
	} else {
	    return null;
	}
    }

    private void btnOpenFile_actionPerformed(ActionEvent e) {
	file = openFile();
	if (file != null) {
	    if (file.canRead()) {
		//File selected and readable, so... do something!
		doPreImporting();
	    } else {
		System.out.println("File not readable");
		file = null;
	    }
	} else {
	    System.out.println("File not selected");
	    file = null;
	}
    }

    private void doPreImporting() {
	try {
	    if (file != null) {
		BufferedReader _inputBufferedReader = new BufferedReader(new FileReader(file));
		linesToHeader = 7;
		linesToContent = linesLimit - linesToHeader;
		String linea;
		int lines = 0;
		totalLines = 0;
		fileLines.removeAllElements();
		while (lines < linesLimit) {
		    if ((linea = _inputBufferedReader.readLine()) != null) {
			lines++;
			totalLines++;
			fileLines.add(new String(linea));
		    }
		}
		while ((linea = _inputBufferedReader.readLine()) != null) {
		    totalLines++;
		}
		loadFileContent();
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    // TODO
	}
    }

    private void loadFileContent() {
	int i = 0;
	taFileHeader.setText("");
	while (i < fileLines.size() & i < linesToHeader) {
	    taFileHeader.append(((String)fileLines.elementAt(i)).replaceAll(separator, "") + "\n");
	    i++;
	}
	tableModel.setColumnCount(0);
	tableModel.setRowCount(0);
	if (i < fileLines.size()) {
	    String[] firstLine = ((String)fileLines.elementAt(i)).split(separator);
	    tableModel.addColumn("SAMPLE #");
	    for (int j = 1; j < firstLine.length; j++) {
		tableModel.addColumn(firstLine[j]);
		jtFileContent.getColumnModel().getColumn(j).setWidth(20);
	    }
	    i++;
	    while (i < fileLines.size()) {
		tableModel.addRow(((String)fileLines.elementAt(i)).split(separator));
		i++;
	    }
	}
	jtFileContent.setAutoResizeMode(BasicTable.AUTO_RESIZE_OFF);
	jtFileContent.getTableHeader().setReorderingAllowed(false);
	jtFileContent.getTableHeader().setResizingAllowed(true);
    }

    private void addLinesToHeader_actionPerformed(ActionEvent e) {
	if (linesToHeader + 1 <= linesLimit && linesToContent - 1 >= 0) {
	    linesToHeader++;
	    linesToContent--;
	}
	loadFileContent();
    }

    private void addLinesToContent_actionPerformed(ActionEvent e) {
	if (linesToContent + 1 <= linesLimit && linesToHeader - 1 >= 0) {
	    linesToContent++;
	    linesToHeader--;
	}
	loadFileContent();
    }

    private void rbTab_actionPerformed(ActionEvent e) {
	separator = "\t";
	tfSeparator.setEditable(false);
	loadFileContent();
    }

    private void rbComma_actionPerformed(ActionEvent e) {
	separator = ",";
	tfSeparator.setEditable(false);
	loadFileContent();
    }

    private void rbDotComma_actionPerformed(ActionEvent e) {
	separator = ";";
	tfSeparator.setEditable(false);
	loadFileContent();
    }

    private void rbSpace_actionPerformed(ActionEvent e) {
	separator = " ";
	tfSeparator.setEditable(false);
	loadFileContent();
    }

    private void tfSeparator_keyTyped(KeyEvent e) {
	if (e.getKeyChar() == '\n' && tfSeparator.getText().length() > 0) {
	    separator = tfSeparator.getText();
	    loadFileContent();
	}
    }

    private void rbOther_actionPerformed(ActionEvent e) {
	tfSeparator.setEditable(true);
    }

    private void btnImport_actionPerformed(ActionEvent e) {
	importFile();
    }

    private void importFile() {
	Thread doImport = new Thread(new Runnable() {

		    public void run() {
			btnImport.setEnabled(false);
			btnOpenFile.setEnabled(false);
			btnClose.setEnabled(false);
			pbImportStatus.setVisible(true);
			pbImportStatus.setEnabled(true);
			pbImportStatus.setStringPainted(true);
			pbImportStatus.setMaximum(100);
			pbImportStatus.setValue(0);
			try {
			    if (file != null) {
				BufferedReader _inputBufferedReader = new BufferedReader(new FileReader(file));
				linesToContent = linesLimit - linesToHeader;
				String _line;
				int lines = 0;
				String header = new String();
				while (lines < linesToHeader) {
				    if ((_line = _inputBufferedReader.readLine()) != null) {
					lines++;
					header += _line.replaceAll(separator, "") + "\n";
				    }
				}
				boolean loadAnyway = true;
				System.out.println("select drilling.existsLabImport('" + file.getName() + "'," + file.length() + ")");
				ResultSet labImport = org.digitall.lib.sql.LibSQL.exQuery("select drilling.existsLabImport('" + file.getName() + "'," + file.length() + ")");
				try {
				    if (labImport.next()) {
					if (labImport.getBoolean("existsLabImport")) {
					    loadAnyway = askLoadAnyway();
					}
				    }
				} catch (SQLException x) {
				    x.printStackTrace();
				}
				if (loadAnyway) {
				    String idLabImport = org.digitall.lib.sql.LibSQL.getCampo("select drilling.newLabImport('" + file.getName() + "','" + header + "'," + file.length() + ")");
				    //System.out.println(idLabImport);
				    //LISTADO DE ENSAYOS, UNIDADES Y MINERALES
				    String[] _tests = new String[0];
				    String[] _units = new String[0];
				    String[] _minerals = new String[0];
				    int _hLines = 0;
				    while (_hLines < 3) {
					if (((_line = _inputBufferedReader.readLine()) != null)) {
					    lines++;
					    //System.out.println("Linea: " + linea);
					    if (_hLines == 0) {
						_tests = _line.split(separator);
					    } else if (_hLines == 1) {
						_minerals = _line.split(separator);
					    } else if (_hLines == 2) {
						_units = _line.split(separator);
					    }
					    _hLines++;
					}
				    }
				    if (_tests.length > 0) {
					while (((_line = _inputBufferedReader.readLine()) != null) && (lines < totalLinesLimit)) {
					    pbImportStatus.setValue(lines * 100 / totalLines);
					    double pbValue = 100.0 * ((double)lines / (double)totalLines);
					    pbImportStatus.setString((new DecimalFormat("#0.00").format(pbValue)) + "%");
					    lines++;
					    String[] _row = _line.split(separator);
					    for (int i = 1; i < _tests.length; i++) {
						String _amount_orig = "";
						if (i >= _row.length) {
						    _amount_orig = "N/A";
						} else {
						    _amount_orig = _row[i];
						}
						double _amount = 0;
						try {
						    _amount = Double.parseDouble(0 + _amount_orig.replaceAll(",", "."));
						} catch (NumberFormatException x) {
						    //TODO
						}
						String _query = "select drilling.insertLabResult(" + idLabImport + ",'" + _row[0] + "','";
						_query += _tests[i] + "','" + _minerals[i] + "','" + _units[i] + "'," + _amount + ",'" + _amount_orig + "')";
						org.digitall.lib.sql.LibSQL.exQuery(_query);
					    }
					}
					pbImportStatus.setValue(100);
				    }
				} else {
				    //Operación cancelada!
				}
			    }
			} catch (IOException x) {
			    x.printStackTrace();
			}
			btnOpenFile.setEnabled(true);
			btnImport.setEnabled(true);
			btnClose.setEnabled(true);
			pbImportStatus.setEnabled(false);
			pbImportStatus.setVisible(false);
			pbImportStatus.setString("");
		    }

		});
	doImport.start();
    }

    public boolean askLoadAnyway() {
	String texto = "Ya se importo un archivo con\nnombre " + file.getName() + "\nlongitud: " + file.length() + "bytes" + "\n Importar archivo?";
	return ((JOptionPane.showConfirmDialog(this, texto, "Message", JOptionPane.OK_CANCEL_OPTION)) == JOptionPane.OK_OPTION);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	dispose();
    }

}
