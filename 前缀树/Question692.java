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