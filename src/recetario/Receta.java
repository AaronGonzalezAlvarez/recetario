package recetario;

import java.util.ArrayList;

public class Receta {
	
	private String titulo;
	private String tiempo;
	private String procedimiento;
	private ArrayList<Ingrediente> ingredientes = new ArrayList();
	
	
	public Receta(String titulo, String tiempo, String procedimiento) {
		this.titulo = titulo;
		this.tiempo = tiempo;
		this.procedimiento = procedimiento;
	}

	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTiempo() {
		return tiempo;
	}
	public void setTiempo(String tiempo) {
		this.tiempo = tiempo;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	
	
	public void addIngrediente(String cantidad,String ingrediente) {
		ingredientes.add(new Ingrediente(cantidad,ingrediente));
	}
	
	public ArrayList<Ingrediente> verIngredientes() {
		return ingredientes;
	}
	
}
