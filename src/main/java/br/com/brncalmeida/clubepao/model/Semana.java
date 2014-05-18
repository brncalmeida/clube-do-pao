package br.com.brncalmeida.clubepao.model;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.LocalDate;

import br.com.brncalmeida.clubepao.utils.Util;

/**
 * Classe responsável por interpretar um periodo de datas equivalente a uma semana
 * 
 * @author bruno.almeida
 * 
 */
public class Semana {

	private Map<LocalDate, Membro> dias;

	/**
	 * Construtor default, responsável por validar o range de datas enviadas e criar estrutura de dias úteis
	 * 
	 * @param dataInicial
	 *            data inicial do range
	 * @param dataFinal
	 *            data final do range
	 */
	public Semana(LocalDate dataInicial, LocalDate dataFinal) {

		// Validação
		if (dataInicial == null)
			throw new NullPointerException("Data inicial não pode ser nula");
		if (dataFinal == null)
			throw new NullPointerException("Data final não pode ser nula");

		// cria mapa com dias úteis
		Set<LocalDate> diasUteis = Util.buscarDiasUteis(dataInicial, dataFinal);

		// limpa valores do mapa
		for (LocalDate dia : diasUteis) {
			dias().put(dia, null);
		}
	}

	/**
	 * dias desta semana
	 * 
	 * @return mapa de dias desta semana
	 */
	public Map<LocalDate, Membro> getDias() {
		return dias();
	}

	/**
	 * limpa programação
	 */
	public void resetarProgramacaoMembros() {
		for (Map.Entry<LocalDate, Membro> dia : dias().entrySet()) {
			if (dia.getValue() != null)
				dia.setValue(null);
		}
	}

	/**
	 * validação se ja existe membro na semana
	 * 
	 * @param membro
	 *            membro a ser consultado
	 * @return true = "ja existe membro nesta semana", false = "não existe este membro nesta semana"
	 */
	public boolean existeMembro(Membro membro) {
		return dias().containsValue(membro);
	}

	/**
	 * tratamento para o mapa de dias
	 * 
	 * @return Mapa de dias concreto
	 */
	private Map<LocalDate, Membro> dias() {
		if (dias == null) {
			this.dias = new TreeMap<LocalDate, Membro>();
		}
		return dias;
	}

}
