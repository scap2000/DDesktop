package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

import org.digitall.apps.legalprocs.manager.FilesCadastralRegisterPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

//
//public class PanelFile extends BasicInternalFrame {

public class PanelFile extends BasicDialog {

    private FilesCadastralRegisterPanel filesCadastralRegisterPanel = new FilesCadastralRegisterPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private BasicPanel jpFile = new BasicPanel();
    private BasicPanel jpContainer = new BasicPanel();
    private BasicPanel jpCadastralRegister = new BasicPanel();
    private TFInputPanel tfFileNumber = new TFInputPanel(DataTypes.INTEGER, "FileNumber", false, true);
    private TFInputPanel tfFileLetter = new TFInputPanel(DataTypes.STRING, "FileLetter", false, true);
    private TFInputPanel tfFileYear = new TFInputPanel(DataTypes.INTEGER, "FileYear", false, true);
    private TFInputPanel tfStartAppDate = new TFInputPanel(DataTypes.DATE, "StartDate", false, true);
    private TFInputPanel tfFreeArea = new TFInputPanel(DataTypes.DOUBLE, "FreeArea", false, true);
    private TFInputPanel tfDate = new TFInputPanel(DataTypes.DATE, "Date", false, true);
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicLabel jLabel5 = new BasicLabel();
    private JRadioButton rbtnYes = new JRadioButton();
    private JRadioButton rbtnNo = new JRadioButton();
    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private JTextArea jTextArea1 = new JTextArea();
    private String title;
    private Manager mgmt;
    private boolean editableFields = false;

    public PanelFile(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelFile(String _title, Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    title = _title;
	    editableFields = true;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(613, 578));
	this.setTitle(title);
	jpContainer.setBounds(new Rectangle(0, 35, 610, 515));
	jpContainer.setLayout(null);
	jpCadastralRegister.setBounds(new Rectangle(10, 125, 585, 140));
	jpCadastralRegister.setLayout(null);
	jpCadastralRegister.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfFreeArea.setBounds(new Rectangle(20, 20, 115, 35));
	tfDate.setBounds(new Rectangle(20, 85, 115, 35));
	jLabel2.setText("Superposicion:");
	jLabel2.setBounds(new Rectangle(195, 20, 100, 20));
	jLabel2.setFont(new Font("Default", 1, 11));
	rbtnYes.setText("Si");
	rbtnYes.setBounds(new Rectangle(305, 20, 45, 20));
	rbtnYes.setSize(new Dimension(45, 18));
	rbtnYes.setFont(new Font("Default", 1, 11));
	rbtnNo.setText("No");
	rbtnNo.setBounds(new Rectangle(355, 20, 45, 20));
	rbtnNo.setSize(new Dimension(45, 18));
	rbtnNo.setFont(new Font("Default", 1, 11));
	jScrollPane1.setBounds(new Rectangle(195, 70, 365, 50));
	jLabel5.setText("Especificar:");
	jLabel5.setBounds(new Rectangle(195, 50, 75, 20));
	jLabel5.setSize(new Dimension(75, 20));
	jLabel5.setFont(new Font("Default", 1, 11));
	tfFileLetter.setBounds(new Rectangle(170, 30, 60, 35));
	tfFileYear.setBounds(new Rectangle(245, 30, 60, 35));
	tfStartAppDate.setBounds(new Rectangle(410, 30, 100, 35));
	tfFileNumber.setBounds(new Rectangle(20, 30, 130, 35));
	btnAccept.setBounds(new Rectangle(10, 477, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(555, 475, 40, 28));
	btnClose.setSize(new Dimension(40, 28));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	jpFile.setBounds(new Rectangle(10, 20, 585, 80));
	jpFile.setLayout(null);
	jpFile.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	filesCadastralRegisterPanel.setBounds(new Rectangle(10, 285, 585, 170));
	jpFile.add(tfStartAppDate, null);
	jpFile.add(tfFileYear, null);
	jpFile.add(tfFileLetter, null);
	jpFile.add(tfFileNumber, null);
	jpCadastralRegister.add(jLabel5, null);
	jScrollPane1.getViewport().add(jTextArea1, null);
	jpCadastralRegister.add(jScrollPane1, null);
	jpCadastralRegister.add(rbtnNo, null);
	jpCadastralRegister.add(rbtnYes, null);
	jpCadastralRegister.add(jLabel2, null);
	jpCadastralRegister.add(tfDate, null);
	jpCadastralRegister.add(tfFreeArea, null);
	jpContainer.add(jpCadastralRegister, null);
	jpContainer.add(jpFile, null);
	jpContainer.add(btnAccept, null);
	jpContainer.add(btnClose, null);
	jpContainer.add(filesCadastralRegisterPanel, null);
	this.add(jpContainer, null);
	jpCadastralRegister.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Informe de Registro Catastral", Color.black, Color.black));
	jpFile.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Expediente", Color.black, Color.black));
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setEditableFieds();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	String fileInsert = armarConsulta();
    }

    private String armarConsulta() {
	//Primero se necesita insertar en la tabla addresses, porque se necesita el idaddress
	String idAddres = "SELECT MAX(idaddress) + 1 FROM org.addresses;";
	String addresessInsert = "INSERT INTO org.addresses VALUES(" + idAddres + " , 1 ," + filesCadastralRegisterPanel.getIdProvince() + " , " + filesCadastralRegisterPanel.getIdLocation() + " , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 ,'');";
	String idFile = "SELECT MAX(idfile) + 1 FROM file.files;";
	String Insert = "INSERT INTO file.files VALUES(" + idFile + "," + idAddres + ",,,,,)";
	return "";
    }

    private void setEditableFieds() {
	if (editableFields) {
	    filesCadastralRegisterPanel.setEditableFields();
	}
    }

}
