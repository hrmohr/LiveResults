/**
 * Copyright (C) 2009 Mads Mohr Christensen, <hr.mohr@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package dk.cubing.liveresults.webservice;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

public abstract class GenericWebService {
	
	private ApplicationContext context = null;
	
	/**
	 * @param username
	 * @param password
	 */
	protected void authenticateUser(String username, String password) throws AuthenticationException, BadCredentialsException {
		if (context == null) {
			context = new ClassPathXmlApplicationContext(new String[] {"security.xml", "database.xml"});
		}
		UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(username, password);
		AuthenticationManager authManager = (AuthenticationManager) context.getBean("_authenticationManager");
		SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(login));
	}

}
