package Consensus;

import Hashes.SHA256Gen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Timer;

public class ProofOfWork {

    public static void main(String[] args){
        ProofOfWork pow = new ProofOfWork();
        // Sample merkleRoot
        String merkleRoot = "5e049f4030e0ab2debb92378f53c0a6e09548aea083f3ab25e1d94ea1155e29d";

        for (int difficulty = 1; difficulty <= 100; difficulty++){
            System.out.println("Setting Difficulty: " + difficulty);
            String target = pow.getTarget(difficulty);
            Date startTime = new Date();
            System.out.println("Started at: " + startTime.toString());
            System.out.println("Target: " + target);
            NonceHash nh = pow.provideProofOfWork(merkleRoot, target);
            Date endTime = new Date();
            System.out.println("Hash  : " + nh.hash);
            System.out.println("Nonce : " + nh.nonce);
            System.out.println("Ended at: " + endTime.toString());
            System.out.println("Duration in ms: " + (endTime.getTime() - startTime.getTime()));
            System.out.println("------------");
        }
    }

    public String getTarget(int difficulty){
        // has a higher target than bitcoin which is "00000000FFFF..."
        String lowestTarget = "0000FFFF00000000000000000000000000000000000000000000000000000000";
        BigInteger i = new BigInteger(lowestTarget, 16).divide(BigInteger.valueOf(difficulty));
        String h = i.toString(16);
        int zerosToPad = lowestTarget.length() - h.length();
        for (int c = 0; c < zerosToPad; c++){
            h = "0" + h;
        }
        return h.toUpperCase();
    }

    public NonceHash provideProofOfWork(String text, String target){
        for(Long l = 0l; l < Long.MAX_VALUE; l++){
            String hashResult = SHA256Gen.hash((text+l).getBytes()).toUpperCase();
            if (target.compareTo(hashResult) > 0) {
                return new NonceHash(l, hashResult);
            }
        }

        return new NonceHash(-1l, "N/A");
    }
}

class NonceHash{
    public Long nonce;
    public String hash;
    public NonceHash(Long n, String h){
        nonce = n;
        hash = h;
    }
}