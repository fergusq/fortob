/**
 * 
 */
package org.kaivos.fortob.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Nullable
 * 
 * @author Iikka Hauhio
 *
 */
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface Nullable {

}
