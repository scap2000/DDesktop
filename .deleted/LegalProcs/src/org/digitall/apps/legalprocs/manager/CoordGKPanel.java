package org.digitall.apps.legalprocs.manager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.digitall.apps.legalprocs.manager.classes.ERPolygon;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.JDecEntry;
import org.digitall.lib.components.basic.BasicCheckBox;
import org.digitall.lib.components.basic.BasicContainerPanel;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;

public class CoordGKPanel extends BasicContainerPanel {

    private BasicLabel lblYPP = new BasicLabel();
    private BasicLabel jLabel27 = new BasicLabel();
    private BasicLabel jLabel26 = new BasicLabel();
    private BasicLabel jLabel25 = new BasicLabel();
    private BasicLabel jLabel24 = new BasicLabel();
    private BasicLabel jLabel23 = new BasicLabel();
    private BasicLabel jLabel22 = new BasicLabel();
    private BasicLabel jLabel21 = new BasicLabel();
    private BasicLabel jLabel111 = new BasicLabel();
    private BasicLabel jLabel19 = new BasicLabel();
    private BasicLabel jLabel17 = new BasicLabel();
    private BasicLabel jLabel16 = new BasicLabel();
    private BasicLabel jLabel15 = new BasicLabel();
    private BasicLabel jLabel13 = new BasicLabel();
    private BasicLabel jLabel8 = new BasicLabel();
    private BasicLabel jLabel7 = new BasicLabel();
    private BasicLabel jLabel6 = new BasicLabel();
    private BasicLabel jLabel5 = new BasicLabel();
    private BasicLabel jLabel4 = new BasicLabel();
    private BasicLabel jLabel3 = new BasicLabel();
    private BasicLabel jLabel2 = new BasicLabel();
    private BasicLabel lblXPP = new BasicLabel();
    private BasicLabel jLabel210 = new BasicLabel();
    private BasicLabel jLabel29 = new BasicLabel();
    private BasicLabel jLabel12 = new BasicLabel();
    private BasicLabel jLabel11 = new BasicLabel();
    private int cantPuntos = 30;
    private JDecEntry puntosx[] = new JDecEntry[cantPuntos];
    private JDecEntry puntosy[] = new JDecEntry[cantPuntos];
    private BasicCheckBox chkPoint[] = new BasicCheckBox[cantPuntos];
    private JDecEntry tfArea = new JDecEntry();
    private BasicLabel jLabel9 = new BasicLabel();
    private BasicLabel jLabel14 = new BasicLabel();
    private BasicLabel jLabel18 = new BasicLabel();
    private BasicLabel jLabel110 = new BasicLabel();
    private BasicLabel jLabel10 = new BasicLabel();
    private BasicLabel jLabel211 = new BasicLabel();
    private BasicLabel jLabel112 = new BasicLabel();
    private BasicLabel jLabel113 = new BasicLabel();
    private BasicLabel jLabel114 = new BasicLabel();
    private BasicPanel jPanel1 = new BasicPanel();
    private BasicLabel jLabel20 = new BasicLabel();
    private BasicLabel jLabel30 = new BasicLabel();
    private BasicPanel jPanel2 = new BasicPanel();
    private BasicCheckBox chkSegunda = new BasicCheckBox();
    private BasicCheckBox chkPrimera = new BasicCheckBox();
    private int pointsSize, quantityPoints;
    private boolean permitir = true;
    //private ExplorationRequestClass_Old prospection;
    private ERPolygon erPolygonClass;
    private BasicLabel jLabel115 = new BasicLabel();
    private BasicLabel jLabel116 = new BasicLabel();
    private BasicLabel jLabel212 = new BasicLabel();
    private BasicLabel jLabel31 = new BasicLabel();
    private BasicLabel jLabel213 = new BasicLabel();
    private BasicLabel jLabel32 = new BasicLabel();
    private BasicLabel jLabel214 = new BasicLabel();
    private BasicLabel jLabel33 = new BasicLabel();
    private BasicLabel jLabel215 = new BasicLabel();
    private BasicLabel jLabel34 = new BasicLabel();
    private BasicLabel lblY15 = new BasicLabel();
    private BasicLabel lblX15 = new BasicLabel();
    private BasicLabel jLabel117 = new BasicLabel();
    private BasicLabel jLabel118 = new BasicLabel();
    private BasicLabel jLabel119 = new BasicLabel();
    private BasicLabel jLabel28 = new BasicLabel();
    private BasicLabel jLabel120 = new BasicLabel();
    private BasicLabel jLabel121 = new BasicLabel();
    private BasicLabel jLabel122 = new BasicLabel();
    private BasicLabel jLabel35 = new BasicLabel();
    private BasicLabel jLabel216 = new BasicLabel();
    private BasicLabel jLabel36 = new BasicLabel();
    private BasicLabel jLabel217 = new BasicLabel();
    private BasicLabel jLabel37 = new BasicLabel();
    private BasicLabel jLabel1110 = new BasicLabel();
    private BasicLabel jLabel123 = new BasicLabel();
    private BasicLabel jLabel1111 = new BasicLabel();
    private BasicLabel jLabel124 = new BasicLabel();
    private BasicLabel jLabel1112 = new BasicLabel();
    private BasicLabel jLabel1113 = new BasicLabel();
    private BasicLabel jLabel218 = new BasicLabel();
    private BasicLabel jLabel125 = new BasicLabel();

    public CoordGKPanel() {
	try {
	    jbInit();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void jbInit() throws Exception {
	int horizPos = 25;
	int horizPos2 = 25;
	for (int i = 0; i < cantPuntos; i++) {
	    puntosx[i] = new JDecEntry();
	    puntosy[i] = new JDecEntry();
	    chkPoint[i] = new BasicCheckBox();
	    if (i < 15) {
		puntosx[i].setBounds(new Rectangle(90, horizPos, 105, 15));
		jPanel1.add(puntosx[i], null);
		puntosy[i].setBounds(new Rectangle(245, horizPos, 105, 15));
		jPanel1.add(puntosy[i], null);
		chkPoint[i].setBounds(new Rectangle(35, horizPos, 17, 20));
		jPanel1.add(chkPoint[i], null);
		horizPos += 20;
	    } else {
		puntosx[i].setBounds(new Rectangle(430, horizPos2, 105, 15));
		jPanel1.add(puntosx[i], null);
		puntosy[i].setBounds(new Rectangle(585, horizPos2, 105, 15));
		jPanel1.add(puntosy[i], null);
		chkPoint[i].setBounds(new Rectangle(385, horizPos2, 17, 20));
		jPanel1.add(chkPoint[i], null);
		horizPos2 += 20;
	    }
	}
	this.setLayout(null);
	this.setSize(new Dimension(760, 402));
	lblYPP.setText("P.P");
	lblYPP.setBounds(new Rectangle(215, 30, 25, 15));
	lblYPP.setFont(new Font("Utopia", 1, 11));
	lblYPP.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel27.setText("1");
	jLabel27.setBounds(new Rectangle(215, 45, 25, 15));
	jLabel27.setFont(new Font("Utopia", 1, 11));
	jLabel27.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel26.setText("3");
	jLabel26.setBounds(new Rectangle(215, 85, 25, 15));
	jLabel26.setFont(new Font("Utopia", 1, 11));
	jLabel26.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel25.setText("2");
	jLabel25.setBounds(new Rectangle(215, 65, 25, 15));
	jLabel25.setFont(new Font("Utopia", 1, 11));
	jLabel25.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel24.setText("6");
	jLabel24.setBounds(new Rectangle(215, 145, 25, 15));
	jLabel24.setFont(new Font("Utopia", 1, 11));
	jLabel24.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel23.setText("7");
	jLabel23.setBounds(new Rectangle(215, 165, 25, 15));
	jLabel23.setFont(new Font("Utopia", 1, 11));
	jLabel23.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel22.setText("5");
	jLabel22.setBounds(new Rectangle(215, 125, 25, 15));
	jLabel22.setFont(new Font("Utopia", 1, 11));
	jLabel22.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel21.setText("4");
	jLabel21.setBounds(new Rectangle(215, 105, 25, 15));
	jLabel21.setFont(new Font("Utopia", 1, 11));
	jLabel21.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel111.setText("10");
	jLabel111.setBounds(new Rectangle(215, 225, 25, 15));
	jLabel111.setFont(new Font("Utopia", 1, 11));
	jLabel111.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel19.setText("9");
	jLabel19.setBounds(new Rectangle(215, 205, 25, 15));
	jLabel19.setFont(new Font("Utopia", 1, 11));
	jLabel19.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel17.setText("8");
	jLabel17.setBounds(new Rectangle(215, 185, 25, 15));
	jLabel17.setFont(new Font("Utopia", 1, 11));
	jLabel17.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel12.setText("Y");
	jLabel12.setBounds(new Rectangle(245, 10, 105, 15));
	jLabel12.setFont(new Font("Utopia", 1, 11));
	jLabel12.setHorizontalAlignment(SwingConstants.CENTER);
	jLabel11.setText("X");
	jLabel11.setBounds(new Rectangle(90, 10, 105, 15));
	jLabel11.setFont(new Font("Utopia", 1, 11));
	jLabel11.setHorizontalAlignment(SwingConstants.CENTER);
	tfArea.setBounds(new Rectangle(485, 350, 145, 20));
	tfArea.setText("0");
	jLabel9.setText("Superficie (Has.):");
	jLabel9.setBounds(new Rectangle(485, 335, 145, 15));
	jLabel9.setFont(new Font("Default", 1, 11));
	jLabel14.setText("14");
	jLabel14.setBounds(new Rectangle(55, 305, 25, 15));
	jLabel14.setFont(new Font("Utopia", 1, 11));
	jLabel14.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel18.setText("13");
	jLabel18.setBounds(new Rectangle(55, 285, 25, 15));
	jLabel18.setFont(new Font("Utopia", 1, 11));
	jLabel18.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel110.setText("12");
	jLabel110.setBounds(new Rectangle(55, 265, 25, 15));
	jLabel110.setFont(new Font("Utopia", 1, 11));
	jLabel110.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel10.setText("11");
	jLabel10.setBounds(new Rectangle(55, 245, 25, 15));
	jLabel10.setFont(new Font("Utopia", 1, 11));
	jLabel10.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel211.setText("11");
	jLabel211.setBounds(new Rectangle(215, 245, 25, 15));
	jLabel211.setFont(new Font("Utopia", 1, 11));
	jLabel211.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel112.setText("12");
	jLabel112.setBounds(new Rectangle(215, 265, 25, 15));
	jLabel112.setFont(new Font("Utopia", 1, 11));
	jLabel112.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel113.setText("13");
	jLabel113.setBounds(new Rectangle(215, 285, 25, 15));
	jLabel113.setFont(new Font("Utopia", 1, 11));
	jLabel113.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel114.setText("14");
	jLabel114.setBounds(new Rectangle(215, 305, 25, 15));
	jLabel114.setFont(new Font("Utopia", 1, 11));
	jLabel114.setHorizontalAlignment(SwingConstants.RIGHT);
	jPanel1.setBounds(new Rectangle(5, 10, 745, 385));
	jPanel1.setLayout(null);
	jPanel1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	jPanel1.setSize(new Dimension(750, 385));
	jLabel20.setText(" Coordenadas Gauss Krüger de la Zona a Explorar");
	jLabel20.setBounds(new Rectangle(15, 0, 315, 15));
	jLabel20.setFont(new Font("Default", 1, 11));
	jLabel20.setOpaque(false);
	jLabel20.setSize(new Dimension(306, 13));
	jLabel30.setText(" Categoría de los minerales a explotar");
	jLabel30.setBounds(new Rectangle(95, 330, 234, 14));
	jLabel30.setFont(new Font("Default", 1, 11));
	jLabel30.setOpaque(false);
	jLabel30.setSize(new Dimension(234, 14));
	jPanel2.setBounds(new Rectangle(90, 340, 290, 35));
	jPanel2.setLayout(null);
	jPanel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
	chkSegunda.setText("2da.");
	chkSegunda.setBounds(new Rectangle(165, 10, 95, 20));
	chkPrimera.setText("1ra.");
	chkPrimera.setBounds(new Rectangle(50, 10, 80, 20));
	jLabel16.setText("8");
	jLabel16.setBounds(new Rectangle(55, 185, 25, 15));
	jLabel16.setFont(new Font("Utopia", 1, 11));
	jLabel16.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel15.setText("9");
	jLabel15.setBounds(new Rectangle(55, 205, 25, 15));
	jLabel15.setFont(new Font("Utopia", 1, 11));
	jLabel15.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel13.setText("10");
	jLabel13.setBounds(new Rectangle(55, 225, 25, 15));
	jLabel13.setFont(new Font("Utopia", 1, 11));
	jLabel13.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel8.setText("4");
	jLabel8.setBounds(new Rectangle(55, 105, 25, 15));
	jLabel8.setFont(new Font("Utopia", 1, 11));
	jLabel8.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel7.setText("5");
	jLabel7.setBounds(new Rectangle(55, 125, 25, 15));
	jLabel7.setFont(new Font("Utopia", 1, 11));
	jLabel7.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel6.setText("7");
	jLabel6.setBounds(new Rectangle(55, 165, 25, 15));
	jLabel6.setFont(new Font("Utopia", 1, 11));
	jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel5.setText("6");
	jLabel5.setBounds(new Rectangle(55, 145, 25, 15));
	jLabel5.setFont(new Font("Utopia", 1, 11));
	jLabel5.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel4.setText("2");
	jLabel4.setBounds(new Rectangle(55, 65, 25, 15));
	jLabel4.setFont(new Font("Utopia", 1, 11));
	jLabel4.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel3.setText("3");
	jLabel3.setBounds(new Rectangle(55, 85, 25, 15));
	jLabel3.setFont(new Font("Utopia", 1, 11));
	jLabel3.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel2.setText("1");
	jLabel2.setBounds(new Rectangle(55, 45, 25, 15));
	jLabel2.setFont(new Font("Utopia", 1, 11));
	jLabel2.setHorizontalAlignment(SwingConstants.RIGHT);
	lblXPP.setText("P.P");
	lblXPP.setBounds(new Rectangle(55, 30, 25, 15));
	lblXPP.setFont(new Font("Utopia", 1, 11));
	lblXPP.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel210.setText("(Art.46 Código de Mineria)");
	jLabel210.setBounds(new Rectangle(375, 290, 155, 15));
	jLabel210.setFont(new Font("Default", 0, 11));
	jLabel210.setHorizontalAlignment(SwingConstants.LEFT);
	jLabel29.setText("Superficie (Has.)");
	jLabel29.setBounds(new Rectangle(375, 250, 155, 15));
	jLabel29.setFont(new Font("Default", 1, 11));
	jLabel29.setHorizontalAlignment(SwingConstants.LEFT);
	jPanel2.add(chkPrimera, null);
	jPanel2.add(chkSegunda, null);
	jPanel1.add(jLabel125, null);
	jPanel1.add(jLabel218, null);
	jPanel1.add(jLabel1113, null);
	jPanel1.add(jLabel1112, null);
	jPanel1.add(jLabel124, null);
	jPanel1.add(jLabel1111, null);
	jPanel1.add(jLabel123, null);
	jPanel1.add(jLabel1110, null);
	jPanel1.add(jLabel37, null);
	jPanel1.add(jLabel217, null);
	jPanel1.add(jLabel36, null);
	jPanel1.add(jLabel216, null);
	jPanel1.add(jLabel35, null);
	jPanel1.add(jLabel122, null);
	jPanel1.add(jLabel121, null);
	jPanel1.add(jLabel120, null);
	jPanel1.add(jLabel28, null);
	jPanel1.add(jLabel119, null);
	jPanel1.add(jLabel118, null);
	jPanel1.add(jLabel117, null);
	jPanel1.add(lblX15, null);
	jPanel1.add(lblY15, null);
	jPanel1.add(jLabel34, null);
	jPanel1.add(jLabel215, null);
	jPanel1.add(jLabel33, null);
	jPanel1.add(jLabel214, null);
	jPanel1.add(jLabel32, null);
	jPanel1.add(jLabel213, null);
	jPanel1.add(jLabel31, null);
	jPanel1.add(jLabel212, null);
	jPanel1.add(jLabel116, null);
	jPanel1.add(jLabel115, null);
	jPanel1.add(jLabel30, null);
	jPanel1.add(jPanel2, null);
	jPanel1.add(jLabel114, null);
	jPanel1.add(jLabel113, null);
	jPanel1.add(jLabel112, null);
	jPanel1.add(jLabel211, null);
	jPanel1.add(jLabel10, null);
	jPanel1.add(jLabel110, null);
	jPanel1.add(jLabel18, null);
	jPanel1.add(jLabel14, null);
	jPanel1.add(jLabel9, null);
	jPanel1.add(tfArea, null);
	jPanel1.add(lblXPP, null);
	jPanel1.add(jLabel2, null);
	jPanel1.add(jLabel3, null);
	jPanel1.add(jLabel4, null);
	jPanel1.add(jLabel5, null);
	jPanel1.add(jLabel6, null);
	jPanel1.add(jLabel7, null);
	jPanel1.add(jLabel8, null);
	jPanel1.add(jLabel13, null);
	jPanel1.add(jLabel15, null);
	jPanel1.add(jLabel16, null);
	jPanel1.add(jLabel11, null);
	jPanel1.add(jLabel12, null);
	jPanel1.add(jLabel17, null);
	jPanel1.add(jLabel19, null);
	jPanel1.add(jLabel111, null);
	jPanel1.add(jLabel21, null);
	jPanel1.add(jLabel22, null);
	jPanel1.add(jLabel23, null);
	jPanel1.add(jLabel24, null);
	jPanel1.add(jLabel25, null);
	jPanel1.add(jLabel26, null);
	jPanel1.add(jLabel27, null);
	jPanel1.add(lblYPP, null);
	this.add(jLabel20, null);
	this.add(jPanel1, null);
	//grupo.add(chkPrimera);
	//grupo.add(chkSegunda);
	chkPrimera.setSelected(true);
	jLabel115.setText("Y");
	jLabel115.setBounds(new Rectangle(590, 15, 105, 15));
	jLabel115.setFont(new Font("Utopia", 1, 11));
	jLabel115.setHorizontalAlignment(SwingConstants.CENTER);
	jLabel116.setText("X");
	jLabel116.setBounds(new Rectangle(435, 15, 105, 15));
	jLabel116.setFont(new Font("Utopia", 1, 11));
	jLabel116.setHorizontalAlignment(SwingConstants.CENTER);
	jLabel212.setText("19");
	jLabel212.setBounds(new Rectangle(555, 105, 25, 15));
	jLabel212.setFont(new Font("Utopia", 1, 11));
	jLabel212.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel31.setText("19");
	jLabel31.setBounds(new Rectangle(400, 105, 25, 15));
	jLabel31.setFont(new Font("Utopia", 1, 11));
	jLabel31.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel213.setText("18");
	jLabel213.setBounds(new Rectangle(555, 85, 25, 15));
	jLabel213.setFont(new Font("Utopia", 1, 11));
	jLabel213.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel32.setText("18");
	jLabel32.setBounds(new Rectangle(400, 85, 25, 15));
	jLabel32.setFont(new Font("Utopia", 1, 11));
	jLabel32.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel214.setText("17");
	jLabel214.setBounds(new Rectangle(555, 65, 25, 15));
	jLabel214.setFont(new Font("Utopia", 1, 11));
	jLabel214.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel33.setText("17");
	jLabel33.setBounds(new Rectangle(400, 65, 25, 15));
	jLabel33.setFont(new Font("Utopia", 1, 11));
	jLabel33.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel215.setText("16");
	jLabel215.setBounds(new Rectangle(555, 45, 25, 15));
	jLabel215.setFont(new Font("Utopia", 1, 11));
	jLabel215.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel34.setText("16");
	jLabel34.setBounds(new Rectangle(400, 45, 25, 15));
	jLabel34.setFont(new Font("Utopia", 1, 11));
	jLabel34.setHorizontalAlignment(SwingConstants.RIGHT);
	lblY15.setText("15");
	lblY15.setBounds(new Rectangle(555, 30, 25, 15));
	lblY15.setFont(new Font("Utopia", 1, 11));
	lblY15.setHorizontalAlignment(SwingConstants.RIGHT);
	lblX15.setText("15");
	lblX15.setBounds(new Rectangle(400, 30, 25, 15));
	lblX15.setFont(new Font("Utopia", 1, 11));
	lblX15.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel117.setText("25");
	jLabel117.setBounds(new Rectangle(555, 225, 25, 15));
	jLabel117.setFont(new Font("Utopia", 1, 11));
	jLabel117.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel118.setText("24");
	jLabel118.setBounds(new Rectangle(555, 205, 25, 15));
	jLabel118.setFont(new Font("Utopia", 1, 11));
	jLabel118.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel119.setText("23");
	jLabel119.setBounds(new Rectangle(555, 185, 25, 15));
	jLabel119.setFont(new Font("Utopia", 1, 11));
	jLabel119.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel28.setText("22");
	jLabel28.setBounds(new Rectangle(555, 165, 25, 15));
	jLabel28.setFont(new Font("Utopia", 1, 11));
	jLabel28.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel120.setText("25");
	jLabel120.setBounds(new Rectangle(400, 225, 25, 15));
	jLabel120.setFont(new Font("Utopia", 1, 11));
	jLabel120.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel121.setText("24");
	jLabel121.setBounds(new Rectangle(400, 205, 25, 15));
	jLabel121.setFont(new Font("Utopia", 1, 11));
	jLabel121.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel122.setText("23");
	jLabel122.setBounds(new Rectangle(400, 185, 25, 15));
	jLabel122.setFont(new Font("Utopia", 1, 11));
	jLabel122.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel35.setText("22");
	jLabel35.setBounds(new Rectangle(400, 165, 25, 15));
	jLabel35.setFont(new Font("Utopia", 1, 11));
	jLabel35.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel216.setText("21");
	jLabel216.setBounds(new Rectangle(555, 145, 25, 15));
	jLabel216.setFont(new Font("Utopia", 1, 11));
	jLabel216.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel36.setText("21");
	jLabel36.setBounds(new Rectangle(400, 145, 25, 15));
	jLabel36.setFont(new Font("Utopia", 1, 11));
	jLabel36.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel217.setText("20");
	jLabel217.setBounds(new Rectangle(555, 125, 25, 15));
	jLabel217.setFont(new Font("Utopia", 1, 11));
	jLabel217.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel37.setText("20");
	jLabel37.setBounds(new Rectangle(400, 125, 25, 15));
	jLabel37.setFont(new Font("Utopia", 1, 11));
	jLabel37.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel1110.setText("29");
	jLabel1110.setBounds(new Rectangle(555, 305, 25, 15));
	jLabel1110.setFont(new Font("Utopia", 1, 11));
	jLabel1110.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel123.setText("29");
	jLabel123.setBounds(new Rectangle(400, 305, 25, 15));
	jLabel123.setFont(new Font("Utopia", 1, 11));
	jLabel123.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel1111.setText("28");
	jLabel1111.setBounds(new Rectangle(555, 285, 25, 15));
	jLabel1111.setFont(new Font("Utopia", 1, 11));
	jLabel1111.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel124.setText("28");
	jLabel124.setBounds(new Rectangle(400, 285, 25, 15));
	jLabel124.setFont(new Font("Utopia", 1, 11));
	jLabel124.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel1112.setText("27");
	jLabel1112.setBounds(new Rectangle(555, 265, 25, 15));
	jLabel1112.setFont(new Font("Utopia", 1, 11));
	jLabel1112.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel1113.setText("27");
	jLabel1113.setBounds(new Rectangle(400, 265, 25, 15));
	jLabel1113.setFont(new Font("Utopia", 1, 11));
	jLabel1113.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel218.setText("26");
	jLabel218.setBounds(new Rectangle(555, 245, 25, 15));
	jLabel218.setFont(new Font("Utopia", 1, 11));
	jLabel218.setHorizontalAlignment(SwingConstants.RIGHT);
	jLabel125.setText("26");
	jLabel125.setBounds(new Rectangle(400, 245, 25, 15));
	jLabel125.setFont(new Font("Utopia", 1, 11));
	jLabel125.setHorizontalAlignment(SwingConstants.RIGHT);
	inhabilitarPuntos();
	chkPoint[0].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint0_actionPerformed(e);
				   }

			       }
	);
	chkPoint[1].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 1);
				   }

			       }
	);
	chkPoint[2].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 2);
				   }

			       }
	);
	chkPoint[3].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 3);
				   }

			       }
	);
	chkPoint[4].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 4);
				   }

			       }
	);
	chkPoint[5].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 5);
				   }

			       }
	);
	chkPoint[6].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 6);
				   }

			       }
	);
	chkPoint[7].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 7);
				   }

			       }
	);
	chkPoint[8].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 8);
				   }

			       }
	);
	chkPoint[9].addActionListener(new ActionListener() {

				   public void actionPerformed(ActionEvent e) {
				       chkPoint1_actionPerformed(e, 9);
				   }

			       }
	);
	chkPoint[10].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 10);
				    }

				}
	);
	chkPoint[11].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 11);
				    }

				}
	);
	chkPoint[12].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 12);
				    }

				}
	);
	chkPoint[13].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 13);
				    }

				}
	);
	chkPoint[14].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 14);
				    }

				}
	);
	chkPoint[15].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 15);
				    }

				}
	);
	chkPoint[16].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 16);
				    }

				}
	);
	chkPoint[17].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 17);
				    }

				}
	);
	chkPoint[18].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 18);
				    }

				}
	);
	chkPoint[19].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 19);
				    }

				}
	);
	chkPoint[20].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 20);
				    }

				}
	);
	chkPoint[21].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 21);
				    }

				}
	);
	chkPoint[22].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 22);
				    }

				}
	);
	chkPoint[23].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 23);
				    }

				}
	);
	chkPoint[24].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 24);
				    }

				}
	);
	chkPoint[25].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 25);
				    }

				}
	);
	chkPoint[26].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 26);
				    }

				}
	);
	chkPoint[27].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 27);
				    }

				}
	);
	chkPoint[28].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 28);
				    }

				}
	);
	chkPoint[29].addActionListener(new ActionListener() {

				    public void actionPerformed(ActionEvent e) {
					chkPoint1_actionPerformed(e, 29);
				    }

				}
	);
	//this.setVerticalScrollBarPolicy(BasicScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	//loadPolygon();
    }

    private void chkPoint0_actionPerformed(ActionEvent e) {
	if (chkPoint[0].isSelected()) {
	    puntosx[0].setEnabled(true);
	    puntosy[0].setEnabled(true);
	} else {
	    for (int i = 0; i < cantPuntos; i++) {
		puntosx[i].setEnabled(false);
		puntosy[i].setEnabled(false);
		chkPoint[i].setSelected(false);
	    }
	}
    }

    private void chkPoint1_actionPerformed(ActionEvent e, int _chek) {
	if (chkPoint[_chek].isSelected()) {
	    if (chkPoint[_chek - 1].isSelected()) {
		// pregunto si esta seleccionado el anterior --> permito registrar el punto
		puntosx[_chek].setEnabled(true);
		puntosy[_chek].setEnabled(true);
	    } else {
		// si no esta seleccionado el punto anterior --> no permitir cargar el punto
		puntosx[_chek].setEnabled(false);
		puntosy[_chek].setEnabled(false);
		chkPoint[_chek].setSelected(false);
		Advisor.messageBox("No tiene asignado el punto anterior", "Error");
	    }
	} else {
	    // caso en que deseo deshabilitar un chek
	    //for (int i = _chek; i < 15; i++) {
	    for (int i = _chek; i < cantPuntos; i++) {
		puntosx[i].setEnabled(false);
		puntosy[i].setEnabled(false);
		chkPoint[i].setSelected(false);
	    }
	}
    }

    private void chkPoint14_actionPerformed(ActionEvent e) {
	if (chkPoint[29].isSelected()) {
	    if (chkPoint[28].isSelected()) {
		//progunto si el punto 13 esta seleccionado --> permito cargar el pto. 14
		puntosx[29].setEnabled(true);
		puntosy[29].setEnabled(true);
	    } else {
		//Si el punto 13 no esta seleccionado --> no permito cargar el punto 14
		chkPoint[29].setSelected(false);
		puntosx[29].setEnabled(false);
		puntosy[29].setEnabled(false);
		Advisor.messageBox("No tiene asignado el punto anterior", "Error");
	    }
	} else {
	    // Permito desseleccionar el punto 14
	    puntosx[29].setText("");
	    puntosy[29].setText("");
	    puntosx[29].setEnabled(false);
	    puntosy[29].setEnabled(false);
	}
    }

    public String getMinCat() {
	String categoria = "";
	if (chkPrimera.isSelected() && chkSegunda.isSelected()) {
	    categoria = "1ra&2da";
	} else if (chkPrimera.isSelected() && (!chkSegunda.isSelected())) {
	    categoria = "1ra";
	} else if ((!chkPrimera.isSelected()) && chkSegunda.isSelected()) {
	    categoria = "2da";
	} else {
	    categoria = "";
	}
	return categoria;
    }

    public Point2D[] getPoints() {
	int numPoints = 0;
	for (int i = 0; i < cantPuntos; i++) {
	    if (chkPoint[i].isSelected()) {
		numPoints++;
	    }
	}
	quantityPoints = numPoints;
	Point2D[] points = new Point2D[numPoints];
	if (control(numPoints)) {
	    for (int i = 0; i < numPoints; i++) {
		double x = Double.parseDouble("0" + puntosx[i].getText());
		double y = Double.parseDouble("0" + puntosy[i].getText());
		points[i] = new Point2D.Double(x, y);
	    }
	    permitir = true;
	} else {
	    //org.digitall.lib.components.Advisor.messageBox("Existe un punto al cual no se le asigno un valor","Error");
	    permitir = false;
	}
	pointsSize = points.length;
	return points;
    }

    private boolean control(int _numPoints) {
	boolean respuesta = true;
	String x = "";
	String y = "";
	int i = 0;
	while ((i < _numPoints) && (respuesta)) {
	    x = puntosx[i].getText();
	    y = puntosy[i].getText();
	    if ((!x.equals("")) && (!y.equals(""))) {
		respuesta = true;
	    } else {
		respuesta = false;
	    }
	    i++;
	}
	return respuesta;
    }

    private void inhabilitarPuntos() {
	for (int i = 0; i < cantPuntos; i++) {
	    chkPoint[i].setSelected(false);
	    puntosx[i].setEnabled(false);
	    puntosy[i].setEnabled(false);
	}
    }

    public String getArea() {
	return tfArea.getText().toString();
    }

    public int getPointsSize() {
	return pointsSize;
    }

    public boolean getPermimtir() {
	return permitir;
    }

    public String getquantityPoints() {
	return String.valueOf(quantityPoints);
    }

    public void loadPolygon() {
	if (erPolygonClass.getQuantityPoints() != 0) {
	    for (int i = 0; i < erPolygonClass.getQuantityPoints() - 1; i++) {
		chkPoint[i].setSelected(true);
		puntosx[i].setText(String.valueOf(erPolygonClass.getXd(i)));
		puntosx[i].setEnabled(true);
		puntosy[i].setText(String.valueOf(erPolygonClass.getYd(i)));
		puntosy[i].setEnabled(true);
	    }
	    if (erPolygonClass.getAreaAux() == -1) {
		tfArea.setText("0");
	    } else {
		tfArea.setText(String.valueOf(erPolygonClass.getAreaAux()));
	    }
	    loadMineralCategory(erPolygonClass.getMineralcategoryAux());
	} else {
	}
    }

    public void setERPolygonObject(ERPolygon _erPolygonClass) {
	erPolygonClass = _erPolygonClass;
	if (erPolygonClass.getIdReference() != -1) {
	    if (erPolygonClass.getAreaAux() == -1) {
		erPolygonClass.retrieveData();
	    }
	    loadPolygon();
	}
    }

    private void loadMineralCategory(String _mineralCategory) {
	if (_mineralCategory.equals("1ra&2da")) {
	    chkPrimera.setSelected(true);
	    chkSegunda.setSelected(true);
	} else if (_mineralCategory.equals("1ra")) {
	    chkPrimera.setSelected(true);
	    chkSegunda.setSelected(false);
	} else if (_mineralCategory.equals("2da")) {
	    chkPrimera.setSelected(false);
	    chkSegunda.setSelected(true);
	} else {
	    chkPrimera.setSelected(false);
	    chkSegunda.setSelected(false);
	}
    }

}
