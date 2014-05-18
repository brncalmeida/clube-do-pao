package br.com.brncalmeida.clubepao.services;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Serviço responsável por traduzir planilha com membros.
 * 
 * @author bruno.almeida
 * 
 */
public interface ExcelFileService {

	public class ReportExcelService {
		private int qtdMembrosIncluidos;
		private List<String> erros;

		public ReportExcelService(List<String> erros, int qtdMembrosIncluidos) {
			this.erros = erros;
			this.qtdMembrosIncluidos = qtdMembrosIncluidos;
		}

		public int getQtdMembrosIncluidos() {
			return qtdMembrosIncluidos;
		}

		public List<String> getErros() {
			return Collections.unmodifiableList(erros);
		}
	}

	public enum ErrosValidacao {

		// TODO procurar forma de centralizar mensagems no enum, ver se é usual e se eh uma boa pratica de mercado
		NOME_INVALIDO("nome.invalido"), EMAIL_INVALIDO("email.invalido"), DISPONIBILIDADE_INVALIDA("disponibilidade.invalida");
		private String descricao;

		private ErrosValidacao(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}
	}

	/**
	 * Traduz a planilha para membros entidade (Membro.class)
	 * 
	 * @param file
	 *            arquivo a ser utilizado
	 * @param sobrescrever
	 *            caso deseja apagar os membros ja existentes
	 * @return Lista de membros padronizadas e incluidas em base
	 * @throws IOException
	 *             caso o arquivo não exista
	 */
	ReportExcelService parse(InputStream file, boolean sobrescrever) throws IOException;

	/**
	 * retorna planilha de exemplo
	 * 
	 * @return planilha de exemplo
	 */
	File getPlanilhaExemplo();

	/**
	 * Retorna planilha com todos os membros cadastrados na base
	 * 
	 * @return lista completa de membros cadastrados na base
	 * @throws IOException
	 */
	ByteArrayOutputStream getPlanilhaAtual();

}
