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

class UserController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        //params.max = Math.min(params.max ? params.int('max') : 10, 100)
        List<SecUser> adminList=[]
        def adminRole = SecRole.findByAuthority('ROLE_ADMIN')
        Collection<SecUserSecRole> adminRoles = SecUserSecRole.findAllBySecRole(adminRole);
        adminRoles.each(){role->
            adminList.add(role.secUser)
            
        }
        
        
     
        [userInstanceList: User.list(params), userInstanceTotal: User.count(), adminInstanceList:adminList, adminInstanceTotal: adminList.size()]
    }

    def create = {
        def userInstance = new User()
        userInstance.properties = params
        return [userInstance: userInstance]
    }

    def save = {
        def userRole = SecRole.findByAuthority('ROLE_USER') ?: new SecRole(authority: 'ROLE_USER').save(failOnError: true)
        if(!SecUser.findByUsername(params.username)){
            def userInstance = new User(params)        
            if (userInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), userInstance.id])}"
                if (!userInstance.authorities.contains(userRole)) {
                    SecUserSecRole.create userInstance, userRole
                }
                redirect(action: "show", id: userInstance.id)
            }
            else {
                render(view: "create", model: [userInstance: userInstance])
            }  
            
            
        }else{
            flash.message = "same username ${params.username} already exists" 
            def userInstance = new User()
            userInstance.properties = params
            render(view: "create", model: [userInstance: userInstance])
        }
    
        
        
       
    }

    def show = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            [userInstance: userInstance]
        }
    }

    def edit = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [userInstance: userInstance]
        }
    }
    
    def updateProfile(){
        log.info "update"
        def userInstance=User.get(params.id)
        if(userInstance){
            userInstance.username=params.username
            userInstance.password=params.password
            userInstance.seekEmailAddress=params.seekEmailAddress
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                render(text: """<script type="text/javascript"> alert("Information about User ${params.username} sucessfully updated."); </script>""", contentType: 'text/javascript')
                render(text: """<script type="text/javascript"> resetEmail('${userInstance.seekEmailAddress}'); </script>""", contentType: 'text/javascript')

            }else{
                flash.message = "Exceptions occur during updating!!"  
            }
        }else{
            flash.message = "User with such ID ${params.id} does not exist."   
        }
        
        render(template:"/ui/user/userProfile", model:[userInstance:userInstance] )
    }

    def update = {
        def userInstance = User.get(params.id)
        if (userInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (userInstance.version > version) {
                    
                    userInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'user.label', default: 'User')] as Object[], "Another user has updated this User while you were editing")
                    render(view: "edit", model: [userInstance: userInstance])
                    return
                }
            }
            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save(flush: true)) {
                flash.message = "Information about User ${params.username} sucessfully updated"
                redirect(action: "show", id: userInstance.id)
            }
            else {
                render(view: "edit", model: [userInstance: userInstance])
            }
        }
        else {
            flash.message = "User ${params.username} not found"
            redirect(action: "list")
        }
    }

    def delete = {
        def userInstance = User.get(params.id)
        def username=userInstance.username
        if (userInstance) {
            try {
                Collection<SecUserSecRole> userRoles = SecUserSecRole.findAllBySecUser(userInstance);
                userRoles*.delete();                        
                userInstance.delete(flush: true)
                flash.message = "User $username deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'user.label', default: 'User'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "User $username not found"
            redirect(action: "list")
        }
    }
}
