package com.example.viikko9xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class TeatteriLista {

    ArrayList<Teatteri> tLista = new ArrayList<>();
    private int lukumaara = 0;
    private static TeatteriLista theaters = null;

    public static TeatteriLista getInstance() {
        if (theaters == null) {
            theaters = new TeatteriLista();
        } return theaters;
    }

    public void lisaaTeatteri(int i, String s) {
        Teatteri teatteri = new Teatteri(i, s);
        tLista.add(teatteri);
        lukumaara++;
    }

    public int getLukumaara() {
        return lukumaara;
    }

    public void tulostaTeatteri(int i) {
        System.out.println("Nimi: " + tLista.get(i).getName());
        System.out.println("ID: " + tLista.get(i).getId());
    }

    public String getName(int i) {
        return tLista.get(i).getName();
    }

    public int getID(int i) {
        return tLista.get(i).getId();
    }

    public TeatteriLista lueXML(TeatteriLista tLista) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = dBuilder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("TheatreArea");


            for (int i = 0 ; i < nList.getLength() ; i++) {
                Node node = nList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    tLista.lisaaTeatteri(Integer.parseInt(element.getElementsByTagName("ID").item(0).getTextContent()), element.getElementsByTagName("Name").item(0).getTextContent());

  /*                  System.out.print("TheatreArea ID: ");
                    System.out.println(element.getElementsByTagName("ID").item(0).getTextContent());
                    System.out.print("TheatreArea Name: ");
                    System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());
*/
                }

            }
            return tLista;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("##################DONITSI####################");
        }
        return null;
    }

    public NodeList parsiNaytokset(int ID, String pvm) {
        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "http://www.finnkino.fi/xml/Schedule/?area=" + ID + "&dt=" + pvm;
            Document doc = dBuilder.parse(urlString);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");
            return nList;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("##################DONITSI####################");
        } return null;
    }

    public String haeNaytos(NodeList nodes, int a) {
        Node node = nodes.item(a);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String name = element.getElementsByTagName("Title").item(0).getTextContent();
                return name;
        }
        return null;
    }

    public String haeAika(NodeList nodes, int a, String alku, String loppu) {
        Node node = nodes.item(a);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String aika = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
            aika = aika.substring(11);
            SimpleDateFormat parser = new SimpleDateFormat("hh:mm");
            try {
                Date alkuaika = parser.parse(alku);
                Date loppuaika = parser.parse(loppu);
                Date esitysaika = parser.parse(aika.substring(0,5));
                if (esitysaika.after(alkuaika) && esitysaika.before(loppuaika)) {
                    return aika;
                } else {
                    return null;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String haeSali(NodeList nodes, int a) {
        Node node = nodes.item(a);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String sali = element.getElementsByTagName("TheatreAuditorium").item(0).getTextContent();
            return sali;
        }
        return null;
    }

    public String haeTeatteri(NodeList nodes, int a) {
        Node node = nodes.item(a);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String teatteri = element.getElementsByTagName("Theatre").item(0).getTextContent();
            return teatteri;
        }
        return null;
    }

}
