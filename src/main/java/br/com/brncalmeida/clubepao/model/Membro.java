package br.com.brncalmeida.clubepao.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

/**
 * Classe responsável por representar entidade Membro
 * 
 * @author bruno.almeida
 * 
 */
@Entity
public class Membro {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	@Size(min = 2)
	private String nome;

	@NotEmpty
	@Size(min = 4)
	private String email;

	@ElementCollection
	@Enumerated(javax.persistence.EnumType.STRING)
	private Set<Disponibilidade> disponibilidades;

	@ElementCollection
	@Type(type = "org.joda.time.contrib.hibernate.PersistentLocalDate")
	private Set<LocalDate> diasProgramados;

	/**
	 * toString com nome do membro
	 */
	@Override
	public String toString() {
		return "Membro [nome=" + nome + "]";
	}

	/**
	 * hashCode padrao
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	/**
	 * Equals padrão
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membro other = (Membro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// ~-~-~-~-~-~-~-~ Construtores ~-~-~-~-~-~-~-~
	/**
	 * Construtor utilizado pelo hibernate
	 */
	@Deprecated
	public Membro() {
	}

	/**
	 * Construtor
	 * 
	 * @param nome
	 *            nome do membro
	 * @param email
	 *            e-mail do membro
	 */
	public Membro(String nome, String email) {
		this.nome = nome;
		this.email = email;
	}

	/**
	 * Construtor
	 * 
	 * @param nome
	 *            nome do membro
	 * @param email
	 *            e-mail do membro
	 * @param disponibilidades
	 *            disponibilidades do membro
	 */
	public Membro(String nome, String email, Set<Disponibilidade> disponibilidades) {
		this(nome, email);
		this.disponibilidades = disponibilidades;
	}

	// ~-~-~-~-~-~-~-~ Manutenções nas disponibilidades ~-~-~-~-~-~-~-~
	/**
	 * adiciona uma disponibilidade na lista do membro
	 * 
	 * @param disponibilidade
	 *            diponibilidade a ser incluida
	 */
	public void addDisponibilidade(Disponibilidade disponibilidade) {
		disponibilidades().add(disponibilidade);
	}

	/**
	 * adiciona o array de disponibilidades
	 * 
	 * @param disponibilidades
	 *            disponibilidades a serem incluidas
	 */
	public void addAllDisponibilidades(Disponibilidade[] disponibilidades) {
		if (disponibilidades == null) {
			throw new NullPointerException("Array de disponibilidade nulo, membro id=" + getId());
		}
		for (Disponibilidade disponibilidade : disponibilidades) {
			addDisponibilidade(disponibilidade);
		}
	}

	/**
	 * remover disponibilidade do membro
	 * 
	 * @param disponibilidade
	 *            disponibilidade a ser removida
	 */
	public void removeDisponibilidade(String disponibilidade) {
		disponibilidades().remove(disponibilidade);
	}

	/**
	 * remove todas as disponibilidades do membro
	 */
	public void removeAllDisponibilidades() {
		disponibilidades = null;
	}

	// ~-~-~-~-~-~-~-~ Manutenções nas programações ~-~-~-~-~-~-~-~
	/**
	 * adiciona uma programação ao membro
	 * 
	 * @param dia
	 *            dia a ser programado
	 */
	public void addDiaProgramado(LocalDate dia) {
		diasProgramados().add(dia);
	}

	/**
	 * remove uma programaçao do membro
	 * 
	 * @param dia
	 *            dia a ser removido
	 */
	protected void removeDiaProgramado(LocalDate dia) {
		diasProgramados().remove(dia);
	}

	// ~-~-~-~-~-~-~-~ Tratamento de coleções ~-~-~-~-~-~-~-~
	/**
	 * tratamento para coleção de disponibilidades
	 * 
	 * @return coleção imutavel
	 */
	public Set<Disponibilidade> getDisponibilidades() {
		return Collections.unmodifiableSet(disponibilidades());
	}

	/**
	 * tratamento para coleção de programações
	 * 
	 * @return coleção imutavel
	 */
	public Set<LocalDate> getDiasProgramados() {
		return Collections.unmodifiableSet(diasProgramados());
	}

	/**
	 * tratamento para coleção de programações
	 * 
	 * @return Set com datas
	 */
	private Set<LocalDate> diasProgramados() {
		if (diasProgramados == null) {
			this.diasProgramados = new TreeSet<LocalDate>();
		}
		return diasProgramados;
	}

	/**
	 * tratamento para coleção de disponibilidades
	 * 
	 * @return Set com disponibilidades
	 */
	private Set<Disponibilidade> disponibilidades() {
		if (disponibilidades == null) {
			this.disponibilidades = new TreeSet<Disponibilidade>(new OrdemById());

		}
		// TODO estudar forma para corrigir alteração do hibernate sem validar no fonte
		else if (disponibilidades instanceof PersistentSet) {
			Set<Disponibilidade> dispsTemporarias = disponibilidades;
			disponibilidades = new TreeSet<Disponibilidade>(new OrdemById());
			disponibilidades.addAll(dispsTemporarias);
		}
		return this.disponibilidades;
	}

	// ~-~-~-~-~-~-~-~ Gets and Sets~-~-~-~-~-~-~-~
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public String getEmail() {
		return email;
	}

}

class OrdemById implements Comparator<Disponibilidade> {
	public int compare(Disponibilidade a, Disponibilidade b) {
		return Integer.valueOf(a.getId()).compareTo(b.getId());
	}
}
