package org.digitall.apps.drilling;

import org.digitall.common.drilling.Coordinate;
import org.digitall.lib.components.List;

//

/**         *************   Coordinador    ***************
 *
 *  ************ Coordina las funciones generales del asistente, sus funciones principales son:
 *
 * SiguientePaso(): Avanza de Formulario;
 * Previopaso(): Retrocede un Formulaio;
 * setCantPasos(int _cantPasos): Setea la cantidad de pasos del asistente;
 * Iniciaar(): Inicia el Asistente;
 *
 *   ************ Poseea las siguientes estructuras de Datos:
 *
 * PasoAsistente[] pasos: Vector que contiene todos los Formularios del Asistente;
 * pasoActual: Variable que indica en que Formulario esta actualmente el Asistente;
 * String[] args: Vector que contiene los datos necesarios de cada Formulario para registrarlos al terminar
 *                Ej: en el caso de Registrar una nueva Obra
 *                coord.args[0] contiene el id del nuevo Proyecto
 *                coord.args[1] contiene el nombre del proyecto
                  coord.args[2] contiene la descripción del proyecto
                  coord.args[3] contiene la fecha del proyecto
                  coord.args[4] contiene la zona del proyecto
                  coord.args[5] contiene el area del proyecto
                  coord.args[6] contiene el texto de procedimiento de QAQC
 * */
public class Coordinador {
    //private BasicDialog frm = null;
    private int cantPasos;
    private PasoAsistente[] pasos;
    private int pasoActual;
    public String[] args;
    private List minAllowed;
    private List altAllowed;
    private Coordinate startUTM;
    // = new Coordinate.Double();
    private Coordinate endUTM;
    // = new Coordinate.Double();

    public Coordinador() {
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

    public void setPasos(PasoAsistente[] _pasos) {
	pasos = _pasos;
    }

    public void iniciar() {
	for (int i = 0; i < cantPasos; i++) {
	    pasos[i].setVisible(false);
	    pasos[i].setModal(true);
	    org.digitall.lib.components.ComponentsManager.centerWindow(pasos[i]);
	    pasos[i].setCoordinador(this);
	}
	pasoActual = 0;
	pasos[pasoActual].setVisible(true);
    }

    public String[] getVector() {
	return args;
    }

    public void setArgumento(int _indice, String _valor) {
	args[_indice] = _valor;
    }

    public String getArgumento(int _indice) {
	return args[_indice];
    }

    public String[] getArgumentos() {
	return args;
    }

    public void setCantidadArgumentos(int _cantidad) {
	args = new String[_cantidad];
    }

    public void exit() {
	for (int i = 0; i < cantPasos; i++) {
	    pasos[i].dispose();
	}
    }

    public void nueva() {
	limpiaArgumentos();
	limpiarFrm();
	pasos[3].setVisible(false);
	pasos[2].setVisible(false);
	pasoActual = 1;
	pasos[pasoActual].mostrar();
    }

    public void limpiaArgumentos() {
	for (int i = 0; i < 14; i++) {
	    args[i] = "";
	}
    }

    public void limpiarFrm() {
	pasos[2].limpiarFrame();
	pasos[3].limpiarFrame();
    }

    public void setMinAll(List _minAllowed) {
	this.minAllowed = _minAllowed;
    }

    public List getMinAllowed() {
	return minAllowed;
    }

    public void setAltAll(List _altAllowed) {
	altAllowed = _altAllowed;
    }

    public List getAltAllowed() {
	return altAllowed;
    }

    public void setStartUTM(Coordinate _startUTM) {
	startUTM = _startUTM;
    }

    public Coordinate getStartUTM() {
	return startUTM;
    }

    public void setEndUTM(Coordinate _endUTM) {
	endUTM = _endUTM;
    }

    public Coordinate getEndUTM() {
	return endUTM;
    }

}
