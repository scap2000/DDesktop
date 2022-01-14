package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;

import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

public class ExplorationGrantPermision extends BasicDialog {

    private String title;
    private BasicPanel jPanel1 = new BasicPanel();
    private TFInputPanel tfDate = new TFInputPanel(DataTypes.DATE, "Date", false, true);
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private TFInputPanel tfNumber = new TFInputPanel(DataTypes.INTEGER, "Number", false, false);

    public ExplorationGrantPermision(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(400, 200));
	this.setTitle(title);
	this.getContentPane().setLayout(null);
	jPanel1.setBounds(new Rectangle(12, 45, 370, 65));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	tfDate.setBounds(new Rectangle(230, 15, 100, 35));
	tfDate.setSize(new Dimension(100, 35));
	btnClose.setBounds(new Rectangle(345, 140, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(15, 140, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	tfNumber.setBounds(new Rectangle(40, 15, 95, 35));
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	jPanel1.add(tfNumber, null);
	jPanel1.add(tfDate, null);
	this.getContentPane().add(jPanel1, null);
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    }

}
