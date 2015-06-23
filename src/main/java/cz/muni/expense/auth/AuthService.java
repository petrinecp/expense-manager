package cz.muni.expense.auth;

import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author majo
 */
public interface AuthService {
    AuthAccessElement login(AuthLoginElement loginElement);
    boolean isAuthorized(String authId, String authToken, Set<String> rolesAllowed);
	boolean logout(String authId, String authToken);
}
