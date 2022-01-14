package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.FindButton;
import org.digitall.lib.components.inputpanels.TFInputPanel;
import org.digitall.lib.data.DataTypes;

//

public class FindExplorationsSchemePanel extends BasicContainerPanel {

    private BasicPanel jpFindES = new BasicPanel();
    private BasicLabel lblResults = new BasicLabel();
    private BasicPanel jpFindES1 = new BasicPanel();
    private int Liminf = 0, CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol = { 304, 150 };
    private Grilla jgExplorationScheme = new Grilla(CantReg1, tcol, tamcol, false, false);
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private String Consulta, ConsultaCount = "";
    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private BasicButton btnSelect = new BasicButton();
    private CloseButton btnClose = new CloseButton();
    private BasicButton btnNew = new BasicButton();
    private FindButton btnSearch = new FindButton();
    private TFInputPanel tfipMineName = new TFInputPanel(DataTypes.STRING, "MineName", false, true);
    private TFInputPanel tfipFileNumber = new TFInputPanel(DataTypes.INTEGER, "Number", false, true);
    private TFInputPanel tfipFileLetter = new TFInputPanel(DataTypes.STRING, "FileLetter", false, true);
    private TFInputPanel tfipFileYear = new TFInputPanel(DataTypes.INTEGER, "FileYear", false, true);
    private BasicLabel lblExptes = new BasicLabel();

    public FindExplorationsSchemePanel(BasicInternalFrame _parent) {
	try {
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public FindExplorationsSchemePanel(BasicDialog _parent) {
	try {
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public FindExplorationsSchemePanel(JFrame _parent) {
	try {
	    parent = _parent;
	    parentType = FRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(506, 329));
	jpFindES.setBounds(new Rectangle(5, 10, 495, 50));
	jpFindES.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jpFindES.setLayout(null);
	lblResults.setText(" Results");
	lblResults.setBounds(new Rectangle(15, 75, 50, 10));
	lblResults.setFont(new Font("Dialog", 1, 11));
	lblResults.setOpaque(false);
	jpFindES1.setBounds(new Rectangle(5, 75, 495, 210));
	jpFindES1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jpFindES1.setLayout(null);
	jgExplorationScheme.setBounds(new Rectangle(5, 5, 485, 200));
	btnSelect.setBounds(new Rectangle(395, 295, 105, 25));
	btnSelect.setText("Seleccionar");
	btnClose.setBounds(new Rectangle(5, 295, 40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	btnNew.setBounds(new Rectangle(215, 295, 85, 25));
	btnNew.setText("Nuevo");
	btnNew.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  btnNew_actionPerformed(e);
			      }

			  }
	);
	btnSearch.setBounds(new Rectangle(445, 13, 35, 25));
	btnSearch.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnSearch_actionPerformed(e);
				 }

			     }
	);
	tfipMineName.setBounds(new Rectangle(5, 5, 185, 35));
	tfipFileNumber.setBounds(new Rectangle(205, 5, 70, 35));
	tfipFileLetter.setBounds(new Rectangle(290, 5, 55, 35));
	tfipFileYear.setBounds(new Rectangle(355, 5, 55, 35));
	lblExptes.setBounds(new Rectangle(15, 65, 85, 15));
	lblExptes.setText(" Expedientes");
	lblExptes.setFont(new Font("Default", 1, 11));
	lblExptes.setOpaque(false);
	this.add(lblExptes, null);
	this.add(btnNew, null);
	this.add(btnClose, null);
	this.add(btnSelect, null);
	jpFindES1.add(jgExplorationScheme, null);
	this.add(jpFindES1, null);
	this.add(jpFindES, null);
	jpFindES.add(tfipFileYear, null);
	jpFindES.add(tfipFileLetter, null);
	jpFindES.add(tfipFileNumber, null);
	jpFindES.add(tfipMineName, null);
	jpFindES.add(btnSearch, null);
	jgExplorationScheme.Redimensiona();
	jgExplorationScheme.getTable().addKeyListener(new KeyAdapter() {

						   public void keyPressed(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

						   public void keyReleased(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

						   public void keyTyped(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

					       }
	);
	jgExplorationScheme.getTable().addKeyListener(new KeyAdapter() {

						   public void keyPressed(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

						   public void keyReleased(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

						   public void keyTyped(KeyEvent e) {
						       jgExplorationScheme_keyTyped(e);
						   }

					       }
	);
	/**
        * Accion que se dispara cuando se hace "UN CLICK" en la grilla
        * */
	jgExplorationScheme.getTable().addMouseListener(new MouseAdapter() {

						     public void mouseClicked(MouseEvent e) {
							 if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
							     /*if (datos != jgUsuarios.VDatos()) {
                                                               datos = jgUsuarios.VDatos();
                                                               habilitaBotones(true);
                                                               //setearGroups();
                                                           }*/
							 }
						     }

						 }
	);
	/**
        * Accion que se dispara cuando se hace "DOBLE CLICK" en la grilla
        * */
	jgExplorationScheme.getTable().addMouseListener(new MouseAdapter() {

						     public void mouseClicked(MouseEvent e) {
							 if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
							     /*datos = jgUsuarios.VDatos();
                                                           PersonalData datosPersonales = new PersonalData(datos.elementAt(0).toString());
                                                           datosPersonales.setModal(true);
                                                           datosPersonales.setVisible(true);*/
							 }
						     }

						 }
	);
	setearCabecera();
	actualizaTabla();
    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG :
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME :
		((BasicInternalFrame)parent).dispose();
		break;
	    case FRAME :
		((JFrame)parent).dispose();
		break;
	}
    }

    private void setearCabecera() {
	cabecera.removeAllElements();
	cabecera.addElement("*");
	cabecera.addElement("*");
	cabecera.addElement("*");
	cabecera.addElement("*");
	cabecera.addElement("Nombre de la Mina");
	cabecera.addElement("Expediente");
    }

    private void actualizaTabla() {
	armarConsulta();
	ConsultaCount = "SELECT count(*) FROM (" + Consulta + ") as mat";
	jgExplorationScheme.ActualizaTabla((BasicPanel)this, Consulta, ConsultaCount, cabecera);
	//System.out.println("Consulta Count --> "+ConsultaCount);
	System.out.println("Consulta --> " + Consulta);
    }

    private void armarConsulta() {
	String ConsultaInicial = "";
	String filtroMineName = "";
	String filtroFileNumber = "";
	String filtroFileLetter = "";
	String filtroYear = "";
	String OrderBy = "";
	ConsultaInicial = "SELECT idfile, filenumber, fileletter, fileyear,minename, (filenumber|| ' - ' || fileletter || ' - ' || fileyear) as expediente FROM file.files WHERE idfile <> 0 AND filenumber <> 0 AND estado <> '*' ";
	if (!tfipMineName.getText().trim().equals("")) {
	    filtroMineName = " AND lower(minename) LIKE lower('%" + tfipMineName.getText().trim() + "%')";
	}
	if (!tfipFileNumber.getText().trim().equals("")) {
	    filtroFileNumber = " AND filenumber = " + tfipFileNumber.getText().trim();
	}
	if (!tfipFileLetter.getText().trim().equals("")) {
	    filtroFileLetter = " AND lower(fileletter) LIKE lower('&" + tfipFileLetter.getText().trim() + "&')  ";
	}
	if (!tfipFileYear.getText().trim().equals("")) {
	    filtroYear = " AND fileyear = " + tfipFileYear.getText().trim();
	}
	OrderBy = " ORDER BY filenumber";
	Consulta = ConsultaInicial + filtroMineName + filtroFileNumber + filtroFileLetter + filtroYear + OrderBy;
	//System.out.println("Consulta --> "+Consulta);
    }

    private void jgExplorationScheme_keyTyped(KeyEvent e) {
	try {
	    if (datos != jgExplorationScheme.VDatos()) {
		datos = jgExplorationScheme.VDatos();
		//setearGroups();
		//habilitaBotones(true);
	    }
	} catch (ArrayIndexOutOfBoundsException x) {
	    e.consume();
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	dispose();
    }

    private void btnNew_actionPerformed(ActionEvent e) {
	ExplorationScheme explorationScheme = new ExplorationScheme();
	explorationScheme.setModal(true);
	explorationScheme.setVisible(true);
	dispose();
    }

    private void btnSearch_actionPerformed(ActionEvent e) {
	actualizaTabla();
    }

}
