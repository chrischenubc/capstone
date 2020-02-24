package logic;

import org.json.JSONArray;

import java.util.*;

public class RankingSystem {
    //Singleton class
    private static RankingSystem instance;
    private Map<String, Integer> countMap;

    private RankingSystem() {
        this.countMap = new HashMap<>();
    }

    public static RankingSystem getInstance() {
        if (instance ==  null) {
            instance = new RankingSystem();
        }
        return instance;
    }

    /**
     * @param keyword
     * add keyword into the ranking system
     */
    public void addKeyWord(String keyword) {
        keyword = keyword.toLowerCase();
        Integer count = countMap.get(keyword);
        if (count == null) {
            count = 0;
        }
        count = count + 1;
        countMap.put(keyword, count);
    }

    public String getMostFrequentKeyword() {
        if (countMap.size() <= 0) {
            return null;
        }
        int maxCount = 0;
        String maxCountString = null;
        for (Map.Entry<String, Integer> entry: countMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                maxCountString = entry.getKey();
            }
        }
        return maxCountString;
    }

    public int getMostFrequentKeywordCount() {
        String maxCountString = getMostFrequentKeyword();
        return countMap.get(maxCountString);
    }

    /**
     * @param k
     * @return return top k frequent keywords in the ranking system
     * if k > size of the system, simply return all
     */
    public List<String> getTopKFrequentKeywords(int k) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(countMap.entrySet());
        sortedList.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return Integer.compare(o2.getValue(), o1.getValue());
            }
        });
        List<String> res = new ArrayList<>();
        if (k >= size()) {
            k = size();
        }

        for (int i = 0; i < k; i++) {
            res.add(sortedList.get(i).getKey());
        }
        return res;
    }

    public JSONArray getRecommendations() {
        return null;
    }


    public int size() {
        return countMap.size();
    }

    /**
     * Clear all keywords in the ranking system
     */
    public void clear() {
        countMap.clear();
    }
}
