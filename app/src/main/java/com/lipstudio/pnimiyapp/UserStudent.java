package com.lipstudio.pnimiyapp;

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;

public class UserStudent extends User{

    private boolean editContent;
    private boolean editSchedule;

    public UserStudent(String firstName, String lastName, long id, String password, ArrayList<String> group) {
        super(firstName, lastName, id, password, group);
        editContent = false;
        editSchedule = false;
    }

    public UserStudent(String firstName, String lastName, long id, String password, ArrayList<String> group,boolean editContent, boolean editSchedule ) {
        super(firstName, lastName, id, password, group);
        this.editContent = editContent;
        this.editSchedule = editSchedule;
    }

    public boolean isEditContent() {
        return editContent;
    }

    public void setEditContent(boolean editContent) {
        this.editContent = editContent;
    }

    public boolean isEditSchedule() {
        return editSchedule;
    }

    public void setEditSchedule(boolean editSchedule) {
        this.editSchedule = editSchedule;
    }

    @Override
    public String toString() {
        return super.toString() + " UserStudent{" +
                "editContent=" + editContent +
                ", editSchedule=" + editSchedule +
                '}';
    }
}
