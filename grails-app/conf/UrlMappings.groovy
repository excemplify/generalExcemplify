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
class UrlMappings {

    static mappings = {
		"/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
             "/"(view: '/ui/welcome')
         "/exception"(view: '/ui/exception')
//         "/public"(view: '/ui/public', controller:'org/hits/ui/experiment')
         "/admin/all"(view: '/ui/admin/all', controller:'org/hits/ui/experiment')
           "/admin" (view: '/ui/admin/index', controller:'org/hits/ui/admin')
          "/lab" (view: '/ui/user/index', controller:'org/hits/ui/lab')
          "/lab/uploadr" (view:'/ui/user/uploadr', controller:'org/hits/ui/experiment')
//         "/lab/logview" (view:'/ui/user/logview', controller:'org/hits/ui/experiment')
//        "/lab/timelineview" (view:'/ui/user/timelineview', controller:'org/hits/ui/experiment')
           "/imprint" (view:'/ui/imprint')
       
          "500"(view:'/error')
         "404"(view:'/error')
         "/login/$action?"(controller: "login")
        "/logout/$action?"(controller: "logout")
       
    }
}
