import java.io.*;
import java.nio.file.*;
public class Run{
	public static void main(String[] args){
		try{
					int noLines = (int)Files.lines(Paths.get(args[0])).count();
					String[] lines = new String[noLines];
					BufferedReader reader = new BufferedReader(new FileReader(args[0]));
					String line;
					for (int i = 0;(line = reader.readLine()) != null; i++){
						lines[i] = line;
					}
					Parser.Prog prog = Parser.parse(lines);
					run(prog);
			} catch (Exception e){
				e.printStackTrace();
			}
	}
	public static void run(Parser.Prog prog){
		for (Parser.Inst i : prog.instructions){
			if (i == null)//we want to skip non-existant line numbers
				continue;
				if (i.command == Parser.Command.PRINT){
					print(i);
					continue;
				} else {
					System.out.println("  command "+i.command +" not recognised");
				}
		}
	}

	public static void print(Parser.Inst inst){
		for (int i= 0; i < inst.params.length; i++)
			System.out.print(inst.params[i] + " ");
		System.out.println();
	}
}
