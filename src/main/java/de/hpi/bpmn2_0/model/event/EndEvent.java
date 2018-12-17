//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.09.07 at 02:19:19 PM CEST 
//

package de.hpi.bpmn2_0.model.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.oryxeditor.server.diagram.Shape;
import org.oryxeditor.server.diagram.StencilType;

/**
 * <p>
 * Java class for tEndEvent complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name=&quot;tEndEvent&quot;&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base=&quot;{http://www.omg.org/bpmn20}tThrowEvent&quot;&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlRootElement(name = "endEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEndEvent")
public class EndEvent extends ThrowEvent {
	/**
	 * Transforming an end event to its JSON-based shape representation.
	 */
	
	@XmlAttribute
    protected String reader;
	
	@XmlAttribute
    protected String startscript;
	
	@XmlAttribute
    protected String isarchive;
	
	@XmlAttribute
    protected String readeroption;
	
	
	public void toShape(Shape shape) {
		super.toShape(shape);

		shape.setStencil(new StencilType("EndNoneEvent"));
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getStartscript() {
		return startscript;
	}

	public void setStartscript(String startscript) {
		this.startscript = startscript;
	}

	public String getIsarchive() {
		return isarchive;
	}

	public void setIsarchive(String isarchive) {
		this.isarchive = isarchive;
	}

	public String getReaderoption() {
		return readeroption;
	}

	public void setReaderoption(String readeroption) {
		this.readeroption = readeroption;
	}
}
