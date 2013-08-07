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
package org.hits.parser

import org.springframework.dao.DataIntegrityViolationException

class SecUserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
             redirect(uri: "/user/list")
       // redirect(action: "list", params: params)
    }

    def list() {
            redirect(uri: "/user/list")
    
//        params.max = Math.min(params.max ? params.int('max') : 10, 100)
//        [secUserInstanceList: SecUser.list(params), secUserInstanceTotal: SecUser.count()]
    }

    def create() {
        [secUserInstance: new SecUser(params)]
    }

    def save() {
              def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
        if(!SecUser.findByUsername(params.username)){
            def secUserInstance = new SecUser(params)       
            if (secUserInstance.save(flush: true)) {
         	flash.message = message(code: 'default.created.message', args: [message(code: 'secUser.label', default: 'SecUser'), secUserInstance.id])
                if (!secUserInstance.authorities.contains(adminRole)) {
                    SecUserSecRole.create secUserInstance, adminRole
                }
                redirect(action: "show", id: secUserInstance.id)
            }
            else {
                render(view: "create", model: [secUserInstance: secUserInstance])
                return
            }            
            
        }

    }

    def show() {
        def secUserInstance = SecUser.get(params.id)
        if (!secUserInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'secUser.label', default: 'SecUser'), params.id])
            redirect(uri: "/user/list")
            return
        }

        [secUserInstance: secUserInstance]
    }

    def edit() {
        def secUserInstance = SecUser.get(params.id)
        if (!secUserInstance) {
              flash.message = "Admin user ${params.username} not found"
            redirect(uri: "/user/list")
            return
        }

        [secUserInstance: secUserInstance]
    }

    def update() {
        def secUserInstance = SecUser.get(params.id)
        if (!secUserInstance) {
           flash.message = "Admin user ${params.username} not found"
            redirect(uri: "/user/list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (secUserInstance.version > version) {
                secUserInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                    [message(code: 'secUser.label', default: 'SecUser')] as Object[],
                          "Another user has updated this SecUser while you were editing")
                render(view: "edit", model: [secUserInstance: secUserInstance])
                return
            }
        }

        secUserInstance.properties = params

        if (!secUserInstance.save(flush: true)) {
            render(view: "edit", model: [secUserInstance: secUserInstance])
            return
        }

       flash.message = "Information about Admin User ${params.username} sucessfully updated"
        redirect(action: "show", id: secUserInstance.id)
    }

    def delete() {
        def secUserInstance = SecUser.get(params.id)
        def username=secUserInstance.username
        if (!secUserInstance) {
         flash.message = "Admin user $username not found"
            redirect(uri: "/user/list")
            return
        }

        try {
            Collection<SecUserSecRole> adminRoles = SecUserSecRole.findAllBySecUser( secUserInstance );
            adminRoles*.delete();          
            
            
            secUserInstance.delete(flush: true)
            flash.message = "Admin user $username deleted"
            redirect(uri: "/user/list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'secUser.label', default: 'SecUser'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
