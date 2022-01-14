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
 * F1.java
 *
 * */
package org.digitall.projects.unsa.censodocpri.classes;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.Collection;
import java.util.Vector;

import org.digitall.lib.components.Advisor;
import org.digitall.lib.sql.LibSQL;

public class F1 {

    private int idEncuesta = -1;
    private String escuela = "";

    //Pregunta 1
    private String f1q1Label = "1. Piense en una idea importante para la enseñanza de"
	    + " la ciencia que usted aprendió en su vida profesional: ¿Cuál es esa idea?";
    private String f1q1 = "";
    private String f1q1OpcionesLabel = "¿Dónde y cómo la aprendió? Puede marcar más de una respuesta";
    private Vector<OptionItem> f1q1Opciones = new Vector<OptionItem>();
    
    //Pregunta 2
    private String f1q2Label = "2. Ordene jerárquicamente la importancia de los siguientes"
	    + " 3 objetivos para la enseñanza de las ciencias:";
    private String f1q2aLabel = "Educación en Ciencias para la formación de científicos";
    private String f1q2bLabel = "Educación en Ciencias para la formación del pensamiento crítico";
    private String f1q2cLabel = "Educación en Ciencias para la participación en sociedad (ciencia para todos)";
    private String f1q2 = f1q2aLabel + "|" + f1q2bLabel + "|" + f1q2cLabel; //formato a|b|c opciones separadas por "pipe" para usar split()
    private String f1q2ComentarioLabel = "Por favor, comente";
    private String f1q2Comentario = ""; //formato a,b,c opciones separadas por comas para usar split()
    private String f1q2OpinionLabel = "¿Cuántos de sus colegas cree usted que concordarían con sus respuestas?";
    private String[] f1q2OpinionOpciones = new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error"};
    private String f1q2Opinion = "";

    //Pregunta 3
    private String f1q3Label = "3. ¿En qué medida afecta a su práctica educativa la interacción"
	    + " con los colegas de su escuela?";
    private String[] f1q3Opciones = new String[] {"en nada", "apenas", "mucho", "necesariamente afecta", "error"};
    private String f1q3 = "";
    
    //Pregunta 4
    private String f1q4Label = "4. ¿En qué medida cree usted que su práctica educacional contribuye"
	    + " en la de sus colegas?";
    private String[] f1q4Opciones = new String[] {"en nada", "apenas", "mucho", "necesariamente afecta", "error"};
    private String f1q4 = "";
    
    //Pregunta 5
    private String f1q5Label = "5. ¿Usted cree que ha habido buenas o malas iniciativas nacionales"
	    + " acerca de la enseñanza de las ciencias en los últimos años? ¿Cuáles?";
    private String f1q5 = "";

    //Pregunta 6
    private String f1q6Label = "6. ¿De qué manera influencian positivamente o negativamente en su enseñanza"
	    + " cada uno de los siguientes factores? Marque con una cruz la columna correspondiente.";
    private Vector<TableItem> f1q6 = new Vector<TableItem>();

    //Pregunta 7
    private String f1q7Label = "7. ¿Ha participado usted de algún programa, o de alguna innovación, ha tenido algún"
	    + " contacto con la investigación educativa en ciencias, etc.?";
    private String f1q7aLabel = "¿Qué fue lo positivo de esta experiencia (para los docentes, los estudiantes, la escuela)?"
	    + " Por favor describa";
    private String f1q7a = "";
    private String f1q7bLabel = "¿Qué fue lo negativo de esta experiencia? Por favor describa";
    private String f1q7b = "";
    
    //Pregunta 8
    private String f1q8Label = "8. ¿Cómo cree usted que son las siguientes acciones a los fines de mejorar"
	    + " la enseñanza y el aprendizaje de la ciencia en la escuela? Marque con una cruz"
	    + " la columna correspondiente";
    private Vector<TableItem> f1q8 = new Vector<TableItem>();
    private String f1q8OpinionLabel = "¿Cuántos de sus colegas cree usted que estarían de acuerdo con su respuesta?";
    private String[] f1q8OpinionOpciones = new String[] {"pocos", "una buena parte", "la mayoría", "casi todos", "error"};
    private String f1q8Opinion = "";

    //Pregunta 9
    private String f1q9Label = "9. Cuando usted no se siente satisfecho con algún aspecto de su enseñanza,"
	    + " ¿dónde busca ideas para mejorar su práctica? (puede marcar más de una opción)";
    private Vector<OptionItem> f1q9Opciones = new Vector<OptionItem>();

    //Pregunta 10
    private String f1q10Label = "10. Si en una determinada clase usted observa que una actividad en particular"
	    + " ha involucrado más a los niños que a las niñas (o viceversa), ¿considera la posibilidad de"
	    + " diseñar una nueva actividad sobre el mismo tema a fin de involucrar más al otro grupo?";
    private String[] f1q10Opciones = new String[] {"Sí", "No" , "En general no observo tales diferencias", "error"};
    private String f1q10 = "";
    
    //Pregunta 11
    private String f1q11Label = "11. Cuando usted piensa temas a proponer para llamar el interés de sus alumnos,"
	    + " ¿considera que usted debería trabajar alguno de ellos mejor con los niños que con las niñas"
	    + " o viceversa? ¿Podría dar algunos ejemplos?";
    private String f1q11 = "";
        
    //Pregunta 12
    private String f1q12Label = "12. ¿Cree usted que la forma en que se lleva a cabo una determinada actividad"
	    + " (por ejemplo en grupos o individual) puede involucrar a los niños más que a las niñas? Comente";
    private String[] f1q12Opciones = new String[] {"Sí", "No", "No sabe/No contesta", "error"};
    private String f1q12 = "";
    private String f1q12Comentario = "";
	
    //Pregunta 13
    private String f1q13Label = "13. ¿Cómo se siente respecto de su vida profesional?";
    private Vector<TableItem> f1q13 = new Vector<TableItem>();
    private String f1q13aLabel = "Si satisfecho, ¿de dónde viene esta satisfacción? (dé ejemplos)";
    private String f1q13a = "";
    private String f1q13bLabel = "Si insatisfecho, ¿qué sería necesario para cambiar la situación?";
    private String f1q13b = "";

    //Pregunta 14
    private String f1q14Label = "14. ¿Qué cree usted que los estudiantes de sus clases recordarían como una"
	    + " particularmente buena experiencia de aprendizaje?";
    private String f1q14 = "";
    
    public F1() {
	this(-1);
    }

    public F1(int _idencuesta) {
	idEncuesta = _idencuesta;
	f1q1Opciones.add(new OptionItem("libros"));
	f1q1Opciones.add(new OptionItem("revistas (de divulgación)"));
	f1q1Opciones.add(new OptionItem("artículos (o eventos) científicos"));
	f1q1Opciones.add(new OptionItem("documentos (oficiales)"));
	f1q1Opciones.add(new OptionItem("internet"));
	f1q1Opciones.add(new OptionItem("formación docente"));
	f1q1Opciones.add(new OptionItem("experiencia docente"));
	f1q1Opciones.add(new OptionItem("interacción con colegas"));
	f1q1Opciones.add(new OptionItem("vida diaria"));

	f1q6.add(new TableItem("Didáctica", "Libro de texto"));
	f1q6.add(new TableItem("Didáctica", "Forma de las pruebas de evaluación"));
	f1q6.add(new TableItem("Didáctica", "Lo que los estudiantes parecen haber aprendido"));
	f1q6.add(new TableItem("Profesionales", "Contribuciones de la formación"));
	f1q6.add(new TableItem("Profesionales", "Dominio del tema"));
	f1q6.add(new TableItem("Factores socio-culturales", "Nivel socio-cultural de los estudiantes"));
	f1q6.add(new TableItem("Factores socio-culturales", "Constitución del grupo familiar de los estudiantes"));
	f1q6.add(new TableItem("Relaciones Interpersonales", "Interacción con los colegas"));
	f1q6.add(new TableItem("Relaciones Interpersonales", "Interacción con los directivos"));

	f1q8.add(new TableItem("Estructurales", "Cambiar las exigencias oficiales"));
	f1q8.add(new TableItem("Estructurales", "Aumentar los recursos (materiales y financieros) de las escuelas"));
	f1q8.add(new TableItem("Estructurales", "Construir laboratorios especiales"));
	f1q8.add(new TableItem("Estructurales", "Asegurar conexiones seguras a internet"));
	f1q8.add(new TableItem("Estructurales", "Cambiar los criterios para la selección de los docentes"));
	f1q8.add(new TableItem("Profesionales", "Cambiar la formación (inicial y contínua)"));
	f1q8.add(new TableItem("Profesionales", "Reorganizar el trabajo docente"));
	f1q8.add(new TableItem("Profesionales", "Involucrar agentes extra-escolares en la práctica educativa"));
	f1q8.add(new TableItem("Profesionales", "Intercambiar ideas con colegas acerca de las acciones exitosas o no en el aula"));
	f1q8.add(new TableItem("Profesionales", "Vincular la práctica docente con los resultados de la investigación educativa"));
	f1q8.add(new TableItem("Didácticas", "Desarrollar nuevos materiales didácticos"));
	f1q8.add(new TableItem("Didácticas", "Cambiar los criterios de evaluación del aprendizaje"));

	f1q9Opciones.add(new OptionItem("libros"));
	f1q9Opciones.add(new OptionItem("revistas (de divulgación)"));
	f1q9Opciones.add(new OptionItem("artículos (o eventos) científicos"));
	f1q9Opciones.add(new OptionItem("documentos (oficiales)"));
	f1q9Opciones.add(new OptionItem("internet"));
	f1q9Opciones.add(new OptionItem("formación docente"));
	f1q9Opciones.add(new OptionItem("experiencia docente"));
	f1q9Opciones.add(new OptionItem("interacción con colegas"));
	f1q9Opciones.add(new OptionItem("vida diaria"));

	f1q13.add(new TableItem("Desde el punto de vista intelectual", ""));
	f1q13.add(new TableItem("Desde el punto de vista personal", ""));
	f1q13.add(new TableItem("Desde el punto de vista social", ""));
	f1q13.add(new TableItem("Desde el punto de vista económico", ""));
    }

    public int getIdEncuesta() {
	return idEncuesta;
    }

    public void setEscuela(String escuela) {
	this.escuela = escuela;
    }

    public String getEscuela() {
	return escuela;
    }

    public String getF1q1Label() {
	return f1q1Label;
    }

    public void setF1q1(String f1q1) {
	this.f1q1 = f1q1;
    }

    public String getF1q1() {
	return f1q1;
    }

    public String getF1q1OpcionesLabel() {
	return f1q1OpcionesLabel;
    }

    public Vector<OptionItem> getF1q1Opciones() {
	return f1q1Opciones;
    }

    public String getF1q2Label() {
	return f1q2Label;
    }

    public String getF1q2aLabel() {
	return f1q2aLabel;
    }

    public String getF1q2bLabel() {
	return f1q2bLabel;
    }

    public String getF1q2cLabel() {
	return f1q2cLabel;
    }

    public void setF1q2(String f1q2) {
	this.f1q2 = f1q2;
    }

    public String getF1q2() {
	return f1q2;
    }

    public String getF1q2ComentarioLabel() {
	return f1q2ComentarioLabel;
    }

    public void setF1q2Comentario(String f1q2Comentario) {
	this.f1q2Comentario = f1q2Comentario;
    }

    public String getF1q2Comentario() {
	return f1q2Comentario;
    }

    public String getF1q2OpinionLabel() {
	return f1q2OpinionLabel;
    }

    public String[] getF1q2OpinionOpciones() {
	return f1q2OpinionOpciones;
    }

    public void setF1q2Opinion(String f1q2Opinion) {
	this.f1q2Opinion = f1q2Opinion;
    }

    public String getF1q2Opinion() {
	return f1q2Opinion;
    }

    public String getF1q3Label() {
	return f1q3Label;
    }

    public String[] getF1q3Opciones() {
	return f1q3Opciones;
    }

    public void setF1q3(String f1q3) {
	this.f1q3 = f1q3;
    }

    public String getF1q3() {
	return f1q3;
    }

    public String getF1q4Label() {
	return f1q4Label;
    }

    public String[] getF1q4Opciones() {
	return f1q4Opciones;
    }

    public void setF1q4(String f1q4) {
	this.f1q4 = f1q4;
    }

    public String getF1q4() {
	return f1q4;
    }

    public String getF1q5Label() {
	return f1q5Label;
    }

    public void setF1q5(String f1q5) {
	this.f1q5 = f1q5;
    }

    public String getF1q5() {
	return f1q5;
    }

    public String getF1q6Label() {
	return f1q6Label;
    }

    public Vector<TableItem> getF1q6() {
	return f1q6;
    }

    public String getF1q7Label() {
	return f1q7Label;
    }

    public String getF1q7aLabel() {
	return f1q7aLabel;
    }

    public void setF1q7a(String f1q7a) {
	this.f1q7a = f1q7a;
    }

    public String getF1q7a() {
	return f1q7a;
    }

    public String getF1q7bLabel() {
	return f1q7bLabel;
    }

    public void setF1q7b(String f1q7b) {
	this.f1q7b = f1q7b;
    }

    public String getF1q7b() {
	return f1q7b;
    }

    public String getF1q8Label() {
	return f1q8Label;
    }

    public Vector<TableItem> getF1q8() {
	return f1q8;
    }

    public String getF1q8OpinionLabel() {
	return f1q8OpinionLabel;
    }

    public String[] getF1q8OpinionOpciones() {
	return f1q8OpinionOpciones;
    }

    public void setF1q8Opinion(String f1q8Opinion) {
	this.f1q8Opinion = f1q8Opinion;
    }

    public String getF1q8Opinion() {
	return f1q8Opinion;
    }

    public String getF1q9Label() {
	return f1q9Label;
    }

    public Vector<OptionItem> getF1q9Opciones() {
	return f1q9Opciones;
    }

    public String getF1q10Label() {
	return f1q10Label;
    }

    public String[] getF1q10Opciones() {
	return f1q10Opciones;
    }

    public void setF1q10(String f1q10) {
	this.f1q10 = f1q10;
    }

    public String getF1q10() {
	return f1q10;
    }

    public String getF1q11Label() {
	return f1q11Label;
    }

    public void setF1q11(String f1q11) {
	this.f1q11 = f1q11;
    }

    public String getF1q11() {
	return f1q11;
    }

    public String getF1q12Label() {
	return f1q12Label;
    }

    public String[] getF1q12Opciones() {
	return f1q12Opciones;
    }

    public void setF1q12(String f1q12) {
	this.f1q12 = f1q12;
    }

    public String getF1q12() {
	return f1q12;
    }

    public void setF1q12Comentario(String f1q12Comentario) {
	this.f1q12Comentario = f1q12Comentario;
    }

    public String getF1q12Comentario() {
	return f1q12Comentario;
    }

    public String getF1q13Label() {
	return f1q13Label;
    }

    public Vector<TableItem> getF1q13() {
	return f1q13;
    }

    public String getF1q13aLabel() {
	return f1q13aLabel;
    }

    public void setF1q13a(String f1q10a) {
	this.f1q13a = f1q10a;
    }

    public String getF1q13a() {
	return f1q13a;
    }

    public String getF1q13bLabel() {
	return f1q13bLabel;
    }

    public void setF1q13b(String f1q10b) {
	this.f1q13b = f1q10b;
    }

    public String getF1q13b() {
	return f1q13b;
    }

    public String getF1q14Label() {
	return f1q14Label;
    }

    public void setF1q14(String f1q11) {
	this.f1q14 = f1q11;
    }

    public String getF1q14() {
	return f1q14;
    }

    
    public int saveData() {
	int _idEncuesta = -1;
	StringBuilder params = new StringBuilder("'" + escuela + "','" + f1q1 + "','");
	for (int i = 0; i < f1q1Opciones.size()-1; i++) {
	    params.append(f1q1Opciones.elementAt(i).getOpcion() + "&" + f1q1Opciones.elementAt(i).isSeleccionada() + "|");
	}
	params.append(f1q1Opciones.elementAt(f1q1Opciones.size()-1).getOpcion() + "&" + f1q1Opciones.elementAt(f1q1Opciones.size()-1).isSeleccionada() + "','");

	params.append(f1q2 + "','" + f1q2Comentario + "','" + f1q2Opinion + "','");
	params.append(f1q3 + "','");
	params.append(f1q4 + "','");
	params.append(f1q5 + "','");

	for (int i = 0; i < f1q6.size()-1; i++) {
	    params.append(f1q6.elementAt(i).getCategoria() + "&" + f1q6.elementAt(i).getSubcategoria() + "&" + f1q6.elementAt(i).getOpcion() + "|");
	}
	params.append(f1q6.elementAt(f1q6.size()-1).getCategoria() + "&" + f1q6.elementAt(f1q6.size()-1).getSubcategoria() + "&" + f1q6.elementAt(f1q6.size()-1).getOpcion() + "','");
	params.append(f1q7a + "','" + f1q7b + "','");

	for (int i = 0; i < f1q8.size()-1; i++) {
	    params.append(f1q8.elementAt(i).getCategoria() + "&" + f1q8.elementAt(i).getSubcategoria() + "&" + f1q8.elementAt(i).getOpcion() + "|");
	}
	params.append(f1q8.elementAt(f1q8.size()-1).getCategoria() + "&" + f1q8.elementAt(f1q8.size()-1).getSubcategoria() + "&" + f1q8.elementAt(f1q8.size()-1).getOpcion() + "','");
	params.append(f1q8Opinion + "','");

	for (int i = 0; i < f1q9Opciones.size()-1; i++) {
	    params.append(f1q9Opciones.elementAt(i).getOpcion() + "&" + f1q9Opciones.elementAt(i).isSeleccionada() + "|");
	}
	params.append(f1q9Opciones.elementAt(f1q9Opciones.size()-1).getOpcion() + "&" + f1q9Opciones.elementAt(f1q9Opciones.size()-1).isSeleccionada() + "','");
	
	params.append(f1q10 + "','");
	params.append(f1q11 + "','");
	params.append(f1q12 + "|" + f1q12Comentario + "','");

	for (int i = 0; i < f1q13.size()-1; i++) {
	    params.append(f1q13.elementAt(i).getCategoria() + "&" + f1q13.elementAt(i).getSubcategoria() + "&" + f1q13.elementAt(i).getOpcion() + "|");
	}
	params.append(f1q13.elementAt(f1q13.size()-1).getCategoria() + "&" + f1q13.elementAt(f1q13.size()-1).getSubcategoria() + "&" + f1q13.elementAt(f1q13.size()-1).getOpcion() + "','");
	params.append(f1q13a + "','");
	params.append(f1q13b + "','");
	params.append(f1q14 + "'");

	idEncuesta = LibSQL.getInt("unsa.setcensodocpri", idEncuesta + "," + params.toString());
	_idEncuesta = idEncuesta;
	return _idEncuesta;
    }
    
    public void retrieveData() {
	if (idEncuesta != -1) {
	    ResultSet _encuesta = LibSQL.exFunction("unsa.getallcensodocpri", idEncuesta, 0, 0);
	    try {
		if (_encuesta.next()) {
		    escuela = _encuesta.getString("escuela");
		    f1q1 = _encuesta.getString("f1q1");
		    String[] _f1q1Opciones = _encuesta.getString("f1q1opciones").split("\\|");
		    f1q1Opciones.removeAllElements();
		    for (int i = 0; i < _f1q1Opciones.length; i++) {
			f1q1Opciones.add(new OptionItem(_f1q1Opciones[i].split("&")[0], new Boolean(_f1q1Opciones[i].split("&")[1])));
		    }
		    f1q2 = _encuesta.getString("f1q2");
		    f1q2Comentario = _encuesta.getString("f1q2comentario");
		    f1q2Opinion = _encuesta.getString("f1q2opinion");
		    f1q3 = _encuesta.getString("f1q3");
		    f1q4 = _encuesta.getString("f1q4");
		    f1q5 = _encuesta.getString("f1q5");

		    String[] _f1q6 = _encuesta.getString("f1q6").split("\\|");
		    f1q6.removeAllElements();
		    for (int i = 0; i < _f1q6.length; i++) {
			String[] _fields = _f1q6[i].split("&",3);
			f1q6.add(new TableItem(_fields[0], _fields[1], _fields[2]));
		    }

		    f1q7a = _encuesta.getString("f1q7a");
		    f1q7b = _encuesta.getString("f1q7b");

		    String[] _f1q8 = _encuesta.getString("f1q8").split("\\|");
		    f1q8.removeAllElements();
		    for (int i = 0; i < _f1q8.length; i++) {
		        String[] _fields = _f1q8[i].split("&",3);
		        f1q8.add(new TableItem(_fields[0], _fields[1], _fields[2]));
		    }
		    f1q8Opinion = _encuesta.getString("f1q8opinion");

		    String[] _f1q9Opciones = _encuesta.getString("f1q9opciones").split("\\|");
		    f1q9Opciones.removeAllElements();
		    for (int i = 0; i < _f1q9Opciones.length; i++) {
		        String[] _fields = _f1q9Opciones[i].split("&",2);
		        f1q9Opciones.add(new OptionItem(_fields[0], new Boolean(_fields[1])));
		    }
		    
		    f1q10 = _encuesta.getString("f1q10");
		    f1q11 = _encuesta.getString("f1q11");
		    f1q12 = _encuesta.getString("f1q12").split("\\|",2)[0];
		    f1q12Comentario = _encuesta.getString("f1q12").split("\\|",2)[1];

		    String[] _f1q13 = _encuesta.getString("f1q13").split("\\|");
		    f1q13.removeAllElements();
		    for (int i = 0; i < _f1q13.length; i++) {
		        String[] _fields = _f1q13[i].split("&",3);
		        f1q13.add(new TableItem(_fields[0], _fields[1], _fields[2]));
		    }
		    f1q13a = _encuesta.getString("f1q13a");
		    f1q13b = _encuesta.getString("f1q13b");
		    f1q14 = _encuesta.getString("f1q14");
		} else {
		    idEncuesta = -1;
		}
	    } catch (SQLException e) {
		Advisor.printException(e);
	    }
	}
    }
}

