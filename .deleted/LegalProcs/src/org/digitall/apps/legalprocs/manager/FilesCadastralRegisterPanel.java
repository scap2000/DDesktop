package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.combos.JCombo;
import org.digitall.common.components.inputpanels.CBInputPanel;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

//

public class FilesCadastralRegisterPanel extends BasicContainerPanel {

    private BasicLabel lblLocation = new BasicLabel();
    private CBInputPanel cbProvince = new CBInputPanel(CachedCombo.PROVINCE_TABS, "ProvinceState");
    private JCombo cbLocation = CachedCombo.getCachedCombo(CachedCombo.LOCATION_TABS);
    private TFInputPanel panelCadastralNEX = new TFInputPanel(DataTypes.INTEGER, "CadastralNEX", false, false);
    private TFInputPanel panelCadastralNEY = new TFInputPanel(DataTypes.INTEGER, "CadastralNEY", false, false);
    private TFInputPanel panelCadastralSOX = new TFInputPanel(DataTypes.INTEGER, "CadastralSOX", false, false);
    private TFInputPanel panelCadastralSOY = new TFInputPanel(DataTypes.INTEGER, "CadastralSOY", false, false);
    private TFInputPanel panelRClassNE = new TFInputPanel(DataTypes.INTEGER, "RightClass", false, false);
    private TFInputPanel panelRClassSO = new TFInputPanel(DataTypes.INTEGER, "RightClass", false, false);
    private BasicButton btnAddLocation = new BasicButton();
    private BasicButton btnAddProvince = new BasicButton();
    private String CRIdProvince, CRIdLocation = "", NEX = "", NEY = "", SOX = "", SOY = "";
    private String idNE = "", idSO = "", rClassNE = "", rClassSO = "";
    private boolean editable = false;

    public FilesCadastralRegisterPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public FilesCadastralRegisterPanel(boolean _editable) {
	try {
	    editable = _editable;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(568, 180));
	this.setBounds(new Rectangle(10, 9, 566, 180));
	this.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	//panelUbicationState.setBounds(new Rectangle(15, 10, 205, 45));
	//panelUbicationLocation.setBounds(new Rectangle(300, 10, 205, 45));
	panelCadastralNEX.setBounds(new Rectangle(15, 65, 205, 50));
	panelCadastralNEY.setBounds(new Rectangle(265, 65, 205, 50));
	panelCadastralSOX.setBounds(new Rectangle(15, 115, 205, 50));
	panelCadastralSOY.setBounds(new Rectangle(265, 115, 205, 50));
	btnAddLocation.setBounds(new Rectangle(485, 20, 30, 25));
	btnAddLocation.setToolTipText("Agregar Localidad");
	btnAddProvince.setBounds(new Rectangle(225, 20, 30, 25));
	btnAddProvince.setToolTipText("Agregar Provincia");
	btnAddProvince.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnAddProvince_actionPerformed(e);
				      }

				  }
	);
	//tFInputPanel1.setText("tFInputPanel1");
	panelRClassNE.setBounds(new Rectangle(485, 65, 70, 40));
	//tFInputPanel2.setText("tFInputPanel2");
	panelRClassSO.setBounds(new Rectangle(485, 115, 70, 40));
	cbProvince.setBounds(new Rectangle(15, 25, 205, 40));
	//cbProvince.setSize(new Dimension(180, 20));
	cbLocation.setBounds(new Rectangle(265, 40, 210, 20));
	lblLocation.setText("Localidad");
	lblLocation.setBounds(new Rectangle(265, 25, 205, 15));
	lblLocation.setFont(new Font("Default", 1, 11));
	this.add(panelRClassSO, null);
	this.add(panelRClassNE, null);
	this.add(lblLocation, null);
	this.add(cbLocation, null);
	this.add(cbProvince, null);
	this.add(btnAddProvince, null);
	this.add(btnAddLocation, null);
	this.add(panelCadastralSOY, null);
	this.add(panelCadastralSOX, null);
	this.add(panelCadastralNEY, null);
	this.add(panelCadastralNEX, null);
	cbProvince.getCombo().addItemListener(new ItemListener() {

					   public void itemStateChanged(ItemEvent evt) {
					       if (evt.getStateChange() == ItemEvent.SELECTED) {
						   loadLocation(cbProvince.getSelectedValue());
					       }
					   }

				       }
	);
	//this.add(panelUbicationLocation, null);
	//this.add(panelUbicationState, null);
	loadData();
	btnAddLocation.setVisible(false);
	btnAddProvince.setVisible(false);
	this.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Matricula Catastral", Color.black, Color.black));
	setEditableFields();
    }

    private void loadData() {
	//panelUbicationState.setText("Not Assigned");
	//panelUbicationLocation.setText("Not Assigned");
	cbProvince.setSelectedValue("SALTA");
	cbLocation.setSelectedValue("SALTA");
	panelCadastralNEX.setText("0");
	panelCadastralNEY.setText("0");
	panelCadastralSOX.setText("0");
	panelCadastralSOY.setText("0");
    }

    public String getNEX() {
	return panelCadastralNEX.getText();
    }

    public String getNEY() {
	return panelCadastralNEY.getText();
    }

    public String getSOX() {
	return panelCadastralSOX.getText();
    }

    public String getSOY() {
	return panelCadastralSOY.getText();
    }

    public String getInsertPointNE() {
	getData();
	idNE = org.digitall.lib.sql.LibSQL.getCampo("Select max(idcadastralregister) + 1 From file.cadastralregister");
	String insertPointNE = "INSERT INTO file.cadastralregister VALUES( " + idNE + " ,0" + CRIdLocation + ",0" + CRIdProvince + ", " + NEX + "," + NEY + " ," + rClassNE + " ,false,'');";
	return insertPointNE;
    }

    public String getInsertPointSO() {
	getData();
	idSO = org.digitall.lib.sql.LibSQL.getCampo("Select max(idcadastralregister) + 2 From file.cadastralregister");
	String insertPointSO = "INSERT INTO file.cadastralregister VALUES( " + idSO + " , 0" + CRIdLocation + ", 0" + CRIdProvince + ", " + SOX + "," + SOY + " , " + rClassSO + " ,false,'');";
	return insertPointSO;
    }

    private void getData() {
	CRIdProvince = cbProvince.getSelectedValue().toString();
	CRIdLocation = cbLocation.getSelectedValue().toString();
	NEX = panelCadastralNEX.getText();
	NEY = panelCadastralNEY.getText();
	SOX = panelCadastralSOX.getText();
	SOY = panelCadastralSOY.getText();
	rClassNE = panelRClassNE.getText();
	rClassSO = panelRClassSO.getText();
	//System.out.println("idProvince --> " + CRIdProvince);
	//System.out.println("idLocation --> " + CRIdLocation);
    }

    private void btnAddProvince_actionPerformed(ActionEvent e) {

    }

    private void loadLocation(Object _idProvince) {
	cbLocation.setFilter(_idProvince);
	if (cbProvince.getSelectedItem().toString().equals("SALTA")) {
	    cbLocation.setSelectedValue("SALTA");
	} else {
	    cbLocation.setSelectedValue(null);
	}
    }

    public String getIdNE() {
	return idNE;
    }

    public String getIdSO() {
	return idSO;
    }

    public String getIdProvince() {
	return cbProvince.getSelectedValue().toString();
    }

    public String getIdLocation() {
	return cbLocation.getSelectedValue().toString();
    }

    public void setEditableFields() {
	panelCadastralNEX.setEditableTxt(true);
	panelCadastralNEY.setEditableTxt(true);
	panelCadastralSOX.setEditableTxt(true);
	panelCadastralSOY.setEditableTxt(true);
	panelRClassNE.setEditableTxt(true);
	panelRClassSO.setEditableTxt(true);
    }

}
