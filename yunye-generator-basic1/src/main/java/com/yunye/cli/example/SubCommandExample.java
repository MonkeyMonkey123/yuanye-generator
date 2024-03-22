package com.yunye.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "main", mixinStandardHelpOptions = true)
public class SubCommandExample implements Runnable {

    @Override
    public void run() {
        System.out.println("执行主命令");

    }

    @Command(name = "add", description = "增加")
    static class AddCommend implements Runnable {

        @Override
        public void run() {
            System.out.println("增加");

        }
    }

    @Command(name = "delete", description = "删除")

    static class DeleteCommend implements Runnable {

        @Override
        public void run() {
            System.out.println("删除");
        }
    }

    @Command(name = "query", description = "删除")

    static class QueryCommend implements Runnable {

        @Override
        public void run() {
            System.out.println("执行查询");
        }
    }

    public static void main(String[] args) {

//        String [] myArgs = new String [] { };
        String[] myArgs = new String[]{"add", "--help"};
//        String[] myArgs = new String[]{"add"};
//        String[] myArgs = new String[]{"--help"};
        //不存在命令报错
//        String[] myArgs = new String[]{"update"};
        int execute = new CommandLine(new SubCommandExample())
                .addSubcommand(new AddCommend())
                .addSubcommand(new DeleteCommend())
                .addSubcommand(new QueryCommend())
                .execute(myArgs);
        System.exit(execute);
    }
}
