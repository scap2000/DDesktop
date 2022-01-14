package org.digitall.apps.legalprocs.calendar.reports;

import java.io.FileWriter;
import java.io.IOException;

//

public class XMLSheetExplorationRequests {

    private FileWriter xmlFile;

    public XMLSheetExplorationRequests(FileWriter _xmlFile) {
	xmlFile = _xmlFile;
    }

    private void xmlWrite(String _section) {
	try {
	    xmlFile.write(_section);
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error");
	    x.printStackTrace();
	}
    }

    private String headerSheet() {
	StringBuffer header = new StringBuffer();
	header.append("<Worksheet ss:Name=\"PERMEXPL\">\n");
	header.append(nameSheet());
	header.append(tableSheet());
	header.append(sheetOptions());
	header.append(pageBreaksSheet());
	header.append("</Worksheet>\n");
	return header.toString();
    }

    private String nameSheet() {
	StringBuffer name = new StringBuffer();
	name.append("<Names>\n");
	name.append("   <NamedRange ss:Name=\"Print_Area\" ss:RefersTo=\"=PERMEXPL!R1C1:R158C35\"/>\n");
	name.append("   <NamedRange ss:Name=\"Print_Titles\" ss:RefersTo=\"=PERMEXPL!R1:R2\"/>\n");
	name.append("</Names>\n");
	return name.toString();
    }

    private String sheetOptions() {
	StringBuffer options = new StringBuffer();
	options.append("<WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\n");
	options.append("   <PageSetup>\n");
	options.append("    <Layout x:CenterHorizontal=\"1\"/>\n");
	options.append("    <Header x:Margin=\"0.39370078740157483\"/>\n");
	options.append("    <Footer x:Margin=\"3.937007874015748E-2\"/>\n");
	options.append("    <PageMargins x:Bottom=\"0.78740157480314965\" x:Left=\"1.5748031496062993\"\n");
	options.append("     x:Right=\"0.39370078740157483\" x:Top=\"1.5748031496062993\"/>\n");
	options.append("   </PageSetup>\n");
	options.append("   <Print>\n");
	options.append("    <ValidPrinterInfo/>\n");
	options.append("    <PaperSizeIndex>9</PaperSizeIndex>\n");
	options.append("    <Scale>85</Scale>\n");
	options.append("    <HorizontalResolution>600</HorizontalResolution>\n");
	options.append("    <VerticalResolution>-4</VerticalResolution>\n");
	options.append("   </Print>\n");
	options.append("   <Selected/>\n");
	options.append("   <ProtectObjects>False</ProtectObjects>\n");
	options.append("   <ProtectScenarios>False</ProtectScenarios>\n");
	options.append("</WorksheetOptions>");
	return options.toString();
    }

    private String pageBreaksSheet() {
	StringBuffer page = new StringBuffer();
	page.append("<PageBreaks xmlns=\"urn:schemas-microsoft-com:office:excel\">\n");
	page.append("   <RowBreaks>\n");
	page.append("    <RowBreak>\n");
	page.append("     <Row>55</Row>\n");
	page.append("     <ColEnd>34</ColEnd>\n");
	page.append("    </RowBreak>\n");
	page.append("    <RowBreak>\n");
	page.append("     <Row>112</Row>\n");
	page.append("     <ColEnd>34</ColEnd>\n");
	page.append("    </RowBreak>\n");
	page.append("   </RowBreaks>\n");
	page.append("</PageBreaks>\n");
	return page.toString();
    }

    private String tableSheet() {
	StringBuffer table = new StringBuffer();
	table.append("<Table ss:ExpandedColumnCount=\"35\" ss:ExpandedRowCount=\"158\" x:FullColumns=\"1\"\n");
	table.append("   x:FullRows=\"1\" ss:StyleID=\"s89\" ss:DefaultColumnWidth=\"14.25\">\n");
	table.append("   <Column ss:Index=\"7\" ss:StyleID=\"s89\" ss:Width=\"15.75\"/>\n");
	table.append("   <Column ss:Index=\"29\" ss:StyleID=\"s89\" ss:Width=\"15.75\"/>\n");
	/** ROWS **/
	table.append("</Table>\n");
	return table.toString();
    }

    private String rowSpace() {
	StringBuffer row = new StringBuffer();
	row.append("<Row ss:Height=\"15\" ss:StyleID=\"styleDefault\">\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:Index=\"35\" ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("</Row>");
	return row.toString();
    }

    private String rowFileData(String _fileNumber, String _fileLetter, String _fileYear) {
	StringBuffer row = new StringBuffer();
	row.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"11.625\" ss:StyleID=\"styleDefault\">\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	char[] fileNumber = _fileNumber.toCharArray();
	row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">Nº DE EXPEDIENTE</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:Index=\"9\" ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileNumber[0] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	for (int i = 1; i < fileNumber.length; i++) {
	    row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileNumber[i] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	}
	row.append("    <Cell ss:Index=\"17\" ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	char[] fileLetter = _fileLetter.toCharArray();
	row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">LETRA</Data><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:Index=\"21\" ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileLetter[0] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	for (int i = 1; i < fileLetter.length; i++) {
	    row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileLetter[i] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	}
	row.append("    <Cell ss:Index=\"24\" ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	char[] fileYear = _fileYear.toCharArray();
	row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">AÑO</Data><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:Index=\"28\" ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileYear[0] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	for (int i = 1; i < fileYear.length; i++) {
	    row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">" + fileYear[i] + "</Data><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	}
	row.append("    <Cell ss:Index=\"35\" ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("</Row>");
	return row.toString();
    }

    private String rowTitle2() {
	StringBuffer row = new StringBuffer();
	row.append("");
	return row.toString();
    }

    private String rowTitle() {
	StringBuffer row = new StringBuffer();
	row.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"18.75\" ss:StyleID=\"styleDefault\">\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">SOLICITUD DE PERMISO DE EXPLORACION</Data><NamedCell\n");
	row.append("      ss:Name=\"Print_Titles\"/><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("   </Row>");
	return row.toString();
    }

    private String rowSubTitle() {
	StringBuffer row = new StringBuffer();
	row.append("<Row ss:AutoFitHeight=\"0\" ss:Height=\"13.5\" ss:StyleID=\"styleDefault\">\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><Data ss:Type=\"String\">ORIGINAL y 2 COPIAS</Data><NamedCell\n");
	row.append("      ss:Name=\"Print_Titles\"/><NamedCell ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("    <Cell ss:Index=\"35\" ss:StyleID=\"styleDefault\"><NamedCell ss:Name=\"Print_Titles\"/><NamedCell\n");
	row.append("      ss:Name=\"Print_Area\"/></Cell>\n");
	row.append("</Row>\n");
	return row.toString();
    }

}
