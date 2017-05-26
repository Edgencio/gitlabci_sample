/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mz.co.technosupport.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import mz.co.hi.web.ActiveUser;
import mz.co.hi.web.FrontEnd;
import mz.co.hi.web.RequestContext;
import mz.co.hi.web.events.args.ControllerRequestInterception;
import mz.co.hi.web.events.args.FrontierRequestInterception;
import mz.co.hi.web.events.listeners.ControllerCallsListener;
import mz.co.hi.web.events.listeners.FrontierCallsListener;
import mz.co.hi.web.events.listeners.TemplateLoadListener;
import mz.co.technosupport.data.services.UserService;
import mz.co.technosupport.dto.UserDTO;


/**
 *
 * @author Romildo Cumbe
 */
@ApplicationScoped
public class MyGodListener implements TemplateLoadListener, ControllerCallsListener, FrontierCallsListener {

    @Inject
    private FrontEnd frontEnd;
    
    @Inject UserService userService;
    
    @Inject RequestContext requestContext;
    
    @Inject
    private ActiveUser activeUser;
    
    
    public void onTemplateLoad() {
        
        try{
            Map tdata = new HashMap();
//             User loggedUser = userService.getLoggedUser();
//                tdata.put("username", loggedUser.getMember().getFirstname());
//                tdata.put("userphoto", loggedUser.getMember().getImageUrl());
           // frontEnd.setTemplateData(tdata);
        }catch(Exception e){
            e.printStackTrace();
            try{
                userService.logout(); 
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

    public void preAction(ControllerRequestInterception args) {
           
        try{
             UserDTO loggedUser = (UserDTO) activeUser.getProperty("user");
            //User loggedUser = userService.getLoggedUser();
            
           //Se o User for um Tecnico, se ele nao estiver  no controller my,  mandar-lhe ao controller my
//           if(loggedUser.getMember.equalsIgnoreCase("T")){
//                   if(!frontEnd.getTemplate().equalsIgnoreCase("technitian")){
//                        frontEnd.setTemplate("technitian");
//                   }
//                   //commented by 
//                   /*if(!args.getClass().getSimpleName().equalsIgnoreCase("my")){
//                       if(!args.getMethod().getName().equalsIgnoreCase("main")){
//                            requestContext.echo("ACCESS-DENIED");
//                       }
//                   }*/
//                   
//           }
           
           
            
        }catch(Exception e){
            e.printStackTrace();
            try{
                //Fazendo Logout
                userService.logout(); 
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        
        
        
            
         
        

        
        
        //System.out.println("Before action call : "+args.getClazz().getSimpleName()+"/"+args.getMethod().getName());

    }

    public void postAction(ControllerRequestInterception args) {
            
        //System.out.println("Post action call : "+args.getClazz().getSimpleName()+"/"+args.getMethod().getName());

    }

    public void preFrontier(FrontierRequestInterception args) {
      
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MyGodListener.class.getName()).log(Level.SEVERE, null, ex);
//        }
        try{
         //   userService.getLoggedUser();
        }catch(Exception e){
            e.printStackTrace();
            try{
                //Fazendo Logout
                userService.logout(); 
            }catch(Exception ex){
                ex.printStackTrace();
            }
        
        }
        //System.out.println("Pre frontier : "+args.getClazz().getSimpleName()+" : "+args.getMethod().getName());

    }

    public void postFrontier(FrontierRequestInterception args) {

        //System.out.println("Post frontier : "+args.getClazz().getSimpleName()+" : "+args.getMethod().getName());

    }

}
