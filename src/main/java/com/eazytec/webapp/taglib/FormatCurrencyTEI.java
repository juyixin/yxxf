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

import javax.servlet.jsp.tagext.*;

public class FormatCurrencyTEI extends TagExtraInfo
{
    public VariableInfo [] getVariableInfo (TagData data)
    {
        String varName = data.getId();
        if ( varName == null ) {
            return new VariableInfo[] {};
        } else {
            return new VariableInfo [] {
                new VariableInfo(varName,
                             "java.lang.String",
                             true,
                             VariableInfo.AT_END)
            };
        }
    }

}
