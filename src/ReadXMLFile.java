import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.TreeSet;

public class ReadXMLFile {

    public static void main(String[] args) {
        findCheapestAndMostExpensiveСurrency();
    }

    private static void findCheapestAndMostExpensiveСurrency() {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = domFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document xmlDocument;
        try {
            xmlDocument = builder.parse("https://www.cbr.ru/scripts/XML_daily.asp");
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        TreeSet<Currency> decimalSet = new TreeSet<>();

        NodeList nodes = xmlDocument.getDocumentElement().getElementsByTagName("Valute");

        for (int i = 0; i < nodes.getLength(); i++) {
            NodeList item = nodes.item(i).getChildNodes();
            String name = ((Element) item).getElementsByTagName("Name").item(0).getTextContent();
            BigDecimal value = new BigDecimal(((Element) item).getElementsByTagName("Value").item(0).getTextContent().replace(",","."));
            Currency currency = new Currency(name, value);
            decimalSet.add(currency);
        }

        if(!decimalSet.isEmpty()) {
            System.out.format("Самая дешевая валюта в рублях: %s. Cтоимость 1-ой единицы в рублях %.4f%n", decimalSet.first().getName(), decimalSet.first().getValue());
            System.out.format("Самая дорогая валюта в рублях: %s. Cтоимость 1-ой единицы в рублях %.4f%n", decimalSet.last().getName(), decimalSet.last().getValue());
        }
    }

    private static class Currency implements Comparable<Currency> {
        private final String name;
        private final BigDecimal value;

        Currency(String name, BigDecimal value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public BigDecimal getValue() {
            return value;
        }

        @Override
        public int compareTo(Currency o) {
            return value.compareTo(o.value);
        }
    }
}