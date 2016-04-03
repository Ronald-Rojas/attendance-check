import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioRestResponse;
import com.twilio.sdk.resource.factory.CallFactory;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.AvailablePhoneNumber;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.instance.Conference;
import com.twilio.sdk.resource.instance.Participant;
import com.twilio.sdk.resource.list.AccountList;
import com.twilio.sdk.resource.list.AvailablePhoneNumberList;
import com.twilio.sdk.resource.list.ParticipantList;
import com.twilio.sdk.verbs.TwiMLException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import java.util.*; 
import com.twilio.sdk.*; 
import com.twilio.sdk.resource.factory.*; 
import com.twilio.sdk.resource.instance.*; 
import com.twilio.sdk.resource.list.*;

public class Updateattendence {
	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase db = mongoClient.getDatabase("classes");
	 public static final String ACCOUNT_SID = "AC40af3a9b2dbbeac45780a1ac63a77d5e"; 
	 public static final String AUTH_TOKEN = "4a2979612b6735faf5c371411aff4088"; 

	public static void main(String[] args ) throws ParseException, IOException{
		while(true){
			try{
				BufferedReader br = new BufferedReader( new FileReader("../input/input.txt"));			
				String input[] = br.readLine().split(",");
				while(input.length > 0 ){
					updateperson(input);

					input = br.readLine().split(",");
				}
			} catch (IOException e ){
				// no new students have arrived
			}
			Set<String> colllist = (Set<String>) db.listCollectionNames();
			for(String coll : colllist){
				long classStart = db.getCollection(coll).count(new Document ("start", "true"));
				if(classStart > 0){
					testStart(coll);
				}
			}
			
			
			
		}
	}
	private static void testStart(String coll) {
		Date currenttime = new Date();
		boolean start = false;
		FindIterable<Document> iterable = db.getCollection("coll").find();
		iterable.forEach(new Block<Document>(){
			public void apply(final Document document){
				String starttimestr = document.getString("starttime");
				Date starttime =null;
				try {
					starttime = format.parse(starttimestr);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(currenttime.after(starttime)){
					document.append("start", "true");
				}
				if(document.containsKey("start") && !document.containsKey("entertime")){
					String phonenumber = document.getString("phonenumber");
					try {
						textnumber( phonenumber , " you are late");
					} catch (TwilioRestException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		
		});

	}
	public static void updateperson( String input[]) {
		String classID = input[0];
		String studentID = input[2];
		String timestamp = input[3];
		MongoClient mongoClient = new MongoClient();
		db.getCollection(classID).updateOne(new Document("studentID", studentID), 
				new Document("$set", new Document("timestamp", timestamp)));
		FindIterable<Document> iterable = db.getCollection(classID).find(new Document("studentID", studentID));
		iterable.forEach(new Block<Document>(){
			public void apply (final Document document){
				try {
					textnumber(document.getString("phonenumber") , "We took your attendance");
				} catch (TwilioRestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	private static void textnumber(String phonenumber, String text) throws TwilioRestException {
			//twilio number  texting
		
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN); 
		 
		 // Build the parameters 
		 List<NameValuePair> params = new ArrayList<NameValuePair>(); 
		 params.add(new BasicNameValuePair("To", phonenumber)); 
		 params.add(new BasicNameValuePair("From", "+12015080954")); 
		 params.add(new BasicNameValuePair("Body", text));   
	 
		 MessageFactory messageFactory = client.getAccount().getMessageFactory(); 
		 Message message = messageFactory.create(params); 
		 System.out.println(message.getSid()); 
	}
}
