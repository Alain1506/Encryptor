import java.io.*;
import java.nio.file.Path;
import java.util.*;

public class Encryptor {

    public static List<String> listOfAllEncryptions = new ArrayList<>();

    public static FileEncryptor fileEncryptor = null;
    public static FileDecryptor fileDecryptor = null;

    public static void main(String[] args) throws IOException {
        System.out.println("Выберите операцию:"
                + "\n0 - зашифровать"
                + "\n1 - расшифровать");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String command = reader.readLine();

            if (command.equals("0")) {
                //зашифровать
                System.out.println("Выберите операцию:"
                        + "\n0 - зашифровать файл в файл"
                        + "\n1 - зашифровать текст, введенный в консоль, в файл");
                String task = reader.readLine();

                if (task.equals("0")) {
                    System.out.println("Введите абсолютный путь файла, который нужно зашифровать:");
                    String fileToEncrypt = reader.readLine();
                    System.out.println("Выберите сложность шифрования от 1 до 5:");
                    int complexity = Integer.parseInt(reader.readLine());
                    System.out.println("Введите абсолютный путь для нового файла:");
                    String pathToEncrypt = reader.readLine();
                    fileEncryptor = new FileEncryptor(Path.of(fileToEncrypt), Path.of(pathToEncrypt), complexity);
                    fileEncryptor.encryptFromFileToFile();

                } else if (task.equals("1")) {
                    System.out.println("Выберите сложность шифрования от 1 до 5:");
                    int complexity = Integer.parseInt(reader.readLine());
                    System.out.println("Введите абсолютный путь для нового файла:");
                    String pathToEncrypt = reader.readLine();
                    System.out.println("Введите текст для шифрования:");
                    String consoleEntry = reader.readLine();
                    fileEncryptor = new FileEncryptor(Path.of(pathToEncrypt), complexity, consoleEntry);
                    fileEncryptor.encryptFromConsoleToFile();
                } else {
                    System.out.println("Вы ввели неверную команду");
                }
            } else if (command.equals("1")) {
                // расшифровать
                System.out.println("Введите абсолютный путь файла, который нужно расшифровать:");
                String fileToDecrypt = reader.readLine();
                System.out.println("Выберите сложность шифрования вашего файла от 1 до 5:");
                int complexity = Integer.parseInt(reader.readLine());

                System.out.println("Выберите операцию:"
                        + "\n0 - расшифровать файл в файл"
                        + "\n1 - расшифровать файл в консоль");
                String task = reader.readLine();

                if (task.equals("0")) {
                    System.out.println("Введите абсолютный путь для нового файла:");
                    String pathToDecrypt = reader.readLine();
                    fileDecryptor = new FileDecryptor(Path.of(fileToDecrypt), Path.of(pathToDecrypt), complexity);
                    fileDecryptor.decryptFromFileToFile();
                } else if (task.equals("1")) {
                    fileDecryptor = new FileDecryptor(Path.of(fileToDecrypt), complexity);
                    fileDecryptor.decryptFromFileToConsole();
                } else {
                    System.out.println("Вы ввели неверную команду");
                }
            } else {
                System.out.println("Вы ввели неверную команду");
            }
        }
    }
}
