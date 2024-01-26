package agenda.dados;

public class Telefone {

	private Long id;
	private String ddd;
	private Long numero;
	
	public Telefone (Long id, String ddd, Long numero) {
		this.id = id;
		this.setDdd(ddd);
		this.setNumero(numero);
	}

	public Long getId() {
		return id;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return String.format("/%d;%s;%d", this.id, this.ddd, this.numero);
	}

}
