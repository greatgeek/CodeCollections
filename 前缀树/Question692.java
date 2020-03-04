/*
Given a non-empty list of words, return the k most frequent elements.

Your answer should be sorted by frequency from highest to lowest. If two words have the same frequency, then the word with the lower alphabetical order comes first.

Example 1:
Input: ["i", "love", "leetcode", "i", "love", "coding"], k = 2
Output: ["i", "love"]
Explanation: "i" and "love" are the two most frequent words.
    Note that "i" comes before "love" due to a lower alphabetical order.

Example 2:
Input: ["the", "day", "is", "sunny", "the", "the", "the", "sunny", "is", "is"], k = 4
Output: ["the", "is", "sunny", "day"]
Explanation: "the", "is", "sunny" and "day" are the four most frequent words,
    with the number of occurrence being 4, 3, 2 and 1 respectively.
Note:
You may assume k is always valid, 1 ≤ k ≤ number of unique elements.
Input words contain only lowercase letters.
*/

class Solution {
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> map = new HashMap<>();
        for(String word:words){
            map.put(word, map.getOrDefault(word, 0)+1);
        }
        
        Trie[] buckets = new Trie[words.length];
        for(Map.Entry<String, Integer> e:map.entrySet()){
            //for each word, add it into trie at its bucket
            String word = e.getKey();
            int freq = e.getValue();
            if(buckets[freq]==null){
                buckets[freq] = new Trie();
            }
            buckets[freq].addWord(word);
        }
        
        List<String> ans = new LinkedList<>();

        for(int i = buckets.length-1;i>=0;i--){
        //for trie in each bucket, get all the words with same frequency in lexicographic order. Compare with k and get the result
            if(buckets[i]!=null){
                List<String> l = new LinkedList<>();                               
                buckets[i].getWords(buckets[i].root, l);
                if(l.size()<k){
                    ans.addAll(l);
                    k = k - l.size(); 
                }
                else {
                   for(int j = 0;j<=k-1;j++){
                       ans.add(l.get(j));
                   } 
                    break;
                }
            }
        }
        return ans;
    }
}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    String word = null;
}

class Trie {
    TrieNode root = new TrieNode();
    public void addWord(String word){
        TrieNode cur = root;
        for(char c:word.toCharArray()){
            if(cur.children[c-'a']==null){
                cur.children[c-'a'] = new TrieNode();
            }
            cur = cur.children[c-'a'];
        }
        cur.word = word;
    }
    
    public void getWords(TrieNode node, List<String> ans){
        //use DFS to get lexicograpic order of all the words with same frequency
        if(node==null){
            return;
        }
        if(node.word!=null){
            ans.add(node.word);
        }
        for(int i = 0;i<=25;i++){
            if(node.children[i]!=null){
                getWords(node.children[i], ans);
            }
        }
        
    }
}