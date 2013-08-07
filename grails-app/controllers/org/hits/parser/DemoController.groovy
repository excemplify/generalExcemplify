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


class DemoController {

    def parserService
    
    static final int BUFF_SIZE = 100000;
    static defaultAction = "demo1"
    def index() { }
    
    
    def demo1(){
        def parserDefList = parserService.getAllParserNames()
        def defaultParser = parserDefList.get(7)
        return [parserDefList:parserDefList,defaultParser:defaultParser]
    }
    
    def save1(){
        File file =session.getAt("file")
    //println "file from session"+file.name
    //spreadsheetInstance.filename = session.getAt("fileName")
        session.removeAttribute("parsedFile")
        session.removeAttribute("parsedFileName")
        
     //   def uploadedFile = request.getFile('payload')
      //  def webRootDir = servletContext.getRealPath("/")
     // def userDir = new File(webRootDir, "/payloadspreadsheet/demo")
     // userDir.mkdirs()
     // File file = new File( userDir, uploadedFile.originalFilename)
     // uploadedFile.transferTo( file)
      parserService.parseSpreadsheet(file,params)
      if (session.getAttribute("parsedFile")){
         flash.message="file successfully parsed"
         session.removeAttribute("file")
        session.removeAttribute("fileName")
         redirect (action:'download')
         render(view:'demo1')
        }
     else   render (view:'demo1')
        
    }
    
    def download(){
        return [parsedFileName:session.getAttribute("parsedFileName")]
    }
    
    def downloadParsing(){ 
        
        def parsedFile = session.getAttribute("parsedFile")
        def parsedFileName=session.getAttribute("parsedFileName")
        
       try {
           
            if (parsedFile){
                println "blah blah"
                session.removeAttribute("parsedFile")
                session.removeAttribute("parsedFileName")
          
                response.setContentType("application/vnd.ms-excel") 
                response.setHeader("Content-disposition", "attachment;filename=${parsedFileName}")
                response.outputStream << parsedFile
                response.outputStream.flush()
            }
            
            }
            catch(Exception ex){
                flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'spreadsheet.label', default: 'Spreadsheet'), params.id])}"
               
            } 
            redirect(action: 'demo1')
           
            
        }
       
        
    
    
    
    def demo2(){
        def parserDefList = parserService.getAllParserNames()
       
        return [parserDefList:parserDefList]
    }
    
    def save2(){
        
    }
    
    def uploadrAction={
                
                 byte[] buffer = new byte[BUFF_SIZE];
        
                def contentType	= request.getHeader("Content-Type") as String
		def fileName    = request.getHeader('X-File-Name') as String
		def fileSize 	= request.getHeader('X-File-Size') as Long
		def name 		= request.getHeader('X-Uploadr-Name') as String
		def info		= session.getAttribute('uploadr')
		//def savePath	= ((name && info && info.get(name) && info.get(name).path) ? info.get(name).path : "/tmp") as String
                def webRootDir = servletContext.getRealPath("/")
                def savePath = webRootDir+"payload/demo"
                
     
		def dir 		= new File(savePath)
               println fileName
               println savePath
		//def file		= new File(savePath,fileName)
                def file    = File.createTempFile(System.nanoTime().toString(),fileName,dir)
                println fileName
                session.putAt("fileName",fileName)
                int dot         = 0
		def namePart    = ""
		def extension   = ""
		def testName    = ""
		def testIterator= 1
		int status      = 0
		def statusText  = ""
                
             
        // set response content type to json
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
           
        
      
		} catch (Exception e) {
			// whoops, looks like something went wrong
                        println "hello"
			status      = 500
			statusText  = e.getMessage()
		} finally {
			if (inStream != null) inStream.close()
			if (outStream != null) outStream.close()
		}

		// make sure the file was properly written
		if (status == 200 && fileSize > file.size()) {
			// whoops, looks like the transfer was aborted!
			status      = 500
			//statusText  = "'${file.name}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
                        statusText  = "'${fileName}' transfer incomplete, received ${file.size()} of ${fileSize} bytes"
		}

		// got an error of some sorts?
		if (status != 200) {
			// then -try to- delete the file
			try {
				file.delete()
			} catch (Exception e) { }
		}
        // render json response
                session.putAt("file",file)
		response.setStatus(status, statusText)
                flash.message="$fileName successfullly uploaded"
		render([written: (status == 200), fileName: fileName, status: status, statusText: statusText] as JSON)
    }
}
