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
package dk.cubing.liveresults.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

public class Consumer {

	private ConnectionFactory factory;
	private String queueName;
	
    private Connection connection;
    private Session session;
    private MessageConsumer consumer;
    
    /**
     * @param factory
     * @param queueName
     */
    public Consumer(ConnectionFactory factory, String queueName) {
    	this.factory = factory;
    	this.queueName = queueName;

    	try {
	    	connection = this.factory.createConnection();
	        connection.start();
	        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        Destination destination = session.createQueue(this.queueName);
	        consumer = session.createConsumer(destination);
    	} catch (JMSException e) {
    		shutdown();
    	}
    }
    
	/**
	 * @param messageListener
	 */
	public void setMessageListener(MessageListener messageListener) {
		try {
			consumer.setMessageListener(messageListener);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close connection
	 */
	public void shutdown() {
    	if (connection != null) {
    		try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
    	}
    }
}
