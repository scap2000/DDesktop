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
 * F1q2.java
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

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.JScrollList;
import org.digitall.lib.components.basic.BasicCheckableListItem;
import org.digitall.lib.components.basic.BasicList;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.lib.components.buttons.UnAssignButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;
import org.digitall.projects.unsa.censodocpri.classes.OptionItem;

public class F1q2 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private JArea lblf1q2 = new JArea();
    private JArea tff1q2Comentario = new JArea();

    private JArea lblf1q2Comentario = new JArea();
    private BasicList tff1q2 = new BasicList();

    private JArea lblf1q2Opinion = new JArea();

    private ButtonGroup f1q2OpcionesGroup = new ButtonGroup();
    private BasicRadioButton rbf1q2Opcion1 = new BasicRadioButton();
    private BasicRadioButton rbf1q2Opcion2 = new BasicRadioButton();
    private BasicRadioButton rbf1q2Opcion3 = new BasicRadioButton();
    private BasicRadioButton rbf1q2Opcion4 = new BasicRadioButton();
    private BasicRadioButton rbf1q2Opcion5 = new BasicRadioButton();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();
    private BasicScrollPane jsList;
    private AssignButton btnDown = new AssignButton(true);
    private UnAssignButton btnUp = new UnAssignButton(true);

    public F1q2(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(675, 498));
	lblf1q2.setText(encuesta.getF1q2Label());
	lblf1q2Comentario.setText(encuesta.getF1q2ComentarioLabel());
	lblf1q2Opinion.setText(encuesta.getF1q2OpinionLabel());

	lblf1q2.setBounds(new Rectangle(15, 25, 635, 55));
	lblf1q2.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q2.getTextArea().setForeground(Color.white);
	lblf1q2.getTextArea().setOpaque(false);
	lblf1q2.setEditable(false);

	lblf1q2Comentario.setBounds(new Rectangle(15, 160, 635, 35));
	lblf1q2Comentario.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q2Comentario.getTextArea().setForeground(Color.white);
	lblf1q2Comentario.getTextArea().setOpaque(false);
	lblf1q2Comentario.setEditable(false);

	lblf1q2Opinion.setBounds(new Rectangle(15, 260, 635, 35));
	lblf1q2Opinion.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q2Opinion.getTextArea().setForeground(Color.white);
	lblf1q2Opinion.getTextArea().setOpaque(false);
	lblf1q2Opinion.setEditable(false);

	jsList = new BasicScrollPane(tff1q2);
	tff1q2.setListData(encuesta.getF1q2().split("\\|"));

	tff1q2Comentario.setBounds(new Rectangle(15, 200, 635, 55));

	tff1q2Comentario.setText(encuesta.getF1q2Comentario());

	Vector<String> f1q2OpcionesVector = new Vector<String>();
	String[] f1q2Opciones = encuesta.getF1q2().split("\\|");
	for (int i = 0; i < f1q2Opciones.length; i++) {
	    f1q2OpcionesVector.add(f1q2Opciones[i]);
	}

	f1q2OpcionesGroup.add(rbf1q2Opcion1);
	f1q2OpcionesGroup.add(rbf1q2Opcion2);
	f1q2OpcionesGroup.add(rbf1q2Opcion3);
	f1q2OpcionesGroup.add(rbf1q2Opcion4);
	f1q2OpcionesGroup.add(rbf1q2Opcion5);

	rbf1q2Opcion1.setText(encuesta.getF1q2OpinionOpciones()[0]);
	rbf1q2Opcion1.setBounds(new Rectangle(15, 300, 140, 20));
	rbf1q2Opcion1.setSelected(encuesta.getF1q2Opinion().equals(encuesta.getF1q2OpinionOpciones()[0]));

	rbf1q2Opcion2.setText(encuesta.getF1q2OpinionOpciones()[1]);
	rbf1q2Opcion2.setBounds(new Rectangle(175, 300, 140, 20));
	rbf1q2Opcion2.setSelected(encuesta.getF1q2Opinion().equals(encuesta.getF1q2OpinionOpciones()[1]));

	rbf1q2Opcion3.setText(encuesta.getF1q2OpinionOpciones()[2]);
	rbf1q2Opcion3.setBounds(new Rectangle(345, 300, 140, 20));
	rbf1q2Opcion3.setSelected(encuesta.getF1q2Opinion().equals(encuesta.getF1q2OpinionOpciones()[2]));

	rbf1q2Opcion4.setText(encuesta.getF1q2OpinionOpciones()[3]);
	rbf1q2Opcion4.setBounds(new Rectangle(515, 300, 140, 20));
	rbf1q2Opcion4.setSelected(encuesta.getF1q2Opinion().equals(encuesta.getF1q2OpinionOpciones()[3]));

	rbf1q2Opcion5.setText(encuesta.getF1q2OpinionOpciones()[4]);
	rbf1q2Opcion5.setBounds(new Rectangle(15, 335, 140, 20));
	rbf1q2Opcion5.setSelected(encuesta.getF1q2Opinion().equals(encuesta.getF1q2OpinionOpciones()[4]));

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
	jsList.setBounds(new Rectangle(15, 85, 590, 70));
	
	btnUp.setBounds(new Rectangle(615, 85, 40, 25));
	btnUp.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnUp_actionPerformed(e);
		}
	    });
	btnDown.setBounds(new Rectangle(615, 130, 40, 25));
	btnDown.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnDown_actionPerformed(e);
		}
	    });
	contentPane.add(btnUp, null);
	contentPane.add(btnDown, null);
	contentPane.add(rbf1q2Opcion1, null);
	contentPane.add(rbf1q2Opcion2, null);
	contentPane.add(rbf1q2Opcion3, null);
	contentPane.add(rbf1q2Opcion4, null);
	contentPane.add(rbf1q2Opcion5, null);
	contentPane.add(jsList, null);
	contentPane.add(tff1q2Comentario, null);
	contentPane.add(lblf1q2, null);

	contentPane.add(lblf1q2Comentario, null);
	contentPane.add(lblf1q2Opinion, null);
	tff1q2.setEnabled(coordinador.isWritable());
	tff1q2Comentario.setEditable(coordinador.isWritable());
	rbf1q2Opcion1.setEnabled(coordinador.isWritable());
	rbf1q2Opcion2.setEnabled(coordinador.isWritable());
	rbf1q2Opcion3.setEnabled(coordinador.isWritable());
	rbf1q2Opcion4.setEnabled(coordinador.isWritable());
	rbf1q2Opcion5.setEnabled(coordinador.isWritable());

	addButton(btnNext);
	addButton(btnPrevious);
	setContent(contentPane);
	this.setSize(new Dimension(675, 579));

    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    String f1q2 = "";
	    if (tff1q2.getModel().getSize() > 0) {
		for (int i = 0; i < tff1q2.getModel().getSize()-1; i++) {
		    f1q2 += tff1q2.getModel().getElementAt(i).toString() + "|";
		}
		f1q2 += tff1q2.getModel().getElementAt(tff1q2.getModel().getSize()-1).toString();
	    }
	    encuesta.setF1q2(f1q2);
	    encuesta.setF1q2Comentario(tff1q2Comentario.getText());
	    encuesta.setF1q2Opinion(rbf1q2Opcion1.isSelected()?rbf1q2Opcion1.getText():
				    rbf1q2Opcion2.isSelected()?rbf1q2Opcion2.getText():
				    rbf1q2Opcion3.isSelected()?rbf1q2Opcion3.getText():
				    rbf1q2Opcion4.isSelected()?rbf1q2Opcion4.getText():
				    rbf1q2Opcion5.isSelected()?rbf1q2Opcion5.getText():"");
	}
	coordinador.showF1q3();
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q1();
    }

    private void btnUp_actionPerformed(ActionEvent e) {
	int _index = tff1q2.getSelectedIndex();
	if (_index > 0) {
	    
	    if (tff1q2.getModel().getSize() > 0) {
		String[] _fields = new String[tff1q2.getModel().getSize()];
	        for (int i = 0; i < tff1q2.getModel().getSize(); i++) {
	            _fields[i] = tff1q2.getModel().getElementAt(i).toString();
	        }
	        String _field = _fields[_index];
	        _fields[_index] = _fields[_index-1];
	        _fields[_index-1] = _field;
	        tff1q2.setListData(_fields);
	        tff1q2.setSelectedIndex(_index-1);
	    }
	}
    }

    private void btnDown_actionPerformed(ActionEvent e) {
	int _index = tff1q2.getSelectedIndex();
	if (_index < tff1q2.getModel().getSize()-1) {
	    if (tff1q2.getModel().getSize() > 0) {
	        String[] _fields = new String[tff1q2.getModel().getSize()];
	        for (int i = 0; i < tff1q2.getModel().getSize(); i++) {
	            _fields[i] = tff1q2.getModel().getElementAt(i).toString();
	        }
		String _field = _fields[_index];
		_fields[_index] = _fields[_index+1];
		_fields[_index+1] = _field;
		tff1q2.setListData(_fields);
		tff1q2.setSelectedIndex(_index+1);
	    }
	}
    }
}
