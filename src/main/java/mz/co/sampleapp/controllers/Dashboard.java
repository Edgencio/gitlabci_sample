package mz.co.sampleapp.controllers;

/**
 *
 * @author Edgêncio da Calista
 */



import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.emerjoin.hi.web.FrontEnd;
import org.emerjoin.hi.web.mvc.Controller;
import org.emerjoin.hi.web.mvc.exceptions.MvcException;


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