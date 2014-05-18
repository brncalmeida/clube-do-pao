package br.com.brncalmeida.clubepao.controller;

import org.junit.Before;
import org.junit.Test;

import br.com.brncalmeida.clubepao.dao.DefaultMembroDao;
import br.com.brncalmeida.clubepao.dao.MembroDao;
import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;
import br.com.brncalmeida.clubepao.services.ExcelFileService;
import br.com.brncalmeida.clubepao.services.impl.DefaultExcelFileService;
import br.com.caelum.vraptor.validator.ValidationException;

public class MembrosControllerTest extends GenericTest {

	private MembroDao dao;
	private MembrosController controller;
	private ExcelFileService planilha;

	@Before
	public void setUp() throws Exception {
		dao = new DefaultMembroDao(getSession());
		planilha = new DefaultExcelFileService(dao, getLocalization());
		controller = new MembrosController(getMockResult(), dao, getMockValidator(), getLocalization(), planilha);
	}

	public static void main(String[] args) throws Exception {
		MembrosControllerTest test = new MembrosControllerTest();
		test.setUp();
		test.alterandoMembroInvalido();
	}

	@Test
	public void adicionandoMembroValido() throws Exception {
		Membro membro = criarMassa(false);
		controller.add(membro, new Integer[] { Disponibilidade.SEGUNDA.getId() });
	}

	@Test(expected = ValidationException.class)
	public void adicionandoMembroInvalido() throws Exception {
		final Membro membro = new Membro("", "");
		controller.add(membro, new Integer[] { Disponibilidade.TERCA.getId() });
	}

	@Test(expected = ValidationException.class)
	public void adicionandoMembroComDisponibilidadeInvalida() throws Exception {
		Membro membro = criarMassa(false);
		controller.add(membro, new Integer[] { Integer.MAX_VALUE });
	}

	@Test
	public void alterandoMembroValido() throws Exception {
		Membro membro = criarMassa(true);
		controller.alterar(membro, new Integer[] { Disponibilidade.QUARTA.getId() });
	}

	@Test(expected = ValidationException.class)
	public void alterandoMembroInvalido() throws Exception {
		Membro membro = criarMassa(false);
		membro.setId(Long.MAX_VALUE);
		controller.alterar(membro, new Integer[] { Disponibilidade.SEXTA.getId() });
	}

	@Test(expected = ValidationException.class)
	public void alterandoMembroComDisponibilidadeInvalida() throws Exception {
		Membro membro = criarMassa(true);
		controller.alterar(membro, new Integer[] { Integer.MAX_VALUE });
	}

	@Test
	public void removendoMembroValido() throws Exception {
		Membro membro = criarMassa(true);
		controller.remover(membro.getId());
	}

	@Test(expected = ValidationException.class)
	public void removendoMembroInvalido() throws Exception {
		controller.remover(Integer.MAX_VALUE);
	}

	private Membro criarMassa(boolean addToBd) {
		Membro membro = new Membro("Membro x", "x@gmail.com");
		if (addToBd) {
			dao.add(membro);
			membro.addDisponibilidade(Disponibilidade.SEGUNDA);
		}
		return membro;
	}

}
