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
