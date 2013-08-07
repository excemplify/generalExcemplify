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
import grails.converters.*
import org.hits.parser.SecUser
import org.hits.parser.core.ParserConfigException
import org.hits.parser.core.ParsingException

class SpreadsheetController {

    def springSecurityService
    
    def parserService
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    static final int BUFF_SIZE = 100000;
 //   static final byte[] buffer = new byte[BUFF_SIZE];
    
       
    
    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        def user = User.get(springSecurityService.principal.id)
  
        if(!params.max) params.max = 10
     
                
        [spreadsheetInstanceList: Spreadsheet.findAllByAuthor(user,params), spreadsheetInstanceTotal: Spreadsheet.countByAuthor(user),params:params]
    }

    def create = {
        def spreadsheetInstance = new Spreadsheet()
        spreadsheetInstance.properties = params
        def parserDefList = parserService.getAllParserNames()
       
        println parserDefList
        return [spreadsheetInstance: spreadsheetInstance, parserDefList:parserDefList]
    }
    
def save = {
    def spreadsheetInstance = new Spreadsheet(params)
    spreadsheetInstance.author = User.get(springSecurityService.principal.id)
    spreadsheetInstance.createdOn = new Date()
    
    //handle uploaded file
    
    def uploadedFile = request.getFile('payload')
        def webRootDir = servletContext.getRealPath("/")
      def userDir = new File(webRootDir, "/payloadspreadsheet/${spreadsheetInstance.author}")
      userDir.mkdirs()
      
      def uniquefilename="${System.nanoTime()}_${uploadedFile.originalFilename}"
      File file = new File( userDir, uniquefilename)
      uploadedFile.transferTo( file)
      session.putValue(uniquefilename,"fileName")
   //def uploadedFile = UploadedFile.get(params.fileId)
    //def webRootDir = servletContext.getRealPath("/")
   // File file =session.getAt("file")
    //println "file from session"+file.name
    //spreadsheetInstance.filename = session.getAt("fileName")
     spreadsheetInstance.filename=uploadedFile.originalFilename
        
    def rows =[]
    println session
  //  if(!uploadedFile.empty){
    //println file.size()
    spreadsheetInstance.binaryData = file.bytes
    //if (!file==null){   
   
    if(!spreadsheetInstance.hasErrors() && spreadsheetInstance.save()) {
        flash.message = "Spreadsheet ${spreadsheetInstance.id} created"
               
       try{   
           
       session.removeAttribute("parsedFile")
       session.removeAttribute("parsedFileName")
       
      // parserService.parseSpreadsheet(file, params.parserType)
       //slightly change this method here to add the template, mapping to/from info
       parserService.parseSpreadsheet(file,params)
        session.removeAttribute("file")
        session.removeAttribute("fileName")
       }
     //  catch(ParserConfigException pcex){
      //     println pcex.message
      //     println "caught a ParserConfigException"
      //     flash.message=pcex.message
      // }
       catch(ParserConfigException ex){
           println ex.message
          // flash.message="Error in parsing spreadsheet"
          flash.message=ex.message
       }
       
       // redirect(action:show,id:spreadsheetInstance.id)
     redirect(action:show, id:spreadsheetInstance.id)
       
    }
    else {
        render(view:'create',model:[spreadsheetInstance:spreadsheetInstance])
    }
}
    

    def show = {
        
        
        def spreadsheetInstance = Spreadsheet.get(params.id)
        
        if (!spreadsheetInstance || !checkUser(spreadsheetInstance.author)) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
            redirect(action: "list")
                     
            
        }
        
        else {
            println "hello"
            if (session.blankcells==true){
                flash.message = "Blank cells detected, see downloaded sheet for details"
                session.blankcells = null
            }
           
            [spreadsheetInstance: spreadsheetInstance]
                   
            }
        }
    

    def edit = {
        def spreadsheetInstance = Spreadsheet.get(params.id)
        
        if (!spreadsheetInstance || spreadsheetInstance.author!=User.get(springSecurityService.principal.id)) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [spreadsheetInstance: spreadsheetInstance]
        }
    }

    def update = {
        def spreadsheetInstance = Spreadsheet.get(params.id)
        def sheetUpdate = new SheetUpdate(comment: params.comment, dateUpdated: new Date())
        if (spreadsheetInstance && checkUser(spreadsheetInstance.author )) {
            
            spreadsheetInstance.properties = params
            spreadsheetInstance.addToUpdates(sheetUpdate)
           // spreadsheetInstance.binaryData = request.getFile('payload').getBytes()
           spreadsheetInstance.binaryData = session.getAt("file").bytes
            
            if (!spreadsheetInstance.hasErrors() && spreadsheetInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), spreadsheetInstance.id])}"
                redirect(action: "show", id: spreadsheetInstance.id)
            }
            else {
                render(view: "edit", model: [spreadsheetInstance: spreadsheetInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def spreadsheetInstance = Spreadsheet.get(params.id)
        if (spreadsheetInstance && checkUser(spreadsheetInstance.author) ) {
            try {
                spreadsheetInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
            redirect(action: "list")
        }
    }
    
    
    def download={
        def spreadsheetInstance = Spreadsheet.get(params.id)
        if (spreadsheetInstance && checkUser(spreadsheetInstance.author )) {
            try {
            
            response.setContentType("application/octet-stream") 
            response.setHeader("Content-disposition", "attachment;filename=${spreadsheetInstance.filename}_downloaded")
            response.outputStream << spreadsheetInstance.binaryData
            
            }
            catch(Exception ex){
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
              redirect(action: "list")   
            }
        }
        else{
              flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
              redirect(action: "list")   
            }
    }

    def saveParsingAsUpdate={
        println "hello, trying to save our parsing"
     def spreadsheetInstance = Spreadsheet.get(params.id)
        def parsedFile = session.parsedFile
        if (spreadsheetInstance && checkUser(spreadsheetInstance.author)){
       try {
           
            if (parsedFile){
                println "saving as update"
                session.removeAttribute("parsedFile")
                def sheetUpdate = new SheetUpdate(comment: "parsing test1", dateUpdated: new Date())
                if (spreadsheetInstance && checkUser(spreadsheetInstance.author )) {
            
         
                spreadsheetInstance.addToUpdates(sheetUpdate)
           // spreadsheetInstance.binaryData = request.getFile('payload').getBytes()
                spreadsheetInstance.binaryData = parsedFile
            
               
                }
            }
            flash.message="update successful"
            redirect(action: "show", id:params.id)
       }
       catch(Exception ex){
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
                redirect(action: "show", id:params.id)   
            } 
        }
        else{
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
              redirect(action: "list")   
        }
        
       
    }
    
    def addRawData={
       def spreadsheetInstance = Spreadsheet.get(params.id) 
       def parserDefList = parserService.getAllParserNames()
       
       
        return [spreadsheetInstance: spreadsheetInstance, parserDefList:parserDefList]
    }                    
                        
   def saveRawData={
      def spreadsheetInstance = Spreadsheet.get(params.id)
      def uploadedFile = request.getFile('payload')
      def webRootDir = servletContext.getRealPath("/")
      def userDir = new File(webRootDir, "/payloadspreadsheet/${spreadsheetInstance.author}")
      userDir.mkdirs()
      
      def uniquefilename="${System.nanoTime()}_${uploadedFile.originalFilename}"
      println "file name is ${uploadedFile.originalFilename}"
      session.putValue("fileName",uploadedFile.originalFilename) 
      File file = new File( userDir, uniquefilename)
      uploadedFile.transferTo( file)
      
      session.putValue("ExperimentWorkbook",spreadsheetInstance.binaryData)
      parserService.parseSpreadsheet(file,params)
       session.removeAttribute("file")
       session.removeAttribute("fileName")
       def sheetUpdate = new SheetUpdate(comment:"testing adding raw data", dateUpdated:new Date())
       spreadsheetInstance.addToUpdates(sheetUpdate)
       spreadsheetInstance.binaryData=session.getAttribute("parsedFile")
       
      redirect(action:'show')
   }   
                        
                        
    def downloadParsing={ 
        def spreadsheetInstance = Spreadsheet.get(params.id)
        def parsedFile = session.parsedFile
        if (spreadsheetInstance && checkUser(spreadsheetInstance.author)){
       try {
           
            if (parsedFile){
                println "blah blah"
                session.removeAttribute("parsedFile")
          
                response.setContentType("application/octet-stream") 
                response.setHeader("Content-disposition", "attachment;filename=parsed_${spreadsheetInstance.filename}")
                response.outputStream << parsedFile
            }
            }
            catch(Exception ex){
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
                redirect(action: "show", id:params.id)   
            } 
        }
        else{
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
              redirect(action: "list")   
        }
        
    }
    
    def checkUser={author->
        return (author==User.get(springSecurityService.principal.id))
    }
    
   
}
