package br.com.brncalmeida.clubepao.controller;

import org.junit.Before;
import org.junit.Test;

import br.com.brncalmeida.clubepao.dao.DefaultMembroDao;
import br.com.brncalmeida.clubepao.dao.MembroDao;
import br.com.caelum.vraptor.validator.ValidationException;

public class ControleControllerTest extends GenericTest {

	private MembroDao dao;
	private ControleController controller;

	@Before
	public void setUp() throws Exception {
		dao = new DefaultMembroDao(getSession());
		controller = new ControleController(getMockResult(), dao, getLocalization(), getMockValidator());
	}

	@Test(expected = ValidationException.class)
	public void gerarRelatorioInvalido() {
		controller.gerar("");
	}

	@Test
	public void gerarRelatorioValido() {
		controller.gerar("2014-04-14");

	}
}
