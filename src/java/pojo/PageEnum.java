/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojo;

/**
 *
 * @author Dynamitos
 */
public enum PageEnum {
    LOGIN("/content/login.jsp"),
    MAIN("/content/musiccloud.jsp");
    private final String name;

    public String getName() {
        return name;
    }

    private PageEnum(String name) {
        this.name = name;
    }
}
