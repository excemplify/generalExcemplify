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
// Place your Spring DSL code here
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter

	 
beans = {
    sessionRegistry(SessionRegistryImpl)
	 
    concurrencyFilter(ConcurrentSessionFilter) {
        sessionRegistry = sessionRegistry
        logoutHandlers = [ref("rememberMeServices"), ref("securityContextLogoutHandler")]
        expiredUrl='/login/concurrentSession'
    }
    concurrentSessionControlStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
        alwaysCreateSession = true
        exceptionIfMaximumExceeded = false
        maximumSessions = 1
    }
}