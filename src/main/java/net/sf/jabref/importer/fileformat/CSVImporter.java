/*  Copyright (C) 2003-2015 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.jabref.importer.fileformat;

import java.io.*;
import java.util.*;

import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.importer.fileformat.ImportFormat;
import net.sf.jabref.model.entry.BibEntry;
import net.sf.jabref.model.entry.BibtexEntryTypes;

/**
 * This importer exists only to enable `--importToOpen someEntry.bib`
 *
 * It is NOT intended to import a bib file. This is done via the option action, which treats the metadata fields
 * The metadata is not required to be read here, as this class is NOT called at --import
 */
public class CSVImporter extends ImportFormat {

    /**
     * @return true as we have no effective way to decide whether a file is in bibtex format or not. See
     *         https://github.com/JabRef/jabref/pull/379#issuecomment-158685726 for more details.
     */
    @Override
    public boolean isRecognizedFormat(InputStream in) throws IOException {
        String[] CamposHeader;
        BufferedReader BufR = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(in));
        String Linha = BufR.readLine();
        if (!(Linha.contains("&1515"))) {
            return false;
        }

        CamposHeader = Linha.split("&1515");

        if (CamposHeader[0] != "BibliographyType") {
            return false;
        }

        return true;
    }

    /**
     * Parses the given input stream.
     * Only plain bibtex entries are returned.
     * That especially means that metadata is ignored.
     *
     * @param in the inputStream to read from
     * @param status the OutputPrinter to put status to
     * @return a list of BibTeX entries contained in the given inputStream
     */
    @Override
    public List<BibEntry> importEntries(InputStream in, OutputPrinter status) throws IOException {
        //ParserResult pr = BibtexParser.parse(ImportFormatReader.getReaderDefaultEncoding(in));
        //return new ArrayList<>(pr.getDatabase().getEntries());
        List<BibEntry> BibItems = new ArrayList<>();
        BufferedReader BufR = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(in));

        String[] Campos;
        String[] CamposHeader;

        //Lê a primeira linha, para decidir as colunas especificadas no arquivo CSV
        String Linha = BufR.readLine();
        CamposHeader = Linha.split("&1515");
        /*int BibEntryT;

        for (BibEntryT = 0; BibEntryT < CamposHeader.length; i++)
        {
            if (CamposHeader[BibEntryT] == "BibliographyType")
                break;
        }*/

        //Lê as entradas do arquivo
        Linha = BufR.readLine();
        while (Linha != null) {
            if (!Linha.trim().isEmpty()) {
                Campos = Linha.split("&1515");
                BibEntry BibE = new BibEntry();
                BibE.setType(BibtexEntryTypes.TECHREPORT);
                switch (Campos[0]) {
                case "Book":
                    BibE.setType(BibtexEntryTypes.BOOK);
                    break;
                case "Booklet":
                    BibE.setType(BibtexEntryTypes.BOOKLET);
                    break;
                case "Proceedings":
                    BibE.setType(BibtexEntryTypes.PROCEEDINGS);
                    break;
                case "Incollection":
                    BibE.setType(BibtexEntryTypes.INCOLLECTION);
                    break;
                case "Inbook":
                    BibE.setType(BibtexEntryTypes.INBOOK);
                    break;
                case "Inproceedings":
                    BibE.setType(BibtexEntryTypes.INPROCEEDINGS);
                    break;
                case "Article":
                    BibE.setType(BibtexEntryTypes.ARTICLE);
                    break;
                case "Manual":
                    BibE.setType(BibtexEntryTypes.MANUAL);
                    break;
                case "Mastersthesis":
                    BibE.setType(BibtexEntryTypes.MASTERSTHESIS);
                    break;
                case "Conference":
                    BibE.setType(BibtexEntryTypes.CONFERENCE);
                    break;
                case "Misc":
                    BibE.setType(BibtexEntryTypes.MISC);
                    break;
                case "Phdthesis":
                    BibE.setType(BibtexEntryTypes.PHDTHESIS);
                    break;
                case "Techreport":
                    BibE.setType(BibtexEntryTypes.TECHREPORT);
                    break;
                case "Unpublished":
                    BibE.setType(BibtexEntryTypes.UNPUBLISHED);
                    break;
                }
                for (int i = 1; i < CamposHeader.length; i++) {
                    //Retirar aspas
                    Campos[i] = Campos[i].replace("\"", "");
                    //Assigna cada campo ao respectivo header
                    if (Campos[i] != "") {
                        BibE.setField(CamposHeader[i], Campos[i]);
                    }
                }
                BibItems.add(BibE);
                Linha = BufR.readLine();
            }
        }
        return BibItems;
    }

    @Override
    public String getFormatName() {
        return "CSV";
    }

    @Override
    public String getExtensions() {
        return "csv";
    }

}
