import logic.RankingSystem;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RankingSystemTest {
    static RankingSystem  instance;
    @BeforeClass
    public static void setup() {
        instance = RankingSystem.getInstance();
    }

    @Test
    public void getMostFrequentKeywordTest() {
        instance.clear();
        Assert.assertNull(instance.getMostFrequentKeyword());

        instance.addKeyWord("Toronto");
        instance.addKeyWord("Toronto");
        instance.addKeyWord("Toronto");
        instance.addKeyWord("Vancouver");
        Assert.assertEquals("Toronto".toLowerCase(), instance.getMostFrequentKeyword());
        instance.clear();
        Assert.assertEquals(0, instance.size());


        instance.addKeyWord("Vancouver");
        Assert.assertEquals("Vancouver".toLowerCase(), instance.getMostFrequentKeyword());

        instance.addKeyWord("Toronto");
        instance.addKeyWord("Toronto");
        Assert.assertEquals(2, instance.getMostFrequentKeywordCount());
        Assert.assertEquals("Toronto".toLowerCase(), instance.getMostFrequentKeyword());

        String[] actual = instance.getTopKFrequentKeywords(2).toArray(new String[2]);
        String[] expected = {"Toronto".toLowerCase(), "Vancouver".toLowerCase()};
        Assert.assertArrayEquals(expected, actual);


    }
}
