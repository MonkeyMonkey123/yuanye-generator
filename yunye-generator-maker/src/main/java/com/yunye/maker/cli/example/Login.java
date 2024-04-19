package com.yunye.maker.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * 交互式输入
 */
class Login implements Callable<Integer> {
    @Option(names = {"-u", "--user"}, description = "User name", arity = "0..1")
    String user;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, arity = "0..1")
   String password;

    public Integer call() throws Exception {

        System.out.println("names:" + user);
        System.out.println("password:" + password);

        return 0;
    }

    public static void main(String[] args) {
        new CommandLine(new Login()).execute("-u", "user123", "-p", "123");
    }


}