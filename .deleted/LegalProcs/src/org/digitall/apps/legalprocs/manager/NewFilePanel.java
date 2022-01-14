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

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;

//

public class NewFilePanel extends BasicContainerPanel {

    private final int FRAME = 1;
    private final int INTERNALFRAME = 2;
    private final int DIALOG = 3;
    private int parentType = -1;
    private Component parent;
    private BasicPanel jpExpteTramite = new BasicPanel();
    private BasicPanel jpGrilla = new BasicPanel();
    private int CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol = { 200, 100, 230, 123, 123, 200, 70 };
    private Grilla jgTramites = new Grilla(CantReg1, tcol, tamcol, false, false);
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private String Consulta = "", ConsultaCount = "", ConsultaInicial = "";
    private CBInput cbApplications = new CBInput(CachedCombo.APPLICATION_TABS, "Applications", true);
    private TFInput tfFileNumber = new TFInput(DataTypes.INTEGER, "FileNumber", true);
    private TFInput tfFileLetter = new TFInput(DataTypes.STRING, "FileLetter", true);
    private TFInput tfFileYear = new TFInput(DataTypes.INTEGER, "FileYear", true);
    private TFInput tfExpirationDate = new TFInput(DataTypes.DATE, "ExpirationDate", true);
    private TFInput tfMineName = new TFInput(DataTypes.STRING, "MineName", false);
    private TFInput tfDescription = new TFInput(DataTypes.STRING, "Description", false);
    private TFInput tfStartAppDate = new TFInput(DataTypes.DATE, "StartDate", false);
    private AssignButton btnAddApplication = new AssignButton(true);
    private DeleteButton btnDeleteApplication = new DeleteButton();
    private AcceptButton btnRegister = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private int error = 0;
    private JRadioButton rbCateo = new JRadioButton();
    private JRadioButton rbMine = new JRadioButton();
    private BasicLabel lblType = new BasicLabel();
    private ButtonGroup typeGroup = new ButtonGroup();
    private CBInput cbProvince = new CBInput(CachedCombo.PROVINCE_TABS, "ProvinceState", true);
    private TFInput tfRegisterDate = new TFInput(DataTypes.DATE, "RegisterDate", false);
    private TFInput tfNotificationDate = new TFInput(DataTypes.DATE, "", false);

    public NewFilePanel(BasicInternalFrame _parent) {
	try {
	    parent = _parent;
	    parentType = INTERNALFRAME;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public NewFilePanel(BasicDialog _parent) {
	try {
	    parent = _parent;
	    parentType = DIALOG;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public NewFilePanel(JFrame _parent) {
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
	this.setSize(new Dimension(560, 598));
	jpExpteTramite.setBounds(new Rectangle(5, 0, 545, 275));
	jpExpteTramite.setLayout(null);
	jpExpteTramite.setBorder(BorderPanel.getBorderPanel("Expediente", Color.black, Color.black));
	jpGrilla.setBounds(new Rectangle(5, 280, 550, 260));
	jpGrilla.setLayout(null);
	jpGrilla.setBorder(BorderPanel.getBorderPanel("Trï¿½mites", Color.black, Color.black));
	cbApplications.setBounds(new Rectangle(10, 130, 515, 35));
	cbApplications.autoSize();
	btnRegister.setBounds(new Rectangle(5, 555, 40, 25));
	btnRegister.addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       btnRegister_actionPerformed(e);
				   }

			       }
	);
	jgTramites.setBounds(new Rectangle(8, 15, 535, 205));
	cbApplications.setBounds(new Rectangle(15, 190, 515, 35));
	jgTramites.Redimensiona();
	tfFileNumber.setBounds(new Rectangle(15, 55, 105, 35));
	tfFileNumber.autoSize();
	tfFileNumber.setEditable(true);
	tfFileLetter.setBounds(new Rectangle(135, 55, 80, 35));
	tfFileLetter.autoSize();
	tfFileLetter.setEditable(true);
	tfFileYear.setBounds(new Rectangle(225, 55, 80, 35));
	tfFileYear.autoSize();
	tfFileYear.setEditable(true);
	tfExpirationDate.setBounds(new Rectangle(200, 230, 125, 35));
	tfExpirationDate.autoSize();
	tfExpirationDate.setEditable(false);
	tfExpirationDate.setVisible(false);
	btnAddApplication.setBounds(new Rectangle(420, 240, 110, 25));
	btnAddApplication.setText("Agregar");
	btnAddApplication.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     btnAddApplication_actionPerformed(e);
					 }

				     }
	);
	btnDeleteApplication.setBounds(new Rectangle(420, 225, 110, 25));
	btnDeleteApplication.setText(" Eliminar");
	btnDeleteApplication.addActionListener(new ActionListener() {

					    public void actionPerformed(ActionEvent e) {
						btnDeleteApplication_actionPerformed(e);
					    }

					}
	);
	btnClose.setBounds(new Rectangle(510, 555, 40, 25));
	btnClose.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    btnClose_actionPerformed(e);
				}

			    }
	);
	rbCateo.setText("Cateo");
	rbCateo.setBounds(new Rectangle(90, 20, 70, 20));
	rbCateo.addMouseListener(new MouseAdapter() {

			      public void mouseClicked(MouseEvent e) {
				  rbCateo_mouseClicked(e);
			      }

			  }
	);
	rbCateo.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   rbCateo_actionPerformed(e);
			       }

			   }
	);
	rbMine.setText("Mina");
	rbMine.setBounds(new Rectangle(160, 20, 65, 20));
	lblType.setText("Tipo:");
	lblType.setBounds(new Rectangle(20, 20, 60, 20));
	lblType.setFont(new Font("Default", 1, 11));
	lblType.setHorizontalAlignment(SwingConstants.RIGHT);
	cbProvince.setBounds(new Rectangle(300, 10, 230, 35));
	cbProvince.autoSize();
	cbProvince.setSelectedValue("JUJUY");
	tfRegisterDate.setBounds(new Rectangle(370, 55, 110, 35));
	tfRegisterDate.autoSize();
	tfRegisterDate.setEditable(false);
	tfRegisterDate.setVisible(false);
	tfStartAppDate.setBounds(new Rectangle(15, 230, 110, 35));
	tfStartAppDate.autoSize();
	tfStartAppDate.setEditable(true);
	tfStartAppDate.setVisible(false);
	tfMineName.setBounds(new Rectangle(15, 95, 290, 35));
	tfMineName.autoSize();
	tfMineName.setEditable(true);
	tfDescription.setBounds(new Rectangle(15, 135, 515, 35));
	tfDescription.autoSize();
	tfDescription.setEditable(true);
	this.add(btnClose, null);
	this.add(jpGrilla, null);
	this.add(jpExpteTramite, null);
	this.add(btnRegister, null);
	jpGrilla.add(jgTramites, null);
	jpGrilla.add(btnDeleteApplication, null);
	jpExpteTramite.add(tfRegisterDate, null);
	jpExpteTramite.add(cbProvince, null);
	jpExpteTramite.add(lblType, null);
	jpExpteTramite.add(rbMine, null);
	jpExpteTramite.add(rbCateo, null);
	jpExpteTramite.add(tfStartAppDate, null);
	jpExpteTramite.add(tfMineName, null);
	jpExpteTramite.add(btnAddApplication, null);
	jpExpteTramite.add(tfExpirationDate, null);
	jpExpteTramite.add(tfFileYear, null);
	jpExpteTramite.add(tfFileLetter, null);
	jpExpteTramite.add(tfFileNumber, null);
	jpExpteTramite.add(cbApplications, null);
	jpExpteTramite.add(tfDescription, null);
	typeGroup.add(rbCateo);
	typeGroup.add(rbMine);
	rbMine.setSelected(true);
	rbMine.addMouseListener(new MouseAdapter() {

			     public void mouseClicked(MouseEvent e) {
				 rbMine_mouseClicked(e);
			     }

			 }
	);
	rbMine.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  rbMine_actionPerformed(e);
			      }

			  }
	);
	jgTramites.getTable().addKeyListener(new KeyAdapter() {

					  public void keyPressed(KeyEvent e) {
					      jgTramites_keyTyped(e);
					  }

					  public void keyReleased(KeyEvent e) {
					      jgTramites_keyTyped(e);
					  }

					  public void keyTyped(KeyEvent e) {
					      jgTramites_keyTyped(e);
					  }

				      }
	);
	/**
        * Accion que se dispara cuando se hace "UN CLICK" en la grilla
        * */
	jgTramites.getTable().addMouseListener(new MouseAdapter() {

					    public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
						    datos = jgTramites.VDatos();
						    btnDeleteApplication.setEnabled(true);
						}
					    }

					}
	);
	/**
        * Accion que se dispara cuando se hace "DOBLE CLICK" en la grilla
        * */
	jgTramites.getTable().addMouseListener(new MouseAdapter() {

					    public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {

						}
					    }

					}
	);
	btnDeleteApplication.setEnabled(false);
	setearCabecera();
	actualizaTabla();
	setCombo();
	//setFields();
    }

    private void jgTramites_keyTyped(KeyEvent e) {

    }

    private void dispose() {
	switch (parentType) {
	    case DIALOG :
		((BasicDialog)parent).dispose();
		break;
	    case INTERNALFRAME :
		((BasicInternalFrame)parent).setVisible(false);
		((BasicInternalFrame)parent).hide();
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
	cabecera.addElement("Nombre");
	cabecera.addElement("Expediente");
	cabecera.addElement("Tramite");
	cabecera.addElement("Fecha Inicio");
	cabecera.addElement("Fecha Vencimiento");
	cabecera.addElement("Descripciï¿½n");
	cabecera.addElement("Usuario");
    }

    private void actualizaTabla() {
	makeSQL();
	ConsultaCount = "SELECT count(*) FROM (" + Consulta + ") as mat";
	jgTramites.ActualizaTabla((BasicPanel)this, Consulta, ConsultaCount, cabecera);
    }

    private void makeSQL() {
	String filtroName = "", filtroFecha = "";
	ConsultaInicial = "SELECT filenumber, fileletter, fileyear, idapplication_tab,minename, (filenumber||'-'|| fileletter||'-'||fileyear) AS expediente, applicationname, applicationstartdate,date,description,username FROM file.applicationlist";
	String OrderBy = "";
	//ORDER BY expediente";
	Consulta = ConsultaInicial + filtroName + filtroFecha + OrderBy;
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	dispose();
    }

    private void btnAddApplication_actionPerformed(ActionEvent e) {
	String insertApplication = makeSQLInsert();
	//System.out.println("insertApplication --> "+ insertApplication);
	if (!insertApplication.equals("")) {
	    LibSQL.exActualizar('a', insertApplication);
	    actualizaTabla();
	    clearFields();
	    btnDeleteApplication.setEnabled(false);
	}
    }

    private String makeSQLInsert() {
	String sqlInsert = "", startDate = "", fileRegisterDate = "", expirationDate = "";
	String idProvince = cbProvince.getSelectedValue().toString();
	//System.out.println("idprovince: " + idProvince);
	if (tfExpirationDate.getString().equals("")) {
	    expirationDate = "null";
	} else {
	    expirationDate = "'" + Proced.setFormatDate(tfExpirationDate.getString(), false) + "'";
	}
	if (tfStartAppDate.getString().equals("")) {
	    startDate = "null";
	} else {
	    startDate = "'" + Proced.setFormatDate(tfStartAppDate.getString(), false) + "'";
	}
	if (tfRegisterDate.getString().equals("")) {
	    fileRegisterDate = "null";
	} else {
	    fileRegisterDate = "'" + Proced.setFormatDate(tfRegisterDate.getString(), false) + "'";
	}
	tfNotificationDate.setValue("");
	if (tfNotificationDate.getString().equals("")) {
	    fileRegisterDate = "null";
	} else {
	    fileRegisterDate = "'" + Proced.setFormatDate(tfRegisterDate.getString(), false) + "'";
	}
	if (control()) {
	    //sqlInsert = " INSERT INTO file.applicationlist VALUES(" + tfFileNumber.getText() + " ,'" + tfFileLetter.getText().toUpperCase().trim() + "' ," + tfFileYear.getText() + " ," + cbApplications.getSelectedValue() + " ,'" + cbApplications.getSelectedItem() + "' , '" + Proced.setFormatDate(tfExpirationDate.getText(), false) + "' , 0,'" + LibSQL.getSQLUser() + "','" + tfMineName.getText().trim() + "', " + startDate + ", '" + tfDescription.getText().trim() + "'," + rbMine.isSelected() + ", " + idProvince + ", " + fileRegisterDate + ");";
	    sqlInsert = " INSERT INTO file.applicationlist VALUES(" + tfFileNumber.getString() + " ,'" + tfFileLetter.getString().toUpperCase().trim() + "' ," + tfFileYear.getString() + " ," + cbApplications.getSelectedValue() + " ,'" + cbApplications.getSelectedItem() + "' ,  " + expirationDate + " , 0,'" + Environment.sessionUser + "','" + tfMineName.getString() + "', " + startDate + ", '" + tfDescription.getString() + "'," + rbMine.isSelected() + ", " + idProvince + ", " + fileRegisterDate + ");";
	} else {
	    msjError();
	}
	return sqlInsert;
    }

    private boolean control() {
	if (tfFileNumber.getString().equals("")) {
	    error = 1;
	    return false;
	} else if (tfFileLetter.getString().equals("")) {
	    error = 2;
	    return false;
	} else if (tfFileYear.getString().equals("")) {
	    error = 3;
	    return false;
	}/*else if (!((Integer.parseInt(tfFileYear.getText().trim()) > 1900) && (Integer.parseInt(tfFileYear.getText().trim()) <= Integer.parseInt(Environment.currentYear)))) {
	    error = 4;
	    return false;
	} */ else if (tfMineName.getString().equals("")) {
	    error = 5;
	    return false;
	}/* else if (tfExpirationDate.getText().trim().equals("")) {
	    error = 6;
	    return false;
	}*/ else {
	    return true;
	}
    }

    private void msjError() {
	if (error == 1) {
	    Advisor.messageBox("El campo Numero estï¿½ vacio", "Error");
	}
	if (error == 2) {
	    Advisor.messageBox("El campo Letra estï¿½ vacio", "Error");
	}
	if (error == 3) {
	    Advisor.messageBox("El campo Aï¿½o estï¿½ vacio", "Error");
	}
	if (error == 4) {
	    Advisor.messageBox("El campo Aï¿½o debe ser:  1900 > aï¿½o <= aï¿½o actual", "Error");
	}
	if (error == 5) {
	    Advisor.messageBox("El campo Nombre de la mina estï¿½ vacio", "Error");
	}
	if (error == 6) {
	    Advisor.messageBox("El campo Fecha estï¿½ vacio", "Error");
	}
    }

    private void clearFields() {
	tfFileNumber.setValue("");
	tfFileLetter.setValue("");
	tfFileYear.setValue("");
	tfMineName.setValue("");
	tfExpirationDate.setValue("");
    }

    private void btnDeleteApplication_actionPerformed(ActionEvent e) {
	String sqlDelete = makeDelete();
	if (LibSQL.exActualizar('m', sqlDelete)) {
	    actualizaTabla();
	    btnDeleteApplication.setEnabled(false);
	}
    }

    private String makeDelete() {
	String delete = " DELETE FROM file.applicationlist WHERE filenumber = " + datos.elementAt(0).toString() + " AND fileletter = '" + datos.elementAt(1) + "' AND fileyear = " + datos.elementAt(2).toString() + " AND idapplication_tab = " + datos.elementAt(3).toString() + ";";
	return delete;
    }

    private void btnRegister_actionPerformed(ActionEvent e) {
	//String superInsert = "SELECT file.addnewexpirations2('" + LibSQL.getSQLUser() + "');";
	String superInsert = "SELECT file.addFilesList('" + Environment.sessionUser + "');";
	ResultSet result = LibSQL.exQuery(superInsert);
	try {
	    if (result.next()) {
		if (result.getBoolean("addFilesList")) {
		    Advisor.messageBox("Insert success!!", "Message");
		    dispose();
		} else {
		    Advisor.messageBox("Ocurrio un error al insertar los datos.\n algunos datos no se registraron", "Message");
		}
	    } else {
		Advisor.messageBox("Ocurrio un error al insertar los datos.\n algunos datos no se registraron", "Message");
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private void rbCateo_mouseClicked(MouseEvent e) {
	tfMineName.setValue("Cateo");
	tfMineName.setEnabled(false);
    }

    private void rbMine_mouseClicked(MouseEvent e) {
	tfMineName.setValue("");
	tfMineName.setEditable(true);
    }

    private void setCombo() {
	cbApplications.setSelectedValue("Digitall - Primera Carga");
    }

    private void setFields() {
	/*String date = Proced.setFormatDate(Proced.FechaHora2(true, false), false);
	String expiredDate = "";
	try {
	    expiredDate = Proced.SumResFechaHora(date, true, "+", "60", "day", false);
	} catch (Exception e) {
	    // TODO
	}
	tfStartAppDate.setText(Proced.FechaHora2(true, false));
	tfExpirationDate.setText(expiredDate);*/
    }

    private void rbCateo_actionPerformed(ActionEvent e) {
	tfMineName.setValue("Cateo");
	tfMineName.setEditable(false);
    }

    private void rbMine_actionPerformed(ActionEvent e) {
	tfMineName.setValue("");
	tfMineName.setEnabled(true);
    }

}
