package edu.neu.cs5200.hmw6.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import edu.neu.cs5200.hmw6.models.Site;
import edu.neu.cs5200.hmw6.models.SiteList;

public class SiteDao {
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("hmw6");
	EntityManager em = null;
	// findSite
	public Site findSite(Integer siteId) {
		Site site = null;
		
		em = factory.createEntityManager();
		em.getTransaction().begin();
		
		site = em.find(Site.class, siteId);
		
		em.getTransaction().commit();
		em.close();
		
		return site;
	}

	// findAllSites
	public List<Site> findAllSites() {
		List<Site> sites = new ArrayList<Site>();
		
		em = factory.createEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("select site from Site site");// object-oriented															// psedo-sql
		sites = query.getResultList();
		
		em.getTransaction().commit();
		em.close();

		return sites;
	}
	
	public void exportSiteDatabaseToXmlFile(SiteList siteList, String xmlFileName){
		File xmlFile = new File(xmlFileName);
		try {
			JAXBContext jaxb = JAXBContext.newInstance(SiteList.class);
			Marshaller marshaller = jaxb.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(siteList, xmlFile);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void convertXmlFileToOutputFile(
			String inputXmlFileName,
			String outputXmlFileName, 
			String xsltFileName){
		
		File inputXmlFile = new File(inputXmlFileName);
		File outputXmlFile = new File(outputXmlFileName);
		File xsltFile = new File(xsltFileName);
		
		StreamSource source = new StreamSource(inputXmlFile);
		StreamSource xslt    = new StreamSource(xsltFile);
		StreamResult output = new StreamResult(outputXmlFile);
		
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer(xslt);
			transformer.transform(source, output);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		SiteDao dao = new SiteDao();
		
//		Site site = dao.findSite(1);
//		System.out.println(site.getName());
		
		List<Site> sites = dao.findAllSites();
//		for(Site site : sites){
//			System.out.println(site.getName());
//		}
		SiteList theSiteList = new SiteList();
		theSiteList.setSites(sites);
		
		//dao.exportSiteDatabaseToXmlFile(theSiteList, "xml/sites.xml");
		//dao.convertXmlFileToOutputFile("xml/sites.xml", "xml/sites.html", "xml/sites2html.xsl");
		dao.convertXmlFileToOutputFile("xml/sites.xml", "xml/equipments.html", "xml/sites2equipment.xsl");
	}
}
