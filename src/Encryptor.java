import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.*;

public class Encryptor {

    List<String> listOfAllEncryptions = new ArrayList<>();

    Map<Character, List<String>> encryptionMap = new HashMap<>();

    public static FileEncryptor fileEncryptor = null;
    FileDecryptor fileDecryptor = null;

    public static void main(String[] args) {
        System.out.println("Выберите операцию:"
                + "\n0 - зашифровать файл"
                + "\n1 - расшифровать файл");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String command = null;
        try {
            command = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (command.equals("0")) {
            //зашифровать файл
            System.out.println("Введите имя файла, который нужно зашифровать");
            Scanner scanner = new Scanner(System.in);
            String fileToEncrypt = scanner.nextLine();
            fileEncryptor = new FileEncryptor(Path.of(fileToEncrypt));
            fileEncryptor.encrypt();
        } else if (command.equals("1")) {
            // расшифровать файл
        } else {
            System.out.println("Вы ввели неверную команду");
        }

    }
}
