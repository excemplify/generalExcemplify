/* ===================================================
 * Copyright 2010-2013 HITS gGmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ========================================================== */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser.excelimp

import org.hits.parser.core.*
import org.hits.parser.textimp.*

/**
 *
 * @author jongle
 */
class ParserFactory {
	
    public static Parser getParser(StateAndQueue state, Map configurations){
       
        switch(configurations.parserType){    //parserType->parserConfigType
        case "GeneralRecordParser":
            Parser parser= new GeneralRecordParser(name:configurations.name)//putting of the sheet number in here configuration in here
            parser.configure(state, configurations)   
            return parser
        case "GeneralBlockParser":
            Parser parser= new GeneralBlockParser(name:configurations.name)//putting of the sheet number in here configuration in here
            parser.configure(state, configurations)   
            return parser
            
            /*
             *for special case as immunoblot
             */    
            //        case "RawDataExcelParser":
            //            Parser parser= new RawDataExcelParser(name:configurations.nextStageName)//putting of the sheet number in here configuration in here
            //            parser.configure(state, configurations)
            //            return parser
            //        case "NonExcelRawDataParser":
            //            Parser parser= new NonExcelRawDataParser(name:configurations.nextStageName)//putting of the sheet number in here configuration in here
            //            parser.configure(state, configurations)
            //            return parser
            //        case "SetupExcelParser":
            //            Parser parser= new SetupExcelParser(name:configurations.nextStageName)//putting of the sheet number in here configuration in here
            //            parser.configure(state, configurations)
            //            return parser
        }
    }
    
}

