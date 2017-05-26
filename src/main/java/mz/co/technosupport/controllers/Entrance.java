/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.controllers;

/**
 *
 * @author edgencio
 */
import mz.co.hi.web.mvc.Controller;
import mz.co.hi.web.mvc.exceptions.MvcException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.RequestContext;
import mz.co.technosupport.data.services.UserService;

@ApplicationScoped
public class Entrance extends Controller {

    @Inject
    private FrontEnd frontEnd;
    
    @Inject 
     private UserService userService;

    public void login() throws MvcException {
       frontEnd.setTemplate("index");
        this.callView();
    }
        
    

}
