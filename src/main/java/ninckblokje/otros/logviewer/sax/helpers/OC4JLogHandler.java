/*
 * Copyright (c) 2013, ninckblokje
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: 
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ninckblokje.otros.logviewer.sax.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jcip.annotations.NotThreadSafe;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import pl.otros.logview.LogData;
import pl.otros.logview.LogDataCollector;
import pl.otros.logview.parser.ParsingContext;

/**
 *
 * @author ninckblokje
 */
@NotThreadSafe
public class OC4JLogHandler extends DefaultHandler {

    private static final Logger LOGGER = Logger.getLogger(OC4JLogHandler.class.getName());
    
    private static final String MESSAGE_ELEMENT_NAME = "MESSAGE";
    private static final String MSG_TXT_ELEMENT_NAME = "MSG_TEXT";
    private static final String TSTZ_ORIGINATING_ELEMENT_NAME = "TSTZ_ORIGINATING";
    
    private static final List<String> MAPPLABLE_ELEMENTS = Arrays.asList(new String[] {MSG_TXT_ELEMENT_NAME, TSTZ_ORIGINATING_ELEMENT_NAME});
    private static final String TSTZ_ORIGINATING_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    
    private String currentElement = null;
    private LogData entry = null;
    private LogDataCollector ldc = null;
    private Boolean mapData = Boolean.FALSE;
    private ParsingContext pc = null;

    public OC4JLogHandler(LogDataCollector ldc, ParsingContext pc) {
        this.ldc = ldc;
        this.pc = pc;
    }

    protected Date parseDate(String value) throws ParseException {
        int startIndex = value.lastIndexOf(":");
        value = value.substring(0, startIndex) + value.substring(startIndex + 1);

        SimpleDateFormat format = new SimpleDateFormat(TSTZ_ORIGINATING_DATE_PATTERN);
        return format.parse(value);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if (mapData
                && currentElement != null) {
            String value = new String(ch, start, length);
            if (TSTZ_ORIGINATING_ELEMENT_NAME.equals(currentElement)) {
                try {
                    entry.setDate(parseDate(value));
                } catch (ParseException ex) {
                    LOGGER.log(Level.SEVERE, "ParseException while parsing date [" + value + "] from element [" + currentElement + "]", ex);
                }
            } else if (MSG_TXT_ELEMENT_NAME.equals(currentElement)) {
                entry.setMessage(value);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentElement = null;
        mapData = Boolean.FALSE;

        if (MESSAGE_ELEMENT_NAME.equals(localName)) {
            entry = null;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = localName;
        mapData = Boolean.FALSE;

        if (MESSAGE_ELEMENT_NAME.equals(localName)) {
            entry = new LogData();
            entry.setId(pc.getGeneratedIdAndIncrease());
            entry.setLogSource(pc.getLogSource());

            ldc.add(entry);
        } else if (MAPPLABLE_ELEMENTS.contains(currentElement)) {
            mapData = Boolean.TRUE;
        }
    }
}
