/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.controllers;

/**
 *
 * @author Edgêncio da Calista
 */

import mz.co.hi.web.mvc.Controller;
import mz.co.hi.web.mvc.exceptions.MvcException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.FrontEnd;

@ApplicationScoped
public class Dashboard extends Controller {
    
      @Inject private FrontEnd frontEnd;


  
    public void tickets() throws MvcException{
        frontEnd.setTemplate("dashboard");
        this.callView();

    }
    
    public void suppliers()throws MvcException{
         frontEnd.setTemplate("dashboard");
    this.callView();
    }
    

   public void customers()throws MvcException{
     frontEnd.setTemplate("dashboard");
    this.callView();
    }
   
   

    public void ticket_details() throws MvcException{
   frontEnd.setTemplate("dashboard");
   this.callView();
   }


}