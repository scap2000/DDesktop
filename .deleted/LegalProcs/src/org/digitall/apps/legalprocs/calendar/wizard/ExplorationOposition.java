package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class ExplorationOposition extends BasicDialog {

    private BasicPanel jPanel1 = new BasicPanel();
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private JTextArea jTextArea1 = new JTextArea();
    private BasicLabel jLabel1 = new BasicLabel();
    private String title;
    private TFInput tfDate = new TFInput(DataTypes.DATE, "Date", false, true);

    public ExplorationOposition(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception ex) {
	    ex.printStackTrace();
	} finally {

	}
    }

    public ExplorationOposition() {
	this(null, "", false);
    }

    public ExplorationOposition(Frame parent, String title, boolean modal) {
	super(parent, title, modal);
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(420, 461));
	this.setTitle(title);
	this.getContentPane().setLayout(null);
	jPanel1.setBounds(new Rectangle(10, 55, 385, 320));
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel1.setLayout(null);
	btnClose.setBounds(new Rectangle(355, 400, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(10, 400, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	jScrollPane1.setBounds(new Rectangle(15, 95, 355, 215));
	jLabel1.setText("Descripcion:");
	jLabel1.setBounds(new Rectangle(15, 75, 70, 15));
	jLabel1.setFont(new Font("Default", 1, 11));
	jLabel1.setSize(new Dimension(85, 15));
	tfDate.setBounds(new Rectangle(15, 10, 115, 55));
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(jPanel1, null);
	jPanel1.add(tfDate, null);
	jPanel1.add(jLabel1, null);
	jScrollPane1.getViewport().add(jTextArea1, null);
	jPanel1.add(jScrollPane1, null);
	tfDate.autoSize();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    }

}
