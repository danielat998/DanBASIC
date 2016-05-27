import java.io.*;
import java.nio.file.*;
import java.util.*;
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
//		for (Parser.Inst i : prog.instructions){
		Scanner input = new Scanner(System.in);
		for (int pc = 0; pc < prog.instructions.length;pc++){
			Parser.Inst i = prog.instructions[pc];
			if (i == null)//we want to skip non-existant line numbers
				continue;
			if (i.command == Parser.Command.PRINT){
				for (String s : i.params){
					if (s.substring(0,1).equals("$")){
						System.out.print(prog.vars.get(s.substring(1,s.length())) + " ");
					} else {
						System.out.print(s + " ");
					}
				}
				System.out.println();
				continue;
			} else if (i.command == Parser.Command.GOTO){
				pc = Integer.parseInt(i.params[0]) -1;//-1 because it wil get incremented at end of loop
				continue;
			} else if (i.command == Parser.Command.INPUT){
				prog.vars.put(i.params[0], input.nextLine());
				continue;
			} else {
				System.out.println("  command "+i.command +" not recognised");
			}
		}//pc loop
	}//run
}//class
