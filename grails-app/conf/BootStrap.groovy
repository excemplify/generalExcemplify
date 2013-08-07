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
import grails.util.GrailsUtil
import org.hits.parser.User
import org.hits.parser.SecRole
import org.hits.parser.SecUser
import org.hits.parser.SecUserSecRole
//import org.hits.parser.ParserDef
//import org.hits.parser.ParserConfiguration
//import org.hits.parser.excelimp.ImmunoParserAction
//import org.hits.parser.SourceDef
//import org.hits.parser.TextFileSourceDef
//import org.hits.parser.TargetDef
//import org.hits.parser.TemplateFile
import org.hits.ui.Template
import org.hits.ui.Knowledge
import org.hits.ui.Resource
import org.hits.ui.Experiment
import org.hits.ui.KnowledgeFetcher
//import org.hits.ui.TemplateToParserDef
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.hits.ui.StandardConcept
import org.hits.ui.UIConcepts

class BootStrap {
    def authenticationManager
    def concurrentSessionController
    def securityContextPersistenceFilter
    def authenticationProcessingFilter
    def concurrentSessionControlStrategy

    def init = { servletContext ->
        switch(GrailsUtil.environment){
        case "test":
            initialize(servletContext)
                     dataclean();
            break

        case "production":
            initialize(servletContext)        
            dataclean();
        
            break
            
        case "development":
            initialize(servletContext)
            dataclean();
            break
        
        }
        
        SpringSecurityUtils.clientRegisterFilter('concurrencyFilter', SecurityFilterPosition.CONCURRENT_SESSION_FILTER)
        authenticationProcessingFilter.sessionAuthenticationStrategy = concurrentSessionControlStrategy
    }
    def destroy = {
    }
    
  
    
    def initialize(servletContext){  //some default
   
        initialDefaultUserAccount()
       // importConcepts()
        //initializeInnerTemplate(servletContext)
        
       
    }
    
    
    def initialDefaultUserAccount(){
        def userRole = SecRole.findByAuthority('ROLE_USER') ?: new SecRole(authority: 'ROLE_USER').save(failOnError: true)
        def adminRole = SecRole.findByAuthority('ROLE_ADMIN') ?: new SecRole(authority: 'ROLE_ADMIN').save(failOnError: true)
        
        
        def adminUser = SecUser.findByUsername('jdoeadmin') ?: new SecUser(
            username: 'jdoeadmin',
            password: 'jdoeadmin',
            enabled: true).save(failOnError: true)
 
        if (!adminUser.authorities.contains(adminRole)) {
            SecUserSecRole.create adminUser, adminRole
        }
            
        def jdoe = SecUser.findByUsername('jdoe') ?: new User(username:"jdoe", password:"pass", name:"John Doe",enabled: true).save(failOnError: true)
        def jsmith = SecUser.findByUsername('jsmith') ?: new User(username:"jsmith", password:"word", name:"Jane Smith",enabled: true).save(failOnError: true)
      
        if (!jsmith.authorities.contains(userRole)) {
            SecUserSecRole.create jsmith, userRole
        }
         
        if (!jdoe.authorities.contains(userRole)) {
            SecUserSecRole.create jdoe, userRole
        }
        
    }

    def dataclean(){
        log.info "start data cleaning"
        def knowledgeFileNames=Knowledge.executeQuery("select distinct fileName from Knowledge" )
        def templateNames=Template.executeQuery("select distinct templateName from Template" )
        def wnum=0
        knowledgeFileNames.each{name->
            if(!templateNames.contains(name)){
                def wastKnowledgeList=Knowledge.findAllByFileName(name); 
                wastKnowledgeList.each{wast->
                    def id=wast.id
                    wast.delete()
                    wnum=wnum+1
                 
                }
            }               
        }
            
        log.info "delete  $wnum wast knowledges"
            
        def resourceIds=Resource.executeQuery("select distinct id from Resource" ) 
        def experimentResources=Experiment.executeQuery("select resources from Experiment" )
        def num=0
        resourceIds.each{id->
            def resource=Resource.get(id)
            if(!experimentResources.contains(resource)){
                resource.delete()
                num=num+1
            }
                
        }
            
        log.info "$num waste resources deleted"
    }
}
