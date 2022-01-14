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
 * PanelPrepararPlanDePagos.java
 *
 * */
package org.digitall.apps.taxes092.interfases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JSeparator;

import org.digitall.apps.taxes.classes.TipoImpuesto;
import org.digitall.apps.taxes092.classes.Coordinador;
import org.digitall.apps.taxes092.classes.EstadoCuenta;
import org.digitall.apps.taxes092.classes.ResumenPlanDePago;
import org.digitall.common.reports.BasicReport;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.JMoneyEntry;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextArea;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;

public class PanelPrepararPlanDePagos extends BasicPanel {

    private BasicPanel panelAyuda = new BasicPanel();
    private BasicPanel panelCentral = new BasicPanel();
    private BasicPanel panelPeriodos = new BasicPanel();

    private BasicLabel lblTitulo = new BasicLabel();
    private BasicLabel lblInteres = new BasicLabel();
    private BasicLabel lblCondonacionIntereses = new BasicLabel();
    private BasicLabel lblCapital = new BasicLabel();
    private BasicLabel lblTotalAPagar = new BasicLabel();
    private BasicLabel lblTotalPeriodosAdeudados = new BasicLabel();
    private BasicLabel lblCantidadCuotas = new BasicLabel();
    private BasicLabel lblInteresFinanciacion = new BasicLabel();
    private BasicLabel lblPagoAnticipado = new BasicLabel();
    private BasicLabel lblTotalAPagarCuotas = new BasicLabel();

    private BasicTextArea taAyuda = new BasicTextArea();
    private TFInput tfPeriodo = new TFInput(DataTypes.STRING, "Desde - Hasta", false);
    private TFInput tfCantAnticipos = new TFInput(DataTypes.STRING, "Períodos", false);
    private TFInput tfBonificacionAplicada = new TFInput(DataTypes.STRING, "Bonificación Aplicada", false);
    private TFInput tfPorcentajeBonif = new TFInput(DataTypes.STRING, "% Bonif.", false);
    private JMoneyEntry tfCapital = new JMoneyEntry();
    private JMoneyEntry tfInteres = new JMoneyEntry();
    private JMoneyEntry tfTotalAPagar = new JMoneyEntry();
    private JMoneyEntry tfSubtotal = new JMoneyEntry();
    private JMoneyEntry tfTotalPeriodosAdeudados = new JMoneyEntry();
    private JMoneyEntry tfSubtotal2 = new JMoneyEntry();
    private JMoneyEntry tfInteresFinanciacion = new JMoneyEntry();
    private JMoneyEntry tfCondonacionIntereses = new JMoneyEntry();
    private JMoneyEntry tfPagoAnticipado = new JMoneyEntry();
    private JMoneyEntry tfTotalAFinanciar = new JMoneyEntry();
    private JComboBox cbCuotas = new JComboBox();
    private JComboBox cbVtos = new JComboBox();
    private JSeparator jSeparator1 = new JSeparator();
    private PrintButton btnImprimir = new PrintButton();
    private BasicButton btnVer = new BasicButton();
    private String leyendaCondonacionIntereses = "Condonación Intereses";
    private String leyendaInteresPorFin = "Interés Financiación";
    private Coordinador coordinador;
    private BasicLabel lblCuotas = new BasicLabel();
    private BasicLabel lblVencimientos = new BasicLabel();
    private boolean verItemChange = false;

    public PanelPrepararPlanDePagos(Coordinador _coordinador) {
	coordinador = _coordinador;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	setLayout(null);
	setSize(new Dimension(845, 475));
	panelAyuda.setBounds(new Rectangle(0, 0, 215, 475));
	panelAyuda.setLayout(null);
	panelAyuda.setBorder(BorderFactory.createEtchedBorder(0));
	panelAyuda.setSize(new Dimension(215, 475));
	lblTitulo.setText("Preparar Plan de Pago");
	lblTitulo.setBounds(new Rectangle(5, 5, 205, 20));
	lblTitulo.setFont(new Font("Dialog", 1, 11));
	lblTitulo.setBackground(Color.black);
	lblTitulo.setOpaque(true);
	lblTitulo.setHorizontalAlignment(0);
	lblTitulo.setHorizontalTextPosition(0);
	taAyuda.setBounds(new Rectangle(5, 30, 205, 440));
	panelCentral.setBounds(new Rectangle(215, 0, 630, 475));
	panelCentral.setLayout(null);
	panelCentral.setBorder(BorderFactory.createTitledBorder(""));
	panelPeriodos.setBounds(new Rectangle(5, 5, 620, 55));
	panelPeriodos.setBorder(BorderFactory.createTitledBorder(""));
	panelPeriodos.setLayout(null);
	lblInteres.setText("Interés Adeudado:");
	lblInteres.setBounds(new Rectangle(5, 180, 195, 30));
	lblInteres.setFont(new Font("Dialog", 0, 18));
	lblCondonacionIntereses.setText(leyendaCondonacionIntereses);
	lblCondonacionIntereses.setBounds(new Rectangle(5, 245, 305, 35));
	lblCondonacionIntereses.setFont(new Font("Dialog", 0, 18));
	tfPeriodo.setBounds(new Rectangle(5, 10, 145, 35));
	tfPeriodo.setEditable(false);
	tfPeriodo.setHorizontalAlignment(0);
	tfCantAnticipos.setBounds(new Rectangle(175, 10, 70, 35));
	tfCantAnticipos.setEditable(false);
	tfCantAnticipos.setHorizontalAlignment(0);
	tfBonificacionAplicada.setBounds(new Rectangle(270, 10, 255, 35));
	tfBonificacionAplicada.setEditable(false);
	tfBonificacionAplicada.setHorizontalAlignment(0);
	tfPorcentajeBonif.setBounds(new Rectangle(545, 10, 70, 35));
	tfPorcentajeBonif.setEditable(false);
	tfPorcentajeBonif.setHorizontalAlignment(0);
	lblCapital.setText("Capital Adeudado:");
	lblCapital.setBounds(new Rectangle(5, 145, 195, 30));
	lblCapital.setFont(new Font("Dialog", 0, 18));
	tfCapital.setBounds(new Rectangle(320, 145, 140, 30));
	tfCapital.setEditable(false);
	tfCapital.setFont(new Font("Dialog", 0, 20));
	tfCapital.setHorizontalAlignment(4);
	tfCapital.setForeground(new Color(82, 132, 0));
	tfInteres.setBounds(new Rectangle(320, 180, 140, 30));
	tfInteres.setEditable(false);
	tfInteres.setFont(new Font("Dialog", 0, 20));
	tfInteres.setHorizontalAlignment(4);
	tfInteres.setForeground(new Color(82, 132, 0));
	tfTotalAPagar.setBounds(new Rectangle(365, 425, 255, 45));
	tfTotalAPagar.setEditable(false);
	tfTotalAPagar.setFont(new Font("Dialog", 1, 30));
	tfTotalAPagar.setHorizontalAlignment(4);
	tfTotalAPagar.setForeground(Color.red);
	tfSubtotal.setBounds(new Rectangle(480, 245, 140, 30));
	tfSubtotal.setEditable(false);
	tfSubtotal.setFont(new Font("Dialog", 0, 20));
	tfSubtotal.setHorizontalAlignment(4);
	tfSubtotal.setForeground(new Color(82, 132, 0));
	lblTotalAPagar.setText("Total a Pagar:");
	lblTotalAPagar.setBounds(new Rectangle(180, 425, 185, 45));
	lblTotalAPagar.setFont(new Font("Dialog", 1, 20));
	tfTotalPeriodosAdeudados.setBounds(new Rectangle(480, 65, 140, 30));
	tfTotalPeriodosAdeudados.setEditable(false);
	tfTotalPeriodosAdeudados.setFont(new Font("Dialog", 1, 21));
	tfTotalPeriodosAdeudados.setHorizontalAlignment(4);
	tfTotalPeriodosAdeudados.setForeground(new Color(0, 165, 0));
	tfSubtotal2.setBounds(new Rectangle(480, 340, 140, 30));
	tfSubtotal2.setEditable(false);
	tfSubtotal2.setFont(new Font("Dialog", 0, 20));
	tfSubtotal2.setHorizontalAlignment(4);
	tfSubtotal2.setForeground(new Color(82, 132, 0));
	lblTotalPeriodosAdeudados.setText("Deuda Total");
	lblTotalPeriodosAdeudados.setBounds(new Rectangle(5, 65, 265, 30));
	lblTotalPeriodosAdeudados.setFont(new Font("Dialog", 1, 18));
	tfInteresFinanciacion.setBounds(new Rectangle(320, 340, 140, 30));
	tfInteresFinanciacion.setEditable(false);
	tfInteresFinanciacion.setFont(new Font("Dialog", 0, 20));
	tfInteresFinanciacion.setHorizontalAlignment(4);
	tfInteresFinanciacion.setForeground(new Color(82, 132, 0));
	lblCantidadCuotas.setText("Cantidad de  Cuotas");
	lblCantidadCuotas.setBounds(new Rectangle(5, 300, 305, 30));
	lblCantidadCuotas.setFont(new Font("Dialog", 0, 18));
	lblInteresFinanciacion.setText(leyendaInteresPorFin);
	lblInteresFinanciacion.setBounds(new Rectangle(5, 340, 305, 30));
	lblInteresFinanciacion.setFont(new Font("Dialog", 0, 18));
	cbCuotas.setBounds(new Rectangle(320, 300, 60, 30));
	cbCuotas.setFont(new Font("Dialog", 1, 20));
	cbCuotas.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == 1 && verItemChange)
			calcularTotalAPagar();
		}
	    });
	cbVtos.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == 1 && verItemChange)
			setDiasVencimiento();
		}

	    });
	cbVtos.setBounds(new Rectangle(400, 300, 60, 30));
	cbVtos.setFont(new Font("Dialog", 1, 20));
	jSeparator1.setBounds(new Rectangle(5, 420, 615, 5));
	tfCondonacionIntereses.setBounds(new Rectangle(320, 245, 140, 30));
	tfCondonacionIntereses.setEditable(false);
	tfCondonacionIntereses.setFont(new Font("Dialog", 0, 20));
	tfCondonacionIntereses.setHorizontalAlignment(4);
	tfCondonacionIntereses.setForeground(new Color(82, 132, 0));
	tfPagoAnticipado.setBounds(new Rectangle(320, 110, 140, 30));
	tfPagoAnticipado.setFont(new Font("Dialog", 0, 20));
	tfPagoAnticipado.setEditable(false);
	tfPagoAnticipado.setHorizontalAlignment(4);
	tfPagoAnticipado.setForeground(new Color(82, 132, 0));
	lblPagoAnticipado.setText("Entrega");
	lblPagoAnticipado.setBounds(new Rectangle(5, 110, 195, 35));
	lblPagoAnticipado.setFont(new Font("Dialog", 0, 18));
	btnImprimir.setOpaque(true);
	btnImprimir.setFont(new Font("Dialog", 1, 10));
	btnImprimir.setForeground(Color.black);
	btnImprimir.setBackground(new Color(255, 33, 33));
	btnImprimir.setBorder(BorderFactory.createBevelBorder(0));
	btnImprimir.setBounds(new Rectangle(10, 435, 65, 30));
	btnImprimir.setToolTipText("Imprimir Plan de Pago");
	btnImprimir.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnImprimir_actionPerformed(e);
		}

	    });
	tfTotalAFinanciar.setBounds(new Rectangle(480, 110, 140, 30));
	tfTotalAFinanciar.setEditable(false);
	tfTotalAFinanciar.setFont(new Font("Dialog", 1, 21));
	tfTotalAFinanciar.setHorizontalAlignment(4);
	tfTotalAFinanciar.setForeground(Color.red);
	lblTotalAPagarCuotas.setBounds(new Rectangle(5, 390, 615, 30));
	lblTotalAPagarCuotas.setFont(new Font("Dialog", 1, 18));
	lblTotalAPagarCuotas.setHorizontalAlignment(0);
	lblTotalAPagarCuotas.setHorizontalTextPosition(0);
	lblTotalAPagarCuotas.setForeground(Color.yellow);
	btnVer.setText("Ver");
	btnVer.setOpaque(true);
	btnVer.setFont(new Font("Dialog", 1, 10));
	btnVer.setForeground(Color.black);
	btnVer.setBackground(new Color(255, 255, 66));
	btnVer.setBorder(BorderFactory.createBevelBorder(0));
	btnVer.setBounds(new Rectangle(85, 435, 65, 30));
	btnVer.setToolTipText("Ver configuración de Plan de Pago");
	btnVer.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnVer_actionPerformed(e);
		}

	    });
	lblCuotas.setText("Cuotas:");
	lblCuotas.setBounds(new Rectangle(320, 280, 60, 15));
	lblVencimientos.setText("Vtos.:");
	lblVencimientos.setBounds(new Rectangle(400, 280, 60, 15));
	taAyuda.setEditable(false);
	panelAyuda.add(taAyuda, null);
	panelAyuda.add(lblTitulo, null);
	panelCentral.add(lblVencimientos, null);
	panelCentral.add(lblCuotas, null);
	panelCentral.add(btnVer, null);
	panelCentral.add(lblTotalAPagarCuotas, null);
	panelCentral.add(tfTotalAFinanciar, null);
	panelCentral.add(btnImprimir, null);
	panelCentral.add(lblPagoAnticipado, null);
	panelCentral.add(tfPagoAnticipado, null);
	panelCentral.add(tfCondonacionIntereses, null);
	panelCentral.add(jSeparator1, null);
	panelCentral.add(cbVtos, null);
	panelCentral.add(cbCuotas, null);
	panelCentral.add(lblInteresFinanciacion, null);
	panelCentral.add(lblCantidadCuotas, null);
	panelCentral.add(tfInteresFinanciacion, null);
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
	add(panelCentral, null);
	add(panelAyuda, null);
	taAyuda.setText("* Seleccione la cantidad de cuotas del Plan de pagos y el dia de vto. de cada cuota");
	taAyuda.setEditable(false);
	loadCombos();
    }

    private void loadCombos() {
	cbCuotas.removeAllItems();
	int mincuota = coordinador.getTipoPlanDePago().getMinCuota();
	int maxcuota = coordinador.getTipoPlanDePago().getMaxCuotas();
	for (int i = mincuota; i < maxcuota + 1; i++)
	    cbCuotas.addItem(Integer.valueOf(i));

	cbVtos.removeAllItems();
	for (int i = 1; i < 28; i++)
	    cbVtos.addItem(Integer.valueOf(i));

	verItemChange = true;
    }

    private void setDiasVencimiento() {
	coordinador.getConfiguracionPlanDePago().setDiaVencimiento(Integer.parseInt(cbVtos.getSelectedItem().toString()));
    }

    private void btnVer_actionPerformed(ActionEvent e) {
	coordinador.setSubOpcion(1);
	coordinador.siguiente();
    }

    private void calcularTotalAPagar() {
	String textoCuotas = "";
	int cuotas = Integer.parseInt(cbCuotas.getSelectedItem().toString());
	if (cuotas > 1) {
	    BigDecimal porcentajeIntFin = new BigDecimal("0");
	    coordinador.getConfiguracionPlanDePago().setPorcentajeInteresFinanciacion(coordinador.getTipoPlanDePago().getPorcentajeInteresCuota());
	    porcentajeIntFin = porcentajeIntFin.add(new BigDecimal((new StringBuilder()).append("").append(cuotas).toString()));
	    porcentajeIntFin = porcentajeIntFin.multiply(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getPorcentajeInteresFinanciacion()).toString()));
	    porcentajeIntFin = porcentajeIntFin.divide(new BigDecimal("100"));
	    BigDecimal montoIntFin = new BigDecimal("0");
	    BigDecimal montoDeuda = new BigDecimal("0");
	    BigDecimal montoPagoCuotas = new BigDecimal("0");
	    BigDecimal montoCuota = new BigDecimal("0");
	    BigDecimal montoTotalCuotasRestantes = new BigDecimal("0");
	    BigDecimal montoPrimerCuota = new BigDecimal("0");
	    BigDecimal subTotal2 = new BigDecimal("0");
	    BigDecimal totalAPagar = new BigDecimal("0");
	    montoDeuda = montoDeuda.add(new BigDecimal(tfSubtotal.getValue().toString()));
	    montoIntFin = montoIntFin.add(new BigDecimal((new StringBuilder()).append("").append(montoDeuda.doubleValue()).toString()));
	    montoIntFin = montoIntFin.multiply(new BigDecimal((new StringBuilder()).append("").append(porcentajeIntFin.doubleValue()).toString()));
	    montoPagoCuotas = montoDeuda.add(montoIntFin);
	    textoCuotas = (new StringBuilder()).append("Entrega ").append(Format.money(coordinador.getConfiguracionPlanDePago().getPagoAnticipado())).append(" + ").toString();
	    int cantCuotasEnteras = cuotas - 1;
	    if (cantCuotasEnteras > 0) {
		montoCuota = Format.toDouble(Math.floor(montoPagoCuotas.doubleValue() / (double)cuotas));
		montoTotalCuotasRestantes = montoCuota.multiply(Format.toDouble(cantCuotasEnteras));
		montoPrimerCuota = montoPagoCuotas.subtract(montoTotalCuotasRestantes);
		if (montoPrimerCuota.doubleValue() > 0.0D)
		    textoCuotas = (new StringBuilder()).append(textoCuotas).append(" 1/c de  ").append(Format.money(montoPrimerCuota.doubleValue())).append(" + ").append(cantCuotasEnteras).append("/c de  ").append(Format.money(montoCuota.doubleValue())).toString();
		else
		    textoCuotas = (new StringBuilder()).append(textoCuotas).append(" 1/c de  ").append(Format.money(montoCuota.doubleValue())).toString();
	    } else {
		montoCuota = Format.toDouble(montoPagoCuotas.doubleValue() / (double)cuotas);
		textoCuotas = (new StringBuilder()).append(textoCuotas).append(" 1/c de $ ").append(Format.money(montoCuota.doubleValue())).toString();
	    }
	    subTotal2 = subTotal2.add(new BigDecimal(tfSubtotal.getValue().toString()));
	    subTotal2 = subTotal2.add(montoIntFin);
	    totalAPagar = totalAPagar.add(montoPrimerCuota);
	    totalAPagar = totalAPagar.add(new BigDecimal(String.valueOf(coordinador.getConfiguracionPlanDePago().getValorAnticipoActual())));
	    tfSubtotal2.setValue(Double.valueOf(subTotal2.doubleValue()));
	    tfInteresFinanciacion.setValue(montoIntFin);
	    lblTotalAPagarCuotas.setText(textoCuotas);
	    tfTotalAPagar.setValue(subTotal2);
	    coordinador.getConfiguracionPlanDePago().setCantidadCuotas(cuotas);
	    coordinador.getConfiguracionPlanDePago().setMontoInteresFinanciacion(montoIntFin.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setValorPrimerCuota(montoPrimerCuota.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setValorCuotasRestantes(montoCuota.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setDiaVencimiento(Integer.parseInt(cbVtos.getSelectedItem().toString()));
	    coordinador.getConfiguracionPlanDePago().setTotalAPagar(totalAPagar.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setTotal(subTotal2.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setMontoTotalCuotas(subTotal2.doubleValue());
	    setLeyendaInteresFinanciacion();
	} else if (cuotas == 1) {
	    BigDecimal porcentajeIntFin = new BigDecimal("0");
	    coordinador.getConfiguracionPlanDePago().setPorcentajeInteresFinanciacion(-1.0*coordinador.getTipoPlanDePago().getPorcentDtoPagoContado());
	    porcentajeIntFin = porcentajeIntFin.add(new BigDecimal((new StringBuilder()).append("").append(cuotas).toString()));
	    porcentajeIntFin = porcentajeIntFin.multiply(new BigDecimal((new StringBuilder()).append("").append(-1D * coordinador.getTipoPlanDePago().getPorcentDtoPagoContado()).toString()));
	    porcentajeIntFin = porcentajeIntFin.divide(new BigDecimal("100"));
	    lblInteresFinanciacion.setText((new StringBuilder()).append(leyendaInteresPorFin).append(" (").append(-1.0 * coordinador.getTipoPlanDePago().getPorcentDtoPagoContado()).append(" %)").toString());
	    BigDecimal montoIntFin = new BigDecimal("0");
	    BigDecimal montoDeuda = new BigDecimal("0");
	    BigDecimal montoPagoCuotas = new BigDecimal("0");
	    BigDecimal montoCuota = new BigDecimal("0");
	    BigDecimal montoPrimerCuota = new BigDecimal("0");
	    BigDecimal subTotal2 = new BigDecimal("0");
	    BigDecimal totalAPagar = new BigDecimal("0");
	    montoDeuda = montoDeuda.add(new BigDecimal(tfSubtotal.getValue().toString()));
	    montoIntFin = montoIntFin.add(new BigDecimal((new StringBuilder()).append("").append(montoDeuda.doubleValue()).toString()));
	    montoIntFin = montoIntFin.multiply(new BigDecimal((new StringBuilder()).append("").append(porcentajeIntFin.doubleValue()).toString()));
	    montoPagoCuotas = montoDeuda.add(montoIntFin);
	    textoCuotas = (new StringBuilder()).append("Entrega ").append(Format.money(coordinador.getConfiguracionPlanDePago().getPagoAnticipado())).append(" + ").toString();
	    montoCuota = Format.toDouble(montoPagoCuotas.doubleValue() / (double)cuotas);
	    textoCuotas = (new StringBuilder()).append(textoCuotas).append(" Pago único de ").append(Format.money(montoCuota.doubleValue())).toString();
	    subTotal2 = subTotal2.add(new BigDecimal(tfSubtotal.getValue().toString()));
	    subTotal2 = subTotal2.add(montoIntFin);
	    totalAPagar = totalAPagar.add(montoPrimerCuota);
	    totalAPagar = totalAPagar.add(new BigDecimal(String.valueOf(coordinador.getConfiguracionPlanDePago().getValorAnticipoActual())));
	    tfSubtotal2.setValue(Double.valueOf(subTotal2.doubleValue()));
	    tfInteresFinanciacion.setValue(montoIntFin);
	    lblTotalAPagarCuotas.setText(textoCuotas);
	    tfTotalAPagar.setValue(subTotal2);
	    coordinador.getConfiguracionPlanDePago().setCantidadCuotas(cuotas);
	    coordinador.getConfiguracionPlanDePago().setMontoInteresFinanciacion(montoIntFin.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setValorPrimerCuota(montoPrimerCuota.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setValorCuotasRestantes(montoCuota.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setDiaVencimiento(Integer.parseInt(cbVtos.getSelectedItem().toString()));
	    coordinador.getConfiguracionPlanDePago().setTotalAPagar(totalAPagar.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setTotal(subTotal2.doubleValue());
	    coordinador.getConfiguracionPlanDePago().setMontoTotalCuotas(subTotal2.doubleValue());
	}
    }

    public void iniciarPanel() {
	String periodo = "";
	BigDecimal subTotalAux = new BigDecimal("0");
	if (coordinador.getConfiguracionPlanDePago().getPagoAnticipado() > 0.0D) {
	    coordinador.getConfiguracionPlanDePago().retrieveData(coordinador.getTipoPlanDePago(), coordinador.getBien(), coordinador.getBonificacion(), coordinador.getConfiguracionPlanDePago().getPagoAnticipado());
	    if (coordinador.getConfiguracionPlanDePago().isGenerado()) {
		if (coordinador.getConfiguracionPlanDePago().getCantidadAnticipos() > 0) {
		    if (coordinador.getBien().esAutomotor()) {
			periodo = (new StringBuilder()).append((coordinador.getConfiguracionPlanDePago().getMenorAnticipoPPEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnioPPEntrega()).append(" AL ").append((coordinador.getConfiguracionPlanDePago().getMayorAnticipoPPEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnioPPEntrega()).toString();
			periodo = (new StringBuilder()).append((coordinador.getConfiguracionPlanDePago().getMenorAnticipoPPEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnioPPEntrega()).append(" AL ").append((coordinador.getConfiguracionPlanDePago().getMayorAnticipoPPEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnioPPEntrega()).toString();
		    } else {
			periodo = (new StringBuilder()).append(coordinador.getConfiguracionPlanDePago().getMenorAnticipoPPEntrega()).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnioPPEntrega()).append(" AL ").append(coordinador.getConfiguracionPlanDePago().getMayorAnticipoPPEntrega()).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnioPPEntrega()).toString();
		    }
		    tfPagoAnticipado.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalBaseCubiertoEntrega()));
		    tfCantAnticipos.setValue(Integer.valueOf(coordinador.getConfiguracionPlanDePago().getCantidadAnticipos()));
		    tfBonificacionAplicada.setValue(coordinador.getBonificacion().getNombre());
		    tfPorcentajeBonif.setValue(Double.valueOf(coordinador.getBonificacion().getPorcentaje().doubleValue() * 100D));
		    tfTotalPeriodosAdeudados.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()));
		    if (coordinador.getBonificacion().getPorcentaje().doubleValue() > 0.0D) {
			subTotalAux = subTotalAux.add(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()).toString()));
			subTotalAux = subTotalAux.subtract(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getMontoCondonacionIntereses()).toString()));
			tfTotalAFinanciar.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()));
			tfCapital.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getCapital()));
			tfInteres.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getInteres()));
			tfTotalPeriodosAdeudados.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados() + coordinador.getConfiguracionPlanDePago().getTotalBaseCubiertoEntrega()));
		    } else {
			subTotalAux = subTotalAux.add(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudadosEntrega()).toString()));
			subTotalAux = subTotalAux.subtract(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getMontoCondonacionIntereses()).toString()));
			tfTotalAFinanciar.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudadosEntrega()));
			tfTotalPeriodosAdeudados.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados() + coordinador.getConfiguracionPlanDePago().getTotalBaseCubiertoEntrega()));
			tfCapital.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getMontoBasePPEntrega()));
			tfInteres.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getInteresBasePPEntrega()));
		    }
		    lblCondonacionIntereses.setText((new StringBuilder()).append(leyendaCondonacionIntereses).append(" (").append(coordinador.getConfiguracionPlanDePago().getPorcentajeCondonacionIntereses()).append("%)").toString());
		    tfCondonacionIntereses.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getMontoCondonacionIntereses()));
		    tfSubtotal.setValue(Double.valueOf(subTotalAux.doubleValue()));
		    setLeyendaInteresFinanciacion();
		    cbCuotas.setSelectedItem(Integer.valueOf(5));
		    cbCuotas.setSelectedItem(Integer.valueOf(2));
		    tfPeriodo.setText(periodo);
		} else {
		    Advisor.messageBox("No se puede hacer un plan de pago sin periodos cubiertos.", "Aviso");
		    coordinador.atras();
		}
	    } else {
		Advisor.messageBox("No se puede hacer un plan de pago con la entrega ingresada.", "Aviso");
		coordinador.atras();
	    }
	} else {
	    coordinador.getConfiguracionPlanDePago().retrieveData(coordinador.getTipoPlanDePago(), coordinador.getBien(), coordinador.getBonificacion(), 0.0D);
	    if (coordinador.getConfiguracionPlanDePago().isGenerado()) {
		if (coordinador.getConfiguracionPlanDePago().getCantidadAnticipos() > 0) {
		    if (coordinador.getBien().esAutomotor())
			periodo = (new StringBuilder()).append(coordinador.getConfiguracionPlanDePago().getmenorAnticipo()).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnio()).append(" AL ").append(coordinador.getConfiguracionPlanDePago().getmayorAnticipo()).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnio()).toString();
		    else
			periodo = (new StringBuilder()).append(coordinador.getConfiguracionPlanDePago().getmenorAnticipo()).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnio()).append(" AL ").append(coordinador.getConfiguracionPlanDePago().getmayorAnticipo()).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnio()).toString();
		    tfPeriodo.setText(periodo);
		    tfCantAnticipos.setValue(Integer.valueOf(coordinador.getConfiguracionPlanDePago().getCantidadAnticipos()));
		    tfBonificacionAplicada.setValue(coordinador.getBonificacion().getNombre());
		    tfPorcentajeBonif.setValue(Double.valueOf(coordinador.getBonificacion().getPorcentaje().doubleValue() * 100D));
		    tfTotalPeriodosAdeudados.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()));
		    tfTotalAFinanciar.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()));
		    tfCapital.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getCapital()));
		    tfInteres.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getInteres()));
		    lblCondonacionIntereses.setText((new StringBuilder()).append(leyendaCondonacionIntereses).append(" (").append(coordinador.getConfiguracionPlanDePago().getPorcentajeCondonacionIntereses()).append("%)").toString());
		    tfCondonacionIntereses.setValue(Double.valueOf(coordinador.getConfiguracionPlanDePago().getMontoCondonacionIntereses()));
		    subTotalAux = subTotalAux.add(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getTotalAnticiposAdeudados()).toString()));
		    subTotalAux = subTotalAux.subtract(new BigDecimal((new StringBuilder()).append("").append(coordinador.getConfiguracionPlanDePago().getMontoCondonacionIntereses()).toString()));
		    tfSubtotal.setValue(Double.valueOf(subTotalAux.doubleValue()));
		    lblInteresFinanciacion.setText((new StringBuilder()).append(leyendaInteresPorFin).append(" (").append(coordinador.getConfiguracionPlanDePago().getPorcentajeInteresFinanciacion() * (double)Integer.parseInt(cbCuotas.getSelectedItem().toString())).append("%)").toString());
		    cbCuotas.setSelectedItem(Integer.valueOf(5));
		    cbCuotas.setSelectedItem(Integer.valueOf(2));
		    loadCombos();
		    tfPagoAnticipado.setValue(Double.valueOf(0.0D));
		} else {
		    Advisor.messageBox("No se puede hacer un plan de pago sin periodos cubiertos.", "Aviso");
		    coordinador.atras();
		}
	    } else {
		Advisor.messageBox("No se puede hacer un plan de pago con la entrega ingresada.", "Aviso");
		coordinador.atras();
	    }
	}
    }

    private void btnImprimir_actionPerformed(ActionEvent e) {
	imprimirConfiguracionPlanPago();
    }

    private void imprimirConfiguracionPlanPago() {
	if (coordinador.getConfiguracionPlanDePago().getTotal() > 0.0) {
	    EstadoCuenta estadoCuenta = new EstadoCuenta(coordinador.getBien(), coordinador.getBonificacion(), coordinador.getImpuesto(), true);
	    estadoCuenta.retrieveData();
	    EstadoCuenta estadoCuentaContado = new EstadoCuenta(coordinador.getBien(), coordinador.getBonificacion(), coordinador.getImpuesto(), true);
	    estadoCuentaContado.retrieveData();
	    ResumenPlanDePago resumen = new ResumenPlanDePago();
	    cargarResumen(resumen);
	    BasicReport report = new BasicReport(PanelPrepararPlanDePagos.class.getResource("xml/EstadoCuentaYMoratoriaReport.xml"));
	    TipoImpuesto tipoImpuesto = new TipoImpuesto();
	    tipoImpuesto.setIdTipoImpuesto(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto());
	    tipoImpuesto.retrieveData();
	    report.setProperty("impuesto", tipoImpuesto.getNombre().toString().toUpperCase());
	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 1 || coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 2) {
		report.setProperty("cuentacorriente", coordinador.getBien().getCatastro().getNroCuenta());
		report.setProperty("tituloapoderado", "Contribuyente:");
		report.setProperty("titulobien", "Catastro:");
		report.setProperty("titulovalorfiscal", "Val. Fisc.:");
		report.setProperty("valorFiscal", Double.valueOf(Double.parseDouble(coordinador.getBien().getCatastro().getValfis())));
		report.setProperty("bien", Integer.valueOf(coordinador.getBien().getCatastro().getCatastro()));
		report.setProperty("apoderado", (new StringBuilder()).append(coordinador.getBien().getCatastro().getApoderadoLastName()).append(" ").append(coordinador.getBien().getCatastro().getApoderadoName()).toString());
		report.setProperty("domicilio", (new StringBuilder()).append(coordinador.getBien().getCatastro().getDcalle()).append(" Nº ").append(coordinador.getBien().getCatastro().getDnumro()).toString());
		report.setProperty("contribuyente", (new StringBuilder()).append(coordinador.getBien().getCatastro().getDomape()).append(" ").append(coordinador.getBien().getCatastro().getDomapeAux()).toString());
	    } else {
		report.setProperty("cuentacorriente", "");
		report.setProperty("tituloapoderado", "");
		report.setProperty("titulobien", "Dominio:");
		report.setProperty("titulovalorfiscal", "");
		report.setProperty("valorFiscal", "");
		report.setProperty("bien", coordinador.getBien().getAutomotor().getDominio());
		report.setProperty("apoderado", "");
		report.setProperty("domicilio", coordinador.getBien().getAutomotor().getDomicilio());
		report.setProperty("contribuyente", coordinador.getBien().getAutomotor().getTitular());
	    }
	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 3)
		report.setProperty("periodos", (new StringBuilder()).append((estadoCuenta.getPeriodoInicial() + 1) / 2).append("/").append(estadoCuenta.getAnioInicial()).append(" al ").append((estadoCuenta.getPeriodoFinal() + 1) / 2).append("/").append(estadoCuenta.getAnioFinal()).toString());
	    else
		report.setProperty("periodos", (new StringBuilder()).append(estadoCuenta.getPeriodoInicial()).append("/").append(estadoCuenta.getAnioInicial()).append(" al ").append(estadoCuenta.getPeriodoFinal()).append("/").append(estadoCuenta.getAnioFinal()).toString());
	    report.setProperty("cantperiodos", Integer.valueOf(estadoCuenta.getCantPeriodos()));
	    report.setProperty("deudaparcial", Double.valueOf(estadoCuenta.getDeudaParcial()));
	    report.setProperty("interespormora", Double.valueOf(estadoCuenta.getInteresParcial()));
	    report.setProperty("subtotaldeuda", Double.valueOf(estadoCuenta.getSubTotal()));
	    report.setProperty("bonificacionespecialoriginal", Double.valueOf(estadoCuenta.getMontoBonificacion()));
	    report.setProperty("montototalconintereses", Double.valueOf(estadoCuentaContado.getTotal()));
	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() != 0)
		report.setProperty("titulodtoespecial", estadoCuenta.getBonificacion().getNombre());
	    else
		report.setProperty("titulodtoespecial", "");
	    if (coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto() == 3)
		report.setProperty("periodosCtdo", (new StringBuilder()).append((estadoCuentaContado.getPeriodoInicial() + 1) / 2).append("/").append(estadoCuentaContado.getAnioInicial()).append(" al ").append((estadoCuenta.getPeriodoFinal() + 1) / 2).append("/").append(estadoCuentaContado.getAnioFinal()).toString());
	    else
		report.setProperty("periodosCtdo", (new StringBuilder()).append(estadoCuentaContado.getPeriodoInicial()).append("/").append(estadoCuentaContado.getAnioInicial()).append(" al ").append(estadoCuenta.getPeriodoFinal()).append("/").append(estadoCuentaContado.getAnioFinal()).toString());
	    report.setProperty("cantperiodosCtdo", Integer.valueOf(estadoCuentaContado.getCantPeriodos()));
	    report.setProperty("lblbonifintereses", "0.00 %");
	    report.setProperty("deudaparcialctdo", Double.valueOf(estadoCuentaContado.getDeudaParcial()));
	    report.setProperty("interespormoractdo", Double.valueOf(estadoCuentaContado.getInteresParcial()));
	    report.setProperty("subtotaldeudactdo", Double.valueOf(estadoCuentaContado.getSubTotal()));
	    report.setProperty("condonacionintereses", Double.valueOf(0.0D));
	    report.setProperty("titulodtoespecialcontado", estadoCuentaContado.getBonificacion().getNombre());
	    report.setProperty("bonificacionespecialcontado", Double.valueOf(estadoCuentaContado.getMontoBonificacion()));
	    report.setProperty("bonificacioncontadoplandepago", Double.valueOf(0.0D));
	    report.setProperty("lblbonifcontado", " 0.00 %");
	    report.setProperty("montocontado", Double.valueOf(estadoCuentaContado.getTotal()));
	    report.setProperty("entrega", Double.valueOf(resumen.getEntrega()));
	    report.setProperty("deudaparcialpp", Double.valueOf(resumen.getDeudaParcial()));
	    report.setProperty("interespormorapp", Double.valueOf(resumen.getInteresPorMora()));
	    report.setProperty("subtotaldeudapp", Double.valueOf(resumen.getSubTotalDeuda()));
	    report.setProperty("titulodtoespecialpp", (new StringBuilder()).append(resumen.getBonificacionEspecial()).append(" ( ").append(resumen.getPorcentajeBonificacionEspecial() * 100D).append(" % )").toString());
	    report.setProperty("bonificacionespecialplandepago", Double.valueOf(resumen.getMontoBonificacionEspecial()));
	    report.setProperty("subtotalpp", Double.valueOf(resumen.getSubTotal()));
	    report.setProperty("cubreentrega", Double.valueOf(resumen.getMontoCubiertoEntrega()));
	    if (resumen.getEntrega() > 0.0)
		report.setProperty("periodoanticipos", (new StringBuilder()).append("(").append(resumen.getPeriodoAnticipos()).append(")").toString());
	    else
		report.setProperty("periodoanticipos", "");
	    report.setProperty("saldoafinanciar", Double.valueOf(resumen.getSaldoAFinanciar()));
	    report.setProperty("periodoplandepago", (new StringBuilder()).append("(").append(resumen.getPeriodoPlanDePago()).append(")").toString());
	    report.setProperty("lblbonifintereses", (new StringBuilder()).append("(").append(resumen.getPorcentajeCondIntereses()).append(" %)").toString());
	    report.setProperty("condonacionintereses", Double.valueOf(resumen.getMontoCondIntereses()));
	    report.setProperty("lblporcentajeinteresporfinanciacion", (new StringBuilder()).append("").append(resumen.getPorcentajeIntXFinanciacion() * (double)resumen.getCantidadCuotas()).append(" % ( ").append(resumen.getPorcentajeIntXFinanciacion()).append(" % por cada cuota)").toString());
	    report.setProperty("porcentajeinteresporfinanciacion", Double.valueOf(resumen.getMontoIntXFinanciacion()));
	    report.setProperty("totalpp", Double.valueOf(resumen.getTotal()));
	    report.setProperty("leyendacuotas", resumen.getLeyendaCuotas());
	    String param = (new StringBuilder()).append("").append(coordinador.getImpuesto().getTipoImpuesto().getIdTipoImpuesto()).append(",").append(coordinador.getBien().getIdBien()).append(",").append(coordinador.getConfiguracionPlanDePago().getMayorAnticipo()).append(",").append(coordinador.getConfiguracionPlanDePago().getMayorAnio()).toString();
	    report.addTableModel("taxes.xmlGetFeesByPlanOfPayment", param);
	    report.doReport();
	} else {
	    Advisor.messageBox("El Monto a pagar debe ser mayor que cero", "Aviso");
	}
    }

    private void cargarResumen(ResumenPlanDePago _resumen) {
	String periodoAnticipos = "";
	String textoCuotas = "";
	EstadoCuenta estadoCuenta = new EstadoCuenta(coordinador.getBien(), coordinador.getBonificacion(), coordinador.getImpuesto(), false);
	estadoCuenta.retrieveData();
	_resumen.setEntrega(coordinador.getConfiguracionPlanDePago().getPagoAnticipado());
	_resumen.setDeudaParcial(estadoCuenta.getDeudaParcial());
	_resumen.setInteresPorMora(estadoCuenta.getInteresParcial());
	_resumen.setSubTotalDeuda(estadoCuenta.getSubTotal());
	_resumen.setBonificacionEspecial(estadoCuenta.getBonificacion().getNombre());
	_resumen.setMontoBonificacionEspecial(estadoCuenta.getMontoBonificacion());
	_resumen.setLeyendaBonificacionEspecial((new StringBuilder()).append("Bonificacion especial        (").append(estadoCuenta.getBonificacion().getNombre()).append(")     ").append(estadoCuenta.getMontoBonificacion()).toString());
	_resumen.setSubTotal(estadoCuenta.getTotal());
	if (coordinador.getConfiguracionPlanDePago().getPagoAnticipado() > 0.0D)
	    _resumen.setMontoCubiertoEntrega(tfPagoAnticipado.getAmount());
	else
	    _resumen.setMontoCubiertoEntrega(0.0D);
	if (coordinador.getConfiguracionPlanDePago().getPagoAnticipado() > 0.0D) {
	    if (coordinador.getBien().esAutomotor())
		periodoAnticipos = (new StringBuilder()).append((coordinador.getConfiguracionPlanDePago().getMenorAnticipoEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnioEntrega()).append(" AL ").append((coordinador.getConfiguracionPlanDePago().getMayorAnticipoEntrega() + 1) / 2).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnioEntrega()).toString();
	    else
		periodoAnticipos = (new StringBuilder()).append(coordinador.getConfiguracionPlanDePago().getMenorAnticipoEntrega()).append("/").append(coordinador.getConfiguracionPlanDePago().getMenorAnioEntrega()).append(" AL ").append(coordinador.getConfiguracionPlanDePago().getMayorAnticipoEntrega()).append("/").append(coordinador.getConfiguracionPlanDePago().getMayorAnioEntrega()).toString();
	} else {
	    periodoAnticipos = "--";
	}
	_resumen.setPeriodoAnticipos(periodoAnticipos);
	_resumen.setSaldoAFinanciar(tfTotalAFinanciar.getAmount());
	_resumen.setPeriodoPlanDePago(tfPeriodo.getTextField().getText());
	_resumen.setCantidadAnticiposPP(coordinador.getConfiguracionPlanDePago().getCantidadAnticipos());
	_resumen.setCapital(tfCapital.getAmount());
	_resumen.setInteres(tfInteres.getAmount());
	_resumen.setPorcentajeCondIntereses(coordinador.getConfiguracionPlanDePago().getPorcentajeCondonacionIntereses());
	_resumen.setMontoCondIntereses(tfCondonacionIntereses.getAmount());
	_resumen.setPorcentajeIntXFinanciacion(coordinador.getConfiguracionPlanDePago().getPorcentajeInteresFinanciacion());
	_resumen.setMontoIntXFinanciacion(tfInteresFinanciacion.getAmount());
	_resumen.setTotal(tfTotalAPagar.getAmount());
	if (coordinador.getConfiguracionPlanDePago().getValorCuotasRestantes() > 0.0D)
	    textoCuotas = (new StringBuilder()).append("(1 cuota de $ ").append(Format.toDouble(coordinador.getConfiguracionPlanDePago().getValorPrimerCuota())).append(" + ").append(coordinador.getConfiguracionPlanDePago().getCantidadCuotas() - 1).append(" cuotas de $ ").append(coordinador.getConfiguracionPlanDePago().getValorCuotasRestantes()).append(").").toString();
	else
	    textoCuotas = (new StringBuilder()).append("(").append(coordinador.getConfiguracionPlanDePago().getCantidadCuotas()).append(" cuotas de $ ").append(coordinador.getConfiguracionPlanDePago().getValorPrimerCuota()).append(").").toString();
	_resumen.setCantidadCuotas(coordinador.getConfiguracionPlanDePago().getCantidadCuotas());
	_resumen.setLeyendaCuotas(textoCuotas);
	_resumen.setPorcentajeBonificacionEspecial(estadoCuenta.getBonificacion().getPorcentaje().doubleValue());
    }

    private void setLeyendaInteresFinanciacion() {
	lblInteresFinanciacion.setText((new StringBuilder()).append(leyendaInteresPorFin).append(" (").append(coordinador.getConfiguracionPlanDePago().getPorcentajeInteresFinanciacion() * (double)Integer.parseInt(cbCuotas.getSelectedItem().toString())).append("%)").toString());
    }
}

