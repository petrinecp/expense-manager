/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.expense.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author majo
 */
public interface UserRole {

    public static final String BASIC_USER = "BASIC_USER";
    public static final String PRIVILEGED_USER = "PRIVILEGED_USER";
    public static final String ADMIN = "ADMIN";

    public static final List<String> LIST_OF_ROLES = Collections.unmodifiableList(
            new ArrayList<String>() {
                {
                    add(BASIC_USER);
                    add(PRIVILEGED_USER);
                    add(ADMIN);
                }
            });
}
