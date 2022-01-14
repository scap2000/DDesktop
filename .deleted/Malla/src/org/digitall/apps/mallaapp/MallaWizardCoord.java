package org.digitall.apps.mallaapp;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.common.ConfigFile;
import org.digitall.lib.misc.Proced;

public class MallaWizardCoord {

    private int cantPasos;
    private MallaWizardStep[] pasos;
    private int pasoActual;
    private MallaProject project = new MallaProject();
    private ConfigFile cfg = new ConfigFile();

    public MallaWizardCoord() {
    }

    public void siguientePaso() {
        if (pasoActual < cantPasos - 1) {
            pasos[pasoActual].setVisible(false);
            pasoActual++;
            pasos[pasoActual].mostrar();
        } else {
            //finAsistente();
        }
    }

    public void previoPaso() {
        if (pasoActual > 0) {
            pasos[pasoActual].setVisible(false);
            pasoActual--;
            pasos[pasoActual].mostrar();

        } else {
            //inicioAsistente();
        }
    }

    public void setCantPasos(int _cantPasos) {
        cantPasos = _cantPasos;
    }

    public void setPasos(MallaWizardStep[] _pasos) {
        pasos = _pasos;
        for (int i = 0; i < pasos.length; i++) {
            pasos[i].setProject(project);
        }
    }

    public void iniciar() {
        for (int i = 0; i < cantPasos; i++) {
            pasos[i].setVisible(false);
            pasos[i].setModal(true);
            Proced.CentraVentana(pasos[i]);
            pasos[i].setCoordinador(this);
        }
        pasoActual = 0;
        pasos[pasoActual].setVisible(true);
    }

    public void finalizar() {
        for (int i = 0; i < cantPasos; i++) {
            pasos[i].dispose();
        }
    }

    public void nueva() {
        pasos[3].setVisible(false);
        pasos[2].setVisible(false);
        pasoActual = 1;
        pasos[pasoActual].mostrar();
    }

    public void setProject(MallaProject _project) {
        project = _project;
        for (int i = 0; i < cantPasos; i++) {
            pasos[i].setProject(project);
        }
    }

    public MallaProject getProject() {
        return project;
    }


    public void setCfg(ConfigFile _cfg) {
        cfg = _cfg;
        for (int i = 0; i < cantPasos; i++) {
            pasos[i].setCfg(cfg);
        }
    }

    public ConfigFile getCfg() {
        return cfg;
    }
}
