package ${basePackage}.cli;

import ${basePackage}.cli.command.ConfigCommand;
import ${basePackage}.cli.command.GeneratorCommand;
import ${basePackage}.cli.command.ListCommand;
import ${basePackage}.cli.command.JSONGeneratorCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "${name}", description = "ACM模板生成器命令启动类", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{
    private final CommandLine commandLine;
    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GeneratorCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand())
                .addSubcommand(new JSONGeneratorCommand());
    }

    @Override
    public void run() {
        System.out.println("请输入具体命令，或输入--help查看命令");
    }

    public Integer doExecute(String[] args) {

        return commandLine.execute(args);

    }
}
