package cz.muni.expense.auth;

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
}
