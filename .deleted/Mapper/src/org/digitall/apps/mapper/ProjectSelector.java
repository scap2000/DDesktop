package org.digitall.apps.mapper;

import java.awt.Color;
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
import javax.swing.JDesktopPane;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.PanelHeader;
import org.digitall.common.mapper.CoordinateViewer;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.JTFecha;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.icons.IconTypes;

//

public class ProjectSelector extends BasicInternalFrame implements KeyListener {

    private BasicLabel lblName = new BasicLabel();
    private BasicLabel lblDate = new BasicLabel();
    private BasicLabel lblResults = new BasicLabel();
    private FindButton bSearch = new FindButton();
    private NextWizardButton bNext = new NextWizardButton();
    private CloseButton bCancel = new CloseButton();
    private BasicPanel pFound = new BasicPanel();
    private BasicTextField tfName = new BasicTextField();
    private JTFecha tfDate = new JTFecha();
    private int Liminf = 0, CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol = { 135, 85, 230 };
    private Grilla jgFindProject = new Grilla(CantReg1, tcol, tamcol, false, false);
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private BasicPanel pResults = new BasicPanel();
    private String ConsultaInicial = "", Consulta = "", ConsultaCount = "";
    final String TEXTO = "Projects";
    private PanelHeader panelHeader;
    private BasicLabel lblFindProject = new BasicLabel();
    private JDesktopPane desktop;
    private CoordinateViewer coordinateViewer = new CoordinateViewer();

    public ProjectSelector(JDesktopPane _desktop) {
	desktop = _desktop;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	setClosable(true);
	setDefaultCloseOperation(BasicInternalFrame.HIDE_ON_CLOSE);
	setIconifiable(true);
	desktop.add(coordinateViewer);
	coordinateViewer.setVisible(false);
	this.setSize(new Dimension(486, 306));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(lblResults, null);
	this.getContentPane().add(lblFindProject, null);
	this.getContentPane().add(panelHeader, null);
	pResults.add(jgFindProject, null);
	this.getContentPane().add(pResults, null);
	this.getContentPane().add(bCancel, null);
	this.getContentPane().add(bNext, null);
	this.getContentPane().add(pFound, null);
	this.getContentPane().setLayout(null);
	this.setTitle("Projects");
	this.setFont(new Font("Dialog", 1, 12));
	this.setBounds(new Rectangle(10, 10, 486, 316));
	pFound.setBounds(new Rectangle(5, 45, 470, 40));
	pFound.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pFound.setLayout(null);
	tfName.setBounds(new Rectangle(45, 10, 170, 20));
	tfName.setFont(new Font("Dialog", 1, 10));
	tfName.setForeground(Color.red);
	tfName.addKeyListener(this);
	tfDate.setBounds(new Rectangle(275, 10, 95, 20));
	tfDate.setFont(new Font("Dialog", 1, 10));
	tfDate.addKeyListener(this);
	lblName.setText("Name:");
	lblName.setBounds(new Rectangle(10, 15, 40, 10));
	lblName.setFont(new Font("Dialog", 1, 10));
	lblDate.setText("Date:");
	lblDate.setBounds(new Rectangle(245, 15, 30, 10));
	lblDate.setFont(new Font("Dialog", 1, 10));
	bSearch.setBounds(new Rectangle(405, 8, 40, 25));
	bSearch.setFont(new Font("Dialog", 1, 10));
	bSearch.setMnemonic('S');
	bSearch.setToolTipText("Search");
	bSearch.setSize(new Dimension(40, 25));
	bSearch.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bSearch_actionPerformed(e);
		    }

		});
	jgFindProject.setBounds(new Rectangle(5, 5, 460, 145));
	jgFindProject.Redimensiona();
	lblResults.setText(" Results:");
	lblResults.setBounds(new Rectangle(10, 95, 50, 10));
	lblResults.setFont(new Font("Dialog", 1, 10));
	lblResults.setOpaque(true);
	pResults.setBounds(new Rectangle(5, 100, 470, 155));
	pResults.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	pResults.setLayout(null);
	lblFindProject.setText(" Find Project");
	lblFindProject.setBounds(new Rectangle(10, 40, 70, 10));
	lblFindProject.setFont(new Font("Dialog", 1, 10));
	lblFindProject.setOpaque(true);
	bNext.setBounds(new Rectangle(435, 260, 40, 25));
	bNext.setMnemonic('N');
	bNext.setFont(new Font("Dialog", 1, 10));
	bNext.setEnabled(false);
	bNext.setToolTipText("Next");
	bNext.setSize(new Dimension(40, 25));
	bNext.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bNext_actionPerformed(e);
		    }

		});
	bCancel.setBounds(new Rectangle(5, 260, 40, 25));
	bCancel.setMnemonic('C');
	bCancel.setFont(new Font("Dialog", 1, 10));
	bCancel.setToolTipText("Cancel");
	bCancel.setSize(new Dimension(40, 25));
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
			    bNext.setEnabled(true);
			}
		    }

		});
	/**
    * Accion que se dispara cuando se hace "DOBLE CLICK" en la grilla
    * */
	jgFindProject.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
			    openDialog();
			}
		    }

		});
	jgFindProject.setSpanishLanguage(false);
	setearCabecera();
	actualizaTabla();
    }

    public void openDialog() {
	datos = jgFindProject.VDatos();
	ProjectViewerOld viewer = new ProjectViewerOld(desktop, Integer.parseInt(datos.elementAt(0).toString()), datos.elementAt(1).toString());
	viewer.setCoordinateViewer(coordinateViewer);
	viewer.mostrar();
	desktop.add(viewer);
	viewer.setVisible(true);
	viewer.show();
	setearCabecera();
	actualizaTabla();
    }

    private void bCancel_actionPerformed(ActionEvent e) {
	setVisible(false);
	hide();
    }

    private void bNext_actionPerformed(ActionEvent e) {
	openDialog();
    }

    private void bSearch_actionPerformed(ActionEvent e) {
	actualizaTabla();
    }

    private void setearCabecera() {
	cabecera.removeAllElements();
	cabecera.addElement("*");
	cabecera.addElement("Project");
	cabecera.addElement("Date");
	cabecera.addElement("Description");
    }

    private void actualizaTabla() {
	armarConsulta();
	ConsultaCount = "SELECT count(*) FROM (" + Consulta + ") as mat";
	jgFindProject.ActualizaTabla((BasicInternalFrame)this, Consulta, ConsultaCount, cabecera);
	//System.out.println("Consulta Count --> "+ConsultaCount);
	//System.out.println("Consulta FrmFrenteObras --> "+Consulta);
    }

    private void armarConsulta() {
	ConsultaInicial = "SELECT idproject,name,date,description FROM drilling.projects WHERE idproject > 0 ";
	String filtroName = "", filtroFecha = "";
	//System.out.println("tfDate--> "+ tfDate.getText());
	//System.out.println("tfName--> "+ tfName.getText());
	if (!tfName.getText().equals("")) {
	    filtroName = " AND name Like ('%" + tfName.getText() + "%') ";
	}
	if (!tfDate.getText().equals("")) {
	    filtroFecha = "AND date = '" + Proced.setFormatDate(tfDate.getText(), false) + "'";
	}
	String OrderBy = " ORDER BY idproject";
	Consulta = ConsultaInicial + filtroName + filtroFecha + OrderBy;
	//System.out.println("Consulta --> "+Consulta);
    }

    private void jgFindProject_keyTyped(KeyEvent e) {
	try {
	    if (datos != jgFindProject.VDatos()) {
		datos = jgFindProject.VDatos();
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

    public void keyReleased(KeyEvent key) {
	if (key.getKeyCode() == KeyEvent.VK_ENTER) {
	    if (key.getSource() == tfName || key.getSource() == tfDate) {
		actualizaTabla();
	    }
	}
    }

    public void keyPressed(KeyEvent key) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

}
