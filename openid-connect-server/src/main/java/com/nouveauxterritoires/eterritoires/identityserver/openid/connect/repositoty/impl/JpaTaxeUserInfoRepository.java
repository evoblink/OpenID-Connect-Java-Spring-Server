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
package com.nouveauxterritoires.eterritoires.identityserver.openid.connect.repositoty.impl;

import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.model.TaxeUserInfo;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.repositoty.TaxeUserInfoRepository;
import static org.mitre.util.jpa.JpaUtil.getSingleResult;
import static org.mitre.util.jpa.JpaUtil.saveOrUpdate;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.mitre.oauth2.model.ClientDetailsEntity;

import org.mitre.openid.connect.model.UserInfo;
import org.mitre.util.jpa.JpaUtil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA UserInfo repository implementation
 * 
 * @author Michael Joseph Walsh
 *
 */
@Repository
public class JpaTaxeUserInfoRepository implements TaxeUserInfoRepository {

	@PersistenceContext
	private EntityManager manager;

	@Override
	@Transactional
	public TaxeUserInfo getBySubject(String sub) {
		TypedQuery<TaxeUserInfo> query = manager.createNamedQuery("TaxeUserInfo.getBySubject", TaxeUserInfo.class);
		query.setParameter("sub", sub);

		return getSingleResult(query.getResultList());
	}

	@Override
	@Transactional
	public TaxeUserInfo getById(Long id) {
		TypedQuery<TaxeUserInfo> query = manager.createNamedQuery("TaxeUserInfo.getById", TaxeUserInfo.class);
		query.setParameter("id", id);

		return getSingleResult(query.getResultList());
	}

	@Override
	@Transactional
	public TaxeUserInfo save(UserInfo userInfo) {
		TaxeUserInfo dui = (TaxeUserInfo)userInfo;
		return saveOrUpdate(dui.getId(), manager, dui);
	}

	@Override
	@Transactional
	public void remove(UserInfo userInfo) {
		TaxeUserInfo dui = (TaxeUserInfo)userInfo;
		UserInfo found = manager.find(TaxeUserInfo.class, dui.getId());

		if (found != null) {
			manager.remove(userInfo);
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	@Transactional
	public Collection<TaxeUserInfo> getAll() {

		TypedQuery<TaxeUserInfo> query = manager.createNamedQuery("TaxeUserInfo.getAll", TaxeUserInfo.class);

		return query.getResultList();
	}

	/**
	 * Get a single UserInfo object by its username
	 */
	@Override
	public TaxeUserInfo getByUsername(String username) {
		TypedQuery<TaxeUserInfo> query = manager.createNamedQuery("TaxeUserInfo.getByUsername", TaxeUserInfo.class);
		query.setParameter("username", username);

		return getSingleResult(query.getResultList());

	}

}
