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
 * F1q13.java
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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTable;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;
import org.digitall.projects.unsa.censodocpri.classes.TableItem;

public class F1q13 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private DefaultTableModel tableModel;
    private BasicTable table;
    private JArea lblf1q13 = new JArea();

    private JArea lblf1q13a = new JArea();
    private JArea tff1q13a = new JArea();
    private JArea lblf1q13b = new JArea();
    private JArea tff1q13b = new JArea();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();
    private BasicScrollPane jsList;

    public F1q13(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(675, 340));
	lblf1q13.setText(encuesta.getF1q13Label());
	lblf1q13a.setText(encuesta.getF1q13aLabel());
	lblf1q13b.setText(encuesta.getF1q13bLabel());

	lblf1q13.setBounds(new Rectangle(15, 25, 645, 40));
	lblf1q13.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q13.getTextArea().setForeground(Color.white);
	lblf1q13.getTextArea().setOpaque(false);
	lblf1q13.setEditable(false);
	this.setContent(contentPane);
	this.setSize(new Dimension(675, 382));
	addButton(btnNext);
	addButton(btnPrevious);

	tableModel = new DefaultTableModel();
	table = new BasicTable(tableModel);

	tableModel.addColumn("Categoría");
	tableModel.addColumn("Opción");
	tableModel.setRowCount(0);

	TableColumn columnaOpcion = table.getColumnModel().getColumn(1);
	BasicCombo comboBox = new BasicCombo();
	comboBox.addItem("Ninguna");
	comboBox.addItem("Totalmente insatisfecho");
	comboBox.addItem("Insatisfecho");
	comboBox.addItem("Satisfecho");
	comboBox.addItem("Plenamente satisfecho");
	columnaOpcion.setCellEditor(new DefaultCellEditor(comboBox));

	Vector<TableItem> f1q13 = encuesta.getF1q13();

	for (int i = 0; i < f1q13.size(); i++) {
	    tableModel.addRow(new Object[] { f1q13.elementAt(i).getCategoria(), f1q13.elementAt(i).getOpcion() });
	}
	jsList = new BasicScrollPane(table);

	lblf1q13a.setBounds(new Rectangle(15, 175, 645, 25));
	lblf1q13a.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q13a.getTextArea().setForeground(Color.white);
	lblf1q13a.getTextArea().setOpaque(false);
	lblf1q13a.setEditable(false);

	lblf1q13b.setBounds(new Rectangle(15, 255, 645, 25));
	lblf1q13b.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q13b.getTextArea().setForeground(Color.white);
	lblf1q13b.getTextArea().setOpaque(false);
	lblf1q13b.setEditable(false);

	tff1q13a.setBounds(new Rectangle(15, 205, 645, 45));
	tff1q13b.setBounds(new Rectangle(15, 285, 645, 45));

	tff1q13a.setText(encuesta.getF1q13a());
	tff1q13b.setText(encuesta.getF1q13b());

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
	contentPane.add(jsList, null);
	contentPane.add(lblf1q13, null);
	contentPane.add(lblf1q13a, null);
	contentPane.add(lblf1q13b, null);
	contentPane.add(tff1q13a, null);
	contentPane.add(tff1q13b, null);
	addButton(btnNext);
	addButton(btnPrevious);
	
	table.setEnabled(coordinador.isWritable());
	tff1q13a.setEditable(coordinador.isWritable());
	tff1q13b.setEditable(coordinador.isWritable());

	jsList.setBounds(new Rectangle(15, 75, 645, 90));

    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    Vector<TableItem> f1q13 = encuesta.getF1q13();
	    f1q13.removeAllElements();
	    for (int i = 0; i < table.getRowCount(); i++) {
	        f1q13.add(new TableItem(table.getValueAt(i, 0).toString(), "", table.getValueAt(i, 1).toString()));
	    }
	    encuesta.setF1q13a(tff1q13a.getText());
	    encuesta.setF1q13b(tff1q13b.getText());
	}
	coordinador.showF1q14();
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q12();
    }

}
