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
import java.util.zip.ZipOutputStream
import java.util.zip.ZipEntry
import java.util.logging.Logger;
import org.hits.parser.SheetUpdate
import java.text.SimpleDateFormat

class ResourceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
    def index() {
        redirect(uri:"/")
    }

    def list() {
        log.info params.expId
        log.info params.type
        def currentExperimentInstance=Experiment.get(params.expId)
        if(currentExperimentInstance){
            def resourcesList=currentExperimentInstance.resources.findAll{ it.type== params.type}
            //            params.max = Math.min(params.max ? params.int('max') : 10, 100)
            [resourceInstanceList:resourcesList, resourceInstanceTotal: resourcesList.size(), experimentName:currentExperimentInstance.filename, experimentId:currentExperimentInstance.id]
  
        }else{
            log.info "Experiment not found."
            flash.message="We Can Not Retrive Raw Data Files Because Experiment With Id \"$expId\"  not found."  
            redirect(uri:"/lab")
        }
    }
    //
    //    def create() {
    //        [resourceInstance: new Resource(params)]
    //    }
    //
    //    def save() {
    //        def resourceInstance = new Resource(params)
    //        if (!resourceInstance.save(flush: true)) {
    //            render(view: "create", model: [resourceInstance: resourceInstance])
    //            return
    //        }
    //
    //        flash.message = message(code: 'default.created.message', args: [message(code: 'resource.label', default: 'Resource'), resourceInstance.id])
    //        redirect(action: "show", id: resourceInstance.id)
    //    }
    def deactive={
        def resourceInstance = Resource.get(params.id)
        resourceInstance.state="inactive"
        resourceInstance.save(flush: true)
        def currentExperimentInstance=Experiment.get(params.expId)
        SheetUpdate expUpdate = new SheetUpdate(entityName:"rawdata_${resourceInstance.fileName.tokenize(".").first()}", fileNameVersion:"${resourceInstance.fileName}[version:${formatter.format(resourceInstance.fileversion)}]", state:"deactive", dateUpdated:new Date(), comment:" ${resourceInstance.fileName} version ${resourceInstance.fileversion} is deactived")             
        currentExperimentInstance.addToUpdates(expUpdate) 
        currentExperimentInstance.save(flush:true) 
        
        
        redirect(action: "list", params:params)
        
    }
    def active={
        def resourceInstance = Resource.get(params.id)
        def resourceName=resourceInstance.fileName
        def currentExperimentInstance=Experiment.get(params.expId)
        if(resourceName.endsWith(".txt")){
            
            flash.message="Such $resourceName raw data file has already transferred into xls."
        }else{
            /*find active resoure which have same name
             ***/
            def resourceDuplicateHitResource=currentExperimentInstance.resources.find{ it.fileName.tokenize(".").first() ==resourceInstance.fileName.tokenize(".").first() && it.state=="active"}     
            if(resourceDuplicateHitResource){
                log.info "try to active a file which have duplicate active version"
                resourceDuplicateHitResource.state="inactive" 
                resourceDuplicateHitResource.save(flush: true)
                SheetUpdate expUpdate1 = new SheetUpdate(entityName:"rawdata_${resourceDuplicateHitResource.fileName.tokenize(".").first()}", fileNameVersion:"${resourceDuplicateHitResource.fileName}[version:${formatter.format(resourceDuplicateHitResource.fileversion)}]", state:"deactive", dateUpdated:new Date(), comment:"because you want to active another same name raw data file, ${resourceDuplicateHitResource.fileName} version ${resourceDuplicateHitResource.fileversion} is automatically deactived")
                currentExperimentInstance.addToUpdates(expUpdate1) 
            }
          
            resourceInstance.state="active"
            resourceInstance.save(flush: true)
       
            
             SheetUpdate expUpdate = new SheetUpdate(entityName:"rawdata_${resourceInstance.fileName.tokenize(".").first()}", fileNameVersion:"${resourceInstance.fileName}[version:${formatter.format(resourceInstance.fileversion)}]", state:"active", dateUpdated:new Date(), comment:" ${resourceInstance.fileName} version ${resourceInstance.fileversion} is actived")
            currentExperimentInstance.addToUpdates(expUpdate) 
        }
 
        redirect(action: "list", params:params)
        
    }
    def download={
        def resourceInstance = Resource.get(params.id)

        try {
            response.setContentType("application/vnd.ms-excel")
            response.setHeader("Content-disposition", "attachment;filename=${resourceInstance.fileName}")
            response.outputStream << resourceInstance.binaryData
        

        }
        catch(Exception ex){
            log.error ex
            flash.message="Exceptions Occur During Raw Data Downloading, Because of $ex"
            redirect(uri:"/lab")
           
        }
       
      
    
    }


    //    def downloadRawDataZip={
    //        log.info "download zip"
    //        log.info "${params.id}"
    //        def experimentInstance=Experiment.get(params.id)
    //
    //        def zipFileName="${experimentInstance.filename}_rawdata.zip"
    //        println zipFileName
    //        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(zipFileName))
    //        def rawDataFiles=experimentInstance.resources
    //        def buffer = new byte[1024]
    //        rawDataFiles.each(){resource->
    //            zipFile.putNextEntry(new ZipEntry(resource.fileName))
    //            def b=new ByteArrayInputStream(resource.binaryData)
    //            zipFile << b
    //            zipFile.closeEntry()
    //
    //        }
    //
    //        zipFile.close()
    //        response.setContentType("application/octet-stream")
    //        response.setHeader("Content-disposition", "attachment;filename=${zipFileName}")
    //        response.outputStream << zipFile
    //
    //
    //    }



    //
    //    def show() {
    //        def resourceInstance = Resource.get(params.id)
    //        if (!resourceInstance) {
    //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        [resourceInstance: resourceInstance]
    //    }

    //    def edit() {
    //        def resourceInstance = Resource.get(params.id)
    //        if (!resourceInstance) {
    //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        [resourceInstance: resourceInstance]
    //    }
    //
    //    def update() {
    //        def resourceInstance = Resource.get(params.id)
    //        if (!resourceInstance) {
    //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
    //            redirect(action: "list")
    //            return
    //        }
    //
    //        if (params.version) {
    //            def version = params.version.toLong()
    //            if (resourceInstance.version > version) {
    //                resourceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
    //                    [message(code: 'resource.label', default: 'Resource')] as Object[],
    //                          "Another user has updated this Resource while you were editing")
    //                render(view: "edit", model: [resourceInstance: resourceInstance])
    //                return
    //            }
    //        }
    //
    //        resourceInstance.properties = params
    //
    //        if (!resourceInstance.save(flush: true)) {
    //            render(view: "edit", model: [resourceInstance: resourceInstance])
    //            return
    //        }
    //
    //        flash.message = message(code: 'default.updated.message', args: [message(code: 'resource.label', default: 'Resource'), resourceInstance.id])
    //        redirect(action: "show", id: resourceInstance.id)
    //    }

    def delete() {
        
        flash.message "We do not encouraging delete raw data file, you can inactive it if it does not fit any more."
        redirect(uri:"/lab")
        //        
        //        def resourceInstance = Resource.get(params.id)
        //        if (!resourceInstance) {
        //            
        //            flash.message = message(code: 'default.not.found.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
        //            redirect(action: "list")
        //            return
        //        }
        //
        //        try {
        //            resourceInstance.delete(flush: true)
        //            flash.message = message(code: 'default.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
        //            redirect(action: "list")
        //        }
        //        catch (DataIntegrityViolationException e) {
        //            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'resource.label', default: 'Resource'), params.id])
        //            redirect(action: "show", id: params.id)
        //        }
    }
}
