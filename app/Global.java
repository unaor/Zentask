import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;
import java.util.*;

public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
        // Check if the database is empty
        if (User.finder.findRowCount() == 0) {
        	Map<String, List<Object>> all = (Map<String, List<Object>>)Yaml.load("initial-data.yml");
            Ebean.save(all.get("users"));
            Ebean.save(all.get("projects"));
            Ebean.save(all.get("tasks"));
        }
    }
}
