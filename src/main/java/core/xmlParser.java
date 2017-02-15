package core;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class xmlParser {

    public static DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    public static DocumentBuilder docBuilder; static {
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void createXML(String path) throws TransformerException {

        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("settings");
        doc.appendChild(rootElement);

        Element prefix = doc.createElement("prefix");
        prefix.appendChild(doc.createTextNode("prefix"));
        rootElement.appendChild(prefix);

        prefix.setTextContent("~");


        TransformerFactory transFac = TransformerFactory.newInstance();
        Transformer trans = transFac.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        trans.transform(source, result);

    }

    public static void editValue(String path, String value) throws TransformerException, IOException, SAXException {

        Document doc = docBuilder.parse(new File(path));

        doc.getElementsByTagName("settings").item(0).setTextContent(value);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(path));
        transformer.transform(source, result);
    }

    public static HashMap<String, String> readXML(String path) throws IOException, SAXException {

        File xmlFile = new File(path);
        Document doc = docBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("prefix");

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < nList.getLength(); i++) {

            Node node = nList.item(i);
            result.put(node.getNodeName(), node.getTextContent());

        }


        return result;
    }
}
