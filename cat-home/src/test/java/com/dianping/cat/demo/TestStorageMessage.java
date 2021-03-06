package com.dianping.cat.demo;

import java.util.Random;

import org.junit.Test;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.message.spi.internal.DefaultMessageTree;

public class TestStorageMessage {

	private String JDBC_CONNECTION = "jdbc:mysql://%s:3306/%s?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";

	@Test
	public void testCross() throws Exception {
		String serverIp = "10.10.10.1";

		while (true) {
			for (int i = 0; i < 5; i++) {
				sendCacheMsg("cache-1", "user-" + i, "get", serverIp + i);
				sendCacheMsg("cache-1", "user-" + i, "remove", serverIp + i);
				sendCacheMsg("cache-1", "user-" + i, "add", serverIp + i);
				sendCacheMsg("cache-1", "user-" + i, "mGet", serverIp + i);

				sendSQLMsg("sql-1", "user-" + i, "select", serverIp + i);
				sendSQLMsg("sql-1", "user-" + i, "insert", serverIp + i);
				sendSQLMsg("sql-1", "user-" + i, "delete", serverIp + i);
				sendSQLMsg("sql-1", "user-" + i, "update", serverIp + i);
				
				sendCacheMsg("cache-2", "user-" + i, "get", serverIp + i);
				sendCacheMsg("cache-2", "user-" + i, "add", serverIp + i);
				sendCacheMsg("cache-2", "user-" + i, "remove", serverIp + i);
				sendCacheMsg("cache-2", "user-" + i, "mGet", serverIp + i);

				sendSQLMsg("sql-2", "user-" + i, "select", serverIp + i);
				sendSQLMsg("sql-2", "user-" + i, "update", serverIp + i);
				sendSQLMsg("sql-2", "user-" + i, "delete", serverIp + i);
				sendSQLMsg("sql-2", "user-" + i, "insert", serverIp + i);
			}
			Thread.sleep(50);
		}
	}

	private void sendCacheMsg(String name, String domain, String method, String serverIp) throws InterruptedException {
		Transaction t = Cat.newTransaction("Cache.memcached-" + name, "oUserAuthLevel:" + method);

		Cat.logEvent("Cache.memcached.server", serverIp);

		MessageTree tree = Cat.getManager().getThreadLocalMessageTree();
		((DefaultMessageTree) tree).setDomain(domain);
		Thread.sleep(500 + new Random().nextInt(1000));
		t.setStatus(Transaction.SUCCESS);
		t.complete();
	}

	private void sendSQLMsg(String name, String domain, String method, String serverIp) throws InterruptedException {
		Transaction t = Cat.newTransaction("SQL", "sql.method");

		Cat.logEvent("SQL.Method", method);
		Cat.logEvent("SQL.Database", String.format(JDBC_CONNECTION, serverIp, name));

		MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

		((DefaultMessageTree) tree).setDomain(domain);
		Thread.sleep(500 + new Random().nextInt(1000));
		t.setStatus(Transaction.SUCCESS);
		t.complete();
	}
}
