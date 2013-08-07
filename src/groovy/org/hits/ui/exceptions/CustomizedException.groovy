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

package org.hits.ui.exceptions

/**
 *
 * @author rongji
 */
class CustomizedException extends Exception{
    String mistake
    CustomizedAction action
    
    public CustomizedException ()
    {
        super();             // call superclass constructor
        mistake = "unknown";
        action=new CustomizedAction("do no action")
    }
  

    //-----------------------------------------------
    // Constructor receives some kind of message that is saved in an instance variable.

    public CustomizedException (String err)
    {
        super(err);     // call super class constructor
        mistake = err;  // save message
        action=new CustomizedAction("do no action")
    }
  
    public CustomizedException (String err,  givenaction)
    {
        super(err);     // call super class constructor
        mistake = err;  // save message
        action=givenaction
    }
    //------------------------------------------------  
    // public method, callable by exception catcher. It returns the error message.

    public String getMessage()
    {
        return mistake;
    }
    
    public String getAction()
    {
        return action;
    }
    
    public setAction(givenaction){
        action=givenaction
    }
}

