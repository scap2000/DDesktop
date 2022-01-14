/**
 LIMITACIÓN DE RESPONSABILIDAD - COPYRIGHT - [Español]
 ================================================================================
 El Suri - Entorno JAVA de Trabajo y Desarrollo para Gobierno Electrónico
 ================================================================================

 Información del Proyecto:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 por D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO es propiedad de
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) y
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 Este programa es software libre: usted puede redistribuirlo y/o modificarlo
 bajo los términos de la Licencia Pública General GNU publicada
 por la Fundación para el Software Libre, ya sea la versión 3
 de la Licencia, o (a su elección) cualquier versión posterior.

 Este programa se distribuye con la esperanza de que sea útil, pero
 SIN GARANTÍA ALGUNA; ni siquiera la garantía implícita
 MERCANTIL o de APTITUD PARA UN PROPÓSITO DETERMINADO.
 Consulte los detalles de la Licencia Pública General GNU para obtener
 una información más detallada.

 Debería haber recibido una copia de la Licencia Pública General GNU
 junto a este programa.
 En caso contrario, consulte <http://www.gnu.org/licenses/>.

 DISCLAIMER - COPYRIGHT - [English]
 =====================================================================================
 El Suri - JAVA Management & Development Environment for Electronic Government
 =====================================================================================

 Project Info:  http://elsuri.sourceforge.net

 Copyright (C) 2007-2010 by D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO.
 D'AMBROSIO MARCELO E CASSINA SANTIAGO SOC DE HECHO is owned by
 Lic. Santiago Cassina (santiago@digitallsh.com.ar - scap2000@yahoo.com) and
 Marcelo D'Ambrosio (marcelo@digitallsh.com.ar - marcelodambrosio@gmail.com)
 http://www.digitallsh.com.ar

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/
/**
 * F1q6.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTable;
import org.digitall.lib.components.buttons.AddComboButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;
import org.digitall.projects.unsa.censodocpri.classes.TableItem;

public class F1q6 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private BasicTable table;
    private DefaultTableModel tableModel;
    
    private JArea lblf1q6 = new JArea();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();
    private AddComboButton btnAddRow = new AddComboButton();
    private BasicScrollPane jsList;
    private BasicLabel lblOtras = new BasicLabel();

    public F1q6(Coordinador _coordinador) {
	coordinador = _coordinador;
	encuesta = coordinador.getEncuesta();
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	contentPane.setLayout( null );
	contentPane.setSize(new Dimension(675, 323));
	lblf1q6.setText(encuesta.getF1q6Label());

	lblf1q6.setBounds(new Rectangle(15, 25, 645, 55));
	lblf1q6.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q6.getTextArea().setForeground(Color.white);
	lblf1q6.getTextArea().setOpaque(false);
	lblf1q6.setEditable(false);
	
	this.setContent(contentPane);
	this.setSize(new Dimension(675, 364));
	addButton(btnNext);
	addButton(btnPrevious);
	
	tableModel = new DefaultTableModel();
	table = new BasicTable(tableModel);
	
	tableModel.addColumn("Categorías");
	tableModel.addColumn("Subcategorías");
	tableModel.addColumn("Opción");
	tableModel.setRowCount(0);
	
	TableColumn columnaOpcion = table.getColumnModel().getColumn(2);
	BasicCombo comboBox = new BasicCombo();
	comboBox.addItem("Ninguna");
	comboBox.addItem("Muy negativamente");
	comboBox.addItem("Negativamente");
	comboBox.addItem("Sin influencia");
	comboBox.addItem("Positivamente");
	comboBox.addItem("Muy positivamente");
	columnaOpcion.setCellEditor(new DefaultCellEditor(comboBox));

	lblOtras.setText("Otras (aumente todas las que usted considere)");
	lblOtras.setBounds(new Rectangle(50, 285, 420, 25));
	lblOtras.setFont(new Font("Lucida Sans", 1, 12));
	Vector<TableItem> f1q6 = encuesta.getF1q6();

	for (int i = 0; i < f1q6.size(); i++) {
	    tableModel.addRow(new Object[] {f1q6.elementAt(i).getCategoria(), f1q6.elementAt(i).getSubcategoria(), f1q6.elementAt(i).getOpcion()});
	}
	jsList = new BasicScrollPane(table);

	contentPane.add(lblOtras, null);
	contentPane.add(jsList, null);
	contentPane.add(lblf1q6, null);
	contentPane.add(btnAddRow, null);
	btnNext.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnNext_actionPerformed(e);
		}
	    });
	btnPrevious.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPrevious_actionPerformed(e);
		}
	    });
	btnAddRow.setBounds(new Rectangle(15, 285, 30, 25));
	btnAddRow.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnAddRow_actionPerformed(e);
		}
	    });
	addButton(btnNext);
	addButton(btnPrevious);
	table.setEnabled(coordinador.isWritable());
	jsList.setBounds(new Rectangle(15, 85, 645, 195));
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    Vector<TableItem> f1q6 = encuesta.getF1q6();
	    f1q6.removeAllElements();
	    for (int i = 0; i < table.getRowCount(); i++) {
		f1q6.add(new TableItem(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), table.getValueAt(i,2).toString()));
	    }
	}
	coordinador.showF1q7();
    }
    
    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q5();
    }

    private void addRow() {
	String _categoria = (String)JOptionPane.showInputDialog(this, "Ingrese la categoría", "Nombre de la categoría", JOptionPane.QUESTION_MESSAGE, null, null, "");
	if (_categoria != null) {
	    String _subcategoria = (String)JOptionPane.showInputDialog(this, "Ingrese la subcategoría", "Nombre de la subcategoría", JOptionPane.QUESTION_MESSAGE, null, null, "");
	    if (_subcategoria != null) {
		String _opcion = (String)JOptionPane.showInputDialog(this, "Seleccione una opción", "Opción", JOptionPane.QUESTION_MESSAGE, null, 
			    new Object[] { "Ninguna", "Muy negativamente", "Negativamente", "Sin influencia", "Positivamente", "Muy positivamente"}, "");
		if (_opcion != null) {
		    tableModel.addRow(new Object[] { _categoria, _subcategoria, _opcion } );
		}
	    }
	}
	
    }

    private void btnAddRow_actionPerformed(ActionEvent e) {
	addRow();
    }
    
}
