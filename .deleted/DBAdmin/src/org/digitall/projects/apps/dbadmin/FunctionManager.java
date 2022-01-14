package org.digitall.projects.apps.dbadmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.digitall.common.components.combos.JCombo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicCheckList;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicList;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.sql.LibSQL;

import org.digitall.projects.apps.dbadmin.BasicCellRenderer;

import org.digitall.projects.apps.dbadmin.DBAdminMain;

import org.postgresql.util.PSQLException;

public class FunctionManager extends BasicPanel {

    private BasicLabel lblFunctionsGreen = new BasicLabel();
    private JCombo cbFunctionsSchemes = new JCombo();
    private BasicScrollPane spFunctionsByScheme = new BasicScrollPane();
    private BasicList functionsByScheme = new BasicList();
    private BasicScrollPane spGroupsByFunction = new BasicScrollPane();
    private BasicCheckList groupsByFunction = new BasicCheckList(new DefaultListModel());
    private SaveDataButton saveGroupsByFunction = new SaveDataButton();
    private CancelDataButton cancelGroupsByFunction = new CancelDataButton();
    private Vector<DBRole> groupVector = new Vector<DBRole>();
    private DBAdminMain manager;

    public FunctionManager(DBAdminMain _manager) {
	manager = _manager;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(548, 475));
	cbFunctionsSchemes.setBounds(new Rectangle(5, 5, 200, 20));
	spGroupsByFunction.setBounds(new Rectangle(5, 280, 200, 185));
	spFunctionsByScheme.setBounds(new Rectangle(5, 30, 535, 245));
	spFunctionsByScheme.getViewport().add(functionsByScheme);
	groupsByFunction.addMouseListener(new MouseAdapter() {

				       public void mouseClicked(MouseEvent me) {
					   if (groupsByFunction.getSelectedValues().length > 0 && me.getButton() == MouseEvent.BUTTON3) {
					       for (int i = 0; i < groupsByFunction.getSelectedValues().length; i++) {
						   DBRole _group = (DBRole)groupsByFunction.getSelectedValues()[i];
						   _group.incPrivilege();
						   _group.setSelected(true);
					       }
					       spGroupsByFunction.repaint();
					   }
				       }

				   }
	);
	groupsByFunction.setCellRenderer(new BasicCellRenderer());
	saveGroupsByFunction.setBounds(new Rectangle(265, 315, 25, 25));
	saveGroupsByFunction.addActionListener(new ActionListener() {

					    public void actionPerformed(ActionEvent e) {
						saveGroupsByFunction_actionPerformed(e);
					    }

					}
	);
	cancelGroupsByFunction.setBounds(new Rectangle(220, 315, 25, 25));
	lblFunctionsGreen.setText("EXEC");
	lblFunctionsGreen.setBounds(new Rectangle(220, 285, 70, 25));
	lblFunctionsGreen.setOpaque(true);
	lblFunctionsGreen.setBackground(Color.green);
	lblFunctionsGreen.setHorizontalAlignment(SwingConstants.CENTER);
	functionsByScheme.addListSelectionListener(new ListSelectionListener() {

						public void valueChanged(ListSelectionEvent e) {
						    if (functionsByScheme.getSelectedIndex() > -1) {
							try {
							    loadGroupsByFunction(functionsByScheme.getSelectedValue().toString());
							} catch (NullPointerException x) {
							    //ignore
							}
						    }
						}

					    }
	);
	add(saveGroupsByFunction);
	add(cancelGroupsByFunction);
	add(lblFunctionsGreen, null);
	add(cbFunctionsSchemes, null);
	spGroupsByFunction.getViewport().add(groupsByFunction);
	add(spGroupsByFunction, null);
	add(spFunctionsByScheme, BorderLayout.CENTER);
	cbFunctionsSchemes.addItemListener(new ItemListener() {

					public void itemStateChanged(ItemEvent e) {
					    if (e.getStateChange() == ItemEvent.SELECTED) {
						if (cbFunctionsSchemes.getSelectedIndex() > -1) {
						    loadFunctionsByScheme(cbFunctionsSchemes.getSelectedItem().toString());
						}
					    }
					}

				    }
	);
    }

    public void boot() {
	cbFunctionsSchemes.loadJCombo("SELECT oid, nspname, 0 FROM pg_namespace WHERE nspname NOT LIKE 'pg_%' AND nspname != 'information_schema' ORDER BY nspname");
	cbFunctionsSchemes.setSelectedIndex(0);
	loadFunctionsByScheme(cbFunctionsSchemes.getSelectedItem().toString());
	loadGroupsByFunction(functionsByScheme.getSelectedValue().toString());
	setSecurityDefinerToAllFunctions();
    }

    private void revokePublicAccessToAllFunctions() {
	String query = "SELECT n.nspname || '.' || proname || '(' || pg_catalog.oidvectortypes(p.proargtypes) ||  ')' as declaration " + "FROM pg_catalog.pg_proc p INNER JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace  INNER JOIN pg_catalog.pg_language pl ON pl.oid = p.prolang " + "WHERE NOT p.proisagg  AND lanname = 'plpgsql' ORDER BY p.proname";
	ResultSet rs = LibSQL.exQuery(query);
	String _query = "";
	try {
	    while (rs.next()) {
		_query += "REVOKE ALL PRIVILEGES ON FUNCTION " + rs.getString("declaration") + " FROM PUBLIC;\n";
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	if (!LibSQL.executeQuery(_query)) {
	   Advisor.messageBox("Fallo al ejecutar revokePublicAccessToAllFunctions", "DBAdmin");
	}
    }

     private void setSecurityDefinerToAllFunctions() {
	 String query = "SELECT n.nspname || '.' || proname || '(' || pg_catalog.oidvectortypes(p.proargtypes) ||  ')' as declaration " + "FROM pg_catalog.pg_proc p INNER JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace  INNER JOIN pg_catalog.pg_language pl ON pl.oid = p.prolang " + "WHERE NOT p.proisagg  AND lanname = 'plpgsql' ORDER BY p.proname";
	 ResultSet rs = LibSQL.exQuery(query);
	 String _query = "";
	 try {
	     while (rs.next()) {
		 _query += "ALTER FUNCTION " + rs.getString("declaration") + " SECURITY DEFINER;\n";
	     }
	 } catch (SQLException e) {
	     e.printStackTrace();
	 }
	 if (!LibSQL.executeQuery(_query)) {
	    Advisor.messageBox("Fallo al ejecutar setSecurityDefinerToAllFunctions", "DBAdmin");
	 }
     }


    private void loadFunctionsByScheme(String _scheme) {
	//String query = "SELECT relname as name, description AS comment FROM pg_description de, pg_class bc, pg_namespace ns WHERE de.objoid = bc.oid and bc.relnamespace = ns.oid AND objsubid = 0 AND   ns.nspname = '" + _scheme + "' order by relname";
	//String query = "SELECT relname as name FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid AND ns.nspname = '" + _scheme + "' AND relkind = 'r' ORDER BY relname";
	//String query = "SELECT p.oid AS prooid, p.proname as name, pg_catalog.format_type(p.prorettype, NULL) AS proresult, " + "CASE WHEN array_to_string(p.proargnames, '') IS NULL THEN '' ELSE array_to_string(p.proargnames, '') " + "END AS proargnames, pg_catalog.oidvectortypes(p.proargtypes) AS proarguments, pl.lanname AS prolanguage," + "CASE WHEN pg_catalog.obj_description(p.oid, 'pg_proc') IS NULL THEN '' " + "ELSE pg_catalog.obj_description(p.oid, 'pg_proc') END AS procomment, prosrc FROM pg_catalog.pg_proc p " + "INNER JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace " + "INNER JOIN pg_catalog.pg_language pl ON pl.oid = p.prolang " + "WHERE NOT p.proisagg  AND lanname = 'plpgsql' AND n.nspname LIKE '%" + _scheme + "%' " + "ORDER BY p.proname, proresult";
	String query = "SELECT p.oid AS prooid, p.proname as name, pg_catalog.format_type(p.prorettype, NULL) AS proresult, " + "CASE WHEN array_to_string(p.proargnames, '') IS NULL THEN '' ELSE array_to_string(p.proargnames, '') END AS proargnames, " + "pg_catalog.oidvectortypes(p.proargtypes) AS proarguments, " + "proname || '(' || pg_catalog.oidvectortypes(p.proargtypes) ||  ')' as declaration " + "FROM pg_catalog.pg_proc p INNER JOIN pg_catalog.pg_namespace n ON n.oid = p.pronamespace  INNER JOIN pg_catalog.pg_language pl ON pl.oid = p.prolang " + "WHERE NOT p.proisagg  AND lanname = 'plpgsql' AND n.nspname = '" + _scheme + "' ORDER BY p.proname, proresult ";
	ResultSet rs = LibSQL.exQuery(query);
	Vector _functionsByScheme = new Vector();
	try {
	    while (rs.next()) {
		_functionsByScheme.add(rs.getString("declaration"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	functionsByScheme.setListData(_functionsByScheme);
	functionsByScheme.setSelectedIndex(0);
	if (functionsByScheme.getSelectedIndex() > -1) {
	    loadGroupsByFunction(functionsByScheme.getSelectedValue().toString());
	}
    }

    private void loadGroupsByFunction(String _function) {
	((DefaultListModel)groupsByFunction.getModel()).removeAllElements();
	for (int i = 0; i < groupVector.size(); i++) {
	    DBRole _group = groupVector.elementAt(i);
	    String priv = "'" + _group.getText() + "','" + cbFunctionsSchemes.getSelectedItem().toString() + "." + _function + "',";
	    String execPriv = priv + "'EXECUTE'";
	    try {
		ResultSet privs = LibSQL.exQuery("SELECT has_function_privilege(" + execPriv + ") AS execPriv");
		if (privs.next()) {
		    _group.setPrivilege(SystemConfiguration.ZERO_PRIV);
		    _group.setPrivilege(privs.getBoolean("execPriv") ? SystemConfiguration.QUERY_PRIV : SystemConfiguration.ZERO_PRIV);
		}
	    } catch (PSQLException e) {
		Advisor.printException(e);
	    } catch (SQLException e) {
		Advisor.printException(e);
	    }
	    ((DefaultListModel)groupsByFunction.getModel()).addElement(_group);
	}
    }

    private void saveGroupsByFunction_actionPerformed(ActionEvent e) {
	String query = "";
	for (int i = 0; i < groupsByFunction.getModel().getSize(); i++) {
	    DBRole _group = (DBRole)groupsByFunction.getModel().getElementAt(i);
	    if (_group.isSelected()) {
		//query += "GRANT USAGE ON SCHEMA " + cbSchemes.getSelectedItem().toString() + " TO GROUP " + _group.getRoleName() + ";\n";
		for (int j = 0; j < functionsByScheme.getSelectedValues().length; j++) {
		    //burbuja maldita
		    //me recuerda a la U
		    //tengo que revisar en las tablas seleccionadas
		    //que permisos tenian antes los usuarios seleccionados
		    //y si han cambiado, entonces actualizarlos, sino, no
		    String _function = cbFunctionsSchemes.getSelectedItem().toString() + "." + functionsByScheme.getSelectedValues()[j].toString();
		    String priv = "'" + _group.getText() + "','" + _function + "',";
		    String execPriv = priv + "'EXECUTE'";
		    query += "REVOKE ALL PRIVILEGES ON FUNCTION " + _function + " FROM PUBLIC;\n";
		    query += "REVOKE ALL PRIVILEGES ON FUNCTION " + _function + " FROM GROUP " + _group.getText() + ";\n";
		    //System.out.println(_group.getPrivilege());
		    if (_group.getPrivilege() != SystemConfiguration.ZERO_PRIV) {
			//tengo que actualizar estos privilegios
			query += "GRANT EXECUTE ON FUNCTION " + _function + " TO GROUP " + _group.getText() + ";\n";
		    }
		}
	    }
	}
	//System.out.println(query);
	if (LibSQL.exActualizar('a', query)) {
	    //taránnnnn
	    for (int i = 0; i < groupsByFunction.getModel().getSize(); i++) {
		((DBRole)groupsByFunction.getModel().getElementAt(i)).setSelected(false);
		updateUI();
	    }
	} 
	else {
	    Advisor.messageBox("Error al intentar asignar los permisos", "Error");
	}
    }

    public void setGroupVector(Vector<DBRole> _groupVector) {
	groupVector = _groupVector;
    }

}
