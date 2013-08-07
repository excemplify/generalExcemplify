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
package org.hits.parser

import org.hits.parser.SecUser

class User extends SecUser {

    static constraints = {
       // login(unique:true)
      //  password(password:true)
        name()
        seekEmailAddress(email: true, blank:true, nullable:true)
    }
    
   // String login
    String name
    String seekEmailAddress
   // String password
    
    String toString(){
        name
    }
}
