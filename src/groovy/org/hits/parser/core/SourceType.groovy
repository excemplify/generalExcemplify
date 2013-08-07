/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hits.parser.core

/**
 *
 * @author rongji
 */
enum SourceType{
    CONCAT("CONCAT"), OUTERPRODUCT("OUTERPRODUCT"),TRACE("TRACE"), CURRWORKBOOK("CURRWORKBOOK"), SIMPLE("SIMPLE"),OUTERPRODUCTALLOWNULL("OUTERPRODUCTALLOWNULL"),   //old ones by lenneke
    LISTING("LISTING"),LABEL("LABEL"),LISTINGANDLABEL("LISTINGANDLABEL")
    
    private String type
    
    SourceType(String type){
        this.type=type
    }
    
    def getType(){
        return type
    }
}

