package controllers;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.fakeGlobal;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.callAction;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.status;
import java.util.List;

import models.Project;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import play.libs.Yaml;
import play.mvc.Result;
import play.test.WithApplication;

public class ProjectsTest extends WithApplication {
	
	@Before
	public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
        
    }
	
	@Test
	public void newProject(){
		Result result = callAction(controllers.routes.ref.Projects.add(),fakeRequest()
				.withSession("email", "bob@example.com").withFormUrlEncodedBody
				(ImmutableMap.of("group","some group")));
		assertEquals(200, status(result));
		Project project = Project.finder.where().eq("folder", "some group").findUnique();
		assertNotNull(project);
		assertEquals("New Project", project.name);
		assertEquals(1, project.members.size());
		assertEquals("bob@example.com", project.members.get(0).email);
	}
	
	@Test
	public void renameProject(){
		Long id = Project.finder.where().eq("members.email", "bob@example.com").
				eq("name", "Private").findUnique().id;
		Result result = callAction(controllers.routes.ref.Projects.rename(id),
				fakeRequest().withSession("email", "bob@example.com").
				withFormUrlEncodedBody(ImmutableMap.of("name","New name"))
				);
		assertEquals(200, status(result));
	    assertEquals("New name", Project.finder.byId(id).name);
	}
	@Test
	public void renameProjectForbidden(){
		Long id = Project.finder.where().eq("members.email", "bob@example.com").
				eq("name", "Private").findUnique().id;
		Result result = callAction(controllers.routes.ref.Projects.rename(id),
				fakeRequest().withSession("email", "jeff@example.com").
				withFormUrlEncodedBody(ImmutableMap.of("name","New name"))
				);
		assertEquals(403,status(result));
		assertEquals("Private", Project.finder.byId(id).name);
	}

}
