package net.sf.jabref.importer.fileformat;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;

public class CSVImporterTest {

    private CSVImporter importer;


    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new CSVImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = CSVImporterTest.class.getResourceAsStream("ric.csv")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testImportEntries() throws IOException {
        try (InputStream stream = CSVImporterTest.class.getResourceAsStream("ric.csv")) {
            List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(2, bibEntries.size());

            for (BibEntry entry : bibEntries) {
                if (entry.getCiteKey().equals("ricardo")) {
                    assertEquals("ricardo", entry.getField("bibtexkey"));
                    assertEquals("Ricardo Szram", entry.getField("author"));
                    assertEquals("Estruturas de Dados: abordagem incial", entry.getField("title"));
                    assertEquals("2019", entry.getField("year"));
                    assertEquals("Simpósio de Guarulhos", entry.getField("journal"));
                } else if (entry.getCiteKey().equals("vitao")) {
                    assertEquals("vitao", entry.getField("bibtexkey"));
                    assertEquals("Vitor Hugo Chaves", entry.getField("author"));
                    assertEquals("Detecção de leucócitos em vídeo", entry.getField("title"));
                    assertEquals("2019", entry.getField("year"));
                    assertEquals("Simpósio de São Carlos", entry.getField("journal"));
                }
            }

        }

    }

    @Test
    public void testGetFormatName() {
        assertEquals("CSV", importer.getFormatName());
    }

    @Test
    public void testGetExtensions() {
        assertEquals("csv", importer.getExtensions());
    }

}
