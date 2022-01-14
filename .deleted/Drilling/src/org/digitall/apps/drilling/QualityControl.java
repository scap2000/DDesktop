package org.digitall.apps.drilling;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;
import org.digitall.lib.sql.LibSQL;

//

public class QualityControl extends BasicDialog {

    private BasicScrollPane spQualityControl = new BasicScrollPane();
    private JTextArea taQualityControl = new JTextArea();
     
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    final String TEXTO = "Quality Control";
    private PanelHeader panelHeader;
    private String idproject = "";

    public QualityControl(String _idproject) {
	try {
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(404, 379));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(btnAccept, null);
	//
	this.getContentPane().setLayout(null);
	this.setTitle("Quality Control");
	spQualityControl.setBounds(new Rectangle(10, 45, 380, 250));
	taQualityControl.setBounds(new Rectangle(0, 0, 380, 250));
	taQualityControl.setWrapStyleWord(true);
	taQualityControl.setLineWrap(true);
	//new Rectangle(0, 306, 395, 5));
	btnAccept.setBounds(new Rectangle(350, 320, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.setToolTipText("Accept");
	btnAccept.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnAccept_actionPerformed(e);
		    }

		});
	btnClose.setBounds(new Rectangle(10, 320, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.setToolTipText("Close");
	btnClose.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnClose_actionPerformed(e);
		    }

		});
	spQualityControl.getViewport().add(taQualityControl, null);
	this.getContentPane().add(spQualityControl, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setData();
    }

    private void setData() {
	String consulta = "SELECT qualitycontrol FROM drilling.projects WHERE idproject = " + idproject;
	taQualityControl.setText(org.digitall.lib.sql.LibSQL.getCampo(consulta));
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	String Update = "UPDATE drilling.projects SET qualitycontrol = '" + taQualityControl.getText() + "' WHERE idproject = " + idproject;
	//System.out.println("update: " + Update);
	if (LibSQL.exActualizar('m', Update)) {
	    org.digitall.lib.components.Advisor.messageBox("Update Success!", "Message");
	    this.dispose();
	} else {
	    org.digitall.lib.components.Advisor.messageBox("Data wasn't registered", "Error");
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

}
