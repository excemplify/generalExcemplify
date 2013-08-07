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
import grails.converters.*
import org.hits.parser.*
import org.hits.parser.config.*
import org.hits.ui.FileNameValidator
import org.hits.ui.Template
import org.hits.ui.VisualEntry
import java.text.SimpleDateFormat
import java.util.TimeZone;
import java.util.zip.ZipOutputStream  
import java.util.zip.ZipEntry  
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import grails.validation.ValidationException


//import java.util.logging.FileHandler;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import java.util.logging.SimpleFormatter;


class ExperimentController {
    def springSecurityService
    def parserService 
    def auxillarySpreadsheetService
    def generalParserConfigService
    def formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
    def formatter2=new SimpleDateFormat("MMM dd yyyy HH:mm:ss", Locale.GERMANY);

    
    
    static final int BUFF_SIZE = 100000;

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
      
    }

  

    def deleteExperiment={
        try{
            def user = User.get(springSecurityService.principal.id)
        
            log.info "delete ${params.id} experiment"
     
            def experimentInstance = Experiment.get(params.id)  
            def experimentType=experimentInstance.type
            def experimentName=experimentInstance.filename
        
            if (!experimentInstance) {
                println "not found"
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])

            }
            if(checkUser(experimentInstance.author )){
                try {
            
                    //                experimentInstance.resources.clear()
                    //                experimentInstance.updates.clear()
              generalParserConfigService.deleteConfig(experimentInstance)
                    
                    def r = []
                    r += experimentInstance.resources
                    r.each{resource->
                        experimentInstance.removeFromResources(resource)
                        resource.delete()        
                    }
                     def s=[]
                       s += experimentInstance.stages
                    s.each{stage->
                        experimentInstance.removeFromStages(stage)
                        stage.delete()        
                    }
                    def u=[]
                    u+=experimentInstance.updates
                    u.each{update->
                        experimentInstance.removeFromUpdates(update)
                        update.delete()            
                    }
                    experimentInstance.delete(flush: true)
                    render(text: """<script type="text/javascript"> alert("$experimentName  and its rawdata files all deleted!"); </script>""", contentType: 'text/javascript')
             
                }
                catch (DataIntegrityViolationException e) {
                    flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])
                    redirect(uri:"/lab")
                }
            }else{
                render(text: """<script type="text/javascript"> warningMessage('Only the author can do this operation!'); </script>""", contentType: 'text/javascript')
    
            }
            if(experimentType && experimentType=="performed"){

                render(template:"/ui/user/performedexperiment", model:[experimentPInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='performed'}] )   
         
            }else{
println"render new"
    session.putAt("selector", "new")
                render(template:"/ui/user/experiment", model:[experimentInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='new'}] )    
      
            }
        }catch(MissingPropertyException ex){
            render(text: """<script type="text/javascript"> warningMessage('Your session might expired, try to log in again!'); </script>""", contentType: 'text/javascript')

        }
    }
    def shareExperiment={
        def user = User.get(springSecurityService.principal.id)
      
        def experimentInstance = Experiment.get(params.id)
        def experimentType=experimentInstance.type
        def experimentName=experimentInstance.filename
        if(experimentInstance && checkUser(experimentInstance.author )){
            experimentInstance.share="public"
            if (!experimentInstance.save(flush: true)) {
                flash.message="Share operation is not succesful!"
                redirect(uri:"/lab")
 
            }
            def expUpdate = new SheetUpdate(comment:"Experiment is shared to public",dateUpdated:new Date()) //not sure if these are actually used
            experimentInstance.addToUpdates(expUpdate) 
            experimentInstance.save(flush:true) 
        
            render(text: """<script type="text/javascript"> alert("$experimentName is public experiment now!"); </script>""", contentType: 'text/javascript')
        }else{
            render(text: """<script type="text/javascript"> warningMessage('Only the author can do this operation!'); </script>""", contentType: 'text/javascript')
        
        }
        if(experimentType && experimentType=="performed"){

            render(template:"/ui/user/performedexperiment", model:[experimentPInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='performed'}] )   
         
        }else{
            render(template:"/ui/user/experiment", model:[experimentInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='new'}] )    
      
        }
          
      
    }
    
    def stopShareExperiment={
        def user = User.get(springSecurityService.principal.id)

        def experimentInstance = Experiment.get(params.id)
        def experimentName=experimentInstance.filename
        def experimentType=experimentInstance.type
        if(experimentInstance && checkUser(experimentInstance.author )){
            experimentInstance.share="private"
            if (!experimentInstance.save(flush: true)) {
                flash.message="Stop sharing operation is not succesful!"
                redirect(uri:"/lab")
 
            }
            def expUpdate = new SheetUpdate(comment:"Experiment is change to private",dateUpdated:new Date()) //not sure if these are actually used
            experimentInstance.addToUpdates(expUpdate) 
            experimentInstance.save(flush:true) 
            render(text: """<script type="text/javascript"> alert("$experimentName is private experiment now!"); </script>""", contentType: 'text/javascript')
        }else{
            render(text: """<script type="text/javascript"> warningMessage('Only the author can do this operation!'); </script>""", contentType: 'text/javascript')
           
        }
        if(experimentType && experimentType=="performed"){

            render(template:"/ui/user/performedexperiment", model:[experimentPInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='performed'}] )   
         
        }else{

            render(template:"/ui/user/experiment", model:[experimentInstanceList:Experiment?.findAllByAuthor(user).findAll{it.type=='new'}] )    
      
        }
    }

    def checkUser={author->
        return (author==User.get(springSecurityService.principal.id))
    }

    def downloadlog={
        def   experimentInstance = Experiment.get(params.id)
        def logfile = writeUpdates(experimentInstance.updates)
        
        response.setContentType("application/octet-stream")
        response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}.log")
        response.outputStream << logfile.bytes     
    }
    
    //    def visualizelogintimeline={
    //        def   experimentInstance = Experiment.get(params.id)
    //        
    //        Map visualEntries=[:]
    //        experimentInstance.updates.each{update->
    //             
    //            if(update.entityName && update.state && update.fileNameVersion){
    //                if(visualEntries.containsKey(update.fileNameVersion)){
    //                    VisualEntry currentVisEntry=visualEntries.get(update.fileNameVersion)
    //                    currentVisEntry.stateMap.put(update.dateUpdated, update.state)
    //                     
    //                }else {
    //                    VisualEntry visEntry=new VisualEntry() 
    //                    visEntry.entityName=update.entityName
    //                    visEntry.fileNameVersion=update.fileNameVersion
    //                    Map states=[:]
    //                    states.put(update.dateUpdated, update.state)
    //                    visEntry.stateMap=states
    //                    visualEntries.put(update.fileNameVersion, visEntry)
    //                }
    //     
    //            }
    //             
    //        }
    //         
    //        /**write jsonString
    //         */
    //      
    //
    //        def jsonString="{\'dateTimeFormat\':\'iso8601\', \'events\':["
    //        int count=0
    //         visualEntries.each{k,v->          
    //            count++
    //               int count2=0
    //            v.stateMap.each{kstate,vstate->
    //                count2++  
    //                def color
    //                if(vstate=="deactive"){
    //                    color="red"
    //                }else if(vstate=="export"){
    //                    color="blue"
    //                }else if(vstate=="initial"||vstate=="add/create"){
    //                    color="white"
    //                }else if(vstate=="active"||vstate=="update"){
    //                    color="green"
    //                }else{
    //                    color="yellow"
    //                }
    //               formatter2.timeZone = TimeZone.getTimeZone('GMT')
    //                
    //                def time=formatter2.format(kstate)
    //                    println time
    //             jsonString=jsonString+"{\'start\':\'${time}\', \'title\':\'${v.entityName}\', \'description\':\'${v.fileNameVersion} ${vstate}\',\'color\':\'${color}\'}"
    //             if(count2<v.stateMap.size()){
    //                    jsonString=jsonString+","  
    //                }
    //            }
    //               if(count< visualEntries.size()){
    //                    jsonString=jsonString+","  
    //                }
    //         }
    //        
    //           jsonString=jsonString+"]}"
    //           println jsonString
    //       
    //        //                    
    //          
    //        
    //        session.putAt("experimentLog",jsonString)
    //        redirect(uri:"/lab/timelineview?experimentId="+params.id)
    //        
    //    }
    
    def visualizelog={
        def   experimentInstance = Experiment.get(params.id)
        log.info "visual"
        Map visualEntries=[:]
        experimentInstance.updates.each{update->
             
            if(update.entityName && update.state && update.fileNameVersion){
                if(visualEntries.containsKey(update.fileNameVersion)){
                    VisualEntry currentVisEntry=visualEntries.get(update.fileNameVersion)
                    currentVisEntry.stateMap.put(update.dateUpdated, update.state)
                     
                }else {
                    VisualEntry visEntry=new VisualEntry() 
                    visEntry.entityName=update.entityName
                    visEntry.fileNameVersion=update.fileNameVersion
                    Map states=[:]
                    states.put(update.dateUpdated, update.state)
                    visEntry.stateMap=states
                    visualEntries.put(update.fileNameVersion, visEntry)
                }
     
            }
             
        }
         
        /**write jsonString
         */
        
  
        def jsonString="["
        int count=0
       
        visualEntries.each{k,v->
            
            count++
            jsonString=jsonString+"{"
            jsonString=jsonString+"\"name\":\"${v.entityName}\", \"blot\":\"${v.entityName.tokenize("_").first()=="rawdata"?v.entityName.tokenize("_").last():"notrawdata"}\", \"file\":\"${v.fileNameVersion}\",\"states\":["
            int count2=0
            v.stateMap.each{kstate,vstate->
                count2++  

                jsonString=jsonString+"[\"${formatter.format(kstate)}\",\"${vstate}\"]"
                if(count2<v.stateMap.size()){
                    jsonString=jsonString+","  
                }
                
            }
            jsonString=jsonString+"], \"times\":["
            int count3=0
            v.stateMap.each{kstate,vstate->
                count3++  
                jsonString=jsonString+"[\"${formatter.format(kstate)}\",\"${formatter.format(kstate)}\"]"
                if(count3<v.stateMap.size()){
                    jsonString=jsonString+","  
                }
                
            }
            
            jsonString=jsonString+"]"
            jsonString=jsonString+"}"
            if(count<visualEntries.size()){
                jsonString=jsonString+","
            }
          
            
        }
        jsonString=jsonString+"]"
       
        //                    
        println jsonString 
        
        session.putAt("experimentLog",jsonString)
        redirect(uri:"/lab/logview?experimentId="+params.id)
    }

    def writeUpdates(updates){
        def webRootDir = servletContext.getRealPath("/")
        def updateFile = File.createTempFile("updates",".log")
        updates.each{
            updateFile << "${it.dateUpdated} ${it.comment} \n"
        }
        return updateFile
    }
    def downloadAll={
        def   experimentInstance = Experiment.get(params.id)
        def allbinary
        def resourcesList=experimentInstance.resources.findAll{it.state=="active"}
        println "resource size ${resourcesList.size()}"
        try{
            allbinary=auxillarySpreadsheetService.collateDataFiles(resourcesList,"")
 
            if (experimentInstance) {
                try {

                    response.setContentType("application/vnd.ms-excel")
                    response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_all.xls")
                    response.outputStream << allbinary

             

                }
                catch(Exception ex){
                    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
      
                }
            }
            else{
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
         
            }
        }catch(ValidationException e){
            e.errors.each {
                println it
                flash.message+="${it.toString()}"
            }
          
        
        }   
    }
    def downloadPerform={
        def   experimentInstance = Experiment.get(params.id)
        def resourcesList=experimentInstance.resources.findAll{(it.type=="gelinspector" || it.type=="setup")&&(it.state=="active")}
        println "resource size ${resourcesList.size()}"
        try{
            experimentInstance.binaryData=auxillarySpreadsheetService.collateDataFiles(resourcesList,"")
            experimentInstance.save(failOnError: true)
            if ( experimentInstance) {
                try {
                    
   if(experimentInstance.contentType=="xlsx"){
           response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
         
                response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_workbook.xlsx")
             
            }else{
                      response.setContentType("application/vnd.ms-excel")
                response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_workbook.xls")
             
            }
//                    response.setContentType("application/vnd.ms-excel")
//                    response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_workbook.xls")
                   response.outputStream <<  experimentInstance.binaryData

             

                }
                catch(Exception ex){
                    flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
                    redirect(action: "list")
                }
            }
            else{
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
                redirect(action: "list")
            }
        }catch(ValidationException e){
            e.errors.each {
                println it
                flash.message+="${it.toString()}"
            }
          
            redirect(action: "list")
        }
        //        Logger logger=Logger.getLogger("${experimentInstance.filename}")
        //        logger.info("workbook downloaded once"); 
    }
    
    def download={
        def   experimentInstance = Experiment.get(params.id)

        //        def webRootDir = servletContext.getRealPath("/")

        if (experimentInstance) {
            try {
            if(experimentInstance.contentType=="xlsx"){
               response.setContentType("application/vnd.ms-excel")
                response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_workbook.xlsx")
             
            }else{
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                response.setHeader("Content-disposition", "attachment;filename=${experimentInstance.filename.trim().replaceAll("\\s+", "_")}_workbook.xls")
             
            }
                    response.outputStream <<  experimentInstance.binaryData

             

            }
            catch(Exception ex){
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
                redirect(action: "list")
            }
        }
        else{
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
            redirect(action: "list")
        }
        //        Logger logger=Logger.getLogger("${experimentInstance.filename}")
        //        logger.info("workbook downloaded once");
    }
    
    def downloadOrigExport={
        def   experimentInstance = Experiment.get(params.id)

        if ( experimentInstance) {
            def gelInspectorWBs=[]
            def gelResources=experimentInstance.resources.findAll{(it.type=="gelinspector")&&(it.state=="active")}
            if(gelResources.size()>0){
            
            String zipFileName = "OrigGelInspectorFiles.zip"  

            ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))  
            gelResources.each{ gel-> 
            
                zipFile.putNextEntry(new ZipEntry("${gel.fileName}"))  
           
                zipFile.write(gel.binaryData)
                
                 
                zipFile.closeEntry()  
            }
            zipFile.close()  
            def zipfile=new File(zipFileName)
            response.setContentType("application/application/zip") 
            response.setHeader("Content-disposition", "attachment;filename=${zipfile.getName()}")
            response.outputStream <<  zipfile.bytes
            zipfile.delete();
            }else{
                 flash.message = "less than 1 active data sources"
              redirect(uri:"/exception")
            }
        }else{
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'experiment.label', default: 'Experiment'), params.id])}"
         redirect(uri:"/exception")
        }
        
    }
        
        




    def exportInto={
        

        def experimentInstance = Experiment.get(params.id)
        def user=User.get(springSecurityService.principal.id)
        def rawdataList = experimentInstance.resources.findAll{ it.type=="rawdata" && it.state=="active"}
        def setUpFileTemplate = Template.findByTemplateName(experimentInstance.setUpTemplateName)
        // def rawDataTemplate = Template.findByTemplateName("rawdata_template.xls") //currently we have only 1 template for raw data exporting
        println "num raw data ${rawdataList.size()}"
        if (params.layout=="other"){
            flash.message="Other layouts not implemented yet"
            redirect(uri:"/lab")
            return
        }
        def parserType = parserService.getParserName(params.layout)//this would change to params.layout
        //
        if(!ExperimentToParserDef.findByExperiment(experimentInstance)){
            
        }
        //
        
        
        def newWorkbook=experimentInstance.binaryData
        def filenamesstring=""
        try{
            rawdataList.each{ resource ->
                session.setAttribute("ExperimentWorkbook",newWorkbook)//required
                session.putAt("fileName",resource.fileName)//required
            
                def datafile = File.createTempFile("${System.nanoTime()}",resource.fileName)
                //test if file is a text or an excel file
                println resource.fileName
                if (resource.fileName =~ /.txt$/){
                    println "textfile raw data file,skip it"
               
                }
                else{
                    def expRUpdate = new SheetUpdate(entityName:"rawdata_${resource.fileName.tokenize(".").first()}",fileNameVersion:"${resource.fileName}[version:${resource.fileversion?formatter.format(resource.fileversion):"old"}]", state:"export", comment:"Rawdata file: ${resource.fileName} is exported into GelInspector Layout.",dateUpdated:new Date()) //not sure if these are actually used
                    experimentInstance.addToUpdates(expRUpdate) 
                
                    datafile.setBytes(resource.binaryData)
                    //actually, we need the           
                    parserService.parseSpreadsheet(datafile,[experiment:experimentInstance,parserType:parserType])   //parserType ->parserDefType
                    newWorkbook = session.getAttribute("parsedFile")
               
                    session.removeAttribute("parsedFile")
                    // currentExperimentInstance.addToUpdates(expUpdate) //now we don't want to update the workbook each time we export
                    // experimentInstance.binaryData=newWorkbook
                    filenamesstring="$filenamesstring ${resource.fileName} "
                }
            }
            if(checkUser(experimentInstance.author)){
             
                def expUpdate = new SheetUpdate(comment:"Exported workbook and rawdata files: ${filenamesstring}",dateUpdated:new Date()) //not sure if these are actually used
                experimentInstance.addToUpdates(expUpdate) 
                experimentInstance.save(flush:true) 
            }else{
                println "it is not the author" 
            }
       
            //use auxillarySpreadsheetService to split the Gel Inspector files into 3 separate spreadsheets and return a Zip of them
            File zipfile = auxillarySpreadsheetService.createGelInspectorZip(newWorkbook)
        
        
            response.setContentType("application/application/zip") 
            response.setHeader("Content-disposition", "attachment;filename=${zipfile.getName()}")
            response.outputStream <<  zipfile.bytes
            zipfile.delete()
        }catch(Exception e){
            flash.message = e.getMessage()
              redirect(uri:"/exception")
        }

    }
    def finishGelUpload={
        try{
            def duplicateList=[]
            def user=User.get(springSecurityService.principal.id)
            def expId=params.id
//            def gelTemplate=Template.get(params.geltemplate)
//            log.info "gel data update to $expId with template $gelTemplate"
 
            def currentExperimentInstance=Experiment.get(expId);
            def resourcesList=currentExperimentInstance.resources.findAll{it.type=="setup" ||it.type=="gelinspector"}
            session.geldata.each(){e->
          
            
                /**first found same name (name before .) file in the experiment
                 */
             
                def resourceDuplicateHitResource=currentExperimentInstance.resources.find{ it.fileName.tokenize(".").first() ==e.fileName.tokenize(".").first() && it.state=="active"&& it.type=="gelinspector"}        
                if(resourceDuplicateHitResource){
                    resourceDuplicateHitResource.state="inactive"
                    resourceDuplicateHitResource.save(flush:true)
                    duplicateList.add(e.fileName) 
                    SheetUpdate expUpdateduplicate = new SheetUpdate(entityName:"gelinspector_${e.fileName.tokenize(".").first()}",fileNameVersion:"${resourceDuplicateHitResource.fileName}[version:${resourceDuplicateHitResource.fileversion?formatter.format(resourceDuplicateHitResource.fileversion):"old"}]", state:"deactive", dateUpdated:new Date(), comment:"because of new ${e.fileName}, old version of ${resourceDuplicateHitResource.fileName} are automatically deactived")
                    currentExperimentInstance.addToUpdates(expUpdateduplicate)              
                    log.info "${e.fileName} duplicated"
                }
                resourcesList.add(e)
            }
            if(duplicateList.size()>0){
                String duplicate=""

                duplicateList.each(){d->
                    duplicate=duplicate+d+" "
                }
         
                flash.message = "${currentExperimentInstance.filename} already containts gelinspector files: $duplicate. old version are automatically deactived."
            }
             
            //            def resourcesList=currentExperimentInstance.resources.findAll{(it.type=="gelinspector" || it.type=="setup")&&(it.state=="active")}
 
            currentExperimentInstance.resources=resourcesList
            //            currentExperimentInstance.binaryData=auxillarySpreadsheetService.collateDataFiles(resourcesList,"")
            //currentExperimentInstance.gelInspectorTemplateName=gelTemplate.templateName
      
        
  
            def sheetUpdate = new SheetUpdate(entityName:"ExperimentBackUp", state:"backup", comment:"upload gelinspector files for performed experiment ${currentExperimentInstance.filename}", dateUpdated: new Date())
            currentExperimentInstance.addToUpdates(sheetUpdate)
            currentExperimentInstance.save(failOnError: true)
            session.removeAttribute("geldata")
            session.removeAttribute("geldatamap")
            session.removeAttribute("experimentId")
            session.removeAttribute("info")
//            render(text: """<script type="text/javascript"> backToLab(); </script>""", contentType: 'text/javascript')
        }catch(Exception e){
            log.info(e.getMessage())
            session.geldata.each{v ->
                log.info v.id
                v.delete()
            }          
            session.removeAttribute("geldata")
            session.removeAttribute("geldatamap")  
            session.removeAttribute("experimentId")
            session.removeAttribute("info")
        }
     
        session.putAt("active", "1")
        redirect(uri:"/lab")
    
        // redirect(uri:"/lab")
        //        render(text: """<script type="text/javascript"> goBack(); </script>""", contentType: 'text/javascript')
   

    }

    def finishUpload={
        try{
            def duplicateList=[]
            def user=User.get(springSecurityService.principal.id)
            def expId=params.id
            log.info "raw data update to $expId"
            def rawdataList=[]
            def currentExperimentInstance=Experiment.get(expId);    
            def resourcesList=currentExperimentInstance.resources.findAll{ it.type=="rawdata" || it.type=="setup"}
            def rawTextDataTemplate = Template.findByTemplateName("rawTextDataTemplate")
    
            session.rawdata.each(){e->
                def convertedFile
                def convertedFileName
            
                /**first found same name (name before .) file in the experiment
                 */
                println "rawdata $e"
                def resourceDuplicateHitResource=currentExperimentInstance.resources.find{ it.fileName.tokenize(".").first() ==e.fileName.tokenize(".").first() && it.state=="active"}        
                if(resourceDuplicateHitResource){
                    resourceDuplicateHitResource.state="inactive"
                    resourceDuplicateHitResource.save(flush:true)
                    duplicateList.add(e.fileName) 
                    SheetUpdate expUpdateduplicate = new SheetUpdate(entityName:"rawdata_${e.fileName.tokenize(".").first()}",fileNameVersion:"${resourceDuplicateHitResource.fileName}[version:${resourceDuplicateHitResource.fileversion?formatter.format(resourceDuplicateHitResource.fileversion):"old"}]", state:"deactive", dateUpdated:new Date(), comment:"because of new ${e.fileName}, old version of ${resourceDuplicateHitResource.fileName} are automatically deactived")
                    currentExperimentInstance.addToUpdates(expUpdateduplicate)
                    log.info "${e.fileName} duplicated"
                }
           
            
                /**
                 *convert txt file into xls
                 */ 
            
            
                if (e.fileName =~ /.txt$/){
                    session.putAt("ExperimentWorkbook",currentExperimentInstance.binaryData)
                    session.putAt("fileName",e.fileName)
                    println "this is a text file, lets transform it to excel!"
                    parserService.parseSpreadsheet(e.binaryData,[experiment:currentExperimentInstance,parserType:"Raw Data (Text) to Excel"])
                    convertedFileName=e.fileName.replace(".txt",".xls")
                    convertedFile=session.getAttribute("parsedFile")
                    e.state="inactive"
                    session.removeAttribute("parsedFile")
                    session.removeAttribute("parsedFileName")
                    println "updateTxtConvertInfo"
                    SheetUpdate txtSheetUpdate = new SheetUpdate(entityName:"rawdata_${e.fileName.tokenize(".").first()}",fileNameVersion:"${e.fileName}[version:${e.fileversion?formatter.format(e.fileversion):"old"}]", state:"converted to xls", dateUpdated:new Date(), comment:"${e.fileName} is converted to ${convertedFileName},  original ${e.fileName} are automatically deactived")
                    currentExperimentInstance.addToUpdates(txtSheetUpdate)
                }
 
                /**
                 *put in here transforming the text files to new style raw data files
                 */
         
                resourcesList.add(e)
                rawdataList.add(e)
                if (convertedFile!=null){
                    def conversion = new Resource(fileName:convertedFileName, type:"rawdata", binaryData:convertedFile, author:user,state:"active", fileversion: new Date());
                    conversion.save(failOnError:true)
                    SheetUpdate convertSheetUpdate = new SheetUpdate(entityName:"rawdata_${convertedFileName.tokenize(".").first()}",fileNameVersion:"${convertedFileName}[version:${formatter.format(conversion.fileversion)}]", state:"add/create", dateUpdated:conversion.fileversion, comment:"${convertedFileName} is added to ${currentExperimentInstance.filename}")
                    currentExperimentInstance.addToUpdates(convertSheetUpdate)
                    resourcesList.add(conversion)
                    rawdataList.add(conversion)
                }
                println "resourcelistSize ${resourcesList.size()}"
                SheetUpdate expUpdate = new SheetUpdate(entityName:"rawdata_${e.fileName.tokenize(".").first()}", fileNameVersion:"${e.fileName}[version:${formatter.format(e.fileversion)}]", state:"add/create", dateUpdated:new Date(), comment:"raw data file ${e.fileName} added to ${currentExperimentInstance.filename}")
                currentExperimentInstance.addToUpdates(expUpdate)
   
            }
        
       
            log.info resourcesList.size();
            currentExperimentInstance.resources=resourcesList
            currentExperimentInstance.save(failOnError:true,flush:true)
            log.info "raw data resource size ${currentExperimentInstance.resources.size()}"
            //        session.removeAttribute("experimentId")
            session.removeAttribute("rawdata")
            session.removeAttribute("rawdatamap")
            session.removeAttribute("experimentId")
            session.removeAttribute("info")
            
            //        Logger logger=Logger.getLogger("${currentExperimentInstance.filename}")
            //        logger.info("new raw data uploaded ");
            if(duplicateList.size()>0){
                String duplicate=""

                duplicateList.each(){d->
                    duplicate=duplicate+d+" "
                }
         
                flash.message = "${currentExperimentInstance.filename} already containts rawdata files: $duplicate , old version are automatically deactived."
            }
        }catch(Exception e){
          
            log.info(e.getMessage())
            session.removeAttribute("parsedFile")
            session.removeAttribute("parsedFileName")
            session.removeAttribute("experimentId")
            session.removeAttribute("info")
            session.removeAttribute("rawdata")
            session.removeAttribute("rawdatamap")  
        }
        session.putAt("active", "0")
        redirect(uri:"/lab")
        //        render(text: """<script type="text/javascript"> goBack(); </script>""", contentType: 'text/javascript')
   

    }
    def deleteGelResourceDuringUpload={

        log.info "gel data size before ${session.geldata.size()}}"
        def fileName=params.deleteFileName
        log.info "delete $fileName"
        
        try{
            def deleteResource=session.geldatamap.get(fileName)  
            session.geldata.remove(deleteResource)
            session.geldatamap.remove(fileName)
            log.info deleteResource.id
            deleteResource.delete()
            Thread.currentThread().sleep(100)
            log.info "gel data size after ${session.geldata.size()}}"
            log.info "gel data map size after ${session.geldatamap.size()}}"
        }catch(Exception e){
            log.info e.getMessage()
        }
    
     

    }
    def deleteResourceDuringUpload={

        log.info "raw data size before ${session.rawdata.size()}}"
        def fileName=params.deleteFileName
        log.info "delete $fileName"
        
        try{
            def deleteResource=session.rawdatamap.get(fileName)  
            session.rawdata.remove(deleteResource)
            session.rawdatamap.remove(fileName)
            log.info deleteResource.id
            deleteResource.delete()
            Thread.currentThread().sleep(100)
            log.info "raw data size after ${session.rawdata.size()}}"
            log.info "raw data map size after ${session.rawdatamap.size()}}"
        }catch(Exception e){
            log.info e.getMessage()
        }
    
     

    }
    def traditionalUploadAction={
        def expId=params.experimentId
        def expType=params.experimentType
        println params.experimentId
        def user=User.get(springSecurityService.principal.id)
        def f = request.getFile('myRawData')
        def webRootDir = servletContext.getRealPath("/")
        def savePath = webRootDir+"rawdata"
        def dir 		= new File(savePath)
        if(!dir.exists()){
            dir.mkdirs()
        }
        def filePath=savePath
        def fileName="null"
        def infoMessage="";
        if(session.getAttribute("info")){
            infoMessage=session.getAttribute("info")
        }
   
        if((!f.empty) && (expType=="new")) {
            print "upload raw data "+f.getOriginalFilename()
            fileName=f.getOriginalFilename()
            def proved=FileNameValidator.validateRawDataFileName(fileName)
            if(proved){
                filePath=filePath+"/"+System.nanoTime().toString()+"${f.getOriginalFilename()}"
                File newFile=new File(filePath)
                f.transferTo(newFile)
                def  rawDataResource=new Resource(fileName:fileName, type:"rawdata", binaryData: newFile.bytes, author:user,state:"active", fileversion: new Date());
                rawDataResource.save(failOnError: true);
                if(!session.rawdata){
                    println "initial "
                    def resourceList=[]
                    def resourceNameMap=[:]
                    resourceList.add(rawDataResource)
                    resourceNameMap.put(fileName, rawDataResource)
                   
                    session.rawdatamap=resourceNameMap
                    session.rawdata=resourceList
             
                    Thread.currentThread().sleep(50)
                }else{
                    println "add"
                    if(!session.rawdatamap.containsKey(fileName)){
                        session.rawdata.add(rawDataResource)
                        session.rawdatamap.put(fileName, rawDataResource)
                    }else{
                        rawDataResource.delete()
                        log.info"duplicate"
                        flash.message="$fileName duplicate "
                    
                    } 
                
                    Thread.currentThread().sleep(50)
                }
                newFile.delete()
                infoMessage=infoMessage+fileName+" "
                session.info= infoMessage
            }else{
                log.info "$fileName does not match the raw data name conventeion. "
                flash.message="$fileName does not match the raw data name conventeion. "
            }
        
        }else if((!f.empty) && (expType=="old")){
            print "upload gel inspector file "+f.getOriginalFilename() 
            fileName=f.getOriginalFilename()
            def proved=true
            if(proved){
                filePath=filePath+"/"+System.nanoTime().toString()+"${f.getOriginalFilename()}"
                File newFile=new File(filePath)
                f.transferTo(newFile)
                def  gelDataResource=new Resource(fileName:fileName, type:"gelinspector", binaryData: newFile.bytes, author:user,state:"active", fileversion: new Date());
                gelDataResource.save(failOnError: true);
                if(!session.geldata){
                    println "initial gel"
                    def resourceList=[]
                    def resourceNameMap=[:]
                    resourceList.add(gelDataResource)
                    resourceNameMap.put(fileName, gelDataResource)
                   
                    session.geldatamap=resourceNameMap
                    session.geldata=resourceList
             
                    Thread.currentThread().sleep(50)
                }else{
                    println "add gel"
                    if(!session.geldatamap.containsKey(fileName)){
                        session.geldata.add(gelDataResource)
                        session.geldatamap.put(fileName, gelDataResource)
                    }else{
                        gelDataResource.delete()
                        log.info"duplicate"
                        flash.message="$fileName duplicate "
                    
                    } 
                
                    Thread.currentThread().sleep(50)
                }
                newFile.delete()
                infoMessage=infoMessage+fileName+" "
                session.info= infoMessage
            }else{
                log.info "$fileName does not match the raw data name conventeion. "
                flash.message="$fileName does not match the raw data name conventeion. "
            }
            
            
            
            
            
        } else{
            log.info "empty file! or unsure experiment type !"
            flash.message="empty file, please check"
        }
      
   
        def urlString="/lab/uploadr?experimentType="+expType+"&&experimentId="+expId
        redirect(uri:urlString )
        
        //        render(text: """<script type="text/javascript"> writeInfo("$filetName ready!"); </script>""", contentType: 'text/javascript')

    }
    
    
    def uploadrGelAction={
        log.info "gel upload"
        def user=User.get(springSecurityService.principal.id)
        byte[] buffer = new byte[BUFF_SIZE];
        def contentType	= request.getHeader("Content-Type") as String
        def fileName    = request.getHeader('X-File-Name') as String
        def fileSize 	= request.getHeader('X-File-Size') as Long
        def name 		= request.getHeader('X-Uploadr-Name') as String
        def info		= session.getAttribute('uploadr2')
        //def savePath	= ((name && info && info.get(name) && info.get(name).path) ? info.get(name).path : "/tmp") as String
        def webRootDir = servletContext.getRealPath("/")
        def savePath = webRootDir+"gelinspectordata"
        def dir 		= new File(savePath)
        if(!dir.exists()){
            dir.mkdirs()
        }
        log.info fileName
        log.info savePath
        def performedResource
        // def  rawDataResource
        def resourceList
        Map resourceNameMap
        int status      = 0
        def statusText  = ""
        def proved=FileNameValidator.validateGelDataFileName(fileName)
        if(proved){
            //def file		= new File(savePath,fileName)
        
            File file    = File.createTempFile(System.nanoTime().toString(),fileName,dir)
            int dot         = 0
            def namePart    = ""
            def extension   = ""
            def testName    = ""
            def testIterator= 1

            response.contentType    = 'application/json'

            InputStream inStream = null
            OutputStream outStream = null

            // handle file upload
            try {
                println "hello uploading gel"
                inStream = request.getInputStream()
                outStream = new FileOutputStream(file)

                // ByteArrayOutputStream baos = new ByteArrayOutputStream()

                while (true) {
                    synchronized (buffer) {
                        int amountRead = inStream.read(buffer);

                        if (amountRead == -1) {

                            break
                        }
                        outStream.write(buffer, 0, amountRead)

                    }

                }
                outStream.flush()
                status      = 200
                statusText  = "'gel ${fileName}' upload successful!"
                   
                //            def currentExperimentInstance=Experiment.get(expId);
                performedResource=new Resource(fileName:fileName, type:"gelinspector", binaryData:file.bytes, author:user,state:"active", fileversion: new Date());
                performedResource.save(failOnError: true);
           
                if(!session.geldata){
                    println "initial gel"
                    resourceList=[]
                    resourceNameMap=[:]
                    resourceList.add(performedResource)
                    resourceNameMap.put(fileName, performedResource)
                    session.geldatamap=resourceNameMap
                    session.geldata=resourceList          
                    Thread.currentThread().sleep(80)
                }else{
                    println "add gel"
                    if(!session.geldatamap.containsKey(fileName)){
                        session.geldata.add(performedResource)
                        session.geldatamap.put(fileName,  performedResource)
                    }else{
                        performedResource.delete()
                        println "duplicate"
                        status      = 500
                        statusText  = "duplicated"
                    } 
                
                    Thread.currentThread().sleep(80)
                }
            


            } catch (Exception e) {
                // whoops, looks like something went wrong
                log.error  e.getMessage()
    
                println "hello"
                status      = 500
                statusText  = e.getMessage()
                if(session.geldatamap.containsKey(fileName)){
                    session.geldata.remove(performedResource)
                    session.geldatamap.remove(fileName)
                }
            } finally {
                if (inStream != null) inStream.close()
                if (outStream != null) outStream.close()
               
            } 
            
            // make sure the file was properly written
            if (status == 200) {
                // whoops, looks like the transfer was aborted!
            
                if(fileSize > file.size()){
                    status      = 500
                    //statusText  = "'${file.name}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
                    statusText  = "'${fileName}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"}
            }else{
                if(session.geldatamap.containsKey(fileName)){
                    session.geldata.remove(performedResource)
                    session.geldatamap.remove(fileName)
                }
            }
            
            
           
            file.delete()
        }else{
            log.info "'${fileName}' does not match the raw data name conventeion. "
            status      = 500
            //statusText  = "'${file.name}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
            statusText  = "'${fileName}' does not match the raw data name conventeion " 
        }
        


    
        // got an error of some sorts?
        //        if (status != 200) {
        //            // then -try to- delete the file
        //            try {
        //                file.delete()
        //            } catch (Exception e) {
        //            }
        //        }
        // render json response
        //session.putAt("rawdatafile",file)
        flash.message="successfullly uploaded file havent sent to experiment yet"
 
        response.setStatus(status, statusText)
 
        render([written: (status == 200), fileName: fileName, status: status, statusText: statusText] as JSON)
 

    }

    def uploadrAction={
     
        def user=User.get(springSecurityService.principal.id)
        byte[] buffer = new byte[BUFF_SIZE];
        def contentType	= request.getHeader("Content-Type") as String
        def fileName    = request.getHeader('X-File-Name') as String
        def fileSize 	= request.getHeader('X-File-Size') as Long
        def name 		= request.getHeader('X-Uploadr-Name') as String
        def info		= session.getAttribute('uploadr')
        //def savePath	= ((name && info && info.get(name) && info.get(name).path) ? info.get(name).path : "/tmp") as String
        def webRootDir = servletContext.getRealPath("/")
        def savePath = webRootDir+"rawdata"
        def dir 		= new File(savePath)
        if(!dir.exists()){
            dir.mkdirs()
        }
        log.info fileName
        log.info savePath
        def  rawDataResource
        def resourceList
        Map resourceNameMap
        int status      = 0
        def statusText  = ""
        def proved=FileNameValidator.validateRawDataFileName(fileName)
        if(proved){
            //def file		= new File(savePath,fileName)
        
            File file    = File.createTempFile(System.nanoTime().toString(),fileName,dir)
            int dot         = 0
            def namePart    = ""
            def extension   = ""
            def testName    = ""
            def testIterator= 1

            response.contentType    = 'application/json'

            InputStream inStream = null
            OutputStream outStream = null

            // handle file upload
            try {
                println "hello uploading"
                inStream = request.getInputStream()
                outStream = new FileOutputStream(file)

                // ByteArrayOutputStream baos = new ByteArrayOutputStream()

                while (true) {
                    synchronized (buffer) {
                        int amountRead = inStream.read(buffer);

                        if (amountRead == -1) {

                            break
                        }
                        outStream.write(buffer, 0, amountRead)

                    }

                }
                outStream.flush()
                status      = 200
                statusText  = "'${fileName}' upload successful!"
                   
                //            def currentExperimentInstance=Experiment.get(expId);
                rawDataResource=new Resource(fileName:fileName, type:"rawdata", binaryData:file.bytes, author:user,state:"active", fileversion: new Date());
                rawDataResource.save(failOnError: true);
           
                if(!session.rawdata){
                    println "initial "
                    resourceList=[]
                    resourceNameMap=[:]
                    resourceList.add(rawDataResource)
                    resourceNameMap.put(fileName, rawDataResource)
                    session.rawdatamap=resourceNameMap
                    session.rawdata=resourceList
             
                    Thread.currentThread().sleep(50)
                }else{
                    println "add"
                    if(!session.rawdatamap.containsKey(fileName)){
                        session.rawdata.add(rawDataResource)
                        session.rawdatamap.put(fileName, rawDataResource)
                    }else{
                        rawDataResource.delete()
                        println "duplicate"
                        status      = 500
                        statusText  = "duplicated"
                    } 
                
                    Thread.currentThread().sleep(50)
                }
            


            } catch (Exception e) {
                // whoops, looks like something went wrong
                log.error  e.getMessage()
    
                println "hello"
                status      = 500
                statusText  = e.getMessage()
                if(session.rawdatamap.containsKey(fileName)){
                    session.rawdata.remove(rawDataResource)
                    session.rawdatamap.remove(fileName)
                }
            } finally {
                if (inStream != null) inStream.close()
                if (outStream != null) outStream.close()
               
            } 
            
            // make sure the file was properly written
            if (status == 200) {
                // whoops, looks like the transfer was aborted!
            
                if(fileSize > file.size()){
                    status      = 500
                    //statusText  = "'${file.name}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
                    statusText  = "'${fileName}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"}
            }else{
                if(session.rawdatamap.containsKey(fileName)){
                    session.rawdata.remove(rawDataResource)
                    session.rawdatamap.remove(fileName)
                }
            }
            
            
           
            file.delete()
        }else{
            log.info "'${fileName}' does not match the raw data name conventeion. "
            status      = 500
            //statusText  = "'${file.name}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
            statusText  = "'${fileName}' does not match the raw data name conventeion " 
        }
        

        flash.message="successfullly uploaded file havent sent to experiment yet"
 
        response.setStatus(status, statusText)
 
        render([written: (status == 200), fileName: fileName, status: status, statusText: statusText] as JSON)
 

    }

    def downloadRawData(){
        def experimentInstance =Experiment.get(params.id)
        
        def dataFilesList=experimentInstance.resources.findAll{ it.type=="rawdata" && it.state=="active" }
        
        def allDataWorkbookBA=auxillarySpreadsheetService.collateDataFiles(dataFilesList,"raw_data_")
        
        response.setContentType("application/vnd.ms-excel") 
        response.setHeader("Content-disposition", "attachment;filename=rawdata_${experimentInstance.filename.trim().replaceAll("\\s+", "_")}.xls")
        response.outputStream <<  allDataWorkbookBA

    }
    
    
    
}
