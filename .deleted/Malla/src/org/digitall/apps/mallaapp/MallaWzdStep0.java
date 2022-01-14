package org.digitall.apps.mallaapp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.geo.shapefile.SHPFilter;
import org.digitall.lib.geo.shapefile.SHPPolygon;
import org.digitall.lib.geo.shapefile.SHPReader;
import org.digitall.lib.geo.shapefile.ShapeTypes;

public class MallaWzdStep0 extends MallaWizardStep {
//public class MallaWzdStep0 extends JDialog {

    private JButton bAbrirSHP = new JButton();
    private JButton bAbrirProyecto = new JButton();
    private Vector shapes = new Vector();
    private int shapeType = -1;
    private JLabel jlFileName = new JLabel();
    private JLabel jlShapes = new JLabel();
    private JLabel jlProjectName = new JLabel();
    private JRadioButton jrFromFile = new JRadioButton();
    private JRadioButton jrFromProject = new JRadioButton();
    private JButton jbImportar = new JButton();
    private MallaProject project = new MallaProject();
    private JTextField jtProjectName = new JTextField();
    private JTextField jtDescription = new JTextField();
    private JLabel jlName = new JLabel();
    private JLabel jlDescription = new JLabel();
    private JButton bSiguiente = new JButton();
    private JButton bSalir = new JButton();
    private boolean autoCloseRings = false;

    public MallaWzdStep0() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout( null );
        this.setSize(new Dimension(400, 299));
        this.setTitle("Malla - Pantalla principal");
        this.setResizable(false);
        bAbrirSHP.setText("Abrir un SHP");
        bAbrirSHP.setBounds(new Rectangle(5, 80, 130, 20));
        bAbrirSHP.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            bAbrirSHP_actionPerformed(e);
                                        }
                                    }
        );
        bAbrirProyecto.setText("Abrir un Proyecto");
        bAbrirProyecto.setBounds(new Rectangle(5, 80, 130, 20));
        bAbrirProyecto.addActionListener(new ActionListener() {
                                             public void actionPerformed(ActionEvent e) {
                                                 bAbrirProyecto_actionPerformed(e);
                                             }
                                         }
        );
        jlFileName.setText("File: None");
        jlFileName.setBounds(new Rectangle(145, 80, 240, 20));
        jlFileName.setBorder(BorderFactory.createEtchedBorder(EtchedBorder
                                                               .RAISED));
        jlShapes.setText("Shape(s): 0");
        jlShapes.setBounds(new Rectangle(5, 105, 380, 20));
        jlShapes.setBorder(BorderFactory.createEtchedBorder(EtchedBorder
                                                               .RAISED));
        jlProjectName.setText("Project: None");
        jlProjectName.setBounds(new Rectangle(145, 80, 240, 20));
        jlProjectName.setBorder(BorderFactory.createEtchedBorder(EtchedBorder
                                                               .RAISED));
        jrFromFile.setText("Desde un SHP");
        jrFromFile.setBounds(new Rectangle(20, 25, 180, 20));
        jrFromProject.setText("Desde un Proyecto");
        jrFromProject.setBounds(new Rectangle(20, 50, 180, 20));
        jrFromProject.setEnabled(false);
        jrFromProject.addActionListener(new ActionListener() {
                                            public void actionPerformed(ActionEvent e) {
                                                jrFromProject_actionPerformed(e);
                                            }
                                        }
        );
        jbImportar.setText("Importar");
        jbImportar.setBounds(new Rectangle(5, 185, 130, 20));
        jbImportar.addActionListener(new ActionListener() {
                                         public void actionPerformed(ActionEvent e) {
                                             jbImportar_actionPerformed(e);
                                         }
                                     }
        );
        jtProjectName.setBounds(new Rectangle(5, 160, 130, 20));
        jtDescription.setBounds(new Rectangle(145, 160, 240, 20));
        jlName.setText("Nombre del proyecto:");
        jlName.setBounds(new Rectangle(5, 140, 130, 15));
        jlName.setFont(new Font("Dialog", 1, 10));
        jlDescription.setText("DescripciÃ³n del proyecto:");
        jlDescription.setBounds(new Rectangle(145, 140, 240, 15));
        jlDescription.setFont(new Font("Dialog", 1, 10));
        bSiguiente.setText("Siguiente");
        bSiguiente.setBounds(new Rectangle(300, 230, 85, 20));
        bSiguiente.addActionListener(new ActionListener() {
                                         public void actionPerformed(ActionEvent e) {
                                             bSiguiente_actionPerformed(e);
                                         }
                                     }
        );
        bSalir.setText("Salir");
        bSalir.setBounds(new Rectangle(5, 229, 77, 22));
        bSalir.addActionListener(new ActionListener() {
                                     public void actionPerformed(ActionEvent e) {
                                         bSalir_actionPerformed(e);
                                     }
                                 }
        );
        this.getContentPane().add(bSalir, null);
        this.getContentPane().add(bSiguiente, null);
        this.getContentPane().add(jlDescription, null);
        this.getContentPane().add(jlName, null);
        this.getContentPane().add(jtDescription, null);
        this.getContentPane().add(jtProjectName, null);
        this.getContentPane().add(jbImportar, null);
        this.getContentPane().add(jrFromProject, null);
        this.getContentPane().add(jrFromFile, null);
        this.getContentPane().add(jlProjectName, null);
        this.getContentPane().add(jlShapes, null);
        this.getContentPane().add(jlFileName, null);
        this.getContentPane().add(bAbrirProyecto, null);
        this.getContentPane().add(bAbrirSHP, null);
        ButtonGroup from = new ButtonGroup();
        from.add(jrFromFile);
        from.add(jrFromProject);
        jrFromFile.setSelected(true);
        jrFromFile.addActionListener(new ActionListener() {
                                         public void actionPerformed(ActionEvent e) {
                                             jrFromFile_actionPerformed(e);
                                         }
                                     }
        );
        showButtons();
    }

    private void bAbrirSHP_actionPerformed(ActionEvent e) {
        String fileName = loadShapesFromSHPFile();
        jlFileName.setText("File: " + fileName);        
        jlShapes.setText("Shape(s): " + shapes.size());        
    }
    
    private String loadShapesFromSHPFile() {
        File file = selectInputFile();
        String fileName;
        if (file != null)
        {
            SHPReader reader = new SHPReader();
            String path = file.getAbsolutePath();
            fileName = path.substring(0,path.indexOf(".shp"));
            shapes = reader.read(fileName);
            shapeType = reader.getShapeType();
            return path;
        } else {
            return "Invalid input file";
        }
    }

    private File selectInputFile() 
    {
        File file;
        String lastpath = getCfg().getProperty("lastpath");
        //String filename = File.separator+"shp";
        JFileChooser fc = new JFileChooser(lastpath);
        
        fc.setFileFilter(new SHPFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        
        // Show open dialog; this method does not return until the dialog is closed
        fc.showOpenDialog(this);
        try 
        {
            file = fc.getSelectedFile();
            getCfg().setProperty("lastpath",file.getPath());
        } catch (NullPointerException ex) 
        {
            file = null;
        }
        return file;
    }

    private void showButtons() {
        bAbrirProyecto.setVisible(jrFromProject.isSelected());
        jlProjectName.setVisible(jrFromProject.isSelected());
        bAbrirSHP.setVisible(jrFromFile.isSelected());
        jlFileName.setVisible(jrFromFile.isSelected());
        jbImportar.setEnabled(jrFromFile.isSelected());
    }

    private void jrFromProject_actionPerformed(ActionEvent e) {
        showButtons();
    }

    private void jrFromFile_actionPerformed(ActionEvent e) {
        showButtons();
    }

    private void bAbrirProyecto_actionPerformed(ActionEvent e) {
    }

    private void jbImportar_actionPerformed(ActionEvent e) {
        if ((jtProjectName.getText().trim().length() == 0) || (jtDescription.getText().trim().length() == 0)) {
            Proced.Mensaje("Debe ingresar Nombre y DescripciÃ³n del proyecto", "Error");
        } else {
            insertarDB(shapes);
        }
    }
    
    private void insertarDB(Vector _shapes) {
        project.setName(jtProjectName.getText());
        project.setDescription(jtDescription.getText());
        Proced.exActualizar('a',project.getSQLString());
        switch (shapeType) {
            case ShapeTypes.POLYGON:
                boolean firstTime = true;
                for (int i = 0; i < _shapes.size(); i++) {
                    System.out.println("Importing Shape " + i);
                    SHPPolygon poly = (SHPPolygon)shapes.elementAt(i);
                    if (!poly.hasClosedRings()) {
                        if (autoCloseRings) {
                            poly.closeRings();
                        } else {
                            int option = JOptionPane.showConfirmDialog((Component) null, "El polÃ­gono no estÃ¡ cerrado, desea cerrarlo?", "Error", JOptionPane.YES_NO_OPTION);
                            if (option == JOptionPane.YES_OPTION) {
                                poly.closeRings();
                                int auto = JOptionPane.showConfirmDialog((Component) null, "Desea cerrar automaticamente el resto de los poligonos?", "Advertencia", JOptionPane.YES_NO_OPTION);
                                if (auto == JOptionPane.YES_OPTION) {
                                    autoCloseRings = true;
                                }
                            }
                        }
                    }
                    Proced.exActualizar('a', poly.getSQLString("mapper","project_geoms", "idproject", project.getIdproject()));
                }
                break;
/*
            case ShapeTypes.POINT:
                for (int i = 0; i < _shapes.size(); i++) {
                    SHPPoint point = (SHPPoint)shapes.elementAt(i);
                    Proced.exActualizar('a', point.getSQLString("mapper","project_points", "idproject", "1"));
                }
                break;
*/
            default:
                Proced.Mensaje("Tipo de datos no disponible para mallar","Error");
                break;
        }
    }

    public void setProject(MallaProject _project) {
        project = _project;
    }

    public MallaProject getProject() {
        return project;
    }

    private void bSiguiente_actionPerformed(ActionEvent e) {
        siguiente();
    }

    private void bSalir_actionPerformed(ActionEvent e) {
        cancelar();
    }
}
