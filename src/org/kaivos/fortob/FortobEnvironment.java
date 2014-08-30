package org.kaivos.fortob;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Represents the environment
 * 
 * @author Iikka Hauhio
 *
 */
public class FortobEnvironment {

	private FortobEnvironment superEnv;
	
	private Stack<FortobValue> stack = new Stack<>();
	private Map<String, FortobValue> map = new HashMap<>();

	/**
	 * Initializes an empty environment
	 */
	public FortobEnvironment() {
		superEnv = null;
	}
	
	/**
	 * Initializes a new subenvironment
	 * 
	 * @param parent The parent environment
	 */
	public FortobEnvironment(FortobEnvironment parent) {
		this.superEnv = parent;
	}
	
	/**
	 * @return The parent environment
	 */
	public FortobEnvironment parent() {
		return superEnv;
	}
	
	/**
	 * @return A new subenvironment
	 */
	public FortobEnvironment sub() {
		return new FortobEnvironment(this);
	}
	
	/**
	 * Pushes a new value to the stack
	 * 
	 * @param val The value
	 * @return self
	 */
	public FortobEnvironment push(FortobValue val) {
		stack.push(val);
		return this;
	}

	/**
	 * Returns the top value of the stack
	 * 
	 * @return The value
	 */
	public FortobValue peek() {
		return stack.peek();
	}
	
	/**
	 * Pops and returns a value from the stack
	 * 
	 * @return The value
	 */
	public FortobValue pop() {
		return stack.pop();
	}
	
	/**
	 * Pops and discards the top value of the stack
	 * 
	 * @return self
	 */
	public FortobEnvironment drop() {
		stack.pop();
		return this;
	}
	
	/**
	 * Duplicates the top value of the stack
	 * @return self
	 */
	public FortobEnvironment dup() {
		stack.push(stack.peek());
		return this;
	}
	
	/**
	 * Puts a value to the map or, if already in the map of the parent environment, the map of the parent environment
	 * 
	 * @param name The name
	 * @param val The value
	 * @return self
	 */
	public FortobEnvironment put(String name, FortobValue val) {
		if (superEnv != null && superEnv.contains(name))
			superEnv.put(name, val);
		else
			map.put(name, val);
		
		return this;
	}
	
	/**
	 * Puts a value to the map
	 * 
	 * @param name The name
	 * @param val The value
	 * @return self
	 */
	public FortobEnvironment putLocal(String name, FortobValue val) {
		map.put(name, val);
		
		return this;
	}
	
	/**
	 * Returns a value from the map
	 * 
	 * @param name The name
	 * @return The value
	 */
	public FortobValue get(String name) {
		if (!map.containsKey(name)) {
			if (superEnv == null)
				throw new IndexOutOfBoundsException(name);
			else
				return superEnv.get(name);
		}
		return map.get(name);
	}
	
	/**
	 * Returns true if the environment contains the given index
	 * 
	 * @param name The name of the value
	 * @return true or false
	 */
	public boolean contains(String name) {
		return map.containsKey(name) || (superEnv != null && superEnv.contains(name));
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
	
}
