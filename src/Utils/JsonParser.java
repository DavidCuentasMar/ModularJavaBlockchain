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
import com.fasterxml.jackson.databind.DeserializationFeature;
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

    public static String chainToJson(Chain chain) {
        //Creating the ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(chain);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonString;
    }

    public static Chain jsonToChain(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Chain newChain = null;
        CollectionType blockReference = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Block.class);
        CollectionType transReference = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, Transaction.class);

        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
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

    public static String transactionToJson(Transaction trans) {
        //Creating the ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(trans);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonString;
    }

    public static Transaction jsonToTransaction(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction trans = null;

        try {
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            String[] data = objectMapper.convertValue(jsonString, String[].class);
            trans = objectMapper.readValue(jsonString, Transaction.class);
            System.out.println("jsonToTransaction correct");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trans;
    }

    public static String blockToJson(Block block) {
        //Creating the ObjectMapper object
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(block);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonString;
    }

    public static Block jsonToBlock(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        Block block = null;
        try {
            block = objectMapper.readValue(jsonString, Block.class);
            System.out.println("jsonToBlock correct");
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return block;
    }

}
