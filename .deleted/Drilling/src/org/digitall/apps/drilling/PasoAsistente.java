package org.digitall.apps.drilling;

import org.digitall.common.drilling.Coordinate;
import org.digitall.lib.components.List;
import org.digitall.lib.components.basic.BasicDialog;

public class PasoAsistente extends BasicDialog {

    private Coordinador coord;

    public PasoAsistente() {
    }

    public void setCoordinador(Coordinador _coord) {
	coord = _coord;
    }

    public void siguiente() {
	coord.siguientePaso();
    }

    public void previo() {
	coord.previoPaso();
    }

    public void getVec() {
	coord.getVector();
    }

    public void setArgumento(int _indice, String _valor) {
	coord.setArgumento(_indice, _valor);
    }

    public String[] getArgumentos() {
	return coord.getArgumentos();
    }

    public String c(int _indice) {
	return coord.getArgumento(_indice);
    }

    public void mostrar() {
	setVisible(true);
    }

    public void cancelar() {
	coord.exit();
    }

    public void nuevaobra() {
	coord.nueva();
    }

    public void limpiarFrame() {
	coord.limpiarFrm();
    }

    public void setMineralizationAllowed(List _minAllowed) {
	coord.setMinAll(_minAllowed);
    }

    public void setAlterationsAllowed(List _altAllowed) {
	coord.setAltAll(_altAllowed);
    }

    public List getMineralizationsList() {
	return coord.getMinAllowed();
    }

    public List getAlterationsList() {
	return coord.getAltAllowed();
    }

    public void setStartCoord(Coordinate _startUtm) {
	coord.setStartUTM(_startUtm);
    }

    public void setEndCoord(Coordinate _endUtm) {
	coord.setEndUTM(_endUtm);
    }

    public Coordinate getStartUtm() {
	return coord.getStartUTM();
    }

    public Coordinate getEndUtm() {
	return coord.getEndUTM();
    }

}
