import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;

public class Encryptor {

    public static List<String> listOfAllEncryptions = new ArrayList<>();

    public static Map<Character, List<String>> encryptionMap = new HashMap<>();

    public static FileEncryptor fileEncryptor = null;
    FileDecryptor fileDecryptor = null;

    public static void main(String[] args) throws IOException {
        System.out.println("Выберите операцию:"
                + "\n0 - зашифровать файл"
                + "\n1 - расшифровать файл");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String command = reader.readLine();

            if (command.equals("0")) {
                //зашифровать файл
                System.out.println("Введите абсолютный путь файла, который нужно зашифровать:");
                String fileToEncrypt = reader.readLine();
                System.out.println("Выберите сложность шифрования от 1 до 5:");
                int complexity = Integer.valueOf(reader.readLine());
                System.out.println("Введите абсолютный путь для нового файла:");
                String pathToNewFile = reader.readLine();
                fileEncryptor = new FileEncryptor(Path.of(fileToEncrypt), Path.of(pathToNewFile), complexity);
                fileEncryptor.encrypt();
            } else if (command.equals("1")) {
                // расшифровать файл
            } else {
                System.out.println("Вы ввели неверную команду");
            }
        }

    }
}
