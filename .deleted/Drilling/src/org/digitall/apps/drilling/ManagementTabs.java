package org.digitall.apps.drilling;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

public class ManagementTabs extends BasicDialog {

    private JTabbedPane jTabbedPane1 = new JTabbedPane();
    private CloseButton bCancel = new CloseButton();
    private PanelGrilla panel1;
    private PanelGrilla panel2;
    private PanelGrilla panel3;
    private PanelGrilla panel4;
    private PanelGrilla panel5;
    private PanelHeader panelHeader;
    final String TEXTO = "Manage Tabs";

    public ManagementTabs() {
	this(null, "", false);
    }

    public ManagementTabs(Frame parent, String title, boolean modal) {
	super(parent, title, modal);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(512, 331));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Management Tabs");
	panel1 = new PanelGrilla("min");
	panel2 = new PanelGrilla("alt");
	panel3 = new PanelGrilla("lith");
	panel4 = new PanelGrilla("struc");
	panel5 = new PanelGrilla("qaqc");
	jTabbedPane1.setBounds(new Rectangle(10, 40, 485, 230));
	bCancel.setBounds(new Rectangle(10, 275, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setToolTipText("Cancel");
	bCancel.setSize(new Dimension(40, 25));
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	panel1.setSize(new Dimension(480, 223));
	jTabbedPane1.addTab("Mineralizations", panel1);
	jTabbedPane1.addTab("Alterations", panel2);
	jTabbedPane1.addTab("Lithologies", panel3);
	jTabbedPane1.addTab("Structures", panel4);
	jTabbedPane1.addTab("QAQC", panel5);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(jTabbedPane1, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
    }

    private void addLithlogy() {
	String consultaInsert = "";
    }

    private void addStructure() {
	String consultaInsert = "";
    }

    private void addMineralization() {
	String consultaInsert = "";
	consultaInsert = "INSERT INTO tabs.mineralization_tabs VALUES ((Select max(idmin_tab) + 1 From tabs.mineralization_tabs), '";
	//+ tfSymbol.getText() +"' , '"+ tfName.getText() +"', '"+ taDescription.getText() +"', '') ";
    }

    private void addAlteration() {
	String consultaInsert = "";
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}
