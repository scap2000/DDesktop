package org.digitall.apps.legalprocs.calendar.reports;

import java.io.FileWriter;
import java.io.IOException;

public class XMLBookApplications {

    private FileWriter xmlFile;
    private String xmlPath = "";

    public XMLBookApplications() {
	//xmlPath = Proced.getRuta() + File.separator + "informes" + File.separator +"form_ley_7141.xml";
	/** REVISAR PARA ELIMINAR EL METODO getRuta() */
    }

    private void xmlWrite(String _section) {
	try {
	    xmlFile.write(_section);
	} catch (IOException x) {
	    org.digitall.lib.components.Advisor.messageBox("Error de E/S", "Error");
	    x.printStackTrace();
	}
    }

    private String headerSection() {
	StringBuffer header = new StringBuffer();
	header.append("<?xml version=\"1.0\"?>\n");
	header.append("<?mso-application progid=\"Excel.Sheet\"?>\n");
	header.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
	header.append(" xmlns:o=\"urn:schemas-microsoft-com:office:office\"\n");
	header.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\n");
	header.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\n");
	header.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
	return header.toString();
    }

    private String documentProperties() {
	StringBuffer properties = new StringBuffer();
	properties.append("<DocumentProperties xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
	properties.append("  <Title>Formularios Anexos</Title>\n");
	properties.append("  <Subject>Codigo de Procedimientos</Subject>\n");
	properties.append("  <Author>E. Catalano </Author>\n");
	properties.append("  <Description>SF</Description>\n");
	properties.append("  <LastAuthor>DIGITALL</LastAuthor>\n");
	properties.append("  <LastPrinted>2006-10-13T23:14:39Z</LastPrinted>\n");
	properties.append("  <Created>1999-06-29T18:48:24Z</Created>\n");
	properties.append("  <LastSaved>2006-10-13T23:18:13Z</LastSaved>\n");
	properties.append("  <Version>11.6360</Version>\n");
	properties.append("</DocumentProperties>");
	return properties.toString();
    }

    private String documentSettings() {
	StringBuffer settings = new StringBuffer();
	settings.append("<OfficeDocumentSettings xmlns=\"urn:schemas-microsoft-com:office:office\">\n");
	settings.append(colorsSection());
	settings.append("</OfficeDocumentSettings>\n");
	return settings.toString();
    }

    private String colorsSection() {
	StringBuffer colors = new StringBuffer();
	colors.append("<Colors>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>16</Index>\n");
	colors.append("    <RGB>#8080FF</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>17</Index>\n");
	colors.append("    <RGB>#802060</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>18</Index>\n");
	colors.append("    <RGB>#FFFFC0</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>19</Index>\n");
	colors.append("    <RGB>#A0E0E0</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>20</Index>\n");
	colors.append("    <RGB>#600080</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>22</Index>\n");
	colors.append("    <RGB>#0080C0</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>23</Index>\n");
	colors.append("    <RGB>#C0C0FF</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>33</Index>\n");
	colors.append("    <RGB>#69FFFF</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>36</Index>\n");
	colors.append("    <RGB>#A6CAF0</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>37</Index>\n");
	colors.append("    <RGB>#CC9CCC</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>39</Index>\n");
	colors.append("    <RGB>#E3E3E3</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>42</Index>\n");
	colors.append("    <RGB>#339933</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>43</Index>\n");
	colors.append("    <RGB>#999933</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>44</Index>\n");
	colors.append("    <RGB>#996633</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>45</Index>\n");
	colors.append("    <RGB>#996666</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>48</Index>\n");
	colors.append("    <RGB>#3333CC</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>49</Index>\n");
	colors.append("    <RGB>#336666</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>52</Index>\n");
	colors.append("    <RGB>#663300</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("   <Color>\n");
	colors.append("    <Index>55</Index>\n");
	colors.append("    <RGB>#424242</RGB>\n");
	colors.append("   </Color>\n");
	colors.append("</Colors>\n");
	return colors.toString();
    }

    private String ExcelWorkbook() {
	StringBuffer workBook = new StringBuffer();
	workBook.append("<ExcelWorkbook xmlns=\"urn:schemas-microsoft-com:office:excel\">\n");
	workBook.append("  <WindowHeight>7260</WindowHeight>\n");
	workBook.append("  <WindowWidth>9720</WindowWidth>\n");
	workBook.append("  <WindowTopX>240</WindowTopX>\n");
	workBook.append("  <WindowTopY>75</WindowTopY>\n");
	workBook.append("  <TabRatio>863</TabRatio>\n");
	workBook.append("  <AcceptLabelsInFormulas/>\n");
	workBook.append("  <ProtectStructure>False</ProtectStructure>\n");
	workBook.append("  <ProtectWindows>False</ProtectWindows>\n");
	workBook.append("</ExcelWorkbook>\n");
	return workBook.toString();
    }

    private String styleSection() {
	StringBuffer styles = new StringBuffer();
	styles.append("");
	return styles.toString();
    }

}
