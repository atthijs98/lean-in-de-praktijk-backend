package lean.util.MessageFactory;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class MessageFactory {
    public MessageUtil getMessageUtil(Language language) {
        try {
            FileReader fileReader = new FileReader("src/main/resources/util.messageFactory/"+language.name()+"Messages.json");
            JsonReader reader = new JsonReader(fileReader);
            return new MessageUtil(new Gson().fromJson(reader, Map.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
