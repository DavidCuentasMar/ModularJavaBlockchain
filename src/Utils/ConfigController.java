/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Paths;

/**
 *
 * @author will
 */
public class ConfigController {

    public static ConfigProgram readConfigJson() {
        
        ConfigProgram config = null;
        System.out.println("AQUI!!!!!!!!!!!!!!");
        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert JSON file to config
            config = mapper.readValue(Paths.get("config.json").toFile(), ConfigProgram.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return config;
    }
}
