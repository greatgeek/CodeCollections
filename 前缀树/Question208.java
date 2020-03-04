/*
Implement a trie with insert, search, and startsWith methods.

Example:

Trie trie = new Trie();

trie.insert("apple");
trie.search("apple");   // returns true
trie.search("app");     // returns false
trie.startsWith("app"); // returns true
trie.insert("app");   
trie.search("app");     // returns true
*/

package leetcode;

public class Question208 {

	public static void main(String[] args) {
		MyTrie trie = new MyTrie();
		
		trie.insert("apple");
		System.out.println(trie.search("apple"));
		System.out.println(trie.search("app"));
		System.out.println(trie.startsWith("app"));
		
		trie.insert("app");
		System.out.println(trie.search("app"));
		
	}

}

class MyTrie{
	private TrieNode root;
	
	public MyTrie() {
		root = new TrieNode();
	}
	
    public void insert(String word) {
        TrieNode cur = root;
        for(char letter:word.toCharArray()) {
        	if(cur.nexts[letter-'a']==null) {
        		cur.nexts[letter-'a']=new TrieNode();
        	}
        	cur = cur.nexts[letter-'a'];
        }
        cur.isEnd = true;
    }
    
    /** Returns if the word is in the trie. */
    public boolean search(String word) {
    	TrieNode node = searchPrefix(word);
    	return node!=null && node.isEnd==true;
    }
    
    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
    	TrieNode node = searchPrefix(prefix);
    	return node!=null;
    }
    
    private TrieNode searchPrefix(String word) {
    	TrieNode cur = root;
    	for(char letter:word.toCharArray()) {
    		if(cur.nexts[letter-'a']!=null) {
    			cur = cur.nexts[letter-'a'];
    		}else {
    			return null;
    		}
    	}
    	return cur;
    }
    
	class TrieNode{
		private TrieNode[] nexts;
		private boolean isEnd;
		
		public TrieNode() {
			nexts = new TrieNode[26];
			isEnd = false;
		}
	}
}
