package recetario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Menu {
	
	private ArrayList<Receta> listReceta = new ArrayList<Receta>();
	
	public void menu() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		ArrayList<Receta>  recetas =leerXml();
		mostrarRecetas();
		
		createNewXml(recetas);
	}
	
	private ArrayList<Receta> leerXml() throws ParserConfigurationException, SAXException, IOException{
		//lo necesario
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document documento = (Document) builder.parse( new File("C:\\Users\\Aarón\\Documents\\xml\\RecetaDOM.xml") );
		
		/*Node nodoRaiz = ((Node) documento).getFirstChild();
		System.out.println(nodoRaiz.getNodeName());
		NodeList listaNodosHijos = nodoRaiz.getChildNodes();*/
		//preguntar a sergio sobre el formato XML
		Node nodoRaiz = documento.getFirstChild();
		NodeList recetas = ((Element) nodoRaiz).getElementsByTagName("Receta");
		
		for (int i = 0; i < recetas.getLength(); i++) {
            Node recetaNode = recetas.item(i);
            if (recetaNode.getNodeType() == Node.ELEMENT_NODE) {
                Element recetaElement = (Element) recetaNode;

                String titulo =  recetaElement.getElementsByTagName("titulo").item(0).getTextContent();
                String tiempo =recetaElement.getElementsByTagName("tiempo").item(0).getTextContent();
                String procedimiento = recetaElement.getElementsByTagName("procedimiento").item(0).getTextContent();

                Receta receta = new Receta(titulo, tiempo, procedimiento);
                NodeList ingredientes = recetaElement.getElementsByTagName("ingrediente");

                for (int j = 0; j < ingredientes.getLength(); j++) {
                    Element ingredienteElement = (Element) ingredientes.item(j);
                    String cantidad = ingredienteElement.getAttribute("cantidad");
                    String nombre = ((Node) ingredienteElement).getTextContent();
                   receta.addIngrediente(cantidad, nombre);
                    
                }             
                listReceta.add(receta);
            }
        }
		
		return listReceta;
		
	}

	private void mostrarRecetas() {
		for(Receta x : listReceta) {
			System.out.println("--------------------------");
			System.out.println("Nombre: " + x.getTitulo());
			System.out.println("Tiempo: " + x.getTiempo());
			System.out.println("Ingredientes: ");
			for(Ingrediente y:  x.verIngredientes()) {
				System.out.println("\tNombre: " + y.getIngrediente() + " Cantidad: " + y.getCantidad());
			}
			System.out.println("Procedimiento: " + x.getProcedimiento());
			System.out.println("--------------------------");
		}
	}
	
	
	private void addReceta(Receta receta) {
		listReceta.add(receta);
	}
	
	private void deleteReceta(String titulo) {
		int aux = 0;
		int delete = 0;
		for (Receta x : listReceta) {
			if (x.getTitulo().equals(titulo)) {
				delete = aux;
			}
			aux++;
		}
		listReceta.remove(delete);
	}
	
	private void updateRecetaTitulo (String titulonuevo, String tituloActual) {
		
		for (Receta x : listReceta) {
			if (x.getTitulo().equals(tituloActual)) {
				x.setTitulo(titulonuevo);
			}
		}
	}
	
	
	private void createNewXml(ArrayList<Receta> recetas) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document documento = (Document) builder.parse(new File("C:\\Users\\Aarón\\Documents\\xml\\RecetaDOM.xml"));

		Node nodoRaiz = documento.getFirstChild();
		
            // Paso 2: Crear un nuevo elemento <Receta> y sus elementos secundarios
		
		for(Receta x: recetas) {
			Element nuevaReceta = documento.createElement("Receta");
			Element titulo = documento.createElement("titulo");
            titulo.setTextContent(x.getTitulo());
            
            Element ingredientes = documento.createElement("ingredientes");
            for(Ingrediente z: x.verIngredientes()) {
            	Element ingrediente = documento.createElement("ingrediente");
            	ingrediente.setAttribute("cantidad", z.getCantidad());
            	ingrediente.setTextContent(z.getIngrediente());
            	ingredientes.appendChild(ingrediente);
            }
            
            Element procedimiento = documento.createElement("procedimiento");
            procedimiento.setTextContent(x.getProcedimiento());

            Element tiempo = documento.createElement("tiempo");
            tiempo.setTextContent(x.getTiempo());
            
            nuevaReceta.appendChild(titulo);
            nuevaReceta.appendChild(ingredientes);
            nuevaReceta.appendChild(procedimiento);
            nuevaReceta.appendChild(tiempo);
            
            Element elementoRaiz = documento.getDocumentElement();
            elementoRaiz.appendChild(nuevaReceta);
		}
            // Paso 4: Guardar el XML modificado en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(documento);
            StreamResult result = new StreamResult(new File("C:\\\\Users\\\\Aarón\\\\Documents\\\\xml\\\\RecetaDOM2.xml"));
            transformer.transform(source, result);
	}
	
}
