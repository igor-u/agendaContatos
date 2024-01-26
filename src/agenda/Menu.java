package agenda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import agenda.dados.Contato;
import agenda.dados.Telefone;

public class Menu {

	private Scanner sc;

	public Menu (Scanner sc) {
		this.sc = sc;
	}

	public void obtemArquivo(List<Contato> contatos) {
		try {
			ManipuladorDeArquivo leitor = new ManipuladorDeArquivo();

			String linha = leitor.getReader().readLine();
			while (linha != null){

				String[] elementosLinha = linha.split("/");
				String contatoString = linha.substring(0, linha.indexOf("/"));
				String[] dadosContato = contatoString.split(";");

				int quantidadeTelefones = 0;

				for(int i = 0; i < linha.length(); i++) {
					if(linha.charAt(i) == '/'){
						quantidadeTelefones++;
					}
				}

				List<Telefone> telefones = new ArrayList<>();				  
				for(int i = 1; i <= quantidadeTelefones; i++) {
					String[] partesTelefone = elementosLinha[i].split(";");
					Telefone telefone = new Telefone(Long.parseLong(partesTelefone[0]), partesTelefone[1], Long.parseLong(partesTelefone[2]));
					telefones.add(telefone);
				}

				Contato contato = new Contato(Long.parseLong(dadosContato[0]), dadosContato[1], dadosContato[2], telefones);
				contatos.add(contato);

				linha = leitor.getReader().readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	public void mostraListaEMenu(List<Contato> contatos) {
		loopMenu: while (true) {

			System.out.println("##################\n"
					+ "##### AGENDA #####\n"
					+ "##################\n");
			System.out.printf("%-3s%-2s%s\n", "Id", "|", "Contato");

			for (Contato contato : contatos) {
				System.out.printf("%-3d%-2s%s %s\n", contato.getId(), "|", contato.getNome(), contato.getSobreNome());
				for (int i = 0; i < contato.getTelefones().size(); i++) {
					System.out.printf("%-3s%-2s%d. (%s) %d\n", " ", "|", contato.getTelefones().get(i).getId(), contato.getTelefones().get(i).getDdd(), contato.getTelefones().get(i).getNumero());
				}
			}

			System.out.println();
			System.out.println(">>>> Menu <<<<\n"
					+ "1 - Adicionar Contato\n"
					+ "2 - Remover Contato\n"
					+ "3 - Editar Contato\n"
					+ "4 - Sair");

			int input = recebeInputMenu();

			switch (input) {	

			case 1:
				adicionaContato(contatos);
				break;

			case 2:
				removeContato(contatos);
				break;

			case 3:
				editaContato(contatos);
				break;

			case 4:
				this.sc.close();
				break loopMenu;	
			}
		}
	}
	private int recebeInputMenu() {
		int input = 0;
		boolean inputValido = false;

		do { try {
			input = this.sc.nextInt(); 
			if (input != 1 && input != 2 && input != 3 && input != 4) {
				System.out.println("Digite um número válido.");
			}
			else inputValido = true;
		}
		catch (InputMismatchException e) {
			System.out.println("Houve um erro. Tente novamente.");
			sc.next();

		} } while (!inputValido);

		return input;
	}
	private void adicionaContato(List<Contato> contatos) {
		Long id = Long.valueOf(contatos.size()) + 1;

		while (jaExisteIdCont(id, contatos)) {
			id++;
		}

		System.out.println("Digite o nome do contato: ");
		String nome = this.sc.next();
		System.out.println("Digite o sobrenome do contato: ");
		String sobrenome = sc.next();

		List<Telefone> telefones = criaListaTelefone(contatos);

		Contato contato = new Contato(id, nome, sobrenome, telefones);

		contatos.add(contato);

		ManipuladorDeArquivo escritor = new ManipuladorDeArquivo();
		escritor.reescreveArquivo(contatos);
	}
	private boolean jaExisteIdCont(Long id, List<Contato> lista) {
		return (lista.stream().anyMatch(contato -> contato.getId().equals(id))); 

	}
	private boolean jaExisteIdTel(Long id, List<Telefone> lista) {
		return (lista.stream().anyMatch(telefone -> telefone.getId().equals(id))); 

	}
	private boolean jaExisteTelefone(String ddd, Long numero, List<Contato> lista) {		
		boolean existe = false;

		for (Contato contato : lista) {
			existe = ((contato.getTelefones().stream().anyMatch(tel -> tel.getDdd().equals(ddd))) &&
					(contato.getTelefones().stream().anyMatch(tel -> tel.getNumero().equals(numero))));

			if (existe) return existe;			
		}		
		return existe;		
	}
	private void removeContato(List<Contato> contatos) {
		contatos.remove(achaContato(contatos));

		ManipuladorDeArquivo escritor = new ManipuladorDeArquivo();
		escritor.reescreveArquivo(contatos);
	}
	private void editaContato(List<Contato> contatos) {
		Contato contatoAEditar = achaContato(contatos);

		int simOuNao = 0;

		do { try {
			System.out.println("Deseja alterar o nome do contato?\n"
					+ "1 - Sim\n"
					+ "2 - Não");
			simOuNao = sc.nextInt();
			if (simOuNao != 1 && simOuNao != 2) {
				System.out.println("Digite um número válido.");
			}
		} 
		catch (InputMismatchException e) {
			imprimeMensagemErro();
			sc.next();
		}
		} while (simOuNao != 1 && simOuNao != 2);

		if (simOuNao == 1) {
			System.out.println("Digite o novo nome do contato: ");
			String nome = sc.next();
			System.out.println("Digite o novo sobrenome do contato: ");
			String sobrenome = sc.next();

			contatoAEditar.setNome(nome);
			contatoAEditar.setSobreNome(sobrenome);
		}

		System.out.println(">>>> Telefones <<<<\n"
				+ "1 - Adicionar Telefone\n"
				+ "2 - Remover Telefone\n"
				+ "3 - Editar Telefone\n"
				+ "4 - Sair");

		int input = recebeInputMenu();

		switch (input) {	

		case 1:
			adicionaTelefone(contatoAEditar.getTelefones(), contatos);
			break;

		case 2:
			removeTelefone(contatoAEditar.getTelefones());
			break;

		case 3:
			editaTelefone(contatoAEditar.getTelefones(), contatos);
			break;

		case 4:
			break;
		}

		ManipuladorDeArquivo escritor = new ManipuladorDeArquivo();
		escritor.reescreveArquivo(contatos);
	}
	private void adicionaTelefone(List<Telefone> telefones, List<Contato> contatos) {
		Long id = Long.valueOf(telefones.size()) + 1;

		while (jaExisteIdTel(id, telefones)) {
			id++;
		}

		String ddd;
		Long numero;

		do {
			ddd = criaDDD();
			numero = criaNumero();

			if (jaExisteTelefone(ddd, numero, contatos)) {
				System.out.println("O número já existe.");
			}
		} while(jaExisteTelefone(ddd, numero, contatos));

		telefones.add(new Telefone(id, ddd, numero));		
	}
	private void removeTelefone(List<Telefone> telefones) {
		telefones.remove(achaTelefone(telefones));

	}
	private void editaTelefone(List<Telefone> telefones, List<Contato> contatos) {
		Telefone telefoneAEditar = achaTelefone(telefones);

		String ddd;
		Long numero;

		do {
			ddd = criaDDD();
			numero = criaNumero();

			if (jaExisteTelefone(ddd, numero, contatos)) {
				System.out.println("O número já existe.");
			}
		} while(jaExisteTelefone(ddd, numero, contatos));

		telefoneAEditar.setDdd(ddd);
		telefoneAEditar.setNumero(numero);
	}
	private Long criaNumero() {
		Long numero = 0L;
		String numeroString = "";

		do { try {
			System.out.println("Digite o número do telefone: ");
			numero = sc.nextLong();
			numeroString = String.valueOf(numero);

			if (numeroString.length() != 8 && numeroString.length() != 9) {
				System.out.println("O número deve conter 8 ou 9 caracteres numéricos.");
			}
		}
		catch (InputMismatchException e) {
			imprimeMensagemErro();
			sc.next();
		}
		} while (numeroString.length() != 8 && numeroString.length() != 9);

		return numero;
	}
	private String criaDDD() {
		int ddd = 0;
		String dddString = "";

		do { try {
			System.out.println("Digite o DDD do telefone: ");
			ddd = sc.nextInt();
			dddString = String.valueOf(ddd);

			if (dddString.length() != 2) {
				System.out.println("O DDD deve conter 2 caracteres numéricos.");
			}
		}
		catch (InputMismatchException e) {
			imprimeMensagemErro();
			sc.next();
		}

		} while (dddString.length() != 2);

		return dddString;
	}
	private Contato achaContato(List<Contato> contatos) {
		Long id = recebeId("contato");

		for (Contato contato : contatos) {
			if (contato.getId() == id) {
				Contato contatoAEditar = contato;
				return contatoAEditar;
			}
		}

		System.out.println("O contato não existe.");

		return achaContato(contatos);
	}
	private Telefone achaTelefone(List<Telefone> telefones) {
		Long id = recebeId("telefone");

		for (Telefone telefone : telefones) {
			if (telefone.getId() == id) {
				Telefone telefoneAEditar = telefone;
				return telefoneAEditar;
			}
		}

		System.out.println("O telefone não existe.");
		return achaTelefone(telefones);	
	}
	private List<Telefone> criaListaTelefone(List<Contato> contatos) {
		List<Telefone> listaTel = new ArrayList<Telefone>();

		adicionaTelefone(listaTel, contatos);
		return listaTel;
	}
	private Long recebeId(String contatoOuTelefone) {

		do { try {
			System.out.printf("Digite o ID do %s : \n", contatoOuTelefone);
			Long id = sc.nextLong();
			return id;
		}
		catch (InputMismatchException e) {
			imprimeMensagemErro();
			sc.next();
		}
		} while (true);
	}
	private void imprimeMensagemErro() {
		System.out.println("Entrada inválida. Tente novamente.");
	}

}
