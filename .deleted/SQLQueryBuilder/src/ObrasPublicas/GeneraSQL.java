package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JScrollPane;

import java.awt.Rectangle;

import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.awt.CheckboxGroup;

import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;

public class GeneraSQL extends JFrame {

    private JScrollPane jScrollPane1 = new JScrollPane();
    private JEditorPane jEditorPane1 = new JEditorPane();
    private JLabel jLabel1 = new JLabel();
    private JComboBox jComboBox1 = new JComboBox();
    private JLabel jLabel2 = new JLabel();
    private JButton borigen = new JButton();
    private JTextField origen = new JTextField();

    public GeneraSQL() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(788, 581));
	jScrollPane1.setBounds(new Rectangle(20, 35, 475, 150));
	jLabel1.setText("Consulta:");
	jLabel1.setBounds(new Rectangle(20, 10, 59, 20));
	jComboBox1.setBounds(new Rectangle(95, 200, 75, 25));
	jLabel2.setText("Condición:");
	jLabel2.setBounds(new Rectangle(20, 200, 66, 25));
	borigen.setText("Origen");
	borigen.setBounds(new Rectangle(25, 245, 88, 25));
	borigen.setMnemonic('O');
	origen.setBounds(new Rectangle(120, 245, 165, 25));
	this.getContentPane().add(origen, null);
	this.getContentPane().add(borigen, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(jComboBox1, null);
	this.getContentPane().add(jLabel1, null);
	jScrollPane1.getViewport().add(jEditorPane1, null);
	this.getContentPane().add(jScrollPane1, null);
    }

}
