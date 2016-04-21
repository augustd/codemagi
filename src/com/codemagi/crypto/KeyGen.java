/* 
 *  Copyright 2012 CodeMagi, Inc.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.codemagi.crypto;

import java.io.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.apache.log4j.Logger;

public class KeyGen {

    static Logger log = Logger.getLogger("com.codemagi.crypto.KeyGen");

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage:");
            System.out.println("java KeyGen <algorithm> <key size> <output file>");
            return;
        }

        String algorithm = args[0];
        int keySize = Integer.parseInt(args[1]);
        String output = args[2];

        try {
            writeKey(keySize, output, algorithm);
            readKey(output, algorithm);

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return;
        }
    }

    public static void writeKey(int keySize, String output, String algorithm) throws Exception {

        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(keySize);
        System.out.println();
        System.out.println("KeyGenerator Object Info: ");
        System.out.println("Algorithm = " + kg.getAlgorithm());
        System.out.println("Provider = " + kg.getProvider());
        System.out.println("Key Size = " + keySize);
        System.out.println("toString = " + kg.toString());

        SecretKey ky = kg.generateKey();

        byte[] kb;
        try (FileOutputStream fos = new FileOutputStream(output)) {
            kb = ky.getEncoded();
            fos.write(kb);
        }
        System.out.println();
        System.out.println("SecretKey Object Info: ");
        System.out.println("Algorithm = " + ky.getAlgorithm());
        System.out.println("Saved File = " + output);
        System.out.println("Size = " + kb.length);
        System.out.println("Format = " + ky.getFormat());
        System.out.println("toString = " + ky.toString());
    }

    public static SecretKey readKey(String input, String algorithm)
            throws IOException, java.security.GeneralSecurityException {

        byte[] kb;
        try (FileInputStream fis = new FileInputStream(input)) {
            int kl = fis.available();
            kb = new byte[kl];
            fis.read(kb);
        }
        KeySpec ks = null;
        SecretKey ky = null;
        SecretKeyFactory kf = null;
        if (algorithm.equalsIgnoreCase("DES")) {
            ks = new DESKeySpec(kb);
            kf = SecretKeyFactory.getInstance("DES");
            ky = kf.generateSecret(ks);

        } else if (algorithm.equalsIgnoreCase("DESede")) {
            ks = new DESedeKeySpec(kb);
            kf = SecretKeyFactory.getInstance("DESede");
            ky = kf.generateSecret(ks);

        } else {
            ks = new SecretKeySpec(kb, algorithm);
            ky = new SecretKeySpec(kb, algorithm);

        }

        System.out.println();
        System.out.println("KeySpec Object Info: ");
        System.out.println("Saved File = " + input);
        System.out.println("Length = " + kb.length);
        System.out.println("toString = " + ks.toString());

        System.out.println();
        System.out.println("SecretKey Object Info: ");
        System.out.println("Algorithm = " + ky.getAlgorithm());
        System.out.println("toString = " + ky.toString());

        return ky;
    }

}
