# Java 中创建对象的5种方式

| **使用new 关键字**                                 | **调用了构造函数**   |
| -------------------------------------------------- | -------------------- |
| **使用Class类的newInstance方法（JDK8中已被废除）** | **调用了构造函数**   |
| **使用Constructor类的newInstance方法**             | **调用了构造函数**   |
| **使用clone方法**                                  | **没有调用构造函数** |
| **使用反序列化**                                   | **没有调用构造函数** |

## 1. 使用 new 关键字

通过这种方式，我们可以调用任意的构造函数（无参的和带参的）

```java
Employee emp1 = new Employee();
0: new           #19          // class org/programming/mitra/exercises/Employee
3: dup
4: invokespecial #21          // Method org/programming/mitra/exercises/Employee."":()V
```

## 2. 使用Class类的newInstance方法(JDK8 中已被废除)

这个`newInstance`方法调用无参的构造函数创建对象。

```java
Employee emp2 = (Employee) Class.forName("org.programming.mitra.exercises.Employee").newInstance();
或者

Employee emp2 = Employee.class.newInstance();
51: invokevirtual    #70    // Method java/lang/Class.newInstance:()Ljava/lang/Object;
```

## 3. 使用Constructor类的newInstance方法

`java.lang.reflect.Constructor` 类里也有一个`newInstance`方法可以创建对象。通过这个`newInstance`方法可以调用有参数的和私有的构造函数。

```java
Constructor<Employee> constructor = Employee.class.getConstructor();
Employee emp3 = constructor.newInstance();
111: invokevirtual  #80  // Method java/lang/reflect/Constructor.newInstance:([Ljava/lang/Object;)Ljava/lang/Object;
```

这两种`newInstance`方法就是大家所说的反射。事实上Class的newInstance方法内部调用Constructor的newInstance方法。这也是众多框架，如Spring，Hibernate,Struts等使用后者的原因。

## 4. 使用clone方法

无论何时我们调用一个对象的clone方法，JVM就会创建一个新的对象，将前面的对象的内容全部拷贝进去。用clone方法创建对象并不会调用任何构造函数。

要使用clone方法，需要先实现`Cloneable`接口并实现`clone`方法，它的作用是：

A class implements the `Cloneable` interface to indicate to the `Object.clone()` method that it is legal for that method to make a field-for-field copy of instances of that class.

```java
public class A implements Cloneable{
    //...
    
    @Override
    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
```



```java
Employee emp4 = (Employee) emp3.clone();
162: invokevirtual #87  // Method org/programming/mitra/exercises/Employee.clone ()Ljava/lang/Object;
```

## 5. 使用反序列化

当我们序列化和反序列化一个对象时，JVM会给我们创建一个单独的对象。在反序列化时，JVM创建对象并不会调用任何构造函数。

为了反序列化一个对象，我们需要让我们的类实现`Serializable`接口。

```java
		// By using clone() method
        Employee emp4 = (Employee) emp3.clone();
        emp4.setName("Atul");
        System.out.println(emp4 + ", hashcode : " + emp4.hashCode());
        // By using Deserialization
        // Serialization
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("data.obj"));
        out.writeObject(emp4);
        out.close();
        //Deserialization
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
        Employee emp5 = (Employee) in.readObject();
        in.close();
```



```java
ObjectInputStream in = new ObjectInputStream(new FileInputStream("data.obj"));
Employee emp5 = (Employee) in.readObject();
261: invokevirtual  #118   // Method java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
```

## 参考

[Java 中创建对象的5种方式](https://www.cnblogs.com/wxd0108/p/5685817.html)