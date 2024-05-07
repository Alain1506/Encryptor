import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileDecryptor {

    private Path fileToDecrypt;
    private Path pathToNewFile;
    private int complexity;

    public FileDecryptor(Path fileToDecrypt, Path pathToNewFile, int complexity) {
        this.fileToDecrypt = fileToDecrypt;
        this.pathToNewFile = pathToNewFile;
        this.complexity = complexity;
    }

    public Path decrypt() throws IOException {

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

        try (FileReader in = new FileReader(fileToDecrypt.toFile());
             FileWriter out = new FileWriter(decryptedFile.toFile())) {

            while (in.ready()) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < complexity; i++) {
                    stringBuilder.append((char) in.read());
                }
                String code = stringBuilder.toString();

                char decryptedChar = 0;
                for (Character key : Encryptor.encryptionMap.keySet()) {
                    if (Encryptor.encryptionMap.get(key).contains(code)) {
                        decryptedChar = key;
                    }
                }

                out.write(String.valueOf(decryptedChar));
            }

        }

        return decryptedFile;
    }
}
