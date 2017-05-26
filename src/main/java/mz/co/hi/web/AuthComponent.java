/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.hi.web;

/**
 *
 * @author edgencio
 */
public interface AuthComponent {

    public boolean isUserInAnyOfThisRoles(String[] strings);

    public boolean doesUserHavePermission(String string);
}