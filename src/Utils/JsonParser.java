/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Blockchain.Model.Block;
import Blockchain.Model.Chain;
import Blockchain.Model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author will
 */
public class JsonParser {
    
    public static String chainToJson(Object obj) {
        //Creating the ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonString;
    } 
    
    public static Chain jsonToChain(String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        Chain newChain = null;
        CollectionType blockReference = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Block.class);
        CollectionType transReference = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Transaction.class);
        
        try {
            String[] data = objectMapper.convertValue(jsonString, String[].class);
            ArrayList<Transaction> transactions = objectMapper.readValue(jsonString, transReference);
//            ArrayList<Transaction> transactions = objectMapper.readValue(
//                    jsonString, new TypeReference<ArrayList<Transaction>>() {}
//            );
//            ArrayList<Block> chain = objectMapper.readValue(
//                    jsonString, new TypeReference<ArrayList<Block>>() {}
//            );
            ArrayList<Block> chain = objectMapper.readValue(jsonString, blockReference);
            newChain = objectMapper.readValue(jsonString, Chain.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("process correct !!!!!!!!!!!");
        return newChain;
    }
}
