# Minimax 博弈

## Game theory

### In general games

The **maximin value** is the highest value that the player can be sure to get without knowing the actions of the other players; equivalently, it is the lowest value the other players can force the player to receive when they know the player's action.

### In zero-sum games

In two-player [zero-sum games](https://en.wikipedia.org/wiki/Zero-sum_game), the minimax solution is the same as the [Nash equilibrium](https://en.wikipedia.org/wiki/Nash_equilibrium).

In the context of zero-sum games, the [minimax theorem](https://en.wikipedia.org/wiki/Minimax_theorem) is equivalent to:[[4\]](https://en.wikipedia.org/wiki/Minimax#cite_note-Osborne-4)[*[failed verification](https://en.wikipedia.org/wiki/Wikipedia:Verifiability)*]

For every two-person, [zero-sum](https://en.wikipedia.org/wiki/Zero-sum) game with finitely many strategies, there exists a value V and a mixed strategy for each player, such that

​	(a) Given player 2's strategy, the best payoff possible for player 1 is V, and

​	(b) Given player 1's strategy, the best payoff possible for player 2 is −V.

Equivalently, Player 1's strategy guarantees them a payoff of V regardless of Player 2's strategy, and similarly Player 2 can guarantee themselves a payoff of −V. The name minimax arises because each player minimizes the maximum payoff possible for the other—since the game is zero-sum, they also minimize their own maximum loss (i.e. maximize their minimum payoff). 

## Combinatorial game theory

In [combinatorial game theory](https://en.wikipedia.org/wiki/Combinatorial_game_theory), there is a minimax algorithm for game solutions.

### Minimax algorithm with alternate moves

A **minimax algorithm**[[5\]](https://en.wikipedia.org/wiki/Minimax#cite_note-5) is a recursive [algorithm](https://en.wikipedia.org/wiki/Algorithm) for choosing the next move in an n-player [game](https://en.wikipedia.org/wiki/Game_theory), usually a two-player game. A value is associated with each position or state of the game. This value is computed by means of a [position evaluation function](https://en.wikipedia.org/wiki/Evaluation_function) and it indicates how good it would be for a player to reach that position. The player then makes the move that maximizes the minimum value of the position resulting from the opponent's possible following moves. If it is **A**'s turn to move, **A** gives a value to each of their legal moves.

A possible allocation method consists in assigning a certain win for **A** as +1 and for **B** as −1. This leads to [combinatorial game theory](https://en.wikipedia.org/wiki/Combinatorial_game_theory) as developed by [John Horton Conway](https://en.wikipedia.org/wiki/John_Horton_Conway). An alternative is using a rule that if the result of a move is an immediate win for **A** it is assigned positive infinity and if it is an immediate win for **B**, negative infinity. The value to **A** of any other move is the maximum of the values resulting from each of **B**'s possible replies. For this reason, **A** is called the *maximizing player* and **B** is called the *minimizing player*, hence the name *minimax algorithm*. The above algorithm will assign a value of positive or negative infinity to any position since the value of every position will be the value of some final winning or losing position. Often this is generally only possible at the very end of complicated games such as [chess](https://en.wikipedia.org/wiki/Chess) or [go](https://en.wikipedia.org/wiki/Go_(board_game)), since it is not computationally feasible to look ahead as far as the completion of the game, except towards the end, and instead, positions are given finite values as estimates of the degree of belief that they will lead to a win for one player or another.

### Pseudocode[[edit](https://en.wikipedia.org/w/index.php?title=Minimax&action=edit&section=9)]

The [pseudocode](https://en.wikipedia.org/wiki/Pseudocode) for the depth limited minimax algorithm is given below.

```
function minimax(node, depth, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic(启发式的) value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, minimax(child, depth − 1, FALSE))
        return value
    else (* minimizing player *)
        value := +∞
        for each child of node do
            value := min(value, minimax(child, depth − 1, TRUE))
        return value
```
```   
(* Initial call *)
minimax(origin, depth, TRUE)
```

The minimax function returns a heuristic value for [leaf nodes](https://en.wikipedia.org/wiki/Leaf_nodes) (terminal nodes and nodes at the maximum search depth). Non leaf nodes inherit their value from a descendant leaf node. The heuristic value is a score measuring the favorability of the node for the maximizing player. Hence nodes resulting in a favorable outcome, such as a win, for the maximizing player have higher scores than nodes more favorable for the minimizing player. The heuristic value for terminal (game ending) leaf nodes are scores corresponding to win, loss, or draw, for the maximizing player. For non terminal leaf nodes at the maximum search depth, an evaluation function estimates a heuristic value for the node. The quality of this estimate and the search depth determine the quality and accuracy of the final minimax result.

## 参考

https://en.wikipedia.org/wiki/Minimax