package lintCode;

public class UniquePathsII115 {
	public int uniquePathsWithObstacles(int[][] A) {
		// write your code here
		int m = A.length;
		int n = A[0].length;
		if (A[0][0] == 1 || A[m - 1][n - 1] == 1) {
			return 0;
		}
		int[][] f = new int[m][n];
		f[0][0] = 1;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (A[i][j] == 1) {
					f[i][j] = 0;
					continue;
				} else {
					if (i - 1 >= 0) {
						f[i][j] += f[i - 1][j];
					}

					if (j - 1 >= 0) {
						f[i][j] += f[i][j - 1];
					}
				}
			}
		}
		return f[m - 1][n - 1];
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
