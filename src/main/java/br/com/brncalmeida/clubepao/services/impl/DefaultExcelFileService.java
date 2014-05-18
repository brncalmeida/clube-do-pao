package br.com.brncalmeida.clubepao.services.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import br.com.brncalmeida.clubepao.dao.MembroDao;
import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;
import br.com.brncalmeida.clubepao.services.ExcelFileService;
import br.com.brncalmeida.clubepao.utils.Util;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Serviço responsável por traduzir planilha com membros.
 * 
 * @author bruno.almeida
 * 
 */
@Component
public class DefaultExcelFileService implements ExcelFileService {

	private String sheetName;
	private MembroDao dao;
	private Localization localization;

	public DefaultExcelFileService(MembroDao dao, Localization localization) {
		this.dao = dao;
		this.localization = localization;
		this.sheetName = "Membros";
	}

	public File getPlanilhaExemplo() {
		return new File("/planilha_exemplo_upload_membros.xlsx");
	}

	/**
	 * Busca a sheet na planilha
	 * 
	 * @param file
	 *            arquivo a ser utilizado
	 * @return sheet (planilha)
	 * @throws IOException
	 *             caso o arquivo não exista
	 */
	protected Sheet getWorkSheet(InputStream file) throws IOException {
		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			throw new IllegalArgumentException(Util.getMessage(localization, "tipo.planilha.invalida"));
		}
		Sheet worksheet = workbook.getSheet(sheetName);
		return worksheet;
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
	public ReportExcelService parse(InputStream file, boolean sobrescrever) throws IOException {
		if (file == null) {
			throw new NullPointerException(Util.getMessage(localization, "planilha.invalida"));
		}

		Sheet worksheet = getWorkSheet(file);

		List<Membro> membros = new ArrayList<Membro>();
		List<String> erros = new ArrayList<String>();

		Row row;
		String nome = null;
		String email = null;
		Membro membro;
		Set<Disponibilidade> disponibilidades;
		List<ErrosValidacao> errosValidacao;
		StringBuilder sb;

		// ocorrer para todas as linhas da planilha
		for (int i = 1; i <= worksheet.getLastRowNum(); i++) {
			sb = new StringBuilder();
			// TODO testar erros validação
			errosValidacao = new ArrayList<ErrosValidacao>();
			disponibilidades = new LinkedHashSet<Disponibilidade>();

			// buscar linha
			row = worksheet.getRow(i);

			// validação da coluna nome
			if (row.getCell(0) != null) {
				nome = row.getCell(0).getStringCellValue();
			} else {
				errosValidacao.add(ErrosValidacao.NOME_INVALIDO);
			}

			// validação da coluna email
			if (row.getCell(1) != null) {
				email = row.getCell(1).getStringCellValue();
			} else {
				errosValidacao.add(ErrosValidacao.EMAIL_INVALIDO);
			}

			// adicionando disponibilidades
			if (row.getCell(2) != null && "x".equalsIgnoreCase(row.getCell(2).getStringCellValue().replace(" ", ""))) {
				disponibilidades.add(Disponibilidade.SEGUNDA);
			}
			if (row.getCell(3) != null && "x".equalsIgnoreCase(row.getCell(3).getStringCellValue().replace(" ", ""))) {
				disponibilidades.add(Disponibilidade.TERCA);
			}
			if (row.getCell(4) != null && "x".equalsIgnoreCase(row.getCell(4).getStringCellValue().replace(" ", ""))) {
				disponibilidades.add(Disponibilidade.QUARTA);
			}
			if (row.getCell(5) != null && "x".equalsIgnoreCase(row.getCell(5).getStringCellValue().replace(" ", ""))) {
				disponibilidades.add(Disponibilidade.QUINTA);
			}
			if (row.getCell(6) != null && "x".equalsIgnoreCase(row.getCell(6).getStringCellValue().replace(" ", ""))) {
				disponibilidades.add(Disponibilidade.SEXTA);
			}

			// validação das colunas de disponibilidades
			if (disponibilidades.size() == 0) {
				errosValidacao.add(ErrosValidacao.DISPONIBILIDADE_INVALIDA);
			}

			if (errosValidacao.size() > 0) {
				sb.append(Util.getMessage(localization, "erro.membro.linha", String.valueOf(i)));
				for (int j = 0; j < errosValidacao.size(); j++) {
					if (j > 0)
						sb.append(", ");
					sb.append(Util.getMessage(localization, errosValidacao.get(j).getDescricao()));
				}
				sb.append(" ").append(Util.getMessage(localization, "membro.nao.adicionado"));
				erros.add(sb.toString());

			} else {
				membro = new Membro(nome, email);
				membro.addAllDisponibilidades(disponibilidades.toArray(new Disponibilidade[0]));
				membros.add(membro);
			}
		}

		if (sobrescrever) {
			limparBase();
		}
		dao.addAll(membros);

		ReportExcelService report = new ReportExcelService(erros, membros.size());
		return report;
	}

	/**
	 * limpa membros para sobrescrever
	 */
	public void limparBase() {
		dao.removeAll(dao.listarTodos());
	}
}
