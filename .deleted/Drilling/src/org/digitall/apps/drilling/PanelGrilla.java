package org.digitall.apps.drilling;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.digitall.lib.components.Grilla;
import org.digitall.lib.components.JPColorLabel;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTable;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.AddButton;

public class PanelGrilla extends BasicContainerPanel {

    private BasicScrollPane jScrollPane1 = new BasicScrollPane();
    private AddButton bNew = new AddButton();
    private AcceptButton bUpdate = new AcceptButton();
    private BasicTable tabla = new BasicTable();
    private int Liminf = 0, CantReg1 = 200;
    private int[] tcol = { };
    private int[] tamcol;
    private Grilla jgGrilla;
    private Vector cabecera = new Vector();
    private Vector datos = new Vector();
    private String Consulta = "", ConsultaCount = "";
    String opcion = "";

    public PanelGrilla(String _opcion) {
	try {
	    opcion = _opcion;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(481, 207));
	//System.out.println("valor de opcion = "+opcion);
	if (opcion.equals("qaqc")) {
	    tamcol = new int[3];
	    tamcol[0] = 100;
	    tamcol[1] = 280;
	    tamcol[2] = 50;
	} else {
	    tamcol = new int[3];
	    tamcol[0] = 66;
	    tamcol[1] = 136;
	    tamcol[2] = 229;
	}
	jgGrilla = new Grilla(CantReg1, tcol, tamcol, false, false);
	jgGrilla.setBounds(new Rectangle(5, 10, 470, 165));
	jgGrilla.setSize(new Dimension(470, 163));
	bUpdate.setBounds(new Rectangle(230, 175, 40, 25));
	bUpdate.setSize(new Dimension(40, 25));
	bUpdate.setMnemonic('P');
	bUpdate.setEnabled(false);
	bUpdate.setFont(new Font("Dialog", 1, 10));
	bUpdate.setHorizontalTextPosition(SwingConstants.LEFT);
	bUpdate.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			bUpdate_actionPerformed(e);
		    }

		});
	jgGrilla.Redimensiona();
	jScrollPane1.setBounds(new Rectangle(5, 25, 700, 210));
	jScrollPane1.setSize(new Dimension(380, 210));
	bNew.setBounds(new Rectangle(435, 175, 40, 25));
	bNew.setMnemonic('N');
	bNew.setFont(new Font("Dialog", 1, 10));
	bNew.setSize(new Dimension(40, 25));
	bNew.setToolTipText("New");
	bNew.setHorizontalTextPosition(SwingConstants.LEFT);
	bNew.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			New_actionPerformed(e);
		    }

		});
	this.add(bUpdate, null);
	this.add(bNew, null);
	this.add(jgGrilla, null);
	jgGrilla.setSpanishLanguage(false);
	jgGrilla.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1 && e.getButton() == e.BUTTON1) {
			    datos = jgGrilla.VDatos();
			    bUpdate.setEnabled(true);
			}
		    }

		});
	jgGrilla.getTable().addMouseListener(new MouseAdapter() {

		    public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2 && e.getButton() == e.BUTTON1) {
			    Vector datos = new Vector();
			    datos = jgGrilla.VDatos();
			    //System.out.println("Fila --> "+datos);
			    NewElement nuevo = new NewElement(opcion, "update", datos.elementAt(0).toString());
			    nuevo.setModal(true);
			    nuevo.setVisible(true);
			    actualizaTabla();
			}
		    }

		});
	setearCabecera();
	actualizaTabla();
	formatearGrilla();
    }

    private void New_actionPerformed(ActionEvent e) {
	NewElement nuevo = new NewElement(opcion, "insert");
	nuevo.setModal(true);
	nuevo.setVisible(true);
	actualizaTabla();
    }

    private void bUpdate_actionPerformed(ActionEvent e) {
	Vector datos = new Vector();
	datos = jgGrilla.VDatos();
	//System.out.println("Fila --> "+datos);
	NewElement nuevo = new NewElement(opcion, "update", datos.elementAt(0).toString());
	nuevo.setModal(true);
	nuevo.setVisible(true);
	actualizaTabla();
    }

    private void setearCabecera() {
	cabecera.removeAllElements();
	cabecera.addElement("*");
	if (!opcion.equals("qaqc")) {
	    cabecera.addElement("Symbol");
	}
	cabecera.addElement("Name");
	cabecera.addElement("Description");
	if (opcion.equals("qaqc")) {
	    cabecera.addElement("Color");
	}
    }

    private void actualizaTabla() {
	if (opcion.equals("min")) {
	    Consulta = "SELECT idmin_tab, code, name, description FROM tabs.mineralization_tabs WHERE (mineralization_tabs.status <> '*' AND idmin_tab > 0) Order by name";
	}
	if (opcion.equals("alt")) {
	    Consulta = "SELECT idalt_tab, code , name , description FROM tabs.alteration_tabs WHERE( alteration_tabs.status <> '*' AND idalt_tab > 0) Order by name";
	}
	if (opcion.equals("lith")) {
	    Consulta = "SELECT idlit_tab, code , name , description  FROM tabs.lithology_tabs WHERE( lithology_tabs.status <> '*' AND idlit_tab > 0) Order by name";
	}
	if (opcion.equals("struc")) {
	    Consulta = "SELECT idstr_tab, code , name , description FROM tabs.structure_tabs WHERE( structure_tabs.status <> '*' AND idstr_tab > 0) Order by name";
	}
	if (opcion.equals("qaqc")) {
	    Consulta = "SELECT idqaqc, name, description, 'Color:' || qaqc_tabs.redcolor || ',' || qaqc_tabs.greencolor || ',' || qaqc_tabs.bluecolor AS color " + "  FROM tabs.qaqc_tabs WHERE( qaqc_tabs.estado <> '*' AND idqaqc > 0) Order by name";
	}
	//System.out.println("Consulta --> "+Consulta);
	ConsultaCount = "SELECT count(*) FROM (" + Consulta + ") as mat";
	jgGrilla.ActualizaTabla((BasicPanel)this, Consulta, ConsultaCount, cabecera);
	bUpdate.setEnabled(false);
    }

    private void formatearGrilla() {
	if (opcion.equals("qaqc")) {
	    jgGrilla.getTable().setDefaultRenderer(JPColorLabel.class, new ExpiredTableCellRenderer());
	}
    }

    class ExpiredTableCellRenderer implements TableCellRenderer {

	public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
	    JPColorLabel exp = (JPColorLabel)object;
	    return exp;
	}

    }

}
