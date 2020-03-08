/*
 * Given a chemical formula (given as a string), return the count of each atom.

An atomic element always starts with an uppercase character, then zero or more lowercase letters, representing the name.

1 or more digits representing the count of that element may follow if the count is greater than 1. If the count is 1, no digits will follow. For example, H2O and H2O2 are possible, but H1O2 is impossible.

Two formulas concatenated together produce another formula. For example, H2O2He3Mg4 is also a formula.

A formula placed in parentheses, and a count (optionally added) is also a formula. For example, (H2O2) and (H2O2)3 are formulas.

Given a formula, output the count of all elements as a string in the following form: the first name (in sorted order), followed by its count (if that count is more than 1), followed by the second name (in sorted order), followed by its count (if that count is more than 1), and so on.

Example 1:
Input: 
formula = "H2O"
Output: "H2O"
Explanation: 
The count of elements are {'H': 2, 'O': 1}.

Example 2:
Input: 
formula = "Mg(OH)2"
Output: "H2MgO2"
Explanation: 
The count of elements are {'H': 2, 'Mg': 1, 'O': 2}.

Example 3:
Input: 
formula = "K4(ON(SO3)2)2"
Output: "K4N2O14S4"
Explanation: 
The count of elements are {'K': 4, 'N': 2, 'O': 14, 'S': 4}.
Note:

1。All atom names consist of lowercase letters, except for the first character which is uppercase.
2。The length of formula will be in the range [1, 1000].
3。formula will only consist of letters, digits, and round parentheses, and is a valid formula as defined in the problem.
 */

package leetcode;

import java.util.*;

public class Question726 {
	int i;

	public String countOfAtoms(String formula) {
		StringBuilder ans = new StringBuilder();
		i = 0;
		Map<String, Integer> count = parse(formula);
		for (String name : count.keySet()) {
			ans.append(name);
			int multiplicity = count.get(name);
			if (multiplicity > 1)
				ans.append("" + multiplicity);
		}
		return new String(ans);
	}

	public Map<String, Integer> parse(String formula) {
		int N = formula.length();
		Map<String, Integer> count = new TreeMap<>();
		while (i < N && formula.charAt(i) != ')') {
			if (formula.charAt(i) == '(') {
				i++;
				for (Map.Entry<String, Integer> entry : parse(formula).entrySet()) {// 此处开始递归调用
					count.put(entry.getKey(), count.getOrDefault(entry.getKey(), 0) + entry.getValue());
				}
			} else {
				int iStart = i++;
				while (i < N && Character.isLowerCase(formula.charAt(i)))
					i++;
				String name = formula.substring(iStart, i);
				iStart = i;
				while (i < N && Character.isDigit(formula.charAt(i)))
					i++;
				int multiplicity = iStart < i ? Integer.parseInt(formula.substring(iStart, i)) : 1;
				count.put(name, count.getOrDefault(name, 0) + multiplicity);
			}
		}

		int iStart = ++i;
		while (i < N && Character.isDigit(formula.charAt(i)))
			i++;
		if (iStart < i) {
			int multiplicity = Integer.parseInt(formula.substring(iStart, i));
			for (String key : count.keySet()) {
				count.put(key, count.get(key) * multiplicity);
			}
		}
		return count;
	}

	public String countOfAtoms2(String formula) {
		int N = formula.length();
		Stack<Map<String, Integer>> stack = new Stack<>();
		stack.push(new TreeMap<>());

		for (int i = 0; i < N;) {
			if (formula.charAt(i) == '(') {
				stack.push(new TreeMap<>());
				i++;
			} else if (formula.charAt(i) == ')') {
				Map<String, Integer> top = stack.pop();
				int iStart = ++i, multiplicity = 1;
				while (i < N && Character.isDigit(formula.charAt(i)))
					i++;
				if (i > iStart) 
					multiplicity = Integer.parseInt(formula.substring(iStart, i));
				for (String c : top.keySet()) {
					int v = top.get(c);
					stack.peek().put(c, stack.peek().getOrDefault(c, 0) + v * multiplicity);
				}
			} else {
				int iStart = i++; // i 当前字符是大写字符，所以要++
				while (i < N && Character.isLowerCase(formula.charAt(i)))
					i++;
				String name = formula.substring(iStart, i);
				iStart = i;
				while (i < N && Character.isDigit(formula.charAt(i)))
					i++;
				int multiplicity = i > iStart ? Integer.parseInt(formula.substring(iStart, i)) : 1;
				stack.peek().put(name, stack.peek().getOrDefault(name, 0) + multiplicity);
			}
		}

		StringBuilder ans = new StringBuilder();
		for (String name : stack.peek().keySet()) {
			ans.append(name);
			int multiplicity = stack.peek().get(name);
			if (multiplicity > 1)
				ans.append("" + multiplicity);
		}

		return new String(ans);
	}

	public static void main(String[] args) {
		String formula = "K4(ON(SO3)2)2";
		int i=0;
		int iStart = i++;
		while(i<formula.length() && Character.isLowerCase(formula.charAt(i))) i++;
		String name = formula.substring(iStart,i); //[iStart,i)
		
		System.out.println(name);
	}

}
