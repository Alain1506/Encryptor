import java.io.*;
import java.util.*;

public class FileDecryptor {

    private Map<Character, List<String>> keys = null;
    private final Reader reader;
    private final Writer writer;
    private String fileWithKeys;
    private int complexity = 0;
    private final Map<String, Character> convertedKeys = new HashMap<>();

    public FileDecryptor(Reader reader, Writer writer, String fileWithKeys) {
        this.reader = reader;
        this.writer = writer;
        this.fileWithKeys = fileWithKeys;
    }

    public void decrypt() {

        while (keys == null) {
            deserializeKeysFromFile();
        }
        try {
            while (reader.ready()) {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < complexity; i++) {
                    stringBuilder.append((char) reader.read());
                }
                String code = stringBuilder.toString();
                char decryptedChar = 0;
                try {
                    decryptedChar = convertedKeys.get(code);
                } catch (NullPointerException e) {
                    System.out.println("Введенный вами файл ключа не подходит для раскодирования, введите корректный файл-ключ:");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                    fileWithKeys = bufferedReader.readLine();
                    deserializeKeysFromFile();
                }
                writer.write(String.valueOf(decryptedChar));
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
        }
    }

    private void deserializeKeysFromFile() {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithKeys))) {
            keys = (Map<Character, List<String>>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            System.out.println("Класс не найден");
        } catch (StreamCorruptedException e) {
            System.out.println("Файл-ключ не содержит ключей, введите корректный файл-ключ:");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            try {
                fileWithKeys = bufferedReader.readLine();
            } catch (IOException ex) {
                System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
            }
            deserializeKeysFromFile();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
        }

        for (Character key: keys.keySet()) {
            for (int i = 0; i < keys.get(key).size(); i++) {
                convertedKeys.put(keys.get(key).get(i), key);
            }
        }
        complexity = convertedKeys.entrySet().stream().findFirst().get().getKey().length();
    }
}