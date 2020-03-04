/*
Given many words, words[i] has weight i.

Design a class WordFilter that supports one function, WordFilter.f(String prefix, String suffix). It will return the word with given prefix and suffix with maximum weight. If no word exists, return -1.

Examples:

Input:
WordFilter(["apple"])
WordFilter.f("a", "e") // returns 0
WordFilter.f("b", "") // returns -1
 

Note:

words has length in range [1, 15000].
For each test case, up to words.length queries WordFilter.f may be made.
words[i] has length in range [1, 10].
prefix, suffix have lengths in range [0, 10].
words[i] and prefix, suffix queries consist of lowercase letters only.
*/

class WordFilter {
	TrieNode trie;	
    public WordFilter(String[] words) {
        trie = new TrieNode();
        for(int index = 0;index < words.length; index++) {
        	String word = words[index]+"{"+words[index];
        	
        	for(int i=0;i<words[index].length()+1;i++) {
                TrieNode cur = trie;
                cur.index = index;
        		for(int j=i;j<word.length();j++) {
        			int k=word.charAt(j)-'a';
        			if(cur.nexts[k]==null) {
        				cur.nexts[k] = new TrieNode();
        			}
        			cur = cur.nexts[k];
        			cur.index = index;
        		}
        	}
        }
    }
    
    public int f(String prefix, String suffix) {
        TrieNode cur = trie;
        for(char letter : (suffix+"{"+prefix).toCharArray()) {
        	if(cur.nexts[letter-'a']==null) return -1;
        	cur = cur.nexts[letter-'a'];
        }
        return cur.index;
    }
}

class TrieNode{
	TrieNode[] nexts;
	int index;
	public TrieNode() {
		nexts = new TrieNode[27];// 刚好'{'的ASCII 码排在z后面
		index = 0;
	}
}




/**
 * Your WordFilter object will be instantiated and called as such:
 * WordFilter obj = new WordFilter(words);
 * int param_1 = obj.f(prefix,suffix);
 */