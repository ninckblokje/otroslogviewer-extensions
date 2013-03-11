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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jcip.annotations.NotThreadSafe;
import ninckblokje.otros.logviewer.definition.Constants;
import ninckblokje.otros.logviewer.definition.OC4JLogLevelEnum;
import org.apache.commons.lang.StringUtils;
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

        SimpleDateFormat format = new SimpleDateFormat(Constants.TSTZ_ORIGINATING_DATE_PATTERN);
        return format.parse(value);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if (mapData
                && currentElement != null) {
            LOGGER.log(Level.FINEST, "Found value [{0}] for element [{1}]", new Object[] {value, currentElement});
            
            if (Constants.TSTZ_ORIGINATING_ELEMENT_NAME.equals(currentElement)) {
                try {
                    entry.setDate(parseDate(value));
                } catch (ParseException ex) {
                    LOGGER.log(Level.SEVERE, "ParseException while parsing date [" + value + "] from element [" + currentElement + "]", ex);
                }
            } else if (Constants.MSG_TXT_ELEMENT_NAME.equals(currentElement)) {
                if(!StringUtils.isEmpty(entry.getMessage())) {
                    value = entry.getMessage() + System.lineSeparator() + value;
                }
                entry.setMessage(value);
            } else if (Constants.SUPPL_DETAIL_ELEMENT_NAME.equals(currentElement)) {
                if(!StringUtils.isEmpty(entry.getMessage())) {
                    value = entry.getMessage() + System.lineSeparator() + value;
                }
                entry.setMessage(value);
            } else if (Constants.MSG_LEVEL_ELEMENT_NAME.equals(currentElement)) {
                entry.setLevel(OC4JLogLevelEnum.getEnum(Integer.valueOf(value)).getLevel());
            }
        } else {
            LOGGER.log(Level.FINEST, "Ignoring value [{0}] for element [{1}]", new Object[] {value, currentElement});
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        LOGGER.log(Level.FINER, "End element with URI [{0}], local name [{1}] and QName [{2}]", new Object[] {uri, localName, qName});
        
        if (Constants.MESSAGE_ELEMENT_NAME.equals(currentElement)) {
            entry = null;
        }
        
        currentElement = null;
        mapData = Boolean.FALSE;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        LOGGER.log(Level.FINER, "Start element with URI [{0}], local name [{1}] and QName [{2}]", new Object[] {uri, localName, qName});
        
        currentElement = (StringUtils.isEmpty(localName)) ? qName : localName;
        mapData = Boolean.FALSE;

        if (Constants.MESSAGE_ELEMENT_NAME.equals(currentElement)) {
            entry = new LogData();
            entry.setId(pc.getGeneratedIdAndIncrease());
            entry.setLogSource(pc.getLogSource());

            ldc.add(entry);
        } else if (Constants.MAPPLABLE_ELEMENTS.contains(currentElement)) {
            mapData = Boolean.TRUE;
        }
    }
}
