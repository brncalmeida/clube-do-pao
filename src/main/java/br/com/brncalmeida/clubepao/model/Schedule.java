package br.com.brncalmeida.clubepao.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.joda.time.LocalDate;

import br.com.brncalmeida.clubepao.utils.Util;

/**
 * Classe responsável por receber um range de datas e uma lista de membros. A partir destas campos, irá intercalar a programação entre os membros.
 * 
 * @author bruno.almeida
 * 
 */
public class Schedule {
	private LocalDate dataInicial;
	private LocalDate dataFinal;
	private List<Membro> membros;
	private Map<Date, String> programacaoPorMembro;
	private Map<Integer, String> programacaoSobrecarga;
	private SugestaoTrocaDisponibilidades sugestao;

	/**
	 * Construtor default
	 * 
	 * @param dataInicial
	 *            data inicial do range que deverá compor a programação
	 * @param dataFinal
	 *            data final do range que deverá compor a programação
	 * @param membros
	 *            membros que irão compor a programação
	 */
	public Schedule(LocalDate dataInicial, LocalDate dataFinal, List<Membro> membros) {
		if (dataInicial == null)
			throw new NullPointerException("Campo data inicial invalido");
		else if (dataFinal == null)
			throw new NullPointerException("campo data final invalido");
		else if (membros == null || membros.size() == 0)
			throw new NullPointerException("campo membros invalido");
		else {
			this.dataInicial = dataInicial;
			this.dataFinal = dataFinal;
			this.membros = membros;
			this.programacaoPorMembro = processarAlocacaoMembro();
			this.programacaoSobrecarga = processarAlocacaoSobrecarga();
			this.sugestao = criarSugestao();
		}
	}

	/**
	 * A sobrecarga da programação equivale ao resumo de programações. A Key equivale a qtd de programações dos membros, já o value equivale a lista concatenada de
	 * membros.
	 * 
	 * @return map(key=qtd programações / value=membros)
	 */
	public Map<Integer, String> getProgramacaoSobrecarga() {
		return Collections.unmodifiableMap(programacaoSobrecarga);
	}

	/**
	 * Programação completa dos dias úteis com membro disponivel para o dia.
	 * 
	 * @return map(key=dia programação / value=membro correspondente)
	 */
	public Map<Date, String> getProgramacaoPorMembro() {
		return Collections.unmodifiableMap(programacaoPorMembro);
	}

	/**
	 * --Deprecated: Em desenvolvimento-- Método responsável por apresentar sugestão de troca de disponibilidades entre membros.
	 * 
	 * @return Sugestão completa.
	 */
	@Deprecated
	public SugestaoTrocaDisponibilidades getSugestao() {
		return sugestao;
	}

	// ~-~-~-~-~-~-~-~-~-~-~-~-~ Métodos privados ~-~-~-~-~-~-~-~-~-~-~-~-~

	/**
	 * Método responsável por apresentar sugestão de troca de disponibilidades entre membros. Em desenvolvimento
	 * 
	 * @return Sugestão completa.
	 */
	@Deprecated
	private SugestaoTrocaDisponibilidades criarSugestao() {
		double[] qtds = new double[membros.size()];
		int i = 0;

		// criando mapa de qtd de programações para um determinado dia
		for (Membro membro : membros) {
			qtds[i++] = membro.getDiasProgramados().size();
		}

		double mediaPaes = Util.getMediaAritmetica(qtds);
		double desvioPadraoPaes = Util.getDesvioPadrao(qtds);
		int rangeInicialOk = (int) (mediaPaes - desvioPadraoPaes);
		int rangeFinalOk = (int) (mediaPaes + desvioPadraoPaes);
		if (rangeInicialOk == 0)
			rangeInicialOk = 1;

		List<Membro> membrosAbaixoRangeOk = new ArrayList<Membro>();
		List<Membro> membrosAcimaRangeOk = new ArrayList<Membro>();
		Set<Disponibilidade> disponibilidades = new LinkedHashSet<Disponibilidade>();
		Set<Disponibilidade> disponibilidadesAusentes = new LinkedHashSet<Disponibilidade>();

		// Mapeando membros com qtd de programações abaixo da média e membros com qtd de programações acima da média.
		for (Membro membro : membros) {
			int qtd = membro.getDiasProgramados().size();
			if (qtd <= rangeInicialOk) {
				membrosAbaixoRangeOk.add(membro);
			} else if (qtd >= rangeFinalOk) {
				membrosAcimaRangeOk.add(membro);
				disponibilidades.add(getSobrecargaMembro(membro));
			}
		}

		// Criando mapa dia SEM_PAO
		for (Entry<Date, String> dia : programacaoPorMembro.entrySet()) {
			// se dia está sem membro
			if (dia.getValue() == null) {
				int diaSemana = new LocalDate(dia.getKey()).getDayOfWeek();
				disponibilidadesAusentes.add(Disponibilidade.getDisponibilidadeById(diaSemana));
			}
		}

		Collections.sort(membrosAbaixoRangeOk, new MembrosMenosSobrecarregadosComparator());
		Collections.sort(membrosAcimaRangeOk, new MembrosSobrecarregadosComparator());

		// criando DTO
		SugestaoTrocaDisponibilidades sugestao = new SugestaoTrocaDisponibilidades(membrosAcimaRangeOk, membrosAbaixoRangeOk, disponibilidades, disponibilidadesAusentes);

		return sugestao;
	}

	/**
	 * Método resposável por buscar se há sobrecarga de programações no membro
	 * 
	 * @param membro
	 * @return Disponibilidade com maior indice de sobrecarga
	 */
	private Disponibilidade getSobrecargaMembro(Membro membro) {
		int[] diasSemana = new int[5];

		// levantamento dos dias utilizados
		for (LocalDate data : membro.getDiasProgramados()) {
			int dia = data.dayOfWeek().get();
			diasSemana[dia - 1]++;
		}

		// definição do dia com maior qtd de programações
		int maiorDisponibilidade = 0;
		int indice = 0;
		for (int i = 0; i < diasSemana.length; i++) {
			if (diasSemana[i] > maiorDisponibilidade) {
				indice = i;
				maiorDisponibilidade = diasSemana[i];
			}
		}
		return Disponibilidade.getDisponibilidadeById(indice + 1);
	}

	/**
	 * A sobrecarga da programação equivale ao resumo de programações. A Key equivale a qtd de programações dos membros, já o value equivale a lista concatenada de
	 * membros.
	 * 
	 * @return map(key=qtd programações / value=membros)
	 */
	private Map<Date, String> processarAlocacaoMembro() {
		Periodo programacao = new Periodo(dataInicial, dataFinal);
		List<Membro> membrosDisponiveis;
		Disponibilidade disponibilidadeProcurada;

		// Calculando programação do período avaliado x membros com disponibilidade
		for (Semana semana : programacao.getSemanas()) {

			// iterando os dias da semana
			for (Entry<LocalDate, Membro> dia : semana.getDias().entrySet()) {
				int diaSemana = dia.getKey().dayOfWeek().get();
				disponibilidadeProcurada = Disponibilidade.getDisponibilidadeById(diaSemana);
				membrosDisponiveis = procurarDisponibilidade(disponibilidadeProcurada, membros);

				// ordenando: 1o = quem tiver a menor qtd de programações / 2o = quem tiver menor disponibilidade.
				Collections.sort(membrosDisponiveis, new MembrosMenosSobrecarregadosComparator());

				// iterando membros disponiveis para o dia
				for (Membro membroDisponivel : membrosDisponiveis) {
					if (!semana.existeMembro(membroDisponivel)) {
						membroDisponivel.addDiaProgramado(dia.getKey());
						dia.setValue(membroDisponivel);
						break;
					}
				}
			}
		}
		return programacao.extrairCronograma();
	}

	/**
	 * Programação completa dos dias úteis com membro disponivel para o dia.
	 * 
	 * @return map(key=dia programação / value=membro correspondente)
	 */
	private Map<Integer, String> processarAlocacaoSobrecarga() {
		Map<Integer, StringBuilder> mapaProgramacao = new TreeMap<Integer, StringBuilder>();
		Map<Integer, String> mapaProgramacaoRetorno = new TreeMap<Integer, String>();
		int qtdProgramacoesMembro;
		StringBuilder builder;

		// iterar todos os membros, classificando pela qtd de programações
		for (Membro membro : membros) {
			qtdProgramacoesMembro = membro.getDiasProgramados().size();
			if ((builder = mapaProgramacao.get(qtdProgramacoesMembro)) == null) {
				builder = new StringBuilder();
				mapaProgramacao.put(qtdProgramacoesMembro, builder);
			} else
				builder.append(", ");
			builder.append(membro.getNome());
		}

		// Criando mapa de sobrecarga de programações
		for (Entry<Integer, StringBuilder> item : mapaProgramacao.entrySet()) {
			mapaProgramacaoRetorno.put(item.getKey(), item.getValue().toString());
		}
		return mapaProgramacaoRetorno;
	}

	/**
	 * Método reponsável por validar o membro tem a disponibilidade informada
	 * 
	 * @param disponibilidade
	 *            disponibilidade buscada
	 * @param membros
	 *            Lista de membros avaliados
	 * @return Membros que contém a disponibilidade informada
	 */
	private List<Membro> procurarDisponibilidade(Disponibilidade disponibilidade, List<Membro> membros) {
		List<Membro> membrosComDisponibilidade = new ArrayList<Membro>();
		for (Membro membro : membros) {
			if (membro.getDisponibilidades().contains(disponibilidade)) {
				membrosComDisponibilidade.add(membro);
			}
		}
		return membrosComDisponibilidade;
	}

}

/**
 * Classe responsável por criar regra de ordenação: 1o avaliar membro com menor programações efetivadas, 2o avaliar membro com menor qtd de disponibilidades e 3o menor
 * id.
 * 
 * @author bruno.almeida
 * 
 */
class MembrosMenosSobrecarregadosComparator implements Comparator<Membro> {
	@Override
	public int compare(Membro primeiroMembro, Membro segundoMembro) {
		int dias = primeiroMembro.getDiasProgramados().size();
		int diasSegundoMembro = segundoMembro.getDiasProgramados().size();
		int comparacaoDiasProgramados = new Integer(dias).compareTo(diasSegundoMembro);
		if (comparacaoDiasProgramados != 0)
			return comparacaoDiasProgramados;

		int disponibilidade = primeiroMembro.getDisponibilidades().size();
		int disponibilidadeSegundoMembro = segundoMembro.getDisponibilidades().size();

		int comparacaoDisponibilidade = new Integer(disponibilidade).compareTo(disponibilidadeSegundoMembro);
		if (comparacaoDisponibilidade != 0)
			return comparacaoDisponibilidade;

		return Long.valueOf(primeiroMembro.getId()).compareTo(segundoMembro.getId());
	}
}

/**
 * Classe responsável por criar regra de ordenação: 1o avaliar membro com maior qtd de programações efetivadas, 2o avaliar membro com maior qtd de disponibilidades e 3o
 * menor id.
 * 
 * @author bruno.almeida
 * 
 */
class MembrosSobrecarregadosComparator implements Comparator<Membro> {

	@Override
	// TODO confirmar ordem
	public int compare(Membro primeiroMembro, Membro segundoMembro) {
		int dias = primeiroMembro.getDiasProgramados().size();
		int diasSegundoMembro = segundoMembro.getDiasProgramados().size();
		int comparacaoDiasProgramados = new Integer(diasSegundoMembro).compareTo(dias);
		if (comparacaoDiasProgramados != 0)
			return comparacaoDiasProgramados;

		int disponibilidade = primeiroMembro.getDisponibilidades().size();
		int disponibilidadeSegundoMembro = segundoMembro.getDisponibilidades().size();

		int comparacaoDisponibilidade = new Integer(disponibilidadeSegundoMembro).compareTo(disponibilidade);
		if (comparacaoDisponibilidade != 0)
			return comparacaoDisponibilidade;

		return Long.valueOf(primeiroMembro.getId()).compareTo(segundoMembro.getId());
	}

}