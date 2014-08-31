package org.kaivos.fortob.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.value.FortobValue;

/**
 * Represents the environment
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public class FortobEnvironment {

	private Stack<@NonNull FortobValue> stack = new Stack<>();
	protected Map<String, @NonNull FortobValue> map = new HashMap<>();

	/**
	 * Initializes an empty environment
	 */
	public FortobEnvironment() {}
	
	/**
	 * @return The parent environment
	 */
	public Optional<FortobEnvironment> parent() {
		return Optional.empty();
	}
	
	/**
	 * @return A new subenvironment
	 */
	public @NonNull FortobEnvironment sub() {
		return new FortobSubenvironment(this);
	}
	
	/**
	 * Pushes a new value to the stack
	 * 
	 * @param val The value
	 * @return self
	 */
	public @NonNull FortobEnvironment push(@NonNull FortobValue val) {
		stack.push(val);
		return this;
	}

	/**
	 * Returns the top value of the stack
	 * 
	 * @return The value
	 */
	public @NonNull FortobValue peek() {
		return stack.peek();
	}
	
	/**
	 * Pops and returns a value from the stack
	 * 
	 * @return The value
	 */
	public @NonNull FortobValue pop() {
		return stack.pop();
	}
	
	/**
	 * Pops and discards the top value of the stack
	 * 
	 * @return self
	 */
	public @NonNull FortobEnvironment drop() {
		stack.pop();
		return this;
	}
	
	/**
	 * Duplicates the top value of the stack
	 * @return self
	 */
	public @NonNull FortobEnvironment dup() {
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
	public @NonNull FortobEnvironment put(String name, @NonNull FortobValue val) {
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
	public @NonNull FortobEnvironment putLocal(String name, @NonNull FortobValue val) {
		map.put(name, val);
		
		return this;
	}
	
	/**
	 * Returns a value from the map
	 * 
	 * @param name The name
	 * @return The value
	 */
	public @NonNull FortobValue get(String name) {
		if (!map.containsKey(name)) {
			throw new IndexOutOfBoundsException(name);
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
		return map.containsKey(name);
	}
	
	@Override
	public String toString() {
		return stack.toString();
	}
	
}
