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
import org.hits.parser.core.StateAndQueue;
import org.hits.parser.core.Block;
/**
 *
 * @author muellewg
 */
public class ReParse {
    public static void main(String[] args){
        //Block block = new Block(name: "root", parser:new SimpleParser());
        Block block = new Block(name: "root")
        Block failBlock = new FailAllwaysBlock(name: "rfailBlock");
        block.right=failBlock
        Block dblock = new Block(name: "downblock")
        block.down=dblock
        Block anotherBlock = new Block(name: "anotherBlock")
        failBlock.right=anotherBlock
        
        StateAndQueue stateAndQueue = new StateAndQueue();
        
        block.buildQueue(stateAndQueue);
        
       
        block.applyQueue(stateAndQueue);
        println stateAndQueue.state
        println stateAndQueue.queue
    }
}

