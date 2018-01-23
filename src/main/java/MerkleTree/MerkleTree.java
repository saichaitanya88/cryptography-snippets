package MerkleTree;

import Hashes.SHA256Gen;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

class MerkleNode {
    public String hash;
    public MerkleNode parent;
    public MerkleNode left;
    public MerkleNode right;
    public Transaction data;
}

class Transaction{
    String senderId;
    String receiverId;
    int amount;

    public Transaction(String senderId, String receiverId, int amount){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public byte[] transactionAsBytes(){
        return toString().getBytes();
    }

    public String toString(){
        return senderId + "->" + amount + "->" + receiverId;
    }
}

public class MerkleTree {
    public MerkleNode root;
    public ArrayList<MerkleNode> merkleNodes;
    public Transaction[] transactions;

    public MerkleTree() {
        root = new MerkleNode();
    }

    public void setTransactions(Transaction[] transactions) {
        this.transactions = transactions;
    }

    public void constructTree() {
        merkleNodes = new ArrayList<MerkleNode>();
        for (Transaction t : transactions) {
            MerkleNode node = new MerkleNode();
            node.data = t;
            node.hash = SHA256Gen.hash(t.transactionAsBytes());
            merkleNodes.add(node);
        }
        root = generateMerkleRoot(merkleNodes);
    }

    public MerkleNode generateMerkleRoot(ArrayList<MerkleNode> merkleNodes) {
        if (merkleNodes.size() == 1)
            return merkleNodes.get(0);
        ArrayList<MerkleNode> newNodeList = new ArrayList<MerkleNode>();
        for (int i = 0; i < merkleNodes.size() - 1; i += 2) {
            MerkleNode node = new MerkleNode();
            node.left = merkleNodes.get(i);
            node.right = merkleNodes.get(i+1);
            node.hash = doubleSHA(node.left.hash, node.right.hash);
            node.left.parent = node;
            node.right.parent = node;
            newNodeList.add(node);
        }

        // it's an odd hash, we need to add this item twice.
        if (merkleNodes.size() % 2 == 1) {
            MerkleNode lastNode = merkleNodes.get(merkleNodes.size() - 1);
            MerkleNode node = new MerkleNode();
            node.left = lastNode;
            node.right = lastNode;
            node.hash = doubleSHA(lastNode.hash, lastNode.hash);
            node.left.parent = node;
            node.right.parent = node; // redundant
            newNodeList.add(node);
        }

        return generateMerkleRoot(newNodeList);
    }

    public String doubleSHA(String hashA, String hashB) {
        byte[] reversedHashA = reverseHash(hashA);
        byte[] reversedHashB = reverseHash(hashB);
        byte[] combinedHashBytes = ArrayUtils.addAll(reversedHashA, reversedHashB);
        byte[] hash = SHA256Gen.hashBytes(SHA256Gen.hashBytes(combinedHashBytes));
        ArrayUtils.reverse(hash);
        String reversedHash = Hex.encodeHexString(hash);
        return reversedHash;
    }

    public byte[] reverseHash(String hash){
        try{
            byte[] hex = Hex.decodeHex(hash);
            ArrayUtils.reverse(hex);
            return hex;
        }
        catch(Exception ex){
            System.out.println("Error: " + ex.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public boolean verifyTransaction(Transaction transaction){
        int index = -1;
        String transactionHash = SHA256Gen.hash(transaction.transactionAsBytes());
        for(int i = 0; i < merkleNodes.size(); i++){
            if (merkleNodes.get(i).hash.equals(transactionHash)){
                index = i;
                break;
            }
        }
        if (index == -1) return false;
        MerkleNode start = merkleNodes.get(index);
        return verifyUpToRoot(root.hash, start);
    }

    public boolean verifyUpToRoot(String merkleRootHash, MerkleNode start){
        if (start.parent == null){
            return start.hash.equals(merkleRootHash);
        }

        String hashToVerify = doubleSHA(start.parent.left.hash, start.parent.right.hash);
        if (!hashToVerify.equals(start.parent.hash))
            return false;

        return verifyUpToRoot(merkleRootHash, start.parent);
    }

}