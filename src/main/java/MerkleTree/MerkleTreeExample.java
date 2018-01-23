package MerkleTree;

import java.util.Random;
import java.util.UUID;

public class MerkleTreeExample {
    public static void main(String[] args){
        for(int i = 0; i < 10; i++){
            MerkleTree mt = new MerkleTree();
            int sampleSize = (int) (Math.random() * 150) + 1;
            System.out.println("testing: " + sampleSize + " transactions" );
            mt.setTransactions(getSampleTransactions(sampleSize));

            mt.constructTree();
            System.out.println("MerkleRoot: " + mt.root.hash);

            boolean verifyResult = mt.verifyTransaction(mt.transactions[1]);
            System.out.println("Verify if the first transaction exists in the tree and is valid: " + verifyResult);

            mt.transactions[1].amount = mt.transactions[0].amount + 5000;
            verifyResult = mt.verifyTransaction(mt.transactions[1]);
            System.out.println("Verify if the tampered transaction exists in the tree and is valid: " + verifyResult);
        }
    }

    public static Transaction[] getSampleTransactions(int sampleSize){
        Transaction[] transactions = new Transaction[sampleSize];
        Random r = new Random();
        for(int i = 0; i < sampleSize; i++){
            transactions[i] = new Transaction(UUID.randomUUID().toString(), UUID.randomUUID().toString(), r.nextInt(1500)+1);
        }
        return transactions;
    }
}
