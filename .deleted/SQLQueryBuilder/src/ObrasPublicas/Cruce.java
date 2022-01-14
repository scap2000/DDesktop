package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JTextField;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Cruce extends JFrame {

    private JTextField jTextField1 = new JTextField();

    private JButton bgenerar = new JButton();
    private JComboBox nexo = new JComboBox();
    private JTextField usuario1 = new JTextField();
    private JComboBox condicion = new JComboBox();
    private JComboBox fcolumna = new JComboBox();
    private JButton delfiltro = new JButton();
    private JButton addpkey = new JButton();
    private JComboBox scolumnas = new JComboBox();
    private JLabel jLabel9 = new JLabel();
    private JComboBox columnas = new JComboBox();
    private JLabel jLabel8 = new JLabel();
    private JLabel jLabel7 = new JLabel();
    private JComboBox filtro = new JComboBox();
    private JComboBox tablad = new JComboBox();
    private JLabel jLabel6 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JComboBox esquemad = new JComboBox();
    private JComboBox tablao = new JComboBox();
    private JLabel jLabel10 = new JLabel();
    private JLabel jLabel11 = new JLabel();
    private JComboBox esquemao = new JComboBox();
    private JLabel jLabel12 = new JLabel();
    private JLabel jLabel13 = new JLabel();
    private JComboBox pkeyd = new JComboBox();
    private JComboBox pkeyo = new JComboBox();
    private JLabel jLabel15 = new JLabel();
    private JLabel jLabel16 = new JLabel();
    private JButton delpkey = new JButton();
    private JComboBox pkeys = new JComboBox();
    private JLabel jLabel17 = new JLabel();
    private JButton addcol = new JButton();
    private JButton delcol = new JButton();
    private JButton addfiltro = new JButton();

    public Cruce() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(790, 592));
	jTextField1.setBounds(new Rectangle(110, 395, 660, 25));
	bgenerar.setText("Generar");
	bgenerar.setBounds(new Rectangle(10, 395, 90, 25));
	bgenerar.setMnemonic('G');
	nexo.setBounds(new Rectangle(500, 320, 50, 25));
	usuario1.setBounds(new Rectangle(370, 320, 120, 25));
	usuario1.setHorizontalAlignment(JTextField.CENTER);
	condicion.setBounds(new Rectangle(205, 320, 155, 25));
	fcolumna.setBounds(new Rectangle(10, 320, 185, 25));
	delfiltro.setText("-");
	delfiltro.setBounds(new Rectangle(560, 355, 45, 25));
	addpkey.setText("+");
	addpkey.setBounds(new Rectangle(560, 115, 45, 25));
	addpkey.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   addpkey_actionPerformed(e);
			       }

			   }
	);
	scolumnas.setBounds(new Rectangle(235, 220, 150, 25));
	jLabel9.setText("Columnas a mostrar:");
	jLabel9.setBounds(new Rectangle(245, 195, 130, 25));
	columnas.setBounds(new Rectangle(15, 220, 155, 25));
	jLabel8.setText("Columnas de la tabla:");
	jLabel8.setBounds(new Rectangle(25, 195, 135, 25));
	jLabel7.setText("Filtros:");
	jLabel7.setBounds(new Rectangle(285, 300, 45, 15));
	filtro.setBounds(new Rectangle(10, 355, 540, 25));
	tablad.setBounds(new Rectangle(205, 115, 150, 25));
	tablad.addItemListener(new ItemListener() {

			    public void itemStateChanged(ItemEvent e) {
				tablad_itemStateChanged(e);
			    }

			}
	);
	jLabel6.setText("Tabla:");
	jLabel6.setBounds(new Rectangle(163, 45, 37, 25));
	jLabel5.setText("Esquema:");
	jLabel5.setBounds(new Rectangle(140, 85, 60, 25));
	esquemad.setBounds(new Rectangle(205, 85, 150, 25));
	esquemad.addItemListener(new ItemListener() {

			      public void itemStateChanged(ItemEvent e) {
				  esquemad_itemStateChanged(e);
			      }

			  }
	);
	tablao.setBounds(new Rectangle(205, 45, 150, 25));
	tablao.addItemListener(new ItemListener() {

			    public void itemStateChanged(ItemEvent e) {
				tablao_itemStateChanged(e);
			    }

			}
	);
	jLabel10.setText("Tabla:");
	jLabel10.setBounds(new Rectangle(163, 115, 37, 25));
	jLabel11.setText("Esquema:");
	jLabel11.setBounds(new Rectangle(140, 15, 60, 25));
	esquemao.setBounds(new Rectangle(205, 15, 150, 25));
	esquemao.addItemListener(new ItemListener() {

			      public void itemStateChanged(ItemEvent e) {
				  esquemao_itemStateChanged(e);
			      }

			  }
	);
	jLabel12.setText("Quiero cruzar:");
	jLabel12.setBounds(new Rectangle(15, 35, 95, 25));
	jLabel13.setText("Con:");
	jLabel13.setBounds(new Rectangle(40, 105, 28, 25));
	pkeyd.setBounds(new Rectangle(395, 115, 155, 25));
	pkeyo.setBounds(new Rectangle(395, 45, 155, 25));
	jLabel15.setText("Clave Primaria");
	jLabel15.setBounds(new Rectangle(428, 15, 89, 25));
	jLabel16.setText("Clave Primaria");
	jLabel16.setBounds(new Rectangle(425, 85, 89, 25));
	delpkey.setText("-");
	delpkey.setBounds(new Rectangle(560, 155, 45, 25));
	delpkey.addActionListener(new ActionListener() {

			       public void actionPerformed(ActionEvent e) {
				   delpkey_actionPerformed(e);
			       }

			   }
	);
	pkeys.setBounds(new Rectangle(140, 155, 410, 25));
	jLabel17.setText("Claves existentes:");
	jLabel17.setBounds(new Rectangle(15, 155, 112, 25));
	addcol.setText("+");
	addcol.setBounds(new Rectangle(175, 220, 45, 25));
	addcol.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  addcol_actionPerformed(e);
			      }

			  }
	);
	delcol.setText("-");
	delcol.setBounds(new Rectangle(390, 220, 45, 25));
	delcol.addActionListener(new ActionListener() {

			      public void actionPerformed(ActionEvent e) {
				  delcol_actionPerformed(e);
			      }

			  }
	);
	addfiltro.setText("+");
	addfiltro.setBounds(new Rectangle(560, 320, 45, 25));
	this.getContentPane().add(addfiltro, null);
	this.getContentPane().add(delcol, null);
	this.getContentPane().add(addcol, null);
	this.getContentPane().add(jLabel17, null);
	this.getContentPane().add(pkeys, null);
	this.getContentPane().add(delpkey, null);
	this.getContentPane().add(jLabel16, null);
	this.getContentPane().add(jLabel15, null);
	this.getContentPane().add(pkeyo, null);
	this.getContentPane().add(pkeyd, null);
	this.getContentPane().add(jLabel13, null);
	this.getContentPane().add(jLabel12, null);
	this.getContentPane().add(esquemao, null);
	this.getContentPane().add(jLabel11, null);
	this.getContentPane().add(jLabel10, null);
	this.getContentPane().add(tablao, null);
	this.getContentPane().add(esquemad, null);
	this.getContentPane().add(jLabel5, null);
	this.getContentPane().add(jLabel6, null);
	this.getContentPane().add(tablad, null);
	this.getContentPane().add(filtro, null);
	this.getContentPane().add(jLabel7, null);
	this.getContentPane().add(jLabel8, null);
	this.getContentPane().add(columnas, null);
	this.getContentPane().add(jLabel9, null);
	this.getContentPane().add(scolumnas, null);
	this.getContentPane().add(addpkey, null);
	this.getContentPane().add(delfiltro, null);
	this.getContentPane().add(fcolumna, null);
	this.getContentPane().add(condicion, null);
	this.getContentPane().add(usuario1, null);
	this.getContentPane().add(nexo, null);
	this.getContentPane().add(bgenerar, null);
	this.getContentPane().add(jTextField1, null);
	Proc.CargaCombo(esquemao, "SELECT nspname as esquema FROM pg_namespace ns WHERE nspname not like 'pg_%' order by nspname", "");
	Proc.CargaCombo(esquemad, "SELECT nspname as esquema FROM pg_namespace ns WHERE nspname not like 'pg_%' order by nspname", "");
	Proc.CargaComboSQLop(condicion);

    }

    private void esquemao_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(tablao, "SELECT relname as tabla FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid and ns.nspname = '" + esquemao.getSelectedItem() + "'  and relam = 0  and nspname not like 'pg_%'  order by nspname, relname", "");
    }

    private void esquemad_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(tablad, "SELECT relname as tabla FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid and ns.nspname = '" + esquemad.getSelectedItem() + "'  and relam = 0  and nspname not like 'pg_%'  order by nspname, relname", "");
    }

    private void tablao_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(pkeyo, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquemao.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tablao.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
    }

    private void tablad_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(pkeyd, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquemad.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tablad.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
	Proc.CargaCombo(columnas, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquemad.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tablad.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
	scolumnas.removeAllItems();
	Proc.CargaCombo(fcolumna, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquemad.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tablad.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
    }

    private void addpkey_actionPerformed(ActionEvent e) {
	pkeys.addItem(esquemao.getSelectedItem() + "." + tablao.getSelectedItem() + "." + pkeyo.getSelectedItem() + "=" + esquemad.getSelectedItem() + "." + tablad.getSelectedItem() + "." + pkeyd.getSelectedItem());
    }

    private void delpkey_actionPerformed(ActionEvent e) {
	pkeys.removeItem(pkeys.getSelectedItem());
    }

    private void addcol_actionPerformed(ActionEvent e) {
	scolumnas.addItem(columnas.getSelectedItem());
	columnas.removeItem(columnas.getSelectedItem());
    }

    private void delcol_actionPerformed(ActionEvent e) {
	columnas.addItem(scolumnas.getSelectedItem());
	scolumnas.removeItem(scolumnas.getSelectedItem());
    }

}
