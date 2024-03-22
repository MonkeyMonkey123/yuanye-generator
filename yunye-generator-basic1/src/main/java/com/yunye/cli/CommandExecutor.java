package com.yunye.cli;

import com.yunye.cli.command.ConfigCommand;
import com.yunye.cli.command.GeneratorCommand;
import com.yunye.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "yunye", description = "ACM模板生成器命令启动类", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{
    private final CommandLine commandLine;
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GeneratorCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        System.out.println("请输入具体命令，或输入--help查看命令");
    }

    public Integer doExecute(String[] args) {

        return commandLine.execute(args);

    }
}
