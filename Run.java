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
			} else if (i.command == Parser.Command.IF){
				if (statementTrue(prog, i.params[0], i.params[1], i.params[2]))
					continue;
				else{
					pc = i.matchingIfOrEnd.lineNo - 1;//skip to ENDIF
					continue;
				}
			}	else if (i.command == Parser.Command.ENDIF){
					continue;
			} else {
				System.out.println("  command "+i.command +" not recognised");
			}
		}//pc loop
	}//run

	public static boolean statementTrue(Parser.Prog prog, String x, String comp, String y){
		//first of all, check if x and/or y are variables, if so, retrieve their values and replace
		//x or y with that value
		if (x.substring(0,1).equals("$")){
			x = prog.vars.get(x.substring(1,x.length()));
			if (x == null){
				System.out.println("$"+ x +
							"does not exist or is null, treating statment as false and jumping to next ENDIF");
				return false;
			}
		}
		if (y.substring(0,1).equals("$")){
			y = prog.vars.get(y.substring(1,y.length()));
			if (y == null){
				System.out.println("$"+ y +
							"does not exist or is null, treating statment as false and jumping to next ENDIF");
				return false;
			}
		}
		//for now we will assume int and String are the only supported types (may be true, check spec)
		//try for integers, failing that, fall back to Strings, note both should be the same, but if we 
		//encounter a type mismatch, the expression should evaluate as false anyway
		try{
			int xInt = Integer.parseInt(x);
			int yInt = Integer.parseInt(y);
			if (comp.equals("<"))
				return xInt < yInt;
			if (comp.equals("="))
					return xInt == yInt;
			if (comp.equals(">"))
					return xInt > yInt;
		} catch (Exception e){
			//use default java string comparison for now
			if (comp.equals("<"))
				return x.compareTo(y) < 0;
			if (comp.equals("="))
				return x.equals(y);
			if (comp.equals(">"))
				return x.compareTo(y) > 0;
		}
		return false;
	}
}//class
