import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileEncryptor {

    private Path fileToEncrypt;
    private Path pathToNewFile;
    private int complexity;

    final String LIST_OF_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public FileEncryptor(Path fileToEncrypt, Path pathToNewFile, int complexity) {
        this.fileToEncrypt = fileToEncrypt;
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
    }

    public Path encrypt() {

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

        try (FileReader in = new FileReader(fileToEncrypt.toFile());
        FileWriter out = new FileWriter(encryptedFile.toFile())) {

            while (in.ready()) {
                char nextChar = (char) in.read();

                if (!Encryptor.encryptionMap.containsKey(nextChar)) {
                    String code = null;
                    while (code == null || !Encryptor.listOfAllEncryptions.contains(code)) {
                        code = codeGenerator();
                    }

                    Encryptor.encryptionMap.put(nextChar, new ArrayList<>());
                    Encryptor.encryptionMap.get(nextChar).add(code);
                    Encryptor.listOfAllEncryptions.add(code);
                    out.write(code);

                } else if (Encryptor.encryptionMap.containsKey(nextChar) && Encryptor.encryptionMap.get(nextChar).size() < 3) {
                    String code = null;
                    while (code == null || !Encryptor.listOfAllEncryptions.contains(code)) {
                        code = codeGenerator();
                    }
                    Encryptor.encryptionMap.get(nextChar).add(code);
                    out.write(code);

                } else {
                    out.write(Encryptor.encryptionMap.get(nextChar).get((int) (Math.random() * 3)));
                }
            }
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }

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

