/*
Given a list of strings words representing an English Dictionary, find the longest word in words that can be built one character at a time by other words in words. If there is more than one possible answer, return the longest word with the smallest lexicographical order.

If there is no answer, return the empty string.

Example 1:
Input: 
words = ["w","wo","wor","worl", "world"]
Output: "world"
Explanation: 
The word "world" can be built one character at a time by "w", "wo", "wor", and "worl".

Example 2:
Input: 
words = ["a", "banana", "app", "appl", "ap", "apply", "apple"]
Output: "apple"
Explanation: 
Both "apply" and "apple" can be built from other words in the dictionary. However, "apple" is lexicographically smaller than "apply".

Note:

All the strings in the input will only contain lowercase letters.
The length of words will be in the range [1, 1000].
The length of words[i] will be in the range [1, 30].
*/

class Solution {
	public String longestWord(String[] words) {
		Trie node = new Trie();
		for (String word : words) {
			node.addWord(word);
		}

		List<String> list = new LinkedList<>();
		node.getWords(node.root, list);

		if (list.size() == 0)
			return "";
		String longestStr = list.get(0);
		for (String str : list) {
			if (str.length() > longestStr.length()) {
				longestStr = str;
			}
		}

		return longestStr;
	}
}

class TrieNode {
	TrieNode[] nexts;
	String word;

	public TrieNode() {
		nexts = new TrieNode[26];
		word = null;
	}
}

class Trie {
	TrieNode root;
	
	public Trie() {
		root = new TrieNode();
	}

	public void addWord(String word) {
		TrieNode cur = root;
		for (char c : word.toCharArray()) {
			if (cur.nexts[c - 'a'] == null) {
				cur.nexts[c - 'a'] = new TrieNode();
			}
			cur = cur.nexts[c - 'a'];
		}
		cur.word = word;
	}

	public void getWords(TrieNode cur, List<String> list) {
		if (cur == null) {
			return;
		}

		if (cur.word != null) {
			list.add(cur.word);
		}

		for (int i = 0; i <= 25; i++) {
			if (cur.nexts[i] != null && cur.nexts[i].word != null) {
				getWords(cur.nexts[i], list);
			}
		}
	}
}
