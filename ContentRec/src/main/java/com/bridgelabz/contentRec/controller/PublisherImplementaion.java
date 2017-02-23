/*
* @Author:Dhareppa Metri
* File:GamesCategoryScoreController.java
* Purpose:Publisher implementation class.
**/
package com.bridgelabz.contentRec.controller;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class PublisherImplementaion {
	Logger LOGGER = Logger.getLogger("PublisherImplementaion");
	@Autowired
	StringRedisTemplate template;
	@Autowired
	RedisConnectionFactory connectionFactory;
	@Autowired
	MessageListenerAdapter listenerAdapter;
	@Autowired
	CountDownLatch latch;

	@Autowired
	VisitorInformation mVisitorInfoPubSub;

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(SubcriberImplementation receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	@Bean
	SubcriberImplementation receiver(CountDownLatch latch) {
		return new SubcriberImplementation(latch);
	}

	@Bean
	CountDownLatch latch() {
		return new CountDownLatch(1);
	}

	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
	
	public void sendMessage() throws InterruptedException {
		System.out.println("Publisher Method Running....");
		System.out.println("Storing CSV info to Db......");
		LOGGER.info("Sending message...");
		mVisitorInfoPubSub.getDataFromCSV();
		System.out.println("CSV file updated to Db");
		template.convertAndSend("chat", "CSV file updated to Db");
		latch.await();
		// System.exit(0);

	}//End of sendMessage Method
}//End of PubImpl class
