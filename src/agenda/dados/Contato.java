package agenda.dados;

import java.util.List;

public class Contato {

	private Long id;
	private String nome;
	private String sobreNome;
	private List<Telefone> telefones;
	
	public Contato(Long id, String nome, String sobreNome, List<Telefone> telefones) {
		this.id = id;
		this.setNome(nome);
		this.setSobreNome(sobreNome);
		this.telefones = telefones;
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSobreNome() {
		return sobreNome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}

	public List<Telefone> getTelefones() {
		return this.telefones;
	}

	@Override
	public String toString() {
		return String.format("%d;%s;%s%s", this.id, this.nome, this.sobreNome, String.valueOf(this.telefones).replace("[", "").replace("]", "").replace(",", "").replace(" ", ""));
	}

}
