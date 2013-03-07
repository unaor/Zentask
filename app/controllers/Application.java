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
        return ok(index.render(Project.findInvolving(request().username()),
        		Task.findTodoInvolving(request().username()),User.finder.byId(request().username())));
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
    
    public static Result logout(){
    	session().clear();
    	flash("success", "You've been logged out");
    	return redirect(routes.Application.login());
    }
    
    public static Result javascriptRoutes(){
    	response().setContentType("text/javascript");
    	return ok(
    			Routes.javascriptRouter("jsRoutes",
    					controllers.routes.javascript.Projects.add(),
    					controllers.routes.javascript.Projects.delete(),
    					controllers.routes.javascript.Projects.rename(),
    					controllers.routes.javascript.Projects.addGroup()
    					));
    }
}
