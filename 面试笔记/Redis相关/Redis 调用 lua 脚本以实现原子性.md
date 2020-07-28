# Redis 调用 lua 脚本以实现原子性

## Atomicity of scripts （可以实现类似 multi/ exec 相同的作用）

**Redis uses the same Lua interpreter to run all the commands. Also Redis guarantees that a script is executed in an atomic way: no other  script or Redis command will be executed while a script is being executed.** **This semantic is similar to the one of [MULTI](https://redis.io/commands/multi) / [EXEC](https://redis.io/commands/exec). From the point of view of all the other clients the effects of a script are either still not visible or already completed.**

However this also means that executing slow scripts is not a good idea.              It is not hard to create fast scripts, as the script overhead is very low, but              if you are going to use slow scripts you should be aware that while the script              is running no other client can execute commands.

## 参考

https://redis.io/commands/eval