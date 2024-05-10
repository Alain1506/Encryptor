import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileEncryptor {

    final String LIST_OF_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private final List<String> listOfAllEncryptions = new ArrayList<>();
    private final Map<Character, List<String>> mapWithKeys = new HashMap<>();

    public FileEncryptor() {
    }

    public void encrypt(Reader reader, Writer writer, int complexity, String keyStorageFile) {
        try {
            while (reader.ready()) {
                char nextChar = (char) reader.read();
                if (!mapWithKeys.containsKey(nextChar)) {
                    String code = generateCode(complexity);
                    mapWithKeys.put(nextChar, new ArrayList<>(List.of(code)));
                    writer.write(code);
                } else if (mapWithKeys.containsKey(nextChar) && mapWithKeys.get(nextChar).size() < 3) {
                    String code = generateCode(complexity);
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
        serializeKeysToFile(keyStorageFile);
    }

    private void serializeKeysToFile(String keyStorageFile) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyStorageFile))) {
            oos.writeObject(mapWithKeys);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
        }
    }

    public String generateCode(int complexity) {

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
            throw new RuntimeException("Невозможно зашифровать ресурс, увеличьте сложность шифрования");
        }

        return code;
    }
}

