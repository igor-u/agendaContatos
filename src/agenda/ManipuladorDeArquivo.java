package agenda;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import agenda.dados.Contato;

public class ManipuladorDeArquivo {

	private String arquivo = "bancoDeDados/agenda.txt";
	private FileReader leitorArquivo;
	private BufferedReader reader;
	
	public ManipuladorDeArquivo () {
		try {
			this.leitorArquivo = new FileReader(this.arquivo);
			this.reader = new BufferedReader(leitorArquivo);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void reescreveArquivo(List<Contato> contatos) {
		try {
			FileWriter stream = new FileWriter(this.arquivo, false);
			PrintWriter print = new PrintWriter(stream);

			for (Contato contato : contatos) {
				String linha = contato.toString();
				print.println(linha);
			}

			print.close();
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public BufferedReader getReader() {
		return this.reader;
	}

}
