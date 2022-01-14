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
 * F1Escuela.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.interfaces;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;

import java.beans.PropertyVetoException;

import java.io.File;
import java.io.IOException;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Types;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.digitall.common.components.combos.JCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.geo.mapping.BasicDrawEngine;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.basic.BasicTabbedPane;
import org.digitall.lib.components.buttons.AddButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.NextWizardButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.esri.ESRIPoint;
import org.digitall.lib.geo.esri.ESRIPolygon;
import org.digitall.lib.geo.mapping.classes.Layer;
import org.digitall.lib.geo.shapefile.ShapeTypes;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.unsa.censodocpri.classes.Coordinador;
import org.digitall.projects.unsa.censodocpri.classes.F1;

import org.digitall.projects.unsa.censodocpri.classes.OptionItem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class F1Escuela extends BasicPrimitivePanel {

    private BasicPanel contentPane = new BasicPanel();
    private CBInput cbEscuelas = new CBInput(0, "Escuelas");
    private TFInput tfBuscar = new TFInput(DataTypes.STRING, "Buscar", false);

    private TFInput tfNumero = new TFInput(DataTypes.STRING, "Número", false);
    private TFInput tfNombre = new TFInput(DataTypes.STRING, "Nombre", false);
    private TFInput tfDomicilio = new TFInput(DataTypes.STRING, "Domicilio", false);
    private TFInput tfBarrio = new TFInput(DataTypes.STRING, "Barrio", false);
    private TFInput tfTelefono = new TFInput(DataTypes.STRING, "Teléfono", false);
    private TFInput tfDepartamento = new TFInput(DataTypes.STRING, "Departamento", false);
    private TFInput tfLocalidad = new TFInput(DataTypes.STRING, "Localidad", false);
    private TFInput tfSector = new TFInput(DataTypes.STRING, "Sector", false);
    private TFInput tfDirector = new TFInput(DataTypes.STRING, "Director", false);
    private NextWizardButton btnNext = new NextWizardButton();
    private AddButton btnAdd = new AddButton();
    private ModifyButton btnModify = new ModifyButton();
    private PrintButton btnPrintDepartamental = new PrintButton();
    private PrintButton btnPrintRegional = new PrintButton();
    
    private JArea tfAyuda = new JArea();

    private Coordinador coordinador;

    public F1Escuela(Coordinador _coordinador) {
	coordinador = _coordinador;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	tfAyuda.setEditable(false);
	tfAyuda.setContentType(JArea.CONTENT_HTML);
	String _ayuda = "<html><p align=center><font size=5><b>¿Un poco de ayuda?</b></font></p>" + 
			"<br><p align=center><b>Los criterios de carga son los siguientes</b>:</p>" +
			"<p align=left>* Si en una pregunta de múltiples opciones <b>excluyentes</b>" +
			" (por ejemplo <i>Sí; No; No sabe/No contesta</i>) el encuestado" +
			" optó por más de una, se considera un error y se debe seleccionar la opción <i>\"error\"</i>." +
			"<br>* Al grabar una encuesta, el sistema devuelve un número identificador, dicho número deberá escribirse" +
			" en la esquina superior derecha de la encuesta (en el papel) para usarlo en caso de necesitar corregirla." +
			"</p>" +
			"<br><p align=center><b>Botones inferiores</b>:</p>" +
			"<p align=left>* Para reiniciar la carga de la encuesta presione el botón <i>\"Nueva encuesta\"</i>." + 
			"<br>* Para <b>corregir</b> una encuesta ya cargada presione el botón <i>\"Corregir encuesta\"</i>," +
			" luego deberá ingresar el número de encuesta a corregir." +
			"<br>* Para generar informes, presione el botón <i>\"Gráficas y porcentajes estadísticos de los datos cargados\"</i>," +
			"este proceso puede demorar un poco dependiendo de la cantidad de encuestas a procesar y de su conexión a internet." +
			"</p>" +
			"<br><p align=center><b>Informes: (Tablas y Gráficas)</b>:</p>" +
			"<p align=left>Al generar el informe aparecerá una ventana con dos pestañas:" +
			"<br>* <b>En la primer pestaña</b> (<i>Tablas</i>) figurarán los datos analíticos. Se puede seleccionar una parte (o el total)" +
			" con el ratón, presionar <b>Ctrl+c</b> para copiar y luego <b>Ctrl-v</b> para pegarlo en un documento" +
			" o planilla de cálculo (generalmente el programa destino conserva el formato y los valores numéricos)." +
			"<br>* <b>En la segunda pestaña</b> (<i>Gráficas</i>) se puede seleccionar la gráfica a mostrar en pantalla y navegar con el" +
			" puntero del ratón sobre la misma para ver los datos de las barras o porciones de la torta." +
			" Con un click derecho sobre la gráfica se pueden configurar algunos parámetros, imprimirla o grabarla como una imagen PNG" +
			" (en este caso se recomienda primero cambiar el tamaño de la ventana al deseado y luego guardar la imagen)." +
			"</p>" +
			"</html>";
	tfAyuda.setText(_ayuda);
	tfAyuda.setPreferredSize(new Dimension(645, 400));
	Advisor.showInternalMessageDialog(_ayuda);
	contentPane.add(cbEscuelas);
	contentPane.add(tfBuscar);
	contentPane.add(tfNumero);
	contentPane.add(tfNombre);
	contentPane.add(tfDomicilio);
	contentPane.add(tfBarrio);
	contentPane.add(tfTelefono);
	contentPane.add(tfDepartamento);
	contentPane.add(tfLocalidad);
	contentPane.add(tfSector);
	contentPane.add(tfDirector);
	contentPane.add(tfAyuda, null);
	this.add(contentPane, BorderLayout.CENTER);
	this.setBounds(new Rectangle(10, 10, 675, 383));
	contentPane.setLayout(null);
	cbEscuelas.setBounds(new Rectangle(145, 20, 470, 35));
	tfBuscar.setBounds(new Rectangle(15, 20, 120, 35));
	tfNumero.setBounds(new Rectangle(15, 65, 120, 35));
	tfNombre.setBounds(new Rectangle(140, 65, 475, 35));
	tfDomicilio.setBounds(new Rectangle(15, 105, 280, 35));
	tfBarrio.setBounds(new Rectangle(300, 105, 185, 35));
	tfTelefono.setBounds(new Rectangle(490, 105, 125, 35));
	tfDepartamento.setBounds(new Rectangle(15, 145, 165, 35));
	tfLocalidad.setBounds(new Rectangle(185, 145, 120, 35));
	tfSector.setBounds(new Rectangle(310, 145, 120, 35));
	tfDirector.setBounds(new Rectangle(435, 145, 180, 35));

	tfNumero.setEditable(false);
	tfNombre.setEditable(false);
	tfDomicilio.setEditable(false);
	tfBarrio.setEditable(false);
	tfTelefono.setEditable(false);
	tfDepartamento.setEditable(false);
	tfLocalidad.setEditable(false);
	tfSector.setEditable(false);
	tfDirector.setEditable(false);

	btnNext.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnNext_actionPerformed(e);
		}
	    });
	addButton(btnNext);
	btnNext.setToolTipText("Aceptar la escuela seleccionada y comenzar la carga de la encuesta");
	btnModify.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnModify_actionPerformed(e);
		}
	    });
	addButton(btnModify);
	btnModify.setToolTipText("Corregir una encuesta previamente cargada");

	btnAdd.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnAdd_actionPerformed(e);
		}
	    });
	addButton(btnAdd);
	btnAdd.setToolTipText("Reiniciar los datos y comenzar una encuesta nueva");

	btnPrintDepartamental.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPrintDepartamental_actionPerformed(e);
		}
	    });
	addButton(btnPrintDepartamental);

	btnPrintRegional.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnPrintRegional_actionPerformed(e);
		}
	    });
	addButton(btnPrintRegional);

	btnPrintDepartamental.setToolTipText("Gráficas y porcentajes estadísticos de los datos cargados");
	btnPrintRegional.setToolTipText("Gráficas y porcentajes estadísticos de los datos cargados por regiones");


	tfBuscar.getTextField().addKeyListener(new KeyAdapter() {

		public void keyTyped(KeyEvent e) {
		    if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
			buscarEscuela();
		    }
		}

	    });

	cbEscuelas.addItemListener(new ItemListener() {

		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			loadEscuela();
		    }
		}
	    });

	tfAyuda.setBounds(new Rectangle(15, 190, 645, 140));
	
    }

    private void loadEscuela() {
	if (cbEscuelas.getSelectedIndex() != -1) {
	    coordinador.setEscuela(cbEscuelas.getSelectedValue() + "|" + cbEscuelas.getSelectedValueRef().toString());
	    String[] _data = cbEscuelas.getSelectedValueRef().toString().split("\\|", 9);
	    tfNumero.setText(_data[0]);
	    tfNombre.setText(_data[1]);
	    tfDomicilio.setText(_data[2]);
	    tfBarrio.setText(_data[3]);
	    tfTelefono.setText(_data[4]);
	    tfDepartamento.setText(_data[5]);
	    tfLocalidad.setText(_data[6]);
	    tfSector.setText(_data[7]);
	    tfDirector.setText(_data[8]);
	} else {
	    coordinador.setEscuela("");
	    tfNumero.setText("");
	    tfNombre.setText("");
	    tfDomicilio.setText("");
	    tfBarrio.setText("");
	    tfTelefono.setText("");
	    tfDepartamento.setText("");
	    tfLocalidad.setText("");
	    tfSector.setText("");
	    tfDirector.setText("");
	}
    }

    private void buscarEscuela() {
	ResultSet _escuelas = LibSQL.exFunction("unsa.getallescuelas", tfBuscar.getStringForSQL());
	cbEscuelas.removeAllItems();
	try {
	    while (_escuelas.next()) {
		cbEscuelas.getCombo().addItem("(" + _escuelas.getString("numero") + ") " + _escuelas.getString("nombre"), _escuelas.getInt("idescuela"),
		    _escuelas.getString("numero") + "|" +
		    _escuelas.getString("nombre") + "|" +
		    _escuelas.getString("domicilio") + "|" +
		    _escuelas.getString("barrio") + "|" +
		    _escuelas.getString("telefono") + "|" +
		    _escuelas.getString("departamento") + "|" +
		    _escuelas.getString("localidad") + "|" +
		    _escuelas.getString("sector") + "|" +
		    _escuelas.getString("director")
		);
	    }
	    loadEscuela();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void btnNext_actionPerformed(ActionEvent e) {
	if (cbEscuelas.getSelectedIndex() != -1) {
	    coordinador.setEscuela(cbEscuelas.getSelectedValue() + "|" + cbEscuelas.getSelectedValueRef().toString());
	} else {
	    coordinador.setEscuela("");
	}
	coordinador.showF1q1();
    }

    private void btnModify_actionPerformed(ActionEvent e) {
	String _categoria = (String)JOptionPane.showInputDialog(this, "Ingrese el ID de la Encuesta a corregir", "ID de Encuesta", JOptionPane.QUESTION_MESSAGE, null, null, "");
	if (_categoria != null) {
	    int _idEncuesta = -1;
	    try {
		_idEncuesta = Integer.parseInt(_categoria);
		F1 _encuesta = new F1(_idEncuesta);
		_encuesta.retrieveData();
		if (_encuesta.getIdEncuesta() != -1) {
		    if (!_encuesta.getEscuela().split("\\|")[0].equals("")) {
			tfBuscar.setValue("");
			buscarEscuela();
			cbEscuelas.setSelectedID(_encuesta.getEscuela().split("\\|")[0]);
			if (!cbEscuelas.getSelectedValue().toString().equals(_encuesta.getEscuela().split("\\|")[0])) {
			    cbEscuelas.removeAllItems();
			}
		    } else {
			cbEscuelas.removeAllItems();
		    }
		    loadEscuela();
		    coordinador.setEncuesta(_encuesta, true);
		} else {
		    Advisor.messageBox("ID de encuesta no válido", "Error");
		}
	    } catch (NumberFormatException nfe) {
		// TODO: Add catch code
		Advisor.messageBox("<html><p align=center>ID de encuesta no válido<br>Debe introducir sólo números enteros</p></html>", "Error");
	    }
	}
    }
    
    private void btnAdd_actionPerformed(ActionEvent e) {
	coordinador.newEncuesta();
    }

    private void btnPrintDepartamental_actionPerformed(ActionEvent e) {
	JCombo _cbDepartamentos = new JCombo();
	_cbDepartamentos.setGeneralItem(true);
	_cbDepartamentos.loadJCombo("unsa.getalldepartamentos","");
	String _departamento = ((String)JOptionPane.showInternalInputDialog(Environment.getActiveDesktop(), "Seleccione el departamento", "Departamento", JOptionPane.QUESTION_MESSAGE, null, _cbDepartamentos.getItemsVector().toArray(), ""));
	if (_departamento != null) {
	    _cbDepartamentos.setSelectedItem(_departamento);
	    if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) {
		coordinador.doCSVReport("Encuestas Docentes Primarios", "unsa.getallencuestasforcsv", Integer.parseInt(_cbDepartamentos.getSelectedValue().toString()));
	    } else {
		doReport(Integer.parseInt(_cbDepartamentos.getSelectedValue().toString()), _cbDepartamentos.getSelectedItem().toString());
	    }
	}
    }
    
    private void btnPrintRegional_actionPerformed(ActionEvent e) {
	JCombo _cbRegiones = new JCombo();
	_cbRegiones.setGeneralItem(true);
	_cbRegiones.loadJCombo("unsa.getallregiones","");
	String _region = ((String)JOptionPane.showInternalInputDialog(Environment.getActiveDesktop(), "Seleccione la región", "Región", JOptionPane.QUESTION_MESSAGE, null, _cbRegiones.getItemsVector().toArray(), ""));
	if (_region != null) {
	    _cbRegiones.setSelectedItem(_region);
	    if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == ActionEvent.SHIFT_MASK) {
		coordinador.doCSVReport("Encuestas Docentes Primarios", "unsa.getallencuestasforcsv", Integer.parseInt(_cbRegiones.getSelectedValue().toString()));
	    } else {
		doReportRegional(Integer.parseInt(_cbRegiones.getSelectedValue().toString()), _cbRegiones.getSelectedItem().toString());
	    }
	}
    }

    private void doReport(int _idDepartamento, String _departamento) {
	BasicTabbedPane _multiQueryTabbedPane = new BasicTabbedPane();
	final HashMap _chartsList = new HashMap();
	StringBuilder _queryResult = new StringBuilder("<html>");

	String[] _sqlFunctions = new String[] {
	    "unsa.getallf1q1opciones",
	    "unsa.getallf1q2opinion",
	    "unsa.getallf1q3",
	    "unsa.getallf1q4",
	    "unsa.getallf1q6",
	    "unsa.getallf1q8",
	    "unsa.getallf1q8opinion",
	    "unsa.getallf1q9opciones",
	    "unsa.getallf1q10",
	    "unsa.getallf1q12",
	    "unsa.getallf1q13"
	};

	//_sqlFunctions = new String[] { "unsa.getallf1q1opciones" };

	int[] _sqlParams = new int[] {
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento,
	    _idDepartamento
	};

	String[] _preguntas = new String[] {
	    "01 - \"1. (Opciones) " + coordinador.getEncuesta().getF1q1OpcionesLabel() + "\"",
	    "02 - \"2. (Opinión) " + coordinador.getEncuesta().getF1q2OpinionLabel() + "\"",
	    "03 - \"" + coordinador.getEncuesta().getF1q3Label() + "\"",
	    "04 - \"" + coordinador.getEncuesta().getF1q4Label() + "\"",
	    "06 - \"" + coordinador.getEncuesta().getF1q6Label() + "\"",
	    "08 - \"" + coordinador.getEncuesta().getF1q8Label() + "\"",
	    "08b - \"8. (Opinión) " + coordinador.getEncuesta().getF1q8OpinionLabel() + "\"",
	    "09 - \"" + coordinador.getEncuesta().getF1q9Label() + "\"",
	    "10 - \"" + coordinador.getEncuesta().getF1q10Label() + "\"",
	    "12 - \"" + coordinador.getEncuesta().getF1q12Label() + "\"",
	    "13 - \"" + coordinador.getEncuesta().getF1q13Label() + "\""
	};

	String[] _fields = new String[] {
	    "f1q1opciones",
	    "f1q2opinion",
	    "f1q3",
	    "f1q4",
	    "f1q6",
	    "f1q8",
	    "f1q8opinion",
	    "f1q9opciones",
	    "f1q10",
	    "f1q12",
	    "f1q13"
	};
	
	Vector<String[]> _opcionesVector = new Vector<String[]>();
	_opcionesVector.add(new String[] {"libros", "revistas", "articulos", "documentos", "internet", "formacion", "experiencia",
						   "interaccion", "vidadiaria" });
	_opcionesVector.add(new String[] {"pocos", "buenaparte", "mayoria", "casitodos", "error" });
	_opcionesVector.add(new String[] {"nada", "apenas", "mucho", "neces", "error"});
	_opcionesVector.add(new String[] {"nada", "apenas", "mucho", "neces", "error"});
	_opcionesVector.add(new String[] {"ninguna", "muyneg", "negat", "sininf", "posit", "muypos"});
	_opcionesVector.add(new String[] {"ninguna", "inutil", "insuf", "relev", "indisp"});
	_opcionesVector.add(new String[] {"pocos", "buenaparte", "mayoria", "casitodos", "error" });
	_opcionesVector.add(new String[] {"libros", "revistas", "articulos", "documentos", "internet", "formacion", "experiencia",
						   "interaccion", "vidadiaria" });
	_opcionesVector.add(new String[] {"si", "no", "noobservo", "error"});
	_opcionesVector.add(new String[] {"si", "no", "nsnc", "error"});
	_opcionesVector.add(new String[] {"ninguna", "totalins", "insat", "satisf", "plensat"});
	
	Vector<String[]> _opcionesNombreVector = new Vector<String[]>();
	_opcionesNombreVector.add(new String[] {"libros", "revistas (de divulgación)", "artículos (o eventos) científicos",
							 "documentos (oficiales)", "internet", "formación docente", "experiencia docente",
							 "interacción con colegas", "vida diaria" });
	_opcionesNombreVector.add(new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error" });
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q3Opciones());
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q4Opciones());
	_opcionesNombreVector.add(new String[] {"Ninguna", "Muy negativamente", "Negativamente", "Sin influencia", "Positivamente", "Muy positivamente" });
	_opcionesNombreVector.add(new String[] {"Ninguna", "Inútiles", "Insuficientes", "Relevantes", "Indispensables" });
	_opcionesNombreVector.add(new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error" });
	_opcionesNombreVector.add(new String[] {"libros", "revistas (de divulgación)", "artículos (o eventos) científicos",
							 "documentos (oficiales)", "internet", "formación docente", "experiencia docente",
							 "interacción con colegas", "vida diaria" });
	_opcionesNombreVector.add(new String[] {"Sí", "No", "En general no observo tales diferencias", "Error"});
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q12Opciones());
	_opcionesNombreVector.add(new String[] {"Ninguna", "Totalmente insatisfecho", "Insatisfecho", "Satisfecho", "Plenamente satisfecho"});

	Vector<String[]> _categoriasVector = new Vector<String[]>();
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {
		" Categoría: Didáctica - Subcategoría: Libro de texto",
		" Categoría: Didáctica - Subcategoría: Forma de las pruebas de evaluación",
		" Categoría: Didáctica - Subcategoría: Lo que los estudiantes parecen haber aprendido",
		" Categoría: Profesionales - Subcategoría: Contribuciones de la formación",
		" Categoría: Profesionales - Subcategoría: Dominio del tema",
		" Categoría: Factores socio-culturales - Subcategoría: Nivel socio-cultural de los estudiantes",
		" Categoría: Factores socio-culturales - Subcategoría: Constitución del grupo familiar de los estudiantes",
		" Categoría: Relaciones Interpersonales - Subcategoría: Interacción con los colegas",
		" Categoría: Relaciones Interpersonales - Subcategoría: Interacción con los directivos"
		});
	_categoriasVector.add(new String[] {
		" Categoría: Estructurales - Subcategoría: Cambiar las exigencias oficiales",
		" Categoría: Estructurales - Subcategoría: Aumentar los recursos (materiales y financieros) de las escuelas",
		" Categoría: Estructurales - Subcategoría: Construir laboratorios especiales",
		" Categoría: Estructurales - Subcategoría: Asegurar conexiones seguras a internet",
		" Categoría: Estructurales - Subcategoría: Cambiar los criterios para la selección de los docentes",
		" Categoría: Profesionales - Subcategoría: Cambiar la formación (inicial y contínua)",
		" Categoría: Profesionales - Subcategoría: Reorganizar el trabajo docente",
		" Categoría: Profesionales - Subcategoría: Involucrar agentes extra-escolares en la práctica educativa",
		" Categoría: Profesionales - Subcategoría: Intercambiar ideas con colegas acerca de las acciones exitosas o no en el aula",
		" Categoría: Profesionales - Subcategoría: Vincular la práctica docente con los resultados de la investigación educativa",
		" Categoría: Didácticas - Subcategoría: Desarrollar nuevos materiales didácticos",
		" Categoría: Didácticas - Subcategoría: Cambiar los criterios de evaluación del aprendizaje"
		});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {
		    " Desde el punto de vista intelectual",
		    " Desde el punto de vista personal",
		    " Desde el punto de vista social",
		    " Desde el punto de vista económico"
		});


	ResultSet _result;
	
	try {
	    for (int k = 0; k < _sqlFunctions.length; k++) {
		_result = LibSQL.exFunction(_sqlFunctions[k], _sqlParams[k]);
		if (_result.next()) {
		    String _pregunta = "Pregunta " + _preguntas[k];
		    String _field = _fields[k];
		    //_queryResult.append("<b>Esta tabla representa porcentajes parciales</b><br>");
    
		    String[] _opciones = _opcionesVector.elementAt(k);
		    String[] _opcionesNombre = _opcionesNombreVector.elementAt(k);
		    if (_categoriasVector.elementAt(k).length > 0) {
			populateTable(_pregunta, _queryResult, _result, _field, _opciones, _opcionesNombre, _categoriasVector.elementAt(k), _chartsList);
		    } else {
			populateTable(_pregunta, _queryResult, _result, _field, _opciones, _opcionesNombre, _chartsList);
		    }
		}
	    }
	    _queryResult.append("</html>");
	} catch (SQLException f) {
	    f.printStackTrace();
	}

	repaint();

	BasicInternalFrame _multiQueryDialog = new BasicInternalFrame("Resultado de Consulta Múltiple - " 
		+ ((_idDepartamento!=-1)?"Departamento " + _departamento:"Todos los departamentos"));
	final BasicPanel _centerPanel = new BasicPanel(new BorderLayout());
	_multiQueryDialog.setLayout(new BorderLayout());
	_multiQueryDialog.setSize(600, 500);
	JArea _multiQuery = new JArea();
	_multiQuery.setContentType("text/html");
	_multiQuery.setEditable(false);
	BasicPanel _jpMultiQuery = new BasicPanel();
	_jpMultiQuery.setLayout(new BorderLayout());
	_jpMultiQuery.add(_multiQuery, BorderLayout.CENTER);

	final BasicPanel _graphicsTab = new BasicPanel(new BorderLayout());
	
	final CBInput _cbPanels = new CBInput(0, "Gráficas");
	_cbPanels.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			_centerPanel.removeAll();
			JFreeChart _chart = (JFreeChart)_chartsList.get(_cbPanels.getSelectedItem());
			_centerPanel.add(new ChartPanel(_chart, true, true, true, true, true), BorderLayout.CENTER);
			final BufferedImage _chartImage = _chart.createBufferedImage(800, 500);

			PrintButton _btnPrint = new PrintButton();
			_btnPrint.setText("Guardar imagen");
			_btnPrint.setToolTipText("Guardar imagen como...");
			_btnPrint.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionEvent) {
				    JFileChooser chooser = new JFileChooser(Environment.cfg.getProperty("ChartImage") + File.separator);
				    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				    int returnVal = chooser.showSaveDialog(chooser.getParent());
				    if (returnVal == JFileChooser.APPROVE_OPTION) {
					// IF File Selected
					try {
					    String path = chooser.getSelectedFile().getAbsolutePath();
					    if (!path.endsWith(".png")) {
						path += ".png";
					    }
					    Environment.cfg.setProperty("ChartImage", chooser.getSelectedFile().getParent());
					    File file = new File(path);
					    ImageIO.write(_chartImage, "png", file);
					    Advisor.messageBox("La imagen se grabó con éxito", "Grabar Imagen");
					} catch (IOException x) {
					    Advisor.messageBox("No se pudo grabar la imagen", "Error de E/S");
					    x.printStackTrace();
					}
				    }
				}

			    });

			_centerPanel.add(_btnPrint, BorderLayout.SOUTH);

			_graphicsTab.updateUI();
		    }
		}
	    });

	_graphicsTab.add(_cbPanels, BorderLayout.NORTH);
	_graphicsTab.add(_centerPanel, BorderLayout.CENTER);

	_multiQueryTabbedPane.insertTab("Tablas", null, _jpMultiQuery, null, 0);
	_multiQueryTabbedPane.insertTab("Gráficas", null, _graphicsTab, null, 1);

	SortedSet<String> sortedset= new TreeSet<String>(_chartsList.keySet());
	Iterator<String> it = sortedset.iterator();
	while (it.hasNext()) {
	    _cbPanels.addItem(it.next());
	}
	_cbPanels.getCombo().setSelectedIndex(-1);
	_cbPanels.getCombo().setSelectedIndex(0);

	_multiQueryDialog.add(_multiQueryTabbedPane, BorderLayout.CENTER);
	_multiQuery.setText(_queryResult.toString());
	_multiQueryDialog.setMaximizable(true);
	_multiQueryDialog.setClosable(true);
	_multiQueryDialog.setResizable(true);
	_multiQueryDialog.setVisible(true);
    }

    private void doReportRegional(int _idRegion, String _region) {
	BasicTabbedPane _multiQueryTabbedPane = new BasicTabbedPane();
	final HashMap _chartsList = new HashMap();
	StringBuilder _queryResult = new StringBuilder("<html>");

	String[] _sqlFunctions = new String[] {
	    "unsa.getallf1q1opciones_regional",
	    "unsa.getallf1q2opinion_regional",
	    "unsa.getallf1q3_regional",
	    "unsa.getallf1q4_regional",
	    "unsa.getallf1q6_regional",
	    "unsa.getallf1q8_regional",
	    "unsa.getallf1q8opinion_regional",
	    "unsa.getallf1q9opciones_regional",
	    "unsa.getallf1q10_regional",
	    "unsa.getallf1q12_regional",
	    "unsa.getallf1q13_regional"
	};

	//_sqlFunctions = new String[] { "unsa.getallf1q1opciones" };

	int[] _sqlParams = new int[] {
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion,
	    _idRegion
	};

	String[] _preguntas = new String[] {
	    "01 - \"1. (Opciones) " + coordinador.getEncuesta().getF1q1OpcionesLabel() + "\"",
	    "02 - \"2. (Opinión) " + coordinador.getEncuesta().getF1q2OpinionLabel() + "\"",
	    "03 - \"" + coordinador.getEncuesta().getF1q3Label() + "\"",
	    "04 - \"" + coordinador.getEncuesta().getF1q4Label() + "\"",
	    "06 - \"" + coordinador.getEncuesta().getF1q6Label() + "\"",
	    "08 - \"" + coordinador.getEncuesta().getF1q8Label() + "\"",
	    "08b - \"8. (Opinión) " + coordinador.getEncuesta().getF1q8OpinionLabel() + "\"",
	    "09 - \"" + coordinador.getEncuesta().getF1q9Label() + "\"",
	    "10 - \"" + coordinador.getEncuesta().getF1q10Label() + "\"",
	    "12 - \"" + coordinador.getEncuesta().getF1q12Label() + "\"",
	    "13 - \"" + coordinador.getEncuesta().getF1q13Label() + "\""
	};

	String[] _fields = new String[] {
	    "f1q1opciones",
	    "f1q2opinion",
	    "f1q3",
	    "f1q4",
	    "f1q6",
	    "f1q8",
	    "f1q8opinion",
	    "f1q9opciones",
	    "f1q10",
	    "f1q12",
	    "f1q13"
	};
	
	Vector<String[]> _opcionesVector = new Vector<String[]>();
	_opcionesVector.add(new String[] {"libros", "revistas", "articulos", "documentos", "internet", "formacion", "experiencia",
						   "interaccion", "vidadiaria" });
	_opcionesVector.add(new String[] {"pocos", "buenaparte", "mayoria", "casitodos", "error" });
	_opcionesVector.add(new String[] {"nada", "apenas", "mucho", "neces", "error"});
	_opcionesVector.add(new String[] {"nada", "apenas", "mucho", "neces", "error"});
	_opcionesVector.add(new String[] {"ninguna", "muyneg", "negat", "sininf", "posit", "muypos"});
	_opcionesVector.add(new String[] {"ninguna", "inutil", "insuf", "relev", "indisp"});
	_opcionesVector.add(new String[] {"pocos", "buenaparte", "mayoria", "casitodos", "error" });
	_opcionesVector.add(new String[] {"libros", "revistas", "articulos", "documentos", "internet", "formacion", "experiencia",
						   "interaccion", "vidadiaria" });
	_opcionesVector.add(new String[] {"si", "no", "noobservo", "error"});
	_opcionesVector.add(new String[] {"si", "no", "nsnc", "error"});
	_opcionesVector.add(new String[] {"ninguna", "totalins", "insat", "satisf", "plensat"});
	
	Vector<String[]> _opcionesNombreVector = new Vector<String[]>();
	_opcionesNombreVector.add(new String[] {"libros", "revistas (de divulgación)", "artículos (o eventos) científicos",
							 "documentos (oficiales)", "internet", "formación docente", "experiencia docente",
							 "interacción con colegas", "vida diaria" });
	_opcionesNombreVector.add(new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error" });
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q3Opciones());
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q4Opciones());
	_opcionesNombreVector.add(new String[] {"Ninguna", "Muy negativamente", "Negativamente", "Sin influencia", "Positivamente", "Muy positivamente" });
	_opcionesNombreVector.add(new String[] {"Ninguna", "Inútiles", "Insuficientes", "Relevantes", "Indispensables" });
	_opcionesNombreVector.add(new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error" });
	_opcionesNombreVector.add(new String[] {"libros", "revistas (de divulgación)", "artículos (o eventos) científicos",
							 "documentos (oficiales)", "internet", "formación docente", "experiencia docente",
							 "interacción con colegas", "vida diaria" });
	_opcionesNombreVector.add(new String[] {"Sí", "No", "En general no observo tales diferencias", "Error"});
	_opcionesNombreVector.add(coordinador.getEncuesta().getF1q12Opciones());
	_opcionesNombreVector.add(new String[] {"Ninguna", "Totalmente insatisfecho", "Insatisfecho", "Satisfecho", "Plenamente satisfecho"});

	Vector<String[]> _categoriasVector = new Vector<String[]>();
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {
		" Categoría: Didáctica - Subcategoría: Libro de texto",
		" Categoría: Didáctica - Subcategoría: Forma de las pruebas de evaluación",
		" Categoría: Didáctica - Subcategoría: Lo que los estudiantes parecen haber aprendido",
		" Categoría: Profesionales - Subcategoría: Contribuciones de la formación",
		" Categoría: Profesionales - Subcategoría: Dominio del tema",
		" Categoría: Factores socio-culturales - Subcategoría: Nivel socio-cultural de los estudiantes",
		" Categoría: Factores socio-culturales - Subcategoría: Constitución del grupo familiar de los estudiantes",
		" Categoría: Relaciones Interpersonales - Subcategoría: Interacción con los colegas",
		" Categoría: Relaciones Interpersonales - Subcategoría: Interacción con los directivos"
		});
	_categoriasVector.add(new String[] {
		" Categoría: Estructurales - Subcategoría: Cambiar las exigencias oficiales",
		" Categoría: Estructurales - Subcategoría: Aumentar los recursos (materiales y financieros) de las escuelas",
		" Categoría: Estructurales - Subcategoría: Construir laboratorios especiales",
		" Categoría: Estructurales - Subcategoría: Asegurar conexiones seguras a internet",
		" Categoría: Estructurales - Subcategoría: Cambiar los criterios para la selección de los docentes",
		" Categoría: Profesionales - Subcategoría: Cambiar la formación (inicial y contínua)",
		" Categoría: Profesionales - Subcategoría: Reorganizar el trabajo docente",
		" Categoría: Profesionales - Subcategoría: Involucrar agentes extra-escolares en la práctica educativa",
		" Categoría: Profesionales - Subcategoría: Intercambiar ideas con colegas acerca de las acciones exitosas o no en el aula",
		" Categoría: Profesionales - Subcategoría: Vincular la práctica docente con los resultados de la investigación educativa",
		" Categoría: Didácticas - Subcategoría: Desarrollar nuevos materiales didácticos",
		" Categoría: Didácticas - Subcategoría: Cambiar los criterios de evaluación del aprendizaje"
		});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {});
	_categoriasVector.add(new String[] {
		    " Desde el punto de vista intelectual",
		    " Desde el punto de vista personal",
		    " Desde el punto de vista social",
		    " Desde el punto de vista económico"
		});


	ResultSet _result;
	
	try {
	    for (int k = 0; k < _sqlFunctions.length; k++) {
		_result = LibSQL.exFunction(_sqlFunctions[k], _sqlParams[k]);
		if (_result.next()) {
		    String _pregunta = "Pregunta " + _preguntas[k];
		    String _field = _fields[k];
		    //_queryResult.append("<b>Esta tabla representa porcentajes parciales</b><br>");
    
		    String[] _opciones = _opcionesVector.elementAt(k);
		    String[] _opcionesNombre = _opcionesNombreVector.elementAt(k);
		    if (_categoriasVector.elementAt(k).length > 0) {
			populateTable(_pregunta, _queryResult, _result, _field, _opciones, _opcionesNombre, _categoriasVector.elementAt(k), _chartsList);
		    } else {
			populateTable(_pregunta, _queryResult, _result, _field, _opciones, _opcionesNombre, _chartsList);
		    }
		}
	    }
	    _queryResult.append("</html>");
	} catch (SQLException f) {
	    f.printStackTrace();
	}

	repaint();

	BasicInternalFrame _multiQueryDialog = new BasicInternalFrame("Resultado de Consulta Múltiple - " 
		+ ((_idRegion!=-1)?"Región: " + _region:"Todas las regiones"));
	final BasicPanel _centerPanel = new BasicPanel(new BorderLayout());
	_multiQueryDialog.setLayout(new BorderLayout());
	_multiQueryDialog.setSize(600, 500);
	JArea _multiQuery = new JArea();
	_multiQuery.setContentType("text/html");
	_multiQuery.setEditable(false);
	BasicPanel _jpMultiQuery = new BasicPanel();
	_jpMultiQuery.setLayout(new BorderLayout());
	_jpMultiQuery.add(_multiQuery, BorderLayout.CENTER);

	final BasicPanel _graphicsTab = new BasicPanel(new BorderLayout());
	
	final CBInput _cbPanels = new CBInput(0, "Gráficas");
	_cbPanels.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent evt) {
		    if (evt.getStateChange() == ItemEvent.SELECTED) {
			_centerPanel.removeAll();
			JFreeChart _chart = (JFreeChart)_chartsList.get(_cbPanels.getSelectedItem());
			_centerPanel.add(new ChartPanel(_chart, true, true, true, true, true), BorderLayout.CENTER);
			final BufferedImage _chartImage = _chart.createBufferedImage(800, 500);

			PrintButton _btnPrint = new PrintButton();
			_btnPrint.setText("Guardar imagen");
			_btnPrint.setToolTipText("Guardar imagen como...");
			_btnPrint.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent actionEvent) {
				    JFileChooser chooser = new JFileChooser(Environment.cfg.getProperty("ChartImage") + File.separator);
				    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				    int returnVal = chooser.showSaveDialog(chooser.getParent());
				    if (returnVal == JFileChooser.APPROVE_OPTION) {
					// IF File Selected
					try {
					    String path = chooser.getSelectedFile().getAbsolutePath();
					    if (!path.endsWith(".png")) {
						path += ".png";
					    }
					    Environment.cfg.setProperty("ChartImage", chooser.getSelectedFile().getParent());
					    File file = new File(path);
					    ImageIO.write(_chartImage, "png", file);
					    Advisor.messageBox("La imagen se grabó con éxito", "Grabar Imagen");
					} catch (IOException x) {
					    Advisor.messageBox("No se pudo grabar la imagen", "Error de E/S");
					    x.printStackTrace();
					}
				    }
				}

			    });

			_centerPanel.add(_btnPrint, BorderLayout.SOUTH);

			_graphicsTab.updateUI();
		    }
		}
	    });

	_graphicsTab.add(_cbPanels, BorderLayout.NORTH);
	_graphicsTab.add(_centerPanel, BorderLayout.CENTER);

	_multiQueryTabbedPane.insertTab("Tablas", null, _jpMultiQuery, null, 0);
	_multiQueryTabbedPane.insertTab("Gráficas", null, _graphicsTab, null, 1);

	SortedSet<String> sortedset= new TreeSet<String>(_chartsList.keySet());
	Iterator<String> it = sortedset.iterator();
	while (it.hasNext()) {
	    _cbPanels.addItem(it.next());
	}
	_cbPanels.getCombo().setSelectedIndex(-1);
	_cbPanels.getCombo().setSelectedIndex(0);

	_multiQueryDialog.add(_multiQueryTabbedPane, BorderLayout.CENTER);
	_multiQuery.setText(_queryResult.toString());
	_multiQueryDialog.setMaximizable(true);
	_multiQueryDialog.setClosable(true);
	_multiQueryDialog.setResizable(true);
	_multiQueryDialog.setVisible(true);
    }


    private JFreeChart[] addCharts(String _pregunta, HashMap _chartsList, DefaultPieDataset _pieDataset, DefaultCategoryDataset _barDataset, String _titleString) {
	JFreeChart[] _charts = new JFreeChart[2];
	TextTitle _title = new TextTitle(_titleString, new Font("Arial",Font.BOLD,12)) ;
	if (_pieDataset.getItemCount() > 0) {
	    JFreeChart _pieChart = ChartFactory.createPieChart3D("", _pieDataset, true, true, true);
	    _pieChart.createBufferedImage(100,100);
	    _pieChart.setTitle(_title);
	    _chartsList.put(_pregunta + " (Torta)", _pieChart);
	    _charts[0] = _pieChart;
	}
	if (_barDataset.getRowCount() > 0) {
	    JFreeChart _barChart = ChartFactory.createBarChart("", "", "Cantidad", _barDataset, PlotOrientation.VERTICAL, true, true, true);
	    _barChart.setTitle(_title);
	    _barChart.createBufferedImage(100,100);
	    _chartsList.put(_pregunta + " (Barras)", _barChart);
	    _charts[1] = _barChart;
	}
	return _charts;
    }

    private void startTable(StringBuilder _queryResult) {
	_queryResult.append("<table border=1 align=center>");
	_queryResult.append("<tr><th>Opción</th><th>\u03A3</th><th>% \u03A3</th></t>");
    }

    private void endTable(StringBuilder _queryResult, int _total) {
	_queryResult.append("Total encuestas: " + _total + "<br>");
	_queryResult.append("</table></p><br>");
    }

    private void populateTable(String _pregunta, StringBuilder _queryResult, ResultSet _result, String _field, String[] _opciones, String[] _opcionesNombre, HashMap _chartsList) throws SQLException {
	DefaultPieDataset _pieDataset = new DefaultPieDataset();
	DefaultCategoryDataset _barDataset = new DefaultCategoryDataset();
	_queryResult.append("<br><p align=center>Tabla de <i>" + _pregunta + "</i><br>");
	for (int j = 0; j < _opciones.length; j++) {
	    _barDataset.setValue(_result.getInt(_field + "_" + _opciones[j]), _opcionesNombre[j] + " [" + _result.getInt(_field + "_" + _opciones[j]) + "] ", "");
	    _pieDataset.setValue(_opcionesNombre[j] + " [" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + _opciones[j] + "porc") * 100.0) + "%] ", _result.getInt(_field + "_" + _opciones[j]));
	}
	JFreeChart[] _charts = addCharts(_pregunta, _chartsList, _pieDataset, _barDataset, _pregunta);

	startTable(_queryResult);
	for (int j = 0; j < _opciones.length; j++) {
	    _queryResult.append("<tr>");
	    String _color = (_charts[0] != null)?Format.color2Hex((Color)((PiePlot)_charts[0].getPlot()).getSectionPaint(_opcionesNombre[j] + " [" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + _opciones[j] + "porc") * 100.0) + "%] ")):"ffffff";
	    _queryResult.append("<td bgcolor=#" + _color + "><b>" + _opcionesNombre[j] + "</b></font></td>");
	    _queryResult.append("<td align=right>" + _result.getInt(_field + "_" + _opciones[j]) + "</td>");
	    _queryResult.append("<td align=right>" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + _opciones[j] + "porc") * 100.0) + " % </td>");
	    _queryResult.append("</tr>");
	}
	endTable(_queryResult, _result.getInt("total"));
    }

    private void populateTable(String _pregunta, StringBuilder _queryResult, ResultSet _result, String _field, String[] _opciones, String[] _opcionesNombre, String[] _categorias, HashMap _chartsList) throws SQLException {
	for (int i = 1; i <= _categorias.length; i++) {
	    DefaultPieDataset _pieDataset = new DefaultPieDataset();
	    DefaultCategoryDataset _barDataset = new DefaultCategoryDataset();
	    _queryResult.append("<br><p align=center>Tabla de <i>" + _pregunta + "</i><br>");
	    _queryResult.append(_categorias[i-1] + "<br>");
	    for (int j = 0; j < _opciones.length; j++) {
		_barDataset.setValue(_result.getInt(_field + "_" + i + "_" + _opciones[j]), _opcionesNombre[j], "");
		_pieDataset.setValue(_opcionesNombre[j] + " [" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + i + "_" + _opciones[j] + "porc") * 100.0) + "%] ", _result.getInt(_field + "_" + i + "_" + _opciones[j]));
	    }
	    JFreeChart[] _charts = addCharts(_pregunta + "\n" + _categorias[i-1], _chartsList, _pieDataset, _barDataset, _pregunta + "\n" + _categorias[i-1]);

	    startTable(_queryResult);
	    for (int j = 0; j < _opciones.length; j++) {
	        _queryResult.append("<tr>");
	        String _color = (_charts[0] != null)?Format.color2Hex((Color)((PiePlot)_charts[0].getPlot()).getSectionPaint(_opcionesNombre[j] + " [" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + i + "_" + _opciones[j] + "porc") * 100.0) + "%] ")):"ffffff";
	        _queryResult.append("<td bgcolor=#" + _color + "><b>" + _opcionesNombre[j] + "</b></font></td>");
	        _queryResult.append("<td align=right>" + _result.getInt(_field + "_" + i + "_" + _opciones[j]) + "</td>");
	        _queryResult.append("<td align=right>" + Format.moneyWithOutSign(_result.getDouble(_field + "_" + i + "_" + _opciones[j] + "porc") * 100.0) + " % </td>");
	        _queryResult.append("</tr>");
	    }
	    endTable(_queryResult, _result.getInt("total"));
	}
    }

}
