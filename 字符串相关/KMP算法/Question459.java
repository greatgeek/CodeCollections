class Solution {
    public boolean repeatedSubstringPattern(String s) {
        int[] next = longestPrefixSuffix((s+"#").toCharArray()); // 要计算多一个位置的最长前后缀长度
        if(next[next.length-1]==0) return false; // 若最后一个字符的最长前后缀长度为0，则必不是重复子串模式
        int num = s.length() - next[next.length-1];
        if(s.length()%num !=0) return false; // num 为模式子串的长度，若总长度为模式子串的长度的倍数则为重复子串，否则不是
        
        return true;
    }
    
	public static int[] longestPrefixSuffix(char[] str) {
		if (str.length == 1) {
			return new int[] { -1 };
		}
		int[] next = new int[str.length];
		next[0] = -1;
		next[1] = 0;

		for (int cn = 0, i = 2; i < next.length;) {
			if (str[i - 1] == str[cn]) {
				next[i] = cn + 1;
				i++;
				cn++;
			} else if (cn > 0) {
				cn = next[cn];
			} else {
				next[i++] = 0;
			}
		}
		return next;
	}
}