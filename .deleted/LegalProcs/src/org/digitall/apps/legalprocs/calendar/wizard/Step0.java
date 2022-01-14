package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JRadioButton;

import org.digitall.apps.legalprocs.manager.classes.FileClass;
import org.digitall.lib.components.basic.BasicPanel;

//

public class Step0 extends PanelWizard {

    private JRadioButton rbtnMine = new JRadioButton();
    private JRadioButton rbtnExploration = new JRadioButton();
    private BasicPanel jPanel1 = new BasicPanel();
    private String title;
    private JDesktopPane parentDesktop;
    private FileClass fileClass;

    public Step0(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step0(String _title, JDesktopPane _parentDesktop, FileClass _fileClass) {
	try {
	    title = _title;
	    parentDesktop = _parentDesktop;
	    fileClass = _fileClass;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(391, 99));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName(title);
	rbtnMine.setText("Adquisicion Mina");
	rbtnMine.setBounds(new Rectangle(25, 60, 150, 18));
	rbtnMine.setSize(new Dimension(150, 18));
	rbtnMine.setFont(new Font("Default", 1, 13));
	rbtnMine.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    rbtnMine_actionPerformed(e);
				}

			    }
	);
	rbtnExploration.setText("Cateo o Solictud de Permiso de Exploracion");
	rbtnExploration.setBounds(new Rectangle(25, 20, 335, 20));
	rbtnExploration.setSize(new Dimension(335, 18));
	rbtnExploration.setSelected(true);
	rbtnExploration.setFont(new Font("Default", 1, 13));
	rbtnExploration.addActionListener(new ActionListener() {

				       public void actionPerformed(ActionEvent e) {
					   rbtnExploration_actionPerformed(e);
				       }

				   }
	);
	this.add(rbtnMine, null);
	this.add(rbtnExploration, null);
	ButtonGroup optionButtonGroup = new ButtonGroup();
	optionButtonGroup.add(rbtnExploration);
	optionButtonGroup.add(rbtnMine);
    }

    private void rbtnMine_actionPerformed(ActionEvent e) {
	setStep(1);
    }

    private void rbtnExploration_actionPerformed(ActionEvent e) {
	setStep(0);
    }

}
