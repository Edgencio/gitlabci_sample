package mz.co.technosupport.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Americo Chaquisse
 */
public class RandomHolder {
    static final Random random = new SecureRandom();
    public static String randomKey(int length) {
        return String.format("%"+length+"s", new BigInteger(length*5/*base 32,2^5*/, random)
                .toString(32)).replace('\u0020', '0');
    }

}
