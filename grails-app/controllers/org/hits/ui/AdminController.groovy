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

import org.hits.parser.extractExcel.*
import org.hits.ui.*
import org.hits.parser.ui.template.*
import org.hits.parser.excelimp.*
import org.hits.parser.*
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.exception.ConstraintViolationException
import org.hits.ui.exceptions.MoreThanOneKnowledgeIdentifieredException





/**
 *
 * @author rongji
 */


class AdminController {
    def springSecurityService
    
    def index() { log.info "admin index" }
    
    def buildKnowledge={
        log.info "build knowledge ${params.file}"
        println "text $params.columnName"
        def file=params.file;
        def markcellrange
        def startLocation="${params.fromlocation}"
        def endLocation="${params.endlocation}"
        if(startLocation!=endLocation){
            
            markcellrange="$startLocation:$endLocation" 
        }else{
            log.info "auto select end of the column"
            markcellrange=startLocation
        }
       
        def  knowledgeInstance=new Knowledge(knowledgeName:"New Concept", columnName:params.columnName, firstRow:params.frow, firstCol:params.fcol, lastRow:params.erow, lastCol:params.ecol, sheetIndex:params.sheetIndex, fileName:params.file, markCellRange:markcellrange, markColor:"color");
        knowledgeInstance.save();
        def templateKnowledgeList =Knowledge.findAllByFileName(file);
        log.info "knowledge count:"+Knowledge.findAllByFileName(file);
        render(template:"/ui/admin/knowledge", model:[templateKnowledgeList:Knowledge.findAllByFileName(file)] )

    }
    def updateAction={
        println "ha ${params.id}  ${params.metaaction}"
        def id=params.id
        def action=params.metaaction
        def currentKnowledge=Knowledge.get(id)
        println"action $action currentKnowledge $currentKnowledge"
        if(action!="NO ACTION" && currentKnowledge){
         
                currentKnowledge.action=action
           
           println "currentKnowledge $currentKnowledge.action"
            currentKnowledge.save(flush:true)
            println "save"
        }
        render(template:"/ui/admin/knowledge", model:[templateKnowledgeList:Knowledge.findAllByFileName(session.getAttribute('openFileName'))] )
    }
    def updateKnowledge={
        log.info "update knowledge ${params.file}"
        log.info "drop at location ${params.location}"
        String dropPoint="${params.location}"
        def ativeSheet="${params.activesheet}"
        //check such location is within which knowledge's markcellrange
        Knowledge currentKnowledge=null
        int count=0
        def allMarkedKnowledge=Knowledge.findAll{(fileName==params.file)&&(sheetIndex==params.activesheet)}
        allMarkedKnowledge.each{knowledge->
            String cellrange=knowledge.markCellRange
            if(KnowledgeIdentifier.inMarkCellRange(cellrange, dropPoint)){
             
                currentKnowledge=knowledge
                count++
            }
        
        }
        
        if(count>1){
            //  throw new MoreThanOneKnowledgeIdentifieredException("there are more than one existing knowledge match the drop point, we take the last one")
            flash.message = "there are more than one existing knowledge match the drop point, we take the last one"
        }
       
        
        // Knowledge currentKnowledge=Knowledge.find{markCellRange == params.location;fileName==params.file};
        if(currentKnowledge!=null){
            println "knowledgeName $params.knowledgeName"
            currentKnowledge.knowledgeName=params.knowledgeName;
            currentKnowledge.markColor=params.knowledgeClass;   
            if( currentKnowledge.save(failOnError: true)){
                render(text: """<script type="text/javascript"> updateKnowledge("$currentKnowledge.firstRow","$currentKnowledge.firstCol","$currentKnowledge.lastRow","$currentKnowledge.lastCol","$currentKnowledge.sheetIndex","$currentKnowledge.markColor"); </script>""", contentType: 'text/javascript')
                render(template:"/ui/admin/knowledge", model:[templateKnowledgeList:Knowledge.findAllByFileName("$currentKnowledge.fileName")] )   
            }

        }else{
            flash.message = "Please drop at one of your marked places"
            render(text: """<script type="text/javascript"> warningMessage("$flash.message") </script>""", contentType: 'text/javascript')
            render(template:"/ui/admin/warningMessage")  
        }
    }
    def saveTemplatePre={
   
    
        def openFileName= session.getAttribute("openFileName");
  
        render(text: """<script type="text/javascript"> saveAs() </script>""", contentType: 'text/javascript')
    }

    
    def loadConcepts={
        def category=params.category
        def existingCategory=UIConcepts.findByCategory(category)
        if(existingCategory){
            session.category=category
        }
        render(text: """<script type="text/javascript"> redirect()</script>""", contentType: 'text/javascript')
        
    }
    def saveTemplate={
        //        println params.actionvalues
        //        def actionlist=params.actionvalues.split(";")
        //        def actionMap=[:]
        //        actionlist.each{
        //            var name=it.split(" ")[0]
        //            var action=it.split(" ")[1]
        //            actionMap.put(name,action )
        //        }
        def openFileName= session.getAttribute("openFileName");
        println openFileName
        def purpose=params.purpose
        def fileName=params.saveas
        def parsingway=params.parsingway
        if(Template.findByTemplateName(fileName)){
            log.info "$fileName already exist."
            flash.message="$fileName already exist."
            render(text: """<script type="text/javascript"> alert("$flash.message") </script>""", contentType: 'text/javascript')
            render(template:"/ui/admin/warningMessage")
            render(text: """<script type="text/javascript"> choosePurpose() </script>""", contentType: 'text/javascript')
        }else{
            // log.info "purpose $purpose"
     
            File file=  session.getAt("file");
            if(fileName && file){
                //update knowledgeList if the name is changed
                def list
                def finallist
                if(openFileName!=fileName){
            
                    list=Knowledge.findAll{fileName==openFileName}
                    list.each{knowledge->
                        knowledge.fileName=fileName
                        //knowledge.action=actionMap.getAt(knowledge.knowledgeName)
                        log.info "update ${knowledge.knowledgeName}"
                        knowledge.save(flush:true)
                    
                    }
                }
            
            
                list =Knowledge.findAllByFileName(fileName);
                def LoadConcepts=[]
                if(purpose=="loading"){
                    list.each{knowledge->
                        def loadconcept= knowledge.columnName.replaceAll(/\s/, "_")
                        knowledge.knowledgeName=loadconcept  
                        LoadConcepts<< loadconcept 
                        knowledge.save(flush:true)
                    
                    }   
                     
      
                    def pconceptList=[]
                    LoadConcepts.each{item->
            
                        println item
           
                        def uiconcept=new StandardConcept(concept: item).save(failOnError: true)
          
                        pconceptList<<uiconcept
                    }
        
                    println pconceptList
                    def category="${fileName.toString().tokenize(".").first()}Concepts"
                    def existingCategory1=UIConcepts.findByCategory(category)
                    if(existingCategory1){
                        def r = []
                        r += existingCategory1.concepts
                        r.each{concept->
                            existingCategory1.removeFromConcepts(concept)
                            concept.delete()        
                        }
                        existingCategory1.delete(flush:true)
                        log.info "delete $existingCategory1.id"
                    }
      
        
                    new UIConcepts(category:category, concepts:pconceptList).save(failOnError: true) 
                    
                }
                finallist=list.findAll{it.knowledgeName!="New Concept"} 
                
                List wasteKnowledgeList=list.findAll{it.knowledgeName=="New Concept"} 
                wasteKnowledgeList.each{wasteknowledge->
                    wasteknowledge.delete()
                }
                     
                
           
                println finallist.size()
            
                def templateInstance      
                try{
                    //create the template instance
                    if(purpose=="mapping"){
                        println"saved parsingway $parsingway"
                        templateInstance=new Template(templateName:fileName, binaryFileData:file.bytes,  knowledgeList:finallist, type:"public", purpose:purpose, parsingway:parsingway, visible:true).save(failOnError: true);        
                     
                    }else{
                        templateInstance=new Template(templateName:fileName, binaryFileData:file.bytes,  knowledgeList:finallist, type:"public", purpose:purpose, visible:true).save(failOnError: true);        
                 
                    }
                    log.info "template $Template.count";
           
          
                }catch(Exception e){
                    session.removeAttribute("openFileName")
                    session.removeAttribute("xml")
                    session.removeAttribute("templateType")
                    session.removeAttribute("file")
                    session.removeAttribute("knowledgeNameSet")
                    file.delete()   
                    log.error e.getMessage()                
                    log.info "$fileName already exist."
                    if(e instanceof ConstraintViolationException){
                        flash.message = "${e.getConstraintName()}" 
                    }else{
                        flash.message="${e.getMessage()}"
                    }     
                    render(text: """<script type="text/javascript"> warningMessage("$flash.message") </script>""", contentType: 'text/javascript')
                    render(template:"/ui/admin/warningMessage")
                }
                file.delete()   
                session.removeAttribute("openFileName")
                session.removeAttribute("xml")
                session.removeAttribute("file")
                session.removeAttribute("templateType")
                session.removeAttribute("knowledgeNameSet")
                render(text: """<script type="text/javascript"> alert("$fileName saved !"); </script>""", contentType: 'text/javascript')
 
            }else{
                flash.message = "You need to open the sheet again."
                render(text: """<script type="text/javascript"> warningMessage("$flash.message") </script>""", contentType: 'text/javascript')
                render(template:"/ui/admin/warningMessage") 
            }
        }
    }
 
    def deleteDef(ParserDef pD){
        log.info "dirty parserDef $pD.name"
        def allConfigs=[]
        allConfigs+= pD.parserConfigurations
        log.info "parserConfigs count for parserDef $pD.id is ${allConfigs.size()}"
        allConfigs.each{conf->
            if(conf){                   
                def allSources=[]
                allSources+=conf.sources  
                log.info "sourceDef count for parserConfigs $conf.id is ${allSources.size()}"
                allSources.each{s->
                    if(s){
                                          
                        conf.removeFromSources(s)  
                        s.delete(flush: true)
                        log.info "sourceDef $s.id delete"
                    }          
                }
                       
                pD.removeFromParserConfigurations(conf)
                                
                def targets=TargetDef.findAllByParserConfiguration(conf)
                targets.each{ta->
                         
                    if(ta.delete())
                    {log.info "targetDef $ta.id delete"}
                }
                                  
                conf.delete(flush: true)
                log.info "parserConfig $conf.id delete"
            }                     
        }
        log.info "parserDef $pD.id delete"
        pD.delete()
 
    }
    
    def deleteTemplateToDefs(Template templateInstance){
        log.info "dirty templateToDefs $templateInstance.templateName"
    }
    
    def deleteTemplate(Template templateInstance){
        def k=[]
        k+=templateInstance.knowledgeList
        k.each{knowledge->
            templateInstance.removeFromKnowledgeList(knowledge)
            knowledge.delete()            
        }

        //deleteTemplateToDefs(templateInstance)                      
        templateInstance.delete(flush: true)
    }

    def openSheet={
        
        log.info "openSheet" 
        session.putAt("username",springSecurityService.authentication.name)
        log.info "user ${session.username}"
        def webRootDir = servletContext.getRealPath("/")
        def savePath = webRootDir+"raw/"
        def dir = new File(savePath)
        session.removeAttribute("openFileName")
        if (!dir.exists()) {
            // attempt to create the path
            try {
                dir.mkdirs()
            } catch (Exception e) {
                response.setStatus(500, "could not create upload path ${savePath}")
                //render([written: false, fileName: file.name] as JSON)
                return false
            }
        }
        def f = request.getFile('myFile')
        def filePath=savePath
        def fileOrigName=f.getOriginalFilename()
        def givenName=fileOrigName
        if(Template.findByTemplateName(fileOrigName)){
            givenName="Template ${System.nanoTime().toString()}"
            flash.message = 'We already has template with the same name. we assign a temporary name to it, please rename it during saving.' 
        }
        //else{
        
        if(!f.empty) {
       
                        
            log.info "upload "+givenName
            session.openFileName=givenName
            filePath=filePath+"/"+System.nanoTime().toString()+"$fileOrigName"
            File newFile=new File(filePath)
            f.transferTo(newFile)
            session.putAt("file", newFile)
            session.removeAttribute("category")
            // new excelReader(filePath ).eachLine{println "First column on row ${it.rowNum} = ${cell(0)}"}
            // def excel=new excelReader(filePath )
            def excel=new excelReader(newFile)
            // def xmlData=excel.excelXmlBuilder()
            def xmlData=excel.excelWithMergedAreaXmlBuilder()
            def dirtyKnowledgeList=Knowledge.findAllByFileName(givenName)
            if(dirtyKnowledgeList){
                log.info"dirty knowledgeList for templateName $givenName"
                dirtyKnowledgeList.each{k->
                    k.delete()                        
                }
            }
            // def xmlData=excel.excelXmlStringBuilderToDo()
            session.filepath=filePath
            session.xml=xmlData
            session.templateType="new"
                
            // }        
        }
        else {
            flash.message = 'file cannot be empty'
        }
        //}
        redirect(uri: "/admin")

    }
    
    def downloadTutorial={
        def webRootDir = servletContext.getRealPath("/")
        def tutorialPath= webRootDir+"Tutorial.pdf"
        File tutorial=new File(tutorialPath)
        if(tutorial){           
            response.setContentType("application/application/zip") 
            response.setHeader("Content-disposition", "attachment;filename=Tutorial.pdf")
            response.outputStream <<  tutorial.bytes
        }

    }
    
    def createParserConfig(){
        def templateList=Template.list()
        println templateList
        render(view: "/ui/admin/targetTemplate", model:[templateList:templateList])
    }

}
