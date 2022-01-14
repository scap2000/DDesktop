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
 * JournalList.java
 *
 * */
package org.digitall.apps.cashflow.interfaces.accounting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.sql.Types;

import java.util.Vector;

import javax.swing.JFileChooser;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import org.digitall.common.cashflow.classes.Account;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.BorderPanel;
import org.digitall.lib.components.JArea;
import org.digitall.lib.components.basic.BasicInternalFrame;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.PrintButton;
import org.digitall.lib.components.buttons.PrintSaveButton;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.html.BrowserLauncher;
import org.digitall.lib.sql.LibSQL;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class JournalList extends BasicPrimitivePanel{

    private BasicPanel findPanel = new BasicPanel();
    private TFInput tfStartDate = new TFInput(DataTypes.SIMPLEDATE,"From",false);
    private TFInput tfEndDate = new TFInput(DataTypes.SIMPLEDATE,"To",false);
    private TFInput tfFindAccount1 = new TFInput(DataTypes.STRING,"FindFromAccounting",false);
    private CBInput cbStartAccount = new CBInput(0,"Accounting",false);
    private PrintButton btnPrint = new PrintButton();
    private PrintButton btnReport = new PrintButton();
    private PrintSaveButton btnFullReport = new PrintSaveButton();
    private CBInput cbEndAccount = new CBInput(0,"Accounting",false);
    private TFInput tfFindAccount2 = new TFInput(0,"FindEvenAccounting",false);
    private DeleteButton btnClear1 = new DeleteButton();
    private DeleteButton btnClear2 = new DeleteButton();

    public JournalList() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(537, 228));
	findPanel.setLayout(null);
	findPanel.setPreferredSize(new Dimension(1, 115));
	findPanel.setSize(new Dimension(739, 70));
	tfStartDate.setBounds(new Rectangle(15, 40, 95, 35));
	tfEndDate.setBounds(new Rectangle(170, 40, 95, 35));
	tfFindAccount1.setBounds(new Rectangle(15, 90, 140, 35));
	cbStartAccount.setBounds(new Rectangle(170, 90, 320, 35));
	btnPrint.setBounds(new Rectangle(235, 195, 28, 33));
	btnPrint.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     btnPrint_actionPerformed(e);
					 }

				     }
	);
	cbEndAccount.setBounds(new Rectangle(170, 130, 320, 35));
	tfFindAccount2.setBounds(new Rectangle(15, 130, 140, 35));
	btnClear1.setBounds(new Rectangle(500, 105, 20, 20));
	btnClear1.setSize(new Dimension(20, 20));
	btnClear1.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnClear1_actionPerformed(e);
				 }

			     }
	);
	btnClear2.setBounds(new Rectangle(500, 145, 20, 20));
	btnClear2.addActionListener(new ActionListener() {

				 public void actionPerformed(ActionEvent e) {
				     btnClear2_actionPerformed(e);
				 }

			     }
	);
	addButton(btnPrint);
	findPanel.add(btnClear2, null);
	findPanel.add(btnClear1, null);
	findPanel.add(tfFindAccount2, null);
	findPanel.add(cbEndAccount, null);
	findPanel.add(cbStartAccount, null);
	findPanel.add(tfFindAccount1, null);
	findPanel.add(tfEndDate, null);
	findPanel.add(tfStartDate, null);
	this.add(findPanel, BorderLayout.CENTER);
	cbStartAccount.autoSize();
	cbEndAccount.autoSize();
	
	tfFindAccount1.getTextField().addKeyListener(new KeyAdapter() {
		 public void keyTyped(KeyEvent e) {
		     if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
			 loadComboAccount(tfFindAccount1.getString(), cbStartAccount);
		     }
		 }

	     }
	);
	
	tfFindAccount2.getTextField().addKeyListener(new KeyAdapter() {
		 public void keyTyped(KeyEvent e) {
		     if ((e.getKeyChar() == KeyEvent.VK_ENTER)) {
			 loadComboAccount(tfFindAccount2.getString(), cbEndAccount);
		     }
		 }

	     }
	);
	
	tfStartDate.setValue(Proced.setFormatDate(Environment.currentYear +"-01-01", true));
	tfEndDate.setValue(Proced.setFormatDate(Environment.currentDate, true));
	btnClear1.setToolTipText("Limpiar Cuenta \"Desde\"");
	btnClear2.setToolTipText("Limpiar Cuenta \"Hasta\"");
	btnPrint.setToolTipText("Imprimir Libro Mayor");
        btnReport.setToolTipText("Imprimir Gráfico de cuenta(s) contable(s)");
        btnReport.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		    btnReport_actionPerformed(e);
		}
	    });
        btnFullReport.setToolTipText("Imprimir Reporte completo entre las fechas seleccionadas");
        btnFullReport.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    btnFullReport_actionPerformed(e);
                }
            });
	findPanel.setBorder(BorderPanel.getBorderPanel("Generar libro mayor"));
        addButton(btnReport);
        addButton(btnFullReport);
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
	super.setParentInternalFrame(_e);
	getParentInternalFrame().setInfo("Puede generar el Libro para una sola Cuenta o para un rango de Cuentas");
    }
    
    private void loadComboAccount(String _text, CBInput _combo){
	String param = "-1,'" + _text + "'";
	_combo.loadJCombo(LibSQL.exFunction("accounting.getAllAccountsToJournal", param)); 
    }
    
    private Account getAccount(CBInput _combo){
	Account account = new Account();
	if (!_combo.getSelectedValue().equals("-1")){
	    account.setIDAccount(Integer.parseInt(""+ _combo.getSelectedValue()));
	    account.setCode(Integer.parseInt(""+ _combo.getSelectedValueRef()));	
	} else {
	    account.setCode(0); 
	}
	
	return account;
    }
    
    private void btnPrint_actionPerformed(ActionEvent e) {
	if (tfStartDate.getDate().length()>0 && tfEndDate.getDate().length()>0){
	    if (Proced.compareDates(Proced.setFormatDate(tfStartDate.getDate(),false),Proced.setFormatDate(tfEndDate.getDate(),false)) == 2) {
	        Advisor.messageBox("La fecha desde no puede ser mayor a la fecha hasta","Error");
	    }else{     
		if (!cbStartAccount.getSelectedValue().equals("-1")){
		    BasicReport report = new BasicReport(JournalList.class.getResource("xml/journal.xml"));
		    String params = getAccount(cbStartAccount).getCode() +","+ getAccount(cbEndAccount).getCode() +",'"+ Proced.setFormatDate(tfStartDate.getDate(), false) +"','"+ Proced.setFormatDate(tfEndDate.getDate(), false) +"', 0, 0";
		    report.setProperty("startdate", tfStartDate.getDate());
		    report.setProperty("enddate", tfEndDate.getDate());
                    report.setProperty("accountname", cbStartAccount.getSelectedItem());
		    report.setProperty("accountcode", cbStartAccount.getSelectedValueRef());
		    report.addTableModel("accounting.getJournal", params);
		    report.doReport();
		} else{
		    Advisor.messageBox("Cuenta(s) requerida(s)","Aviso");
		}
	    }
	    
	} else{
	    Advisor.messageBox("Rango de fechas requerido","Aviso");
	}
    }

    private void btnClear1_actionPerformed(ActionEvent e) {
	cbStartAccount.removeAllItems();
    }

    private void btnClear2_actionPerformed(ActionEvent e) {
	cbEndAccount.removeAllItems();
    }

    private void btnFullReport_actionPerformed(ActionEvent e) {
        if (tfStartDate.getDate().length()>0 && tfEndDate.getDate().length()>0) {
            if (Proced.compareDates(Proced.setFormatDate(tfStartDate.getDate(),false),Proced.setFormatDate(tfEndDate.getDate(),false)) == 2) {
                Advisor.messageBox("La fecha desde no puede ser mayor a la fecha hasta","Error");
            } else {     
                exportDataToExcel();
            }
        }
    }
    
    private String getColName(int num) {
            Vector v = new Vector();
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for (int i = 0; i < alphabet.length(); i++) {
                    v.add(alphabet.charAt(i));
            }
            for (int i = 0; i < alphabet.length(); i++) {
                    for (int j = 0; j < alphabet.length(); j++) {
                            v.add(alphabet.charAt(i) + alphabet.charAt(j));
                    }
            }
            return(v.elementAt(num).toString());
    }

    private void exportDataToExcel() {
        JFileChooser chooser = new JFileChooser(Environment.cfg.getProperty("GridPanelXLSExport") + File.separator);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showSaveDialog(chooser.getParent());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // IF File Selected
            try {
                String path = chooser.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".xls")) {
                    path += ".xls";
                }
                Environment.cfg.setProperty("GridPanelXLSExport", chooser.getSelectedFile().getParent());
                File file = new File(path);
                FileOutputStream fileOut = new FileOutputStream(file);
                ResultSet result;
                ResultSetMetaData resultMD;
                int rowNumber = 0;
                
                Environment.jpStatusBar.setAction("Preparando exportación, obteniendo datos");
                
                String params = getAccount(cbStartAccount).getCode() +","+ getAccount(cbEndAccount).getCode() +",'"+ Proced.setFormatDate(tfStartDate.getDate(), false) +"','"+ Proced.setFormatDate(tfEndDate.getDate(), false) +"'";
                result = LibSQL.exFunction("accounting.getJournal", params, 0, 0);
                resultMD = result.getMetaData();
                /* Obtiene la META-Informacion de la tabla que devuelve la consulta "SQLString" */
                HSSFWorkbook workBook = new HSSFWorkbook();
                String _sheetTitle = "Mayor general";
                if (_sheetTitle.length() == 0) {
                    _sheetTitle = "Hoja 1";
                } else if (_sheetTitle.length()>31) {
                    _sheetTitle = _sheetTitle.substring(0,31);
                }
                _sheetTitle = _sheetTitle.replaceAll("/", "_").replaceAll("\\\\", "_").replaceAll("\\[", "_").replaceAll("\\]", "_").replaceAll("\\*", "_").replaceAll("\\?", "_");
                HSSFSheet sheet = workBook.createSheet(_sheetTitle);
                HSSFRow headerRow = sheet.createRow(rowNumber);
                HSSFFont headerFont = workBook.createFont();
                headerFont.setFontHeightInPoints((short)11);
                headerFont.setFontName(headerFont.FONT_ARIAL);
                headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                HSSFCellStyle headerStyle = workBook.createCellStyle();
                headerStyle.setWrapText(true);
                headerStyle.setAlignment(HSSFCellStyle. ALIGN_CENTER);
                headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                headerStyle.setBorderBottom((short)1);
                headerStyle.setBorderTop((short)1);
                headerStyle.setBorderLeft((short)1);
                headerStyle.setBorderRight((short)1);
                headerStyle.setFont(headerFont);
                //for (int i = 1; i <= resultMD.getColumnCount(); ++i) {
                
                Vector<String> headerTable = new Vector<String>();
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                headerTable.addElement(Environment.lang.getProperty("Fecha"));
                headerTable.addElement(Environment.lang.getProperty("Concepto"));
                headerTable.addElement(Environment.lang.getProperty("Nº. Comp."));
                headerTable.addElement(Environment.lang.getProperty("$ Debe"));
                headerTable.addElement(Environment.lang.getProperty("$ Haber"));
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement("*");
                    headerTable.addElement(Environment.lang.getProperty("S. Deudor"));
                    headerTable.addElement(Environment.lang.getProperty("S. Acreedor"));
                    headerTable.addElement(Environment.lang.getProperty("Cuenta"));

                for (int i = 1; i <= headerTable.size(); ++i) {
                    HSSFCell celda = headerRow.createCell(i-1);
                    celda.setCellStyle(headerStyle);
                    //celda.setCellValue(new HSSFRichTextString(resultMD.getColumnName(i)));
                    if (headerTable.elementAt(i-1).toString().startsWith("*")) {
                        celda.setCellValue(new HSSFRichTextString(headerTable.elementAt(i-1).toString().replaceFirst("\\*", "").trim()));
                        sheet.setColumnHidden(i-1, true);
                    } else {
                        celda.setCellValue(new HSSFRichTextString(headerTable.elementAt(i-1).toString()));
                    }
                }
                sheet.setAutoFilter(new CellRangeAddress(0, 1, 0, headerTable.size()-1));
                rowNumber++;
                HSSFCellStyle dateStyle = workBook.createCellStyle();
                dateStyle.setDataFormat(workBook.createDataFormat().getFormat("dd/MM/yyyy"));
                HSSFCellStyle timeStampStyle = workBook.createCellStyle();
                timeStampStyle.setDataFormat(workBook.createDataFormat().getFormat("dd/MM/yyyy HH:mm:ss"));
                HSSFCellStyle timeStyle = workBook.createCellStyle();
                timeStyle.setDataFormat(workBook.createDataFormat().getFormat("HH:mm:ss"));
                HSSFCellStyle moneyStyle = workBook.createCellStyle();
                moneyStyle.setDataFormat(workBook.createDataFormat().getFormat("$ * #,##0.00"));
                moneyStyle.setAlignment(HSSFCellStyle. ALIGN_LEFT);

                Environment.jpStatusBar.setAction("Exportando datos, por favor espere...");

                while (result.next()) {
                    HSSFRow fila = sheet.createRow(rowNumber);
                    for (int i = 1; i <= resultMD.getColumnCount(); ++i) {
                        HSSFCell celda = fila.createCell(i-1);
                        try {
                            switch (resultMD.getColumnType(i)) {
                                case -7 :
                                    celda.setCellValue(result.getBoolean(i));
                                    break;
                                case Types.DATE :
                                    celda.setCellValue(result.getDate(i));
                                    celda.setCellStyle(dateStyle);
                                    break;
                                case Types.TIMESTAMP :
                                    celda.setCellValue(new HSSFRichTextString(result.getString(i)));
                                    celda.setCellStyle(timeStampStyle);
                                    break;
                                case Types.TIME :
                                    celda.setCellValue(result.getTime(i));
                                    celda.setCellStyle(timeStyle);
                                    break;
                                case Types.INTEGER :
                                    celda.setCellValue(result.getInt(i));
                                    break;
                                case Types.BIGINT :
                                    celda.setCellValue(result.getLong(i));
                                    break;
                                case Types.DOUBLE :
                                case Types.NUMERIC :
                                case Types.DECIMAL :
                                    celda.setCellValue(result.getDouble(i));
                                    if (headerTable.elementAt(i-1).toString().startsWith("$") || headerTable.elementAt(i-1).toString().startsWith("($)")) {
                                        celda.setCellStyle(moneyStyle);
                                    }
                                    break;
                                case Types.VARCHAR :
                                case Types.CHAR :
                                case Types.LONGVARCHAR :
                                default :
                                    celda.setCellValue(new HSSFRichTextString(result.getString(i)));
                                    break;
                            }
                        } catch (Exception x) {
                            celda.setCellValue(new HSSFRichTextString(result.getString(i)));
                            break;
                        }
                    }
                    rowNumber++;
                }
                HSSFRow fila = sheet.createRow(rowNumber);
                for (int i = 1; i <= headerTable.size(); ++i) {
                    //celda.setCellValue(new HSSFRichTextString(resultMD.getColumnName(i)));
                    if (headerTable.elementAt(i-1).toString().startsWith("$") || headerTable.elementAt(i-1).toString().startsWith("($)")) {
                        HSSFCell celda = fila.createCell(i-1);
                        HSSFCellStyle totalStyle = workBook.createCellStyle();
                        totalStyle.setWrapText(true);
                        totalStyle.setAlignment(HSSFCellStyle. ALIGN_LEFT);
                        totalStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                        totalStyle.setFont(headerFont);
                        totalStyle.setBorderBottom((short)1);
                        totalStyle.setBorderTop((short)1);
                        totalStyle.setBorderLeft((short)1);
                        totalStyle.setBorderRight((short)1);
                        totalStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
                        totalStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                        totalStyle.setDataFormat(workBook.createDataFormat().getFormat("$ * #,##0.00"));
                        celda.setCellStyle(totalStyle);
                        celda.setCellFormula("SUM(" + getColName(i-1) + "2:" + getColName(i-1) + (rowNumber) + ")");
                    }
                }
                
                Environment.jpStatusBar.setAction("Formateando archivo, casi terminado...");

                try {
                    for (int i = 1; i <= resultMD.getColumnCount(); ++i) {
                        sheet.autoSizeColumn(i-1);
                    }
                } catch (ArrayIndexOutOfBoundsException x) {
                    //ignore
                    x.printStackTrace();
                }
                sheet.createFreezePane(0, 1);
                workBook.write(fileOut);
                fileOut.close();

                Environment.jpStatusBar.setAction("Archivo exportado, lanzando visor...");

                Advisor.messageBox("Los datos se han exportado correctamente", "Exportar datos");
                BrowserLauncher.openURL(file.getAbsolutePath());
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                Advisor.messageBox("No se pudo guardar el archivo", "Error de E/S");
                Advisor.printException(e);
            } catch (SQLException e) {
                Advisor.printException(e);
            } catch (Exception e) {
                e.printStackTrace();
                Advisor.printException(e);
            }
        }
    }

    
    private void btnReport_actionPerformed(ActionEvent e) {
	if (tfStartDate.getDate().length()>0 && tfEndDate.getDate().length()>0) {
	    if (Proced.compareDates(Proced.setFormatDate(tfStartDate.getDate(),false),Proced.setFormatDate(tfEndDate.getDate(),false)) == 2) {
		Advisor.messageBox("La fecha desde no puede ser mayor a la fecha hasta","Error");
	    }else {     
		if (cbStartAccount.getSelectedIndex() != -1) {
		    ExtendedInternalFrame _multiQueryDialog = new ExtendedInternalFrame("Gráfico de cuenta(s) contable(s)");
		    _multiQueryDialog.setSize(400, 300);
		    JArea _multiQuery = new JArea();
		    _multiQuery.setContentType("text/html");
		    _multiQuery.setEditable(false);

		    String _params = getAccount(cbStartAccount).getCode() +",'"+ Proced.setFormatDate(tfStartDate.getDate(), false) +"','"+ Proced.setFormatDate(tfEndDate.getDate(), false) +"', 0, 0";
		    ResultSet _results = LibSQL.exFunction("accounting.getMonthlyJournal", _params);

		    TimeSeries _serie1 = new TimeSeries(cbStartAccount.getSelectedItem().toString(), Month.class);

		    try {
			while (_results.next()) {
			    _serie1.add(new Month(_results.getInt("month"), _results.getInt("year")), _results.getInt("monto"));
			}
		    } catch (SQLException x) {
			x.printStackTrace();
		    }

		    TimeSeriesCollection _dataset = new TimeSeriesCollection();
		    _dataset.addSeries(_serie1);

		    if (cbEndAccount.getSelectedIndex() != -1) {
			TimeSeries _serie2 = new TimeSeries(cbEndAccount.getSelectedItem().toString(), Month.class);
			_params = getAccount(cbEndAccount).getCode() +",'"+ Proced.setFormatDate(tfStartDate.getDate(), false) +"','"+ Proced.setFormatDate(tfEndDate.getDate(), false) +"', 0, 0";
			_results = LibSQL.exFunction("accounting.getMonthlyJournal", _params);
    
			try {
			    while (_results.next()) {
				_serie2.add(new Month(_results.getInt("month"), _results.getInt("year")), _results.getInt("monto"));
			    }
			} catch (SQLException x) {
			    x.printStackTrace();
			}
		        _dataset.addSeries(_serie2);
		    }

		    JFreeChart _timeSeriesChart = ChartFactory.createTimeSeriesChart("Gráfico de cuenta(s) contable(s)", "Mes", "Monto", _dataset, true, true, false);

		    ChartPanel _chartPanel = new ChartPanel(_timeSeriesChart);

		    XYPlot plot = (XYPlot) _timeSeriesChart.getPlot();
		    plot.setDomainGridlinePaint(Color.black);
		    //plot.setRangeGridlinePaint(Color.black);
		    plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		    plot.setDomainCrosshairVisible(true);
		    plot.setRangeCrosshairVisible(true);

		    XYItemRenderer r = plot.getRenderer();
		    if (r instanceof XYLineAndShapeRenderer) {
		        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
		        renderer.setShapesVisible(true);
		        renderer.setShapesFilled(true);
		    }
		    
		    /*Cuentas más utilizadas
		     * SELECT count(bookkeepingentrydetail.idaccount), name
			    FROM accounting.bookkeepingentrydetail, accounting.accounts
			    WHERE bookkeepingentrydetail.idaccount = accounts.idaccount
			    GROUP BY name
			    ORDER BY count(bookkeepingentrydetail.idaccount) DESC;
		     */

		    _multiQueryDialog.setCentralPanel(_chartPanel);
		    
		    _multiQueryDialog.setMaximizable(true);
		    _multiQueryDialog.setClosable(true);
		    _multiQueryDialog.setResizable(true);
		    _multiQueryDialog.setVisible(true);

		} else{
		    Advisor.messageBox("Cuenta(s) requerida(s)","Aviso");
		}
	    }
	    
	} else{
	    Advisor.messageBox("Rango de fechas requerido","Aviso");
	}
    }
}
