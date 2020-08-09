package lean.util.MessageFactory;

import java.util.Map;

public class MessageUtil {
    private Map<String, String> languageMap;
    public MessageUtil(Map<String, String> map) {
        languageMap = map;
    }
    public String get(Message key) {
        return languageMap.get(key.name());
    }
}
