package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JRadioButton;

import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;

public class ExplorationPublications extends BasicDialog {

    private String title;
    private BasicPanel jPanel1 = new BasicPanel();
    private JRadioButton rbtnNewsPaper2 = new JRadioButton();
    private JRadioButton rbtnDoor2 = new JRadioButton();
    private JRadioButton rbtnBulletin2 = new JRadioButton();
    private BasicLabel jLabel1 = new BasicLabel();
    private CloseButton btnClose = new CloseButton();
    private AcceptButton btnAccept = new AcceptButton();
    private BasicPanel jPanel2 = new BasicPanel();
    private JRadioButton rbtnBulletin1 = new JRadioButton();
    private JRadioButton rbtnDoor1 = new JRadioButton();
    private JRadioButton rbtnNewsPaper1 = new JRadioButton();
    private BasicLabel jLabel2 = new BasicLabel();
    private TFInput tfDate1 = new TFInput(DataTypes.DATE, "Date", false, true);
    private TFInput tfNumber1 = new TFInput(DataTypes.INTEGER, "Number", false, true);
    private TFInput tfPage1 = new TFInput(DataTypes.INTEGER, "Page", false, true);
    private TFInput tfDate2 = new TFInput(DataTypes.DATE, "Date", false, true);
    private TFInput tfNumber2 = new TFInput(DataTypes.INTEGER, "Number", false, true);
    private TFInput tfPage2 = new TFInput(DataTypes.INTEGER, "Page", false, true);

    public ExplorationPublications(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(501, 383));
	this.setTitle(title);
	this.getContentPane().setLayout(null);
	jPanel1.setBounds(new Rectangle(10, 195, 475, 100));
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel1.setLayout(null);
	rbtnNewsPaper2.setText("Diario");
	rbtnNewsPaper2.setBounds(new Rectangle(15, 25, 60, 18));
	rbtnNewsPaper2.setFont(new Font("Default", 1, 11));
	rbtnNewsPaper2.setSize(new Dimension(70, 18));
	rbtnDoor2.setText("Puerta del Escribano");
	rbtnDoor2.setBounds(new Rectangle(155, 25, 150, 18));
	rbtnDoor2.setSize(new Dimension(160, 18));
	rbtnDoor2.setFont(new Font("Default", 1, 11));
	rbtnBulletin2.setText("Boletin Oficial");
	rbtnBulletin2.setBounds(new Rectangle(345, 25, 110, 18));
	rbtnBulletin2.setSize(new Dimension(120, 18));
	rbtnBulletin2.setFont(new Font("Default", 1, 11));
	jLabel2.setText(" 1º Publicacion: ");
	jLabel2.setBounds(new Rectangle(25, 50, 110, 15));
	jLabel2.setFont(new Font("Default", 1, 12));
	jLabel2.setSize(new Dimension(110, 15));
	jLabel2.setOpaque(false);
	tfDate1.setBounds(new Rectangle(15, 55, 100, 35));
	tfPage1.setBounds(new Rectangle(345, 55, 55, 35));
	tfPage1.setSize(new Dimension(60, 35));
	tfDate2.setBounds(new Rectangle(15, 55, 115, 35));
	tfDate2.setSize(new Dimension(100, 35));
	tfNumber2.setBounds(new Rectangle(155, 55, 80, 35));
	tfNumber2.setSize(new Dimension(80, 35));
	tfPage2.setBounds(new Rectangle(345, 55, 60, 35));
	tfPage2.setSize(new Dimension(60, 35));
	tfNumber1.setBounds(new Rectangle(155, 55, 80, 35));
	tfNumber1.setSize(new Dimension(80, 35));
	jLabel1.setText(" 2º Publicacion: ");
	jLabel1.setBounds(new Rectangle(25, 185, 110, 15));
	jLabel1.setFont(new Font("Default", 1, 12));
	jLabel1.setSize(new Dimension(110, 15));
	jLabel1.setOpaque(false);
	btnClose.setBounds(new Rectangle(445, 320, 40, 25));
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnAccept.setBounds(new Rectangle(10, 320, 40, 25));
	btnAccept.setSize(new Dimension(40, 25));
	jPanel2.setBounds(new Rectangle(10, 65, 475, 100));
	jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel2.setLayout(null);
	rbtnBulletin1.setText("Boletin Oficial");
	rbtnBulletin1.setBounds(new Rectangle(345, 25, 110, 18));
	rbtnBulletin1.setSize(new Dimension(120, 18));
	rbtnBulletin1.setFont(new Font("Default", 1, 11));
	rbtnDoor1.setText("Puerta del Escribano");
	rbtnDoor1.setBounds(new Rectangle(155, 25, 150, 18));
	rbtnDoor1.setSize(new Dimension(160, 18));
	rbtnDoor1.setFont(new Font("Default", 1, 11));
	rbtnNewsPaper1.setText("Diario");
	rbtnNewsPaper1.setBounds(new Rectangle(15, 25, 60, 18));
	rbtnNewsPaper1.setFont(new Font("Default", 1, 11));
	rbtnNewsPaper1.setSize(new Dimension(70, 18));
	jPanel1.add(tfPage2, null);
	jPanel1.add(tfNumber2, null);
	jPanel1.add(tfDate2, null);
	jPanel1.add(rbtnBulletin2, null);
	jPanel1.add(rbtnDoor2, null);
	jPanel1.add(rbtnNewsPaper2, null);
	jPanel2.add(rbtnBulletin1, null);
	jPanel2.add(rbtnDoor1, null);
	jPanel2.add(rbtnNewsPaper1, null);
	jPanel2.add(tfNumber1, null);
	jPanel2.add(tfPage1, null);
	jPanel2.add(tfDate1, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(jLabel1, null);
	this.getContentPane().add(jPanel1, null);
	this.getContentPane().add(jPanel2, null);
	tfDate1.autoSize();
	tfNumber1.autoSize();
	tfPage1.autoSize();
	tfDate2.autoSize();
	tfNumber2.autoSize();
	tfPage2.autoSize();
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    }

}
