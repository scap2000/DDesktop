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
 * GaiaDistrictPanel.java
 *
 * */

package org.digitall.projects.unsa.gaia.interfaces;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.digitall.common.geo.mapping.BasicDrawEngine;
import org.digitall.common.reports.BasicReport;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicList;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTabbedPane;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.grid.GridPanel;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.esri.ESRIPoint;
import org.digitall.lib.geo.esri.ESRIPolygon;
import org.digitall.lib.geo.gaia.GaiaEnvironment;
import org.digitall.lib.geo.gaia.components.GaiaConfigPanel;
import org.digitall.lib.sql.LibSQL;

public class GaiaDistrictPanel extends GaiaConfigPanel {

    private BorderLayout borderLayout1 = new BorderLayout();
    private BasicPanel jpDatosPrincipales = new BasicPanel();
    private BasicPanel jpFichaTecnica = new BasicPanel();
    private BasicLabel lblDepartamento = new BasicLabel();
    private BasicPanel jpDepartamento = new BasicPanel();
    private BasicLabel lblFichaTecnica = new BasicLabel();
    
    private int[] sizeColumnList = { 287, 81 };
    private Vector dataRow = new Vector();
    private GridPanel listPanel = new GridPanel(5000, sizeColumnList, "Listado de Escuelas", dataRow) {

	@Override
	public void calculate() {
	    updateTotals();
	}
    };
    private Vector listHeader = new Vector();
    
    private BorderLayout borderLayout2 = new BorderLayout();

    private BasicTabbedPane tabContainer = new BasicTabbedPane();
    private PrintButton btnPrintFichaCatastral = new PrintButton();
    private BasicDrawEngine drawEngine = null;

    public GaiaDistrictPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(borderLayout1);
	this.setSize(new Dimension(446, 196));
	this.setBounds(new Rectangle(10, 10, 446, 206));
	listPanel.removeControls();
	jpDatosPrincipales.setBorder(BorderFactory.createLineBorder(new Color(148, 107, 0), 2));
	jpDatosPrincipales.setBackground(Color.white);
	jpDatosPrincipales.setLayout(null);

	jpFichaTecnica.setBounds(new Rectangle(5, 5, 430, 25));
	jpFichaTecnica.setBackground(new Color(198, 198, 255));
	jpFichaTecnica.setBorder(BorderFactory.createLineBorder(new Color(148, 107, 0), 2));
	jpFichaTecnica.setLayout(borderLayout2);

	jpDepartamento.setBounds(new Rectangle(5, 5, 430, 25));
	jpDepartamento.setBackground(new Color(198, 198, 255));
	jpDepartamento.setBorder(BorderFactory.createLineBorder(new Color(148, 107, 0), 2));

	lblFichaTecnica.setText("Departamento: ");
	lblFichaTecnica.setFont(new Font("Dialog", 1, 12));
	lblFichaTecnica.setForeground(new Color(0, 99, 148));
	lblFichaTecnica.setHorizontalTextPosition(SwingConstants.CENTER);
	lblFichaTecnica.setHorizontalAlignment(SwingConstants.CENTER);
	lblDepartamento.setText("Ficha Técnica - Identificador Nº: ---");
	lblDepartamento.setFont(new Font("Dialog", 1, 12));
	lblDepartamento.setForeground(new Color(0, 99, 148));
	lblDepartamento.setHorizontalTextPosition(SwingConstants.CENTER);
	lblDepartamento.setHorizontalAlignment(SwingConstants.CENTER);
	listPanel.setBounds(new Rectangle(5, 35, 430, 115));
	//jpDatosPrincipales.add(btnPrintFichaCatastral, null);
	jpDepartamento.add(lblDepartamento, BorderLayout.CENTER);
	jpDatosPrincipales.add(jpDepartamento, null);
	jpDatosPrincipales.add(listPanel, null);
	tabContainer.addTab("Datos Principales", jpDatosPrincipales);
	//tabContainer.addTab("Escuelas", jpEscuelas);
	jpFichaTecnica.add(lblFichaTecnica, BorderLayout.CENTER);
	this.add(jpFichaTecnica, BorderLayout.NORTH);
	this.add(tabContainer, BorderLayout.WEST);
	tabContainer.setBounds(new Rectangle(95, 290, 60, 40));
	tabContainer.setSize(new Dimension(440, 182));
	tabContainer.setPreferredSize(new Dimension(445, 57));
	tabContainer.setMaximumSize(new Dimension(445, 32767));
	tabContainer.setMinimumSize(new Dimension(445, 57));

	btnPrintFichaCatastral.setToolTipText("Imprimir Ficha Catastral - Control para todos los catastros de la parcela - Shift para varios catastros a elección");
        btnPrintFichaCatastral.setBounds(new Rectangle(405, 115, 25, 30));
        btnPrintFichaCatastral.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnPrintFichaCatastral_actionPerformed(e);
                }
		
	    });
	setListHeader();
    }

    private void setListHeader() {
	listHeader.removeAllElements();
	listHeader.addElement(Environment.lang.getProperty("Escuela"));
	listHeader.addElement("*"); //Environment.lang.getProperty("Departamento"));
	listHeader.addElement(Environment.lang.getProperty("Encuestas"));
	String params = "-1";
	listPanel.setParams("unsa.getQtyEncuestasPorEscuelasPorDepartamento", params, listHeader);
    }

    private void btnPrintFichaCatastral_actionPerformed(ActionEvent e) {
	
    }
    
    public void setContentObject(Object _contentObject) {
	super.setContentObject(_contentObject);
	if (_contentObject instanceof ESRIPolygon) {
	    loadDistrict(((ESRIPolygon)_contentObject).getIdPolygon());
	}
    }

    private void loadDistrict(int _idDistrict) {
	listPanel.refresh(_idDistrict);
	lblFichaTecnica.setText("Departamento: " + LibSQL.getString("unsa.getdepartamento", _idDistrict));
	doReport(_idDistrict);
    }

    private void doReport(int _idDistrict) {
	ResultSet _rs = LibSQL.exFunction("unsa.getQtyEncuestasPorEscuelasPorDepartamento", _idDistrict + ",0,0");
	boolean _hasResults = false;
	int _total = 0;
	StringBuilder _queryResult = new StringBuilder("<html>");
	_queryResult.append("<p align=center><font size=8px><b>Departamento \"" + LibSQL.getString("unsa.getdepartamento", _idDistrict) + "\"</b><br></font></p><br>" );
	_queryResult.append("<table border=1 align=center>");
	_queryResult.append("<tr><th>Escuela</th><th>Cantidad</th></t>");

	try {
	    while (_rs.next()) {
		_hasResults = true;
		_queryResult.append("<tr>");
		_queryResult.append("<td align=left>" + _rs.getString("escuela") + "</td>");
		_queryResult.append("<td align=right>" + _rs.getInt("cantidad") + "</td>");
		_total += _rs.getInt("cantidad");
		_queryResult.append("</tr>");
	    }
	} catch (SQLException sqle) {
	    // TODO: Add catch code
	    sqle.printStackTrace();
	}

	_queryResult.append("<tr>");
	_queryResult.append("<td><b>TOTAL ENCUESTAS</b></td>");
	_queryResult.append("<td align=right>" + _total + "</td>");
	_queryResult.append("</tr>");
	_queryResult.append("</table></p><br><p align=center>");
	_queryResult.append("</html>");

	Advisor.showInternalMessageDialog(_queryResult.toString());

	/*BasicReport _report = new BasicReport(GaiaDistrictPanel.class.getResource("xml/FichaCatastral.xml"));
        _report.setProperty("title", "Ficha Catastral");
	String _catastro = "-1";
        ResultSet _rsCatastro = LibSQL.exFunction("taxes.getCatastro", _idCatastro + ",''"); 
        try  {
            if (_rsCatastro.next())  {
		_catastro = _rsCatastro.getString("numero");
                _report.setProperty("departamento", _rsCatastro.getString("departamento"));
                _report.setProperty("localidad", _rsCatastro.getString("localidad"));
                _report.setProperty("catastro", _catastro);
                _report.setProperty("numdocumento", _rsCatastro.getString("domnudoc"));
                _report.setProperty("vigencia", _rsCatastro.getString("vigencia"));
                _report.setProperty("plano", _rsCatastro.getString("plano"));
                _report.setProperty("fechaasignacion", _rsCatastro.getString("tecfeasg"));
                _report.setProperty("seccion", _rsCatastro.getString("tecsec"));
                _report.setProperty("manzana", _rsCatastro.getString("tecman"));
                _report.setProperty("letramanzana", _rsCatastro.getString("tecmanl"));
                _report.setProperty("parcela", _rsCatastro.getString("tecpar"));
                _report.setProperty("letraparcela", _rsCatastro.getString("tecparl"));
                _report.setProperty("catoriginal1", _rsCatastro.getString("tecorg1"));
                _report.setProperty("catoriginal2", _rsCatastro.getString("tecorg2"));
                _report.setProperty("supurbana", _rsCatastro.getDouble("tecsuurb") + " m2");
                _report.setProperty("terreno", _rsCatastro.getString("terreno"));
                _report.setProperty("valorterreno", _rsCatastro.getDouble("terval"));
                _report.setProperty("valormejoras", _rsCatastro.getDouble("tervmej"));
                _report.setProperty("valorfiscal", _rsCatastro.getDouble("valfis"));
                _report.setProperty("direccion", _rsCatastro.getString("dcalle"));
                _report.setProperty("direccionnumero", _rsCatastro.getString("dnumro"));
                _report.setProperty("barrio", _rsCatastro.getString("ddesbarrio"));
		_report.setProperty("plano", _rsCatastro.getString("plano"));
		int _scale = 4;
		if (drawEngine != null) {
		    _report.setProperty("mapImage",drawEngine.getCadastralMapImage(new Dimension(250*_scale,250*_scale), LibSQL.getInt(GaiaEnvironment.getScheme() + ".getidparcelabycatastro", _rsCatastro.getString("numero"))));
		}

		_report.addTableModel("taxes.getDatosTitularPorCatastro", _catastro);
		
            }
        } catch (SQLException ex)  {
            ex.printStackTrace();
        }
        
        _report.doReport();*/
    }

    public void setDrawEngine(BasicDrawEngine _drawEngine) {
	drawEngine = _drawEngine;
    }
    
    private void updateTotals() {
	listPanel.setToolTipText("Total: " + (int)listPanel.getSum(2));
	lblDepartamento.setText("Total encuestas: " + (int)listPanel.getSum(2));
    }
}
