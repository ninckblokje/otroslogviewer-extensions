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
package ninckblokje.otros.logviewer.importer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import ninckblokje.otros.logviewer.DummyLogDataCollector;
import ninckblokje.otros.logviewer.definition.Constants;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.otros.logview.LogData;
import pl.otros.logview.LogDataCollector;
import pl.otros.logview.parser.ParsingContext;

/**
 *
 * @author ninckblokje
 */
public class TestOC4JLogImporter {

    @BeforeClass
    public static void initClass() {
        System.setProperty("java.util.logging.config.file", "src/test/config/logging.properties");
    }

    @Test
    public void test1() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/test1.xml");

        LogDataCollector collector = new DummyLogDataCollector();

        OC4JLogImporter importer = new OC4JLogImporter();
        importer.importLogs(is, collector, new ParsingContext());

        is.close();

        Assert.assertNotNull(collector.getLogData());
        Assert.assertEquals(1, collector.getLogData().length);
        
        LogData data = collector.getLogData()[0];
        Assert.assertNotNull(data);
        Assert.assertEquals("Deleted log file: log1274.xml, size = 10483777 bytes", data.getMessage());
        
        Assert.assertNotNull(data.getLevel());
        Assert.assertEquals(Level.FINEST, data.getLevel());
        
        SimpleDateFormat format = new SimpleDateFormat(Constants.TSTZ_ORIGINATING_DATE_PATTERN);
        Assert.assertNotNull(data.getDate());
        Assert.assertEquals("2013-01-26T06:01:19.194+0100", format.format(data.getDate()));
    }
    
    @Test
    public void test2() throws Exception {
        InputStream is = new FileInputStream("src/test/resources/test2.xml");

        LogDataCollector collector = new DummyLogDataCollector();

        OC4JLogImporter importer = new OC4JLogImporter();
        importer.importLogs(is, collector, new ParsingContext());

        is.close();

        Assert.assertNotNull(collector.getLogData());
        Assert.assertEquals(1, collector.getLogData().length);
        
        LogData data = collector.getLogData()[0];
        Assert.assertNotNull(data);
        Assert.assertEquals("Failed to commit transaction" + System.lineSeparator(), data.getMessage());
        
        Assert.assertNotNull(data.getLevel());
        Assert.assertEquals(Level.SEVERE, data.getLevel());
        
        SimpleDateFormat format = new SimpleDateFormat(Constants.TSTZ_ORIGINATING_DATE_PATTERN);
        Assert.assertNotNull(data.getDate());
        Assert.assertEquals("2013-01-26T06:01:19.040+0100", format.format(data.getDate()));
    }
}
