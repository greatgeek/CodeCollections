## MySQL 锁-何时加锁？加什么锁？

参考：https://dev.mysql.com/doc/refman/5.7/en/innodb-locking.html

### InnoDB Locking

This section describes lock types used by `InnoDB`.

* Shared and Exclusive Locks
* Intention Locks
* Record Locks
* Gap Locks
* Next-Key Locks
* Insert Intention Locks
* AUTO-INC Locks
* Predicate Locks for Spatial Indexes

### Shared and Exclusive Locks

`InnoDB` implements standard row-level locking where there are two types of locks, shared(s) locks and exclusive(x) locks.

* A shared(s) lock permits the transaction that holds the lock to read a row.
* An exclusive(x) lock permits the transaction that holds the lock to update or delete a row.

If transaction `T1` holds a shared(s) lock on row `r`, then requests from some distinct transaction `T2` for a lock on row `r` are handled as follows:

* A request by `T2` for an `S` lock can be granted immediately. As a result, both `T1` and `T2` hold an `S` lock on `r`.
* A request by `T2` for an `X` lock cannot be granted immediately.

If a transaction `T1` holds an exclusive(x) lock on row `r`, a request from some distinct transaction `T2` for a lock of either type on `r` cannot be granted immediately. Instead transaction `T2` has to wait for transaction `T1` to release its lock on row `r`.

### Intention Locks

`InnoDB` supports ***multiple granularity locking***  which permits coexistence of row locks and table locks. For example, a statement such as `LOCK TABLES ... WRITE` takes an exclusive lock(an x lock) on the specified table. To make locking at multiple granularity levels practical, `InnoDB` uses ***intention locks***. Intention locks are table-level locks that indicate which type of lock (shared or exclusive) a transaction requires later for a row in a table. There are two types of intention locks:

* An intention shared lock(IS) indicates that a transaction intends to set a ***shared*** lock on individual rows in a table.
* An intention exclusive lock(IX) indicates that a transaction intends to set an exclusive lock on individual rows in a table.

For example, `SELECT ... LOCK IN SHARE MODE` sets an IS lock, and `SELECT ... FOR UPDATE` sets an IX lock.

The intention locking protocol is as follows:

* Before a transaction can acquire a shared lock on a row in a table, it must first acquire an IS lock or stronger on the table.

* Before a transaction can acquire an exclusive lock on a row in a table, it must first acquire an IX lock on the table.

Table-level lock type compatibility is summarized in the        following matrix.

|      | `X`      | `IX`       | `S`        | `IS`       |
| ---- | -------- | ---------- | ---------- | ---------- |
| `X`  | Conflict | Conflict   | Conflict   | Conflict   |
| `IX` | Conflict | Compatible | Conflict   | Compatible |
| `S`  | Conflict | Conflict   | Compatible | Compatible |
| `IS` | Conflict | Compatible | Compatible | Compatible |

A lock is granted to a requesting transaction if it is compatible with existing locks, but not if it conflicts with existing locks. A transaction waits until the conflicting existing lock is released. If a lock request conflicts with an existing lock and cannot be granted because it would cause [deadlock](https://dev.mysql.com/doc/refman/5.7/en/glossary.html#glos_deadlock), an error occurs.      

Intention locks do not block anything except full table requests (for example, [`LOCK TABLES ... WRITE`](https://dev.mysql.com/doc/refman/5.7/en/lock-tables.html)). The main purpose of intention locks is to show that someone is locking a row, or going to lock a row in the table.      

Transaction data for an intention lock appears similar to the following in [`SHOW ENGINE INNODB STATUS`](https://dev.mysql.com/doc/refman/5.7/en/show-engine.html) and [InnoDB monitor](https://dev.mysql.com/doc/refman/5.7/en/innodb-standard-monitor.html) output:      

```mysql
TABLE LOCK table `test`.`t` trx id 10080 lock mode IX
```

### Record Locks (索引失效，行锁就会升级为表锁)

**A record lock is a lock on an index record.** For example, SELECT c1 FROM t WHERE c1 = 10 FOR UPDATE;`        prevents any other transaction from inserting, updating, or deleting rows where the value of `t.c1` is        `10`.      

**Record locks always lock index records, even if a table is defined with no indexes.** For such cases, InnoDB` creates a hidden clustered index and uses this index for record locking. See Section 14.6.2.1,[ “Clustered and Secondary Indexes”](https://dev.mysql.com/doc/refman/5.7/en/innodb-index-types.html).      

Transaction data for a record lock appears similar to the following in [`SHOW ENGINE INNODB STATUS`](https://dev.mysql.com/doc/refman/5.7/en/show-engine.html) and [InnoDB monitor](https://dev.mysql.com/doc/refman/5.7/en/innodb-standard-monitor.html) output:      

```mysql
RECORD LOCKS space id 58 page no 3 n bits 72 index `PRIMARY` of table `test`.`t`
trx id 10078 lock_mode X locks rec but not gap
Record lock, heap no 2 PHYSICAL RECORD: n_fields 3; compact format; info bits 0
 0: len 4; hex 8000000a; asc     ;;
 1: len 6; hex 00000000274f; asc     'O;;
 2: len 7; hex b60000019d0110; asc        ;;
```

### Gap Locks

A gap lock is a lock on a gap between index records, or a lock on the gap before the first or after the last index record. For example, `SELECT c1 FROM t WHERE c1 BETWEEN 10 and 20 FOR UPDATE`; prevents other transactions from inserting a value of `15` into column `t.c1`, whether or not there was already any such value in the column, because the gaps between all existing values in the range are locked.

A gap might span a single index value, multiple index values, or even be empty.

Gap locks are part of the trade off between performance and concurrency, and are used in some transaction isolation levels and not others.

Gap locking is not needed for statements that lock rows using a unique index to search for a unique row. (This does not include the case that the search condition includes only some columns of a multiple-column unique index; in that case, gap locking does occur.) For example, if the `id` column has a unique index, the following statement uses only an index-record lock for the row having `id` value 100 and it does not matter whether other sessions insert rows in the preceding gap:

```mysql
SELECT * FROM child WHERE id = 100;
```

If `id` is not indexed or has non unique index, the statement does lock the preceding gap.

It is also worth noting here that conflicting locks can be held on a gap by different transactions. For example, transaction A can hold a shared gap lock (gap-S-lock) on a gap while transaction B holds an exclusive gap lock (gap X-lock) on the same gap. The reason conflicting gap locks are allowed is that if a record is purged from an index, the gap locks held on the record by different transactions must be merged.

Gap locks in `InnoDB` are "purely inhibitive" , which means that their only purpose is to prevent other transactions from inserting to the gap. Gap locks can co-exist. A gap lock taken by one transaction does not prevent another transaction from taking a gap lock on the same gap. There is no difference between shared and exclusive gap locks. They do not conflict with each other, and they perform the same function. 

Gap locking can be disabled explicitly. This occurs if you change the transaction isolation level to [`READ COMMITTED`](https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#isolevel_read-committed) or enable the [innodb_locks_unsafe_for_binlog` system variable (which is now deprecated). Under these circumstances, gap locking is disabled for searches and index scans and is used only for foreign-key constraint checking and duplicate-key checking.      

There are also other effects of using the [`READ COMMITTED`](https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#isolevel_read-committed) isolation level or enabling [innodb_locks_unsafe_for_binlog`](https://dev.mysql.com/doc/refman/5.7/en/innodb-parameters.html#sysvar_innodb_locks_unsafe_for_binlog). Record locks for nonmatching rows are released after MySQL has evaluated the `WHERE` condition. For   `UPDATE` statements, `InnoDB`  does a “semi-consistent” read, such that it returns the latest committed version to MySQL so that MySQL can determine whether the row matches the `WHERE`   condition of the [`UPDATE`](https://dev.mysql.com/doc/refman/5.7/en/update.html).

### Next-Key Locks

A next-key lock is a combination of a record lock on the index record and a gap lock on the gap before the index record.      

`InnoDB` performs row-level locking in such a way that when it searches or scans a table index, it sets shared or exclusive locks on the index records it encounters. Thus, the row-level locks are actually index-record locks. A next-key lock on an index record also affects the “gap” before that index record. That is, a next-key lock is an index-record        lock plus a gap lock on the gap preceding the index record. If one session has a shared or exclusive lock on record        `R` in an index, another session cannot insert a new index record in the gap immediately before  `R` in the index order.      

Suppose that an index contains the values 10, 11, 13, and 20. The possible next-key locks for this index cover the following intervals, where a round bracket denotes exclusion of the interval endpoint and a square bracket denotes inclusion of the endpoint:      

```mysql
(negative infinity, 10]
(10, 11]
(11, 13]
(13, 20]
(20, positive infinity)
```

​        For the last interval, the next-key lock locks the gap above the largest value in the index and the “supremum”        pseudo-record having a value higher than any value actually in the index. The supremum is not a real index record, so, in effect, this next-key lock locks only the gap following the largest index value.      

By default, `InnoDB` operates in  [`REPEATABLE READ`](https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html#isolevel_repeatable-read) transaction isolation level. In this case, `InnoDB` uses        next-key locks for searches and index scans, which prevents phantom rows (see [Section 14.7.4, “Phantom Rows”](https://dev.mysql.com/doc/refman/5.7/en/innodb-next-key-locking.html)).      

 Transaction data for a next-key lock appears similar to the following in [`SHOW ENGINE INNODB STATUS`](https://dev.mysql.com/doc/refman/5.7/en/show-engine.html) and   [InnoDB monitor](https://dev.mysql.com/doc/refman/5.7/en/innodb-standard-monitor.html)  output:      

```mysql
RECORD LOCKS space id 58 page no 3 n bits 72 index `PRIMARY` of table `test`.`t`
trx id 10080 lock_mode X
Record lock, heap no 1 PHYSICAL RECORD: n_fields 1; compact format; info bits 0
 0: len 8; hex 73757072656d756d; asc supremum;;

Record lock, heap no 2 PHYSICAL RECORD: n_fields 3; compact format; info bits 0
 0: len 4; hex 8000000a; asc     ;;
 1: len 6; hex 00000000274f; asc     'O;;
 2: len 7; hex b60000019d0110; asc        ;;
```

### Insert Intention Locks

An insert intention lock is a type of gap lock set by `INSERT` operations prior to row insertion. This lock signals the intent to insert in such a way that multiple transactions inserting into the same index gap need not wait for each other if they are not inserting at the same position within the gap. Suppose that there are index records with values of 4 and 7. Separate transactions that attempt to insert values of 5 and 6, respectively ,each lock the gap between 4 and 7 with insert intention locks prior to obtaining the exclusive lock on the inserted row, but do not block each other because the rows are non conflicting.

The following example demonstrates a transaction taking an insert intention lock prior to obtaining an exclusive lock on the inserted record. The example involves two clients, A and B.

Client A creates a table containing two index records (90 and 102) and then starts a transaction that places an exclusive lock on index records with an ID greater than 100. The exclusive lock includes a gap lock before record 102:

```msyql
mysql> CREATE TABLE child (id int(11) NOT NULL, PRIMARY KEY(id)) ENGINE=InnoDB;
mysql> INSERT INTO child (id) values (90),(102);

mysql> START TRANSACTION;
mysql> SELECT * FROM child WHERE id > 100 FOR UPDATE;
+-----+
| id  |
+-----+
| 102 |
+-----+
```

Client B begins a transaction to insert a record into the gap. The transaction takes an insert intention lock while it waits to obtain an exclusive lock.

```mysql
mysql> START TRANSACTION;
mysql> INSERT INTO child (id) VALUES (101);
```

Transaction data for an insert intention lock appears similar to the following in [`SHOW ENGINE INNODB STATUS`](https://dev.mysql.com/doc/refman/5.7/en/show-engine.html) and  [InnoDB monitor](https://dev.mysql.com/doc/refman/5.7/en/innodb-standard-monitor.html)   output:      

```mysql
RECORD LOCKS space id 31 page no 3 n bits 72 index `PRIMARY` of table `test`.`child`
trx id 8731 lock_mode X locks gap before rec insert intention waiting
Record lock, heap no 3 PHYSICAL RECORD: n_fields 3; compact format; info bits 0
 0: len 4; hex 80000066; asc    f;;
 1: len 6; hex 000000002215; asc     " ;;
 2: len 7; hex 9000000172011c; asc     r  ;;...
```

