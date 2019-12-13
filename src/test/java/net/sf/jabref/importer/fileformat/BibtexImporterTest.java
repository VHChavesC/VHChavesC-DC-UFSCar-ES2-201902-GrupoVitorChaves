package net.sf.jabref.importer.fileformat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.OutputPrinterToNull;
import net.sf.jabref.model.entry.BibEntry;

/**
 * This class tests the BibtexImporter.
 * That importer is only used for --importToOpen, which is currently untested
 * <p>
 * TODO:
 * 1. Add test for --importToOpen
 * 2. Move these tests to the code opening a bibtex file
 */
public class BibtexImporterTest {

    private BibtexImporter importer;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        importer = new BibtexImporter();
    }

    @Test
    public void testIsRecognizedFormat() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("BibtexImporter.examples.bib")) {
            assertTrue(importer.isRecognizedFormat(stream));
        }
    }

    @Test
    public void testGetFormatName() {
        assertEquals("BibTeX", importer.getFormatName());
    }

    @Test
    public void testGetExtensions() {
        assertEquals("bib", importer.getExtensions());
    }

    ///////////////////////// CASOS TESTES INSERIDOS ///////////////////////////
    @Test
    public void testImportTest1() throws IOException {
        try (InputStream stream = BibtexImporterTest.class.getResourceAsStream("test2.bib")) {
            List<BibEntry> bibEntries = importer.importEntries(stream, new OutputPrinterToNull());

            assertEquals(2, bibEntries.size());

            for (BibEntry entry : bibEntries) {

                if (entry.getCiteKey().equals("Coli2016")) {
                    assertEquals("Coli, João", entry.getField("author"));
                    assertEquals("Estamos testando", entry.getField("title"));
                    assertEquals("C", entry.getField("address"));
                    assertEquals("123a", entry.getField("year"));
                    assertEquals("RITA", entry.getField("publisher"));
                    assertEquals("coelho", entry.getField("keywords"));
                    assertEquals("CC, 123 Senta", entry.getField("pages"));
                    assertEquals("https://www.facebook.com/jgabriel.coli", entry.getField("URL"));
                    assertEquals("2.0, sei la", entry.getField("edition"));
                    assertEquals("123456789", entry.getField("ISBN"));
                } else if (entry.getCiteKey().equals("Leo1999")) {
                    assertEquals("Leo, Leo", entry.getField("author"));
                    assertEquals(null, entry.getField("title"));
                    assertEquals(null, entry.getField("journaltitle"));
                    assertEquals(null, entry.getField("date"));
                    assertEquals(null, entry.getField("volume"));
                    assertEquals(null, entry.getField("number"));
                    assertEquals(null, entry.getField("pages"));
                    assertEquals(null, entry.getField("indextitle"));
                }
            }
        }
    }

}
