package org.kaivos.fortob.value;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.environment.FortobEnvironment;

/**
 * Represents a value
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
public interface FortobValue {

	/**
	 * Invokes a method
	 * 
	 * @param env The environment
	 * @param name The name of the method
	 * @param args The arguments
	 * @return The return value of the method
	 */
	public FortobValue invokeMethod(FortobEnvironment env, String name,
			FortobValue... args);
	
	/**
	 * The type of the value
	 * 
	 * @return the type
	 */
	public default FortobType getType() {
		return FortobType.OTHER;
	}
	
	/**
	 * Returns a new exception for calling a nonexisting method
	 * 
	 * @param val The object
	 * @param name The name of the method
	 * @param args The arguments
	 * @return The exception
	 */
	static RuntimeException unknownMethod(FortobValue val, String name, FortobValue...args) {
		return new RuntimeException("Unknown method `" + val.getClass().getSimpleName() + "." + name + "(" + Arrays.asList(args).stream()
				.map(a -> a.getClass().getSimpleName()).collect(Collectors.joining(", ")) + ")'");
	}
	
}
