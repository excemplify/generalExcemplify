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


/**
 * A Block is part of a parser.
 * It parses its assigned region.
 * 
 * It maintains a call stack that can be used later in order to find
 * out which parsers are going to be called.
 * 
 * 
 * @author muellewg
 */
class Block {
    Parser parser
    String name;
    Block right;
    Block down;
    
    /**
     * Parse this object at a given region.
     */
    def parseThis = {StateAndQueue state ->
        // do nothing, but be successful
       println "I am parsing this ${name}: ${this}";  
        state=parser.parse(state)       
        //if (state.state.success == true;
       // println state.state
  
        
        return state;
    }
    
    def parserConfig = {StateAndQueue state, Map configuration ->
        parser.configure(state,configuration)}
    
    /**
     * First traverse the tree and build a queue.
     * Then simply traverse the queue one by one.
     */
    public buildQueue(StateAndQueue state){
        
        state.queue << this.parseThis; 
        right?.buildQueue(state);
        down?.buildQueue(state);
    }
    
    /**
     * Now apply the sequence 
     */
    public applyQueue(StateAndQueue state){

        boolean success = false;
        
        int popcount = 0;
        
        println "applyingQueue"
        state.queue.each{
            
         
            
            it.call(state)
            //println state.state
            //println state.queue
            
            if (state.state.success==true){
                println "need to pop"
                ++popcount;
                
            }
            else {
                throw new ParsingException("what happened?")
              //  while (!success){
                //throw exception, ask question and then resume...
              //  println "something went wrong...continue?"
             //   this.fix(state)
             //   if (state.state.fixed==true) {
              //          println "fixed"
              //          success=true
              //          println "need to pop"
              //          ++popcount;
              //  }
              //  else {
              //      println "not fixed yet"
                }
               
                }
            }
        
    
       
    // simulates a "fixing" of the parsing in reality this requires some user interaction probably
    public fix(StateAndQueue state){
        
        def rand = new Random()
        def nextrand = rand.nextInt(100)
        println nextrand 
        if (nextrand>50) state.state.fixed=true
        
        return state
    } 
       // popcount.times{
        //    println "popping"
         //   ((List)state.queue).remove(0);
       // }
    
    
    
}

