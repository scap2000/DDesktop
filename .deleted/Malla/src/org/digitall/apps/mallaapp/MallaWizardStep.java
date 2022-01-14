package org.digitall.apps.mallaapp;

import javax.swing.JDialog;

import org.digitall.lib.common.ConfigFile;

public class MallaWizardStep extends JDialog {

    private MallaWizardCoord coord;
    private MallaProject project = new MallaProject();
    private ConfigFile cfg = new ConfigFile();

    public MallaWizardStep() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    public void setCoordinador(MallaWizardCoord _coord) {
        coord = _coord;
    }

    public void mostrar() {
        setVisible(true);
    }

    public void cancelar() {
        coord.finalizar();
    }

    public void siguiente() {
        coord.siguientePaso();
    }

    public void previo() {
        coord.previoPaso();
    }

    public void setProject(MallaProject _project) {
        project = _project;
    }

    public MallaProject getProject() {
        return project;
    }

    public void setCfg(ConfigFile cfg) {
        this.cfg = cfg;
    }

    public ConfigFile getCfg() {
        return cfg;
    }
}
