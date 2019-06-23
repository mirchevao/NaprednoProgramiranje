import java.io.InputStream;
import java.util.*;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class TermFrequencyTest {
    public static void main(String[] args) {
        String[] stop = new String[] { "во", "и", "се", "за", "ќе", "да", "од",
                "ги", "е", "со", "не", "тоа", "кои", "до", "го", "или", "дека",
                "што", "на", "а", "но", "кој", "ја" };
        TermFrequency tf = new TermFrequency(System.in, stop);
        System.out.println(tf.countTotal());
        System.out.println(tf.countDistinct());
        System.out.println(tf.mostOften(10));
    }
}
class TermFrequency {

    TreeMap<String, Integer> words;
    int total;

    public TermFrequency(InputStream inputStream, String[] stopWords) {
        words = new TreeMap<String, Integer>();
        List<String> ignore = Arrays.asList(stopWords);
        Scanner scanner = new Scanner(inputStream);
        String line;
        String fullText = "";
        while(scanner.hasNextLine()) {
            fullText+=scanner.nextLine();
        }
        StringTokenizer tokens = new StringTokenizer(fullText);
        while(tokens.hasMoreTokens()) {
            String word = tokens.nextToken();
            word = word.replaceAll("[.,]*", "");
            word = word.toLowerCase();
            if(!ignore.contains(word)&&word.equals("")) {
                total++;
                if(word.charAt(word.length()-1) == '-') {
                    word = word.substring(0,word.length()-1);
                }
                if(!words.containsKey(word)) {
                    words.put(word,1);
                } else {
                    words.put(word, words.get(word)+1);
                }
            }
        }

    }
    private static String prepareWord(String word) {
        String prepared="";
        char[] letters = word.toCharArray();
        for(int i=0; i<letters.length; i++) {

        }
        return null;
    }
    public int countTotal() {
        return total;
    }
    public int countDistinct() {
        return words.size();
    }
    public List<String> mostOften(int k) {

        TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(new ValuesComparator(words));
        sorted.putAll(words);
        Iterator<Entry<String, Integer>> it = sorted.entrySet().iterator();
        List<String> mostUsed = new ArrayList<String>();

        while (it.hasNext()) {
            mostUsed.add(it.next().getKey());
            k--;

            if (k == 0) {
                break;
            }
        }

        return mostUsed;

    }
}
class ValuesComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValuesComparator(Map<String, Integer> base) {
        this.base = base;
    }

    @Override
    public int compare(String x, String y) {
        int tmp = base.get(y)-base.get(x);
        if (tmp != 0) {
            return tmp;
        } else {
            return x.compareTo(y);
        }
    }

}