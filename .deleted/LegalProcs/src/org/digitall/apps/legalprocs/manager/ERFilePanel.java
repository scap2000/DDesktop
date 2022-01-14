package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

//

public class ERFilePanel extends BasicContainerPanel {

    private BasicPanel jpExpedientes = new BasicPanel();
    private UbicationPanel ubicationPanel = new UbicationPanel();
    private FilesCadastralRegisterPanel filesCadastralRegisterPanel = new FilesCadastralRegisterPanel();
    private BasicLabel lblExpte = new BasicLabel();
    private BasicLabel lblUbicacion = new BasicLabel();
    private TFInputPanel panelFileNumber = new TFInputPanel(DataTypes.INTEGER, "FileNumber", false, true);
    private TFInputPanel panelFileLetter = new TFInputPanel(DataTypes.STRING, "FileLetter", false, true);
    private TFInputPanel panelFileYear = new TFInputPanel(DataTypes.INTEGER, "FileYear", false, true);
    private String NEX = "", NEY = "", SOX = "", SOY = "";
    private String type = "", idLocation = "";
    private TFInputPanel tfStartAppDate = new TFInputPanel(DataTypes.DATE, "StartDate", false, true);
    private TFInputPanel tfExpirationDate = new TFInputPanel(DataTypes.DATE, "ExpirationDate", false, true);

    public ERFilePanel(String _type) {
	try {
	    type = _type;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(577, 410));
	this.add(lblUbicacion, null);
	this.add(filesCadastralRegisterPanel, null);
	this.add(ubicationPanel, null);
	this.add(lblExpte, null);
	this.add(jpExpedientes, null);
	lblExpte.setText(" Expediente");
	lblExpte.setBounds(new Rectangle(15, 10, 80, 10));
	lblExpte.setFont(new Font("Default", 1, 11));
	lblExpte.setOpaque(false);
	jpExpedientes.setSize(new Dimension(585, 90));
	jpExpedientes.setBounds(new Rectangle(8, 15, 560, 85));
	jpExpedientes.setLayout(null);
	jpExpedientes.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	ubicationPanel.setBounds(new Rectangle(8, 290, 560, 110));
	filesCadastralRegisterPanel.setBounds(new Rectangle(5, 100, 560, 165));
	filesCadastralRegisterPanel.setBounds(new Rectangle(8, 110, 560, 170));
	//filesCadastralRegisterPanel.setSize(new Dimension(560, 154));
	//lblUbicacion.setText(" Ubicación de la");
	lblUbicacion.setBounds(new Rectangle(15, 285, 175, 10));
	lblUbicacion.setFont(new Font("Default", 1, 11));
	lblUbicacion.setOpaque(false);
	panelFileNumber.setBounds(new Rectangle(30, 20, 90, 40));
	panelFileLetter.setBounds(new Rectangle(145, 20, 90, 40));
	panelFileYear.setBounds(new Rectangle(270, 20, 90, 40));
	tfStartAppDate.setBounds(new Rectangle(430, 23, 95, 35));
	tfExpirationDate.setBounds(new Rectangle(365, 20, 95, 35));
	jpExpedientes.add(tfStartAppDate, null);
	jpExpedientes.add(tfExpirationDate, null);
	jpExpedientes.add(panelFileNumber, null);
	jpExpedientes.add(panelFileLetter, null);
	jpExpedientes.add(panelFileYear, null);
	jpExpedientes.add(tfStartAppDate, null);
	tfExpirationDate.setVisible(false);
	loadData();
    }

    private void loadData() {
	if (type.equals("ER")) {
	    lblUbicacion.setText(" Ubicación de la solicitud");
	} else {
	    lblUbicacion.setText(" Ubicación de la manifestación");
	}
    }

    private void getCadastralRegisterData() {
	//CRIdProvcince = filesCadastralRegisterPanel.getIDProvince();
	//CRIdLocation = filesCadastralRegisterPanel.getIDLocation();
	NEX = filesCadastralRegisterPanel.getNEX();
	NEY = filesCadastralRegisterPanel.getNEY();
	SOX = filesCadastralRegisterPanel.getSOX();
	SOY = filesCadastralRegisterPanel.getSOY();
    }

    public String getCRInsertPointNE() {
	String insertPointNE = filesCadastralRegisterPanel.getInsertPointNE();
	return insertPointNE;
    }

    public String getCRIdPointNE() {
	return filesCadastralRegisterPanel.getIdNE();
    }

    public String getCRInsertPointSO() {
	String insertPointSO = filesCadastralRegisterPanel.getInsertPointSO();
	return insertPointSO;
    }

    public String getCRIdPointSO() {
	return filesCadastralRegisterPanel.getIdSO();
    }

    public String getIdCountry() {
	return ubicationPanel.getIdCountry();
    }

    public String getIdProvince() {
	return ubicationPanel.getIdProvince();
    }

    public String getLocationInsert() {
	return ubicationPanel.getUbicationInsert();
    }

    public String getIdUbicationInsert() {
	return ubicationPanel.getIdUbication();
    }

    public String getFileNumber() {
	return panelFileNumber.getText().trim();
    }

    public String getFileLetter() {
	return panelFileLetter.getText().trim();
    }

    public String getFileYear() {
	return panelFileYear.getText().trim();
    }

    public String getStartDate() {
	return tfStartAppDate.getText().trim();
    }

    public String getExpirationDate() {
	return tfExpirationDate.getText().trim();
    }

}
