package org.digitall.apps.legalprocs.calendar.wizard;

//

/** Argumentos:
 * args[0]: area del polígono;
 * args[1]: id del Solicitante;
 * args[2]: id del Represemtante Legal
 * args[3]: id del Pais ---------> "Ubicación de la Solicitud"
 * args[4]: id de la Provincia --> "Ubicación de la Solicitud"
 * args[5]: id de la Localidad --> "Ubicación de la Solicitud"
 * args[6]: Campo que contiene la cantidad de puntos del Polígono
 * args[7]: Campo que contiene la consulta para guardar los puntos del Polígono (coord. GK).
 * A partir de aca es para Terrain Data
 * args[8]:  Cultivado
 * args[9]:  Cercado
 * args[10]: Edificado
 * args[11]: Erial
 * args[12]: Sitio Público
 * args[13]: Sitio Histórico
 * args[14]: Sitio Religioso
 * args[15]: Reserva Natural
 * args[16]: Otros
 * args[17]: Nombre del propietario
 * args[18]: idprovincia
 * args[19]: idlocalidad
 * args[20]: idcalle
 * args[21]: numero de la calle
 * A partir de aca es para Program
 * args[22]: Plazo
 * args[23]: CycleType
 * args[24]: CycleNumber
 * args[25]: Permisos Otorgados
 * args[26]: Unidades Otorgadas
 * args[27]: id del Programa Mínimo de Exploración registrado
 *para la declaracion jurada
 * args[28]: Declaracion jurada.
 * args[29]: Área del polígono
 * args[30]: Categoría del mineral a explorar
 * args[31]: Programa mínimo de Trabajo, True: adjunta PMT, FALSE: no adjuntaPMT
 *
 *
 */
public class Manager {

    private int qtyStep;
    private PanelWizard[] wizardSteps;
    private int actualStep;
    //    private String[] args;
    private boolean newProspection;
    private String ifFile;
    public int idfile = -1;
    public static final int INIT = 0;
    public static final int EXPLORATIONREQUEST = 1;
    public static final int DISCOVERYTYPE = 2;
    public static final int MINESTEPS = 3;
    private String titleText;

    public Manager() {

    }

    public void setQtyStep(int _qtyStep) {
	qtyStep = _qtyStep;
    }

    public void setWizardSteps(PanelWizard[] _wizardSteps) {
	wizardSteps = _wizardSteps;
    }

    public void setInit() {
	//actualStep = 0;
	Wizard wizard = new Wizard(this);
	org.digitall.lib.components.ComponentsManager.centerWindow(wizard);
	wizard.goStep(actualStep);
	wizard.setVisible(true);
	setWizard(wizard);
    }

    public void setInit(int _idFile) {
	Wizard wizard = new Wizard(this);
	org.digitall.lib.components.ComponentsManager.centerWindow(wizard);
	wizard.goStep(actualStep);
	wizard.setIdFile(_idFile);
	wizard.setVisible(true);
	setWizard(wizard);
    }

    private void setWizard(Wizard _wizard) {
	for (int i = 0; i < qtyStep; i++) {
	    wizardSteps[i].setWizard(_wizard);
	}
    }

    public void setStep(int _step) {
	actualStep = _step;
    }

    public int getActualStep() {
	return actualStep;
    }

    public PanelWizard getPanelStep(int _step) {
	actualStep = _step;
	return wizardSteps[_step];
    }

    public int getNextStep() {
	if (actualStep < qtyStep - 1) {
	    actualStep += 1;
	}
	return actualStep;
    }

    public int getPreviousStep() {
	if (actualStep > 0) {
	    actualStep -= 1;
	}
	return actualStep;
    }

    public int getQtyStep() {
	return qtyStep;
    }

    public void setNewProspection(boolean _valor) {
	newProspection = _valor;
    }

    public boolean getNewProspection() {
	return newProspection;
    }

    public void setIfFile(String _ifFile) {
	ifFile = _ifFile;
    }

    public String getIfFile() {
	return ifFile;
    }

    public void setTitleText(String _titleText, int _step) {
	titleText = _titleText;
    }

    public String getTitleText() {
	return titleText;
    }

}
