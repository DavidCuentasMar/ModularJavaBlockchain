package Blockchain.Model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
public class Block {
    int idCadena;
    int index;
    LocalDateTime timestamp;
    ArrayList<Transaction> transactions;
    String previousHash;
    String hash;
    int nonce;
    Random r;
}
