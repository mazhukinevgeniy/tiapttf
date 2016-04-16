package homemade.menu.model.save;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by Marid on 28.03.2016.
 */
public class Save
{
    private String pathToFile;
    private Document xmlDocument;

    private Save() {}

    public Save(String path)
    {
        pathToFile = path;
        xmlDocument = getXMLDocument();
    }

    private Document getXMLDocument()
    {
        Document document = null;
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            document = builder.parse(new File(pathToFile));
        } catch (Exception exception) {
            System.err.print("XML parsing error!");
        }

        return document;
    }

    public boolean isValid()
    {
        return xmlDocument != null;
    }

    public String getParameterValue(String blockName, String parameterName)
    {
        Node block = findBlock(blockName);
        Node node = findNode(block, parameterName);
        String value = null;
        if(node != null)
        {
            value = getAttributeValue(node, Attribute.VALUE);
        }
        return value;
    }

    public void setParameterValue(String blockName, String parameterName, String newValue)
    {
        Node block = findBlock(blockName);
        Node node = findNode(block, parameterName);

        if (node != null)
        {
            Node attribute = getAttribute(node, Attribute.VALUE);
            attribute.setNodeValue(newValue);

            saveDocument();
        }
        addBlock("test");
    }

    private Node findBlock(String blockName)
    {
        Node mainNode = getMainNode();
        Node sought = null;

        NodeList children = mainNode.getChildNodes();
        int numberOfChild = children.getLength();
        for (int i = 0; i < numberOfChild; ++i)
        {
            Node currentBlock = children.item(i);
            String currentBlockName = currentBlock.getNodeName();
            if(currentBlockName.equals(blockName))
            {
                sought = currentBlock;
                break;
            }
        }
        return sought;
    }

    private Node getMainNode()
    {
        return xmlDocument.getChildNodes().item(0);
    }

    private Node findNode(Node block, String parameterName)
    {
        Node sought = null;

        NodeList children = block.getChildNodes();
        int numberOfChild = children.getLength();
        for (int i = 0; i < numberOfChild; ++i) {
            Node node = children.item(i);
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null)
            {
                Node nameAttrib = attributes.getNamedItem(Attribute.NAME);
                String name = nameAttrib.getNodeValue();
                if (name.equals(parameterName))
                {
                    sought = node;
                    break;
                }
            }
        }
        return sought;
    }

    private String getAttributeValue(Node node, String attributeName)
    {
        Node attribute = getAttribute(node, attributeName);
        String value = null;
        if (attribute != null)
        {
            value = attribute.getNodeValue();
        }

        return value;
    }

    private Node getAttribute(Node node, String attributeName)
    {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = null;
        if (attributes != null)
        {
            attribute = attributes.getNamedItem(attributeName);
        }

        return attribute;
    }

    private Node addBlock(String blockName)
    {
        Element root = xmlDocument.getDocumentElement();
        Element newBlock = xmlDocument.createElement(blockName);
        root.appendChild(newBlock);
        addParameterNode(newBlock);

        return newBlock;
    }

    private Node addParameterNode(Node parentNode)
    {
        Node parameter = xmlDocument.createElement("parameter");
        Node name = xmlDocument.createAttribute(Attribute.NAME);
        Node value = xmlDocument.createAttribute(Attribute.VALUE);
        NamedNodeMap attributes = parameter.getAttributes();
        attributes.setNamedItem(name);
        attributes.setNamedItem(value);
        //parameter.appendChild(attributeName);
        //parameter.appendChild(attributeValue);
        parentNode.appendChild(parameter);

        return parameter;
    }

    private void saveDocument()
    {
        try
        {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(pathToFile));
            Source input = new DOMSource(xmlDocument);

            transformer.transform(input, output);
        }
        catch (Exception exception)
        {
            System.err.print("XML save error!");
        }
    }

    public final class Attribute
    {
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
    }
}
