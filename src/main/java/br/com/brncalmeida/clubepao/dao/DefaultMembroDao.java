package br.com.brncalmeida.clubepao.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;
import br.com.caelum.vraptor.ioc.Component;

/**
 * Implementação Default para o Dao MembroDao.
 * 
 * @author Bruno Almeida
 */
@Component
public class DefaultMembroDao implements MembroDao {

	private final Session session;

	public DefaultMembroDao(Session session) {
		this.session = session;
	}

	public void add(Membro membro) {
		session.save(membro);
	}

	public void addAll(List<Membro> membros) {
		for (Membro membro : membros) {
			add(membro);
		}
	}

	public void remove(Membro membro) {
		session.delete(membro);
	}

	public void removeAll(List<Membro> membros) {
		for (Membro membro : membros) {
			remove(membro);
		}
	}

	public void refresh(Membro membro) {
		session.refresh(membro);
	}

	public void update(Membro membro) {
		session.update(membro);
	}

	public Membro getById(long id) {
		return (Membro) session.createCriteria(Membro.class).add(Restrictions.idEq(id)).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<Membro> procurarDisponibilidade(Disponibilidade disponibilidade) {
		// TODO utilizar criteria para essa query
		Query query = session.createQuery("select m from Membro m inner join m.disponibilidades d where d='" + disponibilidade.name() + "'");
		return query.list();
	}

	@SuppressWarnings("unchecked")
	public List<Membro> listarTodos() {
		return session.createCriteria(Membro.class).list();
	}

}
