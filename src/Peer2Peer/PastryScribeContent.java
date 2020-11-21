/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Peer2Peer;

import rice.p2p.commonapi.NodeHandle;
import rice.p2p.scribe.ScribeContent;

/**
 *
 * @author Yennifer Herrera
 */
public class PastryScribeContent implements ScribeContent {

    public NodeHandle from;
    public String content;
    public enum contentType {
        TRANSACTION,
        BLOCK,
        CHAIN,
        TEXT
    }
    public contentType type;


    public PastryScribeContent(NodeHandle from, String content, contentType type) {
        this.from = from;
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return "The content is " + content + " from: " + from;
    }
}
