package org.digitall.apps.drilling.reports;

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

//

/** FORMATOS DE TEXTO EN XML
TAMAï¿½ DE LETRA (48 en XML es 24 en Word, 32 en XML es 16 en Word):
  <w:r><w:rPr><w:sz w:val=\"48\"/><w:sz-cs w:val=\"48\"/></w:rPr><w:t> TEXTO </w:t></w:r>
SUBRAYADO SIMPLE:
  <w:r><w:rPr><w:u w:val=\"single\"/></w:rPr><w:t> TEXTO </w:t></w:r>
NEGRITA:
  <w:r><w:rPr><w:b/></w:rPr><w:t> TEXTO </w:t></w:r>
PARRAFO CENTRADO:
  <w:p><w:pPr><w:jc w:val=\"center\"/></w:pPr></w:p>
 */
public class XMLDrillSheet {

    private FileWriter xmlFile;
    private BufferedWriter log;
    //   private int anchocolumna = 0;
    private String EncabezadoReporte = "";
    private String[] querys;
    private int columnasMinAlt;
    //   private Vector cabecera;
    private String xmlPath = "";
    private ResultSet minPermitidas;
    private ResultSet altPermitidas;
    private Vector mineralizaciones = new Vector();
    private Vector alteraciones = new Vector();
    private int cantMin = 0;
    private int cantAlt = 0;
    private String proyecto = "", filatabla = "", idproject = "";
    private int metros;
    private boolean vacio;
    private ConfigFile cfg = new ConfigFile("ddesktop.conf");

    public XMLDrillSheet(String _idproject, int _metros, boolean _vacio) {
	idproject = _idproject;
	metros = _metros;
	vacio = _vacio;
	buscaProyecto();
	setearResultSet();
	cantMin = mineralizaciones.size();
	cantAlt = alteraciones.size();
	//************
	columnasMinAlt = cantMin + cantAlt;
	System.out.println("cantMin: " + cantMin);
	System.out.println("cantAlt: " + cantAlt);
	// Abro el Archivo
	//System.out.println("Ruta: " + Proced.getRuta());
	//xmlPath = Proced.ruta + Proced.separador + "Informe de "+ params[0][0].replaceAll("/","-") +" - "+ Proced.FechaHora2(false,true) +".xls";
	//xmlPath = Proced.getRuta() + File.separator + "informes" + File.separator + proyecto +" DDH template.xml";    //"+ cabecera.elementAt(0).toString().replaceAll("/","-")  +".xls";
	xmlPath = cfg.getProperty("TemplatesPath") + File.separator;
	//System.out.println("ruta: "+ xmlPath);
	if (AbreArchivo(xmlPath)) {
	    write(Seccion1());
	    write(Seccion2());
	    write(Seccion3());
	    writeEncabezadoPie();
	    write(InicioCuerpo());
	    write(Cuerpo());
	    write(FinCuerpo());
	    write(Seccion4());
	    write(FinSeccion1());
	    //String Fecha = Proced.FechaHora2(true,false);
	    if (CierraArchivo()) {
		//System.out.println("Archivo XML generado con éxito");
		//        Advisor.messageBoxPopupWindow("<html>Reporte generado con éxito en<br><a href=file:///" + xmlPath + ">" + xmlPath + "</a></html>");
		//        Advisor.messageBoxPopupWindow("Reporte generado con éxito");
		Advisor.messagePopupWindow("<html><p align=center>Reporte generado con éxito<br><a href=>Click aquí para verlo</a></p></html>", xmlPath);
	    }
	}
    }

    private void setearResultSet() {
	try {
	    String consulta = "SELECT idmin_tab,name FROM tabs.mineralization_tabs " + " WHERE idmin_tab In( SELECT (idall_min) as idmin " + " FROM drilling.mineralization_depth_interval " + " Where idall_min In (Select idall_min From drilling.mineralization_depth_interval " + " Where idall_min In (Select idall_min From drilling.allowed_mineralizations " + " Where  idproject = " + idproject + ")) And status<>'*' )Order By idmin_tab";
	    String ConsultaMin = "SELECT idmin_tab, name from tabs.mineralization_tabs where idmin_tab in \n" + "	(select idmin_tab from drilling.allowed_mineralizations where idproject = " + idproject + ")\n" + "order by idmin_tab";
	    //System.out.println("consulta Min.: "+ConsultaMin);
	    minPermitidas = org.digitall.lib.sql.LibSQL.exQuery(ConsultaMin);
	    while (minPermitidas.next()) {
		mineralizaciones.addElement(new String(minPermitidas.getString("name")));
	    }
	    consulta = "SELECT idalt_tab,name FROM tabs.alteration_tabs WHERE idalt_tab In " + " ( SELECT (idall_alt) as idalt FROM drilling.alteration_depth_interval " + " Where idall_alt In (Select idall_alt From drilling.alteration_depth_interval" + " Where idall_alt In (Select idall_alt From drilling.allowed_alterations " + " Where  idproject = " + idproject + ")) And status<>'*' ) Order By idalt_tab";
	    String ConsultaAlt = "SELECT idalt_tab, name from tabs.alteration_tabs where idalt_tab in \n" + "	(select idalt_tab from drilling.allowed_alterations where idproject = " + idproject + ")\n" + "order by idalt_tab;";
	    //System.out.println("consulta Alt.: "+ ConsultaAlt);
	    altPermitidas = org.digitall.lib.sql.LibSQL.exQuery(ConsultaAlt);
	    while (altPermitidas.next()) {
		alteraciones.addElement(new String(altPermitidas.getString("name")));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
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
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error XMLDrillSheet");
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
	String seccion1 = "<?xml version=\"1.0\"?>\n" + "<?mso-application progid=\"Excel.Sheet\"?>\n" + "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" + " xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n" + " xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n" + " xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n" + " xmlns:html=\"http://www.w3.org/TR/REC-html40\">\n";
	return seccion1;
    }

    private String FinSeccion1() {
	String finseccion1 = "</Workbook>\n";
	return finseccion1;
    }

    private String Seccion2() {
	// Seccion 2: Propiedades
	String seccion2 = " <DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n" + "  <Author>DIGITALL</Author>\n" + "  <LastAuthor>DIGITALL</LastAuthor>\n" + "  <Created>2007-05-07T08:12:27Z</Created>\n" + "  <Company>SERMAQ OS. - Digitall</Company>\n" + "  <Version>11.6360</Version>\n" + " </DocumentProperties>\n" + " <ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "  <WindowHeight>8700</WindowHeight>\n" + "  <WindowWidth>11595</WindowWidth>\n" + "  <WindowTopX>360</WindowTopX>\n" + "  <WindowTopY>120</WindowTopY>\n" + "  <ProtectStructure>False</ProtectStructure>\n" + "  <ProtectWindows>False</ProtectWindows>\n" + " </ExcelWorkbook>\n";
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
	    " <Styles>\n" + "   <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n" + "     <Alignment ss:Vertical=\"Bottom\"/>\n" + "     <Borders/>\n" + "     <Font/>\n" + "     <Interior/>\n" + "     <NumberFormat/>\n" + "     <Protection/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s18\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s19\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s20\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s21\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s22\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s23\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" />\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s24\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s25\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + " <Style ss:ID=\"s26\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:Rotate=\"90\"/>\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + " </Style>\n" + "  <Style ss:ID=\"s27\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Bottom\" />\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + "  <Style ss:ID=\"s28\">\n" + "     <Borders>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "  </Style>\n" + "  <Style ss:ID=\"s29\">\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "  </Style>\n" + "  <Style ss:ID=\"s30\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n " + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + " <Style ss:ID=\"s31\">\n" + "     <Alignment ss:Horizontal=\"Center\" ss:Vertical=\"Center\" ss:WrapText=\"1\" ss:Rotate=\"90\"/>\n " + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Left\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "  </Style>\n" + "   <Style ss:ID=\"s32\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Top\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s33\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + "   <Style ss:ID=\"s34\">\n" + "     <Alignment ss:Horizontal=\"Left\" ss:Vertical=\"Top\" />\n" + "     <Borders>\n" + "       <Border ss:Position=\"Bottom\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "       <Border ss:Position=\"Right\" ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/>\n" + "     </Borders>\n" + "     <Font x:Family=\"Swiss\" ss:Bold=\"1\"/>\n" + "   </Style>\n" + " </Styles>\n";
	return seccion3;
    }

    private String Seccion4() {
	//  Seccion 4: Propiedades del Documento de Word
	String seccion4 =
	    //      "  <AutoFilter x:Range=\"R1C1:R1C" + columnas + "\" xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" +
	    //      "  </AutoFilter>\n" +
	    "  <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n" + "   <PageSetup>\n" + "     <Layout x:Orientation=\"Landscape\"/>\n" + "     <Header x:Margin=\"0\"/>\n" + "     <Footer x:Margin=\"0\"/>\n" + "     <PageMargins x:Bottom=\"0.98425196850393704\" x:Left=\"0.78740157480314965\"\n" + "     x:Right=\"0.78740157480314965\" x:Top=\"0.98425196850393704\"/>\n" + "   </PageSetup>\n" + "   <Print>\n" + "     <ValidPrinterInfo/>\n" + "     <PaperSizeIndex>8</PaperSizeIndex>\n" + "     <HorizontalResolution>300</HorizontalResolution>\n" + "     <VerticalResolution>300</VerticalResolution>\n" + "   </Print>\n" + "   <Selected/>\n" + "   <SplitHorizontal>7</SplitHorizontal>\n" + "   <TopRowBottomPane>7</TopRowBottomPane>\n" + "   <ActivePane>2</ActivePane>\n" + "   <Panes>\n" + "    <Pane>\n" + "     <Number>3</Number>\n" + "    </Pane>\n" + "    <Pane>\n" + "     <Number>2</Number>\n" + "        <RangeSelection>R8</RangeSelection>\n" + "    </Pane>\n" + "   </Panes>\n" + "   <ProtectObjects>False</ProtectObjects>\n" + "   <ProtectScenarios>False</ProtectScenarios>\n" + "  </WorksheetOptions>\n" + "</Worksheet>\n";
	return seccion4;
    }
    /* private String writeFoto(int _idfoto, double _width, double _height)
  {
    try
    {
      //ResultSet rs = ps.executeQuery();
      ResultSet rs = org.digitall.lib.sql.LibSQL.exQuery("SELECT foto, ancho, alto from fotos where idfoto = " + _idfoto);
      if (rs != null)
      {
        write("<w:pict><w:binData w:name=\"wordml://" + _idfoto + ".jpg\">");

        while(rs.next())
        {
          byte[] imgBytes =rs.getBytes(1);
          write(new String(Base64Coder.encode(imgBytes)));
        }
        double anchoimagen = rs.getDouble("ancho");
        double altoimagen = rs.getDouble("alto");
        double factor1 = _width/anchoimagen;
        double factor2 = _height/altoimagen;
        double factor = 1;
        if (factor1>factor2) factor = factor1; else factor = factor2;
        double width = anchoimagen * factor;
        double height = altoimagen * factor;
        write("</w:binData><v:shape id=\"_x0000_i1025\" type=\"#_x0000_t75\" style=\"width:"
        + width + "pt" + ";height:" + height + "pt\">" +
        "<v:imagedata src=\"wordml://" + _idfoto + ".jpg\" o:title=\"Foto\"/>" +
        "</v:shape></w:pict>");
        rs.close();
      }
    } catch (Exception x)
    {
      org.digitall.lib.components.Advisor.messageBox("Error al obtener la Foto","Error");
      x.printStackTrace();
    }
    return "";
  }
*/

    private void writeEncabezadoPie() {
	//  Encabezado y Pie de Página
	writeln("<!-- Inicio del Encabezado y Pie -->");
	//Encabezado
	//    writeFoto(1,20,30);
	//Pie de Pï¿½ina
	writeln("<!-- Fin del Encabezado y Pie -->");
    }

    private String InicioCuerpo() {
	String iniciocuerpo = "<!-- Inicio del cuerpo -->\n" + " <Worksheet ss:Name=\"Hoja1\">\n" + "   <Names>\n" + "       <NamedRange ss:Name=\"Print_Titles\" ss:RefersTo=\"=Hoja1!R1:R7\"/>\n" + "   </Names>\n";
	return iniciocuerpo;
    }

    private String Cuerpo() {
	write(InicioTabla());
	write(EncabezadoTabla());
	/*for (int i = 0; i<querys.length; i++)
    {*/
	//writeCuerpoTabla(i);    //este es el original
	writeCuerpoTabla();
	//}
	write(FinTabla());
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
	String iniciotabla = "<!-- Inicio de la tabla -->\n" + " <Table>\n" + "   <Column ss:AutoFitWidth=\"0\" ss:Width=\"45.75\" ss:Span=\"3\"/>\n";
	//        if ((cantMin > 0) && (cantAlt > 0) ){
	iniciotabla += "   <Column ss:Index=\"5\" ss:AutoFitWidth=\"0\" ss:Width=\"66.75\" ss:Span=\"3\" />\n" + "   <Column ss:Index=\"9\" ss:AutoFitWidth=\"0\" ss:Width=\"35.25\" ss:Span=\"3\"/>\n" + "   <Column ss:Index=\"13\" ss:AutoFitWidth=\"0\" ss:Width=\"56\" />\n";
	//      }
	return titulotabla + iniciotabla;
    }

    private String EncabezadoTabla() {
	String filatabla = "";
	if ((cantMin > 0) && (cantAlt > 0)) {
	    if (columnasMinAlt > 3) {
		//fila 1
		//fila 2
		//    "    <Cell ss:MergeAcross=\""+(cantMin + cantAlt-1)+"\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Date:</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n"+
		//fila 3
		//    "    <Cell ss:MergeAcross=\""+(cantMin + cantAlt-1)+"\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Sheet No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n"+
		//fila 4
		//    "    <Cell ss:MergeAcross=\" "+(cantMin + cantAlt-1)+" \" ss:StyleID=\"s24\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n"+
		// fila 6
		filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"3\" ss:MergeDown=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Imagen </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s19\"><Data ss:Type=\"String\"> Project </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Depth </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Geologist </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + (columnasMinAlt - 4)) + "\" ss:StyleID=\"s32\"><Data ss:Type=\"String\"> Notes </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> DDH No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM E: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Date:</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + (columnasMinAlt - 4)) + "\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Azimuth: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM N: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Sheet No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + (columnasMinAlt - 4)) + "\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Dip.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Scale: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + (columnasMinAlt - 4)) + "\" ss:StyleID=\"s34\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row ss:Index=\"6\" ss:Height=\"26.25\"> \n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Structure </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Description </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (cantMin - 1) + "\"  ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Mineralogy (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (cantAlt - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> WallAlt'n (w) 1-2-3 (s) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sampling and Assay </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n";
		//Fila 7
		filatabla += "   <Row ss:Index=\"7\" ss:Height=\"50\"> \n" + "    <Cell ss:Index=\"1\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> mts. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"2\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Lithology </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"3\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Core Axis </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"4\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Dip Angle </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		int index = 9;
		for (int i = 0; i < cantMin; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + mineralizaciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
		index = 9 + cantMin;
		for (int i = 0; i < cantAlt; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + alteraciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
		filatabla += "    <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Density (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sample No. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> From (m). </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> To (m) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> QAQC </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n";
	    } else {
		//fila 1
		//fila 2
		//fila 3
		//fila 4
		// fila 6
		//        "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Description </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n"+
		filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"3\" ss:MergeDown=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Imagen </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s19\"><Data ss:Type=\"String\"> Project </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Depth </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Geologist </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s32\"><Data ss:Type=\"String\"> Notes </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> DDH No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM E: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Date:</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Azimuth: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM N: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Sheet No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Dip.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Scale: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s34\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row ss:Index=\"6\" ss:Height=\"26.25\"> \n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Structure </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ";
		if (columnasMinAlt == 3) {
		    filatabla += "ss:MergeAcross=\"4\"";
		} else {
		    filatabla += "ss:MergeAcross=\"5\"";
		}
		//Fila 7
		filatabla += " ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Description </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (cantMin - 1) + "\"  ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Mineralogy (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (cantAlt - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> WallAlt'n (w) 1-2-3 (s) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sampling and Assay </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row ss:Index=\"7\" ss:Height=\"50\"> \n" + "    <Cell ss:Index=\"1\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> mts. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"2\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Lithology </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"3\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Core Axis </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"4\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Dip Angle </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"5\"";
		int index = 0;
		if (columnasMinAlt == 3) {
		    filatabla += " ss:MergeAcross=\"4\"";
		    index = 10;
		} else {
		    filatabla += " ss:MergeAcross=\"5\"";
		    index = 11;
		}
		filatabla += " ss:StyleID=\"s25\"><Data ss:Type=\"String\">Geological Description</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		for (int i = 0; i < cantMin; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + mineralizaciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
		for (int i = 0; i < cantAlt; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + alteraciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
		filatabla += "    <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Density (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sample No. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> From (m). </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> To (m) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> QAQC </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n";
	    }
	} else if ((cantMin == 0) || (cantAlt == 0)) {
	    //fila 1
	    //fila 2
	    //fila 3
	    //fila 4
	    // fila 6
	    //        "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Description </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n"+
	    filatabla += "   <Row> \n" + "    <Cell ss:MergeAcross=\"3\" ss:MergeDown=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Imagen </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s19\"><Data ss:Type=\"String\"> Project </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Depth </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s22\"><Data ss:Type=\"String\"> Geologist </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + columnasMinAlt) + "\" ss:StyleID=\"s32\"><Data ss:Type=\"String\"> Notes </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> DDH No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM E: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Date:</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + columnasMinAlt) + "\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Azimuth: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> UTM N: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s20\"><Data ss:Type=\"String\"> Sheet No.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + columnasMinAlt) + "\" ss:StyleID=\"s33\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row> \n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Dip.: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"1\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"> Scale: </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s24\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:MergeAcross=\"" + (4 + columnasMinAlt) + "\" ss:StyleID=\"s34\"><Data ss:Type=\"String\"></Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row ss:Index=\"6\" ss:Height=\"26.25\"> \n" + "    <Cell ss:MergeAcross=\"3\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Structure </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ";
	    if (columnasMinAlt != 0) {
		// Uno de los dos es igual a cero
		filatabla += "ss:MergeAcross=\"5\"";
	    } else {
		filatabla += "ss:MergeAcross=\"7\"";
	    }
	    filatabla += " ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Description </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
	    if (cantMin != 0) {
		filatabla += "    <Cell ss:MergeAcross=\"" + (cantMin - 1) + "\"  ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Mineralogy (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
	    } else if (cantAlt != 0) {
		filatabla += "    <Cell ss:MergeAcross=\"" + (cantAlt - 1) + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> WallAlt'n (w) 1-2-3 (s) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
	    }
	    //Fila 7
	    filatabla += "    <Cell ss:MergeAcross=\"4\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sampling and Assay </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n" + "   <Row ss:Index=\"7\" ss:Height=\"50\"> \n" + "    <Cell ss:Index=\"1\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> mts. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"2\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Lithology </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"3\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Core Axis </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"4\" ss:StyleID=\"s31\"><Data ss:Type=\"String\"> Dip Angle </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:Index=\"5\"";
	    int index = 11;
	    if (columnasMinAlt != 0) {
		// Uno de los dos es igual a cero
		filatabla += " ss:MergeAcross=\"5\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Geological Description</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
	    } else {
		filatabla += " ss:MergeAcross=\"7\" ss:StyleID=\"s25\"><Data ss:Type=\"String\">Geological Description</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		index = 13;
	    }
	    if (cantMin != 0) {
		for (int i = 0; i < cantMin; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + mineralizaciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
	    }
	    if (cantAlt != 0) {
		for (int i = 0; i < cantAlt; i++) {
		    filatabla += "   <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s31\"><Data ss:Type=\"String\">" + alteraciones.elementAt(i) + "</Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n";
		    index = index + 1;
		}
	    }
	    filatabla += "    <Cell ss:Index=\"" + index + "\" ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Vein Density (%) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\"> Sample No. </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> From (m). </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> To (m) </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "    <Cell ss:StyleID=\"s31\"><Data ss:Type=\"String\"> QAQC </Data><NamedCell ss:Name=\"Print_Titles\"/></Cell>\n" + "   </Row> \n";
	}
	return filatabla;
    }
    //private void writeCuerpoTabla(int indice)

    private void writeCuerpoTabla() {
	//String fila = "";
	/* try
    {*/
	//System.out.println(querys[indice]);
	/*ResultSet Resulx = org.digitall.lib.sql.LibSQL.exQuery(querys[indice]);
      while (Resulx.next())
      {
//        fila = "   <Row ss:AutoFitHeight=\"0\" ss:Height=\"16.5\">\n";
        fila = "   <Row ss:Height=\"16.5\">\n";
        for (int i=0; i<columnas; i++) fila +=
          "    <Cell ss:StyleID=\"s25\"><Data ss:Type=\"String\">"+ Resulx.getString(i+1) + "</Data></Cell>\n";
        fila += "   </Row>\n";
        write(fila);
      }*/
	/*} catch (SQLException x)
    {
      org.digitall.lib.components.Advisor.messageBox(x.getMessage(), "Error");
      x.printStackTrace();
    }*/
	String fila = "";
	if (vacio) {
	    for (int j = 1; j <= metros / 2; j++) {
		fila += "  <Row>\n";
		if ((j % 5) == 0) {
		    fila += "    <Cell ss:StyleID=\"s27\"><Data ss:Type=\"String\">" + (j * 2) + "</Data></Cell>\n";
		} else {
		    fila += "    <Cell ss:StyleID=\"s28\"/>\n";
		}
		if ((cantMin > 0) && (cantAlt > 0)) {
		    if (columnasMinAlt > 3) {
			fila += "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:Index=\"5\" ss:MergeAcross=\"3\" ss:StyleID=\"s27\"/>\n";
			for (int i = 9; i < (9 + columnasMinAlt - 1); i++) {
			    fila += "    <Cell ss:Index=\"" + i + "\" ss:StyleID=\"s28\"/>\n";
			}
			fila += "    <Cell ss:Index=\"" + (9 + columnasMinAlt) + "\" ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n";
		    } else {
			fila += "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:Index=\"5\"";
			int index = 0;
			if (columnasMinAlt == 3) {
			    fila += " ss:MergeAcross=\"4\"";
			    index = 10;
			} else {
			    fila += " ss:MergeAcross=\"5\"";
			    index = 11;
			}
			fila += " ss:StyleID=\"s27\"/>\n";
			for (int i = index; i < (index + columnasMinAlt - 1); i++) {
			    fila += "    <Cell ss:Index=\"" + i + "\" ss:StyleID=\"s28\"/>\n";
			}
			fila += "    <Cell ss:Index=\"" + (index + columnasMinAlt) + "\" ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n";
		    }
		    fila += "  </Row>\n";
		} else {
		    fila += "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/>\n" + "    <Cell ss:Index=\"5\"";
		    int index = 11;
		    if (columnasMinAlt != 0) {
			// Uno de los dos es igual a cero
			fila += " ss:MergeAcross=\"5\"";
		    } else {
			fila += " ss:MergeAcross=\"7\"";
			index = 13;
		    }
		    fila += " ss:StyleID=\"s27\"/>\n";
		    for (int i = index; i < (index + columnasMinAlt); i++) {
			fila += "    <Cell ss:Index=\"" + i + "\" ss:StyleID=\"s28\"/>\n";
		    }
		    fila += "    <Cell ss:Index=\"" + (index + columnasMinAlt) + "\" ss:StyleID=\"s28\"/>\n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "    <Cell ss:StyleID=\"s28\"/> \n" + "  </Row>\n";
		}
	    }
	    /***************** ABAJO DE LA TABLA***************/
	    fila += "  <Row>\n";
	    int cantColBase = 13;
	    for (int i = 1; i <= (cantColBase + columnasMinAlt); i++) {
		fila += "    <Cell ss:Index=\"" + i + "\" ss:StyleID=\"s29\"/>\n";
	    }
	    fila += "  </Row>\n";
	}
	write(fila);
    }

    private String FinTabla() {
	String fintabla = "  </Table>\n" + "<!-- Fin de la tabla -->\n";
	return fintabla;
    }
    /*  private String formatear(String _cadena)
  {
    return _cadena.replaceAll("ï¿½,"a").replaceAll("ï¿½,"e").replaceAll("ï¿½,"i").replaceAll("ï¿½,"o").replaceAll("","u")
                  .replaceAll("ï¿½,"A").replaceAll("ï¿½,"E").replaceAll("ï¿½,"I").replaceAll("ï¿½,"O").replaceAll("ï¿½,"U")
                  .replaceAll("ï¿½,"n").replaceAll("ï¿½,"N").replaceAll("ï¿½,"U").replaceAll("","u")
                  .replaceAll("","o").replaceAll("","a");
  }
*/

    public String getEncabezadoReporte() {
	return EncabezadoReporte;
    }

    public void setEncabezadoReporte(String _EncabezadoReporte) {
	EncabezadoReporte = _EncabezadoReporte;
    }
    /*private String writeFoto(int indice)
  {
    try
    {
      ResultSet rs = org.digitall.lib.sql.LibSQL.exQuery("SELECT foto, ancho, alto from fotos where idfoto = " + (indice+1));
      if (rs != null)
      {
        write("<w:pict><w:binData w:name=\"wordml://" + indice + ".jpg\">");

        while(rs.next())
        {
          byte[] imgBytes =rs.getBytes(1);
          write(new String(Base64Coder.encode(imgBytes)));
        }

        write("</w:binData><v:shape id=\"_x0000_i1025\" type=\"#_x0000_t75\" style=\"width:"
        + rs.getString(2) + "pt" + ";height:" + rs.getString(3) + "pt\">" +
        "<v:imagedata src=\"wordml://" + indice + ".jpg\" o:title=\"Foto\"/>" +
        "</v:shape></w:pict>");
        rs.close();
      }
    } catch (Exception x)
    {
      org.digitall.lib.components.Advisor.messageBox("Error al obtener la Foto","Error");
      x.printStackTrace();
    }
    return "";
  }*/

}
