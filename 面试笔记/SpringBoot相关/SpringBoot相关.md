# SpringBoot 相关



## SpringBoot 的 Starter

The `spring-boot-starter-parent` is a special starter that provides useful Maven defaults. It also provides a [`dependency-management`](https://docs.spring.io/spring-boot/docs/2.3.2.RELEASE/reference/html/using-spring-boot.html#using-boot-dependency-management) section so that you can omit `version` tags for “blessed” dependencies. （**starter-parent 提供依赖管理，可以在之后 的依赖中省略版本号）**

Other “Starters” provide dependencies that you are likely to need when developing a specific type of application. Since we are developing a web application, we add a `spring-boot-starter-web` dependency. **（其他的starter 提供了特定类型应用所需要的依赖，例如：`spring-boot-starter-web`）**



## The @RestController and @RequestMapping Annotations

```java
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Example {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        SpringApplication.run(Example.class, args);
    }
}
```

`@RestController` . This is known as a *stereotype* annotation. It provides hints for people reading the code and for Spring that the class plays a specific role. In this case, our class is a web `@Controller`, so Spring considers it when **handling incoming web requests**.

The `@RequestMapping` annotation provides **“routing” information**. It tells Spring that any HTTP request with the `/` path should be mapped to the `home` method. The `@RestController` annotation tells Spring to render the resulting string directly back to the caller.

## The @EnableAutoConfiguration Annotation

The second class-level annotation is `@EnableAutoConfiguration`. This annotation tells Spring Boot to “guess” how you want to configure  Spring, based on the jar dependencies that you have added. Since `spring-boot-starter-web` added **Tomcat** and **Spring MVC**,  **the auto-configuration assumes that you are developing a web application and sets up Spring accordingly.**