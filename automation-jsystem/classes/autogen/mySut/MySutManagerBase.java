package autogen.mySut;
import com.ability.ease.auto.systemobjects.MySut;
import junit.framework.SystemTestCase;
/**
 * Auto generate management object.
 * Managed object class: com.ability.ease.auto.systemobjects.MySut
 * This file <b>shouldn't</b> be changed, to overwrite methods behavier
 * change: MySutManager.java
 * Object javadoc:
 * Represents the SUT object
 */
public abstract class MySutManagerBase extends SystemTestCase{
	protected MySut mySut = null;
	public void setUp() throws Exception {
		mySut = (MySut)system.getSystemObject("mySut");
	}
}
