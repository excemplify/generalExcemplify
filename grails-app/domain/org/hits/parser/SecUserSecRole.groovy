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

import org.apache.commons.lang.builder.HashCodeBuilder

class SecUserSecRole implements Serializable {

	SecUser secUser
	SecRole secRole

	boolean equals(other) {
		if (!(other instanceof SecUserSecRole)) {
			return false
		}

		other.secUser?.id == secUser?.id &&
			other.secRole?.id == secRole?.id
	}

	int hashCode() {
		def builder = new HashCodeBuilder()
		if (secUser) builder.append(secUser.id)
		if (secRole) builder.append(secRole.id)
		builder.toHashCode()
	}

	static SecUserSecRole get(long secUserId, long secRoleId) {
		find 'from SecUserSecRole where secUser.id=:secUserId and secRole.id=:secRoleId',
			[secUserId: secUserId, secRoleId: secRoleId]
	}

	static SecUserSecRole create(SecUser secUser, SecRole secRole, boolean flush = false) {
		new SecUserSecRole(secUser: secUser, secRole: secRole).save(flush: flush, insert: true)
	}

	static boolean remove(SecUser secUser, SecRole secRole, boolean flush = false) {
		SecUserSecRole instance = SecUserSecRole.findBySecUserAndSecRole(secUser, secRole)
		if (!instance) {
			return false
		}

		instance.delete(flush: flush)
		true
	}

	static void removeAll(SecUser secUser) {
		executeUpdate 'DELETE FROM SecUserSecRole WHERE secUser=:secUser', [secUser: secUser]
	}

	static void removeAll(SecRole secRole) {
		executeUpdate 'DELETE FROM SecUserSecRole WHERE secRole=:secRole', [secRole: secRole]
	}

	static mapping = {
		id composite: ['secRole', 'secUser']
		version false
	}
}
