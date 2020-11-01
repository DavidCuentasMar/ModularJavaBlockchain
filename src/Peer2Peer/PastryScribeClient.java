package Peer2Peer;

import rice.p2p.commonapi.*;
import rice.p2p.commonapi.Application;
import rice.p2p.commonapi.CancellableTask;
import rice.p2p.commonapi.Endpoint;
import rice.p2p.commonapi.Id;
import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;
import rice.p2p.commonapi.RouteMessage;
import rice.p2p.scribe.Scribe;
import rice.p2p.scribe.ScribeClient;
import rice.p2p.scribe.ScribeContent;
import rice.p2p.scribe.ScribeImpl;
import rice.p2p.scribe.Topic;
import rice.pastry.commonapi.PastryIdFactory;
/**
 *
 * @author Yennifer Herrera
 */
public class PastryScribeClient implements ScribeClient, Application {
    Scribe myScribe;
    Topic myTopic;
    protected Endpoint endpoint;

    public PastryScribeClient(Node node) {
        this.endpoint = node.buildEndpoint(this, "myinstance");
        myScribe = new ScribeImpl(node, "myScribeInstance");
        // construct the topic
        myTopic = new Topic(new PastryIdFactory(node.getEnvironment()), "Mining");
        System.out.println("myTopic = "+myTopic);

        // now we can receive messages
        endpoint.register();
    }
    
    public void subscribe() {
        myScribe.subscribe(myTopic, this);
    }
    
    public void sendMulticast(String msg) {
        if (myScribe.containsTopic(myTopic)) {
            System.out.println("Node "+endpoint.getLocalNodeHandle()+" broadcasting "+msg);
            PastryScribeContent myMessage = new PastryScribeContent(endpoint.getLocalNodeHandle(), msg);
            myScribe.publish(myTopic, myMessage);
        }else {
            System.out.println("Ups. Parece que aún no te has suscrito.");
        }
    }

    public void unsuscribe() {
        myScribe.unsubscribe(myTopic, this);
    }
    
    @Override
    public boolean anycast(Topic topic, ScribeContent content) {
        return true;
    }

    @Override
    public void childAdded(Topic arg0, NodeHandle arg1) {
        //child added
    }

    @Override
    public void childRemoved(Topic arg0, NodeHandle arg1) {
        // child removed
    }

    @Override
    public void subscribeFailed(Topic arg0) {
        System.out.println("Something happened. Subscribe failed");
    }

    @Override
    public boolean forward(RouteMessage arg0) {
        return true;
    }

    @Override
    public void deliver(Topic topic, ScribeContent content) {
        System.out.println("MyScribeClient.deliver("+topic+","+content+")");
        if (((PastryScribeContent)content).from == null) {
          new Exception("Stack Trace").printStackTrace();
        }
    }

    @Override
    public void update(NodeHandle arg0, boolean arg1) {
        
    }

    @Override
    public void deliver(Id id, Message message) {
        if (message instanceof PastryScribeContent) {
            sendMulticast(((PastryScribeContent) message).content);
        }
    }
    
}
