package br.com.brncalmeida.clubepao.model;

import br.com.brncalmeida.clubepao.utils.Util;

/**
 * Enum responsavel por centralizar dias da semana
 * 
 * @author bruno.almeida
 * 
 */
public enum Disponibilidade {

	// TODO arrumar internacionalização das disponibilidades (na tela de cadastro de membros não está pegando item dinamico)
	SEGUNDA(1, "segunda"), TERCA(2, "terca"), QUARTA(3, "quarta"), QUINTA(4, "quinta"), SEXTA(5, "sexta");

	private int id;
	private String message;

	/**
	 * Construtor padrão
	 * 
	 * @param id
	 *            id da disponibilidades
	 * @param message
	 *            property key da disponibilidade
	 */
	private Disponibilidade(int id, String message) {
		this.id = id;
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return Util.primeiraLetraMaiuscula(super.toString().substring(0, 3));
	}

	/**
	 * traduz a partir dos ids das disponibilidades, disponibilidades padronizadas
	 * 
	 * @param ids
	 *            ids a serem buscadas
	 * @return disponibilidades encontradas
	 */
	public static Disponibilidade[] getDisponibilidadesById(Integer[] ids) {
		Disponibilidade[] disponibilidades = new Disponibilidade[ids.length];
		int i = 0;
		for (Integer id : ids) {
			disponibilidades[i] = getDisponibilidadeById(id);
		}
		return disponibilidades;
	}

	/**
	 * traduz a partir do id da disponibilidade, disponibilidade padronizada
	 * 
	 * @param id
	 *            id a ser buscada
	 * @return disponibilidade encontrada
	 */
	public static Disponibilidade getDisponibilidadeById(int id) {
		for (Disponibilidade dispo : Disponibilidade.values()) {
			if (dispo.getId() == id) {
				return dispo;
			}
		}
		throw new IllegalArgumentException("Disponibilidade não encontrada, id=" + id);
	}

}
