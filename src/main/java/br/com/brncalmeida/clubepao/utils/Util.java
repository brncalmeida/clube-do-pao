package br.com.brncalmeida.clubepao.utils;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.joda.LocalDateKitCalculatorsFactory;

import org.apache.commons.lang3.text.WordUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;

import br.com.caelum.vraptor.core.Localization;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;

/**
 * Classe com método uteis genericos
 * 
 * @author bruno.almeida
 *
 */
public class Util {

	/**
	 * Busca no bundle padrão
	 * 
	 * @param localization
	 *            bundle
	 * @param property
	 *            propriedade buscada
	 * @return mensagem localizada
	 */
	public static String getMessage(Localization localization, String property) {
		return localization.getBundle().getString(property);
	}

	/**
	 * Busca no bundle padrão
	 * 
	 * @param localization
	 *            bundle
	 * @param property
	 *            propriedade buscada
	 * @param complement
	 *            complemento da mensagem
	 * @return mensagem localizada
	 */
	public static String getMessage(Localization localization, String property, String complement) {
		return MessageFormat.format(getMessage(localization, property), complement);
	}

	/**
	 * Conveter a primeira letra de casa palavra
	 * 
	 * @param frase
	 *            frase a ser convertida
	 * @return frase convertida
	 */
	public static String primeiraLetraMaiuscula(String frase) {
		return WordUtils.capitalizeFully(frase);
	}

	/**
	 * Valida se o texto não está nulo e se tem a qtd minima de caracteres
	 * 
	 * @param texto
	 *            texto a ser validado
	 * @param qtdMinima
	 *            qtd minima de caracteres obrigatorias
	 * @return texto validado
	 */
	public static boolean naoNuloComQtdMinimaCaracteres(String texto, int qtdMinima) {
		return null != texto && texto.length() >= qtdMinima;
	}

	/**
	 * parse de texto para api jodatime
	 * 
	 * @param stringDate
	 *            strind que será parseada
	 * @param pattern
	 *            patterno utilizado no parse
	 * @return jodatime parseado
	 * @throws ParseException
	 */
	public static LocalDate stringToDate(String stringDate, String pattern) throws ParseException {
		LocalDate date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("pt_BR"));
		date = new LocalDate(sdf.parse(stringDate));
		return date;
	}

	/**
	 * busca os feriados
	 * 
	 * @return lista de feriados
	 */
	public static Set<LocalDate> buscarFeriados() {
		HolidayManager gerenciadorDeFeriados = HolidayManager.getInstance(de.jollyday.HolidayCalendar.BRAZIL);
		Set<Holiday> feriados = gerenciadorDeFeriados.getHolidays(new DateTime().getYear());
		Set<LocalDate> dataDosFeriados = new HashSet<LocalDate>();
		for (Holiday h : feriados) {
			dataDosFeriados.add(new LocalDate(h.getDate(), ISOChronology.getInstance()));
		}
		return dataDosFeriados;
	}

	/**
	 * busca os dias uteis de um range
	 * 
	 * @param dataInicial
	 *            data inicial do range
	 * @param dataFinal
	 *            data final do range
	 * @return dias uteis do range informado
	 */
	public static Set<LocalDate> buscarDiasUteis(LocalDate dataInicial, LocalDate dataFinal) {
		Set<LocalDate> diasUteis = new LinkedHashSet<LocalDate>();

		// retirando os feriados
		HolidayCalendar<LocalDate> calendarioDeFeriados = new DefaultHolidayCalendar<LocalDate>(buscarFeriados());
		LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("BR", calendarioDeFeriados);
		DateCalculator<LocalDate> calendario = LocalDateKitCalculatorsFactory.getDefaultInstance().getDateCalculator("BR", HolidayHandlerType.FORWARD);

		// calculando dia útil
		LocalDate dataTemporaria = dataInicial;
		while (dataTemporaria.isBefore(dataFinal)) {
			if (!calendario.isNonWorkingDay(dataTemporaria))
				diasUteis.add(new LocalDate(dataTemporaria));
			dataTemporaria = dataTemporaria.plusDays(1);
		}
		return diasUteis;
	}

	/**
	 * busca a meria aritmetica dos dados do array
	 * 
	 * @param array
	 *            array a ser calculado
	 * @return calculo
	 */
	public static double getMediaAritmetica(double array[]) {
		double total = 0;
		for (int counter = 0; counter < array.length; counter++)
			total += array[counter];
		return total / array.length;
	}

	/**
	 * soma dos dados do array
	 * 
	 * @param array
	 *            array a ser calculado
	 * @return calculo
	 */
	public static double getSomaDosElementos(double array[]) {
		double total = 0;
		for (int counter = 0; counter < array.length; counter++)
			total += array[counter];
		return total;
	}

	/**
	 * soma ao quadrado dos dados do array
	 * 
	 * @param array
	 *            array a ser calculado
	 * @return calculo
	 */
	public static double getSomaDosElementosAoQuadrado(double array[]) {
		double total = 0;
		for (int counter = 0; counter < array.length; counter++)
			total += Math.pow(array[counter], 2);
		return total;
	}

	/**
	 * variancia dos dados do array
	 * 
	 * @param array
	 *            array a ser calculado
	 * @return calculo
	 */
	public static double getVariancia(double array[]) {
		double p1 = 1 / Double.valueOf(array.length - 1);
		double p2 = getSomaDosElementosAoQuadrado(array) - (Math.pow(getSomaDosElementos(array), 2) / Double.valueOf(array.length));
		return p1 * p2;
	}

	/**
	 * desvio padrao dos dados do array
	 * 
	 * @param array
	 *            array a ser calculado
	 * @return calculo
	 */
	public static double getDesvioPadrao(double array[]) {
		return Math.sqrt(getVariancia(array));
	}
}
