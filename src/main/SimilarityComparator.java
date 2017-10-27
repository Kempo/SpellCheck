package main;

import java.util.Comparator;

public class SimilarityComparator implements Comparator<Word> {

    // 1 = greater than
    // -1 = less than
    @Override
    public int compare(Word w1, Word w2) {
        if (w1.getPercent() > w2.getPercent()) {
            return 1;
        } else {
            if (w1.getPercent() < w2.getPercent()) {
                return -1;
            }
        }

        return 0;
    }
}
