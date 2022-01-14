package org.digitall.apps.mapper.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.mapper.classes.Layer;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.inputpanels.ColorSelectorPanel;

public class LayerColorConfig extends BasicContainerPanel {

    private BasicLabel labelname = new BasicLabel();
    private BasicLabel labelexpression = new BasicLabel();
    private BasicLabel labelbackgroundcolor = new BasicLabel();
    private BasicLabel labelcolor = new BasicLabel();
    private BasicLabel labelmaxsize = new BasicLabel();
    private BasicLabel labelminsize = new BasicLabel();
    private BasicLabel labeloutlinecolor = new BasicLabel();
    private BasicLabel labelmaxsize1 = new BasicLabel();
    private BasicLabel labelsymbol = new BasicLabel();
    private BasicLabel labeltemplate = new BasicLabel();
    private BasicLabel labeltext = new BasicLabel();
    private BasicTextField name = new BasicTextField();
    private ColorSelectorPanel backgroundcolor = new ColorSelectorPanel();
    private ColorSelectorPanel color = new ColorSelectorPanel();
    private BasicTextField maxsize = new BasicTextField();
    private BasicTextField minsize = new BasicTextField();
    private ColorSelectorPanel outlinecolor = new ColorSelectorPanel();
    private BasicTextField expression = new BasicTextField();
    private BasicTextField size = new BasicTextField();
    private BasicTextField template = new BasicTextField();
    private BasicTextField text = new BasicTextField();
    private BasicCombo symbol = new BasicCombo();
    private Layer layer;

    public LayerColorConfig() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	this.setSize(new Dimension(462, 446));
	labelname.setText("Nombre:");
	labelname.setBounds(new Rectangle(15, 10, 55, 20));
	name.setBounds(new Rectangle(75, 10, 260, 20));
	expression.setBounds(new Rectangle(140, 275, 260, 20));
	labelexpression.setText("Expresión:");
	labelexpression.setBounds(new Rectangle(65, 275, 70, 20));
	labelbackgroundcolor.setText("Color de los símbolos no transparentes:");
	labelbackgroundcolor.setBounds(new Rectangle(103, 55, 255, 15));
	backgroundcolor.setBounds(new Rectangle(10, 75, 440, 40));
	labelcolor.setText("Color de los dibujos:");
	labelcolor.setBounds(new Rectangle(165, 125, 130, 15));
	color.setBounds(new Rectangle(10, 145, 440, 40));
	labelmaxsize.setText("Tamaño Máximo:");
	labelmaxsize.setBounds(new Rectangle(170, 305, 110, 20));
	maxsize.setBounds(new Rectangle(175, 330, 105, 20));
	labelminsize.setText("Tamaño Minimo:");
	labelminsize.setBounds(new Rectangle(300, 305, 110, 20));
	minsize.setBounds(new Rectangle(305, 330, 105, 20));
	labeloutlinecolor.setText("Color de los contornos:");
	labeloutlinecolor.setBounds(new Rectangle(155, 195, 150, 15));
	outlinecolor.setBounds(new Rectangle(10, 215, 440, 40));
	size.setBounds(new Rectangle(45, 330, 105, 20));
	labelmaxsize1.setText("Tamaño:");
	labelmaxsize1.setBounds(new Rectangle(70, 305, 60, 20));
	labelsymbol.setText("Símbolo:");
	labelsymbol.setBounds(new Rectangle(45, 360, 60, 20));
	labeltemplate.setText("Plantilla:");
	labeltemplate.setBounds(new Rectangle(45, 390, 60, 20));
	template.setBounds(new Rectangle(110, 390, 300, 20));
	labeltext.setText("Texto:");
	labeltext.setBounds(new Rectangle(45, 420, 45, 20));
	text.setBounds(new Rectangle(95, 420, 315, 20));
	symbol.setBounds(new Rectangle(105, 360, 305, 20));
	symbol.addItem("0 - Sin símbolo");
	symbol.addItem("1 - Ángulo");
	symbol.addItem("2 - Estrella");
	symbol.addItem("3 - Triángulo");
	symbol.addItem("4 - Cuadrado");
	symbol.addItem("5 - \"+\"");
	symbol.addItem("6 - \"X\"");
	symbol.addItem("7 - Círculo");
	symbol.addItem("8 - \"_\"");
	symbol.addItem("9 - \" \"");
	symbol.addItem("10 - \"\\\"");
	symbol.addItem("11 - \"/\"");
	symbol.addItem("12 - \"X\"");
	symbol.addItem("13 - Círculo");
	this.add(labeltext, null);
	this.add(labeltemplate, null);
	this.add(labelsymbol, null);
	this.add(labelmaxsize1, null);
	this.add(labeloutlinecolor, null);
	this.add(labelminsize, null);
	this.add(labelmaxsize, null);
	this.add(labelcolor, null);
	this.add(labelbackgroundcolor, null);
	this.add(labelexpression, null);
	this.add(labelname, null);
	this.add(symbol, null);
	this.add(text, null);
	this.add(template, null);
	this.add(size, null);
	this.add(outlinecolor, null);
	this.add(minsize, null);
	this.add(maxsize, null);
	this.add(color, null);
	this.add(backgroundcolor, null);
	this.add(expression, null);
	this.add(name, null);
    }

    public void setEmpty() {
	this.name.setText("");
	this.backgroundcolor.setColor(new Color(0, 0, 0));
	this.color.setColor(new Color(0, 0, 0));
	this.maxsize.setText("");
	this.minsize.setText("");
	this.outlinecolor.setColor(new Color(0, 0, 0));
	this.expression.setText("");
	this.size.setText("");
	this.template.setText("");
	this.text.setText("");
	this.symbol.setSelectedIndex(0);
    }

}
