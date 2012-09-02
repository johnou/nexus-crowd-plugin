/**
 * Copyright (c) 2010 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.nexus.plugins.crowd.client;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.integration.exception.ObjectNotFoundException;
import com.atlassian.crowd.integration.soap.SOAPEntity;
import com.atlassian.crowd.service.GroupManager;
import com.atlassian.crowd.service.GroupMembershipManager;
import com.atlassian.crowd.service.soap.client.SecurityServerClient;

public class DefaultNexusRoleManager implements NexusRoleManager {

    private final Logger logger = LoggerFactory.getLogger(DefaultNexusRoleManager.class);

    private GroupManager groupManager;
    private GroupMembershipManager groupMembershipManager;
    private SecurityServerClient securityServerClient;

    public DefaultNexusRoleManager(GroupManager groupManager,
            GroupMembershipManager groupMembershipManager, SecurityServerClient securityServerClient) {
        this.groupManager = groupManager;
        this.groupMembershipManager = groupMembershipManager;
        this.securityServerClient = securityServerClient;
    }

    public List<String> getAllNexusRoles() throws RemoteException, InvalidAuthenticationException, com.atlassian.crowd.exception.InvalidAuthorizationTokenException {
        return groupManager.getAllGroupNames();
    }

    public List<String> getNexusRoles(String username) throws RemoteException,
            InvalidAuthorizationTokenException, ObjectNotFoundException, UserNotFoundException, InvalidAuthenticationException, com.atlassian.crowd.exception.InvalidAuthorizationTokenException {
        if (logger.isDebugEnabled()) {
            logger.debug("Looking up role list for username: " + username);
        }

        List<String> roles = groupMembershipManager.getMemberships(username);
        if (logger.isDebugEnabled()) {
            logger.debug("Obtained role list: " + roles.toString());
        }

        return roles;
    }

    /**
     * {@inheritDoc}
     * @throws com.atlassian.crowd.exception.InvalidAuthorizationTokenException 
     * @throws InvalidAuthenticationException 
     * @throws GroupNotFoundException 
     */
    public SOAPEntity getNexusRole(String roleName) throws RemoteException, InvalidAuthorizationTokenException, ObjectNotFoundException, GroupNotFoundException, InvalidAuthenticationException, com.atlassian.crowd.exception.InvalidAuthorizationTokenException {
        return groupManager.getGroup(roleName);
    }

}
