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
 * F1q4.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicRadioButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;

public class F1q4 extends BasicPrimitivePanel {


    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private JArea lblf1q4 = new JArea();

    private ButtonGroup f1q4OpcionesGroup = new ButtonGroup();
    private BasicRadioButton rbf1q4Opcion1 = new BasicRadioButton();
    private BasicRadioButton rbf1q4Opcion2 = new BasicRadioButton();
    private BasicRadioButton rbf1q4Opcion3 = new BasicRadioButton();
    private BasicRadioButton rbf1q4Opcion4 = new BasicRadioButton();
    private BasicRadioButton rbf1q4Opcion5 = new BasicRadioButton();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();

    public F1q4(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(400, 191));
	lblf1q4.setText(encuesta.getF1q4Label());
	lblf1q4.setBounds(new Rectangle(15, 25, 645, 55));
	lblf1q4.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q4.getTextArea().setForeground(Color.white);
	lblf1q4.getTextArea().setOpaque(false);
	lblf1q4.setEditable(false);
	contentPane.add(lblf1q4, null);

	f1q4OpcionesGroup.add(rbf1q4Opcion1);
	f1q4OpcionesGroup.add(rbf1q4Opcion2);
	f1q4OpcionesGroup.add(rbf1q4Opcion3);
	f1q4OpcionesGroup.add(rbf1q4Opcion4);
	f1q4OpcionesGroup.add(rbf1q4Opcion5);

	rbf1q4Opcion1.setText(encuesta.getF1q4Opciones()[0]);
	rbf1q4Opcion1.setBounds(new Rectangle(15, 90, 110, 20));
	rbf1q4Opcion1.setSelected(encuesta.getF1q4().equals(encuesta.getF1q4Opciones()[0]));

	rbf1q4Opcion2.setText(encuesta.getF1q4Opciones()[1]);
	rbf1q4Opcion2.setBounds(new Rectangle(135, 90, 110, 20));
	rbf1q4Opcion2.setSelected(encuesta.getF1q4().equals(encuesta.getF1q4Opciones()[1]));

	rbf1q4Opcion3.setText(encuesta.getF1q4Opciones()[2]);
	rbf1q4Opcion3.setBounds(new Rectangle(255, 90, 110, 20));
	rbf1q4Opcion3.setSelected(encuesta.getF1q4().equals(encuesta.getF1q4Opciones()[2]));

	rbf1q4Opcion4.setText(encuesta.getF1q4Opciones()[3]);
	rbf1q4Opcion4.setBounds(new Rectangle(390, 90, 270, 20));
	rbf1q4Opcion4.setSelected(encuesta.getF1q4().equals(encuesta.getF1q4Opciones()[3]));

	rbf1q4Opcion5.setText(encuesta.getF1q4Opciones()[4]);
	rbf1q4Opcion5.setBounds(new Rectangle(15, 125, 270, 20));
	rbf1q4Opcion5.setSelected(encuesta.getF1q4().equals(encuesta.getF1q4Opciones()[4]));

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

	contentPane.add(rbf1q4Opcion1, null);
	contentPane.add(rbf1q4Opcion2, null);
	contentPane.add(rbf1q4Opcion3, null);
	contentPane.add(rbf1q4Opcion4, null);
	contentPane.add(rbf1q4Opcion5, null);
	
	rbf1q4Opcion1.setEnabled(coordinador.isWritable());
	rbf1q4Opcion2.setEnabled(coordinador.isWritable());
	rbf1q4Opcion3.setEnabled(coordinador.isWritable());
	rbf1q4Opcion4.setEnabled(coordinador.isWritable());
	rbf1q4Opcion5.setEnabled(coordinador.isWritable());

	addButton(btnNext);
	addButton(btnPrevious);
	setContent(contentPane);
	this.setSize(new Dimension(675, 198));
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    encuesta.setF1q4(rbf1q4Opcion1.isSelected()?rbf1q4Opcion1.getText():
				    rbf1q4Opcion2.isSelected()?rbf1q4Opcion2.getText():
				    rbf1q4Opcion3.isSelected()?rbf1q4Opcion3.getText():
				    rbf1q4Opcion4.isSelected()?rbf1q4Opcion4.getText():
				    rbf1q4Opcion5.isSelected()?rbf1q4Opcion5.getText():"");
	}
	coordinador.showF1q5();
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q3();
    }
}
