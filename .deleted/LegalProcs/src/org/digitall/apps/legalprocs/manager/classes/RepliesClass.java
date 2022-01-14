package org.digitall.apps.legalprocs.manager.classes;

import java.util.Date;

public class RepliesClass {

    private int idreply = -1;
    private int idcadastralregister = -1;
    private int idapplication_tab = -1;
    private int idapplication = -1;
    private boolean overlap = false;
    //Superpposición
    private String overlaptext = "";
    private Date date = null;
    private double freearea = 0;
    private String estado = "";

    public RepliesClass() {

    }

    public RepliesClass(int _idFile) {

    }

}
