package com.eazytec.webapp.taglib;
/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

import java.text.Format;
import java.text.NumberFormat;

/** Formats a {@link Number} instance using the current {@link java.util.Locale} 
  * and the Percent {@link NumberFormat}.
  *
  * @author James Strachan
  * @version $Revision$
  */
public class FormatPercentTag extends FormatTagSupport {
    
    protected static final String _tagname = "i18n:formatPercent";

    // Implementation methods
    //-------------------------------------------------------------------------
    protected Format getFormat() {
        return NumberFormat.getPercentInstance( getLocale() );
    }
}