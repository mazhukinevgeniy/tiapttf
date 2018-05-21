package homemade.menu.model.save;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Save {
    private String pathToFile;
    private Document xmlDocument;

    public Save(String path) {
        pathToFile = path;
        xmlDocument = getXMLDocument();
    }

    private Document getXMLDocument() {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            File file = new File(pathToFile);
            document = builder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException exception) {
            document = new DocumentImpl();
            createRootNode(document);
            saveDocument();
        }

        return document;
    }

    private void createRootNode(Document document) {
        Element newBlock = document.createElement(SaveNode.ROOT_NODE);
        document.appendChild(newBlock);
    }

    private void saveDocument() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File(pathToFile));
            Source input = new DOMSource(xmlDocument);

            transformer.transform(input, output);
        } catch (TransformerException exception) {
            System.err.print("XML save error!");
        }
    }

    public String getParameterValue(String blockName, String parameterName) {
        Node block = findBlock(blockName);
        String value = null;
        if (block != null) {
            Node node = findNode(block, parameterName);
            if (node != null) {
                value = getAttributeValue(node, Attribute.VALUE);
            }
        }
        return value;
    }

    public void setParameterValue(String blockName, String parameterName, String newValue) {
        Node block = findBlock(blockName);
        if (block == null) {
            block = addBlock(blockName);
        }

        Node parameterNode = findNode(block, parameterName);
        if (parameterNode == null) {
            parameterNode = addParameterNode(block, parameterName);
        }
        setAtributeValue(parameterNode, Attribute.VALUE, newValue);

        saveDocument();
    }

    private Node findBlock(String blockName) {
        Node mainNode = getMainNode();
        Node sought = null;

        NodeList children = mainNode.getChildNodes();
        int numberOfChild = children.getLength();
        for (int i = 0; i < numberOfChild; ++i) {
            Node currentBlock = children.item(i);
            String currentBlockName = currentBlock.getNodeName();
            if (currentBlockName.equals(blockName)) {
                sought = currentBlock;
                break;
            }
        }
        return sought;
    }

    private Node getMainNode() {
        return xmlDocument.getChildNodes().item(0);
    }

    private Node findNode(Node block, String parameterName) {
        List<Node> nodeList = findOf(block, parameterName, true);

        return nodeList.isEmpty() ? null : nodeList.get(0);
    }

    private List<Node> findNodes(Node block, String parameterName) {
        return findOf(block, parameterName, false);
    }

    private List<Node> findOf(Node block, String parameterName, boolean justFirst) {
        List<Node> soughts = new ArrayList<>();

        NodeList children = block.getChildNodes();
        int numberOfChild = children.getLength();
        for (int i = 0; i < numberOfChild; ++i) {
            Node node = children.item(i);
            if (isEligibleNode(node, parameterName)) {
                soughts.add(node);
                if (justFirst) {
                    break;
                }
            }
        }
        return soughts;
    }

    private boolean isEligibleNode(Node node, String parameterName) {
        boolean isEligible = false;
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            Node nameAttribute = attributes.getNamedItem(Attribute.NAME);
            if (nameAttribute != null) {
                String name = nameAttribute.getNodeValue();
                if (name.equals(parameterName)) {
                    isEligible = true;
                }
            }
        }

        return isEligible;
    }

    private String getAttributeValue(Node parameterNode, String attributeName) {
        Node attribute = getAttribute(parameterNode, attributeName);
        String value = null;
        if (attribute != null) {
            value = attribute.getNodeValue();
        }
        return value;
    }

    private void setAtributeValue(Node parameterNode, String attributeName, String value) {
        Node attribute = getAttribute(parameterNode, attributeName);
        if (attribute != null) {
            attribute.setNodeValue(value);
        }
    }

    private Node getAttribute(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        Node attribute = null;
        if (attributes != null) {
            attribute = attributes.getNamedItem(attributeName);
        }
        return attribute;
    }

    private Node addBlock(String blockName) {
        Element root = xmlDocument.getDocumentElement();
        Element newBlock = xmlDocument.createElement(blockName);
        root.appendChild(newBlock);

        return newBlock;
    }

    private Node addParameterNode(Node parentNode, String parameterName) {
        Node parameter = xmlDocument.createElement(SaveNode.PARAMETER);
        Node name = xmlDocument.createAttribute(Attribute.NAME);
        name.setNodeValue(parameterName);
        Node value = xmlDocument.createAttribute(Attribute.VALUE);
        NamedNodeMap attributes = parameter.getAttributes();

        attributes.setNamedItem(name);
        attributes.setNamedItem(value);
        parentNode.appendChild(parameter);

        return parameter;
    }

    public void addParameter(String blockName, String parameterName, String value) {
        Node block = findBlock(blockName);
        if (block == null) {
            block = addBlock(blockName);
        }

        Node parameterNode = addParameterNode(block, parameterName);
        setAtributeValue(parameterNode, Attribute.VALUE, value);

        saveDocument();
    }

    public List<String> getParametersValues(String blockName, String parametersName) {
        List<String> values = new ArrayList<>();

        Node block = findBlock(blockName);
        if (block != null) {
            List<Node> parameters = findNodes(block, parametersName);
            for (Node param : parameters) {
                String value = getAttributeValue(param, Attribute.VALUE);
                values.add(value);
            }
        }

        return values;
    }

    public void deleteParameters(String blockName, String parametersName) {
        Node block = findBlock(blockName);
        if (block != null) {
            List<Node> parameters = findNodes(block, parametersName);
            for (Node param : parameters) {
                block.removeChild(param);
            }
            saveDocument();
        }
    }

    private static final class Attribute {
        private static final String NAME = "name";
        private static final String VALUE = "value";
    }

    private static final class SaveNode {
        private static final String PARAMETER = "parameter";
        private static final String ROOT_NODE = "data";
    }
}
