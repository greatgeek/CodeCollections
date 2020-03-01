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