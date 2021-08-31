package kr.ac.uos.ai.arbi.monitor.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import kr.ac.uos.ai.arbi.model.GeneralizedList;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "rsif-monitor-model-type")
@XmlRootElement(name = "rsif-monitor-model")
public class MonitorModelContent {

	private static final GeneralizedList[] GL_NULL_ARRAY = new GeneralizedList[0];

	@XmlTransient
	private List<URLNameBean> aaURLNameList;

	@XmlTransient
	private List<URLNameBean> ltmURLNameList;

	@XmlTransient
	private List<GeneralizedList> aaFilterList;

	@XmlTransient
	private List<GeneralizedList> ltmFilterList;

	public static MonitorModelContent newInstance(String xmlString) {
		return ((MonitorModelContent) JAXB.unmarshal(new StringReader(xmlString), MonitorModelContent.class));
	}

	public static MonitorModelContent newInstance(File xmlFile) {
		try {
			return ((MonitorModelContent) JAXB.unmarshal(new FileReader(xmlFile), MonitorModelContent.class));
		} catch (FileNotFoundException e) {
		}
		return new MonitorModelContent();
	}
	
	private MonitorModelContent() {
		this.aaURLNameList = new CopyOnWriteArrayList();
		this.ltmURLNameList = new CopyOnWriteArrayList();
		this.aaFilterList = new CopyOnWriteArrayList();
		this.ltmFilterList = new CopyOnWriteArrayList();
	}
	
	public void setAAURLNameList(List<URLNameBean> aaURLNameList) {
		if(aaURLNameList == null)
			return;
		this.aaURLNameList.addAll(aaURLNameList);
	}
	
	public void setLTMURLNameList(List<URLNameBean> dsURLNameList) {
		if(dsURLNameList == null)
			return;
		this.ltmURLNameList.addAll(dsURLNameList);
	}
	
	public void setAAFilterList(List<GeneralizedList> filterList) {
		if(filterList == null)
			return;
		this.aaFilterList.addAll(filterList);
	}
	
	public void setLTMFilterList(List<GeneralizedList> filterList) {
		if(filterList == null)
			return;
		this.ltmFilterList.addAll(filterList);
	}
	
	@XmlElementWrapper(name = "aa-url-name-mapper-list")
	@XmlElement(name = "url-name-mapper")
	private List<URLNameBean> getAAURLNameList() {
		return this.aaURLNameList;
	}
	
	@XmlElementWrapper(name = "ltm-url-name-mapper-list")
	@XmlElement(name = "url-name-mapper")
	private List<URLNameBean> getLTMURLNameList() {
		return this.ltmURLNameList;
	}
	
	@XmlElementWrapper(name = "aa-message-filter-list")
	@XmlElement(name = "filter-gl")
	@XmlJavaTypeAdapter(GLAdapter.class)
	private List<GeneralizedList> getAAFilterList() {
		return this.aaFilterList;
	}
	
	@XmlElementWrapper(name = "ltm-message-filter-list")
	@XmlElement(name = "filter-gl")
	@XmlJavaTypeAdapter(GLAdapter.class)
	private List<GeneralizedList> getLTMFilterList() {
		return this.ltmFilterList;
	}
	
	public URLNameBean[] getAAURLNameMappings() {
		return ((URLNameBean[])getAAURLNameList().toArray(URLNameBean.NULL_ARRAY));
	}
	
	public URLNameBean[] getLTMURLNameMappings() {
		return ((URLNameBean[]) getLTMURLNameList().toArray(URLNameBean.NULL_ARRAY));
	}

	public GeneralizedList[] getAAFilters() {
		return ((GeneralizedList[]) getAAFilterList().toArray(GL_NULL_ARRAY));
	}

	public GeneralizedList[] getLTMFilters() {
		return ((GeneralizedList[]) getLTMFilterList().toArray(GL_NULL_ARRAY));
	}

	public String toXML() {
		StringWriter writer = new StringWriter();
		JAXB.marshal(this, writer);
		return writer.toString();
	}
	
}
