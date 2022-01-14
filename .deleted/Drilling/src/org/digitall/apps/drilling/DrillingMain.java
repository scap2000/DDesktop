package org.digitall.apps.drilling;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

//

public class DrillingMain extends JFrame {

    private PanelHeader panelHeader;
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel statusBar = new BasicLabel();
    private AcceptButton bNext = new AcceptButton();
    private CloseButton bClose = new CloseButton();
    private BasicPanel panelCenter = new BasicPanel();
    private BorderLayout layoutMain = new BorderLayout();
    private JRadioButton rbCreate = new JRadioButton();
    private JRadioButton rbManage = new JRadioButton();
    private JRadioButton rbManageLith = new JRadioButton();
    private JRadioButton rbImport = new JRadioButton();
    private ButtonGroup grupo = new ButtonGroup();
    final String TEXTO = "<html><p align=center>W E L C O M E !!!<br>Project Wizard</p></html>";

    public DrillingMain() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(360, 193));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.setTitle("Drilling Project - Main Frame");
	this.setBounds(new Rectangle(10, 10, 360, 193));
	this.getContentPane().setBackground(new Color(48, 48, 48));
	this.getContentPane().setLayout(layoutMain);
	panelCenter.setLayout(null);
	this.setResizable(false);
	rbCreate.setText("Create New Project");
	rbCreate.setBounds(new Rectangle(80, 10, 195, 15));
	rbManage.setText("Manage an existing Project");
	rbManage.setBounds(new Rectangle(80, 25, 195, 20));
	rbManage.setMnemonic('M');
	rbManage.setFont(new Font("Dialog", 1, 12));
	jPanel1.setBounds(new Rectangle(5, 40, 345, 90));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	rbImport.setText("Import File");
	rbImport.setBounds(new Rectangle(80, 65, 230, 20));
	rbImport.setFont(new Font("Dialog", 1, 12));
	rbImport.setMnemonic('R');
	//rbImport.setEnabled(false);
	rbManageLith.setText("Manage tabs");
	rbManageLith.setBounds(new Rectangle(80, 45, 240, 20));
	rbManageLith.setFont(new Font("Dialog", 1, 12));
	rbManageLith.setMnemonic('G');
	bNext.setBounds(new Rectangle(300, 135, 50, 25));
	bNext.setMnemonic('N');
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bClose.setBounds(new Rectangle(5, 135, 50, 25));
	bClose.setMnemonic('C');
	bClose.setToolTipText("Close");
	bClose.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bClose_actionPerformed(e);
		    }

		});
	statusBar.setText("");
	this.getContentPane().add(statusBar, BorderLayout.SOUTH);
	jPanel1.add(rbImport, null);
	jPanel1.add(rbManageLith, null);
	jPanel1.add(rbCreate, null);
	jPanel1.add(rbManage, null);
	panelCenter.add(jPanel1, null);
	panelCenter.add(bClose, null);
	panelCenter.add(bNext, null);
	grupo.add(rbCreate);
	grupo.add(rbManage);
	grupo.add(rbManageLith);
	grupo.add(rbImport);
	this.getContentPane().add(panelCenter, BorderLayout.CENTER);
	rbCreate.setSelected(true);
	rbCreate.setMnemonic('t');
	rbCreate.setFont(new Font("Dialog", 1, 12));
    }

    private PasoAsistente[] instancia(int cantpasos, String opcion, Coordinador _coord) {
	if (opcion.equals("n")) {
	    PasoAsistente[] formularios = new PasoAsistente[cantpasos];
	    formularios[0] = new Paso0();
	    formularios[1] = new Paso1();
	    formularios[2] = new Paso2();
	    formularios[3] = new Paso3();
	    formularios[4] = new Paso4();
	    formularios[5] = new Paso5();
	    return formularios;
	} else {
	    System.out.println("Error, no ingreso por el if principal");
	    return null;
	}
    }

    void fileExit_ActionPerformed(ActionEvent e) {
	//System.exit(0);
	dispose();
    }

    void helpAbout_ActionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(this, new DrillingMain_AboutBoxPanel1(), "About", JOptionPane.PLAIN_MESSAGE);
    }

    private void bNext_actionPerformed(ActionEvent e) {
	if (rbCreate.isSelected()) {
	    Coordinador coord = new Coordinador();
	    coord.setCantidadArgumentos(15);
	    int cantpasos = 6;
	    coord.setCantPasos(cantpasos);
	    coord.setPasos(instancia(cantpasos, "n", coord));
	    coord.iniciar();
	}
	if (rbManage.isSelected()) {
	    FindProject BuscarProyecto = new FindProject();
	    BuscarProyecto.setModal(true);
	    BuscarProyecto.setVisible(true);
	}
	if (rbManageLith.isSelected()) {
	    ManagementTabs man = new ManagementTabs();
	    man.setModal(true);
	    man.setVisible(true);
	}
	if (this.rbImport.isSelected()) {
	    //ImportFiles importar = new ImportFiles();
	    FindLabResult findLabResult = new FindLabResult();
	    findLabResult.setModal(true);
	    findLabResult.setVisible(true);
	}
    }

    private void bClose_actionPerformed(ActionEvent e) {
	//System.exit(0);
	dispose();
    }

    private void bX_actionPerformed(ActionEvent e) {
    }

}
