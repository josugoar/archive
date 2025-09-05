package org.ccrew.cchess.ai;

import static org.ccrew.cchess.util.Logging.debug;
import static org.ccrew.cchess.util.Logging.warning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

public class AIProfile {

    private String name;

    public String getName() {
        return name;
    }

    private String protocol;

    public String getProtocol() {
        return protocol;
    }

    private String binary;

    public String getBinary() {
        return binary;
    }

    private String path;

    public String getPath() {
        return path;
    }

    private int delaySeconds = 2;

    public int getDelaySeconds() {
        return delaySeconds;
    }

    private String[] easyArgs;

    public String[] getEasyArgs() {
        return easyArgs;
    }

    private String[] normalArgs;

    public String[] getNormalArgs() {
        return normalArgs;
    }

    private String[] hardArgs;

    public String[] getHardArgs() {
        return hardArgs;
    }

    private String[] easyOptions;

    public String[] getEasyOptions() {
        return easyOptions;
    }

    private String[] normalOptions;

    public String[] getNormalOptions() {
        return normalOptions;
    }

    private String[] hardOptions;

    public String[] getHardOptions() {
        return hardOptions;
    }

    private String[] easyUciGoOptions;

    public String[] getEasyUciGoOptions() {
        return easyUciGoOptions;
    }

    private String[] normalUciGoOptions;

    public String[] getNormalUciGoOptions() {
        return normalUciGoOptions;
    }

    private String[] hardUciGoOptions;

    public String[] getHardUciGoOptions() {
        return hardUciGoOptions;
    }

    public static List<AIProfile> loadAiProfiles(String filename) {
        var profiles = new ArrayList<AIProfile>();

        var file = new Properties();
        try {
            file.load(AIProfile.class.getClassLoader().getResourceAsStream(filename));
        } catch (IOException | IllegalArgumentException | NullPointerException e) {
            warning("Failed to load AI profiles: %s", e.getMessage());
            return profiles;
        }

        for (String name : getGroups(file)) {
            debug("Loading AI profile %s", name);
            var profile = new AIProfile();
            try {
                profile.name = name;
                profile.protocol = file.getProperty(name + ".protocol");
                profile.binary = file.getProperty(name + ".binary");
                profile.easyArgs = loadArray(file, name, "arg", "easy");
                profile.normalArgs = loadArray(file, name, "arg", "normal");
                profile.hardArgs = loadArray(file, name, "arg", "hard");
                profile.easyOptions = loadArray(file, name, "option", "easy");
                profile.normalOptions = loadArray(file, name, "option", "normal");
                profile.hardOptions = loadArray(file, name, "option", "hard");
                profile.easyUciGoOptions = loadArray(file, name, "uci-go-option", "easy");
                profile.normalUciGoOptions = loadArray(file, name, "uci-go-option", "normal");
                profile.hardUciGoOptions = loadArray(file, name, "uci-go-option", "hard");

                if (file.containsKey(name + ".delay-before-move")) {
                    profile.delaySeconds = Integer.parseInt(file.getProperty(name + ".delay-before-move"));
                }
            } catch (Exception e) {
                warning("Error reading AI profiles: %s", e.getMessage());
                continue;
            }

            var path = Stream.of(System.getenv("PATH").split(File.pathSeparator))
                    .filter((String absolutePath) -> absolutePath.endsWith(profile.binary)).findFirst().get();
            if (path != null) {
                profile.path = path;
                profiles.add(profile);
            }
        }

        return profiles;
    }

    private static List<String> getGroups(Properties properties) {
        List<String> groups = new ArrayList<>();

        for (String property : properties.stringPropertyNames()) {
            String group = property.split(".")[0];
            if (!groups.contains(group)) {
                groups.add(group);
            }
        }

        return groups;
    }

    private static String[] loadArray(Properties file, String name, String type, String difficulty) throws IOException {
        int count = 0;
        while (file.containsKey(name + String.format(".%s-%s-%d", type, difficulty, count))) {
            count++;
        }

        String[] options = new String[count];
        for (var i = 0; i < count; i++) {
            options[i] = file.getProperty(name + String.format(".%s-%s-%d", type, difficulty, i));
        }

        return options;
    }

}
