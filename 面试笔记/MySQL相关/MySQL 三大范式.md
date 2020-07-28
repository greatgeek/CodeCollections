# MySQL 三大范式

## 第一范式：主键、字段不能再分

定义：要求有主键，数据库中每一字段都是原子性不能再分；

结论：

1. 每一行必须唯一，也就是每个表必须有主键，这是我们数据库设计的最基本要求；
2. 主键通常采用数值型或定长字符串表示；
3. 关于列不可再分，应根据具体的情况来决定。如联系方式，为了开发上的便利可能就采用一个原本可再分的字段了。

## 第二范式：非主键字段完全依赖主键

定义：第二范式是建立在第一范式基础之上，要求数据库中所有非主键字段完全依赖主键，不能产生部分依赖；**（严格意义上说：尽量不要使用联合主键）**

### 多对多的关系

两张表分别存储个体的信息，还需要第三张关系表来存储**多对多的关系**。

例：学生表、教师表、学生与老师的关系表。

其中关系表中，学生字段是外键字段来自于学生表，教师字段是外键字段来自于教师表。

## 第三范式

定义：建立在第二范式基础之上，要求非主键字段不能产生传递依赖于主键字段。

### 一对多的关系

一对多的关系，在多的一方添加外键。

### 一对一的关系

#### 方案一：分两张表存储，共享主键

例：t_husband和 t_wife 两张表

```mysql
create table t_hansband(
    hno int(4) primary key,
    hname varchar(32),
    wifeno int(4) unique,
    foreign key(wifeno) references t_wife(wno)
);
```

```mysql
create table t_wife(
    wno int(4) primary key,
    wname varchar(32)
);
```

#### 方案二：使用第三张表，关系表，加外键和加唯一

```mysql
create table t_hansband(
    hno int(4) primary key,
    hname varchar(32),
);
```

```mysql
create table t_wife(
    wno int(4) primary key,
    wname varchar(32)
);
```

```mysql
create table t_couple(
    hansbandno int(4) unique,
    wifeno int(4) unique,
    foreign key(hansbandno) references t_hansband(hno),
    foreign key(wifeno) references t_wife(wno)
);
```

