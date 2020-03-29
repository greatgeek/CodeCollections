#### 题目描述

Given a **non-empty** string *s* and a dictionary *wordDict* containing a list of **non-empty** words, add spaces in *s* to construct a sentence where each word is a valid dictionary word. Return all such possible sentences.

**Note:**

- The same word in the dictionary may be reused multiple times in the segmentation.
- You may assume the dictionary does not contain duplicate words.

**Example 1:**

```
Input:
s = "catsanddog"
wordDict = ["cat", "cats", "and", "sand", "dog"]
Output:
[
  "cats and dog",
  "cat sand dog"
]
```

**Example 2:**

```
Input:
s = "pineapplepenapple"
wordDict = ["apple", "pen", "applepen", "pine", "pineapple"]
Output:
[
  "pine apple pen apple",
  "pineapple pen apple",
  "pine applepen apple"
]
Explanation: Note that you are allowed to reuse a dictionary word.
```

**Example 3:**

```
Input:
s = "catsandog"
wordDict = ["cats", "dog", "sand", "and", "cat"]
Output:
[]
```

#### 使用带记忆的回溯解法

要使用回溯法，并且加速其过程，就需要先画出其回溯树。

以 **Example 1** 为例画出其回溯树：

```
									catsanddog
									/		\
							cats|anddog		cat|sanddog
								/				/
							and|dog			sand|dog
								/				/
							dog|""			   dog|""
```

由此可以发现，这个过程类似二叉树的先序遍历，在回溯的角度解读，其实这是一棵多叉树的先序遍历。

再以 Example 2 为例画出其回溯树：

```
								pineapplepenapple
								/				\
						pine|applepenapple			pineapple|penapple
							/			\						/
					apple|penapple		applepen|apple			pen|apple
						  /						/				  /
					pen|apple				apple|""			  apple|""
						/
					apple|""
```

发现回溯树中有不少重复的节点，例如 penapple, apple。所以在回溯的过程中，若在将节点在其第一次出现时就将其分隔的方式记录下来，则可以减少计算量。

#### 代码

```java
class Solution {
    public List<String> wordBreak(String s, List<String> wordDict) {
        return process(s,wordDict,new HashMap<>());
    }
    
    public static List<String> process(String s, List<String> dict, Map<String, List<String>> memo) {
        if (s == null || s.equals("")) {
            return null;
        } else if (memo.containsKey(s)) {
            return memo.get(s);
        }

        List<String> res = new LinkedList<>();
        for (String word : dict) {
            if (s.startsWith(word)) {
                List<String> subList = process(s.substring(word.length()),dict,memo);
                if(subList!=null){
                    for(String sub:subList){
                        res.add(word+" "+sub);
                    }
                }else{
                    res.add(word);
                }

            }
        }
        memo.put(s,res);
        return res;
    }
}
```

