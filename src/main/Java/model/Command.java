package model;

import java.util.List;
import java.util.Objects;

public class Command {
    String topic;
    List<String> keywords;

    public Command(String topic, List<String> keywords) {
        this.topic = topic.toLowerCase();
        this.keywords = keywords;
        for (int i = 0; i < keywords.size(); i++) {
            String lowercase = keywords.get(i).toLowerCase();
            keywords.set(i, lowercase);
        }
    }

    public String getTopic() {
        return topic.toLowerCase();
    }

    public List<String> getKeywords() {
        return keywords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return topic.equals(command.topic) &&
                keywords.equals(command.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topic, keywords);
    }

    @Override
    public String toString() {
        return "Command{" +
                "topic='" + topic + '\'' +
                ", keywords=" + keywords.toString() +
                '}';
    }
}
