package simple.chain;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class SimpleChainTest {


	@Test
	public void testBlockchain() {

		SimpleBlockchain<Transaction> chain1 = new SimpleBlockchain<Transaction>();
	
		chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C"));

		SimpleBlockchain<Transaction> chain2 = chain1.Clone();

		chain1.add(new Transaction("D"));

		System.out.println(String.format("Chain 1 Hash: %s", chain1.getHead().getHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.getHead().getHash()));
		System.out.println(
		String.format("Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

		chain2.add(new Transaction("D"));
		
		System.out.println(String.format("Chain 1 Hash: %s", chain1.getHead().getHash()));
		System.out.println(String.format("Chain 2 Hash: %s", chain2.getHead().getHash()));
		System.out.println(
				String.format("Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

		assertTrue(chain1.blockChainHash().equals(chain2.blockChainHash()));
		
		System.out.println("Current Chain Head Transactions: ");
		for (Block block : chain1.chain) {
			System.out.println(block.getTransactions());
		}

		// Block Merkle root should equal root hash in Merkle Tree computed from block transactions
		Block headBlock = chain1.getHead();
		List<Transaction> merkleTree = headBlock.merkleTree();
		assertTrue(headBlock.getMerkleRoot().equals(merkleTree.get( merkleTree.size() - 1)));
	
	}
	
	@Test
	public void merkleTreeTest() {
		
		// create chain, add transaction
		
		SimpleBlockchain<Transaction> chain1= new SimpleBlockchain<Transaction>();

		chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C")).add(new Transaction("D"));
	
		// get a block in chain
		Block<Transaction> block = chain1.getHead(); 
	   
		System.out.println("Merkle Hash tree :"+block.merkleTree());
		
		// get a transaction from block 
		Transaction tx = block.getTransactions().get(0);
		
		// see if hash is valid... using merkle Tree...
		block.isTransactionValid(tx);
		assertTrue( block.isTransactionValid(tx) );
		
		// mutate the transaction data 
		tx.setValue("Z");
		
		assertFalse(block.isTransactionValid(tx) );
				
	}
	
	@Test
	public void blockMinerTest() {
		
		// create 30 transactions, that should result in 3 blocks in the chain.
		SimpleBlockchain<Transaction> chain= new SimpleBlockchain<Transaction>();
	
		// Respresents a proof of work miner
		// Creates 
		Miner miner = new Miner(chain);
				
		// This represents transactions being created by a network 
	    for (int i = 0; i < 30; i++) {    	
	    	miner.mine(new Transaction(""+i));
	    }	
	    
		System.out.println("Number of Blocks Mined = "+chain.getChain().size());
		assertTrue( chain.getChain().size() == 3);
				
	}


}
