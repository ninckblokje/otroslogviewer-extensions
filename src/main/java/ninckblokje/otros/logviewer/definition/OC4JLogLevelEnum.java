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

import java.util.logging.Level;

/**
 * Enumeration for mapping log level values from the OC4J log.xml to a {@link java.util.logging.Level}.
 * 
 * @author ninckblokje
 */
public enum OC4JLogLevelEnum {
    
    FINEST(7),
    FINER(6),
    FINE(5),
    CONFIG(4),
    INFO(3),
    WARNING(2),
    SEVERE(1);
    
    private final int value;
    
    private OC4JLogLevelEnum(int value) {
        this.value = value;
    }

    public Level getLevel() {
        return Level.parse(this.name());
    }

    public int getValue() {
        return value;
    }
    
    public static OC4JLogLevelEnum getEnum(int value) {
        if(SEVERE.value == value) {
            return SEVERE;
        } else if(WARNING.value == value) {
            return WARNING;
        } else if(INFO.value == value) {
            return INFO;
        } else if(CONFIG.value == value) {
            return CONFIG;
        } else if(FINE.value == value) {
            return FINE;
        } else if(FINER.value == value) {
            return FINER;
        }
        
        return FINEST;
    }
}
