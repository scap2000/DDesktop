package org.digitall.apps.drilling;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.digitall.common.drilling.Coordinate;
import org.digitall.common.drilling.PanelHeader;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.lib.icons.IconTypes;

//

public class UtmCoordinate extends BasicDialog {

    private BasicPanel jpCoordinate = new BasicPanel();
    private JDecEntry tfEastCoordinate = new JDecEntry();
    private JDecEntry tfNorthCoordinate = new JDecEntry();
    private AcceptButton btnAccept = new AcceptButton();
    private CloseButton btnClose = new CloseButton();
    private BasicLabel lblEast = new BasicLabel();
    private BasicLabel lblNorth = new BasicLabel();
    private Coordinate utmCoord;
    private String TEXTO = "Enter UTM Coordinate";
    private PanelHeader panelHeader;
    /* public UtmCoordinate(JDecEntry _tfEastCoordinate, JDecEntry _tfNorthCoordinate) {
        try {
            tfEastCoordinate = _tfEastCoordinate;
            tfNorthCoordinate = _tfNorthCoordinate;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public UtmCoordinate(Coordinate _coordinate) {
	try {
	    utmCoord = _coordinate;
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	this.setSize(new Dimension(431, 157));
	panelHeader = new PanelHeader(this.getWidth(), TEXTO, IconTypes.CR_IconHeaderLogo, IconTypes.CRDrilling_IconHeaderLogo);
	this.getContentPane().add(panelHeader, null);
	this.getContentPane().setLayout(null);
	this.setTitle("UTM Coordinate");
	tfEastCoordinate.setBounds(new Rectangle(20, 15, 180, 20));
	tfNorthCoordinate.setBounds(new Rectangle(225, 15, 180, 25));
	tfNorthCoordinate.setSize(new Dimension(180, 20));
	jpCoordinate.setBounds(new Rectangle(5, 40, 415, 55));
	jpCoordinate.setLayout(null);
	jpCoordinate.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
	btnAccept.setBounds(new Rectangle(380, 100, 40, 25));
	btnAccept.setToolTipText("Accept");
	btnAccept.setSize(new Dimension(40, 25));
	btnAccept.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnAccept_actionPerformed(e);
		    }

		});
	btnClose.setBounds(new Rectangle(5, 100, 50, 30));
	btnClose.setToolTipText("Close");
	btnClose.setSize(new Dimension(40, 25));
	btnClose.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e) {
			btnClose_actionPerformed(e);
		    }

		});
	lblEast.setText("E:");
	lblEast.setBounds(new Rectangle(5, 15, 15, 20));
	lblEast.setFont(new Font("Default", 1, 12));
	lblEast.setHorizontalAlignment(SwingConstants.LEFT);
	lblNorth.setText("N:");
	lblNorth.setBounds(new Rectangle(210, 15, 15, 20));
	lblNorth.setFont(new Font("Default", 1, 12));
	lblNorth.setHorizontalAlignment(SwingConstants.LEFT);
	jpCoordinate.add(tfEastCoordinate, null);
	jpCoordinate.add(tfNorthCoordinate, null);
	jpCoordinate.add(lblNorth, null);
	jpCoordinate.add(lblEast, null);
	this.getContentPane().add(btnClose, null);
	this.getContentPane().add(btnAccept, null);
	this.getContentPane().add(jpCoordinate, null);
	org.digitall.lib.components.ComponentsManager.centerWindow(this);
	if (utmCoord.isNull()) {
	    tfEastCoordinate.setText("");
	    tfNorthCoordinate.setText("");
	} else {
	    tfEastCoordinate.setText(String.valueOf(utmCoord.getX()));
	    tfNorthCoordinate.setText(String.valueOf(utmCoord.getY()));
	}
    }

    private void btnClose_actionPerformed(ActionEvent e) {
	this.dispose();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
	//validar datos
	if ((tfEastCoordinate.getText().trim().length() == 0) || (tfNorthCoordinate.getText().trim().length() == 0)) {
	    utmCoord.setNull(true);
	} else {
	    utmCoord.setLocation(new Point2D.Double(Double.parseDouble(0 + tfEastCoordinate.getText()), Double.parseDouble(0 + tfNorthCoordinate.getText())));
	}
	this.dispose();
    }

}
