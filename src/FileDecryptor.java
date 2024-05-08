import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileDecryptor {

    private final Path fileToDecrypt;
    private Path pathToNewFile;
    private Map<Character, List<String>> encryptionMap = null;

    public FileDecryptor(Path fileToDecrypt, Path pathToNewFile) {
        this.fileToDecrypt = fileToDecrypt;
        this.pathToNewFile = pathToNewFile;
    }

    public FileDecryptor(Path fileToDecrypt) {
        this.fileToDecrypt = fileToDecrypt;
    }

    public void decryptFromFileToFile() {
        Path decryptedFile = createNewFile();

        try (FileReader in = new FileReader(fileToDecrypt.toFile());
             FileWriter out = new FileWriter(decryptedFile.toFile())) {

            deserializingKeysFromFile();

            while (in.ready()) {
                char decryptedChar = getDecryptedChar(in);
                out.write(String.valueOf(decryptedChar));
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
    }

    private Path createNewFile() {
        Path decryptedFile;
        if (!Files.exists(pathToNewFile)) {
            try {
                decryptedFile = Files.createFile(pathToNewFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            decryptedFile = pathToNewFile;
        }
        return decryptedFile;
    }

    public void decryptFromFileToConsole() {
        try (FileReader in = new FileReader(fileToDecrypt.toFile())) {

            deserializingKeysFromFile();

            System.out.println("Расшифрованный текст:");

            while (in.ready()) {
                char decryptedChar = getDecryptedChar(in);
                System.out.print(decryptedChar);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
    }

    private char getDecryptedChar(FileReader in) {
        StringBuilder stringBuilder = new StringBuilder();

        int complexity = Objects.requireNonNull(encryptionMap.entrySet().stream().findAny().orElse(null)).getValue().get(0).length();
        for (int i = 0; i < complexity; i++) {
            try {
                stringBuilder.append((char) in.read());
            } catch (IOException e) {
                System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
            }
        }
        String code = stringBuilder.toString();

        char decryptedChar = 0;
        for (Character key : encryptionMap.keySet()) {
            if (encryptionMap.get(key).contains(code)) {
                decryptedChar = key;
                break;
            }
        }

        return decryptedChar;
    }

    private void deserializingKeysFromFile() {
        String fileWithKeys = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите абсолютный путь файла-ключа:");

        while (true) {
            try {
                fileWithKeys = reader.readLine();
            } catch (IOException e) {
                System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
            }

            if (fileWithKeys != null && Files.exists(Path.of(fileWithKeys))) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithKeys))) {
                    encryptionMap = (HashMap<Character, List<String>>) ois.readObject();
                    break;
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (StreamCorruptedException e) {
                    System.out.println("Данный файл не содержит ключей");
                } catch (FileNotFoundException e) {
                    System.out.println("Файл не найден");
                } catch (IOException e) {
                    System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
                }
            } else {
                System.out.println("Введите корректный путь к файлу-ключу:");
            }
        }
    }
}
