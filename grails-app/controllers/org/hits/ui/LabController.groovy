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
import org.hits.ui.*
import grails.converters.JSON
import org.hits.parser.*
import org.hits.ui.KnowledgeFetcher
import java.text.SimpleDateFormat
import org.hits.parser.config.*
import org.hits.ui.exceptions.CustomizedException


/**
 *
 * @author rongji
 */

class LabController {
    def springSecurityService
    // def parserService
    //def experimentParsersConfigService
    def generalParserService
    def generalParserConfigService
    def mailService
    def formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
    def auxillarySpreadsheetService
    
    static INNERRAWDATATEMPLATENAME="rawdata_template.xls"
    static INNERLOADINGTEMPLATENAME="laneloading_template_inner.xls"
    static INNERGELINSPECTORTEMPLATENAME="gelInspectorTemplate_inner.xls"

    def index() {
 
    }

    def initialExp(){
        println "start initialization of experiment"
        def user = User.get(springSecurityService.principal.id)
        def errorMessage="unknown"
        session.putAt("username",springSecurityService.authentication.name)
        log.info "user ${session.username}"
        log.info "ExperimentName: ${params.experimentName}"
        log.info "Using sourceTemplate: ${params.sourcetemplate}"
        log.info "Using targetTemplate: ${params.targettemplate}"
        log.info "Set Up Resource: ${params.setUpResourceId}"
        
        session.removeAttribute("setUpResourceId")
        session.removeAttribute("setUpResourceName")
        /**
         *  put following stuff into stages
         */
   
        Template sourceTemplate=Template.get(params.sourcetemplate)
        def consideredConcepts=sourceTemplate.knowledgeList.collect{it.knowledgeName}
        def sourceStage=new Stage(stageIndex:1, stageName:'source', stageTemplate:sourceTemplate)
        Template targetTemplate=Template.get(params.targettemplate)
        def targetStage=new Stage(stageIndex:2, stageName:'target', stageTemplate:targetTemplate)
       

        File setupFile = session.getAttribute("setup")
        if (setupFile==null) {
            log.error "no file uploaded yet"
            render(text: """<script type="text/javascript"> warningMessage('no file uploaded yet'); </script>""", contentType: 'text/javascript')    
          
        }else{
        
            def experiment = new Experiment()
            def setupResource=Resource.findById(params.setUpResourceId)
            def resourceName=setupResource.fileName
            if(resourceName.endsWith("xlsx")){
                experiment.contentType="xlsx"
            }else{
                experiment.contentType="xls" 
            }
            def resources=[]
            def date=new Date()
            resources.add(setupResource)
            experiment.filename=params.experimentName
            experiment.binaryData=setupResource.binaryData
            experiment.author=user
            experiment.createdOn=date
            experiment.resources=resources
            experiment.setUpTemplateName=sourceTemplate.templateName
            experiment.addToStages(sourceStage)  
            experiment.addToStages(targetStage)
            //  experiment.share="private"
            experiment.type="new"
            experiment.save(failOnError: true)
            //        experiment.logFile=logFileName
            
            // experimentParsersConfigService.config(experiment)
             try{ 
            generalParserConfigService.config(experiment)
            
          
  
                def sheetUpdate = new SheetUpdate(entityName:"ExperimentSetUp", state:"add/create", fileNameVersion:"${setupResource.fileName}[version:${setupResource.fileversion?formatter.format(setupResource.fileversion):"old"}]", comment:"upload the setup file and initial ${params.experimentName}", dateUpdated: setupResource.fileversion)
                experiment.addToUpdates(sheetUpdate)
                experiment.save()
                session.putAt("fileName",resourceName)
                session.removeAttribute("parsedFile")
                session.removeAttribute("parsedFileName")     
                // lenneke change here to set up the parserDef
                // instead should pass in the template name
                def fileToParse=setupFile

                println "we start to try parse"
                generalParserService.parseSpreadsheet(fileToParse, [experiment:experiment, targetStageIndex:2]) 
                
                fileToParse=session.getAttribute("parsedFile")
         
                if(session.getAttribute("parsedFile")){
                    experiment.binaryData=session.getAttribute("parsedFile")
            
                    sheetUpdate = new SheetUpdate(entityName:"parserd", state:"auto generate", fileNameVersion:"target sheet in the workbook",comment:"", dateUpdated:new Date())
                    experiment.addToUpdates(sheetUpdate)
       
                    experiment.save(failOnError: true)
                }
                session.removeAttribute("setup")
                setupFile.delete()
                session.removeAttribute("setUpResourceId")
                session.removeAttribute("experimentId")
                session.putAt("active", "0")
                render(text: """<script type="text/javascript"> afterUpload(1,1); </script>""", contentType: 'text/javascript')
                render(text: """<script type="text/javascript"> refreshTableSorter();</script>""", contentType: 'text/javascript')
          
            }catch(Exception e){
               
                log.error e.getMessage()
                setupFile.delete()
                if(experiment){
                    generalParserConfigService.deleteConfig(experiment)
                       
                    if(experiment.resources){
                        def r = []
                        r += experiment.resources
                        r.each{resource->
                            experiment.removeFromResources(resource)
                            resource.delete()        
                        }
                    }else{
                        setupResource.delete()  
                    }
                    if(experiment.updates){           
                        def u=[]
                        u+=experiment.updates   
                        experiment.updates.clear()
                        u.each{update->
                       
                            experiment.removeFromUpdates(update) 
                            if(update!=null){
                                update.delete(flush:true)   
                            }
                        }
                    }  
             
                    experiment.delete(flush: true)
                }else{
                    setupResource.delete()   
                }   
            
                if (e instanceof grails.validation.ValidationException){
                    render(text: """<script type="text/javascript"> warningMessage('Please make sure the experiment name is unique.'); </script>""", contentType: 'text/javascript')
       
                }else if(e instanceof  NullPointerException){
                    render(text: """<script type="text/javascript"> warningMessage('Please make sure you upload the right set up file and also make sure the template you told us is the template you really use. You can delete the experiment or update the set up file to try again'); </script>""", contentType: 'text/javascript')
        
                }else if(e instanceof CustomizedException){
                    log.error "rule violat"  
                    render(text: """<script type="text/javascript"> warningMessage('${e.getMessage()}'); </script>""", contentType: 'text/javascript')
          
                }else{
                    render(text: """<script type="text/javascript"> warningMessage('Some parsing errors happens, please check and try again'); </script>""", contentType: 'text/javascript')
          
                }
 
        
            }
        }
        render(text: """<script type="text/javascript"> afterUpload(1,1); </script>""", contentType: 'text/javascript')
        render(text: """<script type="text/javascript"> refreshTableSorter(); </script>""", contentType: 'text/javascript')
        
        render(template:"/ui/user/experiment", model:[experimentInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='new'}] )
   
    }
    
    def warning(){
        log.info "warning"
        render(template: "/ui/user/warning")
    }
    def returnToMain(){
        session.removeAttribute("setup")
        session.removeAttribute("setupupdate")
        session.removeAttribute("performsetup")
        session.removeAttribute('setUpResourceId')
        session.removeAttribute('setUpResourceName')
        session.removeAttribute('setUpResourceNameUpdate')
        session.removeAttribute('setUpResourceIdUpdate')
        session.removeAttribute("performedSetUpResourceId")
        session.removeAttribute("performedSetUpResourceName")
        
        
        session.removeAttribute("rawdata")
        session.removeAttribute("rawdatamap")
        session.removeAttribute("geldata")
        session.removeAttribute("geldatamap")
        session.removeAttribute("experimentId")
        session.removeAttribute("experimentName")
        session.removeAttribute("info")
     
        session.removeAttribute("fileName")
        session.removeAttribute("parsedFile")
        session.removeAttribute("parsedFileName") 
        session.removeAttribute("openFileName") 
        session.removeAttribute("file") 
        session.removeAttribute("active") 
        session.removeAttribute("xml") 
        session.removeAttribute("filepath")
        session.removeAttribute("templateType") 
     
        session.removeAttribute("category")
        redirect(uri:"/")
    }
    def clear(){
        def after=params.after

        File setupFile=session.getAttribute("setup")
        File setupUpdateFile=session.getAttribute("setupupdate")
        File performedSetupFile=session.getAttribute("performsetup")
        if(setupFile){
            setupFile.delete();
            log.info "setup delete"
        }
        if(setupUpdateFile){
            setupUpdateFile.delete();
            log.info "setupupdate delete"           
        }
        
        if( performedSetupFile){
            performedSetupFile.delete();
            log.info "performed setup delete"
        }
        session.removeAttribute("setup")
        session.removeAttribute("setupupdate")
        session.removeAttribute("performsetup")
        
        def setup=session.getAttribute("setUpResourceId")
        if(setup){
            def setupResource=Resource.findById(setup)
            if(setupResource){
                setupResource.delete() 
            }
        }
        def setupupdate=session.getAttribute("setUpResourceIdUpdate")
        if(setupupdate){
            def setupUpdateResource=Resource.findById(setupupdate)
            if(setupUpdateResource){
                setupUpdateResource.delete() 
            }
        }
        def psetup=session.getAttribute("performedSetUpResourceId")
        if(psetup){
            def performedSetupResource=Resource.findById(psetup)
            if(performedSetupResource){
                performedSetupResource.delete() 
            }
        }
        session.removeAttribute('setUpResourceId')
        session.removeAttribute('setUpResourceName')
        session.removeAttribute('setUpResourceNameUpdate')
        session.removeAttribute('setUpResourceIdUpdate')
        session.removeAttribute("performedSetUpResourceId")
        session.removeAttribute("performedSetUpResourceName")
        session.removeAttribute("knowledgeNameSet")
        
        session.removeAttribute("rawdata")
        session.removeAttribute("rawdatamap")
        session.removeAttribute("geldata")
        session.removeAttribute("geldatamap")
        session.removeAttribute("experimentId")
        session.removeAttribute("experimentName")
        session.removeAttribute("info")
     
        session.removeAttribute("fileName")
        session.removeAttribute("parsedFile")
        session.removeAttribute("parsedFileName") 
        session.removeAttribute("openFileName") 
        session.removeAttribute("file") 
        session.removeAttribute("active") 
        session.removeAttribute("xml") 
        render(text: """<script type="text/javascript"> afterUpload($after,$after); </script>""", contentType: 'text/javascript')
        // redirect(uri:"/")
    }

    
    def upload(){

        log.info "upload set up"
        def webRootDir = servletContext.getRealPath("/")
        def savePath = webRootDir+"raw/"
        def dir = new File(savePath)
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
        def f = request.getFile('mySetUp')
        def filePath=savePath
        if(!f.empty) {
            print "upload "+f.getOriginalFilename()
            def fileName=f.getOriginalFilename()
         
            filePath=filePath+"/"+System.nanoTime().toString()+"${f.getOriginalFilename()}"
            File newFile=new File(filePath)
            f.transferTo(newFile)
            
            session.putAt("setup", newFile)
            
            def user=User.get(springSecurityService.principal.id)
            def  setUpResource=new Resource(fileName:fileName, type:"setup", binaryData:newFile.bytes, author:user, state:"active", fileversion:new Date());            
            setUpResource.save();
            log.info "resourceId ${setUpResource.id}"
            session.putAt("setUpResourceId",setUpResource.id )
            session.putAt("setUpResourceName",fileName )
            session.putAt("active", "0")
            session.putAt("selector", "new")
      
            redirect(uri:"/lab")
    
        
        }
        else {
            flash.message = 'file cannot be empty'
        }

        log.info "Resource: $Resource.count"
  
    

    }


}
