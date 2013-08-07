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

package org.hits.ui

/**
 *
 * @author rongji
 * The logic is based on GelInspector randomization logic
 */
class Randomization {
    static List getRandomizedSequence(List origSequence, int min1, int min2, int min3) {
                
        int minDist1=2
        int minDist2=2
        int minDist3=2
        if(min1){
            minDist1=min1  
        }
        if(min2){
            minDist2=min2
        }
        if(min3){
            minDist3=min3
        }
        
      
        def count=0
        def n=origSequence.size()
        boolean ready=false
        print origSequence
        println "minDist1: $minDist1 minDist2: $minDist2 minDist3: $minDist3"
        while(!ready){
            ready=true
            Collections.shuffle(origSequence)
           // print "shuffle $origSequence"
            for(int i=1; i<n; i++){
                if((origSequence.get(i)-origSequence.get(i-1)).abs()<minDist1){
                    
                    ready=false
                    break
                }
                if((i>=2)&&((origSequence.get(i)-origSequence.get(i-2)).abs()<minDist2)){
                    ready=false
                    break
                }
                if((i>=3)&&((origSequence.get(i)-origSequence.get(i-3)).abs()<minDist3)){
                    ready=false
                    break
                }
        
               
            }
          
            if(count>=100000){
                println "probably no solution exists for given options"
                break
            }
            count=count+1
        }
  
        print origSequence
        return origSequence

    }
}

