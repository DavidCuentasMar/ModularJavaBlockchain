package Utils;

import Blockchain.Model.Chain;
import Blockchain.Model.Miner;
import Peer2Peer.PastryScribeClient;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import rice.Continuation;
import rice.environment.Environment;
import rice.p2p.commonapi.Id;
import rice.p2p.past.*;
import rice.pastry.NodeIdFactory;
import rice.pastry.PastryNode;
import rice.pastry.PastryNodeFactory;
import rice.pastry.commonapi.PastryIdFactory;
import rice.pastry.socket.SocketPastryNodeFactory;
import rice.pastry.standard.RandomNodeIdFactory;
import rice.persistence.*;

public class PastryPeer2Peer {
    /**
    * This constructor sets up a PastryNode.  It will bootstrap to an 
    * existing ring if it can find one at the specified location, otherwise
    * it will start a new ring.
    * 
    * @param bindport the local port to bind to 
    * @param bootaddress the IP:port of the node to boot from
    * @param env the environment for these nodes
    */

    public PastryPeer2Peer(int bindport, InetSocketAddress bootaddress, Environment env) throws Exception {
    
        // Generate the NodeIds Randomly
        NodeIdFactory nidFactory = new RandomNodeIdFactory(env);
        
        // construct the PastryNodeFactory, this is how we use rice.pastry.socket
        PastryNodeFactory factory = new SocketPastryNodeFactory(nidFactory, bindport, env);

        // construct a node
        PastryNode node = factory.newNode();
        
        // used for generating PastContent object Ids.
        // this implements the "hash function" for our DHT
        PastryIdFactory idf = new PastryIdFactory(env);
        
        PastryScribeClient app;
        if(bindport == bootaddress.getPort()){
            Chain theChain = new Chain();
            app = new PastryScribeClient(node, theChain);
        }else {
            app = new PastryScribeClient(node);
        }
    
        
        node.boot(bootaddress);
                
        // the node may require sending several messages to fully boot into the ring
        synchronized(node) {
            while(!node.isReady() && !node.joinFailed()) {
                // delay so we don't busy-wait
                node.wait(500);
                
                // abort if can't join
                if (node.joinFailed()) {
                  throw new IOException("Could not join the FreePastry ring.  Reason:"+node.joinFailedReason()); 
                }
            }
        }
        
        System.out.println("Finished creating new node "+node);
        
        // wait 10 seconds
        env.getTimeSource().sleep(10000);
        
        
//        PastryMenu PastryMenuThread = new PastryMenu(app, node, env);
//        PastryMenuThread.start();
    }

}

class BlockchainScribeApp {
    Chain chain;
    Miner miner;
    public PublicKey publicKey;
    private PrivateKey privateKey;
    PastryScribeClient client;

    public BlockchainScribeApp(Chain chain, Miner miner) {
        this.chain = chain;
        this.miner = miner;
        KeyPair keyPairA = DigitalSignature.generateKeyPair();
        publicKey = keyPairA.getPublic();
        privateKey = keyPairA.getPrivate();
    }
//    Programar creación de transacciones
    
    
}

class PastryMenu extends Thread {
    PastryScribeClient client;
    PastryNode node;
    Environment env;

    PastryMenu(PastryScribeClient client, PastryNode node, Environment env) {
        this.client = client;
        this.node = node;
        this.env = env;
    }

    @Override
    public void run() {
        System.out.println("MyThread running");
        try {
            System.out.println("Bienvenido");
            Scanner sc = new Scanner(System.in);
            Scanner scm = new Scanner(System.in);
            int RES = 0;
            while (RES != 2) {
                System.out.println("------------------| Selecciona una opción |------------------");
                System.out.println("1. Suscribirme");
                System.out.println("2. Enviar mensaje multicast");
                System.out.println("3. Salir");
                int in = sc.nextInt();
                if (in != 1 && in != 2 && in != 3) {
                    throw new Exception("Respuesta inválida. Adios");
                } else if (in == 1) {
                    System.out.println("Ya estás suscrito. Ahora puedes publicar contenido.");
                    //client.subscribe();
                } else if (in == 2) {
                    System.out.print("Escribe el mensaje: ");
                    String msg = scm.nextLine();
                    if(msg.isEmpty()){
                        throw new Exception("Respuesta inválida. Adios");
                    }
                    client.sendMulticast(msg);
                }else {
                    break;
                }
            }
        }
        catch(IOException io){
            System.out.println("Hubo un problema con el archivo");
        }catch (Exception e) {
            System.out.println("Respuesta inválida. Adios");
        } finally {
            client.unsuscribe();
            env.destroy();
            System.exit(1);
            
        }
    }
}