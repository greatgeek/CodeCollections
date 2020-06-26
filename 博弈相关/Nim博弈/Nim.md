# Nim 博弈

**Nim** is a mathematical game of strategy in which two players take turns removing (or "nimming") object from distinct heaps or piles. On each turn, a player must remove at least one object, and may remove any number of objects provided they all come from the same heap or pile. Depending on the version being played, the goal of the game is either to avoid taking the last object, or to take the last object.

## Game play and illustration

The normal game is between two players and played with three heaps of any number of objects. The two players alternate taking any number of objects from any single one of the heaps. The goal is to be the last to take an object.

The following example of a normal game is played between fictional players Bob and Alice who start with heaps of three, four and five objects.

```
Sizes of heaps  Moves
A B C
 
3 4 5           Bob   takes 2 from A
1 4 5           Alice takes 3 from C
1 4 2           Bob   takes 1 from B
1 3 2           Alice takes 1 from B
1 2 2           Bob   takes entire A heap, leaving two 2s.
0 2 2           Alice takes 1 from B
0 1 2           Bob   takes 1 from C leaving two 1s. (In misère play he would take 2 from C leaving (0, 1, 0).)
0 1 1           Alice takes 1 from B
0 0 1           Bob   takes entire C heap and wins.
```

## Winning positions

The practical strategy to win at the game of ***Nim*** is for a player to get the other into one of the following positions, and every successive turn afterwards they should be able to make one of the lower positions. Only the last move changes between misere and normal play.

| 2 Heaps | 3 Heaps  | 4 Heaps   |
| ------- | -------- | --------- |
| 1 1 *   | 1 1 1 ** | 1 1 1 1 * |
| 2 2     | 1 2 3    | 1 1 n n   |
| 3 3     | 1 4 5    | 1 2 4 7   |
| 4 4     | 1 6 7    | 1 2 5 6   |
| 5 5     | 1 8 9    | 1 3 4 6   |
| 6 6     | 2 4 6    | 1 3 5 7   |
| 7 7     | 2 5 7    | 2 3 4 5   |
| 8 8     | 3 4 7    | 2 3 6 7   |
| 9 9     | 3 5 6    | 2 3 8 9   |
| (n n)   | 4 8 12   | 4 5 6 7   |
|         | 4 9 13   | 4 5 8 9   |
|         | 5 8 13   | n n m m   |
|         | 5 9 12   | n n n n   |

\* Only valid for normal play.

** Only valid for misere.

For the generalisations, *n* and *m* can be any value > 0, and they may be the same.

## Mathematical theory

Nim has been mathematically solved for any number of initial heaps and objects, and there is an easily calculated way to determine which player will win and what winning moves are open to that player.

The key to the theory of the game is the binary digital sum of the heap sizes, that is, the sum (in binary) neglecting all carries from one digit to another. This operation is also known as "exclusive or" (xor) or "vector addition over GF(2)" (bitwise addition modulo 2). Within combinatorial game theory it is usually called the **nim-sum** , as it will be called here. The nim-sum of x and y is written *x* ⊕ *y* to distinguish it from the ordinary sum, x+y. An example of the calculation with heaps of size 3,4 and 5 is as follows:

```
Binary Decimal
 
  0112    310    Heap A
  1002    410    Heap B
  1012    510    Heap C
  ---
  0102    210    The nim-sum of heaps A, B, and C, 3 ⊕ 4 ⊕ 5 = 2
```

An equivalent procedure, which is often easier to perform mentally, is to express the heap sizes as sums of distinct [powers](https://en.wikipedia.org/wiki/Exponentiation) of 2, cancel pairs of equal powers, and then add what is left:

```
3 = 0 + 2 + 1 =     2   1      Heap A
4 = 4 + 0 + 0 = 4              Heap B
5 = 4 + 0 + 1 = 4       1      Heap C
--------------------------------------------------------------------
2 =                 2          What is left after canceling 1s and 4s
```

In normal play, the winning strategy is to finish every move with a nim-sum of 0. This is always possible if the nim-sum is not zero before the move. If the nim-sum is zero, then the next player will lose if the other does not make a mistake. To find out which move to make, let X be the nim-sum of all the heap sizes. Find a heap where the nim-sum of X and heap-size is less than the heap-size - the winning strategy is to play in such a heap, reducing that heap to the nim-sum of its original size with X. In the example above, taking the nim-sum of the sizes is *X* = 3 ⊕ 4 ⊕ 5 = 2. The nim-sums of the heap sizes A=3, B=4, and C=5 with X=2 are 

    A ⊕ X = 3 ⊕ 2 = 1 [Since (011) ⊕ (010) = 001 ]
    B ⊕ X = 4 ⊕ 2 = 6
    C ⊕ X = 5 ⊕ 2 = 7
The only heap that is reduced is heap A, so the winning move is to reduce the size of heap A to 1 (by removing two objects).

As a particular simple case, if there are only two heaps left, the strategy is to reduce the number of objects in the bigger heap to make the heaps equal. After that , no matter what move your opponent makes, you can make the same move on the other heap, guaranteeing that you take the last object. 

When played as a misère game, Nim strategy is different only when the  normal play move would leave only heaps of size one. In that case, the  correct move is to leave an odd number of heaps of size one (in normal  play, the correct move would be to leave an even number of such heaps).

These strategies for normal play and a misère game are the same until the number of heaps with at least two objects is exactly equal to one.  At that point, the next player removes either all or all but one  objects from the heap that has two or more, so no heaps will have more  than one object (in other words, all remaining heaps have exactly one  object each), so the players are forced to alternate removing exactly  exactly one object until the game ends.  In normal play, the player  leaves an even number of non-zero heaps, and player takes last; in  misère play), the players leaves an odd number of non-zero heaps, so the other player takes last.

In a misère game with heaps of sizes three, four and five, the strategy would be applied like this:

```
A B C nim-sum 
 
3 4 5 0102=210   I take 2 from A, leaving a sum of 000, so I will win.
1 4 5 0002=010   You take 2 from C
1 4 3 1102=610   I take 2 from B
1 2 3 0002=010   You take 1 from C
1 2 2 0012=110   I take 1 from A
0 2 2 0002=010   You take 1 from C
0 2 1 0112=310   The normal play strategy would be to take 1 from B, leaving an even number (2)
                 heaps of size 1.  For misère play, I take the entire B heap, to leave an odd
                 number (1) of heaps of size 1.
0 0 1 0012=110   You take 1 from C, and lose.
```

## 参考

[Nim](https://en.wikipedia.org/wiki/Nim#Mathematical_theory)