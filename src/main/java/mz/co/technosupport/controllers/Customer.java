/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.controllers;

import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.mvc.Controller;
import mz.co.hi.web.mvc.exceptions.MvcException;

/**
 *
 * @author Edgêncio da Calista
 */
@ApplicationScoped
public class Customer extends Controller{
    
        @Inject private FrontEnd frontEnd;
    
    
    
    public void tickets() throws MvcException{
        frontEnd.setTemplate("customer");
        Map data = new HashMap();
        data.put("navigator", "A");
        frontEnd.setTemplateData(data);
        this.callView();

    }
    
    
      public void affiliates()throws MvcException{
        frontEnd.setTemplate("customer");
        Map data = new HashMap();
        data.put("navigator", "B");
        frontEnd.setTemplateData(data);
        this.callView();
    }
    
    public void addresses()throws MvcException{
        frontEnd.setTemplate("customer");
        Map data = new HashMap();
        data.put("navigator", "C");
        frontEnd.setTemplateData(data);
        this.callView();
    }
        
    
   public void tdetails(Map param) throws MvcException{
        frontEnd.setTemplate("customer");
        Map data = new HashMap();
        String ticket_id = param.get("id").toString();
        data.put("ticket_id", ticket_id);
        this.callView(data);
   }
    
}
