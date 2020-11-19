/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author will
 */
public class JsonParser {
    
    public static String chainToJson(Object obj) {
        System.out.println("chain:"+ obj);
        JSONObject jsonObj = new JSONObject((Map) obj);
        String str = jsonObj.toString();
        return str;
    } 
    
}
