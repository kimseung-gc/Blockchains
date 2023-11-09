import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

/**
 * An Implementation for block chain. Some more methods added for convenience.
 * 
 * @author Seunghyeon (Hyeon) Kim
 * @author Che Glenn
 */

public class BlockChain implements BlockChainInterface{
  /**
   * DESIGN DECISIONS:
   * We decided to not include the current node as for loop iterations were the only ones
   * required, and we do not need any arbitrary pointers to make the code more efficient
   */
  // +--------+------------------------------------------------------------
  // | Fields |
  // +--------+
  /* Front node */
  Node<Block> front;
  /* Last node */
  Node<Block> last;

  // +--------------+------------------------------------------------------
  // | Constructors |
  // +--------------+
  /**
   * Creates a block chain with front node and last node.
   * @param initial
   */
  public BlockChain(int initial) throws NoSuchAlgorithmException{
    this.front = new Node<Block>(new Block(0, initial, null), null);
    this.last = this.front;
  } // BlockChain (int)

  // +---------+-----------------------------------------------------------
  // | Methods |
  // +---------+
  /**
   * Automatically mines a block and returns the mined block.
   * @return Block mined
   */
  public Block mine(int amount) throws NoSuchAlgorithmException{
    /* Use the constructor from Block to mine a block */
    return new Block(this.getSize(), amount, this.last.value.getHash());
  } // mine (int)

  /**
   * @return the size of the blockChain
   */
  public int getSize(){
    /* last index - first index (0 in this case) + 1 = size */
    return (this.last.value.getNum()-this.front.value.getNum())+1;
  } // getSize()

  /**
   * Adds a block to the last.
   * @return nothing
   * @throws IllegalArgumentException
   */
  public void append(Block blk) throws IllegalArgumentException{
    /* When given block is invalid, throw error. */
    if(!blk.getHash().isValid() || blk.getPrevHash() == null){
      throw new IllegalArgumentException();
    } // if
    /* Otherwise, add the new block to the next of the last node */
    this.last.next = new Node<Block>(blk, null);
    /* Then cast the last node to the next one */
    this.last = this.last.next;
  } // append (Block)

  /**
   * removes the last node
   * @return whether the last node is successfully removed
   */
  public boolean removeLast() {
    /* When the size is 1, there are no more blocks to remove, so return false
     * without any modification.
     */
    if(this.getSize() == 1){
      return false;
    } // if
    Node<Block> temp = this.front;
    /* Iterate through the nodes until it reaches the node right before this.last, and
     * modify it such that the last node is removed. As it is the last node, 
     * this.last.value.next does not have to be modified (null anyways).
     */
    for(temp = this.front; temp.next != null; temp = temp.next){
      if(temp.next.equals(this.last)){
        temp.next = null;
        this.last = temp;
        return true;
      } // if
    } // for
    return false;
  } // removeLast ()

  /**
   * @return the last node's hash
   */
  public Hash getHash(){
    return this.last.value.getHash();
  } // getHash ()
  
  /**
   * @return Whether the blockChain is valid (i.e. each of the blocks are 
   * valid and the amount is never negative)
   */
  public boolean isValidBlockChain(){
    long alexisBalance = this.front.value.getAmount();
    long blakeBalance = 0;
    /* Iterate until the end of the blocks */
    for(Node<Block> temp = this.front.next; temp != null; temp = temp.next){
      /* If any hash is not valid, return false */
      if(!temp.value.getHash().isValid()){
        return false;
      } // if
      /* Add the amounts to each of their name */
      alexisBalance += temp.value.getAmount();
      blakeBalance -= temp.value.getAmount();
      /* If invalid any time, return false */
      if(alexisBalance < 0 || blakeBalance < 0){
        return false;
      } // if
    } // for
    /* When valid at all circumstances, return true */
    return true;
  } // isValidBlockChain ()

  /**
   * prints Alexis' balance and Blake's balance through the given PrintWriter.
   */
  public void printBalances(PrintWriter pen){
    long alexisBalance = this.front.value.getAmount();
    long blakeBalance = 0;
    for(Node<Block> temp = this.front.next; temp != null; temp = temp.next){
      alexisBalance += temp.value.getAmount();
      blakeBalance -= temp.value.getAmount();
    } // for
    pen.println(String.format("Alexis: %d, Blake: %d\n", alexisBalance, blakeBalance));
  } // printBalances(PrintWriter)

  /**
   * @return a string representation of the BlockChain which is simply the 
   * string representation of each of its blocks, earliest to latest, one 
   * per line.
   */
  public String toString(){
    String ret = "";
    for(Node<Block> temp = this.front; temp != null; temp = temp.next){
      ret += temp.value.toString();
      ret += "\n";
    } // for
    return ret;
  } // toString()

  /**
   * formats the mined block into a printable way
   * @return the formatted string.
   */
  public String stringMine(int amt) throws NoSuchAlgorithmException{
    /* Mine the block and return the formatted string as given in the project sample output. */
    Block temp = this.mine(amt);
    return String.format("amount = %d, nonce = %d\n", temp.getAmount(), temp.getNonce());
  } // stringMine(int)
} // class BlockChain
