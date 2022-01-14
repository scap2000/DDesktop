package org.digitall.projects.apps.dbadmin;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;

import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.digitall.common.components.combos.JCombo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.ComponentsManager;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicCheckList;
import org.digitall.lib.components.basic.BasicCheckableListItem;
import org.digitall.lib.components.basic.BasicList;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicScrollPane;
import org.digitall.lib.components.buttons.AddUserButton;
import org.digitall.lib.components.buttons.CancelDataButton;
import org.digitall.lib.components.buttons.DeleteButton;
import org.digitall.lib.components.buttons.ModifyButton;
import org.digitall.lib.components.buttons.SaveDataButton;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.sql.LibSQL;
import org.digitall.projects.apps.dbadmin.DBAdminMain;
import org.digitall.projects.apps.dbadmin.DBRole;
import org.digitall.projects.apps.dbadmin.NewUserPanel;

public class UserManager extends BasicPanel {

    private BasicList usersList = new BasicList();
    private BasicCheckList groupsByUser = new BasicCheckList();
    private BasicScrollPane spGroupsByUser = new BasicScrollPane();
    private BasicCheckList groupsNotByUser = new BasicCheckList();
    private BasicScrollPane spGroupsNotByUser = new BasicScrollPane();
    private BasicScrollPane spUsers = new BasicScrollPane();
    private SaveDataButton btnSavePrivileges = new SaveDataButton();
    private CancelDataButton btnCancelOperation = new CancelDataButton();
    private AddUserButton btnAddUser = new AddUserButton();
    private DeleteButton btnDeleteUser = new DeleteButton();
    private ModifyButton btnModifyUser = new ModifyButton();
    private DeleteButton btnPreDelete = new DeleteButton();
    private JCombo cbTablesSchemes = new JCombo();
    private Vector<DBRole> groupVector = new Vector<DBRole>();
    private Vector userVector = new Vector();
    private DBAdminMain manager;

    public UserManager(DBAdminMain _manager) {
	manager = _manager;
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(624, 475));
	spGroupsByUser.setBounds(new Rectangle(225, 5, 395, 185));
	spGroupsNotByUser.setBounds(new Rectangle(225, 195, 395, 185));
	spUsers.setBounds(new Rectangle(5, 5, 215, 375));
	spGroupsByUser.getViewport().add(groupsByUser);
	add(spGroupsByUser, null);
	spGroupsNotByUser.getViewport().add(groupsNotByUser);
	add(spGroupsNotByUser, null);
	spUsers.getViewport().add(usersList);
	add(spUsers, null);
	add(btnModifyUser, null);
	add(btnDeleteUser, null);
	add(btnAddUser, null);
	add(btnSavePrivileges, null);
	add(btnCancelOperation, null);
	add(btnPreDelete, null);
	btnAddUser.setText("Agregar\nusuario");
	btnPreDelete.setText("Desvincular\np/eliminación");
	btnDeleteUser.setText("Eliminar\nusuario");
	btnModifyUser.setText("Modificar\nusuario");
	btnSavePrivileges.setText("Guardar\ncambios");
	btnCancelOperation.setText("Cancelar\ncambios");
	usersList.addListSelectionListener(new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
					    if (usersList.getSelectedIndex() > -1) {
						loadGroupsByUser(usersList.getSelectedValue().toString());
					    }
					}

				    }
	);
	usersList.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
				    users_mouseClicked(e);
				}

			    }
	);
	btnSavePrivileges.setBounds(new Rectangle(441, 410, 70, 60));
	btnSavePrivileges.addActionListener(new ActionListener() {

					 public void actionPerformed(ActionEvent e) {
					     saveGroupsByUser_actionPerformed(e);
					 }

				     }
	);
	btnCancelOperation.setBounds(new Rectangle(550, 410, 70, 60));
	btnCancelOperation.addActionListener(new ActionListener() {

					  public void actionPerformed(ActionEvent e) {
					      cancelGroupsByUser_actionPerformed(e);
					  }

				      }
	);
	btnAddUser.setBounds(new Rectangle(5, 410, 70, 60));
	btnAddUser.setHorizontalAlignment(SwingConstants.CENTER);
	btnAddUser.addActionListener(new ActionListener() {

				  public void actionPerformed(ActionEvent e) {
				      btnAddUser_actionPerformed(e);
				  }

			      }
	);
	btnDeleteUser.setBounds(new Rectangle(332, 410, 70, 60));
	btnDeleteUser.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnDeleteUser_actionPerformed(e);
				     }

				 }
	);
	btnPreDelete.setBounds(new Rectangle(223, 410, 70, 60));
	btnPreDelete.addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					btnRevokeUsers_actionPerformed(e);
				    }

				}
	);
	btnModifyUser.setBounds(new Rectangle(114, 410, 70, 60));
	btnModifyUser.addActionListener(new ActionListener() {

				     public void actionPerformed(ActionEvent e) {
					 btnModifyUser_actionPerformed(e);
				     }

				 }
	);
    }

    public void boot() {
	loadUsers();
	cbTablesSchemes.loadJCombo("SELECT oid, nspname, 0 FROM pg_namespace WHERE nspname NOT LIKE 'pg_%' AND nspname != 'information_schema' ORDER BY nspname");
	cbTablesSchemes.setSelectedIndex(0);
    }

    private void revokeAllToUser(boolean _drop) {
	if (usersList.getSelectedValues().length > -1) {
	    String query = "";
	    for (int i = 0; i < usersList.getSelectedValues().length; i++) {
	    	String _userName = usersList.getSelectedValues()[i].toString();
		for (int j = 0; j < cbTablesSchemes.getItemCount(); j++) {
		    String _scheme = cbTablesSchemes.getItemAt(j).toString();
		    query += "REVOKE USAGE ON SCHEMA " + _scheme + " FROM " + _userName + ";\n";
		    ResultSet _tables = LibSQL.exQuery("SELECT relname as name FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid AND ns.nspname = '" + _scheme + "' AND relkind = 'r' ORDER BY relname");
		    try {
			while (_tables.next()) {
			    query += "REVOKE ALL PRIVILEGES ON " + _scheme + "." + _tables.getString("name") + " FROM " + _userName + ";\n";
			}
		    } catch (SQLException x) {
			x.printStackTrace();
		    }
		    ResultSet _deps = LibSQL.exQuery("SELECT nspname||'.'||relname AS table FROM pg_class INNER JOIN pg_authid ON pg_authid.oid = pg_class.relowner " + "INNER JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid " + "WHERE rolname = '" + _userName + "' AND nspname NOT LIKE 'pg_%'");
		    try {
			while (_deps.next()) {
			    query += "ALTER TABLE " + _deps.getString("table") + " OWNER TO " + Environment.sessionUser + ";\n";
			}
		    } catch (SQLException x) {
			x.printStackTrace();
		    }
		    ResultSet _procs = LibSQL.exQuery("SELECT nspname ||'.'||proname|| '(' || pg_catalog.oidvectortypes(pg_proc.proargtypes) ||  ')' as declaration FROM pg_proc " + "INNER JOIN pg_catalog.pg_namespace ON pg_namespace.oid = pg_proc.pronamespace " + "INNER JOIN pg_authid ON pg_authid.oid = pg_proc.proowner " + "WHERE rolname = '" + _userName + "'");
		    try {
			while (_procs.next()) {
			    query += "ALTER FUNCTION " + _procs.getString("declaration") + " OWNER TO " + Environment.sessionUser + ";\n";
			}
		    } catch (SQLException x) {
			x.printStackTrace();
		    }
		}
		if (_drop) {
		    query += "DROP USER " + _userName + ";\n";
		}

	        ResultSet _users = LibSQL.exQuery("SELECT count(*) AS qty FROM pg_class INNER JOIN pg_namespace ON pg_class.relnamespace = pg_namespace.oid " + "WHERE relname='users' AND nspname='org'");
	        try {
	            if (_users.next()) {
			if (_users.getInt("qty") > 0){
			    query += " DELETE FROM org.users WHERE username = '" + _userName + "';\n";
			}
	            }
	        } catch (SQLException x) {
	            x.printStackTrace();
	        }
		System.out.println(query);
		if (LibSQL.exActualizar('a', query)) {
		    loadUsers();
		} else {
		    Advisor.messageBox("Error al intentar borrar el usuario " + usersList.getSelectedValue(), "Error");
		}
	    }
	}
    }

    private void btnDeleteUser_actionPerformed(ActionEvent e) {
	if (Advisor.question("Borrar usuario(s)", "¿Esta seguro de borrar el(los) usuario(s)?")) {
	    revokeAllToUser(true);
	}
    }

    private void loadGroupsByUser(String _user) {
	String query = "SELECT grosysid as gid, groname as name, (SELECT usesysid FROM pg_user WHERE usename = '" + _user + "')=any(grolist) as belongs FROM pg_group order by groname";
	ResultSet rs = LibSQL.exQuery(query);
	Vector _groupsByUser = new Vector();
	Vector _groupsNotByUser = new Vector();
	try {
	    while (rs.next()) {
		BasicCheckableListItem item = new BasicCheckableListItem(rs.getInt("gid"), rs.getString("name"));
		item.setSelected(rs.getBoolean("belongs"));
		if (item.isSelected()) {
		    _groupsByUser.add(item);
		} else {
		    _groupsNotByUser.add(item);
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	groupsByUser.setListData(_groupsByUser);
	groupsByUser.setEnabled(false);
	groupsNotByUser.setListData(_groupsNotByUser);
	groupsNotByUser.setEnabled(false);
	btnCancelOperation.setEnabled(false);
	btnSavePrivileges.setEnabled(false);
	groupsByUser.setSelectedIndex(0);
	groupsNotByUser.setSelectedIndex(0);
    }

    private void loadUsers() {
	String query = "SELECT distinct usename as name, usesysid as uid FROM pg_user ORDER BY usename";
	ResultSet rs = LibSQL.exQuery(query);
	Vector _users = new Vector();
	try {
	    while (rs.next()) {
		_users.add(rs.getString("name"));
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	userVector = _users;
	usersList.setListData(_users);
	manager.setUserVector(userVector);
    }

    private void users_mouseClicked(MouseEvent e) {
	if (usersList.getSelectedIndex() >= 0) {
	    if (e.getClickCount() == 1) {
		loadGroupsByUser(usersList.getSelectedValue().toString());
	    } else if (e.getClickCount() == 2) {
		groupsByUser.setEnabled(true);
		groupsNotByUser.setEnabled(true);
		btnCancelOperation.setEnabled(true);
		btnSavePrivileges.setEnabled(true);
	    }
	}
    }

    private void saveGroupsByUser_actionPerformed(ActionEvent e) {
	String query = "";
	for (int i = 0; i < groupsByUser.getModel().getSize(); i++) {
	    BasicCheckableListItem group = (BasicCheckableListItem)groupsByUser.getModel().getElementAt(i);
	    if (!group.isSelected()) {
		query += "REVOKE " + group.getName() + " FROM " + usersList.getSelectedValue().toString() + ";\n";
	    }
	}
	for (int i = 0; i < groupsNotByUser.getModel().getSize(); i++) {
	    BasicCheckableListItem group = (BasicCheckableListItem)groupsNotByUser.getModel().getElementAt(i);
	    if (group.isSelected()) {
		query += "GRANT " + group.getName() + " TO " + usersList.getSelectedValue().toString() + ";\n";
	    }
	}
	if (LibSQL.exActualizar('a', query)) {
	    loadGroupsByUser(usersList.getSelectedValue().toString());
	} else {
	    Advisor.messageBox("Error al intentar asignar los permisos", "Error");
	}
    }

    private void cancelGroupsByUser_actionPerformed(ActionEvent e) {
	loadGroupsByUser(usersList.getSelectedValue().toString());
    }

    private void btnAddUser_actionPerformed(ActionEvent e) {
	NewUserPanel newUserPanel = new NewUserPanel();
	ComponentsManager.centerWindow(newUserPanel);
	newUserPanel.setModal(true);
	newUserPanel.setVisible(true);
	loadUsers();
    }

    private void btnRevokeUsers_actionPerformed(ActionEvent e) {
	if (Advisor.question("Desvincular usuario(s)", "¿Esta seguro de desvincular el(los) usuario(s) de las bases de datos?")) {
	    revokeAllToUser(false);
	}
    }

    private void btnModifyUser_actionPerformed(ActionEvent e) {
	if (usersList.getSelectedValues().length > 0) {
	    if (!usersList.getSelectedValue().equals(null)) {
		String userName = usersList.getSelectedValue().toString();
		NewUserPanel newUserPanel = new NewUserPanel();
		if (newUserPanel.setUserName(userName)) {
		    ComponentsManager.centerWindow(newUserPanel);
		    newUserPanel.setModal(true);
		    newUserPanel.setVisible(true);
		    loadUsers();
		} else {
		    Advisor.messageBox("Incongruencia de Datos, revise las Tablas relacionadas al Usuario", "Usuario no válido");
		}
	    }
	}
    }

    public void setGroupVector(Vector<DBRole> _groupVector) {
	groupVector = _groupVector;
    }

}
