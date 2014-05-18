package br.com.brncalmeida.clubepao.interceptor;

import java.util.ArrayList;
import java.util.List;

import br.com.brncalmeida.clubepao.controller.MembrosController;
import br.com.brncalmeida.clubepao.dao.MembroDao;
import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;

/**
 * Classe responsavel por adicionar massa durante tempo de desenvolvimento
 * 
 * @author bruno.almeida
 *
 */
@Intercepts
public class Massa implements Interceptor {

	private final MembroDao dao;

	public Massa(MembroDao dao) {
		this.dao = dao;
	}

	/**
	 * Metodo responsavel por gerar a massa e seguir com o fluxo normal do workflow do vraptor
	 */
	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		gerarMassa(6);
		stack.next(method, resourceInstance);
	}

	/**
	 * metodo que irá gerar n membros de acordo com o parametro enviado e inclui na base
	 * 
	 * @param qtd
	 *            quantidade de membros que será gerada
	 * @return
	 */
	public List<Membro> gerarMassa(int qtd) {
		List<Membro> massa = criarMassaMembros(qtd);
		this.dao.addAll(massa);
		return massa;
	}

	/**
	 * gera massa de somente 1 membro
	 * 
	 * @return
	 */
	public Membro gerarMassa() {
		return gerarMassa(1).get(0);
	}

	/**
	 * metodo que valida se a requisição deve ser interceptada ou não.
	 */
	@Override
	public boolean accepts(ResourceMethod method) {
		// TODO arrumar parametro e criar properties
		boolean mock = "true".equals(System.getProperty("mock"));

		// criterio para intercepção da chamada
		return mock && method.getResource().getType() == MembrosController.class && dao.listarTodos().size() == 0;
	}

	/**
	 * Criação da massa
	 * 
	 * @param qtd
	 *            qtd de membros que será gerado
	 * @return
	 */
	private static List<Membro> criarMassaMembros(int qtd) {
		List<Membro> membros = new ArrayList<Membro>(qtd);
		Membro membro = null;
		for (int i = 1; i < qtd; i++) {

			// criação do membro
			membro = new Membro("Membro " + i, "m" + i + "@gmail.com");

			// criação das disponibilidades de maneira randomica
			int qtdMaxima = (int) (Math.random() * 5);
			if (qtdMaxima == 0)
				qtdMaxima = 2;
			for (int j = 0; j < qtdMaxima; j++) {
				int indice = (int) (Math.random() * 6);
				if (indice == 0)
					indice = 1;
				if (indice == 6)
					indice--;
				membro.addDisponibilidade(Disponibilidade.getDisponibilidadeById(indice));
			}
			membros.add(membro);
		}
		return membros;
	}

}
