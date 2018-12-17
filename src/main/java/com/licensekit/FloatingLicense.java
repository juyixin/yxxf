package com.licensekit;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.StringTokenizer;

import com.eazytec.common.util.CommonUtil;

public class FloatingLicense extends Thread
{
   /*
   private static boolean DO_TEST = false;
   private static String[] testHost = {
      "mercury", "venus", "terra", "mars", "saturn", "jupiter",
      "uranus", "neptune", "pluto", "zeus", "hera", "poseidon",
      "kairos", "apollo", "aphrodite", "dionysos", "demeter", "hermes",
      "minos", "pasiphae", "daedalos", "ikaros", "helena", "paris"
   };
   private static int nextTestHost = 0;
   private static int testHosts = testHost.length;
   */

   private static boolean DO_BROADCAST = true;
   private static boolean DO_MULTICAST = true;
   
   // The IP address to send our BROADCAST queries to.
   private static final String BROADCAST_ADDRESS = "255.255.255.255";
   
   // The IP address to send our MULTICAST queries to.
   // This must be a class D IP address (224.* to 239.*).
   private static final String MULTICAST_ADDRESS = "224.0.0.1";
   
   // The hop count to use for multicasting. The default
   // minimum hop count of 1 limits multicasts to the same
   // subnet, i.e. to all machines directly hooked to your
   // local network. The maximum recommended hop count is
   // 32, which limits multicasts to the same site.
   private static final int MULTICAST_HOPS = 1;
   
   // We'll send our queries this number of times to each
   // of our three broadcast/multicast port numbers.
   private static final int QUERY_TRIES = 3;

   // This is the delay between each two queries, given
   // in milliseconds. (500L is one half of a second.)
   private static final long QUERY_INTERVAL = 333L;

   // Each reply from a new host will extend our patience
   // by this amount of milliseconds.
   private static final long WAIT_FURTHER = 3000L;

   // Query strings look like this: "MyApp 12345 adrianna 2632".
   // "MyApp" is our application name, "12345" is some random
   // number identifying our series of queries. "adrianna" is our
   // hostname (i.e. the sender's hostname), and "2632" is the
   // port number where replies shall be sent to.

   // When replying to adrianna (or whatever hostname) at port 2632
   // (or whatever port number), we send back "MyApp 12362 honey 47813".
   // "MyApp" is our application name, and is hopefully the same
   // as the sender's application name. "12362" is the received random
   // number, incremented by 17. (This is a confirmation that both sides
   // know what these messages are all about.) "honey" is the hostname
   // of the replying application. "47813" is the thread port number
   // we had received the sender's message at.

   // If we have to do bind()-polling, we'll do it every this amount
   // of milliseconds.
   private static final long POLL_INTERVAL = 5000L;
   
   // The name of our application. Our three broadcast port numbers
   // are generated from this name.
   private static String myApp = "";
   
   // The hostname of the machine we are currently run on.
   private static String myHost = "";
   
   // This thread's port number...
   int threadPort = -1;
   // ...to listen to at this thread's multicast socket.
   MulticastSocket threadMcastSock = null;
   // (threadMcastSock is 'null' if this thread shall create
   // and bind a DatagramSocket at the given port number.
   
   // The thread constructor. Will be called three times with
   // a pre-created MulticastSocket to listen to, and three
   // times with a value of 'null' for 'aSock' to tell the
   // thread to create and bind a DatagramSocket to listen
   // to at 'aPort'.
   FloatingLicense (int aPort, MulticastSocket aSock)
   {
      threadPort = aPort;
      threadMcastSock = aSock;
   }
   
   public void run ()
   {
      DatagramSocket thread_bcast_sock = null;
      
      if (threadMcastSock == null) {
         
         // bind thread_bcast_sock to threadPort
         
         do {
            try {
               thread_bcast_sock = new DatagramSocket(threadPort);
            } catch (Exception e) {
               thread_bcast_sock = null;
               try {
                  //System.out.println("bind failed for port " + threadPort);
                  sleep(POLL_INTERVAL);
               } catch (Exception e2) {
               }
            }
         } while (thread_bcast_sock == null);
         //System.out.println("bind done for port " + threadPort);
      }
      
      while (true) {
         try {
            byte[] buf = new byte[254];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            
            // wait until a new request arrives
            
            if (threadMcastSock == null)
               thread_bcast_sock.receive(packet);
            else
               threadMcastSock.receive(packet);
            
            // got one, verify it...
            
            String query_string = new String(packet.getData(), 0, packet.getLength());
            //System.out.println("received message:\"" + query_string + "\"");
            StringTokenizer st = new StringTokenizer(query_string);
            
            String sender_appname = st.nextToken();
            int sender_id = Integer.parseInt(st.nextToken());
            String sender_hostname = st.nextToken();
            int sender_port = Integer.parseInt(st.nextToken());

            if ((sender_id >= 0) && (sender_port >= 0)) {
               
               // ...and reply to it
               
               /*if (DO_TEST) {
                  for (int n = 0; n < 3; n++) {
                     String reply_string = myApp + " " + (sender_id + 17) + " " + testHost[nextTestHost] + " " + threadPort;
                     if (++nextTestHost >= testHosts)
                        nextTestHost = 0;                   
                     //System.out.println("replying with:\"" + reply_string + "\"");
                     byte[] reply_msg = reply_string.getBytes();
                     DatagramPacket reply_packet = new DatagramPacket(reply_msg, reply_msg.length,
                        InetAddress.getByName(sender_hostname), sender_port);
               
                     if (threadMcastSock == null)
                        thread_bcast_sock.send(reply_packet);
                     else
                        threadMcastSock.send(reply_packet);
                  }
                  
               } else {*/
                  
               String reply_string = myApp + " " + (sender_id + 17) + " " + myHost + " " + threadPort;
               //System.out.println("replying with:\"" + reply_string + "\"");
               byte[] reply_msg = reply_string.getBytes();
               DatagramPacket reply_packet = new DatagramPacket(reply_msg, reply_msg.length,
                  InetAddress.getByName(sender_hostname), sender_port);
         
               if (threadMcastSock == null)
                  thread_bcast_sock.send(reply_packet);
               else
                  threadMcastSock.send(reply_packet);
               
               /*}*/
            }

         } catch (Exception e) {
         }
      }
   }

   public static int getCurrentUsers (String applicationName, int maxUsers, FloatingLicenseAddHostDelegate anAddHostDelegate)
      throws IllegalArgumentException, java.io.IOException
   {
      int i, len;

      // evaluate args
         
      if (!DO_BROADCAST && !DO_MULTICAST)
         throw new IllegalArgumentException("!DO_BROADCAST && !DO_MULTICAST");
         
      if ((applicationName == null) || ((len = applicationName.length()) < 1))
         throw new IllegalArgumentException("applicationName");
         
      if (maxUsers < 1)
         throw new IllegalArgumentException("maxUsers");
         
      // hash applicationName, set thread_port[] numbers
         
      for (i = 0, myApp = ""; i < len; i++) {
         char c = applicationName.charAt(i);
         myApp += (Character.isWhitespace(c)) ? '_' : c;
      }
   
      int thread_port[] = HashString.hashString(myApp);
      //System.out.println("'" + myApp + "' yields: " + thread_port[0] + "(+1) " + thread_port[1] + "(+1) " + thread_port[2] + "(+1)");
      
      // obtain our own hostname
         
      InetAddress addr = InetAddress.getLocalHost();
      //InetAddress addr = InetAddress.getByName(null);
      myHost = addr.getHostName();
      
      
    //  System.out.println("User Count List === "+userCount);
      int users=0;
      System.out.println("The Logged In User Counting "+CommonUtil.getLoggedUserDetail().size());
      users = CommonUtil.getLoggedUserDetail().size(); 
      // create the sender broadcast and multicast sockets
      //commented below the lines by prakash.
      
    /*  DatagramSocket bcast_snd_sock = null;
      if (DO_BROADCAST)
         bcast_snd_sock = new DatagramSocket();
      // The SO_BROADCAST option should be set for this sock, but Java provides
      // no facility to accomplish this. Whether or not an OS allows broadcasts
      // without the SO_BROADCAST option set is implementation-dependent. Since
      // JDK1.2, the API Specification says: "UDP broadcasts sends and receives
      // are always enabled on a DatagramSocket." ... so we'll keep our fingers
      // crossed. If UDP broadcasts fail, we have to rely on our UDP multicasts
      // to succeed. FYI here's the missing code, given in C:
      // int on = 1;
      // setsockopt(snd_sock, SOL_SOCKET, SO_BROADCAST, &on, sizeof(on));

      MulticastSocket mcast_snd_sock = null;

      if (DO_MULTICAST) {
         mcast_snd_sock = new MulticastSocket();
         
         if (MULTICAST_HOPS > 1) {
            try {
               Method setTimeToLive = MulticastSocket.class.getMethod("setTimeToLive", new Class[] { int.class } );
               setTimeToLive.invoke(mcast_snd_sock, new Object[] { new Integer(MULTICAST_HOPS) } );
            }
            catch (Exception e) {
               try {
                  Method setTTL = MulticastSocket.class.getMethod("setTTL", new Class[] { byte.class } );
                  setTTL.invoke(mcast_snd_sock, new Object[] { new Byte((byte)((MULTICAST_HOPS <= 127) ? MULTICAST_HOPS : 127)) } );
               }
               catch (Exception e2) {
               }
            }
         }
      }
      
      // create the receiver socket
      
      DatagramSocket rcv_sock = new DatagramSocket();
      rcv_sock.setSoTimeout(1);  // make it non-blocking

      int rcv_port = rcv_sock.getLocalPort();
      
      // prepare our query
      
      long msec_now = System.currentTimeMillis();
      long sec_now = msec_now / 1000;
      long query_id = (sec_now % 16000) + (msec_now % 16000);
      
      String query_string = myApp + " " + query_id + " " + myHost + " " + rcv_port;
      byte[] query_msg = query_string.getBytes();
      //System.out.println("message to send:\"" + query_string + "\"");
      
      long msec_next_try = msec_now;
      long msec_further = 0;
      
      int next_try = 0;          // 0 .. (QUERY_TRIES - 1)
      int next_port_index = 0;   // 0 .. 2
                  
      Vector hostnames = new Vector(maxUsers);
      int users = 0;             // number of hosts other than our own one
      
      DatagramPacket broadcast_packet[] = { null, null, null };
      DatagramPacket multicast_packet[] = { null, null, null };
         
      // now loop
         
      while (true) {
         msec_now = System.currentTimeMillis();
         
         // check whether to broadcast another query
            
         if ((next_try < QUERY_TRIES) && (msec_now >= msec_next_try)) {
               
            // yes, send (i.e. broadcast and multicast) our next query
            
            if (DO_BROADCAST) {
               if (broadcast_packet[next_port_index] == null)
                  broadcast_packet[next_port_index] = new DatagramPacket(query_msg, query_msg.length,
                     InetAddress.getByName(BROADCAST_ADDRESS), thread_port[next_port_index]);
            }
            
            if (DO_MULTICAST) {
               if (multicast_packet[next_port_index] == null)
                  multicast_packet[next_port_index] = new DatagramPacket(query_msg, query_msg.length,
                     InetAddress.getByName(MULTICAST_ADDRESS), thread_port[next_port_index] + 1);
            }
            
            if (DO_BROADCAST && DO_MULTICAST) {
               try {
                  bcast_snd_sock.send(broadcast_packet[next_port_index]);
               } catch (Exception e) {
                  System.err.println("can't send broadcast_packet, relying on multicast_packet");
               }
               mcast_snd_sock.send(multicast_packet[next_port_index]);
            
            } else if (DO_BROADCAST)
               bcast_snd_sock.send(broadcast_packet[next_port_index]);
            
            else
               mcast_snd_sock.send(multicast_packet[next_port_index]);

            // increment next_* values
            
            if (++next_port_index > 2) {
               next_port_index = 0;
               next_try++;
            }
            msec_now = System.currentTimeMillis();
            if (next_try < QUERY_TRIES) {
               msec_next_try = msec_now;
               msec_next_try += QUERY_INTERVAL;
            } else
               msec_further = msec_now + WAIT_FURTHER;
         }
      
         // check whether to wait for any further replies
      
         if ((next_try >= QUERY_TRIES) && (msec_now >= msec_further))
            break;
         
         // check whether some reply has arrived...
         
         try {
            byte[] reply_buf = new byte[254];
            DatagramPacket reply_packet = new DatagramPacket(reply_buf, reply_buf.length);
            
            rcv_sock.receive(reply_packet);
            
            // ...yes, we got another reply! verify it...

            String reply = new String(reply_packet.getData(), 0, reply_packet.getLength());
            StringTokenizer st = new StringTokenizer(reply);
            
            String ret_appname = st.nextToken();
            int ret_id = Integer.parseInt(st.nextToken());
            String ret_hostname = st.nextToken();
            int ret_port = Integer.parseInt(st.nextToken());
            
            if (ret_id == (query_id + 17)) {
               
               // ...and if it's a valid reply, evaluate it
               
               if (!ret_appname.equals(myApp)) {
                  // OOPS: a foreign app also uses one of our broadcast ports!
                  // for now, we just tell the sysop to take care of this
                  System.err.println(myApp + ": warning: '" + ret_appname + "' on host " + ret_hostname + " also uses port " + ret_port);
               
               } else if (!ret_hostname.equals(myHost)) {
                  
                  // check whether ret_hostname is already in our list...

                  int count = hostnames.size();
                  for (i = 0; i < count; i++)
                     if (((String)hostnames.elementAt(i)).equals(ret_hostname))
                        break;
                  
                  if (i == count) { // ...no, add it to list

                     hostnames.addElement(ret_hostname);
                     
                     try {
                        if (anAddHostDelegate != null)
                           anAddHostDelegate.addHost(ret_hostname);
                     } catch (Exception e) {
                     }
                     
                     if (++users >= maxUsers) { // too many users
                    	 System.out.println("too many users....");
                        if (DO_BROADCAST)
                           bcast_snd_sock.close();
                        if (DO_MULTICAST)
                           mcast_snd_sock.close();
                        rcv_sock.close();
                        return users;
                     }
                     
                     // keep waiting for at least now + WAIT_FURTHER msecs
      
                     if (next_try >= QUERY_TRIES) {
                        msec_now = System.currentTimeMillis();
                        msec_further = msec_now + WAIT_FURTHER;
                     }
                  
                  }  // if (i == count)
                  
               }  // if (!ret_hostname.equals(myHost))
               
            }  // if (ret_id == (query_id + 17))
               
         } catch (Exception e) {
            // timeout (i.e. no reply), or malformed/invalid reply
         }
         
      }  // while (true)
            
      // query phase done, 'maxUsers' threshold has not
      // been reached... all lights are green... time to
      // establish our 3+3 listener threads!
      
      if (DO_BROADCAST)
         bcast_snd_sock.close();
      if (DO_MULTICAST)
         mcast_snd_sock.close();
      rcv_sock.close();
      
      // create the 3 MULTICAST sockets and fork their reply threads
      // (the port numbers are one higher than the broadcast ones)

      if (DO_MULTICAST) {
         InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
         for (i = 0; i < 3; i++) {
            MulticastSocket mcast_sock = new MulticastSocket(thread_port[i] + 1);
            mcast_sock.joinGroup(group);
            FloatingLicense thread = new FloatingLicense(thread_port[i] + 1, mcast_sock);
            thread.setDaemon(true);
            int p = thread.getPriority();
            if (p < Thread.MAX_PRIORITY)
               thread.setPriority(p + 1);
            thread.start();
         }
      }
      
      // fork the 3 BROADCAST reply threads (these will create and bind
      // their sockets on their own)
      
      if (DO_BROADCAST) {
         for (i = 0; i < 3; i++) {
            FloatingLicense thread = new FloatingLicense(thread_port[i], null);
            thread.setDaemon(true);
            int p = thread.getPriority();
            if (p < Thread.MAX_PRIORITY)
               thread.setPriority(p + 1);
            thread.start();
         }
      }
      */
      // done
   
      return users;
   }

}
