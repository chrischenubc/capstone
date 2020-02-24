package db;

import model.Command;

import java.util.List;

public interface DBConnection {

    public void addCommand(Command command);

    public Command getMostCommonCommand();

    public List<Command> getCommandsWithKeyword(String keyword);

    public List<Command> getCommandsWithTopic(String topic);

    public List<Command> getAllCommands();

    public void close();

    public void deleteAll();
}
