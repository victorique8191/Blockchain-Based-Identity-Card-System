package com.company;
import java.math.BigInteger;


public class RSA {
    public static void main(String[] args) {
        // Prime numbers
        BigInteger p = new BigInteger("389651984374348681198937829437632876251983672816323");
        BigInteger q = new BigInteger("672838012783469248520967128763116286378291649376839");

        // Public key
        BigInteger n = p.multiply(q);
        BigInteger e = new BigInteger("723732698112534512672761745123638923636112736572367");

        // Let's compute private key d by given p, q, and e
        BigInteger r=p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		BigInteger d = e.modInverse(r);

        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("n = " + n);
        System.out.println("e = " + e);
        System.out.println("d = " + d);

        String plainText = "RSA INVOLVES A PUBLIC KEY AND A PRIVATE KEY THE PUBLIC KEY CAN BE KNOWN BY EVERYONE AND IS USED FOR ENCRYPTION MESSAGES MESSAGES ENCRYPTED WITH THE PUBLIC KEY CAN BE ONLY BE DECRYPTED IN A REASONABLE AMOUNT OF TIME USING THE PRIVATE KEY THE KEYS FOR THE RSA ALGORITHM ARE GENERATED THE FOLLOWING WAY";
        System.out.println("Plain text = " + plainText);

        // Encode the plain text and print out the result
        int[] encodedNumbers = getEncodedNumbers(plainText);
        System.out.print("M = ");
        printEncodedNumbers(encodedNumbers);
        System.out.println();

        // Encrypt using the public key
        System.out.print("C = ");
        String c = encryptEncodedNumbers(encodedNumbers, e, n);
        System.out.println(c);
        System.out.println("After coding:");
        System.out.println(Tobig(c,d,n));

        // HOMEWORK: Parse the c line by line using java.util.Scanner class to decrypt the result back to encodedNumbers
        // using the private key d
        /*
        BigInteger m = c.modPow(d, n);
        System.out.println("m = " + m);
        */

        // Example to generate a probable prime
        /*
        BigInteger newP = BigInteger.probablePrime(200, new SecureRandom());
        System.out.println("P = " + newP);
        */
    }

    // Print the encoded numbers using a pretty-print format
    private static void printEncodedNumbers(int[] encodedNumbers) {
        // Print the encoded numbers for each block of two numbers
        for (int i = 0; i < encodedNumbers.length; i+= 2) {
            // If the first number is 0, don't print anything, otherwise,
            // print the number
            if (encodedNumbers[i] != 0) {
                System.out.print(encodedNumbers[i]);
            }
            // Print the 2nd number of the block using two digit format with leading zero
            // For example:
            // 0 will print "00"
            // 1 will print "01"
            // 8 will print "08"
            // 15 will print "15"
            System.out.print(String.format("%02d", encodedNumbers[i+1]));
            System.out.print(" ");
        }
    }

    // Encrypt the encoded numbers in two-number block using the specified e & n
    private static String encryptEncodedNumbers(int[] encodedNumbers, BigInteger e, BigInteger n) {
        String result = "";
        //Encrypt the encoded text using 4-character block
        for (int i = 0; i < encodedNumbers.length; i+= 2) {
            // Convert the two-number block to a string of 4 digits
            // For example,
            // [17, 18] will be converted to "1718"
            // [0, 26] will be converted to "26"
            // [8, 13] will be converted to "813"
            String blockString = String.format("%d", encodedNumbers[i] * 100 + encodedNumbers[i+1]);
            // Encrypt the block
            BigInteger block = new BigInteger(blockString);
            BigInteger c = block.modPow(e, n);

            // Keep adding the encrypted big integer into the result string
            result += c;
            result += "\n";
        }
        return result;
    }

    private static int[] getEncodedNumbers(String plainText) {
        if (plainText.length() % 2 == 1) {
            // Add padding space if the length of the string is an odd number
            plainText += " ";
        }

        int[] encodedNumbers = new int[plainText.length()];
        for (int i = 0; i < plainText.length(); i+= 2) {
            encodedNumbers[i] = encodeChar(plainText.charAt(i));
            encodedNumbers[i+1] = encodeChar(plainText.charAt(i+1));
        }
        return encodedNumbers;
    }

    private static int encodeChar(char ch) throws IllegalArgumentException
    {
        int encodedNumber = 0;
        if (ch >= 'A' && ch <= 'Z') {
            encodedNumber = (int)ch - (int)'A';
        } else if (ch == ' ') {
            encodedNumber = 26;
        } else {
            throw new IllegalArgumentException("A to Z, or space is expected");
        }
        // Format the encoded number as zero-padded number
        return encodedNumber;
    }
    private static String Tobig(String C, BigInteger d, BigInteger n){
    	String[] c=C.split("\n");
    	String aftercoding="";
    	aftercoding=new BigInteger(c[0]+"").modPow(d,n)+"";
    	for(int i=1;i<c.length;i++){
    		aftercoding=aftercoding+" "+new BigInteger(c[i]+"").modPow(d,n);
    	}
    	return aftercoding;
    }
}
