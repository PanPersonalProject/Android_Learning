package algorithm.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BinarySearchTest {


    public static class Gene {

        public enum Nucleotide {
            A, C, G, T
        }

        public static class Codon implements Comparable<Codon> {
            public final Nucleotide first, second, third;
            // 首先比较第一个元素，然后是第二个元素，依此类推。
            // 换句话说，第一个元素优先于第二个元素，第二个元素优先于第三个元素。
            private final Comparator<Codon> comparator = Comparator.comparing((Codon c) -> c.first).thenComparing((Codon c) -> c.second).thenComparing((Codon c) -> c.third);

            public Codon(String codonStr) {
                first = Nucleotide.valueOf(codonStr.substring(0, 1));
                second = Nucleotide.valueOf(codonStr.substring(1, 2));
                third = Nucleotide.valueOf(codonStr.substring(2, 3));
            }

            @Override
            public int compareTo(Codon other) {
                return comparator.compare(this, other);
            }
        }

        private final ArrayList<Codon> codons = new ArrayList<>();

        public Gene(String geneStr) {
            for (int i = 0; i < geneStr.length() - 3; i += 3) {
                // Take every 3 characters in the String and form a Codon
                codons.add(new Codon(geneStr.substring(i, i + 3)));
            }
        }


        public boolean binaryContains(Codon key) {
            Collections.sort(codons);
            int low = 0, high = codons.size() - 1;
            while (low <= high) {
                int middle = (low + high) / 2;
                int compareResult = key.compareTo(codons.get(middle));
                if (compareResult > 0) {
                    low = middle + 1;
                } else if (compareResult < 0) {
                    high = middle - 1;
                } else {
                    return true;
                }
            }

            return false;
        }


    }

    @Test
    public void test() {
        String geneStr = "ACGTGGCTCTCTAACGTACGTACGTACGGGGTTTATATATACCCTAGGACTCCCTTT";
        Gene myGene = new Gene(geneStr);
        Gene.Codon acg = new Gene.Codon("ACG");
        Gene.Codon gat = new Gene.Codon("GAT");
        System.out.println(myGene.binaryContains(acg)); // true
        System.out.println(myGene.binaryContains(gat)); // false

        assertTrue(myGene.binaryContains(acg));
        assertFalse(myGene.binaryContains(gat));
    }
}
