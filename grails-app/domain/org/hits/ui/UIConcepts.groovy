package org.hits.ui

class UIConcepts {
    String category   //unique
    List concepts   
    static hasMany=[concepts:StandardConcept]
    static constraints = {
        category(unique:true)
    }
}
