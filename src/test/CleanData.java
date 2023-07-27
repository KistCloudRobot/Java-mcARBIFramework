package test;

import java.io.File;
import java.io.IOException;

import org.apache.activemq.store.kahadb.KahaDBStore;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

public class CleanData {
	
	private RedisClient redisClient;
	private RedisCommands<String, String> command;
	private KahaDBStore kaha;
	
	public CleanData() {

		redisClient = RedisClient.create("redis://localhost:6379/0");
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		command = connection.sync();
		
		kaha = new KahaDBStore();
		kaha.setDirectory(new File("activemq-data/localhost/KahaDB"));
		
		
	}
	
	public void clean() {
		
		command.flushall();
		try {
			kaha.deleteAllMessages();
			
			File file_log = new File(kaha.getDirectory()+"/db-1.log");
			if(file_log.exists())
				file_log.delete();
			
			File file_data = new File(kaha.getDirectory() +"/db.data");
			if(file_data.exists())
				file_data.delete();
			
			File file_redo = new File(kaha.getDirectory() +"/db.redo");
			if(file_redo.exists())
				file_redo.delete();
			
			File file_lock = new File(kaha.getDirectory()+"/lock");
			if(file_lock.exists())
				file_lock.delete();
			
			System.out.println("Clean Data Success!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		new CleanData().clean();
		
	}
}
