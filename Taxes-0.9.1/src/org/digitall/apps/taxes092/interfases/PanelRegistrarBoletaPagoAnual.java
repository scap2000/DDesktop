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
 * PanelRegistrarBoletaPagoAnual.java
 *
 * */
package org.digitall.apps.taxes092.interfases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.taxes092.classes.Coordinador;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.JMoneyEntry;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextArea;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;

public class PanelRegistrarBoletaPagoAnual extends BasicPanel{

    private BasicPanel panelAyuda = new BasicPanel();
    private BasicPanel panelCentral = new BasicPanel();
    private BasicPanel panelPeriodos = new BasicPanel();
    
    private BasicLabel lblInteres = new BasicLabel();
    private BasicLabel lblCondonacionIntereses = new BasicLabel();
    private BasicLabel lblTitulo = new BasicLabel();
    private BasicLabel lblCapital = new BasicLabel();
    private BasicLabel lblTotalAPagar = new BasicLabel();
    private BasicLabel lblTotalPeriodosAdeudados = new BasicLabel();
    private BasicLabel lblBonificacionPagoCtdo = new BasicLabel();
    private BasicLabel lblTotalAFinanciar = new BasicLabel();
    
    private BasicTextArea taAyuda = new BasicTextArea();
    
    private TFInput tfPeriodo = new TFInput(DataTypes.STRING,"Desde - Hasta", false);
    private TFInput tfCantAnticipos = new TFInput(DataTypes.STRING,"Periodos", false);
    private TFInput tfBonificacionAplicada = new TFInput(DataTypes.STRING,"Bonificación Aplicada", false);
    private TFInput tfPorcentajeBonif = new TFInput(DataTypes.PERCENT,"% Bonif.", false);
    private JMoneyEntry tfCapital = new JMoneyEntry();
    private JMoneyEntry tfInteres = new JMoneyEntry();
    private JMoneyEntry tfTotalAPagar = new JMoneyEntry();
    private JMoneyEntry tfSubtotal = new JMoneyEntry();
    private JMoneyEntry tfTotalPeriodosAdeudados = new JMoneyEntry();
    private JMoneyEntry tfSubtotal2 = new JMoneyEntry();
    private JMoneyEntry tfBonificacionPagoAnual = new JMoneyEntry();
    private JMoneyEntry tfBonificacion = new JMoneyEntry();
    private JMoneyEntry tfTotalAFinanciar = new JMoneyEntry();
    private JSeparator jSeparator1 = new JSeparator();
    private BasicButton btnRegistrarBoleta = new BasicButton();
    private String leyendaBonificacionPagoCtdo = "Bonificación Pago Anual";
    
    private Coordinador coordinador;

    public PanelRegistrarBoletaPagoAnual(Coordinador _coordinador) {
	coordinador = _coordinador;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(845, 475));
	panelAyuda.setBounds(new Rectangle(0, 0, 215, 475));
	panelAyuda.setLayout(null);
	panelAyuda.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	panelAyuda.setSize(new Dimension(215, 475));
	lblTitulo.setText("<html><center>Registrar Boleta <br> Pago Anual</center></html>");
	lblTitulo.setBounds(new Rectangle(5, 5, 205, 30));
	lblTitulo.setFont(new Font("Dialog", 1, 11));
        lblTitulo.setBackground(Color.black);
        lblTitulo.setOpaque(true);
	lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	lblTitulo.setHorizontalTextPosition(SwingConstants.CENTER);
	taAyuda.setBounds(new Rectangle(5, 40, 205, 430));
	panelCentral.setBounds(new Rectangle(215, 0, 630, 475));
	panelCentral.setLayout(null);
	panelCentral.setBorder(BorderFactory.createTitledBorder(""));
	panelPeriodos.setBounds(new Rectangle(5, 5, 620, 55));
	panelPeriodos.setBorder(BorderFactory.createTitledBorder(""));
	panelPeriodos.setLayout(null);
	lblInteres.setText("Interés:");
	lblInteres.setBounds(new Rectangle(5, 170, 80, 30));
	lblInteres.setFont(new Font("Dialog", 0, 18));
	lblCondonacionIntereses.setText("Bonificación");
	lblCondonacionIntereses.setBounds(new Rectangle(5, 255, 305, 35));
	lblCondonacionIntereses.setFont(new Font("Dialog", 0, 18));
	tfPeriodo.setBounds(new Rectangle(5, 10, 145, 35));
	tfPeriodo.setEditable(false);
	tfPeriodo.setHorizontalAlignment(TFInput.CENTER);
	tfCantAnticipos.setBounds(new Rectangle(175, 10, 65, 35));
	tfCantAnticipos.setEditable(false);
	tfCantAnticipos.setHorizontalAlignment(TFInput.CENTER);
	tfBonificacionAplicada.setBounds(new Rectangle(270, 10, 225, 35));
	tfBonificacionAplicada.setEditable(false);
	tfBonificacionAplicada.setHorizontalAlignment(TFInput.CENTER);
	tfPorcentajeBonif.setBounds(new Rectangle(515, 10, 100, 35));
	tfPorcentajeBonif.setEditable(false);
	tfPorcentajeBonif.setHorizontalAlignment(TFInput.CENTER);
	lblCapital.setText("Capital:");
	lblCapital.setBounds(new Rectangle(5, 135, 80, 30));
	lblCapital.setFont(new Font("Dialog", 0, 18));
	tfCapital.setBounds(new Rectangle(320, 135, 140, 30));
	tfCapital.setEditable(false);
	tfCapital.setFont(new Font("Dialog", 0, 20));
	tfCapital.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfCapital.setForeground(new Color(82, 132, 0));
	tfInteres.setBounds(new Rectangle(320, 170, 140, 30));
	tfInteres.setEditable(false);
	tfInteres.setFont(new Font("Dialog", 0, 20));
	tfInteres.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfInteres.setForeground(new Color(82, 132, 0));
	tfTotalAPagar.setBounds(new Rectangle(365, 425, 255, 45));
	tfTotalAPagar.setEditable(false);
	tfTotalAPagar.setFont(new Font("Dialog", 1, 30));
	tfTotalAPagar.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfTotalAPagar.setForeground(Color.red);
	tfSubtotal.setBounds(new Rectangle(480, 255, 140, 30));
	tfSubtotal.setEditable(false);
	tfSubtotal.setFont(new Font("Dialog", 0, 20));
	tfSubtotal.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfSubtotal.setForeground(new Color(82, 132, 0));
	lblTotalAPagar.setText("Total a Pagar:");
	lblTotalAPagar.setBounds(new Rectangle(180, 425, 185, 45));
	lblTotalAPagar.setFont(new Font("Dialog", 1, 20));
	tfTotalPeriodosAdeudados.setBounds(new Rectangle(480, 65, 140, 30));
	tfTotalPeriodosAdeudados.setEditable(false);
	tfTotalPeriodosAdeudados.setFont(new Font("Dialog", 1, 21));
	tfTotalPeriodosAdeudados.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfTotalPeriodosAdeudados.setForeground(new Color(0, 165, 0));
	tfSubtotal2.setBounds(new Rectangle(480, 350, 140, 30));
	tfSubtotal2.setEditable(false);
	tfSubtotal2.setFont(new Font("Dialog", 0, 20));
	tfSubtotal2.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfSubtotal2.setForeground(new Color(82, 132, 0));
	lblTotalPeriodosAdeudados.setText("Total Períodos Adeudados");
	lblTotalPeriodosAdeudados.setBounds(new Rectangle(5, 65, 265, 30));
	lblTotalPeriodosAdeudados.setFont(new Font("Dialog", 1, 18));
	tfBonificacionPagoAnual.setBounds(new Rectangle(320, 350, 140, 30));
	tfBonificacionPagoAnual.setEditable(false);
	tfBonificacionPagoAnual.setFont(new Font("Dialog", 0, 20));
	tfBonificacionPagoAnual.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfBonificacionPagoAnual.setForeground(new Color(82, 132, 0));
	lblBonificacionPagoCtdo.setText(leyendaBonificacionPagoCtdo);
	lblBonificacionPagoCtdo.setBounds(new Rectangle(5, 350, 310, 30));
	lblBonificacionPagoCtdo.setFont(new Font("Dialog", 0, 18));
	jSeparator1.setBounds(new Rectangle(5, 385, 615, 5));
	tfBonificacion.setBounds(new Rectangle(320, 255, 140, 30));
	tfBonificacion.setEditable(false);
	tfBonificacion.setFont(new Font("Dialog", 0, 20));
	tfBonificacion.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfBonificacion.setForeground(new Color(82, 132, 0));
	tfTotalAFinanciar.setBounds(new Rectangle(480, 210, 140, 30));
	tfTotalAFinanciar.setEditable(false);
	tfTotalAFinanciar.setFont(new Font("Dialog", 1, 21));
	tfTotalAFinanciar.setHorizontalAlignment(JMoneyEntry.RIGHT);
	tfTotalAFinanciar.setForeground(Color.red);
	lblTotalAFinanciar.setText("Total");
	lblTotalAFinanciar.setBounds(new Rectangle(5, 210, 200, 30));
	lblTotalAFinanciar.setFont(new Font("Dialog", 1, 18));
	btnRegistrarBoleta.setText("Registrar Boleta");
	btnRegistrarBoleta.setOpaque(true);
	btnRegistrarBoleta.setFont(new Font("Dialog", 1, 10));
	btnRegistrarBoleta.setForeground(Color.black);
	btnRegistrarBoleta.setBackground(new Color(165, 165, 0));
	btnRegistrarBoleta.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	btnRegistrarBoleta.setBounds(new Rectangle(15, 435, 130, 30));
	btnRegistrarBoleta.setToolTipText("Registrar Pago Anual");
	btnRegistrarBoleta.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    btnGenerar_actionPerformed(e);
		}
	    }
	);
	panelAyuda.add(taAyuda, null);
	panelAyuda.add(lblTitulo, null);
	panelCentral.add(btnRegistrarBoleta, null);
	panelCentral.add(lblTotalAFinanciar, null);
	panelCentral.add(tfTotalAFinanciar, null);
	panelCentral.add(tfBonificacion, null);
	panelCentral.add(jSeparator1, null);
	panelCentral.add(lblBonificacionPagoCtdo, null);
	panelCentral.add(tfBonificacionPagoAnual, null);
	panelCentral.add(lblTotalPeriodosAdeudados, null);
	panelCentral.add(tfSubtotal2, null);
	panelCentral.add(tfTotalPeriodosAdeudados, null);
	panelCentral.add(lblTotalAPagar, null);
	panelCentral.add(tfSubtotal, null);
	panelCentral.add(tfTotalAPagar, null);
	panelCentral.add(tfInteres, null);
	panelCentral.add(tfCapital, null);
	panelCentral.add(lblCapital, null);
	panelCentral.add(lblCondonacionIntereses, null);
	panelCentral.add(lblInteres, null);
	panelCentral.add(panelPeriodos, null);
	panelPeriodos.add(tfBonificacionAplicada, null);
	panelPeriodos.add(tfPorcentajeBonif, null);
	panelPeriodos.add(tfCantAnticipos, null);
	panelPeriodos.add(tfPeriodo, null);
	this.add(panelCentral, null);
	this.add(panelAyuda, null);
	taAyuda.setEditable(false);
    }
    
    public void iniciarPanel() {
	//coordinador.getTipoPlanDePago().setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
	//coordinador.getTipoPlanDePago().retrieveData2();
	tfPeriodo.setValue(coordinador.getPagoAnual().getPeriodoDesdeVista() + "/" + coordinador.getPagoAnual().getAnioDesde() + " AL " + coordinador.getPagoAnual().getPeriodoHastaVista() + "/" +coordinador.getPagoAnual().getAnioHasta());
	tfCantAnticipos.setValue(coordinador.getPagoAnual().getCantidadPeriodos());
	tfBonificacionAplicada.setValue(coordinador.getBonificacion().getNombre());
	tfPorcentajeBonif.setValue(coordinador.getBonificacion().getPorcentaje() * 100);
	tfTotalPeriodosAdeudados.setValue(coordinador.getPagoAnual().getTotalPuro());
	tfCapital.setValue(coordinador.getPagoAnual().getCapitalPuro());
	tfInteres.setValue(coordinador.getPagoAnual().getInteresPuro());
	tfTotalAFinanciar.setValue(coordinador.getPagoAnual().getTotalPuro());
	tfBonificacion.setValue(coordinador.getPagoAnual().getDescuento());
	tfSubtotal.setValue(coordinador.getPagoAnual().getSubTotal());
	tfBonificacionPagoAnual.setValue(coordinador.getPagoAnual().getDescuentoPagoAnual());
	tfSubtotal2.setValue(coordinador.getPagoAnual().getTotal());
	tfTotalAPagar.setValue(coordinador.getPagoAnual().getTotal());
    }
    
    private void btnGenerar_actionPerformed(ActionEvent e) {
        if(Advisor.question("Aviso","¿Está seguro de generar la boleta para " + coordinador.getPagoAnual().getCantidadPeriodos() + " anticipos por un total de " + Format.money(coordinador.getPagoAnticipos().getMontoTotal())+"?")){
            if(coordinador.registrarBoletaPagoAnual()){
               coordinador.clearPagoAnticipo();
               coordinador.clearPagoAnual();
               coordinador.inicio();
            }else{
                Advisor.messageBox("Ocurrió un error al grabar los datos del pago anual.","Aviso!");
            }
        }
    }

}

