package com.dianping.cat.consumer;

import junit.framework.Assert;

import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.cat.analysis.MessageAnalyzer;
import com.dianping.cat.consumer.transaction.TransactionAnalyzer;
import com.dianping.cat.message.spi.core.MessageConsumer;

public class DefaultConsumerTest extends ComponentTestCase {

	@Test
	public void test() throws Exception {
		RealtimeConsumer consumer = (RealtimeConsumer) lookup(MessageConsumer.class);
		MessageAnalyzer analyzer = consumer.getCurrentAnalyzer(TransactionAnalyzer.ID);

		Assert.assertEquals(true, analyzer != null);
	}
}
