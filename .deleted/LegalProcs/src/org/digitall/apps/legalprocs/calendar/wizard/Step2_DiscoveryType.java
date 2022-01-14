package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JRadioButton;

import org.digitall.apps.legalprocs.manager.classes.FileClass;

public class Step2_DiscoveryType extends PanelWizard {

    private String title;
    private JRadioButton rbtnDiscovery = new JRadioButton();
    private JRadioButton rbtnExpired = new JRadioButton();
    private JRadioButton rbtnVacancy = new JRadioButton();
    private JDesktopPane parentDesktop;
    private FileClass fileClass;

    public Step2_DiscoveryType(String _title) {
	try {
	    title = _title;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public Step2_DiscoveryType(String _title, JDesktopPane _parentDesktop, FileClass _fileClass) {
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
	this.setSize(new Dimension(283, 122));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName(title);
	rbtnDiscovery.setText("Adquisicion de Mina por Descubrimiento");
	rbtnDiscovery.setBounds(new Rectangle(10, 10, 245, 18));
	rbtnDiscovery.setSize(new Dimension(245, 18));
	rbtnExpired.setText("Adquisicion por Mina Caduca ");
	rbtnExpired.setBounds(new Rectangle(10, 45, 245, 18));
	rbtnExpired.setSize(new Dimension(245, 18));
	rbtnExpired.setFont(new Font("Default", 0, 11));
	rbtnVacancy.setText("Adquisicion por Mina Vacante");
	rbtnVacancy.setBounds(new Rectangle(10, 85, 245, 18));
	rbtnVacancy.setSize(new Dimension(245, 18));
	rbtnVacancy.setFont(new Font("Default", 0, 11));
	this.add(rbtnVacancy, null);
	this.add(rbtnExpired, null);
	this.add(rbtnDiscovery, null);
	rbtnDiscovery.setSelected(true);
	rbtnDiscovery.setFont(new Font("Default", 0, 11));
	ButtonGroup typeGroup = new ButtonGroup();
	typeGroup.add(rbtnDiscovery);
	typeGroup.add(rbtnExpired);
	typeGroup.add(rbtnVacancy);
    }

}
