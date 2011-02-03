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
package dk.cubing.liveresults.resultsbot;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dk.cubing.liveresults.resultsbot.engine.ResultsBot;

public class App {
	
    public static void main( String[] args ) {
    	
    	final Logger log = LoggerFactory.getLogger(App.class);
    	
    	// get bean
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"botContext.xml"}); 
		final ResultsBot bot = (ResultsBot) context.getBean("resultsBot");
		
		// capture shutdown and handle it more cleanly
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
			public void run() {
		        bot.shutdown();
		    }
		});

		// start the bot
		new Thread(bot, "ResultsBot").start();
    	
    	// ease testing in eclipse
		if (new File(".settings").isDirectory()) {
			try {
				System.in.read();
			} catch (IOException e) {
				log.error("Unable to read from stdin!");
			}
			System.exit(0);
		}
    }
}
