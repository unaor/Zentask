package controllers;

/*import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

import play.mvc.*;
import play.libs.Yaml;
import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;*/
import models.User;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {

	@Before
	public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
        
    }
	
	@Test
	public void authenticateSuccess(){
		User bob = User.finder.byId("bob@example.com");
		Result result = callAction(
		        controllers.routes.ref.Application.authenticate(),
		        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
		            "email", bob.email,
		            "password", bob.password))
		    );
		    assertEquals(303, status(result));
		    assertEquals("bob@example.com", session(result).get("email"));
		
	}
	
	@Test
	public void authenticateFailure() {
	    Result result = callAction(
	        controllers.routes.ref.Application.authenticate(),
	        fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
	            "email", "bob@example.com",
	            "password", "badpassword"))
	    );
	    assertEquals(400, status(result));
	    assertNull(session(result).get("email"));
	}
	
	@Test
	public void authenticated(){
		Result result = callAction(controllers.routes.ref.Application.index(),
				fakeRequest().withSession("email", "bob@example.com"));
		 assertEquals(200, status(result));
	}
	@Test
	public void notAuthenticated() {
	    Result result = callAction(
	        controllers.routes.ref.Application.index(),
	        fakeRequest()
	    );
	    assertEquals(303, status(result));
	    assertEquals("/login", header("Location", result));
	}
}