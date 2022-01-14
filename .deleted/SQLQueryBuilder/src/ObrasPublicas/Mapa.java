package ObrasPublicas;

import javax.swing.JFrame;

import java.awt.Dimension;

import javax.swing.JTextField;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JComboBox;

public class Mapa extends JFrame {

    private JTextField m_nombre = new JTextField();
    private JLabel jLabel1 = new JLabel();
    private JTextField MinX = new JTextField();
    private JTextField MinY = new JTextField();
    private JTextField MaxX = new JTextField();
    private JTextField MaxY = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel jLabel5 = new JLabel();
    private JLabel jLabel6 = new JLabel();
    private JComboBox limites = new JComboBox();
    private JLabel jLabel7 = new JLabel();
    private JComboBox tamanio = new JComboBox();
    private JLabel jLabel8 = new JLabel();

    public Mapa() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout(null);
	this.setSize(new Dimension(323, 242));
	m_nombre.setBounds(new Rectangle(135, 10, 125, 20));
	jLabel1.setText("Nombre:");
	jLabel1.setBounds(new Rectangle(75, 10, 55, 20));
	MinX.setBounds(new Rectangle(90, 110, 69, 19));
	MinY.setBounds(new Rectangle(215, 110, 69, 19));
	MaxX.setBounds(new Rectangle(90, 135, 69, 19));
	MaxY.setBounds(new Rectangle(215, 135, 69, 19));
	jLabel2.setText("MinX:");
	jLabel2.setBounds(new Rectangle(35, 110, 45, 20));
	jLabel3.setText("MaxX:");
	jLabel3.setBounds(new Rectangle(35, 135, 45, 20));
	jLabel4.setText("MinY:");
	jLabel4.setBounds(new Rectangle(170, 110, 45, 20));
	jLabel5.setText("MaxY:");
	jLabel5.setBounds(new Rectangle(170, 135, 45, 20));
	jLabel6.setText("Límites:");
	jLabel6.setBounds(new Rectangle(155, 50, 55, 15));
	limites.setBounds(new Rectangle(110, 80, 175, 20));
	jLabel7.setText("Presets:");
	jLabel7.setBounds(new Rectangle(35, 80, 55, 20));
	tamanio.setBounds(new Rectangle(110, 170, 175, 20));
	jLabel8.setText("Tamaño:");
	jLabel8.setBounds(new Rectangle(35, 170, 55, 20));
	this.getContentPane().add(jLabel8, null);
	this.getContentPane().add(tamanio, null);
	this.getContentPane().add(jLabel7, null);
	this.getContentPane().add(limites, null);
	this.getContentPane().add(jLabel6, null);
	this.getContentPane().add(jLabel5, null);
	this.getContentPane().add(jLabel4, null);
	this.getContentPane().add(jLabel3, null);
	this.getContentPane().add(jLabel2, null);
	this.getContentPane().add(MaxY, null);
	this.getContentPane().add(MaxX, null);
	this.getContentPane().add(MinY, null);
	this.getContentPane().add(MinX, null);
	this.getContentPane().add(jLabel1, null);
	this.getContentPane().add(m_nombre, null);
    }

}
