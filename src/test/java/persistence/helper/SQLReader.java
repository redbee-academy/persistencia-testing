package persistence.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class SQLReader {

    private static final String SEMICOLON = ";$";

    public static SQLReader ofSemicolon() {
        return new SQLReader(SEMICOLON);
    }

    private final String stmtDelimiterRegex;

    public SQLReader(String stmtDelimiterRegex) {
        this.stmtDelimiterRegex = stmtDelimiterRegex;
    }

    public List<String> readFile(String path) {
        try {
            return List.of(
                    Files.readAllLines(Path.of(path))
                            .stream()
                            .filter(line -> !line.isBlank() && !line.startsWith("--"))
                            .collect(Collectors.joining("\n"))
                            .split(stmtDelimiterRegex)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
