import java.util.HashMap;
/* As a guideline, see:https://en.wikipedia.org/wiki/Tiny_BASIC but note we are not necessarily
 aiming to implement this perfectly*/
public class Parser{
	//only use this for testing
	public static void main(String[] args){
		String[] lines = {"0 PRINT 10 20 hello goodbye", "10 goto 0"};
		Prog prog = parse(lines);
		System.out.println(prog.instructions[0] + "\n" + prog.instructions[10]);
	}

	public static enum Command {
		PRINT,INPUT,GOTO,IF,THEN,LET//...
	}

	public static class Inst{
		public int lineNo;//same as array index
		public Command command;
		public String[] params;
		@Override
		public String toString(){
			String prms = "";
			for (String p:params)
				prms += " " + p;
			return "lineNo:"+lineNo + ", command:" + command +", params:" + prms;
		}
	}

	public static class Prog{//keep things public and simple
		public Inst[] instructions;//this will go from 0 to last line no, null for gaps...;
		//for now, we will treat all vars as Strings
		public HashMap<String,String> vars = new HashMap<String,String>();
	}

	public static int getLastLineNo(String[] lines){
		return Integer.parseInt(lines[lines.length -1].split(" ")[0]);
	}

	//keep everything static
	//take an array of lines, each corresponding to a command, put these into the array
  public static Prog parse(String[] lines){
		Prog prog = new Prog();
		prog.instructions = new Inst[getLastLineNo(lines) + 1];
		boolean lastLineWasIf = false;

		for (String line:lines){
			String[] tokens = line.split(" ");
			Inst inst = new Inst();
			inst.lineNo = Integer.parseInt(tokens[0]);
			prog.instructions[inst.lineNo] = inst;
			if (lastLineWasIf){//must wait for endif...
				//inst.command 
				continue;
			}
			switch(tokens[1]){//actual command
				case "PRINT":case "print":
						inst.command = Command.PRINT;
						inst.params = new String[tokens.length-2];
						for (int i = 0; i < tokens.length -2;i++)
							inst.params[i] = tokens[i+2];
						break;
				case "GOTO":case"goto":
						inst.command = Command.GOTO;
						inst.params = new String[]{tokens[2]};
						break;
				case "IF":case"if":
						inst.command = Command.IF;
						inst.params = new String[]{tokens[2],tokens[3],tokens[4]};
						//gonna be complicated, not sure whether this belongs in the parse or interpreter
						break;
				case "INPUT":case "input":
						inst.command = Command.INPUT;
						inst.params = new String[tokens.length-2];
						for (int i = 0; i < tokens.length -2;i++)
							inst.params[i] = tokens[i+2];
						break;
				case "LET":case "let":
						inst.command = Command.LET;
						//finish this, again think at least part of this belongs in interpreter...
						//TODO:GOSUB,RETURN,CLEAR,LIST,RUN,END
				default:
						System.out.println("Unrecognised command, please try again");			
			}
		}
		return prog;
	}
}
