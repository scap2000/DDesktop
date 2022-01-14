package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.sql.ResultSet;

import javax.swing.BorderFactory;

import org.digitall.apps.legalprocs.calendar.wizard.ExplorationRequestClass_Old;
import org.digitall.apps.legalprocs.manager.classes.EntitiesByFileClass;
import org.digitall.common.legalprocs.manager.AddNewEntity;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AddComboButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.combos.JCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.sql.LibSQL;

//

public class LegalRepresentativePanel extends BasicContainerPanel {

    private BasicLabel jLabel1 = new BasicLabel();
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicPanel jpLegalRepresentativeData = new BasicPanel();
    private SolicitorAddressPanel legalRepresentativeAddressPanel = new SolicitorAddressPanel();
    private TFInput tfIdentification = new TFInput(DataTypes.STRING, "Identification", false);
    private TFInput tfIdentificationNumber = new TFInput(DataTypes.INTEGER, "FileNumber", false);
    private BasicButton btnAddProfession = new BasicButton();
    private BasicButton btnAddCivilState = new BasicButton();
    private BasicButton btnOther = new BasicButton();
    private CBInput cbRepresentative = new CBInput(CachedCombo.ENTITIES_ALLPERSONLIST, "LegalRepresentative");
    private ExplorationRequestClass_Old prospection;
    private String idreference = "0";
    private AddComboButton btnAddLegalRep = new AddComboButton();
    private EntitiesByFileClass legalRepClass;

    public LegalRepresentativePanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(572, 297));
	jpLegalRepresentativeData.setBounds(new Rectangle(5, 20, 560, 125));
	jpLegalRepresentativeData.setLayout(null);
	jpLegalRepresentativeData.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	legalRepresentativeAddressPanel.setBounds(new Rectangle(5, 170, 560, 120));
	tfIdentification.setBounds(new Rectangle(15, 65, 120, 42));
	tfIdentification.autoSize();
	tfIdentification.setEditable(true);
	tfIdentification.autoSize();
	tfIdentificationNumber.setBounds(new Rectangle(180, 65, 110, 42));
	tfIdentificationNumber.autoSize();
	tfIdentificationNumber.setEditable(true);
	jLabel1.setText(" Dirección Legal");
	jLabel1.setBounds(new Rectangle(15, 160, 105, 15));
	jLabel1.setOpaque(false);
	jLabel1.setFont(new Font("Default", 1, 11));
	jLabel2.setText(" Información general");
	jLabel2.setBounds(new Rectangle(15, 10, 135, 15));
	jLabel2.setOpaque(false);
	jLabel2.setFont(new Font("Default", 1, 11));
	//legalRepresentativeAddressPanel.setBounds(new Rectangle(5, 140, 560, 125));
	btnOther.setText("Otros");
	btnOther.setBounds(new Rectangle(445, 80, 75, 25));
	cbRepresentative.setBounds(new Rectangle(15, 10, 460, 45));
	btnAddLegalRep.setBounds(new Rectangle(485, 25, 20, 20));
	btnAddLegalRep.addActionListener(new ActionListener() {

				      public void actionPerformed(ActionEvent e) {
					  btnAddLegalRep_actionPerformed(e);
				      }

				  }
	);
	cbRepresentative.autoSize();
	jpLegalRepresentativeData.add(btnAddLegalRep, null);
	jpLegalRepresentativeData.add(cbRepresentative, null);
	jpLegalRepresentativeData.add(btnOther, null);
	jpLegalRepresentativeData.add(btnAddCivilState, null);
	jpLegalRepresentativeData.add(btnAddProfession, null);
	jpLegalRepresentativeData.add(tfIdentificationNumber, null);
	jpLegalRepresentativeData.add(tfIdentification, null);
	this.add(jLabel2, null);
	this.add(jLabel1, null);
	this.add(legalRepresentativeAddressPanel, null);
	this.add(jpLegalRepresentativeData, null);
	btnOther.setVisible(false);
	//cbRepresentative.setSelectedValue("Not, Assigned");
	//loadComboListener();
	loadSolicitorPeopleCombo();
    }

    private void setData() {
	if (cbRepresentative.getSelectedValue().toString().equals("0")) {
	    tfIdentification.setValue("");
	    tfIdentificationNumber.setValue("");
	    btnAddProfession.setText("");
	    btnAddCivilState.setText("");
	    legalRepresentativeAddressPanel.setData("", "", "", "");
	} else {
	    idreference = cbRepresentative.getSelectedValue().toString();
	    ResultSet result = LibSQL.exFunction("file.getentitypersondata", idreference);
	    try {
		if (result.next()) {
		    tfIdentification.setValue(result.getString("identificationtype"));
		    tfIdentificationNumber.setValue(result.getString("identificationnumber"));
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	    String params = idreference + ",0,0";
	    ResultSet result2 = LibSQL.exFunction("org.getallpersonaddresses", params);
	    try {
		if (result2.next()) {
		    if (result2.getString("idaddresstype").equals("1")) {
			legalRepresentativeAddressPanel.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
		    } else if (result2.getString("idaddresstype").equals("3")) {
			legalRepresentativeAddressPanel.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
		    }
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	}
    }

    public String getIdRepresentative() {
	return cbRepresentative.getSelectedValue().toString();
    }

    public void setProspection(ExplorationRequestClass_Old _explorationRequestClass_Old) {
	prospection = _explorationRequestClass_Old;
	loadCombo();
    }

    private void loadCombo() {
	try {
	    //if (prospection.getArgument(2).equals("0")) {  // a ser borrado cuando los objetos funcionen bien
	    if (legalRepClass.getIdReferencedAux() == -1 || legalRepClass.getIdReferencedAux() == 0) {
		cbRepresentative.setSelectedValue("Not, Assigned");
	    } else {
		//cbRepresentative.setSelectedID(prospection.getArgument(2));  // a ser borrado cuando los objetos funcionen bien
		cbRepresentative.setSelectedID(String.valueOf(legalRepClass.getIdReferencedAux()));
	    }
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    cbRepresentative.setSelectedValue("Not, Assigned");
	}
    }

    private void btnAddLegalRep_actionPerformed(ActionEvent e) {
	AddNewEntity newEntity = new AddNewEntity(AddNewEntity.PEOPLE, "");
	newEntity.setModal(true);
	newEntity.setVisible(true);
	loadSolicitorPeopleCombo();
    }

    private void loadSolicitorPeopleCombo() {
	JCombo combo = new JCombo();
	combo.loadJCombo(LibSQL.exFunction("file.getAllEntitiesPersons", ""));
	cbRepresentative.setCombo(combo);
	cbRepresentative.updateUI();
	//setData();
	loadComboListener();
    }

    private void loadComboListener() {
	cbRepresentative.getCombo().addItemListener(new ItemListener() {

						 public void itemStateChanged(ItemEvent evt) {
						     if (evt.getStateChange() == ItemEvent.SELECTED) {
							 setData();
						     }
						 }

					     }
	);
    }

    public void setLegalRepClass(EntitiesByFileClass _legalRepClass) {
	legalRepClass = _legalRepClass;
	if (legalRepClass.getIdFile() != -1) {
	    legalRepClass.retrieveData(false);
	    loadCombo();
	}
    }

    public EntitiesByFileClass getLegalRepClass() {
	return legalRepClass;
    }

}
