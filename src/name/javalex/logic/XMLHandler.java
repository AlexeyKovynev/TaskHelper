package name.javalex.logic;


import name.javalex.entities.SimplifiedProcess;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.File;
import java.util.List;

public class XMLHandler {

    public Document generatedDocument;
    public Document importedDocument;

    public void create(List<SimplifiedProcess> spList) {

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();

            // Create instance of DOM and root element
            generatedDocument = documentBuilder.newDocument();
            Element rootElement = generatedDocument.createElement("tasks");
            generatedDocument.appendChild(rootElement);

            for (SimplifiedProcess sp : spList) {

                // Create element "task" and place it under root
                Element task = generatedDocument.createElement("task");
                rootElement.appendChild(task);

                // Create element "name" and place it under task
                Element name = generatedDocument.createElement("name");
                name.appendChild(generatedDocument.createTextNode(sp.getName()));
                task.appendChild(name);

                // Create element "memory" and place it under task
                Element memory = generatedDocument.createElement("memory");
                memory.appendChild(generatedDocument.createTextNode(String.valueOf(sp.getUsedMemory()) + " KB"));
                task.appendChild(memory);
            }

        } catch (ParserConfigurationException pce) {
            System.out.println("XML: Error occurred when trying to instantiate DocumentBuilder \n" + pce);
        }
    }

    public void write(String path) {

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "6");

            DOMSource source = new DOMSource(generatedDocument);
            StreamResult result = new StreamResult(new File(path));
            //StreamResult result =  new StreamResult(System.out);
            transformer.transform(source, result);
            System.out.println("File saved!");

        } catch (TransformerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void read() {

        try {

            File fXmlFile = new File("D:\\file.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            importedDocument = documentBuilder.parse(fXmlFile);

            importedDocument.getDocumentElement().normalize();

            System.out.println("Root element :" + importedDocument.getDocumentElement().getNodeName());

            NodeList nodeList = importedDocument.getElementsByTagName("task");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {

                Node nNode = nodeList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println("Memory : " + eElement.getElementsByTagName("memory").item(0).getTextContent());

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
