package br.com.brncalmeida.clubepao.model;

import java.util.List;
import java.util.Set;

/**
 * Classe reponsável por padronizar e transportar sugestões para alocações entre membros.
 * 
 * @author bruno.almeida
 * 
 */
public class SugestaoTrocaDisponibilidades {

	private List<Membro> membrosASeremRemovidos;
	private List<Membro> membrosASeremColocados;
	private Set<Disponibilidade> disponibilidadesSobrecarregadas;
	private Set<Disponibilidade> disponibilidadesAusentes;

	/**
	 * Construtor default
	 * 
	 * @param membrosASeremRemovidos
	 *            membros que têm muitas programações
	 * @param membrosASeremColocados
	 *            membros que têm poucas programações
	 * @param disponibilidadesSobrecarregadas
	 *            disponibilidades que ocorrem muitas vezes
	 * @param disponibilidadesAusentes
	 *            disponibilidades que precisam ser adicionadas.
	 */
	public SugestaoTrocaDisponibilidades(List<Membro> membrosASeremRemovidos, List<Membro> membrosASeremColocados, Set<Disponibilidade> disponibilidadesSobrecarregadas,
			Set<Disponibilidade> disponibilidadesAusentes) {
		this.membrosASeremRemovidos = membrosASeremRemovidos;
		this.membrosASeremColocados = membrosASeremColocados;
		this.disponibilidadesSobrecarregadas = disponibilidadesSobrecarregadas;
		this.disponibilidadesAusentes = disponibilidadesAusentes;
	}

	/**
	 * Concatena disponibilidades sobrecarregadas em uma unica string
	 * 
	 * @return disponibilidades sobrecarregadas concatenadas
	 */
	public String getDisponibilidadesSobrecarregadas() {
		StringBuilder sb = new StringBuilder();
		for (Disponibilidade disponibilidade : disponibilidadesSobrecarregadas) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(disponibilidade);
		}
		return sb.toString();
	}

	/**
	 * Concatena disponibilidades ausentes em uma unica string
	 * 
	 * @return disponibilidades ausentes concatenadas
	 */
	public String getDisponibilidadesAusentes() {
		StringBuilder sb = new StringBuilder();
		for (Disponibilidade disponibilidade : disponibilidadesAusentes) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(disponibilidade);
		}
		return sb.toString();
	}

	/**
	 * Concatena membros que têm muitas programações efetivadas
	 * 
	 * @return membros com muitas programações concatenados
	 */
	public String getMembrosASeremRemovidos() {
		StringBuilder sb = new StringBuilder();
		for (Membro membro : membrosASeremRemovidos) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(membro.getNome());
		}
		return sb.toString();
	}

	/**
	 * Concatena membros que têm poucas programações efetivadas
	 * 
	 * @return membros com poucas programações concatenados
	 */
	public String getMembrosASeremColocados() {
		StringBuilder sb = new StringBuilder();
		for (Membro membro : membrosASeremColocados) {
			if (sb.length() > 0) {
				sb.append("/");
			}
			sb.append(membro.getNome());
		}
		return sb.toString();
	}

}
