package org.digitall.apps.mallaapp;

import java.awt.Dimension;

import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import org.cardero.DrawDesk;
import org.digitall.lib.misc.Proced;

public class MallaWzdStep1 extends MallaWizardStep {

    private JLabel statusBar = new JLabel();
    private DrawDesk panel;
    private JLabel logocr = new JLabel(new ImageIcon(Proced.class.getResource("iconos/logocr.jpg")));
    private JLabel logodgl = new JLabel(new ImageIcon(Proced.class.getResource("iconos/logodgl.jpg")));
    private boolean firstLoad = true;

    public MallaWzdStep1() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setTitle("Prueba");
        this.setSize(new Dimension(840, 722));
        this.getContentPane().setLayout(null);
        logocr.setBounds(new Rectangle(185, 615, 220, 50));
        logodgl.setBounds(new Rectangle(450, 615, 220, 50));
        statusBar.setBounds(new Rectangle(0, 670, 830, 20));
        statusBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        this.getContentPane().add(statusBar, null);
        this.getContentPane().add(logocr, null);
        this.getContentPane().add(logodgl, null);
    }
    
    public boolean hasPolygons() {
        return panel.hasPolygons();
    }
    
    public void mostrar() {
        int idProject = getProject().getIdproject();
        System.out.println("IDPROJECT: " + idProject);
        if (idProject != -1) {
            if (firstLoad) {
                panel = new DrawDesk(statusBar, idProject);
                panel.setBounds(new Rectangle(15, 15, 800, 600));
                this.getContentPane().add(panel, null);
                firstLoad = false;
            }
        }
        setVisible(true);
    }
}
