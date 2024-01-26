package agenda;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import agenda.dados.Contato;

public class Main {

	public static void main(String[] args) {	

		Scanner scanner = new Scanner(System.in);
		List<Contato> lista = new ArrayList<Contato>();

		Menu menu = new Menu(scanner);
		menu.obtemArquivo(lista);
		menu.mostraListaEMenu(lista);

	}

}

