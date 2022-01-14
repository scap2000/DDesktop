package org.digitall.apps.personalfiles.Dependencia.ejemplos;

import java.awt.Dimension;

import java.awt.Rectangle;

import javax.swing.JFrame;

public class FramePrueba extends JFrame {
    Separador separador = new Separador(300,3,0);
    public FramePrueba() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.getContentPane().setLayout( null );
	this.setSize( new Dimension(400, 300) );
	separador.setBounds(new Rectangle(45, 135, 300, 3));
	add(separador, null);	
    }

}
