import java.util.HashMap;
public class Parser{
	//only use this for testing
	public static void main(String[] args){
		String[] lines = {"0 PRINT 10", "10 print hello"};
		Prog prog = parse(lines);
		System.out.println(prog.instructions[0] + "\n" + prog.instructions[10]);
	}

	public static enum Command {
		PRINT,INPUT,GOTO,IF,THEN//...
	}

	public static class Inst{
		public int lineNo;//same as array index
		public Command command;
		public String param;
		@Override
		public String toString(){
			return "lineNo:"+lineNo + ", command:" + command +", param:" + param;
		}
		/*public Inst(int line, String command, ...){
			lineNo = line;
		}*/
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

		for (String line:lines){
			String[] tokens = line.split(" ");
			Inst inst = new Inst();
			inst.lineNo = Integer.parseInt(tokens[0]);
			prog.instructions[inst.lineNo] = inst;
			switch(tokens[1]){//actual command
				case "PRINT":case "print":
						inst.command = Command.PRINT;
						inst.param = tokens[2];
						break;
			}
		}
		return prog;
	}
}
