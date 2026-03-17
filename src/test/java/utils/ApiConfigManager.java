package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiConfigManager {

    private static final Logger log = LogManager.getLogger(ApiConfigManager.class);
    private static Properties properties = new Properties();
    private static ApiConfigManager instance;

    private ApiConfigManager() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) { log.error("No se encontro config.properties"); return; }
            properties.load(input);
            log.info("config.properties API cargado");
        } catch (IOException e) {
            log.error("Error: " + e.getMessage());
        }
    }

    public static ApiConfigManager getInstance() {
        if (instance == null) instance = new ApiConfigManager();
        return instance;
    }

    public String get(String key)   { return properties.getProperty(key); }
    public int getInt(String key)   { return Integer.parseInt(properties.getProperty(key)); }

    public String getBaseUrl()          { return get("base.url"); }
    public int getDefaultResults()      { return getInt("default.results"); }
    public String getSeed()             { return get("seed"); }
    public String getNatMX()            { return get("nat.mx"); }
    public String getGenderMale()       { return get("gender.male"); }
    public String getGenderFemale()     { return get("gender.female"); }
}
