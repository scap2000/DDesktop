package org.digitall.apps.drilling.reports;

import java.awt.Color;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.JFileChooser;

import org.digitall.lib.common.ConfigFile;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.data.Format;

//

public class XMLProjectReport {

    private FileWriter xmlFile;
    private BufferedWriter log;
    private String EncabezadoReporte = "";
    private String xmlPath = "";
    private String proyecto = "", idproject = "", Pozos = "";
    private int columnas, cantPozos, columnasLithology, columnasStructure;
    private ResultSet minPermitidas;
    private ResultSet altPermitidas;
    private Vector mineralizaciones = new Vector();
    private Vector alteraciones = new Vector();
    private int cantMin = 0, cantFilas;
    private int cantAlt = 0;
    private int columnasMinAlt;
    private String seccion[];
    private ConfigFile cfg = new ConfigFile("ddesktop.conf");

    public XMLProjectReport(String _idproject) {
	/************* SETEO DE LAS VARIABLES QUE SE NECESITAN ************/
	idproject = _idproject;
	buscaProyecto();
	Pozos = org.digitall.lib.sql.LibSQL.getCampo("SELECT count(*) FROM drilling.drills WHERE idproject = " + idproject + " AND estado <> '*' ");
	cantPozos = Integer.parseInt(Pozos);
	cantFilas = cantPozos + 2;
	columnas = 6;
	columnasLithology = 4;
	columnasStructure = 5;
	setearResultSet();
	cantMin = mineralizaciones.size();
	cantAlt = alteraciones.size();
	//************
	columnasMinAlt = cantMin + cantAlt;
	seccion = new String[] { "drilling.mineralization_depth_interval", "drilling.alteration_depth_interval", "drilling.samples" };
	/*******************************************************************/
	// Abro el Archivo
	//xmlPath = Proced.getRuta() + File.separator + "informes" + File.separator + proyecto +".xml";
	xmlPath = cfg.getProperty("TemplatesPath") + File.separator;
	/*******************************************************************/
	// Abro el Archivo
	//xmlPath = Proced.getRuta() + File.separator + "informes" + File.separator + proyecto +".xml";
	xmlPath = cfg.getProperty("TemplatesPath") + File.separator;
	if (AbreArchivo(xmlPath)) {
	    write(Seccion1());
	    write(Seccion2());
	    write(Seccion3());
	    writeEncabezadoPie();
	    write(Cuerpo());
	    write(FinCuerpo());
	    write(FinSeccion1());
	    if (CierraArchivo()) {
		//System.out.println("Archivo XML generado con éxito");
		//        Advisor.messageBoxPopupWindow("<html>Reporte generado con ï¿½xito en<br><a href=file:///" + xmlPath + ">" + xmlPath + "</a></html>");
		//        Advisor.messageBoxPopupWindow("Reporte generado con ï¿½xito");
		Advisor.messagePopupWindow("<html><p align=center>Reporte generado con ï¿½xito<br><a href=>Click aquï¿½ para verlo</a></p></html>", xmlPath);
	    }
	}
    }

    private void buscaProyecto() {
	String consulta = "SELECT name FROM drilling.projects WHERE idproject = " + idproject;
	proyecto = org.digitall.lib.sql.LibSQL.getCampo(consulta);
    }

    private boolean AbreArchivo(String _path) {
	JFileChooser chooser = new JFileChooser(_path);
	chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	int returnVal = chooser.showSaveDialog(chooser.getParent());
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    // IF File Selected
	    try {
		String path = chooser.getSelectedFile().getAbsolutePath();
		if (!path.endsWith(".xls")) {
		    path += ".xls";
		}
		cfg.setProperty("TemplatesPath", chooser.getSelectedFile().getParent());
		xmlFile = new FileWriter(path, false);
		log = new BufferedWriter(xmlFile);
		return true;
	    } catch (IOException x) {
		org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error XMLWorkBook");
		x.printStackTrace();
		return false;
	    }
	} else {
	    return false;
	}
    }

    private boolean CierraArchivo() {
	try {
	    xmlFile.close();
	    return true;
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error XMLProjectReport");
	    x.printStackTrace();
	    return false;
	}
    }

    private void write(String _cadena) {
	try {
	    xmlFile.write(_cadena);
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error");
	    x.printStackTrace();
	}
    }

    private void writeln(String _cadena) {
	try {
	    xmlFile.write(_cadena + "\n");
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error");
	    x.printStackTrace();
	}
    }

    private void writeln() {
	try {
	    xmlFile.write("<w:p/>\n");
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error");
	    x.printStackTrace();
	}
    }

    private String Seccion1() {
	// Seccion 1: Tipo de Documento y Esquemas
	String seccion1 = "<?xml version=\"1.0\"?>\n" + 
			    "<?mso-application progid=\"Excel.Sheet\"?>\n" + 
			    "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" + 
			    " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" + 
			    " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n" + 
			    " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" + 
			    " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n";
	return seccion1;
    }

    private String FinSeccion1() {
	String finseccion1 = "</Workbook>\n";
	return finseccion1;
    }

    private String Seccion2() {
	// Seccion 2: Propiedades
	String seccion2 = " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n" + "  <Author>DIGITALL</Author>\n" + "  <LastAuthor>DIGITALL</LastAuthor>\n" + "  <Created>2007-05-07T08:12:27Z</Created>\n" + "  <Company>SERMAQ OS - Digitall</Company>\n" + "  <Version>11.6360</Version>\n" + " </DocumentProperties>\n" + " <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "  <WindowHeight>8700</WindowHeight>\n" + "  <WindowWidth>11595</WindowWidth>\n" + "  <WindowTopX>360</WindowTopX>\n" + "  <WindowTopY>120</WindowTopY>\n" + "  <ProtectStructure>False</ProtectStructure>\n" + "  <ProtectWindows>False</ProtectWindows>\n" + " </ExcelWorkbook>\n";
	return seccion2;
    }

    private String Seccion3() {
	// Seccion 3: Fonts y Styles
	String seccion3 =
	    //        Letra alineada arriba a la izquirda con Borde: Derecho
	    //        Letra alineada arriba a la izquirda con Borde: Izquierdo y superior
	    //Solo letra alineada arriba a la izquierda
	    //        Letra alineada arriba a la izquirda con Borde: Izquierdo
	    //        Letra alineada arriba a la izquirda con Borde: Superior
	    //        Solo letra alineada arriba al centro
	    //        Letra alineada arriba a la izquirda con Borde: Superior
	    //    Estilo: Alineado Horiz.-->Center, Vertical-->Bottom; con Border: Bottom, left, rigth
	    //solo borde left-Rigth
	    //solo Border Top
	    //    Estilo: Alineado Horiz.-->Center, Vertical-->Bottom; con Border: Bottom, left, rigth
	    //    Estilo: Letra: Centrada y Vertical. Borde: Bottom,left,Right,Top
	    //        Letra alineada arriba a la izquirda con Borde: Top y Right
	    //        Letra alineada arriba a la izquirda con Borde: Right
	    //        Letra alineada arriba a la izquirda con Borde: Right y Bottom
	    " <Styles>\n" + "   <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n" + "     <Alignment ss:Vertical=\"Bottom\"/>\n" + "     <Borders/>\n" + "     <Font/>\n" + "     <Interior/>\n" + "     <NumberFormat/>\n" + "     <Protection/>\n" + "   </Style>\n" + "   <Style ss:ID=\"encabezado\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "     <Interior ss:Color=\"#c6c6c6\" ss:Pattern=\"Solid\"/>\n" + "   </Style>\n" + " <Style ss:ID=\"s16\" ss:Name=\"Hipervínculo\"> \n" + "  <Font x:Family=\"Swiss\" ss:Color=\"#0000FF\" ss:Underline=\"Single\"/> \n" + "  </Style> \n" + "   <Style ss:ID=\"s17\" ss:Parent=\"s16\"> \n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "   </Style>\n" + "   <Style ss:ID=\"s18\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s19\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s20\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s21\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s22\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s23\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" />\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s24\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s25\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + " <Style ss:ID=\"s26\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:Rotate=\"90\"/>\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + " </Style>\n" + "  <Style ss:ID=\"s27\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" />\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + "  <Style ss:ID=\"s28\">\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "  </Style>\n" + "  <Style ss:ID=\"s29\">\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "  </Style>\n" + "  <Style ss:ID=\"s30\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n " + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + " <Style ss:ID=\"s31\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" ss:Rotate=\"90\"/>\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + "   <Style ss:ID=\"s32\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s33\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s34\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s35\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"0\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"Normal8DateTime\">\n" + "    <Alignment ss:Horizontal=\"Right\" ss:Vertical=\"Center\" ss:WrapText=\"1\"/>\n" + "    <Borders>\n" + "     <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "    </Borders>\n" + "    <Font ss:FontName=\"Times New Roman\" x:Family=\"Roman\" ss:Size=\"8\" />\n" + "   <NumberFormat ss:Format=\"yyyy-mm-dd;@\"/>\n" + "   </Style>\n";
	seccion3 += appendStyles();
	seccion3 += " </Styles>\n";
	return seccion3;
    }

    private String appendStyles() {
	String consulta = "SELECT * FROM tabs.qaqc_tabs WHERE estado <> '*'";
	String estilo = "";
	ResultSet result = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	try {
	    while (result.next()) {
		String colorStyle = Format.color2Hex(new Color(result.getInt("redcolor"), result.getInt("greencolor"), result.getInt("bluecolor")));
		estilo += "   <Style ss:ID=\"Color " + result.getString("name") + "\">\n" + "    <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" />\n" + "      <Borders>\n" + "        <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "        <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "        <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "        <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "      </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"0\"/>\n" + "     <Interior ss:Color=\"#" + colorStyle + "\" ss:Pattern=\"Solid\"/>\n" + "   </Style>\n";
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return estilo;
    }

    private String Seccion4() {
	//  Seccion 4: Propiedades del Documento de Word
	String seccion4 = "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "   <PageSetup>\n" + "     <Layout x:Orientation=\"Landscape\"/>\n" + "     <Header x:Margin=\"0\"/>\n" + "     <Footer x:Margin=\"0\"/>\n" + "     <PageMargins x:Bottom=\"0.98425196850393704\" x:Left=\"0.78740157480314965\"\n" + "     x:Right=\"0.78740157480314965\" x:Top=\"0.98425196850393704\"/>\n" + "   </PageSetup>\n" + "   <Print>\n" + "     <ValidPrinterInfo/>\n" + "     <PaperSizeIndex>8</PaperSizeIndex>\n" + "     <HorizontalResolution>300</HorizontalResolution>\n" + "     <VerticalResolution>300</VerticalResolution>\n" + "   </Print>\n" + "   <Selected/>\n" + "   <ProtectObjects>False</ProtectObjects>\n" + "   <ProtectScenarios>False</ProtectScenarios>\n" + "  </WorksheetOptions>\n" + "</Worksheet>\n";
	return seccion4;
    }

    private void writeEncabezadoPie() {
	//  Encabezado y Pie de Pï¿½gina
	writeln("<!-- Inicio del Encabezado y Pie -->");
	writeln("<!-- Fin del Encabezado y Pie -->");
    }

    private String InicioCuerpo() {
	String iniciocuerpo = "<!-- Inicio del cuerpo -->\n" + " <Worksheet ss:Name=\"General Report\">\n";
	return iniciocuerpo;
    }

    private String InicioCuerpoSecundario(String _ddh) {
	String iniciocuerpo = "<!-- Inicio del cuerpo -->\n" + " <Worksheet ss:Name=\"" + _ddh + "\" >\n";
	return iniciocuerpo;
    }

    private String Seccion4Principal() {
	//  Seccion 4: Propiedades del Documento de Word
	String seccion4 =
	    //  "   <ActivePane>2</ActivePane>\n" +
	    "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "   <PageSetup>\n" + "     <Layout x:Orientation=\"Landscape\"/>\n" + "     <Header x:Margin=\"0\"/>\n" + "     <Footer x:Margin=\"0\"/>\n" + "     <PageMargins x:Bottom=\"0.98425196850393704\" x:Left=\"0.78740157480314965\"\n" + "     x:Right=\"0.78740157480314965\" x:Top=\"0.98425196850393704\"/>\n" + "   </PageSetup>\n" + "   <Print>\n" + "     <ValidPrinterInfo/>\n" + "     <PaperSizeIndex>8</PaperSizeIndex>\n" + "     <HorizontalResolution>300</HorizontalResolution>\n" + "     <VerticalResolution>300</VerticalResolution>\n" + "   </Print>\n" + "   <Selected/>\n" + "   <ProtectObjects>False</ProtectObjects>\n" + "   <ProtectScenarios>False</ProtectScenarios>\n" + "  </WorksheetOptions>\n" + "   <DataValidation xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "     <Range>R3C1:R" + cantFilas + "C1</Range>\n" + "     <InputTitle>Drill</InputTitle>\n" + "     <InputMessage>Click to see drill data</InputMessage>\n" + "    </DataValidation>\n" + " </Worksheet>\n";
	return seccion4;
    }

    private String Seccion4Secundario() {
	//  Seccion 4: Propiedades del Documento de Word
	String seccion4 =
	    //  "   <ActivePane>2</ActivePane>\n" +
	    //      "  <AutoFilter x:Range=\"R1C1:R1C" + columnas + "\" xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
	    //      "  </AutoFilter>\n" +
	    "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "   <PageSetup>\n" + "     <Layout x:Orientation=\"Landscape\"/>\n" + "     <Header x:Margin=\"0\"/>\n" + "     <Footer x:Margin=\"0\"/>\n" + "     <PageMargins x:Bottom=\"0.98425196850393704\" x:Left=\"0.78740157480314965\"\n" + "     x:Right=\"0.78740157480314965\" x:Top=\"0.98425196850393704\"/>\n" + "   </PageSetup>\n" + "   <Print>\n" + "     <ValidPrinterInfo/>\n" + "     <PaperSizeIndex>8</PaperSizeIndex>\n" + "     <HorizontalResolution>300</HorizontalResolution>\n" + "     <VerticalResolution>300</VerticalResolution>\n" + "   </Print>\n" + "   <Selected/>\n" + "   <ProtectObjects>False</ProtectObjects>\n" + "   <ProtectScenarios>False</ProtectScenarios>\n" + "  </WorksheetOptions>\n" + "</Worksheet>\n";
	return seccion4;
    }

    private String Cuerpo() {
	write(InicioCuerpoSecundario(proyecto));
	write(InicioTabla());
	write(EncabezadoTabla());
	writeCuerpoTabla();
	write(FinTabla());
	write(Seccion4Principal());
	ResultSet drills = org.digitall.lib.sql.LibSQL.exQuery("SELECT iddrill, ddh FROM drilling.drills WHERE idproject = " + idproject + " AND estado <> '*' ORDER BY iddrill");
	try {
	    while (drills.next()) {
		int cantLit = Integer.parseInt(org.digitall.lib.sql.LibSQL.getCampo("SELECT count(*) FROM drilling.drill_lithologies, tabs.lithology_tabs WHERE iddrill = " + drills.getString("iddrill").toString() + " AND drilling.drill_lithologies.idlit_tab = tabs.lithology_tabs.idlit_tab"));
		int cantStr = Integer.parseInt(org.digitall.lib.sql.LibSQL.getCampo("SELECT count(*) FROM drilling.drill_structures, tabs.structure_tabs WHERE iddrill = " + drills.getString("iddrill").toString() + " AND drilling.drill_structures.idstr_tab = tabs.structure_tabs.idstr_tab "));
		write(InicioCuerpoSecundario("Drill " + drills.getString("ddh").toString()));
		write(InicioTablaSecundaria());
		if (cantLit != 0) {
		    write(EncabezadoTablaLithology(drills.getString("ddh").toString(), drills.getString("iddrill").toString()));
		    writeCuerpoTablaLithology(drills.getString("iddrill").toString());
		}
		if (cantStr != 0) {
		    write(EncabezadoTablaStructure(drills.getString("ddh").toString()));
		    writeCuerpoTablaStructure(drills.getString("iddrill").toString());
		}
		writeCuerpoTablaMinAlt(drills.getString("ddh").toString(), drills.getString("iddrill").toString());
		write(FinTabla());
		write(Seccion4Secundario());
	    }
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
	return "";
    }

    private String FinCuerpo() {
	String fincuerpo = "";
	return fincuerpo;
    }

    private String InicioTabla() {
	//  Supuestamente una tabla tiene 8644 unidades de ancho,
	//  por ende habrÃ­a que hacer ancho_columna = 8644/cantidad_columnas
	String titulotabla = "";
	String iniciotabla = "<!-- Inicio de la tabla -->\n" + " <Table>\n" + "    <Column ss:AutoFitWidth=\"0\" ss:Width=\"66.75\" ss:Span=\"4\" />\n";
	return titulotabla + iniciotabla;
    }

    private String InicioTablaSecundaria() {
	//  Supuestamente una tabla tiene 8644 unidades de ancho,
	//  por ende habrÃ­a que hacer ancho_columna = 8644/cantidad_columnas
	String titulotabla = "";
	String iniciotabla = "<!-- Inicio de la tabla -->\n" + " <Table>\n" + "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"66.75\" ss:Span=\"3\" />\n";
	return titulotabla + iniciotabla;
    }

    private String EncabezadoTabla() {
	String filatabla = "";
	filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"5\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Project " + proyecto + " Summary</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:StyleID=\"s25\" ><Data ss:Type=\"String\">Drill name</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">UTM E</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">UTM N</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Max. Depth</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Date</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Geologist</Data></Cell>\n" + "   </Row> \n";
	return filatabla;
    }

    private String EncabezadoTablaLithology(String _ddh, String _iddrill) {
	String filatabla = "";
	filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"5\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Project " + proyecto + " Summary</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Drill name</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">UTM E</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">UTM N</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Max. Depth</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Date</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Geologist</Data></Cell>\n" + "   </Row> \n";
	String consulta = "SELECT ddh, X(utm) as E, Y(utm) as N , depth, date, geologist FROM drilling.drills WHERE iddrill = " + _iddrill;
	ResultSet Resulx = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	try {
	    while (Resulx.next()) {
		filatabla += "   <Row>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Resulx.getString("ddh") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("E") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("N") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("depth") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Resulx.getString("date") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Resulx.getString("geologist").trim() + "</Data></Cell>\n";
		filatabla += "   </Row>\n";
		//write(filatabla);
	    }
	} catch (SQLException x) {
	    org.digitall.lib.components.Advisor.messageBox(x.getMessage(), "Error");
	    x.printStackTrace();
	}
	filatabla += "   <Row>\n" + "   </Row>\n";
	//fila 1
	//fila 2
	filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"6\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Drill " + _ddh + " - Lithology list</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Code</Data></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Description</Data></Cell>\n" + "   </Row> \n";
	return filatabla;
    }

    private String EncabezadoTablaStructure(String _ddh) {
	String filatabla = "";
	//fila 1
	//fila 2
	filatabla += "   <Row> \n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Drill " + _ddh + " - Structure list</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Code</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Dip. direction</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Dip. angle</Data></Cell>\n" + "   </Row> \n";
	return filatabla;
    }

    private void writeCuerpoTabla() {
	String filatabla = "";
	String consulta = "SELECT ddh, X(utm) as E, Y(utm) as N , depth, date, geologist FROM drilling.drills WHERE idproject = " + idproject + " Order by ddh";
	ResultSet Resulx = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	try {
	    while (Resulx.next()) {
		filatabla += "   <Row>\n";
		filatabla += "    <Cell ss:StyleID=\"s17\" ss:HRef=\"#'Drill " + Resulx.getString("ddh") + "'!A1\" ><Data ss:Type=\"String\">" + Resulx.getString("ddh") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("E") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("N") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + Resulx.getDouble("depth") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Resulx.getString("date") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + Resulx.getString("geologist") + "</Data></Cell>\n";
		filatabla += "   </Row>\n";
	    }
	    write(filatabla);
	    filatabla += "";
	} catch (SQLException x) {
	    org.digitall.lib.components.Advisor.messageBox(x.getMessage(), "Error");
	    x.printStackTrace();
	}
    }

    private void writeCuerpoTablaLithology(String _iddrill) {
	String filatabla = "";
	/******* CUERPO DE LA TABLA LITHOLOGY*******/
	try {
	    //System.out.println(querys[indice]);
	    String consulta = "SELECT drill_lithologies.\"from\", drill_lithologies.\"to\", lithology_tabs.code, " + " drill_lithologies.description FROM drilling.drill_lithologies, " + " tabs.lithology_tabs WHERE iddrill = " + _iddrill + " AND " + " drilling.drill_lithologies.idlit_tab = tabs.lithology_tabs.idlit_tab " + " Order By \"from\", \"to\"; ";
	    ResultSet ResulLithology = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	    while (ResulLithology.next()) {
		filatabla = "   <Row>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulLithology.getDouble("from") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulLithology.getDouble("to") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + ResulLithology.getString("code") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + ResulLithology.getString("description") + "</Data></Cell>\n";
		filatabla += "   </Row>\n";
		write(filatabla);
	    }
	} catch (SQLException x) {
	    org.digitall.lib.components.Advisor.messageBox(x.getMessage(), "Error");
	    x.printStackTrace();
	}
    }

    private void writeCuerpoTablaStructure(String _iddrill) {
	/******* CUERPO DE LA TABLA STRUCTURE  *******/
	String filatabla = "";
	String consulta = "SELECT drill_structures.\"from\", drill_structures.\"to\", structure_tabs.code, " + " drill_structures.dip_direction, drill_structures.dip_angle" + " FROM drilling.drill_structures, tabs.structure_tabs " + " WHERE iddrill = " + _iddrill + " AND drilling.drill_structures.idstr_tab = tabs.structure_tabs.idstr_tab " + " Order By \"from\", \"to\"; ";
	ResultSet ResulStructure = org.digitall.lib.sql.LibSQL.exQuery(consulta);
	try {
	    while (ResulStructure.next()) {
		filatabla = "   <Row>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulStructure.getDouble("from") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulStructure.getDouble("to") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"String\">" + ResulStructure.getString("code") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulStructure.getString("dip_direction") + "</Data></Cell>\n";
		filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + ResulStructure.getString("dip_angle") + "</Data></Cell>\n";
		filatabla += "   </Row>\n";
		write(filatabla);
	    }
	} catch (SQLException x) {
	    org.digitall.lib.components.Advisor.messageBox(x.getMessage(), "Error");
	    x.printStackTrace();
	}
    }

    private String FinTabla() {
	String fintabla = "  </Table>\n" + "<!-- Fin de la tabla -->\n";
	return fintabla;
    }

    public String getEncabezadoReporte() {
	return EncabezadoReporte;
    }

    public void setEncabezadoReporte(String _EncabezadoReporte) {
	EncabezadoReporte = _EncabezadoReporte;
    }

    private void setearResultSet() {
	try {
	    String consulta = "SELECT idmin_tab,code FROM tabs.mineralization_tabs " + " WHERE idmin_tab In( SELECT (idall_min) as idmin " + " FROM drilling.mineralization_depth_interval " + " Where idall_min In (Select idall_min From drilling.mineralization_depth_interval " + " Where idall_min In (Select idall_min From drilling.allowed_mineralizations " + " Where  idproject = " + idproject + ")) And status<>'*' )Order By idmin_tab";
	    String ConsultaMin = "SELECT idmin_tab, code from tabs.mineralization_tabs where idmin_tab in \n" + "	(select idmin_tab from drilling.allowed_mineralizations where idproject = " + idproject + ")\n" + "order by idmin_tab";
	    //System.out.println("consulta Min.: "+ConsultaMin);
	    minPermitidas = org.digitall.lib.sql.LibSQL.exQuery(ConsultaMin);
	    while (minPermitidas.next()) {
		mineralizaciones.addElement(new String(minPermitidas.getString("code")));
	    }
	    consulta = "SELECT idalt_tab,code FROM tabs.alteration_tabs WHERE idalt_tab In " + " ( SELECT (idall_alt) as idalt FROM drilling.alteration_depth_interval " + " Where idall_alt In (Select idall_alt From drilling.alteration_depth_interval" + " Where idall_alt In (Select idall_alt From drilling.allowed_alterations " + " Where  idproject = " + idproject + ")) And status<>'*' ) Order By idalt_tab";
	    String ConsultaAlt = "SELECT idalt_tab, code FROM tabs.alteration_tabs WHERE idalt_tab in \n" + "	(SELECT idalt_tab FROM drilling.allowed_alterations WHERE idproject = " + idproject + ")\n" + "ORDER BY idalt_tab;";
	    //System.out.println("consulta Alt.: "+ ConsultaAlt);
	    altPermitidas = org.digitall.lib.sql.LibSQL.exQuery(ConsultaAlt);
	    while (altPermitidas.next()) {
		alteraciones.addElement(new String(altPermitidas.getString("code")));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private void writeCuerpoTablaMinAlt(String _ddh, String _iddrill) {
	String filatabla = "";
	filatabla += "   <Row> \n" + "   </Row> \n" + "   <Row> \n" + "       <Cell ss:MergeAcross=\"" + (columnasMinAlt + 3 - 1) + "\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Drill " + _ddh + " - Log data</Data></Cell>\n" + "   </Row> \n";
	if ((cantMin > 0) && (cantAlt > 0)) {
	    filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"" + (cantMin - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Mineralogy (%)</Data></Cell>\n";
	    if (cantAlt == 1) {
		filatabla += "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">WallAlt'n (w) 1-2-3- (s)</Data></Cell>\n";
	    } else {
		filatabla += "    <Cell ss:MergeAcross=\"" + (cantAlt - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">WallAlt'n (w) 1-2-3- (s)</Data></Cell>\n";
	    }
	    filatabla += "    <Cell ss:MergeAcross=\"2\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Depth Interval</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n";
	    for (int i = 0; i < cantMin; i++) {
		filatabla += "   <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">" + mineralizaciones.elementAt(i) + "</Data></Cell>\n";
	    }
	    if (cantAlt == 1) {
		filatabla += "   <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">" + alteraciones.elementAt(0) + "</Data></Cell>\n";
	    } else {
		for (int i = 0; i < cantAlt; i++) {
		    filatabla += "   <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">" + alteraciones.elementAt(i) + "</Data></Cell>\n";
		}
	    }
	    filatabla += "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Density (%)</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "   </Row> \n";
	    write(filatabla);
	    writeDrillData(_iddrill);
	    writeSampleData(_ddh, _iddrill);
	} else if ((cantMin > 0) && (cantAlt == 0)) {
	    filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"" + (cantMin - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Mineralogy (%)</Data></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Sampling and Assay</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n";
	    for (int i = 0; i < cantMin; i++) {
		filatabla += "   <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + mineralizaciones.elementAt(i) + "</Data></Cell>\n";
	    }
	    filatabla += "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Density (%)</Data></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "   </Row> \n";
	    write(filatabla);
	    writeDrillData(_iddrill);
	} else if ((cantMin == 0) && (cantAlt > 0)) {
	    filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"" + (cantAlt - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">WallAlt'n (w) 1-2-3- (s)</Data></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Sampling and Assay</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n";
	    for (int i = 0; i < cantAlt; i++) {
		filatabla += "   <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">" + alteraciones.elementAt(i) + "</Data></Cell>\n";
	    }
	    filatabla += "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Density (%)</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "   </Row> \n";
	    write(filatabla);
	    writeDrillData(_iddrill);
	} else {
	    filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Sampling and Assay</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "         <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Vein Density (%)</Data></Cell>\n" + "         <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "         <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "   </Row> \n";
	    write(filatabla);
	    writeDrillData(_iddrill);
	}
    }

    private void writeDrillData(String _iddrill) {
	ResultSet intervals = org.digitall.lib.sql.LibSQL.exQuery("SELECT iddep_int FROM drilling.depth_interval WHERE iddrill = " + _iddrill + " Order By \"from\", \"to\"");
	String filatabla = "";
	try {
	    while (intervals.next()) {
		filatabla += "   <Row> \n";
		String consulta = "SELECT idmin_dep_int, amount FROM " + seccion[0] + " where iddrill = " + _iddrill + " and iddep_int = " + intervals.getString("iddep_int") + "  AND status <> '*' order by idall_min";
		ResultSet mineralizaciones = org.digitall.lib.sql.LibSQL.exQuery(consulta);
		while (mineralizaciones.next()) {
		    filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + mineralizaciones.getDouble("amount") + "</Data></Cell>\n";
		}
		ResultSet alteraciones = org.digitall.lib.sql.LibSQL.exQuery("SELECT idalt_dep_int, amount FROM " + seccion[1] + " WHERE iddrill = " + _iddrill + " AND iddep_int = " + intervals.getString("iddep_int") + " AND status <> '*' order by idall_alt");
		while (alteraciones.next()) {
		    filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + alteraciones.getDouble("amount") + "</Data></Cell>\n";
		}
		String ConsultaSamples = "SELECT drilling.depth_interval.iddep_int, drilling.depth_interval.veindensity," + " drilling.depth_interval.from, drilling.depth_interval.to " + " FROM drilling.samples, drilling.depth_interval, tabs.qaqc_tabs " + " WHERE depth_interval.iddrill = " + _iddrill + " AND drilling.depth_interval.iddep_int = " + intervals.getString("iddep_int") + " AND depth_interval.estado <> '*'" + " ORDER BY \"from\", \"to\"";
		//System.out.println("consulta samples: " +ConsultaSamples);
		ResultSet samples = org.digitall.lib.sql.LibSQL.exQuery(ConsultaSamples);
		if (samples.next()) {
		    filatabla += "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + samples.getDouble("veindensity") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + samples.getDouble("from") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"s35\"><Data ss:Type=\"Number\">" + samples.getDouble("to") + "</Data></Cell>\n";
		}
		filatabla += "   </Row> \n";
	    }
	    write(filatabla);
	} catch (SQLException ex) {
	    ex.printStackTrace();
	}
    }

    private void writeSampleData(String _ddh, String _iddrill) {
	String filatabla = "";
	filatabla += "   <Row> \n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"encabezado\"><Data ss:Type=\"String\">Drill " + _ddh + " - Sample</Data></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Sample Number</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">From</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">To</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">Mass</Data></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">QAQC</Data></Cell>\n" + "   </Row> \n";
	String ConsultaSamples = "SELECT idsample, samplenumber,\"from\",\"to\", samples.idqaqc,mass,tabs.qaqc_tabs.name " + " FROM drilling.samples, tabs.qaqc_tabs " + " WHERE iddrill = " + _iddrill + " AND qaqc_tabs.idqaqc = samples.idqaqc " + " AND samples.estado <> '*' " + " ORDER BY \"from\", \"to\"";
	//System.out.println("consulta samples: " +ConsultaSamples);
	String style = "ss:StyleID=\"s35\" ";
	ResultSet samples = org.digitall.lib.sql.LibSQL.exQuery(ConsultaSamples);
	try {
	    while (samples.next()) {
		filatabla += "  <Row> \n" + "    <Cell ss:StyleID=\"Color " + samples.getString("name") + "\"><Data ss:Type=\"String\">" + samples.getString("samplenumber") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"Color " + samples.getString("name") + "\"><Data ss:Type=\"Number\">" + samples.getDouble("from") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"Color " + samples.getString("name") + "\"><Data ss:Type=\"Number\">" + samples.getDouble("to") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"Color " + samples.getString("name") + "\"><Data ss:Type=\"Number\">" + samples.getDouble("mass") + "</Data></Cell>\n" + "    <Cell ss:StyleID=\"Color " + samples.getString("name") + "\"><Data ss:Type=\"String\">" + samples.getString("name") + "</Data></Cell>\n" + "  </Row> \n";
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	write(filatabla);
    }

}
