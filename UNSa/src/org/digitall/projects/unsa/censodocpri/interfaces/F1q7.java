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
 * F1q7.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.interfaces;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PreviousWizardButton;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;

public class F1q7 extends BasicPrimitivePanel {

    private Coordinador coordinador;
    private F1 encuesta;
    private BasicPanel contentPane = new BasicPanel();

    private JArea lblf1q7 = new JArea();
    private JArea lblf1q7a = new JArea();
    private JArea tff1q7a = new JArea();
    private JArea lblf1q7b = new JArea();
    private JArea tff1q7b = new JArea();

    private NextWizardButton btnNext = new NextWizardButton();
    private PreviousWizardButton btnPrevious = new PreviousWizardButton();

    public F1q7(Coordinador _coordinador) {
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
	contentPane.setSize(new Dimension(675, 318));
	lblf1q7.setText(encuesta.getF1q7Label());
	lblf1q7a.setText(encuesta.getF1q7aLabel());
	lblf1q7b.setText(encuesta.getF1q7bLabel());

	lblf1q7.setBounds(new Rectangle(15, 25, 640, 45));
	lblf1q7.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q7.getTextArea().setForeground(Color.white);
	lblf1q7.getTextArea().setOpaque(false);
	lblf1q7.setEditable(false);

	lblf1q7a.setBounds(new Rectangle(15, 80, 640, 45));
	lblf1q7a.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q7a.getTextArea().setForeground(Color.white);
	lblf1q7a.getTextArea().setOpaque(false);
	lblf1q7a.setEditable(false);

	lblf1q7b.setBounds(new Rectangle(15, 195, 640, 45));
	lblf1q7b.getTextArea().setFont(new Font("Dialog", 1, 12));
	lblf1q7b.getTextArea().setForeground(Color.white);
	lblf1q7b.getTextArea().setOpaque(false);
	lblf1q7b.setEditable(false);

	tff1q7a.setBounds(new Rectangle(15, 130, 640, 60));
	tff1q7b.setBounds(new Rectangle(15, 245, 640, 60));

	contentPane.add(lblf1q7, null);
	contentPane.add(tff1q7a, null);
	contentPane.add(tff1q7b, null);
	contentPane.add(lblf1q7a, null);
	contentPane.add(lblf1q7b, null);
	this.setContent(contentPane);
	this.setSize(new Dimension(675, 358));
	addButton(btnNext);
	addButton(btnPrevious);
	
	tff1q7a.setText(encuesta.getF1q7a());
	tff1q7b.setText(encuesta.getF1q7b());

	tff1q7a.setEditable(coordinador.isWritable());
	tff1q7b.setEditable(coordinador.isWritable());

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
	    
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (coordinador.isWritable()) {
	    encuesta.setF1q7a(tff1q7a.getText());
	    encuesta.setF1q7b(tff1q7b.getText());
	}
	coordinador.showF1q8();
    }
    private void btnPrevious_actionPerformed(ActionEvent e) {
	coordinador.showF1q6();
    }

}
