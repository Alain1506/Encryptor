import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Encryptor {

    public static FileEncryptor fileEncryptor = null;
    public static FileDecryptor fileDecryptor = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("Выберите операцию:"
                    + "\n0 - зашифровать ресурс в файл"
                    + "\n1 - расшифровать файл");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                String command = reader.readLine();

                if (command.equals("0")) {
                    //зашифровать
                    int complexity = 0;
                    while (complexity < 3 || complexity > 5) {
                        System.out.println("Выберите сложность шифрования от 3 до 5:");
                        complexity = Integer.parseInt(reader.readLine());
                    }
                    System.out.println("Введите абсолютный путь для нового файла:");
                    String fileWithResult = reader.readLine();
                    System.out.println("Введите абсолютный путь для файла-ключа:");
                    String keyStorageFile = reader.readLine();

                    while (keyStorageFile == null) {
                        System.out.println("Введите корректный путь к файлу-ключу:");
                        keyStorageFile = reader.readLine();
                    }

                    while (true) {
                        System.out.println("Выберите источник ввода:"
                                + "\n0 - файл"
                                + "\n1 - текст, введенный в консоль");
                        String source = reader.readLine();

                        if (source.equals("0")) {
                            System.out.println("Введите абсолютный путь файла, который нужно зашифровать:");
                            String fileWithSourse = reader.readLine();
                            while (!Files.exists(Path.of(fileWithSourse))) {
                                System.out.println("Данного файла не существует, введите корректный путь к файлу:");
                                fileWithSourse = reader.readLine();
                            }
                            FileReader in = new FileReader(Path.of(fileWithSourse).toFile());
                            FileWriter out = new FileWriter(Path.of(fileWithResult).toFile());
                            fileEncryptor = new FileEncryptor();
                            fileEncryptor.encrypt(in, out, complexity, keyStorageFile);
                        } else if (source.equals("1")) {
                            System.out.println("Введите текст для шифрования:");
                            String consoleEntry = reader.readLine();
                            InputStreamReader in = new InputStreamReader(new ByteArrayInputStream(consoleEntry.getBytes(StandardCharsets.UTF_8)));
                            FileWriter out = new FileWriter(Path.of(fileWithResult).toFile());
                            fileEncryptor = new FileEncryptor();
                            fileEncryptor.encrypt(in, out, complexity, keyStorageFile);
                        } else {
                            System.out.println("Вы ввели неверную команду");
                            continue;
                        }
                        break;
                    }

                } else if (command.equals("1")) {
                    // расшифровать
                    System.out.println("Введите абсолютный путь файла, который нужно расшифровать:");
                    String encodedSource = reader.readLine();
                    while (!Files.exists(Path.of(encodedSource))) {
                        System.out.println("Данного файла не существует, введите корректный путь к файлу:");
                        encodedSource = reader.readLine();
                    }
                    System.out.println("Введите абсолютный путь файла-ключа:");
                    String fileWithKeys = reader.readLine();

                    while (fileWithKeys == null || !Files.exists(Path.of(fileWithKeys))) {
                        System.out.println("Введите корректный путь к файлу-ключу:");
                        fileWithKeys = reader.readLine();
                    }

                    while (true) {
                        System.out.println("Выберите операцию:"
                                + "\n0 - расшифровать файл в новый файл"
                                + "\n1 - расшифровать файл в консоль");
                        String task = reader.readLine();

                        if (task.equals("0")) {
                            System.out.println("Введите абсолютный путь для нового файла:");
                            String fileWithResult = reader.readLine();

                            FileReader in = new FileReader(Path.of(encodedSource).toFile());
                            FileWriter out = new FileWriter(Path.of(fileWithResult).toFile());
                            fileDecryptor = new FileDecryptor();
                            fileDecryptor.decrypt(in, out, fileWithKeys);
                        } else if (task.equals("1")) {
                            FileReader in = new FileReader(Path.of(encodedSource).toFile());
                            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
                            fileDecryptor = new FileDecryptor();
                            fileDecryptor.decrypt(in, out, fileWithKeys);
                        } else {
                            System.out.println("Вы ввели неверную команду");
                            continue;
                        }
                        break;
                    }
                } else {
                    System.out.println("Вы ввели неверную команду");
                    continue;
                }
            } catch (IOException e) {
                System.out.println("Возникла ошибка ввода-вывода: " + e.getMessage());
            }
            break;
        }
    }
}
