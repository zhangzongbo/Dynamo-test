package config;

/**
 * Created by zhangzb on 3/19/18.
 */





import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashSet;

import lombok.extern.slf4j.Slf4j;
import util.FileUtil;
//import lombok.extern.slf4j.Slf4j;

/**
 * Configuration manager.
 * Usage:
 *   Config.getString("config_name")
 *   Config.getInt("config_name", default_value)
 * Our configurations are stored in several files, both stored in src/main/resources/conf/.
 * common.conf contains environment-independent configurations. It will always be loaded.
 * prod.conf.conf/staging.conf/dev.conf contains configurations that are different in each environment. Only one of them
 * will be loaded.
 * Environment variable PANDORA_ACTIVE_CONFIG determines which one will be loaded. If it is not specified, we will
 * load dev.conf
 */

@Slf4j

public class Config {


    // Valid config names
    public static final String DEV = "dev";
    public static final String STAGING = "staging";
    public static final String PROD = "prod";
    public static final String TEST = "test";
    public static final String root_dir = System.getProperty("user.dir");

    // Get active configuration.
    public static String getActiveConfig() {
        return instance.activeConfig;
    }

    public static void loadTestConfig() {
        instance.activeConfig = TEST;
        instance.loadActiveConfig();
//        ConnectionProvider.initDBSource();
    }

    // Get a string type config. Returns null if not found.
    public static String getString(String key) {
        return instance.configuration.getString(key);
    }

    // Get a string type config. Returns defaultValue if not found.
    public static String getString(String key, String defaultValue) {
        return instance.configuration.getString(key, defaultValue);
    }

    // Get a string type config. Panic and exit if not found.
    public static String getCriticalString(String key) {
        String value = instance.configuration.getString(key);
        if (value == null) {
            log.error("Cannot find configuration: " + key, new Exception());
            System.exit(-1);
        }
        return value;
    }

    public static BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        return instance.configuration.getBigDecimal(key, defaultValue);
    }

    // Get a int type config. Returns defaultValue if not found.
    public static int getInt(String key, int defaultValue) {
        return instance.configuration.getInt(key, defaultValue);
    }

    // Singleton.
    private static Config instance = new Config();

    private String activeConfig;
    private CompositeConfiguration configuration;

    // Loads config files. Panics if anything goes wrong.
    private Config() {
        // Find active configuration from environment variable.
        activeConfig = System.getenv("CRAWLER_ACTIVE_CONFIG");
        if (activeConfig == null) {
            activeConfig = DEV;
        }
        loadActiveConfig();
    }

    private void loadActiveConfig() {
        // Verify active configuration. Panic if active configuration is invalid.
        HashSet<String> validConfigs = new HashSet<>();
        validConfigs.add(DEV);
        validConfigs.add(STAGING);
        validConfigs.add(PROD);
        validConfigs.add(TEST);
        if (!validConfigs.contains(activeConfig)) {
            log.error("Invalid configuration name: " + activeConfig, new Exception());
            System.exit(-1);
        }

        // Load common configuration and active configuration.
        configuration = new CompositeConfiguration();
        configuration.addConfiguration(loadConf("common"));
        configuration.addConfiguration(loadConf("strings"));
//        configuration.addConfiguration(loadEnv());
        configuration.addConfiguration(loadConf(activeConfig));
    }
    private Configuration loadEnv(){
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
        String captcha_url = System.getenv("CAPTCHA_URL");
        String captcha_path = System.getenv("CAPTCHA_PATH");
        if (captcha_url == null || captcha_path == null){
            captcha_url = "http://127.0.0.1:8888/";
            // captcha_url = "http://192.168.1.68:8888/";
            captcha_path = "/tmp/";
        }
        log.info("captcha_url = " + captcha_url);
        log.info("captcha_path = " + captcha_path);
        if (captcha_url == null || captcha_path == null){
            log.error("Invalid configuration name: CAPTCHA_URL and CAPTCHA_PATH", new Exception());
            System.exit(-1);
        }
        propertiesConfiguration.setProperty("captcha.url",captcha_url);
        propertiesConfiguration.setProperty("captcha.path",captcha_path);
        return propertiesConfiguration;
    }
    // Loads classpath:/conf/<configName>.conf
    // Panics if loading failed.
    private Configuration loadConf(String configName) {
        PropertiesConfiguration conf = new PropertiesConfiguration();
        InputStream is = null;
        try {
            log.info("/conf/" + configName + ".conf");
            is = Config.class.getResourceAsStream("/conf/" + configName + ".conf");
            conf.load(is);
        } catch (ConfigurationException e) {
            log.error("Cannot load configuration " + configName, e);
            FileUtil.safeClose(is);
            System.exit(-1);
        } finally {
            FileUtil.safeClose(is);
        }
        return conf;
    }
}
