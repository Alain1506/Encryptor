import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDecryptor {

    private Path fileToDecrypt;
    private Path pathToNewFile;
    private int complexity;

    private Map<Character, List<String>> encryptionMap = null;

    public FileDecryptor(Path fileToDecrypt, Path pathToNewFile, int complexity) {
        this.fileToDecrypt = fileToDecrypt;
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
    }

    public FileDecryptor(Path fileToDecrypt, int complexity) {
        this.fileToDecrypt = fileToDecrypt;
        this.complexity = complexity;
    }

    public Path decryptFromFileToFile() throws IOException {

        Path decryptedFile = createNewFile();

        try (FileReader in = new FileReader(fileToDecrypt.toFile());
             FileWriter out = new FileWriter(decryptedFile.toFile())) {

            deserializingKeysFromFile();

            while (in.ready()) {
                char decryptedChar = getDecryptedChar(in);
                out.write(String.valueOf(decryptedChar));
            }
        }

        return decryptedFile;
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

    public void decryptFromFileToConsole() throws IOException {
        try (FileReader in = new FileReader(fileToDecrypt.toFile())) {

            deserializingKeysFromFile();

            while (in.ready()) {
                char decryptedChar = getDecryptedChar(in);
                System.out.print(decryptedChar);
            }
        }
    }

    private char getDecryptedChar(FileReader in) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < complexity; i++) {
            stringBuilder.append((char) in.read());
        }
        String code = stringBuilder.toString();

        char decryptedChar = 0;
        for (Character key : encryptionMap.keySet()) {
            if (encryptionMap.get(key).contains(code)) {
                decryptedChar = key;
            }
        }

        return decryptedChar;
    }

    private void deserializingKeysFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите абсолютный путь файла-ключа:");
        String fileWithKeys = reader.readLine();

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileWithKeys))) {
            encryptionMap = (HashMap<Character, List<String>>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
