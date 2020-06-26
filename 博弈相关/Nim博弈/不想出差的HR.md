# [不想出差的HR](https://www.nowcoder.com/question/next?pid=11848090&qid=223151&tid=34227915)

按照卡中心校园招聘的要求，HR小招和小商需要从三个科室中（分别为A、B、C）抽派面试官去往不同城市。
 两名HR按照以下规定轮流从任一科室选择面试官：每次至少选择一位，至多选择该科室剩余面试官数。最先选不到面试官的HR需要自己出差。
 假设HR小招和小商都不想出差且每次选择都采取最优策略，如果是小招先选，写一个函数来判断她是否需要出差。如果不需要出差，请给出第一步的最优策略。

##### **输入描述:**

```
输入为三个正整数，分别代表三个科室的面试官人数，用英文逗号分隔
```



##### **输出描述:**

```
若小招需要出差，则输出：1；
若小招不需要出差，则输出：第一步选择的科室名称和选择人数，用英文逗号分隔
```



##### **输入例子1:**

```
1,8,9
```



##### **输出例子1:**

```
1
```



##### **输入例子2:**

```
2,0,4
```



##### **输出例子2:**

```
C,2
```

## show me the code

```java
import java.util.Scanner;

public class Main {

    static String func(int[] num){
        int A=num[0],B=num[1],C=num[2];
        if((A^B^C)==0) return "1";
        else {
            if(A>(B^C)) return "A,"+(A-(B^C));
            else if(B>(A^C)) return "B,"+(B-(A^C));
            else if(C>(A^B)) return "C,"+(C-(A^B));
        }
        return "";
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String line = sc.nextLine();
        String[] str=line.split(",");
        int[] num = new int[str.length];
        for (int i=0;i<str.length;i++){
            num[i]=Integer.valueOf(str[i]);
        }
        System.out.println(func(num));
    }
}

```

