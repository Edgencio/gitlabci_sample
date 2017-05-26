/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.controllers;

import javax.enterprise.context.ApplicationScoped;
import mz.co.hi.web.mvc.Controller;
import mz.co.hi.web.mvc.exceptions.MvcException;

/**
 *
 * @author Edgêncio da Calista
 */
@ApplicationScoped
public class User extends Controller {

    public void profile() throws MvcException {
        this.callView();
    }

    public void password() throws MvcException {
        this.callView();
    }

}
