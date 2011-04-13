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
package dk.cubing.liveresults.action;

import org.apache.struts2.StrutsStatics;

import com.opensymphony.xwork2.ActionSupport;

public abstract class FrontendAction extends ActionSupport implements StrutsStatics {

	private static final long serialVersionUID = 1L;
	
	protected int size = 25;
	protected int page = 1;
	
	/**
	 * @return
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * @return
	 */
	public int getPage() {
		return page;
	}
	
	/**
	 * @param page
	 */
	public void setPage(int page) {
		this.page = page;
	}
	
	/**
	 * @return
	 */
	abstract public String list();

}
