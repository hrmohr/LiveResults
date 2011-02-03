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
package dk.cubing.liveresults.uploader.engine;

import java.io.File;

import org.apache.commons.jci.listeners.FileChangeListener;

import dk.cubing.liveresults.uploader.LoggerWrapper;
import dk.cubing.liveresults.uploader.LoggerWrapperFactory;

public class ResultsFileChangeListener extends FileChangeListener {

	private static final LoggerWrapper log = LoggerWrapperFactory.getInstance().getLogger(ResultsFileChangeListener.class);
	
	private ResultsEngine engine;
	
	public ResultsFileChangeListener(ResultsEngine engine) {
		super();
		this.engine = engine;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jci.listeners.FileChangeListener#onFileChange(java.io.File)
	 */
	@Override
	public void onFileChange(File pFile) {
		super.onFileChange(pFile);
		log.info("Results file has been changed.");
		engine.uploadResults(pFile);
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.jci.listeners.FileChangeListener#onFileDelete(java.io.File)
	 */
	@Override
	public void onFileDelete(File pFile) {
		super.onFileDelete(pFile);
		log.error("Results file: '{}' was deleted!", pFile.getAbsolutePath());
	}

}
