package org.digitall.projects.apps.dbadmin;

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
import org.digitall.lib.components.buttons.AssignButton;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.ReloadButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.apps.dbadmin.BasicCellRenderer;
import org.digitall.projects.apps.dbadmin.DBAdminMain;
import org.digitall.projects.apps.dbadmin.DBRole;

public class TableManager extends BasicPanel {

    private BasicList tablesByScheme = new BasicList();
    private BasicCheckList groupsByTable = new BasicCheckList(new DefaultListModel());
    private Vector<DBRole> groupVector = new Vector<DBRole>();
    private BasicScrollPane spGroupsByTable = new BasicScrollPane();
    private BasicScrollPane spTablesByScheme = new BasicScrollPane();
    private BasicLabel lblTablesRed = new BasicLabel();
    private BasicLabel lblTablesYellow = new BasicLabel();
    private BasicLabel lblTablesGreen = new BasicLabel();
    private JCombo cbTablesSchemes = new JCombo();
    private SaveDataButton saveGroupsByTable = new SaveDataButton();
    private AssignButton excludeTable = new AssignButton();
    private ReloadButton resetExcludedTables = new ReloadButton();
    private CancelDataButton cancelGroupsByTable = new CancelDataButton();
    private DBAdminMain manager;

    public TableManager(DBAdminMain _manager) {
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
	spGroupsByTable.setBounds(new Rectangle(235, 30, 200, 335));
	spGroupsByTable.getViewport().add(groupsByTable);
	spTablesByScheme.setBounds(new Rectangle(15, 30, 200, 335));
	spTablesByScheme.getViewport().add(tablesByScheme);
	lblTablesRed.setText("ADMIN");
	lblTablesRed.setBounds(new Rectangle(20, 375, 70, 25));
	lblTablesRed.setBackground(Color.red);
	lblTablesRed.setOpaque(true);
	lblTablesRed.setHorizontalAlignment(SwingConstants.CENTER);
	lblTablesYellow.setText("USER");
	lblTablesYellow.setBounds(new Rectangle(100, 375, 70, 25));
	lblTablesYellow.setOpaque(true);
	lblTablesYellow.setBackground(Color.yellow);
	lblTablesYellow.setHorizontalAlignment(SwingConstants.CENTER);
	lblTablesGreen.setText("QUERY");
	lblTablesGreen.setBounds(new Rectangle(180, 375, 70, 25));
	lblTablesGreen.setOpaque(true);
	lblTablesGreen.setBackground(Color.green);
	lblTablesGreen.setHorizontalAlignment(SwingConstants.CENTER);
	groupsByTable.setCellRenderer(new BasicCellRenderer());
	tablesByScheme.addListSelectionListener(new ListSelectionListener() {

					     public void valueChanged(ListSelectionEvent e) {
						 if (tablesByScheme.getSelectedIndex() > -1) {
						     try {
							 loadGroupsByTable(tablesByScheme.getSelectedValue().toString());
						     } catch (NullPointerException x) {
							 //ignore
						     }
						 }
					     }

					 }
	);
	groupsByTable.addMouseListener(new MouseAdapter() {

				    public void mouseClicked(MouseEvent me) {
					if (groupsByTable.getSelectedValues().length > 0 && me.getButton() == MouseEvent.BUTTON3) {
					    for (int i = 0; i < groupsByTable.getSelectedValues().length; i++) {
						DBRole _group = (DBRole)groupsByTable.getSelectedValues()[i];
						_group.incPrivilege();
						_group.setSelected(true);
					    }
					    spGroupsByTable.repaint();
					}
				    }

				}
	);
	saveGroupsByTable.setBounds(new Rectangle(410, 390, 25, 25));
	excludeTable.setBounds(new Rectangle(380, 420, 25, 25));
	resetExcludedTables.setBounds(new Rectangle(410, 420, 25, 25));
	resetExcludedTables.addActionListener(new ActionListener() {

					   public void actionPerformed(ActionEvent e) {
					       resetExcludedTables_actionPerformed(e);
					   }

				       }
	);
	excludeTable.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					excludeTable_actionPerformed(e);
				    }

				}
	);
	saveGroupsByTable.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     saveGroupsByTable_actionPerformed(e);
					 }

				     }
	);
	cancelGroupsByTable.setBounds(new Rectangle(380, 390, 25, 25));
	add(spGroupsByTable, null);
	add(lblTablesGreen, null);
	add(lblTablesYellow, null);
	add(lblTablesRed, null);
	add(cbTablesSchemes, null);
	add(spTablesByScheme, null);
	add(saveGroupsByTable, null);
	add(excludeTable, null);
	add(resetExcludedTables, null);
	add(cancelGroupsByTable, null);
	cbTablesSchemes.setBounds(new Rectangle(15, 5, 200, 20));
	cbTablesSchemes.addItemListener(new ItemListener() {

				     public void itemStateChanged(ItemEvent e) {
					 if (e.getStateChange() == ItemEvent.SELECTED) {
					     if (cbTablesSchemes.getSelectedIndex() > -1) {
						 loadTablesByScheme(cbTablesSchemes.getSelectedItem().toString());
					     }
					 }
				     }

				 }
	);
    }

    public void boot() {
	cbTablesSchemes.loadJCombo("SELECT oid, nspname, 0 FROM pg_namespace WHERE nspname NOT LIKE 'pg_%' AND nspname != 'information_schema' ORDER BY nspname");
	cbTablesSchemes.setSelectedIndex(0);
	loadTablesByScheme(cbTablesSchemes.getSelectedItem().toString());
	loadGroupsByTable(tablesByScheme.getSelectedValue().toString());
    }

    private void loadGroupsByTable(String _table) {
	((DefaultListModel)groupsByTable.getModel()).removeAllElements();
	for (int i = 0; i < groupVector.size(); i++) {
	    DBRole _group = groupVector.elementAt(i);
	    String priv = "'" + _group.getText() + "','" + cbTablesSchemes.getSelectedItem().toString() + "." + _table + "',";
	    String queryPriv = priv + "'SELECT'";
	    String adminPriv = priv + "'UPDATE'";
	    String userPriv = priv + "'INSERT'";
	    ResultSet privs = LibSQL.exQuery("SELECT has_table_privilege(" + queryPriv + ") AS queryPriv," + " has_table_privilege(" + userPriv + ") AS userPriv," + " has_table_privilege(" + adminPriv + ") AS adminPriv");
	    try {
		if (privs.next()) {
		    _group.setPrivilege(SystemConfiguration.ZERO_PRIV);
		    _group.setPrivilege(privs.getBoolean("queryPriv") ? SystemConfiguration.QUERY_PRIV : _group.getPrivilege());
		    _group.setPrivilege(privs.getBoolean("userPriv") ? SystemConfiguration.USER_PRIV : _group.getPrivilege());
		    _group.setPrivilege(privs.getBoolean("adminPriv") ? SystemConfiguration.ADMIN_PRIV : _group.getPrivilege());
		}
	    } catch (SQLException e) {
		Advisor.printException(e);
	    }
	    ((DefaultListModel)groupsByTable.getModel()).addElement(_group);
	}
    }

    private void loadTablesByScheme(String _scheme) {
	//String query = "SELECT relname as name, description AS comment FROM pg_description de, pg_class bc, pg_namespace ns WHERE de.objoid = bc.oid and bc.relnamespace = ns.oid AND objsubid = 0 AND   ns.nspname = '" + _scheme + "' order by relname";
	String query = "SELECT relname as name FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid AND ns.nspname = '" + _scheme + "' AND relkind = 'r' ORDER BY relname";
	ResultSet rs = LibSQL.exQuery(query);
	Vector _tablesByScheme = new Vector();
	try {
	    while (rs.next()) {
		_tablesByScheme.add(rs.getString("name"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	tablesByScheme.setListData(_tablesByScheme);
	tablesByScheme.setSelectedIndex(0);
	if (tablesByScheme.getSelectedIndex() > -1) {
	    loadGroupsByTable(tablesByScheme.getSelectedValue().toString());
	}
    }

    private void saveGroupsByTable_actionPerformed(ActionEvent e) {
	//REVISAR COLORES Y TABLAS!!!
	String query = "";
	for (int i = 0; i < groupsByTable.getModel().getSize(); i++) {
	    DBRole _group = (DBRole)groupsByTable.getModel().getElementAt(i);
	    if (_group.isSelected()) {
		//query += "GRANT USAGE ON SCHEMA " + cbSchemes.getSelectedItem().toString() + " TO GROUP " + _group.getRoleName() + ";\n";
		for (int j = 0; j < tablesByScheme.getSelectedValues().length; j++) {
		    //burbuja maldita
		    //me recuerda a la U
		    //tengo que revisar en las tablas seleccionadas
		    //que permisos tenian antes los usuarios seleccionados
		    //y si han cambiado, entonces actualizarlos, sino, no
		    String _table = cbTablesSchemes.getSelectedItem().toString() + "." + tablesByScheme.getSelectedValues()[j].toString();
		    String priv = "'" + _group.getText() + "','" + _table + "',";
		    String queryPriv = priv + "'SELECT'";
		    String adminPriv = priv + "'UPDATE'";
		    String userPriv = priv + "'INSERT'";
		    if (((LibSQL.getBoolean("has_table_privilege", queryPriv) || LibSQL.getBoolean("has_table_privilege", adminPriv) || LibSQL.getBoolean("has_table_privilege", userPriv)))) {
			// && _group.privilege == ZERO_PRIV)) {
			//tengo que quitarle todos los privilegios
			query += "REVOKE ALL PRIVILEGES ON " + _table + " FROM GROUP " + _group.getText() + ";\n";
		    }
		    if (_group.getPrivilege() == SystemConfiguration.QUERY_PRIV) {
			//tengo que actualizar estos privilegios
			query += "GRANT SELECT ON " + _table + " TO GROUP " + _group.getText() + ";\n";
		    } else if (_group.getPrivilege() == SystemConfiguration.USER_PRIV) {
			query += "GRANT SELECT, INSERT ON " + _table + " TO GROUP " + _group.getText() + ";\n";
		    } else if (_group.getPrivilege() == SystemConfiguration.ADMIN_PRIV) {
			query += "GRANT ALL PRIVILEGES ON " + _table + " TO GROUP " + _group.getText() + ";\n";
		    }
		}
		_group.setSelected(false);
		groupsByTable.updateUI();
	    }
	}
	//System.out.println(query);
	if (LibSQL.exActualizar('a', query)) {
	    //taránnnnn
	} else {
	    Advisor.messageBox("Error al intentar asignar los permisos", "Error");
	}
    }

    public void setGroupVector(Vector<DBRole> _groupVector) {
	groupVector = _groupVector;
    }

    private void excludeTable_actionPerformed(ActionEvent e) {
	for (int j = 0; j < tablesByScheme.getSelectedValues().length; j++) {
	    SystemConfiguration.tablesExclusionList.add(cbTablesSchemes.getSelectedItem().toString() + "." + tablesByScheme.getSelectedValues()[j].toString());
	}
    }

    private void resetExcludedTables_actionPerformed(ActionEvent e) {
	SystemConfiguration.tablesExclusionList.removeAllElements();
    }

}
