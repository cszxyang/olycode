# Olycode

Olycode is an online integrated development environment which supports executing the code written in Java, Python, Lua and other programming languages for free.

## How to build

A Java-related development environment should be installed in your machine, once you have configured the maven environment variables, you can package the project by typing the following command on the command line.

```shell
mvn clean package
```
Olycode is based on SpringBoot, so you can easily deploy and run the jar locally by simply using the following command.

```shell
java -jar olycode.jar
```

## Related technologies

- Java dynamic compilation and class loading process
- SpringBoot asynchronous programming
- Bytecode manipulation
- System I/O proxy
- Java abstract syntax tree manipulation
- Homemade lua compiler and virtual machine extended from [zxh0/luago-book](https://github.com/zxh0/luago-book)
