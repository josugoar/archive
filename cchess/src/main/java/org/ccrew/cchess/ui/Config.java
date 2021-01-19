package org.ccrew.cchess.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String CONFIG_PATH = "config/piece.properties";

    private static Properties properties = new Properties();

    static {
        InputStream inStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_PATH);
        try {
            properties.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBishop(boolean white) {
        if (white) {
            return properties.getProperty("whiteBishop");
        } else {
            return properties.getProperty("blackBishop");
        }
    }

    public static String getKing(boolean white) {
        if (white) {
            return properties.getProperty("whiteKing");
        } else {
            return properties.getProperty("blackKing");
        }
    }

    public static String getKnight(boolean white) {
        if (white) {
            return properties.getProperty("whiteKnight");
        } else {
            return properties.getProperty("blackKnight");
        }
    }

    public static String getPawn(boolean white) {
        if (white) {
            return properties.getProperty("whitePawn");
        } else {
            return properties.getProperty("blackPawn");
        }
    }

    public static String getQueen(boolean white) {
        if (white) {
            return properties.getProperty("whiteQueen");
        } else {
            return properties.getProperty("blackQueen");
        }
    }

    public static String getRook(boolean white) {
        if (white) {
            return properties.getProperty("whiteRook");
        } else {
            return properties.getProperty("blackRook");
        }
    }

}
