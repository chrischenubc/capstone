package db.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import com.mysql.cj.xdevapi.JsonArray;
import db.DBConnection;
import model.Command;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Updates.*;

public class MongoDBConnection implements DBConnection {
    private MongoClient mongoClient;
    private MongoDatabase db;
    private static final String DB_NAME = "capstone";
    private MongoCollection<Document> collection;


    public MongoDBConnection() {
        // Connects to local mongodb server.
        mongoClient = MongoClients.create();
        db = mongoClient.getDatabase(DB_NAME);
        collection = db.getCollection("commands");
    }


    @Override
    public void addCommand(Command command) {
        MongoCursor<Document> cursor = collection.find(new Document("topic", command.getTopic()).append(
                "keywords", command.getKeywords())).iterator();
        if (cursor.hasNext()) {
            collection.updateOne(new BasicDBObject("topic", command.getTopic()).append(
                    "keywords", command.getKeywords()), combine(currentTimestamp("last_visited"),
                    new BasicDBObject("$inc", new Document("count", 1))));
        } else {
            Document doc = new Document("topic", command.getTopic()).append("keywords",
                    command.getKeywords()).append("count", 1);
            collection.insertOne(doc);
            collection.updateOne(new BasicDBObject("topic", command.getTopic()).append(
                    "keywords", command.getKeywords()), combine(currentTimestamp("last_visited")));
        }
    }

    @Override
    public Command getMostCommonCommand() {
        Document result = collection.find().sort(Sorts.orderBy(descending("count"))).first();
        if (result == null) {
            return null;
        }
        String topic = result.getString("topic");
        List<String> keywords = result.getList("keywords", String.class);
        return new Command(topic, keywords);
    }

    //db.commands.find({"keywords":["Toronto"]})
    @Override
    public List<Command> getCommandsWithKeyword(String keyword) {
        keyword = keyword.toLowerCase();
        List<Command> res = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find(new Document("keywords", keyword)).iterator();
        try {
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject obj = new JSONObject(doc.toJson());
                String topic = obj.getString("topic");
                List<String> keywords = new ArrayList<>();
                JSONArray array  = obj.getJSONArray("keywords");
                for (int i = 0; i < array.length(); i++) {
                    keywords.add(array.getString(i));
                }
                res.add(new Command(topic, keywords));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        };
        return res;
    }

    @Override
    public List<Command> getCommandsWithTopic(String topic) {
        topic = topic.toLowerCase();
        List<Command> res = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find(new Document("topic", topic)).iterator();
        try {
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject obj = new JSONObject(doc.toJson());
                String topicStr = obj.getString("topic");
                List<String> keywords = new ArrayList<>();
                JSONArray array  = obj.getJSONArray("keywords");
                for (int i = 0; i < array.length(); i++) {
                    keywords.add(array.getString(i));
                }
                res.add(new Command(topicStr, keywords));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        };
        return res;
    }

    @Override
    public List<Command> getAllCommands() {
        List<Command> res = new ArrayList<>();

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while(cursor.hasNext()) {
                Document doc = cursor.next();
                JSONObject obj = new JSONObject(doc.toJson());
                String topic = obj.getString("topic");
                List<String> keywords = new ArrayList<>();
                JSONArray array  = obj.getJSONArray("keywords");
                for (int i = 0; i < array.length(); i++) {
                    keywords.add(array.getString(i));
                }
                res.add(new Command(topic, keywords));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        };
        return res;
    }

    public void deleteAll() {
        collection.deleteMany(new BasicDBObject());
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public void initDB() {
        deleteAll();
        addCommand(new Command("Weather", Arrays.asList("Vancouver", "tomorrow")));
        addCommand(new Command("Weather", Arrays.asList("Vancouver", "tomorrow")));
        addCommand(new Command("Weather", Arrays.asList("Vancouver", "tomorrow")));
        addCommand(new Command("Weather", Arrays.asList("Vancouver", "today")));
        addCommand(new Command("Sport", Arrays.asList("Toronto", "tomorrow")));
        addCommand(new Command("Weather", Arrays.asList("Vancouver", "today")));
        addCommand(new Command("News", Arrays.asList("Toronto", "tomorrow")));
    }

    public static void main(String[] args) {
        MongoDBConnection connection = new MongoDBConnection();
        connection.initDB();
        connection.getMostCommonCommand();

    }

}
