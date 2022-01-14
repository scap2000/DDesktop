package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JComboBox;

import java.awt.Rectangle;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class SelectorOrigen extends JFrame {

    private JComboBox esquema = new JComboBox();
    private JTextField usuario = new JTextField();
    private JTextField host = new JTextField();
    private JLabel jLabel1 = new JLabel();
    private JLabel jLabel2 = new JLabel();
    private JPasswordField clave = new JPasswordField();
    private JLabel jLabel3 = new JLabel();
    private JButton jButton1 = new JButton();
    private JLabel jLabel4 = new JLabel();
    private JTextField base = new JTextField();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private JComboBox tabla = new JComboBox();
    private JComboBox filtro = new JComboBox();
    private JLabel jLabel7 = new JLabel();
    private JLabel jLabel8 = new JLabel();
    private JComboBox geom = new JComboBox();
    private JLabel jLabel9 = new JLabel();
    private JComboBox label = new JComboBox();
    private JButton jButton2 = new JButton();
    private JButton jButton3 = new JButton();
    private JComboBox fcolumna = new JComboBox();
    private JComboBox condicion = new JComboBox();
    private JTextField usuario1 = new JTextField();
    private JComboBox nexo = new JComboBox();
    private JButton bgenerar = new JButton();
    private JTextField jTextField1 = new JTextField();
    private JButton jButton4 = new JButton();

    public SelectorOrigen() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(790, 305));
	esquema.setBounds(new Rectangle(80, 45, 150, 25));
	esquema.addItemListener(new ItemListener() {

			     public void itemStateChanged(ItemEvent e) {
				 esquema_itemStateChanged(e);
			     }

			 }
	);
	usuario.setBounds(new Rectangle(70, 10, 95, 20));
	usuario.setHorizontalAlignment(JTextField.CENTER);
	usuario.setText("consulta");
	host.setBounds(new Rectangle(365, 10, 95, 20));
	host.setText("192.168.2.6");
	host.setHorizontalAlignment(JTextField.CENTER);
	jLabel1.setText("Usuario:");
	jLabel1.setBounds(new Rectangle(15, 10, 55, 20));
	jLabel2.setText("Clave:");
	jLabel2.setBounds(new Rectangle(180, 10, 40, 20));
	clave.setBounds(new Rectangle(220, 10, 95, 20));
	clave.setHorizontalAlignment(JTextField.CENTER);
	clave.setText("consulta");
	jLabel3.setText("Host:");
	jLabel3.setBounds(new Rectangle(330, 10, 35, 20));
	jButton1.setText("Conectar");
	jButton1.setBounds(new Rectangle(680, 5, 90, 30));
	jButton1.setMnemonic('C');
	jButton1.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    jButton1_actionPerformed(e);
				}

			    }
	);
	jLabel4.setText("Base de Datos:");
	jLabel4.setBounds(new Rectangle(480, 10, 92, 20));
	base.setBounds(new Rectangle(575, 10, 95, 20));
	base.setText("master");
	base.setHorizontalAlignment(JTextField.CENTER);
	jLabel5.setText("Esquema:");
	jLabel5.setBounds(new Rectangle(15, 50, 60, 15));
	jLabel6.setText("Tabla:");
	jLabel6.setBounds(new Rectangle(250, 50, 37, 15));
	tabla.setBounds(new Rectangle(290, 45, 150, 25));
	tabla.addItemListener(new ItemListener() {

			   public void itemStateChanged(ItemEvent e) {
			       tabla_itemStateChanged(e);
			   }

		       }
	);
	filtro.setBounds(new Rectangle(10, 195, 540, 25));
	jLabel7.setText("Filtros:");
	jLabel7.setBounds(new Rectangle(285, 140, 43, 15));
	jLabel8.setText("Columna de Geometrías:");
	jLabel8.setBounds(new Rectangle(15, 85, 153, 15));
	geom.setBounds(new Rectangle(15, 105, 155, 25));
	geom.addItemListener(new ItemListener() {

			  public void itemStateChanged(ItemEvent e) {
			      geom_itemStateChanged(e);
			  }

		      }
	);
	jLabel9.setText("Columna de Señalización:");
	jLabel9.setBounds(new Rectangle(195, 85, 160, 15));
	label.setBounds(new Rectangle(200, 105, 150, 25));
	jButton2.setText("+");
	jButton2.setBounds(new Rectangle(560, 160, 44, 25));
	jButton3.setText("-");
	jButton3.setBounds(new Rectangle(560, 195, 44, 25));
	fcolumna.setBounds(new Rectangle(10, 160, 185, 25));
	condicion.setBounds(new Rectangle(205, 160, 155, 25));
	usuario1.setBounds(new Rectangle(370, 160, 120, 25));
	usuario1.setHorizontalAlignment(JTextField.CENTER);
	nexo.setBounds(new Rectangle(500, 160, 50, 25));
	bgenerar.setText("Generar");
	bgenerar.setBounds(new Rectangle(10, 235, 88, 25));
	bgenerar.setMnemonic('G');
	jTextField1.setBounds(new Rectangle(110, 235, 660, 25));
	jButton4.setText("jButton4");
	jButton4.setBounds(new Rectangle(665, 70, 88, 25));
	jButton4.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    jButton4_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(jButton4, null);
	this.getContentPane().add(jTextField1, null);
	this.getContentPane().add(bgenerar, null);
	this.getContentPane().add(nexo, null);
	this.getContentPane().add(usuario1, null);
	this.getContentPane().add(condicion, null);
	this.getContentPane().add(fcolumna, null);
	this.getContentPane().add(jButton3, null);
	this.getContentPane().add(jButton2, null);
	this.getContentPane().add(label, null);
	this.getContentPane().add(jLabel9, null);
	this.getContentPane().add(geom, null);
	this.getContentPane().add(jLabel8, null);
	this.getContentPane().add(jLabel7, null);
	this.getContentPane().add(filtro, null);
	this.getContentPane().add(tabla, null);
	this.getContentPane().add(jLabel6, null);
	this.getContentPane().add(jLabel5, null);
	this.getContentPane().add(base, null);
	this.getContentPane().add(jLabel4, null);
	this.getContentPane().add(jButton1, null);
	this.getContentPane().add(jLabel3, null);
	this.getContentPane().add(clave, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(jLabel1, null);
	this.getContentPane().add(host, null);
	this.getContentPane().add(usuario, null);
	this.getContentPane().add(esquema, null);
    }

    private void jButton1_actionPerformed(ActionEvent e) {
	Proc.Database = "jdbc:postgresql://" + host.getText() + "/" + base.getText();
	Proc.SQLUser = usuario.getText();
	Proc.SQLPass = new String(clave.getPassword());
	Proc.CargaCombo(esquema, "SELECT nspname as esquema FROM pg_namespace ns WHERE nspname not like 'pg_%' order by nspname", "");
    }

    private void esquema_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(tabla, "SELECT relname as tabla FROM pg_class bc, pg_namespace ns WHERE bc.relnamespace = ns.oid and ns.nspname = '" + esquema.getSelectedItem() + "'  and relam = 0  and nspname not like 'pg_%'  order by nspname, relname", "");
    }

    private void geom_itemStateChanged(ItemEvent e) {

    }

    private void jButton5_actionPerformed(ActionEvent e) {

    }

    private void tabla_itemStateChanged(ItemEvent e) {
	Proc.CargaCombo(geom, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquema.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tabla.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
	Proc.CargaCombo(label, "SELECT attname as columna, typname as tipodatos FROM pg_class bc, pg_attribute ta, pg_namespace ns, " + " pg_type ty WHERE ta.attrelid = bc.oid and ta.attnum > 0  and not ta.attisdropped and bc.relnamespace = ns.oid " + " and ns.nspname = '" + esquema.getSelectedItem() + "' and relam = 0 AND bc.relname = '" + tabla.getSelectedItem() + "' " + " and nspname not like 'pg_%' and ta.atttypid = ty.oid order by attnum", "");
    }

    private void jButton4_actionPerformed(ActionEvent e) {
	JFrame tmp2 = new Cruce();
	tmp2.show();
    }

}
