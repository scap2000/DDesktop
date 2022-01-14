package org.digitall.apps.legalprocs.calendar.wizard;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;

public class Step00 extends BasicContainerPanel {

    private BasicButton jButton1 = new BasicButton();
    private BasicButton jButton2 = new BasicButton();
    private BasicLabel jLabel1 = new BasicLabel();
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicLabel jLabel3 = new BasicLabel();
    private BasicLabel jLabel4 = new BasicLabel();

    public Step00() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(571, 449));
	this.setSize(new Dimension(383, 335));
	this.setBounds(new Rectangle(10, 10, this.getWidth(), this.getHeight()));
	this.setName("temp");
	jButton1.setText("jButton1");
	jButton1.setBounds(new Rectangle(25, 35, 77, 22));
	jButton2.setText("jButton2");
	jButton2.setBounds(new Rectangle(30, 75, 77, 22));
	jLabel1.setText("jLabel1");
	jLabel1.setBounds(new Rectangle(135, 40, 38, 14));
	jLabel2.setText("jLabel2");
	jLabel2.setBounds(new Rectangle(140, 75, 38, 14));
	jLabel3.setText("jLabel3");
	jLabel3.setBounds(new Rectangle(225, 40, 38, 14));
	jLabel4.setText("jLabel4");
	jLabel4.setBounds(new Rectangle(235, 80, 38, 14));
	this.add(jLabel4, null);
	this.add(jLabel3, null);
	this.add(jLabel2, null);
	this.add(jLabel1, null);
	this.add(jButton2, null);
	this.add(jButton1, null);
    }

}
