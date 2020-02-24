import db.mongodb.MongoDBConnection;
import model.Command;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MongoDBConnectionTest {
    MongoDBConnection connection;
    @Before
    public void setUp() throws Exception {
         connection = new MongoDBConnection();
         connection.deleteAll();
    }

    @Test
    public void addCommandTest() {
        Command c1 = new Command("Weather", Arrays.asList("Vancouver", "tomorrow"));
        Command c2 = new Command("Weather", Arrays.asList("Vancouver", "today"));
        Command c3 = new Command("Weather", Arrays.asList("Toronto", "tomorrow"));
        connection.addCommand(c1);
        connection.addCommand(c2);
        connection.addCommand(c3);
    }

    @Test
    public void getCommandsTest() {
        connection.deleteAll();
        Command c1 = new Command("Weather", Arrays.asList("Vancouver", "tomorrow"));
        Command c2 = new Command("Weather", Arrays.asList("Vancouver", "today"));
        Command c3 = new Command("Sport", Arrays.asList("Toronto", "tomorrow"));
        connection.addCommand(c1);
        connection.addCommand(c2);
        connection.addCommand(c3);
        List<Command> res = connection.getAllCommands();

        Set<Command> set = new HashSet<>();
        set.add(c1);
        set.add(c2);
        set.add(c3);
        Assert.assertEquals(3, res.size());
        for (Command c: res) {
            boolean bool = set.contains(c);
            Assert.assertTrue(bool);
        }

        res = connection.getCommandsWithKeyword("Toronto");
        Assert.assertEquals(1, res.size());
        Assert.assertEquals(c3, res.get(0));

        res = connection.getCommandsWithTopic("Weather");
        Assert.assertEquals(2, res.size());

        connection.addCommand(c1);
    }

    @Test
    public void getMostCommonCommandTest() {
        connection.initDB();
        Assert.assertEquals(new Command("Weather", Arrays.asList("Vancouver", "tomorrow")),
                connection.getMostCommonCommand());
    }
}
