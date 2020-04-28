package newCoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Question5 {

    static final int[][] Cor = {{0, 0}, {0, 3}, {0, 6}, {3, 0}, {3, 3}, {3, 6}, {6, 0}, {6, 3}, {6, 6}}; // 小九宫格的起始坐标

    public static boolean func(int[][] A, int i, int j) {
        if (i == 9) return true;
        if (j >= 9) return func(A, i + 1, 0);
        if(A[i][j]!=0) return func(A,i,j+1);

        for (int num = 1; num <= 9; num++) {
            if (!isValid(A, i, j, num)) continue;
            A[i][j] = num;
            if (func(A, i, j + 1)) return true; // 只有一种情况可以不用回溯
            A[i][j] = 0;
        }
        return false;
    }

    public static boolean isValid(int[][] A, int i, int j, int num) {

        for (int k = 0; k < 9; k++) {
            if (A[i][k] == num) return false; // 遍历第i 行；
        }

        for (int k = 0; k < 9; k++) {
            if (A[k][j] == num) return false; // 遍历第j 列；
        }

        for (int r = 0; r < Cor.length; r++) { // 扫描小九宫格
            int x = Cor[r][0];
            int y = Cor[r][1];
            if (i >= x && i <= x + 2 && j >= y && j <= y + 2) {
                for (int m = x; m <= x + 2; m++) {
                    for (int n = y; n <= y + 2; n++) {
                        if (A[m][n] == num) return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FileInputStream fin = new FileInputStream(new File("src/newCoder/Question5.txt"));
        Scanner sc = new Scanner(fin);
        int[][] A = new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                A[i][j]=sc.nextInt();
            }
        }

        func(A,0,0);

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(A[i][j]+" ");
            }
            System.out.println();
        }
    }
}
