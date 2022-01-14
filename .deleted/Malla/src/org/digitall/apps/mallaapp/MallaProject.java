package org.digitall.apps.mallaapp;

public class MallaProject {

    private int idproject = -1;
    private String name;
    private String description;
    private String datetime;
    private boolean nuevo = true;

    public MallaProject() {
    }

    public void setIdproject(int _idproject) {
        idproject = _idproject;
    }

    public int getIdproject() {
        return idproject;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String _description) {
        description = _description;
    }

    public String getDescription() {
        return description;
    }

    public void setDatetime(String _datetime) {
        datetime = _datetime;
    }

    public String getDatetime() {
        return datetime;
    }
    
    public String getSQLString() {

        String sqlString = "";
        
        if (nuevo) {
            idproject = Integer.parseInt(Proced.getCampo("select max(idproject)+1 from mapper.projects"));
            sqlString = "Insert into mapper.projects values(" + 
                idproject + ",'" + name + "','" + description + "',( select now() ),'')";
        } else {
            sqlString = "Update mapper.projects set " + 
                " name = '" + name + "'," + 
                " description = '" + description + "'," + 
                " datetime = ( select now() )" + 
                " where idproject = " + idproject;
        }
        
        return sqlString;

    }
}
