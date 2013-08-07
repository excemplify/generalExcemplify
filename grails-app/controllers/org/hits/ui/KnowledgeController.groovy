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

class KnowledgeController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {

    }

    def list() {
        def tempId=params.id
        if(tempId){
            def currentTemplateInstance=Template.get(tempId)
            def knowledgeList=currentTemplateInstance.knowledgeList
            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            [knowledgeInstanceList:knowledgeList, knowledgeInstanceTotal: knowledgeList.size(), templateName:currentTemplateInstance.templateName] 
        }else{
    
            redirect(uri:"/error")
        }
       

    }
    //
    //    def create() {
    //        [knowledgeInstance: new Knowledge(params)]
    //    }
    //
    //    def save() {
    //        def knowledgeInstance = new Knowledge(params)
    //        if (!knowledgeInstance.save(flush: true)) {
    //            render(view: "create", model: [knowledgeInstance: knowledgeInstance])
    //            return
    //        }
    //
    //		flash.message = message(code: 'default.created.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), knowledgeInstance.id])
    //        redirect(action: "show", id: knowledgeInstance.id)
    //    }
    //
    //    def show() {
    //        def knowledgeInstance = Knowledge.get(params.id)
    //        if (!knowledgeInstance) {
    //			flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        [knowledgeInstance: knowledgeInstance]
    //    }
    //
    //    def edit() {
    //        def knowledgeInstance = Knowledge.get(params.id)
    //        if (!knowledgeInstance) {
    //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        [knowledgeInstance: knowledgeInstance]
    //    }
    //
    //    def update() {
    //        def knowledgeInstance = Knowledge.get(params.id)
    //        if (!knowledgeInstance) {
    //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        if (params.version) {
    //            def version = params.version.toLong()
    //            if (knowledgeInstance.version > version) {
    //                knowledgeInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
    //                          [message(code: 'knowledge.label', default: 'Knowledge')] as Object[],
    //                          "Another user has updated this Knowledge while you were editing")
    //                render(view: "edit", model: [knowledgeInstance: knowledgeInstance])
    //                return
    //            }
    //        }
    //
    //        knowledgeInstance.properties = params
    //
    //        if (!knowledgeInstance.save(flush: true)) {
    //            render(view: "edit", model: [knowledgeInstance: knowledgeInstance])
    //            return
    //        }
    //
    //		flash.message = message(code: 'default.updated.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), knowledgeInstance.id])
    //        redirect(action: "show", id: knowledgeInstance.id)
    //    }
    //
    //    def delete() {
    //        def knowledgeInstance = Knowledge.get(params.id)
    //        if (!knowledgeInstance) {
    //			flash.message = message(code: 'default.not.found.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        try {
    //            knowledgeInstance.delete(flush: true)
    //			flash.message = message(code: 'default.deleted.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "list")
    //        }
    //        catch (DataIntegrityViolationException e) {
    //			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'knowledge.label', default: 'Knowledge'), params.id])
    //            redirect(action: "show", id: params.id)
    //        }
    //    }
}
