/*******************************************************************************
 * Copyright 2014 The MITRE Corporation
 *   and the MIT Kerberos and Internet Trust Consortium
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.nouveauxterritoires.eterritoires.identityserver.openid.connect.repositoty;

import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.model.TaxeUserInfo;
import java.util.Collection;
import org.mitre.openid.connect.model.UserInfo;

import org.mitre.openid.connect.repository.UserInfoRepository;

/**
 * UserInfo repository interface
 * 
 * @author Michael Joseph Walsh
 *
 */
public interface TaxeUserInfoRepository{

	/**
	 * Returns the UserInfo for the given subject
	 * 
	 * @param sub
	 *            the subject of the UserInfo
	 * @return a valid UserInfo if it exists, null otherwise
	 */
	public TaxeUserInfo getById(Long id);

	/**
	 * Returns the UserInfo for the given subject
	 * 
	 * @param sub
	 *            the subject of the UserInfo
	 * @return a valid UserInfo if it exists, null otherwise
	 */
	public TaxeUserInfo getBySubject(String sub);

	/**
	 * Persists a UserInfo
	 *
	 * @param user
	 * @return
	 */
	public TaxeUserInfo save(UserInfo userInfo);

	/**
	 * Removes the given UserInfo from the repository
	 * 
	 * @param userInfo
	 *            the UserInfo object to remove
	 */
	public void remove(UserInfo userInfo);

	/**
	 * Get a UserInfo object by its preferred_username field
	 * @param username
	 * @return
	 */
	public TaxeUserInfo getByUsername(String username);


	/**
	 * Return a collection of all UserInfos managed by this repository
	 * 
	 * @return the UserInfo collection, or null
	 */
	public Collection<TaxeUserInfo> getAll();
        
}
