package project5;
import java.util.*;

//I have used an algorithm which is efficient than the brute force implememntation with a time complexity of O(N.(Log N)^2)

public class SuffixArray {

   // Suffix class represents a suffix with its index, rank, and next rank

  public static class Suffix implements Comparable<Suffix> {
    int suffixIndex;
    int suffixRank;
    int nextRank;
 
    public Suffix(int index, int rank, int nextRank) {
      suffixIndex = index;
      suffixRank = rank;
      this.nextRank = nextRank;
    }
         
    public int compareTo(Suffix s) {
      if (suffixRank != s.suffixRank)
        return Integer.compare(suffixRank, s.suffixRank);
      return Integer.compare(nextRank, s.nextRank);
    }
  }

  // Function to create the suffix array from an input string
  public static ArrayList<Integer> createSuffixArray(String inputString) {
    int n = inputString.length();
    Suffix[] suffixes = new Suffix[n];

    // Create suffix objects with their initial ranks
         
    for (int i = 0; i < n; i++) {
      suffixes[i] = new Suffix(i, inputString.charAt(i) - '$', 0);
    }

    // Assign next ranks to suffixes based on their positions

    for (int i = 0; i < n; i++)
      suffixes[i].nextRank = (i + 1 < n ? suffixes[i + 1].suffixRank : -1);
 
    // Sort the suffixes array using compareTo method

    Arrays.sort(suffixes);
  
    int[] indexes = new int[n];

    // Perform iteration to update suffix ranks and next ranks

    for (int length = 4; length < 2 * n; length <<= 1) {
      updateSuffixRanks(suffixes, indexes, length);
      updateNextRanks(suffixes, indexes, n, length);
      Arrays.sort(suffixes);
    }

    // Convert the suffix array to ArrayList<Integer>
    
    ArrayList<Integer> suffixArray = new ArrayList<>();
    for (int i = 0; i < n; i++)
      suffixArray.add(suffixes[i].suffixIndex);
 
    return suffixArray;
  }   

  // Helper function to update suffix ranks based on previous ranks and next ranks
  private static void updateSuffixRanks(Suffix[] suffixes, int[] indexes, int length) {
    int rank = 0;
    int prev = suffixes[0].suffixRank;
    suffixes[0].suffixRank = rank;
    indexes[suffixes[0].suffixIndex] = 0;
    
    for (int i = 1; i < suffixes.length; i++) {
      if (suffixes[i].suffixRank == prev && suffixes[i].nextRank == suffixes[i - 1].nextRank) {
        prev = suffixes[i].suffixRank;
        suffixes[i].suffixRank = rank;
      } else {
        prev = suffixes[i].suffixRank;
        suffixes[i].suffixRank = ++rank;
      }
      indexes[suffixes[i].suffixIndex] = i;
    }
  }

  // Helper function to update next ranks of suffixes

  private static void updateNextRanks(Suffix[] suffixes, int[] indexes, int n, int length) {
    for (int i = 0; i < n; i++) {
      int nextPosition = suffixes[i].suffixIndex + length / 2;
      suffixes[i].nextRank = nextPosition < n ? suffixes[indexes[nextPosition]].suffixRank : -1;
    }
  }

    
  public ArrayList<Integer> construct(String S) {
   return createSuffixArray(S);
  }

}
