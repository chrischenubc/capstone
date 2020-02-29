package logic;

import model.Command;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CommandsRelationGraphTest {
    static CommandsRelationGraph instance;
    @BeforeClass
    public static void setUp() throws Exception {
        instance = new CommandsRelationGraph();
    }

    @Test
    public void addCommand() {
        assertEquals(0, instance.numOfUniqueCommands());
        Command c1 = new Command("Weather", Arrays.asList("Vancouver", "tomorrow"));
        Command c2 = new Command("Weather", Arrays.asList("Vancouver", "today"));
        Command c3 = new Command("Weather", Arrays.asList("Toronto", "tomorrow"));
        List<Command> prediction = instance.addCommand(c1);
        assertEquals(1, instance.numOfUniqueCommands());
        assertEquals(1, instance.counter());
        assertEquals(prediction.get(0), c1);

        instance.addCommand(c2);
        assertEquals(2, instance.numOfUniqueCommands());
        assertEquals(2, instance.counter());
        assertEquals(1, instance.numOfRelations());

        instance.addCommand(c3);
        assertEquals(3, instance.numOfUniqueCommands());
        assertEquals(3, instance.counter());
        assertEquals(2, instance.numOfRelations());

        instance.addCommand(c2);
        assertEquals(3, instance.numOfUniqueCommands());
        assertEquals(4, instance.counter());

        instance.addCommand(c3);
        assertEquals(3, instance.numOfUniqueCommands());
        assertEquals(5, instance.counter());

        assertEquals(2, instance.numOfRelations());
        assertEquals(2, instance.getTraverseSteps());

    }

    @Test
    public void reset() {
        Command c1 = new Command("Weather", Arrays.asList("Vancouver", "tomorrow"));
        Command c2 = new Command("Weather", Arrays.asList("Vancouver", "today"));
        Command c3 = new Command("Weather", Arrays.asList("Toronto", "tomorrow"));
        instance.addCommand(c1);
        instance.addCommand(c2);
        instance.addCommand(c3);
        instance.reset();
        assertEquals(0, instance.numOfUniqueCommands());
        assertEquals(0, instance.counter());
    }

    @Test
    public void test1() {
        instance.reset();
        int[] seq = new int[]{1,2,3,1,3,1,3};
        TestHelper helper = new TestHelper(seq);
        System.out.println("\nTest1 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(3, instance.numOfRelations());
    }

    @Test
    public void test2() {
        instance.reset();
        int[] seq = new int[]{1,2,3,4,1,4,2};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test2 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(5, instance.numOfRelations());
    }

    @Test
    public void test3() {
        instance.reset();
        int[] seq = new int[]{4,1,4,3,2};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test3 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(3, instance.numOfRelations());
    }

    @Test
    public void test4() {
        instance.reset();
        int[] seq = new int[]{5,5,5,5,5,5,5};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test4 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(1, instance.numOfRelations());
    }

    @Test
    public void test5() {
        instance.reset();
        int[] seq = new int[]{6,7,6,5,5,5,5};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test5 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(3, instance.numOfRelations());
    }

    @Test
    public void test6() {
        instance.reset();
        int[] seq = new int[]{6,6,6,6,6,6,6,6,6};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test6 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(1, instance.numOfRelations());
    }


    @Test
    public void test7() {
        instance.reset();
        int[] seq = new int[]{7};
        TestHelper helper = new TestHelper(seq);
        System.out.println("Test7 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
        assertEquals(0, instance.numOfRelations());
    }

    @Test
    public void testRandom1() {
        instance.reset();
        TestHelper helper = new TestHelper(10);
        int[] seq = helper.getSeq();
        System.out.println("Test7 Start Simulation " + helper.getSimulation());
        helper.simulate();
        assertEquals(helper.countUniqueInt(), instance.numOfUniqueCommands());
        assertEquals(seq.length, instance.counter());
    }

    static class TestHelper {
        private int[] seq;
        private List<Command> commandPool;
        private List<Command> simulation;
        private static String[] topics = {"Weather", "Sport", "News", "Stock", "Entertainment", "Music", "Traffic"};
        private static String[] keywords = {"Vancouver", "Tomorrow", "Los Angles", "Today", "Yesterday", "A week ago",
                "Next Week", "Toronto", "New York", "Downtown"};

        public TestHelper(int size) {
            commandPool = generateDeFaultCommandPool();
            this.seq = getRandomSeq(size);
            simulation = generationSimulation(commandPool, this.seq);
        }

        public TestHelper(int[] seq) {
            this.seq = seq;
            commandPool = generateDeFaultCommandPool();
            simulation = generationSimulation(commandPool, this.seq);
        }

        public TestHelper(List<Command> commandPool, int[] seq) {
            this.seq = seq;
            this.commandPool = commandPool;
            simulation = generationSimulation(commandPool, seq);
        }

        public TestHelper(String[] topics, String[] keywords, int[] seq) {
            this.topics = topics;
            this.keywords = keywords;
            new TestHelper(seq);
        }

        private static List<Command> generateDeFaultCommandPool() {
            Set<Command> commands = new HashSet<>();

            for (String topic: topics) {
                for (int i = 0; i < keywords.length; i++) {
                    for (int j = i + 1; j < keywords.length; j++) {
                        commands.add(new Command(topic, Arrays.asList(keywords[i], keywords[j])));
                    }
                }
            }
            return new ArrayList<>(commands);
        }

        private static List<Command> generationSimulation(List<Command> pool, int[] seq) {
            List<Command> res = new ArrayList<>();
            for (int i = 0; i < seq.length; i++) {
                System.out.printf("Command: %d: %s\n", seq[i] , pool.get(seq[i]).toString());
                res.add(pool.get(seq[i]));
            }
            return res;
        }

        public List<Command> getCommandPool() {
            return commandPool;
        }

        public List<Command> getSimulation() {
            return simulation;
        }

        public void simulate() {
            List<Command> prediction;
            for (int i = 0; i < simulation.size(); i++) {
                prediction = instance.addCommand(simulation.get(i));
                System.out.printf("Prediction %d: %s\n", i, prediction.toString());
            }
        }

        public int countUniqueInt() {
            Set<Integer> set = new HashSet<>();
            for (int i : seq) {
                set.add(i);
            }
            return set.size();
        }

        private int[] getRandomSeq(int size) {
            Random rand = new Random();
            int[] seq = new int[size];

            for (int i = 0; i < size; i++) {
                seq[i] = rand.nextInt(commandPool.size());
            }
            return seq;
        }

        public int[] getSeq() {
            return seq;
        }

        public static void main(String[] args) {
            int[] seq = new int[]{};
            TestHelper helper = new TestHelper(seq);
            System.out.println(Arrays.toString(helper.getRandomSeq(8)));
        }

    }
}

