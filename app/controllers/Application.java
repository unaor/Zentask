package controllers;

import models.Project;
import models.Task;
import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import play.mvc.Security.Authenticated;

import views.html.*;

public class Application extends Controller {
  
	@Authenticated(Secured.class)
    public static Result index() {
        return ok(index.render(Project.finder.all(),
        		Task.find.all()));
    }
    
    public static Result login() {
        return ok(
            login.render(Form.form(Login.class))
        );
    }
    
    public static Result authenticate(){
    	Form<Login> loginform = Form.form(Login.class).bindFromRequest();
    	if(loginform.hasErrors()){
    		return badRequest(login.render(loginform));
    	}
    	else{
    		session().clear();
    		session("email",loginform.get().email);
    		return redirect(routes.Application.index());
    	}
    }
    
    public static class Login{
    	public String email;
    	public String password;
    
    	public String validate(){
    		if(User.authenticate(email, password)==null){
    			return "invalid user or password";
    		}
    		return null;
    	}
    
    }
}
