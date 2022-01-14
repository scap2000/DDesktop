package org.digitall.apps.drilling;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.common.drilling.ShowData;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.JTFecha;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.icons.IconTypes;

//

public class FindDrills extends BasicDialog implements KeyListener {

    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblDate = new BasicLabel();
    private BasicLabel lblResults = new BasicLabel();
    private BasicLabel lblFinDrill = new BasicLabel();
    private BasicLabel lblProjectName = new BasicLabel();
    private BasicLabel lblProject = new BasicLabel();
    private FindButton bSearch = new FindButton();
    private NextWizardButton bNext = new NextWizardButton();
    private CloseButton bCancel = new CloseButton();
    private AddButton bNewDrill = new AddButton();
    private ModifyButton bModify = new ModifyButton();
    private BasicPanel pFound = new BasicPanel();
    private BasicTextField tfName = new BasicTextField();
    private JTFecha tfDate = new JTFecha();
    private int Liminf = 0, CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol = { 105, 90, 145, 145 };
    private Grilla jgFindProject = new Grilla(CantReg1, tcol, tamcol, false, false);
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private BasicPanel pResults = new BasicPanel();
    private String ConsultaInicial = "", Consulta = "", ConsultaCount = "", idproject = "", iddrill = "", proyecto = "";
    final String TEXTO = "Drills";
    private PanelHeader panelHeader;
     

    public FindDrills(String _idproject) {
	try {
	    idproject = _idproject;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(563, 396));
	this.setBounds(new Rectangle(10, 10, 563, 392));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblProject, null);
	this.getContentPane().add(panelHeader, null);
	//
	this.getContentPane().add(lblProjectName, null);
	this.getContentPane().add(lblFinDrill, null);
	this.getContentPane().add(lblResults, null);
	this.getContentPane().add(bModify, null);
	this.getContentPane().add(bNewDrill, null);
	pResults.add(jgFindProject, null);
	this.getContentPane().add(pResults, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(pFound, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Drills");
	this.setFont(new Font("Dialog", 1, 12));
	pFound.setBounds(new Rectangle(5, 70, 545, 40));
	pFound.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pFound.setLayout(null);
	tfName.setBounds(new Rectangle(85, 10, 170, 20));
	tfName.setFont(new Font("Dialog", 1, 10));
	tfName.setForeground(Color.red);
	tfName.addKeyListener(this);
	tfDate.setBounds(new Rectangle(330, 10, 95, 20));
	tfDate.setFont(new Font("Dialog", 1, 10));
	tfDate.addKeyListener(this);
	lblName.setText("DDH:");
	lblName.setBounds(new Rectangle(50, 15, 30, 10));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblDate.setText("Date:");
	lblDate.setBounds(new Rectangle(300, 15, 30, 10));
	lblDate.setFont(new Font("Dialog", 1, 10));
	bSearch.setBounds(new Rectangle(495, 8, 40, 25));
	bSearch.setFont(new Font("Dialog", 1, 10));
	bSearch.setMnemonic('S');
	bSearch.setToolTipText("Search");
	bSearch.setSize(new Dimension(40, 25));
	bSearch.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bSearch_actionPerformed(e);
		    }

		});
	jgFindProject.setBounds(new Rectangle(5, 5, 535, 185));
	jgFindProject.Redimensiona();
	lblResults.setText(" Results");
	lblResults.setBounds(new Rectangle(15, 120, 50, 10));
	lblResults.setFont(new Font("Dialog", 1, 10));
	lblResults.setOpaque(true);
	pResults.setBounds(new Rectangle(5, 125, 545, 200));
	pResults.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pResults.setLayout(null);
	//new Rectangle(0, 55, 560, 5));
	findProjectName();
	lblProject.setText("" + proyecto);
	lblProject.setBounds(new Rectangle(100, 40, 380, 15));
	lblProject.setFont(new Font("Dialog", 1, 11));
	lblProject.setHorizontalAlignment(SwingConstants.LEFT);
	lblProject.setForeground(Color.blue);
	lblFinDrill.setText(" Find Drill");
	lblFinDrill.setBounds(new Rectangle(15, 65, 55, 10));
	lblFinDrill.setFont(new Font("Dialog", 1, 10));
	lblFinDrill.setOpaque(true);
	lblProjectName.setText("Project Name: ");
	lblProjectName.setBounds(new Rectangle(15, 40, 85, 15));
	lblProjectName.setFont(new Font("Dialog", 1, 11));
	lblProjectName.setHorizontalAlignment(SwingConstants.LEFT);
	bModify.setBounds(new Rectangle(220, 335, 40, 25));
	bModify.setToolTipText("Modify Drill Data");
	bModify.setMnemonic('M');
	bModify.setEnabled(false);
	bNewDrill.setBounds(new Rectangle(300, 335, 40, 25));
	bNewDrill.setFont(new Font("Dialog", 1, 10));
	bNewDrill.setMnemonic('W');
	bNewDrill.setToolTipText("New drill");
	bNewDrill.setSize(new Dimension(40, 25));
	bNewDrill.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNewDrill_actionPerformed(e);
		    }

		});
	bNext.setBounds(new Rectangle(510, 335, 40, 25));
	bNext.setMnemonic('N');
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setEnabled(false);
	bNext.setSize(new Dimension(40, 25));
	bNext.setToolTipText("Next");
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 335, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setToolTipText("Cancel");
	bCancel.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bCancel_actionPerformed(e);
		    }

		});
	pFound.add(bSearch, null);
	pFound.add(lblName, null);
	pFound.add(tfDate, null);
	pFound.add(tfName, null);
	pFound.add(lblDate, null);
	jgFindProject.getTable().addKeyListener(new KeyAdapter() {

		    public void keyPressed(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		    public void keyReleased(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		    public void keyTyped(KeyEvent e) {
			jgFindProject_keyTyped(e);
		    }

		});
	/**
    * Accion que se dispara cuando se hace "UN CLICK" en la grilla
    * */
	jgFindProject.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
			    datos = jgFindProject.VDatos();
			    /*if (buscar)
            {
              //bseleccionar.setEnabled(true);
              //nameproject.setText(datos.elementAt(1).toString());
              //idproject.setText(datos.elementAt(0).toString());
            } else
            {
              //bseleccionar.setEnabled(false);
            }*/
			    iddrill = datos.elementAt(0).toString();
			    controlaBotones(true);
			}
		    }

		});
	/**
    * Accion que se dispara cuando se hace "DOBLE CLICK" en la grilla
    * */
	jgFindProject.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
			    datos = jgFindProject.VDatos();
			    iddrill = datos.elementAt(0).toString();
			    /*ProjectMan AdministradorProyecto = new ProjectMan(datos.elementAt(0).toString());
            AdministradorProyecto.setModal(true);
            AdministradorProyecto.setEnabled(true);*/
			    ShowData VerDatos = new ShowData(idproject, iddrill, datos.elementAt(1).toString());
			    VerDatos.setModal(true);
			    VerDatos.setVisible(true);
			}
		    }

		});
	jgFindProject.setSpanishLanguage(false);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	setearCabecera();
	actualizaTabla();
	controlaBotones(false);
	bModify.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bModify_actionPerformed(e);
		    }

		});
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void bNext_actionPerformed(ActionEvent e) {
	//System.out.println("iddrill----> "+iddrill);
	ShowData VerDatos = new ShowData(idproject, iddrill, datos.elementAt(1).toString());
	VerDatos.setModal(true);
	VerDatos.setVisible(true);
    }

    private void bSearch_actionPerformed(ActionEvent e) {
	actualizaTabla();
    }

    private void setearCabecera() {
	cabecera.removeAllElements();
	cabecera.addElement("*");
	cabecera.addElement("DDH");
	cabecera.addElement("Date");
	cabecera.addElement("UTM E.");
	cabecera.addElement("UTM N.");
    }

    private void actualizaTabla() {
	armarConsulta();
	ConsultaCount = "SELECT count(*) FROM (" + Consulta + ") as mat";
	jgFindProject.ActualizaTabla((BasicDialog)this, Consulta, ConsultaCount, cabecera);
    }

    private void armarConsulta() {
	String filtroName = "", filtroFecha = "";
	ConsultaInicial = "SELECT iddrill,ddh,date,X(utm),Y(utm) FROM drilling.drills WHERE idproject = " + idproject + " AND iddrill > 0 ";
	if (!tfName.getText().equals("")) {
	    filtroName = " AND upper(ddh) Like upper('%" + tfName.getText() + "%') ";
	}
	if (!tfDate.getText().equals("")) {
	    filtroFecha = " AND date = '" + Proced.setFormatDate(tfDate.getText(), false) + "'";
	}
	String OrderBy = "ORDER BY iddrill";
	Consulta = ConsultaInicial + filtroName + filtroFecha + OrderBy;
    }

    private void jgFindProject_keyTyped(KeyEvent e) {
	try {
	    if (datos != jgFindProject.VDatos()) {
		datos = jgFindProject.VDatos();
		iddrill = datos.elementAt(0).toString();
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

    private void bNewDrill_actionPerformed(ActionEvent e) {
	NewDrill newDrill = new NewDrill(idproject);
	this.setVisible(false);
	newDrill.setModal(true);
	newDrill.setVisible(true);
	actualizaTabla();
	this.setVisible(true);
    }

    private void bModify_actionPerformed(ActionEvent e) {
	ModifyDrill modificaDrill = new ModifyDrill(idproject, iddrill);
	this.setVisible(false);
	modificaDrill.setModal(true);
	modificaDrill.setVisible(true);
	actualizaTabla();
	controlaBotones(false);
	this.setVisible(true);
    }

    private void controlaBotones(boolean _estado) {
	bNext.setEnabled(_estado);
	bModify.setEnabled(_estado);
    }

    public void keyPressed(KeyEvent key) {
	if (key.getKeyCode() == 10) {
	    Component c = (Component)key.getSource();
	    if (key.getSource() == tfName) {
		actualizaTabla();
	    } else if (key.getSource() == tfDate) {
		actualizaTabla();
	    }
	}
    }

    public void keyReleased(KeyEvent keyEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    private void findProjectName() {
	proyecto = org.digitall.lib.sql.LibSQL.getCampo("SELECT name FROM drilling.projects WHERE idproject = " + idproject);
    }

}
