package dtw.webmail.util.config;

import java.util.Properties;
import java.io.Serializable;

/** 
 * Class encapsulating the secure connection information
 * and setup.
 * <p>
 * @author Dieter Wimberger (wimpi)
 * @version (created Feb 27, 2003)
 */
public class Security
    implements Serializable {

  private String m_SSLFactory="javax.net.ssl.SSLSocketFactory";

  public Security() {
  }//constructor

  /**
   * Returns the <tt>SocketFactory</tt> class to be used
   * for creating secure communication sockets.
   *
   * @return the factory class as <tt>String</tt>.
   */
  public String getSecureSocketFactory() {
    return m_SSLFactory;
  }//getSecureSocketFactory

  /**
   * Sets the <tt>SocketFactory</tt> class to be used
   * for creating secure communication sockets.
   *
   * @param factory the factory class as <tt>String</tt>.
   */
  public void setSecureSocketFactory(String factory) {
    if(factory!=null && factory.length()>0) {
      m_SSLFactory = factory;
    }
  }//setSecureSocketFactory

  public void addSocketProperties(String protocol, int port) {


    Properties props =  System.getProperties();
    props.setProperty(
        "mail." + protocol + ".socketFactory.class", m_SSLFactory
    );
    props.setProperty(
         "mail." + protocol + ".socketFactory.fallback", "false"
    );
    props.setProperty(
         "mail." + protocol + ".socketFactory.port", "" + port
    );
    props.setProperty(
         "mail." + protocol + ".port", "" + port
    );

  }//prepareProperties

}//class Security
