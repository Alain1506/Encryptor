import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileEncryptor {

    private final int complexity;
    private final Reader reader;
    private final Writer writer;
    private final String keyStorageFile;

    final String LIST_OF_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private final List<String> listOfAllEncryptions = new ArrayList<>();
    private final Map<Character, List<String>> mapWithKeys = new HashMap<>();

    public FileEncryptor(Reader reader, Writer writer, int complexity, String keyStorageFile) {
        this.reader = reader;
        this.writer = writer;
        this.complexity = complexity;
        this.keyStorageFile = keyStorageFile;
    }

    public void encrypt() {
        try {
            while (reader.ready()) {
                char nextChar = (char) reader.read();
                if (!mapWithKeys.containsKey(nextChar)) {
                    String code = generateCode();
                    mapWithKeys.put(nextChar, new ArrayList<>(List.of(code)));
                    writer.write(code);
                } else if (mapWithKeys.containsKey(nextChar) && mapWithKeys.get(nextChar).size() < 3) {
                    String code = generateCode();
                    mapWithKeys.get(nextChar).add(code);
                    writer.write(code);
                } else {
                    writer.write(mapWithKeys.get(nextChar).get((int) (Math.random() * 3)));
                }
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
        }
        serializeKeysToFile();
    }

    private void serializeKeysToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyStorageFile))) {
            oos.writeObject(mapWithKeys);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
        }
    }

    public String generateCode() {

        String code = null;
        for (int j = 0; j < 5; j++) {
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < complexity; i++) {
                stringBuilder.append(LIST_OF_CHARACTERS.charAt((int) (Math.random() * LIST_OF_CHARACTERS.length())));
            }
            code = stringBuilder.toString();

            if (!listOfAllEncryptions.contains(code)) {
                listOfAllEncryptions.add(code);
                break;
            } else {
                code = null;
            }
        }
        if (code == null) {
            System.out.println("Невозможно закодировать ресурс, увеличьте сложность кодирования");
            System.exit(0); //как вернуть задание в начало?
        }

        return code;
    }
}

