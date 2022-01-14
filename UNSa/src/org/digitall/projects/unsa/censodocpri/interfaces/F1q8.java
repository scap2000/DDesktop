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
 * F1q8.java
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

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTable;
import org.digitall.lib.components.buttons.AddComboButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.environment.Environment;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;
import org.digitall.projects.unsa.censodocpri.classes.TableItem;

public class F1q8 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private BasicTable table;
    private DefaultTableModel tableModel;
    private JArea lblf1q8 = new JArea();

    private JArea lblf1q8Opinion = new JArea();
    private ButtonGroup f1q8OpcionesGroup = new ButtonGroup();
    private BasicRadioButton rbf1q8Opcion1 = new BasicRadioButton();
    private BasicRadioButton rbf1q8Opcion2 = new BasicRadioButton();
    private BasicRadioButton rbf1q8Opcion3 = new BasicRadioButton();
    private BasicRadioButton rbf1q8Opcion4 = new BasicRadioButton();
    private BasicRadioButton rbf1q8Opcion5 = new BasicRadioButton();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();
    private AddComboButton btnAddRow = new AddComboButton();
    private BasicLabel lblOtras = new BasicLabel();
    private BasicScrollPane jsList;

    public F1q8(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(675, 330));
	lblf1q8.setText(encuesta.getF1q8Label());

	lblf1q8.setBounds(new Rectangle(15, 25, 640, 55));
	lblf1q8.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q8.getTextArea().setForeground(Color.white);
	lblf1q8.getTextArea().setOpaque(false);
	lblf1q8.setEditable(false);
	this.setContent(contentPane);
	this.setSize(new Dimension(675, 372));
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
	comboBox.addItem("Inútiles");
	comboBox.addItem("Insuficientes");
	comboBox.addItem("Relevantes");
	comboBox.addItem("Indispensables");
	columnaOpcion.setCellEditor(new DefaultCellEditor(comboBox));

	Vector<TableItem> f1q8 = encuesta.getF1q8();

	for (int i = 0; i < f1q8.size(); i++) {
	    tableModel.addRow(new Object[] { f1q8.elementAt(i).getCategoria(), f1q8.elementAt(i).getSubcategoria(), f1q8.elementAt(i).getOpcion() });
	}
	jsList = new BasicScrollPane(table);

	lblf1q8Opinion.setText(encuesta.getF1q8OpinionLabel());
	lblf1q8Opinion.setBounds(new Rectangle(15, 265, 640, 25));
	lblf1q8Opinion.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q8Opinion.getTextArea().setForeground(Color.white);
	lblf1q8Opinion.getTextArea().setOpaque(false);
	lblf1q8Opinion.setEditable(false);

	f1q8OpcionesGroup.add(rbf1q8Opcion1);
	f1q8OpcionesGroup.add(rbf1q8Opcion2);
	f1q8OpcionesGroup.add(rbf1q8Opcion3);
	f1q8OpcionesGroup.add(rbf1q8Opcion4);
	f1q8OpcionesGroup.add(rbf1q8Opcion5);

	rbf1q8Opcion1.setText(encuesta.getF1q8OpinionOpciones()[0]);
	rbf1q8Opcion1.setBounds(new Rectangle(15, 300, 135, 20));
	rbf1q8Opcion1.setSelected(encuesta.getF1q8Opinion().equals(encuesta.getF1q8OpinionOpciones()[0]));

	rbf1q8Opcion2.setText(encuesta.getF1q8OpinionOpciones()[1]);
	rbf1q8Opcion2.setBounds(new Rectangle(165, 300, 170, 20));
	rbf1q8Opcion2.setSelected(encuesta.getF1q8Opinion().equals(encuesta.getF1q8OpinionOpciones()[1]));

	rbf1q8Opcion3.setText(encuesta.getF1q8OpinionOpciones()[2]);
	rbf1q8Opcion3.setBounds(new Rectangle(355, 300, 135, 20));
	rbf1q8Opcion3.setSelected(encuesta.getF1q8Opinion().equals(encuesta.getF1q8OpinionOpciones()[2]));

	rbf1q8Opcion4.setText(encuesta.getF1q8OpinionOpciones()[3]);
	rbf1q8Opcion4.setBounds(new Rectangle(520, 300, 135, 20));
	rbf1q8Opcion4.setSelected(encuesta.getF1q8Opinion().equals(encuesta.getF1q8OpinionOpciones()[3]));

	rbf1q8Opcion5.setText(encuesta.getF1q8OpinionOpciones()[4]);
	rbf1q8Opcion5.setBounds(new Rectangle(15, 335, 135, 20));
	rbf1q8Opcion5.setSelected(encuesta.getF1q8Opinion().equals(encuesta.getF1q8OpinionOpciones()[4]));

	lblOtras.setText("Otras (aumente todas las que usted considere)");
	lblOtras.setBounds(new Rectangle(50, 225, 420, 25));
	lblOtras.setFont(new Font("Lucida Sans", 1, 12));

	contentPane.add(lblOtras, null);
	contentPane.add(jsList, null);
	contentPane.add(lblf1q8, null);
	contentPane.add(rbf1q8Opcion1, null);
	contentPane.add(rbf1q8Opcion2, null);
	contentPane.add(rbf1q8Opcion3, null);
	contentPane.add(rbf1q8Opcion4, null);
	contentPane.add(rbf1q8Opcion5, null);
	contentPane.add(lblf1q8Opinion, null);
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
	table.setEnabled(coordinador.isWritable());
	rbf1q8Opcion1.setEnabled(coordinador.isWritable());
	rbf1q8Opcion2.setEnabled(coordinador.isWritable());
	rbf1q8Opcion3.setEnabled(coordinador.isWritable());
	rbf1q8Opcion4.setEnabled(coordinador.isWritable());
	rbf1q8Opcion5.setEnabled(coordinador.isWritable());

	btnAddRow.setBounds(new Rectangle(15, 225, 30, 25));
	btnAddRow.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnAddRow_actionPerformed(e);
		}
	    });
	addButton(btnNext);
	addButton(btnPrevious);
	jsList.setBounds(new Rectangle(15, 85, 640, 135));

    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    Vector<TableItem> f1q8 = encuesta.getF1q8();
	    f1q8.removeAllElements();
	    for (int i = 0; i < table.getRowCount(); i++) {
	        f1q8.add(new TableItem(table.getValueAt(i, 0).toString(), table.getValueAt(i, 1).toString(), table.getValueAt(i, 2).toString()));
	    }
	    encuesta.setF1q8Opinion(rbf1q8Opcion1.isSelected()?rbf1q8Opcion1.getText():
	                            rbf1q8Opcion2.isSelected()?rbf1q8Opcion2.getText():
				    rbf1q8Opcion3.isSelected()?rbf1q8Opcion3.getText():
				    rbf1q8Opcion4.isSelected()?rbf1q8Opcion4.getText():
	                            rbf1q8Opcion5.isSelected()?rbf1q8Opcion5.getText():"");
	    
	}
	coordinador.showF1q9();
    }
    
    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q7();
    }

    private void addRow() {
	String _categoria = (String)JOptionPane.showInputDialog(this, "Ingrese la categoría", "Nombre de la categoría", JOptionPane.QUESTION_MESSAGE, null, null, "");
	if (_categoria != null) {
	    String _subcategoria = (String)JOptionPane.showInputDialog(this, "Ingrese la subcategoría", "Nombre de la subcategoría", JOptionPane.QUESTION_MESSAGE, null, null, "");
	    if (_subcategoria != null) {
	        String _opcion = (String)JOptionPane.showInputDialog(this, "Seleccione una opción", "Opción", JOptionPane.QUESTION_MESSAGE, null, 
			    new Object[] { "Ninguna", "Inútiles", "Insuficientes", "Relevantes", "Indispensables"}, "");
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
