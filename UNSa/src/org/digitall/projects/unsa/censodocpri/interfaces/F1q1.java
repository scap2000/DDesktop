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
 * F1q1.java
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

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicCheckList;
import org.digitall.lib.components.basic.BasicCheckableListItem;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;
import org.digitall.projects.unsa.censodocpri.classes.OptionItem;

public class F1q1 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private JArea lblf1q1 = new JArea();
    private JArea tff1q1 = new JArea();

    private BasicScrollPane jsList;
    private JArea lblf1Opciones = new JArea();
    private BasicCheckList f1q1Opciones = new BasicCheckList();
    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();

    public F1q1(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(675, 395));
	lblf1q1.setText(encuesta.getF1q1Label());
	lblf1Opciones.setText(encuesta.getF1q1OpcionesLabel());

	lblf1q1.setBounds(new Rectangle(15, 25, 640, 55));
	lblf1q1.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q1.getTextArea().setForeground(Color.white);
	lblf1q1.getTextArea().setOpaque(false);
	lblf1q1.setEditable(false);
	
	tff1q1.setEditable(coordinador.isWritable());
	f1q1Opciones.setEnabled(coordinador.isWritable());
	
	tff1q1.setBounds(new Rectangle(15, 85, 640, 85));
	lblf1Opciones.setBounds(new Rectangle(15, 185, 640, 40));
	lblf1Opciones.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1Opciones.getTextArea().setForeground(Color.white);
	lblf1Opciones.getTextArea().setOpaque(false);
	lblf1Opciones.setEditable(false);

	jsList = new BasicScrollPane(f1q1Opciones);

	contentPane.add(tff1q1, null);
	contentPane.add(lblf1q1, null);
	contentPane.add(lblf1Opciones, null);
	contentPane.add(jsList, null);
	jsList.setBounds(new Rectangle(15, 230, 640, 85));
	this.setContent(contentPane);
	this.setSize(new Dimension(680, 366));
	addButton(btnNext);
	addButton(btnPrevious);
	
	tff1q1.setText(encuesta.getF1q1());
	
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
	Vector f1q1OpcionesVector = new Vector();
	for (int i = 0; i < encuesta.getF1q1Opciones().size(); i++) {
	    BasicCheckableListItem item = new BasicCheckableListItem(i, encuesta.getF1q1Opciones().elementAt(i).getOpcion(), encuesta.getF1q1Opciones().elementAt(i).isSeleccionada());
	    f1q1OpcionesVector.add(item);
	}
	f1q1Opciones.setListData(f1q1OpcionesVector);
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    encuesta.setF1q1(tff1q1.getText());
	    Vector<OptionItem> f1q1OpcionesVector = encuesta.getF1q1Opciones();
	    f1q1OpcionesVector.removeAllElements();
	    for (int i = 0; i < f1q1Opciones.getModel().getSize(); i++) {
		BasicCheckableListItem option = (BasicCheckableListItem)f1q1Opciones.getModel().getElementAt(i);
		f1q1OpcionesVector.add(new OptionItem(option.getName(), option.isSelected()));
	    }
	}
	coordinador.showF1q2();
    }

    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1Escuela();
    }
}
