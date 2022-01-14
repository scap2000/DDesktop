package org.pentaho.reportdesigner.crm.report.tests.numbers;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * User: Martin
 * Date: 23.01.2007
 * Time: 17:27:01
 */
@SuppressWarnings({"ALL"})
public class FunnySystem
{
    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");


    public static void main(String[] args)
    {
        int MAX_TEST = 1000000;
        BitSet bitSet = new BitSet(MAX_TEST);

        for (int i = 2; i < MAX_TEST; i++)
        {
            if (test(bitSet, new BigInteger("" + i)))
            {
                //System.out.println("ja:   " + i);
            }
            else
            {
                System.out.println("nein: " + i);
            }

            if (i % 100000 == 0)
            {
                System.out.println("i = " + i);
            }
        }
    }


    private static boolean test(BitSet bitSet, BigInteger number)
    {
        BigInteger n = number;
        while (true)
        {
            if (n.equals(BigInteger.ONE)/* || bitSet.get(n.longValue())*/)
            {
                //bitSet.set(number.intValue(), true);
                return true;
            }
            else if (n.mod(TWO).equals(BigInteger.ZERO))
            {
                //n = n / 2;
                n = n.divide(TWO);
            }
            else
            {
                //n = n * 3 + 1;
                n = n.multiply(THREE).add(BigInteger.ONE);
            }
        }
    }
}
