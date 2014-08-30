package org.kaivos.fortob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.kaivos.fortob.environment.FortobEnvironment;
import org.kaivos.fortob.value.FortobBoolean;
import org.kaivos.fortob.value.FortobNumber;
import org.kaivos.fortob.value.FortobString;
import org.kaivos.fortob.value.FortobValue;
import org.kaivos.nept.parser.Token;
import org.kaivos.nept.parser.TokenList;

/**
 * FortobReadcom is a class that reads and interprets commands.
 * 
 * @author Iikka Hauhio
 */
public class FortobReadcom implements FortobCommand {

	private static int ilevel = 0;
	private static String i() {
		return IntStream.range(0, ilevel).mapToObj(i -> "\t").collect(Collectors.joining());
	}
	private static boolean DEBUG = false;
	
	/**
	 * FortobReadcom. The original and the best.
	 */
	public static final FortobReadcom READCOM = new FortobReadcom();
	
	private Map<String, FortobCommand> commandMap = new HashMap<>();
	
	private FortobReadcom() {
		
		commandMap.put(";", (tl, env) -> env.pop());
		
		commandMap.put("[", (tl, env) -> {
			String str = "";
			
			int i = 0;
			while (true) {
				if (tl.isNext("[")) i++;
				if (tl.isNext("]")) i--;
				if (i < 0) break;
				
				str += " " + tl.nextString();
			}
			
			tl.accept("]");
			
			env.push(new FortobString(str.trim()));
		});
		
		commandMap.put("\"", (tl, env) -> {
			String str = tl.nextString();
			str = str.substring(1, str.length()-1);
			
			env.push(new FortobString(str));
		});
		
		commandMap.put("_", (tl, env) -> env.push(new FortobString(" ")));
		
		commandMap.put("'", (tl, env) -> env.push(new FortobNumber(0)));
		
		IntStream.range(0, 9).forEach(i ->
			commandMap.put(""+i, (tl, env) -> env.push(env.pop().invokeMethod(env, "*", new FortobNumber(10)).invokeMethod(env, "+", new FortobNumber(i)))));
		
		commandMap.put(".", (tl, env) -> {
			FortobValue obj = env.pop();
			String method = tl.nextString();
			List<FortobValue> args = new ArrayList<>();
			
			if (tl.isNext(":")) {
				do {
					tl.next();
					args.add(eval(tl, env));
				} while (tl.isNext(","));
			}
			
			if (DEBUG) System.err.println(i() + obj + "." + method + args);
			ilevel++;
			env.push(obj.invokeMethod(env, method, args.toArray(new FortobValue[0])));
			ilevel--;
			if (DEBUG) System.err.println(i() + "= " + env.peek());
		});
		
		for (String operator : new String[] {"+", "-", "*", "/", "=", "<", ">"})
			commandMap.put(operator, (tl, env) -> env.push(env.pop().invokeMethod(env, operator, eval(tl, env))));
		commandMap.put("(", (tl, env) -> {
			proceed(tl, env);
			tl.accept(")");
		});
		
		commandMap.put("$", (tl, env) -> {
			String name = tl.nextString();
			if (tl.isNext("=")) {
				tl.accept("=");
				env.put(name, eval(tl, env));
			}
			env.push(env.get(name));
		});
		
		commandMap.put("p", (tl, env) -> System.out.println(env.peek()));
	}
	
	@Override
	public void proceed(TokenList tl, FortobEnvironment env) {
		env.putLocal("?", (lenv, name, args) -> {
			if (args.length == 0) {
				switch (name) {
				case "nextString":
					return new FortobString(tl.nextString());
				case "nextNumber":
					return new FortobNumber(Double.parseDouble(tl.nextString()));
				case "seekString":
					return new FortobString(tl.seekString());
				case "seekNumber":
					return new FortobNumber(Double.parseDouble(tl.seekString()));
				}
			}
			
			if (args.length == 1) {
				switch (name) {
				case "accept":
					tl.accept(args[0].toString());
					return new FortobBoolean(true);
				}
			}
			
			throw new RuntimeException("Unknown method `" + name + "(" + Arrays.asList(args).stream()
					.map(a -> a.getClass().getName()).collect(Collectors.joining(", ")) + ")'");
		});
		
		if (tl.isNext("\\")) {
			tl.accept("\\");
			return;
		}
		
		while (tl.hasNext() ? (commandMap.containsKey(tl.seekString())) || tl.seekString().startsWith("\"") : false) {
			proceedOnce(tl, env);
			if (tl.isNext("\\")) {
				tl.accept("\\");
				break;
			}
		}
	}
	
	private void proceedOnce(TokenList tl, FortobEnvironment env) {
		String command;
		int line;
		if (!tl.seekString().startsWith("\"")) {
			Token next = tl.next();
			
			command = next.getToken();
			line = next.getLine();
		}
		else {
			command = "\"";
			line = tl.seek().getLine();
		}
		
		if (DEBUG) System.err.println(i() + command + "@" + line);
		
		ilevel++;
			
		commandMap.get(command).proceed(tl, env);
				
		ilevel--;
			
		if (DEBUG) System.err.println(i() + env);
	}
	
	/**
	 * Same as proceed, but returns the top value of the stack
	 * 
	 * @param tl The token list
	 * @param env The environment
	 * @return the top value of the stack
	 */
	public FortobValue eval(TokenList tl, FortobEnvironment env) {
		proceed(tl, env);
		if (DEBUG) System.err.println(i() + "= " + env.peek());
		return env.pop();
	}

}
