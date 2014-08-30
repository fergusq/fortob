/**
 * 
 */
package org.kaivos.fortob.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * NonNullByDefault
 * 
 * @author Iikka Hauhio
 *
 */
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.PACKAGE,ElementType.TYPE,ElementType.METHOD,ElementType.CONSTRUCTOR,ElementType.FIELD,ElementType.LOCAL_VARIABLE})
public @interface NonNullByDefault {

}
