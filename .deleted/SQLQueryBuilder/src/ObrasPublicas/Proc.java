package ObrasPublicas;

import java.util.Vector;

import java.sql.*;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

import java.text.SimpleDateFormat;

import java.util.Date;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.Component;

import javax.swing.JOptionPane;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.JSplitPane;

import java.util.Enumeration;

import javax.swing.JTextField;

import java.util.Timer;
import java.util.TimerTask;

import java.text.ParseException;

import java.net.UnknownHostException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Proc {

    /*  static String  SQLDriver="com.mysql.jdbc.Driver";
  static String Database="jdbc:mysql://localhost/ccm";*/
    static String SQLDriver = "org.postgresql.Driver";
    static String Database = "jdbc:postgresql://192.168.2.21/master";
    static String Esquema = "gis.";
    /*  static String TablaCatastros = "catastros"; //por ahora, debería ser catastros en realidad*/
    static String SQLUser = "cepax";
    static String SQLPass = "cepax";
    static Vector vectorcombo, contenidocombo;
    static JTextField jthora;

    public Proc() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {

    }

    static ResultSet exConsulta(Connection Conx, String Query) throws SQLException {
	Statement Stat = Conx.createStatement();
	ResultSet Resul1 = Stat.executeQuery(Query);
	return Resul1;
    }

    static Connection CCon() throws SQLException {
	try {
	    Class.forName(SQLDriver);
	} catch (ClassNotFoundException x) {

	}
	return DriverManager.getConnection(Database, SQLUser, SQLPass);
    }

    static boolean exActualizar(char ch, String Query) {
	//  try {Class.forName(SQLDriver);} catch (ClassNotFoundException x) {}
	boolean bol = true;
	String accion = "";
	try {
	    Connection Conx = Proc.CCon();
	    Statement Stat = Conx.createStatement();
	    if (ch == 'a') {
		accion = " insertar ";
		Stat.executeUpdate(Query);
	    } else if (ch == 'b') {
		int result = JOptionPane.showConfirmDialog((Component)null, "¿Está seguro que desea eliminar el registro?", "Eliminación", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
		    accion = " eliminar ";
		    Stat.executeUpdate(Query);
		} else
		    bol = false;
	    } else if (ch == 'm') {
		int result = JOptionPane.showConfirmDialog((Component)null, "¿Está seguro que desea guardar los cambios?", "Actualización", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
		    accion = " actualizar ";
		    Stat.executeUpdate(Query);
		} else
		    bol = false;
	    }
	    Conx.close();
	    return bol;

	} catch (SQLException x) {
	    String msg = "Ha ocurrido el siguiente error al" + accion + "el registro\n" + x.getMessage();
	    JOptionPane.showMessageDialog((Component)null, msg, "Error", JOptionPane.OK_OPTION);
	    //      System.out.println(msg);
	    //      System.out.println(Query);
	    return false;
	}

    }

    static JTree CreaArbol(String tabla, String campoid, String campodescrip, String padre) throws Exception {
	Connection ConTmp = Proc.CCon();
	ResultSet Resul;
	Resul = exConsulta(ConTmp, "Select * from " + Esquema + tabla + " where " + campoid + "=" + padre + " and estado<>'*' order by oid");
	Resul.next();
	DefaultMutableTreeNode arbol = new DefaultMutableTreeNode(Resul.getString(campoid) + " - " + Resul.getString(campodescrip));
	Resul = exConsulta(ConTmp, "Select * from " + Esquema + tabla + " where padre=" + padre + " and estado<>'*' order by oid");
	CreaHijo(tabla, campoid, campodescrip, arbol, Resul);
	ConTmp.close();
	return new JTree(arbol);
    }

    static void CreaHijo(String tabla, String campoid, String campodescrip, DefaultMutableTreeNode padre, ResultSet rs) throws SQLException {
	while (rs.next()) {
	    DefaultMutableTreeNode hijos = new DefaultMutableTreeNode(rs.getString(campoid) + " - " + rs.getString(campodescrip));
	    padre.add(hijos);
	    Connection ConTmp = Proc.CCon();
	    ResultSet Resul = exConsulta(ConTmp, "Select * from " + Esquema + tabla + " where estado<>'*' and padre=" + rs.getString(campoid) + " order by oid");
	    CreaHijo(tabla, campoid, campodescrip, hijos, Resul);
	    ConTmp.close();
	}
    }

    static Vector getNextRow(ResultSet rs, ResultSetMetaData rsmd)//funcion que te devuelve un vector (registro)
	throws SQLException {
	Vector FilaActual = new Vector();
	try {
	    for (int i = 1; i <= rsmd.getColumnCount(); ++i)
		switch (rsmd.getColumnType(i)) {
		    case Types.VARCHAR:
			FilaActual.addElement(rs.getString(i));
			break;
		    case Types.CHAR:
			FilaActual.addElement(rs.getString(i));
			break;
		    case Types.DATE:
			FilaActual.addElement(Fecha2Slash(rs.getString(i)));
			break;
		    case Types.TIME:
			FilaActual.addElement(Hora(rs.getString(i)));
			break;
		    case Types.LONGVARCHAR:
			FilaActual.addElement(rs.getString(i));
			break;
		    case Types.INTEGER:
			FilaActual.addElement(new Long(rs.getLong(i)));
			break;
		    case Types.DOUBLE:
			FilaActual.addElement(new Double(rs.getDouble(i)));
			break;
		    case Types.DECIMAL:
			FilaActual.addElement(new Double(rs.getDouble(i)));
			break;
		    default:
		}
	    //end switch
	} catch (Exception x) {
	    (Proc.Mensaje(x.getMessage(), "Error"));
	}
	return FilaActual;
	//vector resultante
    }

    static String CantRegistro(String ConsultaCount, int CantReg, int Liminf, JButton siguiente, JButton anterior) throws SQLException {
	ResultSet Resul1;
	Connection Con1 = Proc.CCon();
	String etiqueta;
	Resul1 = Proc.exConsulta(Con1, ConsultaCount);
	Resul1.next();
	int i = Resul1.getInt(1);
	if (Liminf < 0)
	    Liminf = 0;
	if (i > CantReg) {
	    if (Liminf + CantReg < i) {
		siguiente.setEnabled(true);
	    } else {
		siguiente.setEnabled(false);
	    }
	    if (Liminf > 0) {
		anterior.setEnabled(true);
	    } else {
		anterior.setEnabled(false);
	    }
	} else {
	    anterior.setEnabled(false);
	    siguiente.setEnabled(false);
	}
	if (Liminf + CantReg > i) {
	    etiqueta = "  Registros: " + i + " coincidencias, mostrando de " + (Liminf + 1) + " a " + i;
	} else {
	    etiqueta = "  Registros: " + i + " coincidencias, mostrando de " + (Liminf + 1) + " a " + (Liminf + CantReg);
	}
	return etiqueta;
    }

    static void RefreshTabla(String Tabla, String ConsultaTabla, int CantReg, int Liminf, DefaultTableModel jTableTmp, JTable jTabla, JScrollPane Panel) throws SQLException {
	String localSQLQuery = "Select columna from " + Esquema + "columnas where columna<>'Estado' and tabla='" + Tabla + "' order by idcolumna";
	//    System.out.println(localSQLQuery);
	Connection ConTmp = Proc.CCon();
	ResultSet Resul = exConsulta(ConTmp, localSQLQuery);
	ResultSetMetaData ResulMD = Resul.getMetaData();
	//ResulMD -- Obtiene la META-Informacion de la tabla que devuelve la consulta "SQLString"
	jTableTmp.setRowCount(0);
	jTableTmp.setColumnCount(0);
	while (Resul.next())//mientras haya registros
	{
	    jTableTmp.addColumn(getNextRow(Resul, ResulMD));
	}
	//end While
	ConTmp.close();
	ConTmp = Proc.CCon();
	Resul = exConsulta(ConTmp, ConsultaTabla + " LIMIT " + CantReg + " OFFSET " + Liminf);
	// + " LIMIT " + CantReg + "," + Liminf);// + " OFFSET " + Liminf); //Result1 -- Contiene la tabla que devuelve la consulta "SQLString" " order by oid" +
	ResulMD = Resul.getMetaData();
	//ResulMD -- Obtiene la META-Informacion de la tabla que devuelve la consulta "SQLString"
	while (Resul.next())//mientras haya registros
	{
	    jTableTmp.addRow(getNextRow(Resul, ResulMD));
	}
	//end While
	jTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	jTabla.getTableHeader().setReorderingAllowed(false);
	jTabla.getTableHeader().setResizingAllowed(true);
	Panel.getViewport().remove(jTabla);
	Panel.getViewport().add(jTabla);
	ConTmp.close();
    }

    static String Max(String Tabla, String Campo, String Filtro) {
	String maximo = "1";
	try {
	    Connection ConTmp = Proc.CCon();
	    ResultSet Resulx = Proc.exConsulta(ConTmp, "Select max(" + Campo + ") + 1 from " + Tabla + Filtro);
	    if (Resulx.next()) {
		maximo = Resulx.getString(1);
		ConTmp.close();
		try {
		    if (maximo.equals(null)) {

		    }
		} catch (NullPointerException x) {
		    maximo = "1";
		}
	    }
	} catch (SQLException x) {

	}
	return maximo;
    }

    static String getCampo(String SQLQuery) throws SQLException {
	Connection ConTmp = Proc.CCon();
	String campo = "";
	ResultSet Resul1 = exConsulta(ConTmp, SQLQuery);
	if (Resul1.next()) {
	    campo = Resul1.getString(1);
	    ConTmp.close();
	}
	return campo;
    }

    static void CargaCombo(JComboBox combo, String ConsultaCombo, String filtro) {
	try {
	    Connection ConTmp = Proc.CCon();
	    ResultSet Result1;
	    Result1 = exConsulta(ConTmp, ConsultaCombo);
	    combo.removeAllItems();
	    int tamaniocombo = 0;
	    while (Result1.next())//mientras haya registros
	    {
		combo.addItem(Result1.getString(1));
		if (filtro.trim().equals(Result1.getString(1).trim())) {
		    combo.setSelectedIndex(tamaniocombo);
		}
		tamaniocombo = tamaniocombo + 1;
	    }
	    //end While
	    ConTmp.close();
	} catch (SQLException x) {
	    x.printStackTrace();
	}
    }

    static void CargaComboSiNo(JComboBox combo, String filtro) {
	combo.removeAllItems();
	int tamaniocombo = 0;
	combo.addItem("Sí");
	combo.addItem("No");
	if (filtro.trim().equals("Sí") | filtro.trim().equals("1") | filtro.trim().equals("t")) {
	    combo.setSelectedIndex(0);
	} else {
	    combo.setSelectedIndex(1);
	}
    }

    static void CargaComboSexo(JComboBox combo, String filtro) throws SQLException {
	combo.removeAllItems();
	int tamaniocombo = 0;
	combo.addItem("Masc.");
	combo.addItem("Fem.");
	if (filtro.trim().equals("Masc.") | filtro.trim().equals("1")) {
	    combo.setSelectedIndex(0);
	} else {
	    combo.setSelectedIndex(1);
	}
    }

    static void CargaComboBacheo(JComboBox combo, String filtro) {
	combo.removeAllItems();
	int tamaniocombo = 0;
	combo.addItem("Ninguno");
	combo.addItem("A");
	combo.addItem("B");
	if (filtro.trim().equals("Ninguno"))
	    combo.setSelectedIndex(0);
	if (filtro.trim().equals("A"))
	    combo.setSelectedIndex(1);
	if (filtro.trim().equals("B"))
	    combo.setSelectedIndex(2);
    }

    static void CargaComboSQLop(JComboBox combo) {
	combo.removeAllItems();
	combo.addItem("=");
	combo.addItem(">");
	combo.addItem("<");
	combo.addItem(">=");
	combo.addItem("<=");
	combo.addItem("like");
	combo.setSelectedIndex(0);
    }

    static String VF(JComboBox combo) {
	if (combo.getSelectedItem().toString().equals("Sí") | combo.getSelectedItem().toString().equals("Masc."))
	    return "true";
	else
	    return "false";
    }

    static String VF(JCheckBox check) {
	if (check.isSelected() == true)
	    return "true";
	else
	    return "false";
    }

    static void CargaCheck(JCheckBox check, String valor) {
	if (valor.equals("1") | valor.equalsIgnoreCase("t"))
	    check.setSelected(true);
	else
	    check.setSelected(false);
    }

    static void RemueveCol(JTable jtabla, int[] vecindexcol) {
	//Los indices de cualquier tabla empiezan en 0, se debe especificar de atras hacia adelante
	for (int i = 0; i < vecindexcol.length; ++i) {
	    //     System.out.println(vecindexcol[i]);
	    jtabla.removeColumn(jtabla.getColumnModel().getColumn(vecindexcol[i]));
	}
    }

    static String Fecha2Slash(String fecha) throws Exception {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date date = (Date)formatter.parse(fecha);
	String fecharet = formatter.format(date);
	formatter.applyPattern("dd/MM/yyyy");
	return formatter.format(date).toString();
    }

    static String Slash2Fecha(String fecha) throws Exception {
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	Date date = (Date)formatter.parse(fecha);
	String fecharet = formatter.format(date);
	formatter.applyPattern("yyyy-MM-dd");
	return formatter.format(date).toString();
    }

    static String Hora(String hora) throws Exception {
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	Date date = (Date)formatter.parse(hora);
	String fecharet = formatter.format(date);
	formatter.applyPattern("HH:mm:ss");
	return formatter.format(date).toString();
    }

    static String FechaHora(boolean fecha) throws Exception//Si fecha es verdadero devuelve la fecha actual, si no devuelve la hora actual
    {
	String SQLQuery;
	if (fecha) {
	    return Fecha2Slash(getCampo("Select current_date as string"));
	} else {
	    return Hora(getCampo("Select current_time as string"));
	}
    }

    static void Mensaje(String mensaje, String titulo) {
	JOptionPane.showMessageDialog((Component)null, mensaje, titulo, JOptionPane.OK_OPTION);
	//     System.out.println(mensaje);
    }

    /*static void ActivaBotones(JButton agregar, JButton modificar, JButton eliminar) 
 {
   if (tipoconsulta.equals("Consulta")) 
   {
     agregar.setEnabled(false);
     modificar.setEnabled(false);
     eliminar.setEnabled(false);
   } else if (tipoconsulta.equals("Carga de Datos")) 
   {
     agregar.setEnabled(true);
     modificar.setEnabled(false);
     eliminar.setEnabled(false);
     
   } else if (tipoconsulta.equals("Modificacion")) 
   {
     agregar.setEnabled(false);
     modificar.setEnabled(true);
     eliminar.setEnabled(false);
   } else if (tipoconsulta.equals("Eliminar")) 
   {
     agregar.setEnabled(false);
     modificar.setEnabled(false);
     eliminar.setEnabled(true);
   }
 }*/

    static void FechaHoraTpoReal(JTextField jtfecha, JTextField hora, Timer timer) {
	try {
	    jthora = hora;
	    jtfecha.setText(Proc.FechaHora(true));
	} catch (Exception x) {
	    Proc.Mensaje(x.getMessage(), "Error");
	}

	int delay = 0;
	// delay for 5 sec.
	int period = 1000;
	// repeat every sec.

	timer.scheduleAtFixedRate(new TimerTask() {

			       public void run() {
				   try {
				       jthora.setText(FechaHora(false));
				   } catch (Exception x) {
				       Proc.Mensaje(x.getMessage(), "Error");
				   }
			       }

			   }
			   , delay, period);
    }

    static String getHost() {
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    String hostname = addr.getHostName();
	    return hostname + ".opsalta.gov";

	} catch (UnknownHostException e) {
	    Proc.Mensaje(e.getMessage(), "Error");
	}
	return "Error al obtener el nombre del host";
    }

    static void TamanioColumna(JTable tabla, int[] vecindextamcol) {
	tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	//Los indices de cualquier tabla empiezan en 0, se debe especificar de atras hacia adelante
	for (int i = 0; i < vecindextamcol.length; ++i) {
	    TableColumn col = tabla.getColumnModel().getColumn(i);
	    col.setPreferredWidth(vecindextamcol[i]);
	}
    }

    static void ExpandeSplit(JSplitPane jSP, boolean op, int tamanio) {
	jSP.getLeftComponent().setVisible(op);
	jSP.setDividerLocation(tamanio);
	jSP.setDividerSize(0);
    }

    static void Centro(JFrame frame) {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = frame.getSize();
	if (frameSize.height > screenSize.height) {
	    frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width) {
	    frameSize.width = screenSize.width;
	}
	frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    static String quote(JTextField x) {
	return x.getText().replaceAll("'", "\\\\'");
    }

    static String quote(JTextArea x) {
	return x.getText().replaceAll("'", "\\\\'");
    }

    static String quote(SComboBox x) {
	return x.getSelectedItem().toString().replaceAll("'", "\\\\'");
    }

    static String validaDate(String fechaing, boolean fecha)//Verdadero si es fecha, falso si es hora
	throws ParseException {
	if (fecha) {
	    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	    Date date = (Date)formatter.parse(fechaing);
	    String fecharet = formatter.format(date);
	    if (fecharet.equalsIgnoreCase(fechaing)) {
		formatter.applyPattern("yyyy/MM/dd");
		return formatter.format(date);
	    } else
		return "*";
	} else {
	    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
	    Date time = (Date)formatter.parse(fechaing);
	    String hora = formatter.format(time);
	    if (hora.equalsIgnoreCase(fechaing))
		return hora;
	    else
		return "*";
	}
    }

    static void Centro(JDialog frame) {
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Dimension frameSize = frame.getSize();
	if (frameSize.height > screenSize.height) {
	    frameSize.height = screenSize.height;
	}
	if (frameSize.width > screenSize.width) {
	    frameSize.width = screenSize.width;
	}
	frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

}
