package com.lipstudio.pnimiyapp;

import java.nio.file.attribute.GroupPrincipal;
import java.util.ArrayList;

public class UserStudent extends User{

    private boolean editContent;

    public UserStudent(String firstName, String lastName, long id, String password, ArrayList<String> group) {
        super(firstName, lastName, id, password, group);
        editContent = false;
    }

    public boolean isEditContent() {
        return editContent;
    }

    public void setEditContent(boolean editContent) {
        this.editContent = editContent;
    }

    @Override
    public String toString() {
        return super.toString() + "UserStudent{" +
                "editContent=" + editContent +
                '}';
    }
}
