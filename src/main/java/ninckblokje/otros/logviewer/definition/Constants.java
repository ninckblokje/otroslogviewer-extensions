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

package ninckblokje.otros.logviewer.definition;

import java.util.Arrays;
import java.util.List;

/**
 * Definitions for constants.
 * 
 * @author ninckblokje
 */
public class Constants {
    private Constants() {}
    
    // for OC4J log.xml
    public static final String MESSAGE_ELEMENT_NAME = "MESSAGE";
    public static final String MSG_LEVEL_ELEMENT_NAME = "MSG_LEVEL";
    public static final String MSG_TXT_ELEMENT_NAME = "MSG_TEXT";
    public static final String SUPPL_DETAIL_ELEMENT_NAME = "SUPPL_DETAIL";
    public static final String TSTZ_ORIGINATING_ELEMENT_NAME = "TSTZ_ORIGINATING";
    
    public static final List<String> MAPPLABLE_ELEMENTS = Arrays.asList(new String[] {
        MSG_LEVEL_ELEMENT_NAME, MSG_TXT_ELEMENT_NAME,
        SUPPL_DETAIL_ELEMENT_NAME, TSTZ_ORIGINATING_ELEMENT_NAME
    });
    public static final String TSTZ_ORIGINATING_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
}
