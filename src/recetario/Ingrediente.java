package recetario;

public class Ingrediente {

	private String cantidad;
	private String ingrediente;
	
	public Ingrediente(String cantidad, String ingrediente) {
		this.cantidad = cantidad;
		this.ingrediente = ingrediente;
	}
	
	public String getCantidad() {
		return cantidad;
	}
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public String getIngrediente() {
		return ingrediente;
	}
	public void setIngrediente(String ingrediente) {
		this.ingrediente = ingrediente;
	}
}
