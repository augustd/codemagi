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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.*;
import java.util.Collection;

/**
 * ImportKey.java
 *
 * <p>
 * This class imports a key and a certificate into a keystore
 * (<code>$home/keystore.ImportKey</code>). If the keystore is already present,
 * it is simply deleted. Both the key and the certificate file must be in
 * <code>DER</code>-format. The key must be encoded with
 * <code>PKCS#8</code>-format. The certificate must be encoded in
 * <code>X.509</code>-format.</p>
 *
 * <p>
 * Key format:</p>
 * <p>
 * <code>openssl pkcs8 -topk8 -nocrypt -in YOUR.KEY -out YOUR.KEY.der
 * -outform der</code></p>
 * <p>
 * Format of the certificate:</p>
 * <p>
 * <code>openssl x509 -in YOUR.CERT -out YOUR.CERT.der -outform
 * der</code></p>
 * <p>
 * Import key and certificate:</p>
 * <p>
 * <code>java comu.ImportKey YOUR.KEY.der YOUR.CERT.der</code></p><br />
 *
 * <p>
 * NOTE: If you have an intermediate certificate, convert it to DER format, and
 * concatenate that to the end of your cert: 
 * <code>cat YOUR.CERT.der INTERMEDIATE.CERT.der >ALL.CERT.der
 * </p>
 *
 * <p>
 * <em>Caution:</em> the old <code>keystore.ImportKey</code>-file is deleted and
 * replaced with a keystore only containing <code>YOUR.KEY</code> and
 * <code>YOUR.CERT</code>. The keystore and the key has no password; they can be
 * set by the <code>keytool -keypasswd</code>-command for setting the key
 * password, and the <code>keytool -storepasswd</code>-command to set the
 * keystore password.
 * <p>
 * The key and the certificate is stored under the alias you specify on the
 * command line
 *
 * Created: Fri Apr 13 18:15:07 2001 Updated: Tue Feb 17 05:07:31 2009
 *
 * @author Joachim Karrer, Jens Carlberg (original authors)
 * @author August Detlefsen (for CodeMagi, Inc.)
 * @version 1.1
 *
 */
public class ImportKey {

    /**
     * <p>
     * Creates an InputStream from a file, and fills it with the complete file.
     * Thus, available() on the returned InputStream will return the full number
     * of bytes the file contains</p>
     *
     * @param fname The filename
     * @return The filled InputStream
     * @exception IOException, if the Streams couldn't be created.
     *
     */
    private static InputStream fullStream(String fname) throws IOException {
        FileInputStream fis = new FileInputStream(fname);
        DataInputStream dis = new DataInputStream(fis);
        byte[] bytes = new byte[dis.available()];
        dis.readFully(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }

    /**
     * <p>
     * Takes two file names for a key and the certificate for the key, and
     * imports those into a keystore. Optionally it takes an alias for the key.
     * <p>
     * The first argument is the filename for the key. The key should be in
     * PKCS8-format.
     * <p>
     * The second argument is the filename for the certificate for the key.
     * <p>
     * If a third argument is given it is used as the alias. If missing, the key
     * is imported with the alias importkey
     * <p>
     * The name of the keystore file can be controlled by setting the keystore
     * property (java -Dkeystore=mykeystore). If no name is given, the file is
     * named <code>keystore.ImportKey</code> and placed in your home directory.
     *
     * @param args [0] Name of the key file, [1] Name of the certificate file
     * [2] Alias for the key.
     *
     */
    public static void main(String args[]) {

        // change this if you want another password by default
        String keypass = "importkey";

        // change this if you want another alias by default
        String defaultalias = "importkey";

        // change this if you want another keystorefile by default
        String keystorename = System.getProperty("keystore");

        if (keystorename == null) {
            keystorename = System.getProperty("user.home")
                    + System.getProperty("file.separator")
                    + "keystore.ImportKey"; // especially this ;-)
        }

        // parsing command line input
        String keyfile = "";
        String certfile = "";
        if (args.length < 2 || args.length > 4) {
            System.out.println("Usage: java comu.ImportKey keyfile certfile [alias [keystore]]");
            System.exit(0);
        } else {
            keyfile = args[0];
            certfile = args[1];
            if (args.length > 2) {
                defaultalias = args[2];
            }
            if (args.length > 3) {
                keystorename = args[3];
            }
        }

        // Get the password for the keystore.
        try {
            System.out.println("Keystore password:  ");
            keypass = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        } catch (IOException ioe) {
            System.out.println("Unable to read password.");
            System.exit(1);
        }

        try {
            // initializing and clearing keystore 
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            ks.load(null, keypass.toCharArray());
            System.out.println("Using keystore-file : " + keystorename);
            ks.store(new FileOutputStream(keystorename),
                    keypass.toCharArray());
            ks.load(new FileInputStream(keystorename),
                    keypass.toCharArray());

            // loading Key
            InputStream fl = fullStream(keyfile);
            byte[] key = new byte[fl.available()];
            KeyFactory kf = KeyFactory.getInstance("RSA");
            fl.read(key, 0, fl.available());
            fl.close();
            PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
            PrivateKey ff = kf.generatePrivate(keysp);

            //create cert factory
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // loading CertificateChain
            InputStream certstream = fullStream(certfile);
            Collection c = cf.generateCertificates(certstream);
            Certificate[] certs = new Certificate[c.toArray().length];

            if (c.size() == 1) {
                certstream = fullStream(certfile);
                System.out.println("One certificate, no chain.");
                Certificate cert = cf.generateCertificate(certstream);
                certs[0] = cert;

            } else {
                System.out.println("Certificate chain length: " + c.size());
                certs = (Certificate[]) c.toArray(new Certificate[0]);
            }

            // storing keystore
            ks.setKeyEntry(defaultalias, ff,
                    keypass.toCharArray(),
                    certs);

            System.out.println("Key and certificate stored.");
            System.out.println("Alias:" + defaultalias + "  Password:" + keypass);
            ks.store(new FileOutputStream(keystorename),
                    keypass.toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
