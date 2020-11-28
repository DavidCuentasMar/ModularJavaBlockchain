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
import java.util.Base64;

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

/**
 * @author Jeff Hoye
 * @author Yennifer Herrera
 */
public class PastryPeer2Peer {

    /**
     * This constructor sets up a PastryNode. It will bootstrap to an existing
     * ring if it can find one at the specified location, otherwise it will
     * start a new ring.
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

        node.boot(bootaddress);

        // the node may require sending several messages to fully boot into the ring
        synchronized (node) {
            while (!node.isReady() && !node.joinFailed()) {
                // delay so we don't busy-wait
                node.wait(500);

                // abort if can't join
                if (node.joinFailed()) {
                    throw new IOException("Could not join the FreePastry ring.  Reason:" + node.joinFailedReason());
                }
            }
        }

        System.out.println("Finished creating new node " + node);

        // wait 10 seconds
        env.getTimeSource().sleep(10000);

        PastryScribeClient app;
        if (bindport == bootaddress.getPort()) {
            app = new PastryScribeClient(node, true);
        } else {
            app = new PastryScribeClient(node);

            // wait 5 seconds
            env.getTimeSource().sleep(5000);

            // Get Chain
            app.requestChain();
        }

        // Request PubKey
        app.sharePublicKeyToTest();

        // wait 5 seconds
        env.getTimeSource().sleep(5000);
        
        app.startPublishTask();

    }

}