package se.anatom.ejbca.ca.sign.junit;

import se.anatom.ejbca.ca.sign.*;
import se.anatom.ejbca.util.*;
import java.util.*;
import java.math.BigInteger;

import org.apache.log4j.*;
import junit.framework.*;


/** Tests generation of serial numbers.
 *
 * @version $Id: TestSernoGenerator.java,v 1.3 2002-09-05 09:14:33 anatom Exp $
 */
public class TestSernoGenerator extends TestCase {


    static Category cat = Category.getInstance( TestSernoGenerator.class.getName() );

    public TestSernoGenerator(String name) {
        super(name);
    }
    protected void setUp() throws Exception {
    }
    protected void tearDown() throws Exception {
    }

    /* Generates many serial numbers...
    */
    public void test01GenerateSernos() throws Exception {
        cat.debug(">test01GenerateSernos()");
        ISernoGenerator gen = SernoGenerator.instance();
        HashMap map = new HashMap(300000);
        String hex=null;
        for (int j=0;j<300;j++) {
            for (int i=0;i<1000;i++) {
                BigInteger bi = gen.getSerno();
                //hex = Hex.encode(serno);
                hex = bi.toString();
                if (map.put(hex,hex) != null) {
                    System.out.println("Duplicate serno produced: "+hex);
                }
            }
            System.out.println((j+1)*1000+" sernos produced: "+hex);
            long seed = Math.abs((new Date().getTime()) + this.hashCode());
            gen.setSeed(seed);
            System.out.println("Reseeding: "+seed);
        }
        System.out.println("Map now contains "+map.size()+" serial numbers.");
        cat.debug("<test01GenerateSernos()");
    }
}

