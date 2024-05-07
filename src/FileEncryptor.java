import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileEncryptor {

    Path fileToEncrypt;
    Path pathToNewFile;
    int complexity;

    final String LIST_OF_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public FileEncryptor(Path fileToEncrypt, Path pathToNewFile, int complexity) {
        this.fileToEncrypt = fileToEncrypt;
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
    }

    public Path encrypt() {

        Path encryptedFile = null;
        if (!Files.exists(pathToNewFile)) {
            try {
                encryptedFile = Files.createFile(pathToNewFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (FileReader in = new FileReader(fileToEncrypt.toFile());
        FileWriter out = new FileWriter(pathToNewFile.toFile())) {

            while (in.ready()) {
                char newChar = (char) in.read();

                if (!Encryptor.encryptionMap.containsKey(newChar)) {
                    List<String> codes = new ArrayList<>();

                    String code = null;
                    while (code == null) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int i = 0; i < complexity; i++) {
                            stringBuilder.append(LIST_OF_CHARACTERS.charAt((int) (Math.random() * LIST_OF_CHARACTERS.length())));
                        }
                        code = stringBuilder.toString();

                        if (!Encryptor.listOfAllEncryptions.contains(code)) {
                            Encryptor.listOfAllEncryptions.add(code);
                        } else {
                            code = null;
                        }
                    }
                    codes.add(code);
                    Encryptor.encryptionMap.put(newChar, codes);
                    out.write(code);
                } else {
                    out.write(Encryptor.encryptionMap.get(newChar).get(0));
                }
            }

        } catch (IOException e) {
            System.out.println("Файл не найден");
        }

        return pathToNewFile;
    }
}

