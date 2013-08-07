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
package org.hits.ui

import org.springframework.dao.DataIntegrityViolationException
import org.hits.parser.*

class TemplateController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [templateInstanceList: Template.list(params), templateInstanceTotal: Template.count()]
     
    }

    def create() {
        [templateInstance: new Template(params)]
    }

    def save() {
        def templateInstance = new Template(params)
        if (!templateInstance.save(flush: true)) {
            render(view: "create", model: [templateInstance: templateInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'template.label', default: 'Template'), templateInstance.id])
        redirect(action: "show", id: templateInstance.id)
    }

    def show() {
        def templateInstance = Template.get(params.id)
        if (!templateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "list")
            return
        }

        [templateInstance: templateInstance]
    }

    def edit() {
        def templateInstance = Template.get(params.id)
        if (!templateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "list")
            return
        }

        [templateInstance: templateInstance]
    }

    def update() {
        def templateInstance = Template.get(params.id)
        if (!templateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (templateInstance.version > version) {
                templateInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                    [message(code: 'template.label', default: 'Template')] as Object[],
                          "Another user has updated this Template while you were editing")
                render(view: "edit", model: [templateInstance: templateInstance])
                return
            }
        }

        templateInstance.properties = params

        if (!templateInstance.save(flush: true)) {
            render(view: "edit", model: [templateInstance: templateInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'template.label', default: 'Template'), templateInstance.id])
        redirect(action: "show", id: templateInstance.id)
    }

    def display(){
        Template templateInstance = Template.get(params.id)  
        if(templateInstance){
            templateInstance.visible=true
            templateInstance.save(flush: true)
        }  else{
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])

        }   
        render(text: """<script type="text/javascript"> refreshTableSorter(); </script>""", contentType: 'text/javascript')
        render(template:"/template/list", model: [templateInstanceList:Template.list()])
    }
    
    def stopDisplay(){
        Template templateInstance = Template.get(params.id)  
        if(templateInstance){
            templateInstance.visible=false
            templateInstance.save(flush: true)
        }  else{
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])

        }   
        render(text: """<script type="text/javascript"> refreshTableSorter(); </script>""", contentType: 'text/javascript')
        render(template:"/template/list", model: [templateInstanceList:Template.list()])
    }
    

    def download={
        def templateInstance = Template.get(params.id)

        try {

            response.setContentType("application/vnd.ms-excel")
            response.setHeader("Content-disposition", "attachment;filename=${templateInstance.templateName}")
            response.outputStream << templateInstance.binaryFileData

        }
        catch(Exception ex){
            redirect(uri:"/lab")
            //  redirect(action: "list")
        }

    }

   
    
    def deleteTemplate(){
     
      
        Template templateInstance = Template.get(params.id)  
       
        if(templateInstance){
            def templateInstanceName=templateInstance.templateName
            def category="${templateInstanceName.toString().tokenize(".").first()}Concepts"
            def existingCategory=UIConcepts.findByCategory(category)
 
            if(templateInstance.type=="public"&& !Stage.findAllByStageTemplate(templateInstance)){       
                println "yes you can delete this template"
                   if(existingCategory){
                        def r = []
                        r += existingCategory.concepts
                        r.each{concept->
                            existingCategory.removeFromConcepts(concept)
                            concept.delete()        
                        }
                        existingCategory.delete(flush:true)
                    log.info "delete $existingCategory.id"
                    }
                def k = []
                k += templateInstance.knowledgeList
                k.each{knowledge->
                    templateInstance.removeFromKnowledgeList(knowledge)
                    knowledge.delete()   
                    log.info "delete template knowledge ${knowledge.knowledgeName}" 
                }
                templateInstance.delete()
                   
                log.info "delete ${params.id} template"
                render(text: """<script type="text/javascript"> alert("$templateInstanceName is deleted!"); </script>""", contentType: 'text/javascript')
             
            }else{        
                render(text: """<script type="text/javascript"> warning('You can not delete it because some existing experiments are using such template!'); </script>""", contentType: 'text/javascript')         
            }   
           
      
        }  else{
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])

        }   
        render(text: """<script type="text/javascript"> refreshTableSorter(); </script>""", contentType: 'text/javascript')
        render(template:"/template/list", model: [templateInstanceList:Template.list()])
    
      
    }
    def delete() {
        def templateInstance = Template.get(params.id)
        if (!templateInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "list")
            return
        }

        try {
            templateInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'template.label', default: 'Template'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
