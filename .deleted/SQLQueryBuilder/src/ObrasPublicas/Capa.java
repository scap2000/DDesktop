package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JEditorPane;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Capa extends JFrame {

    private JLabel jLabel1 = new JLabel();
    private JTextField nombre = new JTextField();
    private JComboBox jComboBox1 = new JComboBox();
    private JLabel jLabel7 = new JLabel();
    private JLabel jLabel8 = new JLabel();
    private JComboBox jComboBox2 = new JComboBox();
    private JTextField nombre1 = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JButton jButton1 = new JButton();

    public Capa() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(400, 300));
	jLabel1.setText("Nombre:");
	jLabel1.setBounds(new Rectangle(45, 10, 55, 19));
	nombre.setBounds(new Rectangle(105, 10, 69, 19));
	jComboBox1.setBounds(new Rectangle(85, 50, 130, 20));
	jLabel7.setText("Tipo:");
	jLabel7.setBounds(new Rectangle(35, 50, 40, 20));
	jLabel8.setText("Conexión:");
	jLabel8.setBounds(new Rectangle(35, 75, 65, 20));
	jComboBox2.setBounds(new Rectangle(105, 75, 130, 20));
	nombre1.setBounds(new Rectangle(40, 145, 280, 20));
	jLabel2.setText("Origen de datos:");
	jLabel2.setBounds(new Rectangle(40, 110, 103, 20));
	jButton1.setText("Generar");
	jButton1.setBounds(new Rectangle(225, 105, 95, 30));
	jButton1.setMnemonic('G');
	jButton1.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
				    jButton1_actionPerformed(e);
				}

			    }
	);
	this.getContentPane().add(jButton1, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(nombre1, null);
	this.getContentPane().add(jComboBox2, null);
	this.getContentPane().add(jLabel8, null);
	this.getContentPane().add(jLabel7, null);
	this.getContentPane().add(jComboBox1, null);
	this.getContentPane().add(nombre, null);
	this.getContentPane().add(jLabel1, null);
    }

    private void jButton1_actionPerformed(ActionEvent e) {

    }

}
