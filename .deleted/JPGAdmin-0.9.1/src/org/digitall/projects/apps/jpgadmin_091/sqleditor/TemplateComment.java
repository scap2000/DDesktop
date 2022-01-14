package org.digitall.projects.apps.jpgadmin_091.sqleditor;

import java.math.BigDecimal;

import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.environment.Environment;

public class TemplateComment {

    private String separator = " = ";
    private String version = "--version"+separator;
    private String numVersion = "1.0";
    private String groups = "--groups"+separator;
    private String descripcion = "--description"+separator;
    private String fecha = "--date"+separator;
    private String hora = "--hour"+separator;
    private String usuario = "--user"+separator;
    
    public TemplateComment() {
	
    }

    public String getComment(){
	fecha += Environment.currentDate;
	hora += Environment.currentTime;
	usuario += Environment.sessionUser;
	String comment = "\n\t"+version +"\n\t" + groups +"\n\t" + descripcion +"\n\t" + fecha +"\n\t" + hora +"\n\t" +usuario+"\n\t";
	return comment;
    }
    
    public void transformComment(String _comment){
	clearData();
	String commentModif = _comment;
	commentModif = commentModif.replace('*','/');
	commentModif = commentModif.replaceAll("/","");
	commentModif = commentModif.replaceAll("\t","");
	commentModif = commentModif.replaceAll("\n\n","\n");//
	commentModif = commentModif.replaceAll("\n"," ");
	commentModif = commentModif.replaceAll("  "," ");
	commentModif = commentModif.replaceAll("#","--#");
	String[] com = commentModif.split("--");
	String dato = "";
	int type = -1;
	for (int i=0 ;i<com.length ;i++ ) {
	    type = getType(com[i].trim(),"#");
	    dato = getData(com[i],"#");
	    //se carga una fila del comentario
	    if(!dato.equals("")){
		setData(dato,type);
	    }else{
	        type = getType(com[i].trim(),"");
	        dato = getData(com[i],"");
	        setData(dato,type);
	    }
	}
	/*System.out.println("date = "+Environment.currentDate); 
	System.out.println("hour = "+Environment.currentTime); 
	System.out.println("user = "+Environment.sessionUser); */
    }
    
    public void clearData(){
	version = "--version"+separator+"1.0";
	groups = "--groups"+separator+"0";
	descripcion = "--description"+separator;
	fecha = "--date"+separator;
	hora = "--hour"+separator;
	usuario = "--user"+separator;
    }
    
    private void setData(String _data, int _type){
	switch (_type)  {
	    case 0: setVersion(_data);
		break;
	    case 1: setGroups(_data);
	        break;
	    case 2: setDescripcion(_data);
	        break;
	    case 3: setFecha(_data);
	        break;
	    case 4: setHora(_data);
	        break;
	    case 5: setUsuario(_data);
	        break;
	}
    }
    
    
    private int getType(String _linea,String _token){
	int retorno = -1;
	if(_linea.startsWith(_token+"version")){
	    retorno = 0;
	}
	else{
	    if(_linea.startsWith(_token+"group")){
	        retorno = 1;
	    }
	    else{
	        if(_linea.startsWith(_token+"description")){
		    retorno = 2;
		}
		else{
		    if(_linea.startsWith(_token+"date")){
		        retorno = 3;
		    }
		    else{
		        if(_linea.startsWith(_token+"hour")){
		            retorno = 4;
		        }
			else{
			    if(_linea.startsWith(_token+"user")){
			        retorno = 5;
			    }
			}
		    }
		}
	    }
	}
	return retorno;
    }
    
    private String getData(String _key){
	String retorno = "";
	retorno = _key.substring(_key.indexOf(separator)+1).trim();
	return retorno;
    }
    
    private String getData(String _linea,String _token){
	String retorno = "";
	if(_linea.startsWith(_token)){
	    retorno = _linea.substring(_linea.indexOf("=")+1).trim();
	}
	return retorno;
    }
    
    private double incrementVersion(String _version){
	double retorno = Double.parseDouble(_version);
	retorno = retorno + 0.01;
	return retorno;
    }
    
    public void incrementVersion(){
	BigDecimal incremento = new BigDecimal(numVersion);
	incremento.add(new BigDecimal("0.01"));
	double retorno = Double.parseDouble(numVersion);
	retorno = retorno + 0.01;
	//numVersion = ""+retorno;
	numVersion = incremento.toString();
    }
    
    public void setVersion(String version) {
	//System.out.println("************************** = "+version);
	numVersion = version;
	if(!version.trim().equals("")){
	    //this.version = "--version"+separator+version;
	    this.version = "--version"+separator+numVersion;
	}else{
	    this.version = "--version"+separator+"1.0";
	}
	
    }

    public String getVersion() {
	return version;
    }

    public void setGroups(String groups) {
	this.groups = "--groups"+separator+ groups;
    }

    public String getGroups() {
	return groups;
    }

    public void setDescripcion(String descripcion) {
	this.descripcion = "--description"+separator + descripcion;
    }

    public String getDescripcion() {
	return descripcion;
    }

    public void setFecha(String fecha) {
	this.fecha = "--date"+separator + fecha;
    }

    public String getFecha() {
	return fecha;
    }

    public void setHora(String hora) {
	this.hora = "--hour"+separator + hora;
    }

    public String getHora() {
	return hora;
    }

    public void setUsuario(String usuario) {
	this.usuario = "--user"+separator + usuario;
    }

    public String getUsuario() {
	return usuario;
    }
    
    //método que reemplaza/inserta comentario - nuevo formato
    public String replaceComment(String _stored){
	transformComment(_stored);
	int inicio = _stored.toUpperCase().indexOf("BEGIN")+5;
	int fin = _stored.toUpperCase().indexOf("*/")+2;
	StringBuilder stored = new StringBuilder(_stored);
	StringBuilder resultado = null;
	StringBuilder comentarioAnterior = null;
	//ingresa por el IF cuando el stored no tiene comentario o tiene comentario con formato nuevo
	if(inicio>=fin){
	    //si el comentario tiene formato nuevo, no se cambia nada
	    if(_stored.toUpperCase().indexOf("--VERSION")>-1){
		resultado = stored;
	    }else{
		//si el stored no tiene comentario, se inserta un comentario con formato nuevo
	        resultado = stored.insert(inicio,getComment()); 
	    }
	}else{//ingresa por el ELSE cuando el stored tiene comentario con formato viejo
	    resultado = stored.replace(inicio,fin,getComment());
	    comentarioAnterior = new StringBuilder(_stored.substring(inicio,fin));
	}
	return resultado.toString();
    }
    
    //metodo incompleto - revisando
    public String getStored(String _stored){
	int inicio = _stored.toUpperCase().indexOf("BEGIN")+5;
	StringBuilder reemplazar = new StringBuilder(_stored);
	//reemplazar.
	String numVersionAnterior = numVersion;
	incrementVersion();
	System.out.println("anterior = "+""+ separator+numVersionAnterior);
	/*System.out.println("anterior = "+""+ separator+numVersionAnterior);
	System.out.println("actual = "+""+ separator+numVersion);
	System.out.println("reemplazar = "+_stored.replaceFirst(""+ separator+numVersionAnterior,""+separator+numVersion));
	System.out.println("hora anterior = "+hora);
	System.out.println("hora actual = "+"--hour"+separator+Environment.currentTime);
	System.out.println("hora = "+_stored.replaceFirst(hora,"--hour"+separator+Environment.currentTime));*/
	reemplazar = new StringBuilder(_stored.replaceFirst(""+ separator+numVersionAnterior,""+separator+numVersion));
	System.out.println("hora = "+Environment.currentTime);
	System.out.println("horaFINAL = "+"--hour"+separator+Environment.currentTime);
	reemplazar = new StringBuilder(reemplazar.toString().replaceFirst(hora,"--hour"+separator+Environment.currentTime));
	return reemplazar.toString();
    }
}
