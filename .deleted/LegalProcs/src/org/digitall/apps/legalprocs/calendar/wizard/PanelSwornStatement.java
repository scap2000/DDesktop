package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.legalprocs.manager.classes.ExplorationRequestClass;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;

//

public class PanelSwornStatement extends BasicDialog {

    private BasicPanel jpSwornStatement = new BasicPanel();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private BasicLabel lblSwornStatement = new BasicLabel();
    private JRadioButton rbJeopardize = new JRadioButton();
    private JRadioButton rbNotJeopardize = new JRadioButton();
    private BasicLabel lblSwornStatement1 = new BasicLabel();
    private ButtonGroup grupo = new ButtonGroup();
    private Manager mgmt;
    private BasicLabel lblArticulos = new BasicLabel();
    private ExplorationRequestClass_Old prospection;
    private ExplorationRequestClass explorationRequestClass;
    private static final int NO_MADE = 1;
    private static final int INCOMPLETE = 2;
    private static final int COMPLETE = 3;
    private static final int REJECTED = 4;
    private static final int YES = 1;
    private static final int NO = 2;

    public PanelSwornStatement() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public PanelSwornStatement(Manager _mgmt) {
	try {
	    mgmt = _mgmt;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    // a ser borrado
    /*public PanelSwornStatement(ExplorationRequestClass_Old _prospection) {
	try {
	    prospection = _prospection;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }*/

    public PanelSwornStatement(ExplorationRequestClass _explorationRequestClass) {
	try {
	    explorationRequestClass = _explorationRequestClass;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(430, 325));
	this.setBounds(new Rectangle(10, 10, 430, 325));
	this.setTitle("Declaración Jurada");
	this.getContentPane().setLayout(null);
	this.getContentPane().add(lblArticulos, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(btnAccept, null);
	jpSwornStatement.setBounds(new Rectangle(7, 45, 410, 195));
	jpSwornStatement.setLayout(null);
	jpSwornStatement.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	btnAccept.setBounds(new Rectangle(10, 265, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnAccept_actionPerformed(e);
				 }

			     }
	);
	btnClose.setBounds(new Rectangle(375, 265, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	lblSwornStatement.setText("Declara bajo juramento estar:");
	lblSwornStatement.setBounds(new Rectangle(10, 25, 390, 30));
	lblSwornStatement.setFont(new Font("Default", 1, 11));
	rbJeopardize.setText("Comprometido");
	rbJeopardize.setBounds(new Rectangle(130, 60, 165, 25));
	rbNotJeopardize.setText("No Comprometido");
	rbNotJeopardize.setBounds(new Rectangle(130, 85, 165, 25));
	lblSwornStatement1.setText("<html>en las prohibiciones establecidas en los Arts. 29 y 30 <br><br>del Código de Minería</html>");
	lblSwornStatement1.setBounds(new Rectangle(10, 110, 390, 60));
	lblSwornStatement1.setFont(new Font("Default", 1, 11));
	lblArticulos.setText("Ver Arts. 29 y 30");
	lblArticulos.setBounds(new Rectangle(152, 267, 120, 20));
	lblArticulos.setHorizontalAlignment(SwingConstants.CENTER);
	lblArticulos.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	this.getContentPane().add(jpSwornStatement, null);
	jpSwornStatement.add(lblSwornStatement1, null);
	jpSwornStatement.add(rbNotJeopardize, null);
	jpSwornStatement.add(rbJeopardize, null);
	jpSwornStatement.add(lblSwornStatement, null);
	jpSwornStatement.setBorder(org.digitall.lib.components.BorderPanel.getBorderPanel("Declaración", Color.black, Color.black));
	grupo.add(rbJeopardize);
	grupo.add(rbNotJeopardize);
	rbNotJeopardize.setSelected(true);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	loadData();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	explorationRequestClass.setERDeclarationStatusAux(explorationRequestClass.getERDeclarationStatus());
	explorationRequestClass.setRecordERSwornStatement(NO);
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	setData();
	this.dispose();
    }

    private void setData() {
	if (rbJeopardize.isSelected()) {
	    explorationRequestClass.setSwornstatement(false);
	} else {
	    explorationRequestClass.setSwornstatement(true);
	}
	explorationRequestClass.setERDeclarationStatusAux(COMPLETE);
	explorationRequestClass.setRecordERSwornStatement(YES);
    }

    private void loadData() {
	if (explorationRequestClass.isSwornstatement()) {
	    rbJeopardize.setSelected(false);
	} else {
	    rbJeopardize.setSelected(true);
	}
    }

}
