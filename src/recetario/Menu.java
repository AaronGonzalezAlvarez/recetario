package recetario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Menu {
	
	private ArrayList<Receta> listReceta = new ArrayList<Receta>();
	
	public void menu() throws ParserConfigurationException, SAXException, IOException, TransformerException {
		boolean menu = true;
		int opcion;
		Scanner scanner = new Scanner(System.in);
		listReceta =leerXml();
		while(menu) {
			System.out.println("Recetas.");
			System.out.println("1. Listar recetas.");
			System.out.println("2. Add receta");
			System.out.println("3. Modificar titulo");
			System.out.println("4. Eliminar receta");
			System.out.println("0. salir.");
			System.out.print("Seleccione opción.");
			opcion = Integer.parseInt(scanner.nextLine());
			
			switch (opcion) {
			case 1:
				mostrarRecetas();
				System.out.print("presiona tecla para continuar");
				scanner.nextLine();
				break;
			case 2:
				System.out.print("Titulo: ");
				String titulo = scanner.nextLine();
				System.out.print("Tiempo: ");
				String tiempo = scanner.nextLine();
				System.out.print("Procedimiento: ");
				String prodecimiento = scanner.nextLine();
				Receta newReceta = new Receta(titulo, tiempo, prodecimiento);
				boolean aux = true;
				while (aux) {
					System.out.print("ingrediente: ");
					String ingrediente = scanner.nextLine();
					System.out.print("cantidad: ");
					String cantidad = scanner.nextLine();
					newReceta.addIngrediente(cantidad, ingrediente);
					System.out.print("Otro ingrediente 1/0?");
					int otro = Integer.parseInt(scanner.nextLine());
					if (otro == 0) {
						aux = false;
					}
				}
				addReceta(newReceta);
				System.out.print("Receta añadida, pulsa una tecla para continuar");
				scanner.nextLine();
				
				break;
			case 3:
				System.out.print("Nombre de recete a cambiar: ");
				String nombreActual = scanner.nextLine();
				System.out.print("Nuevo nombre: ");
				String newNombre = scanner.nextLine();
				boolean change = updateRecetaTitulo(newNombre,nombreActual);
				if(change) {
					System.out.print("Receta cambiada con exito");
				}else {
					System.out.print("No se ha podido modificar");
				}
				scanner.nextLine();
				
				break;
			case 4:
				System.out.print("Escriba el titulo de la receta: ");
				String tituloDelte = scanner.nextLine();
				boolean result = deleteReceta(tituloDelte);
				if(result) {
					System.out.print("Receta eliminada con existo");
				}else {
					System.out.print("No se ha podido eliminar");
				}
				scanner.nextLine();
				break;
			case 0:
				System.out.print("Guardar cambios: si=1, no=0");
				int cambios = Integer.parseInt(scanner.nextLine());
				if(cambios == 1) {
					//createNewXml(listReceta);
					crearNuevoXML(listReceta);
				}
				System.out.print("Vuelva cuando quiera");
				menu=false;
				
				break;
			default:
				throw new IllegalArgumentException("Parámetro no valido: " + opcion);
			}
		}
		 
		
		
		//usar luego1
		//createNewXml(recetas);
	}
	
	private ArrayList<Receta> leerXml() throws ParserConfigurationException, SAXException, IOException{
		//lo necesario
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document documento = (Document) builder.parse( new File("C:\\Users\\DAW_M\\Documents\\xml\\RecetaDOM.xml") );
		
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
		if(listReceta.size() == 0) {
			System.out.println("Recetero vacio");
		}else {
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
	}
	
	
	private void addReceta(Receta receta) {
		listReceta.add(receta);
	}
	
	private boolean deleteReceta(String titulo) {
		int aux = 0;
		int delete =-1;
		boolean deleted = false;
		for (Receta x : listReceta) {
			if (x.getTitulo().equals(titulo)) {
				delete = aux;
				deleted = true;
			}
			aux++;
		}
		if(delete != -1) {
			listReceta.remove(delete);			
		}
		return deleted;
	}
	
	private boolean updateRecetaTitulo (String titulonuevo, String tituloActual) {
		boolean change= false;
		for (Receta x : listReceta) {
			if (x.getTitulo().equals(tituloActual)) {
				x.setTitulo(titulonuevo);
				change=true;
			}
		}
		return change;
	}
	
	//crearlo desde cero
	private void crearNuevoXML(ArrayList<Receta> recetas) throws ParserConfigurationException, TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		// Creo una instancia de DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// Creo un documentBuilder
		DocumentBuilder builder = factory.newDocumentBuilder();
		// Creo un DOMImplementation
		DOMImplementation implementation = builder.getDOMImplementation();
		
		Document documento = implementation.createDocument(null, "Recetas", null);
		documento.setXmlVersion("1.0");
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
            
            documento.getDocumentElement().appendChild(nuevaReceta);
		}
		
		// Asocio el source con el Document
		Source source = new DOMSource(documento);
		// Creo el Result, indicado que fichero se va a crear
		Result result = new StreamResult(new File("concesionario.xml"));
		 
		// Creo un transformer, se crea el fichero XML
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.transform(source, result);
	}
	
	private void createNewXml(ArrayList<Receta> recetas) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document documento = (Document) builder.parse(new File("C:\\Users\\DAW_M\\Documents\\xml\\RecetaDOMNew.xml"));

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
            StreamResult result = new StreamResult(new File("C:\\Users\\DAW_M\\Documents\\xml\\RecetaDOM2.xml"));
            transformer.transform(source, result);
	}
	
}
