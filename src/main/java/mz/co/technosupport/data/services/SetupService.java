package mz.co.technosupport.data.services;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
//import technosupport.data.model.Member;


/**
 * Created by Romildo Cumbe 
 */
@ApplicationScoped
public class SetupService {

    @Inject
    UserService userService;

    public void run(){
       /* if(userService.count()==0){
            Member c = new Member();
            c.setEmail("admin@technoplus.co.mz");
            c.setFirstname("Admin");
            c.setLastname("TechnoSupport");
            c.setImageUrl("webroot/assets/img/male.png");
            c.setPhone("(+258) 84 501 5012");
            c.setGender("M");
            c.setPosition("A");
            c.setWorkingArea("Geral");
            
            memberService.create(c);
        } */
    }
    
}
