package models;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;


import play.libs.Yaml;
import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class ModelsTest extends WithApplication {
	
	@Before
	public void setUp(){
		//start(fakeApplication(inMemoryDatabase()));
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}
	
	@Test
	public void createAndRetrieveUser(){
		new User("bob@gmail.com", "Bob", "secret").save();
		User bob = User.finder.where().like("email", "bob%").findUnique();//  eq("email", "bob@gmail.com").findUnique();
		assertNotNull(bob);
		assertEquals("Bob", bob.name);
	}
	
	@Test
	public void tryAuthenticate(){
		 User user  = new User("bob@gmail.com", "Bob", "secret");
		 user.save();
		 assertEquals( User.authenticate("bob@gmail.com", "secret"), user);
		 assertNull(User.authenticate("bob@gmail.com", "secrdffdet"));
	}
	
	@Test
    public void findProjectsInvolving() {
        new User("bob@gmail.com", "Bob", "secret").save();
        new User("jane@gmail.com", "Jane", "secret").save();

        Project.create("Play 2", "play", "bob@gmail.com");
        Project.create("Play 1", "play", "jane@gmail.com");

        List<Project> results = Project.findInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Play 2", results.get(0).name);
    }
	
	@Test
	public void findTodoTaskInvolving(){
		User bob = new User("bob@gmail.com", "Bob", "secret");
        bob.save();
        
        Project project = Project.create("Play 2", "play", "bob@gmail.com");
        Task t1 = new Task();
        t1.title="Write a tutorial";
        t1.assignedTo=bob;
        t1.done=true;
        t1.save();
        
        Task t2 = new Task();
        t2.title = "Release next version";
        t2.project = project;
        t2.save();
        
        List<Task> results = Task.findTodoInvolving("bob@gmail.com");
        assertEquals(1, results.size());
        assertEquals("Release next version", results.get(0).title);
	}
	
	@Test
	public void ymlTest(){
		Ebean.save((List) Yaml.load("test-data.yml"));
		int count = User.finder.findRowCount();
		assertEquals(count, User.finder.findRowCount());
	}

}
