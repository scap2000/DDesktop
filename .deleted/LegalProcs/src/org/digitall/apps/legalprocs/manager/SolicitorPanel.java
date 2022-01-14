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
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JRadioButton;

import org.digitall.apps.legalprocs.calendar.wizard.ExplorationRequestClass_Old;
import org.digitall.apps.legalprocs.manager.classes.EntitiesByFileClass;
import org.digitall.common.legalprocs.manager.AddNewEntity;
import org.digitall.lib.components.BorderPanel;
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

public class SolicitorPanel extends BasicContainerPanel {

    private BasicLabel lblSolicitorData1 = new BasicLabel();
    private BasicLabel lblSolicitorData2 = new BasicLabel();
    private TFInput tfIdentification = new TFInput(DataTypes.STRING, "Identification", false);
    private TFInput tfIdentificationNumber = new TFInput(DataTypes.INTEGER, "FileNumber", false);
    private TFInput tfBirthday = new TFInput(DataTypes.INTEGER, "Birthdate", false);
    private TFInput tfNationality = new TFInput(DataTypes.STRING, "Nationality", false);
    private TFInput tfProfession = new TFInput(DataTypes.INTEGER, "Profession", false);
    private TFInput tfCivilState = new TFInput(DataTypes.STRING, "CivilState", false);
    private BasicPanel jpSolicitor = new BasicPanel();
    private SolicitorAddressPanel jpRealAddress = new SolicitorAddressPanel();
    private SolicitorAddressPanel jpLegalAddress = new SolicitorAddressPanel();
    private BasicButton btnOther = new BasicButton();
    private CBInput cbSolicitor = new CBInput(CachedCombo.ENTITIES_ALLPERSONLIST, "Solicitor", false);
    private ExplorationRequestClass_Old explorationRequestClass_Old;
    private AddComboButton btnAddSolicitor = new AddComboButton();
    private JRadioButton rbPeople = new JRadioButton();
    private JRadioButton rbCompanies = new JRadioButton();
    private ButtonGroup solicitorgroup = new ButtonGroup();
    private BasicButton btnEditSolicitor = new BasicButton();
    private String idreference = "0";
    private JDesktopPane parentDesktop;
    private EntitiesByFileClass solicitorClass;
    private static final int YES = 1;
    private static final int NO = 2;

    public SolicitorPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(572, 421));
	tfBirthday.setBounds(new Rectangle(295, 65, 95, 45));
	tfBirthday.autoSize();
	tfBirthday.setEditable(false);
	tfIdentificationNumber.setBounds(new Rectangle(165, 65, 110, 45));
	tfIdentificationNumber.autoSize();
	tfIdentificationNumber.setEditable(false);
	tfIdentification.setBounds(new Rectangle(10, 65, 120, 45));
	tfIdentification.autoSize();
	tfIdentification.setEditable(false);
	tfNationality.setBounds(new Rectangle(405, 65, 110, 45));
	tfNationality.autoSize();
	tfNationality.setEditable(false);
	tfProfession.setBounds(new Rectangle(10, 105, 225, 45));
	tfProfession.autoSize();
	tfProfession.setEditable(false);
	tfCivilState.setBounds(new Rectangle(295, 105, 150, 45));
	tfCivilState.autoSize();
	tfCivilState.setEditable(false);
	jpRealAddress.setBounds(new Rectangle(5, 175, 560, 112));
	jpSolicitor.setBounds(new Rectangle(5, 0, 560, 160));
	jpSolicitor.setLayout(null);
	jpSolicitor.setBorder(BorderPanel.getBorderPanel("Datos del Solicitante", Color.black, Color.black));
	//jpSolicitor.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	btnOther.setText("Otros");
	btnOther.setBounds(new Rectangle(485, 110, 70, 25));
	btnOther.setVisible(false);
	cbSolicitor.setBounds(new Rectangle(165, 20, 320, 40));
	cbSolicitor.autoSize();
	jpLegalAddress.setBounds(new Rectangle(5, 300, 560, 112));
	jpLegalAddress.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	lblSolicitorData1.setText(" Dirección Real");
	lblSolicitorData1.setBounds(new Rectangle(10, 165, 95, 15));
	lblSolicitorData1.setFont(new Font("Dialog", 1, 11));
	lblSolicitorData1.setOpaque(false);
	lblSolicitorData2.setText(" Dirección Legal");
	lblSolicitorData2.setBounds(new Rectangle(10, 293, 95, 15));
	lblSolicitorData2.setFont(new Font("Dialog", 1, 11));
	lblSolicitorData2.setOpaque(false);
	//jpSolicitor.add(cbTest, null);
	jpSolicitor.add(btnEditSolicitor, null);
	jpSolicitor.add(rbCompanies, null);
	jpSolicitor.add(rbPeople, null);
	jpSolicitor.add(btnAddSolicitor, null);
	jpSolicitor.add(cbSolicitor, null);
	jpSolicitor.add(btnOther, null);
	jpSolicitor.add(tfCivilState, null);
	jpSolicitor.add(tfProfession, null);
	jpSolicitor.add(tfNationality, null);
	jpSolicitor.add(tfBirthday, null);
	jpSolicitor.add(tfIdentificationNumber, null);
	jpSolicitor.add(tfIdentification, null);
	this.add(lblSolicitorData2, null);
	this.add(lblSolicitorData1, null);
	this.add(jpLegalAddress, null);
	this.add(jpSolicitor, null);
	this.add(jpRealAddress, null);
	btnAddSolicitor.setBounds(new Rectangle(495, 35, 20, 20));
	btnAddSolicitor.setToolTipText("Add");
	btnAddSolicitor.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   btnAddSolicitor_actionPerformed(e);
				       }

				   }
	);
	rbPeople.setText("Personas");
	rbPeople.setBounds(new Rectangle(10, 25, 90, 15));
	rbCompanies.setText("Empresas");
	rbCompanies.setBounds(new Rectangle(10, 40, 90, 15));
	rbCompanies.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       rbCompanies_actionPerformed(e);
				   }

			       }
	);
	/*cbTest.setBounds(new Rectangle(455, 125, 100, 30));
        cbTest.autoSize();*/
	btnEditSolicitor.setBounds(new Rectangle(525, 35, 20, 20));
	btnEditSolicitor.setToolTipText("Edit");
	btnEditSolicitor.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
					    btnEditSolicitor_actionPerformed(e);
					}

				    }
	);
	solicitorgroup.add(rbPeople);
	solicitorgroup.add(rbCompanies);
	rbPeople.setSelected(true);
	rbPeople.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    rbPeople_actionPerformed(e);
				}

			    }
	);
	cbSolicitor.setSelectedValue("Not, Assigned");
	loadComboListener();
	btnEditSolicitor.setVisible(false);
    }

    private void setData() {
	clearFields();
	if (cbSolicitor.getSelectedValue().toString().equals("0")) {
	    tfIdentification.setValue("");
	    tfIdentificationNumber.setValue("");
	    tfBirthday.setValue("");
	    tfNationality.setValue("");
	    tfProfession.setValue("");
	    tfCivilState.setValue("");
	    jpRealAddress.setData("", "", "", "");
	    jpLegalAddress.setData("", "", "", "");
	} else {
	    idreference = cbSolicitor.getSelectedValue().toString();
	    if (rbPeople.isSelected()) {
		ResultSet result = LibSQL.exFunction("file.getentitypersondata", idreference);
		try {
		    if (result.next()) {
			tfIdentification.setValue(result.getString("identificationtype"));
			tfIdentificationNumber.setValue(result.getString("identificationnumber"));
			tfBirthday.setValue(result.getString("birthdate"));
			tfNationality.setValue("");
			tfProfession.setValue(result.getString("profession"));
			tfCivilState.setValue(result.getString("civilstate"));
		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		String params = idreference + ",0,0";
		ResultSet result2 = LibSQL.exFunction("org.getallpersonaddresses", params);
		try {
		    if (result2.next()) {
			if (result2.getString("idaddresstype").equals("1")) {
			    jpRealAddress.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
			} else if (result2.getString("idaddresstype").equals("3")) {
			    jpLegalAddress.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
			}
		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    } else {
		ResultSet result = LibSQL.exFunction("org.getcompany", idreference);
		try {
		    if (result.next()) {
			tfIdentification.setValue(result.getString("identification"));
			tfIdentificationNumber.setValue(result.getString("identificationnumber"));
			tfBirthday.setValue("");
			tfNationality.setValue("");
			tfProfession.setValue("");
			tfCivilState.setValue("");
		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		String params = idreference + ",0,0";
		ResultSet result2 = LibSQL.exFunction("org.getallcompanyaddresses", params);
		try {
		    if (result2.next()) {
			if (result2.getString("idaddresstype").equals("1")) {
			    jpRealAddress.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
			} else if (result2.getString("idaddresstype").equals("3")) {
			    jpLegalAddress.setData2(result2.getString("idprovince"), result2.getString("idlocation"), result2.getString("street"), result2.getString("number"));
			}
		    }
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	}
    }

    public String getIdSolicitor() {
	return cbSolicitor.getSelectedValue().toString();
    }

    public boolean getIsPerson() {
	return rbPeople.isSelected();
    }

    public void setER(ExplorationRequestClass_Old _explorationRequestClass_Old) {
	explorationRequestClass_Old = _explorationRequestClass_Old;
	loadCombo();
    }

    private void loadCombo() {
	try {
	    //if (explorationRequestClass_Old.getArgument(1).equals("0")) {
	    //if (solicitorClass.getIdReferenced() == -1 || solicitorClass.getIdReferenced() == 0) {
	    if (solicitorClass.getIdReferencedAux() == -1 || solicitorClass.getIdReferencedAux() == 0) {
		cbSolicitor.setSelectedValue("Not, Assigned");
	    } else {
		//cbSolicitor.setSelectedID(explorationRequestClass_Old.getArgument(1));
		if (solicitorClass.isPersonAux()) {
		    rbPeople.setSelected(true);
		    loadSolicitorPeopleCombo();
		    cbSolicitor.setSelectedID(String.valueOf(solicitorClass.getIdReferencedAux()));
		} else {
		    rbCompanies.setSelected(true);
		    loadSolicitorCompanyCombo();
		    cbSolicitor.setSelectedID(String.valueOf(solicitorClass.getIdReferencedAux()));
		}
	    }
	} catch (Exception ex) {
	    //ex.printStackTrace();
	    cbSolicitor.setSelectedID("1");
	}
    }

    private void btnAddSolicitor_actionPerformed(ActionEvent e) {
	if (rbPeople.isSelected()) {
	    AddNewEntity newEntity = new AddNewEntity(AddNewEntity.PEOPLE, "");
	    newEntity.setModal(true);
	    newEntity.setVisible(true);
	} else {
	    AddNewEntity newEntity = new AddNewEntity(AddNewEntity.COMPANY, "");
	    newEntity.setModal(true);
	    newEntity.setVisible(true);
	}
	loadSolicitorPeopleCombo();
    }

    private void rbPeople_actionPerformed(ActionEvent e) {
	loadSolicitorPeopleCombo();
    }

    private void rbCompanies_actionPerformed(ActionEvent e) {
	loadSolicitorCompanyCombo();
    }

    private void loadSolicitorPeopleCombo() {
	JCombo combo = new JCombo();
	combo.loadJCombo(LibSQL.exFunction("file.getAllEntitiesPersons", ""));
	cbSolicitor.setCombo(combo);
	cbSolicitor.updateUI();
	setData();
	loadComboListener();
    }

    private void loadSolicitorCompanyCombo() {
	JCombo combo = new JCombo();
	combo.loadJCombo(LibSQL.exFunction("file.getAllEntitiesCompanies", ""));
	cbSolicitor.setCombo(combo);
	cbSolicitor.updateUI();
	loadComboListener();
    }

    private void loadComboListener() {
	cbSolicitor.getCombo().addItemListener(new ItemListener() {

					    public void itemStateChanged(ItemEvent evt) {
						if (evt.getStateChange() == ItemEvent.SELECTED) {
						    setData();
						}
					    }

					}
	);
    }

    private void btnEditSolicitor_actionPerformed(ActionEvent e) {
	if (rbPeople.isSelected()) {
	    ResultSet entity = LibSQL.exFunction("org.getPerson", idreference);
	    try {
		if (entity.next()) {
		    AddNewEntity newEntity = new AddNewEntity(parentDesktop, AddNewEntity.PEOPLE, idreference, entity);
		    newEntity.setModal(true);
		    newEntity.setVisible(true);
		}
	    } catch (Exception ex) {
		ex.printStackTrace();
	    }
	} else {
	    AddNewEntity newEntity = new AddNewEntity(AddNewEntity.COMPANY, idreference);
	    newEntity.setModal(true);
	    newEntity.setVisible(true);
	}
    }

    public void setParentDesktop(JDesktopPane _parentDesktop) {
	parentDesktop = _parentDesktop;
    }

    private void clearFields() {
	tfIdentification.setValue("");
	tfIdentificationNumber.setValue("");
	tfBirthday.setValue("");
	tfNationality.setValue("");
	tfProfession.setValue("");
	tfCivilState.setValue("");
	jpRealAddress.setData("", "", "", "");
	jpLegalAddress.setData("", "", "", "");
    }

    public void setSolicitorClass(EntitiesByFileClass _solicitorClass) {
	solicitorClass = _solicitorClass;
	if (solicitorClass.getIdFile() != -1) {
	    if (solicitorClass.getRecord() == NO) {
		solicitorClass.retrieveData(true);
	    }
	    loadCombo();
	}
    }

    public EntitiesByFileClass getSolicitorClass() {
	return solicitorClass;
    }

}
