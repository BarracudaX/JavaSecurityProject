package IDS02;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Για περισσότερες πληροφορίες,
 * https://docs.oracle.com/javase/tutorial/essential/io/
 */
public class FilePathNames {

    /**
     * Directory Traversal attack που χρησιμοποιεί ../ για να ξεφύγει από το προκαθορισμένο χώρο εργασίας.
     * @throws IOException
     */
    @Test
    public void directoryTraversalVulnerabilityAttack() throws IOException {
        Path path = Paths.get(directoryTraversalVulnerability());

        System.out.println(path.toString());

        if (!isInSecureDirectory(path.toString())) {
            throw new IllegalArgumentException("You are out of the secure directory.");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                System.out.println(line);
            }
        }

    }

    /**
     * Directory Traversal Solution with Path class.
     */
    @Test
    public void directoryTraversalSolution() throws IOException {
        Path path = Paths.get(directoryTraversalVulnerability());
        System.out.println("Before normalization : " + path.toString());

//        path = path.normalize();
        //ή
//        path = path.toAbsolutePath();
        //ή
        path = path.toRealPath();


        System.out.println("After normalization : " + path.toString());

        String pathToCheck = path.toString();
        assertThrows(
                IllegalArgumentException.class,
                () -> {
                    if (!validate(pathToCheck)) {
                        throw new IllegalArgumentException("Trying to see passwords.txt file.");
                    }
        });
    }

    /**
     * equivalence path mame attack που περνάει το validation με τη χρήση soft link.
     * @throws IOException
     */
    @Test
    @Disabled("Πριν τρεξετε το test,δημιουργήστε το symbolic link userDir/symbolicLink")
    public void equivalencePathNameAttackThatPassBothValidationAndIsSecureChecks() throws IOException {
        Path path = Paths.get(equivalentPathNameVulnerability()).toAbsolutePath();

        System.out.println(path.toString());

        if (!isInSecureDirectory(path.toString())) {
            throw new IllegalArgumentException("You are out of the secure directory.");
        } else if (!validate(path.toString())) {
            throw new IllegalArgumentException("Trying to see passwords.txt");
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                System.out.println(line);
            }
        }
    }

    @Test
    public void equivalencePathNameAttackSolution() throws IOException {
        Path path = Paths.get(equivalentPathNameVulnerability());

        System.out.println("Before call to toRealPath:" + path.toString());

        Path realPath = path.toRealPath();

        System.out.println("After call to toRealPath:" + realPath.toString());


        assertThrows(IllegalArgumentException.class,() -> {
            if (!validate(realPath.toString())) {
                throw new IllegalArgumentException("Trying to see passwords.txt");
            }
        });

    }

    /**
     * Ελέγχει ότι το path name δεν περιέχει κομμάτι που αναφέρεται στο passwords/passwords.txt.
     * Μπορεί εύκολα να ξεπεραστεί με τη δημιουργία των soft links
     * @param absolutePath
     * @return
     */
    private boolean validate(String absolutePath) {
        return !Pattern.compile(".*passwords\\\\passwords.txt*").matcher(absolutePath).matches();
    }

    /**
     * Ελέγχει ότι ο χρήστης δουλέυει μέσα στο secure directory,αλλά μπορεί εύκολα να ξεπεραστεί με
     * directory traversal vulnerability.
     * @param absolutePath
     * @return
     */
    private boolean isInSecureDirectory(String absolutePath) {
        return Pattern.compile(".*userDir\\\\.*").matcher(absolutePath).matches();
    }

    private String directoryTraversalVulnerability(){
        return "userDir/../passwords/passwords.txt";
    }

    public String equivalentPathNameVulnerability() {
        return "userDir/symbolicLink";
    }

}
