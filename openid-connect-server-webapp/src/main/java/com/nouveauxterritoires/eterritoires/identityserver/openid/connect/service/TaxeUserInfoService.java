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
package com.nouveauxterritoires.eterritoires.identityserver.openid.connect.service;

import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.model.TaxeUserInfo;
import java.util.Collection;
import org.mitre.openid.connect.model.UserInfo;

/**
 * Interface for UserInfo service
 * 
 * @author Michael Joseph Walsh
 * 
 */
public interface TaxeUserInfoService{

	/**
	 * Save an TaxeUserInfo
	 * 
	 * @param taxeUserInfo
	 *            the TaxeUserInfo to be saved
	 */
	public TaxeUserInfo save(TaxeUserInfo taxeUserInfo);
        
	/**
	 * Save an UserInfo
	 * 
	 * @param userInfo
	 *            the UserInfo to be saved
	 */
	public void save(UserInfo userInfo);

	/**
	 * Get UserInfo for the Subject
	 * 
	 * @param id
	 *            subject for UserInfo
	 * @return UserInfo for id, or null
	 */
	public TaxeUserInfo getById(Long id);

	/**
	 * Get UserInfo for the Subject
	 * 
	 * @param sub
	 *            subject for UserInfo
	 * @return UserInfo for sub, or null
	 */
	public TaxeUserInfo getBySubject(String sub);

	/**
	 * Remove the UserInfo
	 * 
	 * @param userInfo
	 *            the UserInfo to remove
	 */
	public void remove(UserInfo userInfo);


	/**
	 * Get the UserInfo for the given username (usually maps to the
	 * preferredUsername field).
	 * @param username
	 * @return
	 */
	public TaxeUserInfo getByUsername(String username);

	/**
	 * Get the UserInfo for the given username (usually maps to the
	 * preferredUsername field) and clientId. This allows pairwise
	 * client identifiers where appropriate.
	 * @param username
	 * @param clientId
	 * @return
	 */
	public TaxeUserInfo getByUsernameAndClientId(String username, String clientId);
        
	/**
	 * Get all the TaxeUserInfo 
         * 
	 * @return Collection<TaxeUserInfo>
	 */
	public Collection<TaxeUserInfo> getAllUsers();
        
        

	public TaxeUserInfo generateTaxeUserInfoSub(TaxeUserInfo taxeUserInfo);
        
	/**
	 * add a new TaxeUserInfo 
         * 
	 * @return TaxeUserInfo
	 */
	public TaxeUserInfo saveNewTaxeUserInfo(TaxeUserInfo taxeUserInfo);
        
	/**
	 * update a TaxeUserInfo 
         * 
	 * @return TaxeUserInfo
	 */
	public TaxeUserInfo updateTaxeUserInfo(TaxeUserInfo oldTaxeUserInfo, TaxeUserInfo newTaxeUserInfo);
        
}
