package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import org.digitall.apps.legalprocs.manager.classes.MineNameClass;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class PanelMineName extends BasicDialog {

    //
    private String title;
    private BasicPanel PanelMineName = new BasicPanel();
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private TFInput tfMineName = new TFInput(DataTypes.STRING, "MineName", false);
    //OBJECTS
    private MineNameClass mineNameClass;
    //CONSTS
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelMineName() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelMineName(String _title, MineNameClass _mineNameClass) {
	title = _title;
	mineNameClass = _mineNameClass;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(438, 200));
	this.setTitle(title);
	//(this.getWidth(), this.getTitle(), IconTypes.CR_IconHeaderLogo, IconTypes.CRFiles_IconHeaderLogo);
	this.getContentPane().setLayout(null);
	btnClose.setBounds(new Rectangle(380, 135, 40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(10, 135, 40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	tfMineName.setBounds(new Rectangle(25, 15, 370, 40));
	tfMineName.setEditable(true);
	tfMineName.autoSize();
	PanelMineName.setBounds(new Rectangle(10, 50, 410, 70));
	PanelMineName.setLayout(null);
	PanelMineName.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	PanelMineName.add(tfMineName, null);
	//);
	this.getContentPane().add(PanelMineName, null);
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	ComponentsManager.centerWindow(this);
	loadMineNameObject();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void loadData() {
	tfMineName.setValue(mineNameClass.getNameAux());
    }

    private void loadMineNameObject() {
	if (mineNameClass.getIdMineAux() == -1) {
	    mineNameClass.retrieveData();
	}
	loadData();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	mineNameClass.setNameAux(tfMineName.getString());
	if (mineNameClass.getIdMineAux() == 0) {
	    mineNameClass.setMineNameStatusAux(INCOMPLETE);
	} else {
	    mineNameClass.setMineNameStatusAux(COMPLETE);
	}
	mineNameClass.setRecord(YES);
	this.dispose();
    }

}
