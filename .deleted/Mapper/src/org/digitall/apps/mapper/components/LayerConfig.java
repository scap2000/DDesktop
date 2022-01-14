package org.digitall.apps.mapper.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.apps.mapper.classes.Layer;
import org.digitall.lib.components.basic.BasicCombo;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicTextField;
import org.digitall.lib.components.inputpanels.ColorSelectorPanel;

public class LayerConfig extends BasicContainerPanel {

    private BasicLabel labelname = new BasicLabel();
    private BasicLabel labelgroup = new BasicLabel();
    private BasicLabel labelstatus = new BasicLabel();
    private BasicLabel labeltype = new BasicLabel();
    private BasicLabel labelconnection = new BasicLabel();
    private BasicLabel labeldata = new BasicLabel();
    private BasicLabel labeluser = new BasicLabel();
    private BasicLabel labeldbname = new BasicLabel();
    private BasicLabel labelhost = new BasicLabel();
    private BasicLabel labelfilter = new BasicLabel();
    private BasicLabel labelfilteritem = new BasicLabel();
    private BasicLabel labellabelitem = new BasicLabel();
    private BasicLabel labellabelmaxscale = new BasicLabel();
    private BasicLabel labellabelminscale = new BasicLabel();
    private BasicLabel labellabelsizeitem = new BasicLabel();
    private BasicLabel llabelminscale = new BasicLabel();
    private BasicLabel llabelmaxscale = new BasicLabel();
    private BasicLabel labeloffsite = new BasicLabel();
    private BasicLabel labeltoleranceunits = new BasicLabel();
    private BasicLabel labeltolerance = new BasicLabel();
    private BasicTextField name = new BasicTextField();
    private BasicTextField group = new BasicTextField();
    private BasicCombo status = new BasicCombo();
    private BasicCombo type = new BasicCombo();
    private BasicCombo connectiontype = new BasicCombo();
    private BasicTextField data = new BasicTextField();
    private BasicTextField user = new BasicTextField();
    private BasicTextField dbname = new BasicTextField();
    private BasicTextField host = new BasicTextField();
    private BasicTextField filter = new BasicTextField();
    private BasicTextField filteritem = new BasicTextField();
    private BasicTextField labelitem = new BasicTextField();
    private BasicTextField labelmaxscale = new BasicTextField();
    private BasicTextField labelminscale = new BasicTextField();
    private BasicTextField labelsizeitem = new BasicTextField();
    private BasicTextField minscale = new BasicTextField();
    private BasicTextField maxscale = new BasicTextField();
    private ColorSelectorPanel offsite = new ColorSelectorPanel();
    private BasicCombo toleranceunits = new BasicCombo();
    private BasicTextField tolerance = new BasicTextField();
    private Layer layer;

    public LayerConfig() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setLayout(null);
	this.setSize(new Dimension(551, 378));
	this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
	labelname.setText("Nombre:");
	labelname.setBounds(new Rectangle(15, 15, 55, 20));
	name.setBounds(new Rectangle(75, 15, 195, 20));
	labelgroup.setText("Grupo:");
	labelgroup.setBounds(new Rectangle(290, 15, 50, 20));
	group.setBounds(new Rectangle(340, 15, 195, 20));
	labelstatus.setText("Activo:");
	labelstatus.setBounds(new Rectangle(15, 45, 55, 20));
	status.setBounds(new Rectangle(65, 45, 70, 20));
	labeltype.setText("Tipo:");
	labeltype.setBounds(new Rectangle(145, 45, 45, 20));
	type.setBounds(new Rectangle(180, 45, 100, 20));
	labelconnection.setText("Conexión:");
	labelconnection.setBounds(new Rectangle(295, 45, 65, 20));
	connectiontype.setBounds(new Rectangle(360, 45, 175, 20));
	labeldata.setText("Data:");
	labeldata.setBounds(new Rectangle(15, 105, 40, 20));
	data.setBounds(new Rectangle(55, 105, 480, 20));
	labeluser.setText("Usuario:");
	labeluser.setBounds(new Rectangle(175, 75, 55, 20));
	user.setBounds(new Rectangle(235, 75, 95, 20));
	labeldbname.setText("Base de Datos:");
	labeldbname.setBounds(new Rectangle(340, 75, 95, 20));
	dbname.setBounds(new Rectangle(440, 75, 95, 20));
	labelhost.setText("Host:");
	labelhost.setBounds(new Rectangle(15, 75, 35, 20));
	host.setBounds(new Rectangle(55, 75, 110, 20));
	labelfilter.setText("Filtro:");
	labelfilter.setBounds(new Rectangle(185, 130, 45, 20));
	filter.setBounds(new Rectangle(230, 130, 110, 20));
	labelfilteritem.setText("FilterItem:");
	labelfilteritem.setBounds(new Rectangle(355, 130, 65, 20));
	filteritem.setBounds(new Rectangle(425, 130, 110, 20));
	labelitem.setBounds(new Rectangle(85, 160, 110, 20));
	labellabelitem.setText("LabelItem:");
	labellabelitem.setBounds(new Rectangle(15, 160, 65, 20));
	labelmaxscale.setBounds(new Rectangle(110, 190, 60, 20));
	labellabelmaxscale.setText("LabelMaxScale:");
	labellabelmaxscale.setBounds(new Rectangle(15, 190, 95, 20));
	labelminscale.setBounds(new Rectangle(275, 190, 60, 20));
	labellabelminscale.setText("LabelMinScale:");
	labellabelminscale.setBounds(new Rectangle(180, 190, 90, 20));
	labelsizeitem.setBounds(new Rectangle(295, 160, 110, 20));
	labellabelsizeitem.setText("LabelSizeItem:");
	labellabelsizeitem.setBounds(new Rectangle(200, 160, 90, 20));
	minscale.setBounds(new Rectangle(240, 225, 60, 20));
	llabelminscale.setText("Escala Mín.:");
	llabelminscale.setBounds(new Rectangle(165, 225, 75, 20));
	maxscale.setBounds(new Rectangle(95, 225, 60, 20));
	llabelmaxscale.setText("Escala Máx.:");
	llabelmaxscale.setBounds(new Rectangle(15, 225, 80, 20));
	labeloffsite.setText("Color transparente (sólo en layers RASTER):");
	labeloffsite.setBounds(new Rectangle(135, 265, 280, 15));
	labeloffsite.setHorizontalAlignment(SwingConstants.CENTER);
	offsite.setBounds(new Rectangle(55, 285, 440, 40));
	labeltoleranceunits.setText("Unidades de Tolerancia:");
	labeltoleranceunits.setBounds(new Rectangle(195, 335, 150, 20));
	toleranceunits.setBounds(new Rectangle(350, 335, 100, 20));
	tolerance.setBounds(new Rectangle(130, 335, 60, 20));
	labeltolerance.setText("Tolerancia:");
	labeltolerance.setBounds(new Rectangle(60, 335, 75, 20));
	this.add(labeltolerance, null);
	this.add(tolerance, null);
	this.add(toleranceunits, null);
	this.add(labeltoleranceunits, null);
	this.add(offsite, null);
	this.add(labeloffsite, null);
	this.add(llabelmaxscale, null);
	this.add(maxscale, null);
	this.add(llabelminscale, null);
	this.add(minscale, null);
	this.add(labellabelsizeitem, null);
	this.add(labelsizeitem, null);
	this.add(labellabelminscale, null);
	this.add(labelminscale, null);
	this.add(labellabelmaxscale, null);
	this.add(labelmaxscale, null);
	this.add(labellabelitem, null);
	this.add(labelitem, null);
	this.add(filteritem, null);
	this.add(labelfilteritem, null);
	this.add(filter, null);
	this.add(labelfilter, null);
	this.add(host, null);
	this.add(labelhost, null);
	this.add(dbname, null);
	this.add(labeldbname, null);
	this.add(user, null);
	this.add(labeluser, null);
	this.add(data, null);
	this.add(labeldata, null);
	this.add(connectiontype, null);
	this.add(labelconnection, null);
	this.add(type, null);
	this.add(labeltype, null);
	this.add(status, null);
	this.add(labelstatus, null);
	this.add(group, null);
	this.add(labelgroup, null);
	this.add(name, null);
	this.add(labelname, null);
	status.addItem("on");
	status.addItem("off");
	status.addItem("default");
	type.addItem("point");
	type.addItem("line");
	type.addItem("polygon");
	type.addItem("circle");
	type.addItem("annotation");
	type.addItem("raster");
	type.addItem("query");
	connectiontype.addItem("");
	connectiontype.addItem("local");
	connectiontype.addItem("sde");
	connectiontype.addItem("ogr");
	connectiontype.addItem("postgis");
	connectiontype.addItem("oraclespatial");
	connectiontype.addItem("wms");
	toleranceunits.addItem("pixels");
	toleranceunits.addItem("feet");
	toleranceunits.addItem("inches");
	toleranceunits.addItem("kilometers");
	toleranceunits.addItem("meters");
	toleranceunits.addItem("miles");
    }

    private void setLayer(Layer _layer) {
    }

    public void setEmpty() {
	this.name.setText("");
	this.group.setText("");
	this.status.setSelectedIndex(0);
	//REVISAR
	this.type.setSelectedIndex(0);
	//REVISAR
	this.connectiontype.setSelectedIndex(0);
	//REVISAR
	this.data.setText("");
	this.user.setText("");
	this.dbname.setText("");
	this.host.setText("");
	this.filter.setText("");
	this.filteritem.setText("");
	this.labelitem.setText("");
	this.labelmaxscale.setText("");
	this.labelminscale.setText("");
	this.labelsizeitem.setText("");
	this.minscale.setText("");
	this.maxscale.setText("");
	this.offsite.setColor(new Color(0, 0, 0));
	this.toleranceunits.setSelectedIndex(0);
	this.tolerance.setText("");
    }

    public Layer getLayer() {
	return layer;
    }

}
