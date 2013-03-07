package controllers;

import java.util.ArrayList;

import models.Project;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import views.html.projects.group;
import views.html.projects.item;
import static play.data.Form.*;

@Authenticated(Secured.class)
public class Projects extends Controller {
	
	public static Result add(){
		Project project = Project.create("New Project",
				form().bindFromRequest().get("group"), request().username());
		return ok(item.render(project));
	}
	
	public static Result rename(Long project){
		if(Secured.isMemberOf(project)){
			return ok(Project.rename(project, form().bindFromRequest().get("name")));
		}
		else{
			return forbidden();
		}
	}
	
	public static Result delete(Long project){
		if(Secured.isMemberOf(project)){
			Project.finder.ref(project).delete();
			return ok();
		}
		else{
			return forbidden();
		}
	}
	
	public static Result addGroup(){
		return ok(group.render("New Group", new ArrayList<Project>()));
	}

}
