import javax.crypto.KeyAgreement;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

// Sourced from: http://www.academicpub.org/DownLoadPaper.aspx?PaperID=14496

public class EllipticCurveCryptography  {
    public KeyPair getKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", "SunEC");
        // alternatives include: secp192r1, secp256k1, secp384r1, etc.
        ECGenParameterSpec ecsp = new ECGenParameterSpec("secp192r1");
        kpg.initialize(ecsp);
        return kpg.genKeyPair();
    }

    public void testKeyPairGen() throws Exception {
        System.out.println("testKeyPairGen...\n");
        KeyPair kp = getKeyPair();
        PrivateKey privKey = kp.getPrivate();
        PublicKey pubKey = kp.getPublic();
        System.out.println("Private Key: " + privKey);
        System.out.println("Public Key: " + pubKey);
    }

    /**
     * Test key agreement using ECDH.
     * Src: https://en.wikipedia.org/wiki/Elliptic-curve_Diffie%E2%80%93Hellman
     * Elliptic-curve Diffie–Hellman (ECDH) is an anonymous key agreement protocol that allows two parties,
     * each having an elliptic-curve public–private key pair, to establish a shared secret over an insecure channel.
     * This shared secret may be directly used as a key, or to derive another key. The key, or the derived key,
     * can then be used to encrypt subsequent communications using a symmetric-key cipher.
     * It is a variant of the Diffie–Hellman protocol using elliptic-curve cryptography.
     * */
    public void testKeyAgreement() throws Exception {
        // Generate U
        KeyPair kpU = getKeyPair();
        PrivateKey privKeyU = kpU.getPrivate();
        PublicKey pubKeyU = kpU.getPublic();
        // Generate V
        KeyPair kpV = getKeyPair();
        PrivateKey privKeyV = kpV.getPrivate();
        PublicKey pubKeyV = kpV.getPublic();
        // Test U agreement
        KeyAgreement ecdhU = KeyAgreement.getInstance("ECDH");
        ecdhU.init(privKeyU);
        ecdhU.doPhase(pubKeyV, true);
        // Test V agreement
        KeyAgreement ecdhV = KeyAgreement.getInstance("ECDH");
        ecdhV.init(privKeyV);
        ecdhV.doPhase(pubKeyU, true);

        System.out.println("Secret computed by U: 0x" + (new BigInteger(1, ecdhU.generateSecret())
                .toString(16)).toUpperCase());
        System.out.println("Secret computed by V: 0x" + (new BigInteger(1, ecdhV.generateSecret())
                .toString(16)).toUpperCase());
    }

    /**
     * Test signing of a message using Elliptic Curve Digital Signatures
     * src: https://en.wikipedia.org/wiki/Elliptic_Curve_Digital_Signature_Algorithm
     * Elliptic Curve Digital Signature Algorithm (ECDSA) offers a variant of the Digital Signature Algorithm (DSA)
     * which uses elliptic curve cryptography.
     */
    public void testDigitalSignature() throws Exception {
        KeyPair kp = getKeyPair();
        PrivateKey privateKey = kp.getPrivate();
        PublicKey publicKey = kp.getPublic();

        Signature ecdsa = Signature.getInstance("SHA1withECDSA", "SunEC");
        ecdsa.initSign(privateKey);

        String text = "The quick brown fox jumps over the lazy dog.";
        System.out.println("Text: " + text);
        byte[] textBytes = text.getBytes("UTF-8");
        // set text as the message that must be signed
        ecdsa.update(textBytes);
        // sign the message
        byte[] signatureBytes = ecdsa.sign();
        System.out.println("Signature: 0x" + (new BigInteger(1, signatureBytes).toString(16)).toUpperCase());

        Signature signature = Signature.getInstance("SHA1withECDSA", "SunEC");
        signature.initVerify(publicKey);
        signature.update(textBytes);
        boolean result = signature.verify(signatureBytes);
        System.out.println("Valid: " + result);

        System.out.println("Testing reuse of the signing ECDSA");
        textBytes = "some other text as bytes".getBytes("UTF-8");
        ecdsa.update(textBytes);
        signatureBytes = ecdsa.sign();
        signature.update(textBytes);
        result = signature.verify(signatureBytes);
        System.out.println("Valid: " + result);
    }

    public static void main(String[] args) throws Exception{
        EllipticCurveCryptography ecc = new EllipticCurveCryptography();
        ecc.testKeyPairGen();
        ecc.testKeyAgreement();
        ecc.testDigitalSignature();
    }
}
