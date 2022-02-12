package cn.xiw.compiler;

import java.io.FileInputStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cn.xiw.compiler.codegen.CodeGenerator;
import cn.xiw.compiler.lexer.Lexer;
import cn.xiw.compiler.parser.Parser;

@SpringBootApplication
public class CompilerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CompilerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try (var inputStream = new FileInputStream(args[0])) {
			var lexer = new Lexer(inputStream);
			var parser = new Parser(lexer);
			var ast = parser.parse();
			var codegen = new CodeGenerator(System.out);
			ast.accept(codegen);
		}
	}
}
