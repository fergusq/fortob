package org.kaivos.fortob.environment;

import java.util.Optional;

import org.kaivos.fortob.annotation.NonNull;
import org.kaivos.fortob.annotation.NonNullByDefault;
import org.kaivos.fortob.value.FortobValue;

/**
 * Subenvironment is like environment but has a parent environment
 * 
 * @author Iikka Hauhio
 *
 */
@NonNullByDefault
class FortobSubenvironment extends FortobEnvironment {
	
	private @NonNull FortobEnvironment parent;
	
	/**
	 * Initializes a new subenvironment
	 * 
	 * @param parent The parent environment
	 */
	public FortobSubenvironment(@NonNull FortobEnvironment parent) {
		this.parent = parent;
	}
	
	@Override
	public Optional<FortobEnvironment> parent() {
		return Optional.of(parent);
	}
	
	/**
	 * Puts a value to the map or, if already in the map of the parent environment, the map of the parent environment
	 * 
	 * @param name The name
	 * @param val The value
	 * @return self
	 */
	@Override
	public FortobEnvironment put(String name, @NonNull FortobValue val) {
		if (parent.contains(name))
			parent.put(name, val);
		else
			map.put(name, val);
		
		return this;
	}
	
	/**
	 * Returns a value from the map
	 * 
	 * @param name The name
	 * @return The value
	 */
	@Override
	public @NonNull FortobValue get(String name) {
		if (!map.containsKey(name)) {
			return parent.get(name);
		}
		return map.get(name);
	}
	
	/**
	 * Returns true if the environment contains the given index
	 * 
	 * @param name The name of the value
	 * @return true or false
	 */
	@Override
	public boolean contains(String name) {
		return map.containsKey(name) || parent.contains(name);
	}
}
