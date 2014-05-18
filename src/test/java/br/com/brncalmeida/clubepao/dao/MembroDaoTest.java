package br.com.brncalmeida.clubepao.dao;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.brncalmeida.clubepao.controller.GenericTest;
import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;

public class MembroDaoTest extends GenericTest {
	private Session session;
	private MembroDao dao;

	@Test
	public void manutencaoMembro() throws Exception {
		// Criando massa
		Membro membro = new Membro("Membro 1", "membro1@gmail.com");
		membro.addDisponibilidade(Disponibilidade.SEGUNDA);
		membro.addDisponibilidade(Disponibilidade.QUARTA);
		dao.add(membro);
		assertThat(dao.procurarDisponibilidade(Disponibilidade.SEGUNDA), hasItem(membro));
		assertThat(dao.procurarDisponibilidade(Disponibilidade.QUARTA), hasItem(membro));
		assertThat(dao.listarTodos(), hasItem(membro));
		dao.remove(membro);
		assertThat(dao.listarTodos(), not(hasItem(membro)));
		assertThat(dao.procurarDisponibilidade(Disponibilidade.SEGUNDA), not(hasItem(membro)));
		assertThat(dao.procurarDisponibilidade(Disponibilidade.QUARTA), not(hasItem(membro)));
	}

	@Before
	public void setUp() throws Exception {
		dao = new DefaultMembroDao(getSession());
	}

	@After
	public void tearDown() throws Exception {
		if (session != null && session.getTransaction().isActive()) {
			session.getTransaction().rollback();
		}
	}

	public static void main(String[] args) throws Exception {
		MembroDaoTest test = new MembroDaoTest();

		test.setUp();
		test.manutencaoMembro();
		test.tearDown();
	}
}
