package IDS04;

import com.sun.xml.internal.ws.spi.db.FieldSetter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ZiPBombsTest {

    /**
     * Η μέθοδος προσπαθεί να κάνει uncompress το zip αρχείο σε ένα αρχείο με όνομα dest.
     * Zip bomb επίθεση.Προσχοη!Η μέθοδος μπορεί να πάρει μερικά λεπτά για να τελειώσει,ανάλογα με το
     * σύστημα.
     * @throws IOException
     */
    @Test
    @Disabled("Αυτό το test είναι disabled by default επειδή απαιτεί πολύ χρόνο και 8+gb μνήμης στον σκληρό δίσκο.")
    public void dOsAttackWithZipBomb() throws IOException {
        long str = System.nanoTime();
        long bytes = 0;
        try(
                ZipInputStream zipIn = new ZipInputStream(
                        new BufferedInputStream(Files.newInputStream(Paths.get("userDir/zipBomb.zip").toRealPath()))
                );

                BufferedOutputStream dest = new BufferedOutputStream(
                        Files.newOutputStream(Paths.get("userDir/dest").toRealPath())
                )

        ) {
            for (ZipEntry zipEntry = zipIn.getNextEntry(); zipEntry != null; zipEntry = zipIn.getNextEntry()) {
                System.out.println("Extracting : "+zipEntry);
                byte[] data = new byte[1000];//1000 bytes of uncompressed data

                while (zipIn.read(data,0,1000) != -1) {
                    bytes += 1000;
                    dest.write(data);
                }

            }
        }finally {
            long end = System.nanoTime();

            System.out.println("Took : "+(end - str )+" nanoseconds.");
            System.out.println("Or : "+ TimeUnit.NANOSECONDS.toSeconds(end - str )+" seconds.");
            System.out.println("The extracted data took : " + bytes + " byte/s on the hard dist.");
            System.out.println("Or:"+bytes/(1000000*1000)+" gigabyte/s");
        }



    }

    /**
     * Το ZipEntry έχει μέθοδο getSize που επιστρέφει το μέγεθος(uncompressed)του αρχείου.
     * @throws IOException
     */
    @Test
    public void solution() throws IOException {
        long MAXIMUM_SIZE = 1000 * 1024;//1Megabyte limit

        try(
                ZipInputStream zipIn = new ZipInputStream(
                        new BufferedInputStream(Files.newInputStream(Paths.get("userDir/zipBomb.zip").toRealPath()))
                );

                BufferedOutputStream dest = new BufferedOutputStream(
                        Files.newOutputStream(Paths.get("userDir/dest").toRealPath())
                )

        ) {
            for (ZipEntry zipEntry = zipIn.getNextEntry(); zipEntry != null; zipEntry = zipIn.getNextEntry()) {
                final ZipEntry entry  = zipEntry;

                IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {

                    if (entry.getSize() > MAXIMUM_SIZE) {
                        throw new IllegalArgumentException("The zip entry is too large." +
                                "Size = " + entry.getSize() / (1000 * 1024) + " megabytes.");
                    }

                });

                if (ex != null) {
                    System.out.println("Error:"+ex.getMessage());
                    break;
                }

                System.out.println("Extracting : "+zipEntry);

                byte[] data = new byte[1000];//1000 bytes of uncompressed data

                while (zipIn.read(data,0,1000) != -1) {
                    dest.write(data);
                }

            }
        }


    }



}
