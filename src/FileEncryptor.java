import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileEncryptor {

    private Path fileToEncrypt;
    private Path pathToNewFile;
    private int complexity;
    private String textFromConsole;

    final String LIST_OF_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private Map<Character, List<String>> encryptionMap = new HashMap<>();

    public FileEncryptor(Path fileToEncrypt, Path pathToNewFile, int complexity) {
        this.fileToEncrypt = fileToEncrypt;
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
    }

    public FileEncryptor(Path pathToNewFile, int complexity, String textFromConsole) {
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
        this.textFromConsole = textFromConsole;
    }

    public Path encryptFromFileToFile() throws IOException {

        Path encryptedFile = createNewFile();

        try (FileReader in = new FileReader(fileToEncrypt.toFile());
             FileWriter out = new FileWriter(encryptedFile.toFile())) {
            while (in.ready()) {
                char nextChar = (char) in.read();
                encryptToFile(nextChar, out);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        serializingKeysToFile();

        return encryptedFile;
    }

    private Path createNewFile() {
        Path encryptedFile;
        if (!Files.exists(pathToNewFile)) {
            try {
                encryptedFile = Files.createFile(pathToNewFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            encryptedFile = pathToNewFile;
        }
        return encryptedFile;
    }

    private void encryptToFile(char nextChar, FileWriter out) throws IOException {
        if (!encryptionMap.containsKey(nextChar)) {
            String code = null;
            while (code == null || !Encryptor.listOfAllEncryptions.contains(code)) {
                code = codeGenerator();
            }
            encryptionMap.put(nextChar, new ArrayList<>());
            encryptionMap.get(nextChar).add(code);
            Encryptor.listOfAllEncryptions.add(code);
            out.write(code);
        } else if (encryptionMap.containsKey(nextChar) && encryptionMap.get(nextChar).size() < 3) {
            String code = null;
            while (code == null || !Encryptor.listOfAllEncryptions.contains(code)) {
                code = codeGenerator();
            }
            encryptionMap.get(nextChar).add(code);
            out.write(code);
        } else {
            out.write(encryptionMap.get(nextChar).get((int) (Math.random() * 3)));
        }
    }

    private void serializingKeysToFile() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите абсолютный путь для файла-ключа:");
        String fileWithKeys = reader.readLine();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileWithKeys))) {
            oos.writeObject(encryptionMap);
        }
        reader.close();
    }

    public Path encryptFromConsoleToFile() throws IOException {

        Path encryptedFile = createNewFile();

        try (InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(textFromConsole.getBytes(StandardCharsets.UTF_8)));
             FileWriter out = new FileWriter(encryptedFile.toFile())) {
            while (in.ready()) {
                char nextChar = (char) in.read();
                encryptToFile(nextChar, out);
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        serializingKeysToFile();

        return encryptedFile;
    }

    public String codeGenerator() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < complexity; i++) {
            stringBuilder.append(LIST_OF_CHARACTERS.charAt((int) (Math.random() * LIST_OF_CHARACTERS.length())));
        }
        String code = stringBuilder.toString();

        if (!Encryptor.listOfAllEncryptions.contains(code)) {
            Encryptor.listOfAllEncryptions.add(code);
        } else {
            code = null;
        }

        return code;
    }
}

