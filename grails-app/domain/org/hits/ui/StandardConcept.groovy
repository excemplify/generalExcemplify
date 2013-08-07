package org.hits.ui

class StandardConcept {
String concept

    static constraints = {
        concept(validator: {concept->
       if(concept.indexOf(" ")!=-1){
      
        return propertyName == "StandardConcept"
       }
     })
    }
}
