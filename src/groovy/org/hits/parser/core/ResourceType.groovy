/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser.core

/**
 *
 * @author rongji
 */
enum ResourceType{
    TRACE("TRACE"), CURRWORKBOOK("CURRWORKBOOK"), SIMPLE("SIMPLE")
   
    
    private String type
    
    ResourceType(String type){
        this.type=type
    }
    
    def getType(){
        return type
    }
}



