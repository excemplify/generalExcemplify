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
package org.hits.parser.core

import org.hits.parser.core.StateAndQueue


/**
 *
 * @author jongle
 * This class essentially defines an interface for a very general parser
 */
interface Parser {
	
    def source
    def target
    Action action
    
    
    //method to do the actual parsing - return true if successful, false otherwise with reason added to the state mapping
    public StateAndQueue parse(StateAndQueue state)
    
    public configure(StateAndQueue state, Map configuration)
    //method to define a region for a parser, whatever that might mean
   // public defineRegion()
    
    public setSource(Source source)
    
    public setAction(Action action)
    
    public setTarget(Target target)
    
   
    
}

