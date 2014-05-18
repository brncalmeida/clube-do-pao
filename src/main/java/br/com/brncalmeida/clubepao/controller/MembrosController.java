package br.com.brncalmeida.clubepao.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import br.com.brncalmeida.clubepao.dao.MembroDao;
import br.com.brncalmeida.clubepao.model.Disponibilidade;
import br.com.brncalmeida.clubepao.model.Membro;
import br.com.brncalmeida.clubepao.services.ExcelFileService;
import br.com.brncalmeida.clubepao.services.ExcelFileService.ReportExcelService;
import br.com.brncalmeida.clubepao.utils.Util;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.ValidationMessage;
import br.com.caelum.vraptor.validator.Validations;

/**
 * Controller responsavel por manter o crud de membros
 * 
 * @author bruno.almeida
 *
 */
@Resource
public class MembrosController {

	private static final String PATH_PLANILHA_EXEMPLO = "planilha_exemplo_upload_membros.xlsx";
	private static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private final Result result;
	private final MembroDao dao;
	private Validator validator;
	private Localization localization;
	private ExcelFileService planilha;

	/**
	 * Controller default
	 * 
	 * @param result
	 *            response controlado pelo vraptor
	 * @param dao
	 *            instancia do dao membros
	 * @param localization
	 *            localizacao formatada pelo vraptor
	 * @param validator
	 *            validador controlado pelo vraptor
	 * @param planilha
	 *            serviço para leitura e manipulação de planilhas
	 */
	public MembrosController(Result result, MembroDao dao, Validator validator, Localization localization, ExcelFileService planilha) {
		this.result = result;
		this.dao = dao;
		this.validator = validator;
		this.localization = localization;
		this.planilha = planilha;
	}

	/**
	 * Método responsável por adicionar membro a partir da pagina
	 * 
	 * @param membro
	 *            membro a ser adicionado
	 * @param disponibilidades
	 *            disponibildades cadastradas para o membro
	 */

	@Path("/membros/add")
	@Post
	public void add(final Membro membro, Integer[] disponibilidades) {
		// validação do membro
		validarMembro(membro, disponibilidades, false);

		// Adicionando...
		dao.add(membro);

		// retornando informações
		String mensagem = Util.getMessage(localization, "membro.adicionado.sucesso", membro.getNome());
		result.include("notice", mensagem);
		result.forwardTo(this).index();
	}

	@Path("/membro/update")
	@Post
	@Consumes
	public void alterar(final Membro membro, Integer[] variasDisponibilidades) {
		// TODO arrumar funcao de alterar
		System.out.println(membro);
		System.out.println(variasDisponibilidades);
	}

	@Path("/membros/update")
	@Post
	@Consumes
	public void alterar(final List<Membro> membros) {
		// TODO arrumar funcao de alterar
		Membro membro;
		for (int i = 0; i < membros.size(); i++) {
			membro = membros.get(i);
			// disponibilidades = variasDisponibilidades.get(i);
			membro.removeAllDisponibilidades();
			// validarMembro(membro, disponibilidades, true);
			dao.update(membro);
			String mensagem = Util.getMessage(localization, "membro.alterado.sucesso", membro.getNome());
			result.include("notice", mensagem);
		}
		result.forwardTo(this).index();
	}

	@Path("/membros2/update")
	@Post
	@Consumes
	public void alterar(final Membro[] membros) {
		// TODO arrumar funcao de alterar
		System.out.println(membros);
		result.forwardTo(this).index();
	}

	/**
	 * Metodo responsável por deletar um unico membro
	 * 
	 * @param id
	 *            id do membro que devera ser deletado
	 */
	@Path("/membro/remove/{id}")
	@Get
	public void remover(long id) {

		// Validando membro
		Membro membro = dao.getById(id);
		if (membro == null) {
			validator.add(new ValidationMessage(Util.getMessage(localization, "membro.invalido"), "erro"));
		}
		validator.onErrorForwardTo(this).index();

		// Removendo...
		dao.remove(membro);

		// retornando informações
		String mensagem = Util.getMessage(localization, "membro.removido.sucesso", membro.getNome());
		result.include("notice", mensagem);
		result.include("down", true);
		result.forwardTo(this).index();
	}

	/**
	 * Metodo responsável por fazer o download da planilha exemplo
	 * 
	 * @return planilha exemplo
	 */
	@Get
	@Path("/membros/add/all/exemplo")
	public Download geraRelatorio() {
		File relatorio = planilha.getPlanilhaExemplo();
		return new FileDownload(relatorio, MIME_XLSX, PATH_PLANILHA_EXEMPLO, true);
	}

	/**
	 * Metodo responsavel por incluir na base membros de acordo com a planilha enviada
	 * 
	 * @param file
	 *            planilha do tipo excel (xlsx ou xls)
	 * @param sobrescrever
	 *            parametro que determina se os membros atuais da base devem ser excluidos antes de adicionar os novos ou não.
	 */
	@Post
	@Path("/membros/add/all")
	public void addAll(final UploadedFile file, boolean sobrescrever) {

		// validação
		ReportExcelService report = null;
		try {
			report = planilha.parse(file.getFile(), sobrescrever);
		} catch (Exception e) {
			validator.add(new ValidationMessage(Util.getMessage(localization, "planilha.invalida"), "erro"));
		}
		validator.onErrorForwardTo(this).index();

		// incluindo no response os erros dentro do parse (e não exception e/ou java.lang.erro)
		addErrors(result, report.getErros());

		// retornando informações
		String mensagem = Util.getMessage(localization, "membros.adicionados.sucesso", String.valueOf(report.getQtdMembrosIncluidos()));
		result.include("notice", mensagem);
		result.forwardTo(this).index();
	}

	/**
	 * metodo criado para tratar existencia do array de erros padrão do vraptor, caso exista, adicione os erros encontrados
	 * 
	 * @param result
	 *            response controlado pelo vraptor
	 * @param erros
	 *            lista de erros que deverão ser adicionados no result
	 */
	private void addErrors(Result result, List<String> erros) {
		// TODO estudar forma de tratar erros de maneira mais elegante
		List<Message> errorsNovo = new ArrayList<Message>();

		// buscando array de erros dentro do map result
		for (Entry<String, Object> a : result.included().entrySet()) {
			if (a.getKey().equalsIgnoreCase("errors")) {
				try {
					// adicionando erros atuais na lista temporaria
					errorsNovo.addAll(Arrays.asList((Message[]) a.getValue()));
				} catch (Exception e) {
					errorsNovo.add(new ValidationMessage(e.getMessage(), "error-system"));
				}
			}
		}

		// adicionando erros
		for (String erro : erros) {
			errorsNovo.add(new ValidationMessage(erro, "erro"));
		}

		// sobrescrevendo e/ou adicionando item no map result
		result.include("errors", errorsNovo.toArray(new Message[0]));
	}

	/**
	 * pagina incial para a guia de membros, este metodo também é utilizado ao final de todas as operaçoes deste controller.
	 */
	@Path("/")
	@Get
	public void index() {

		// listando membros
		result.include("membros", dao.listarTodos());
		result.include("pagina_ativa", "membro");

		// adicionando disponibilidades
		Disponibilidade[] disponibilidades = Disponibilidade.values();
		result.include("disponibilidades", disponibilidades);
	}

	/**
	 * metodo reponsável por validar o membro enviado junto com as disponibilidades cadastradas.
	 * 
	 * @param membro
	 *            membro a ser validado
	 * @param disponibilidades
	 *            disponibilidades a serem validadas
	 * @param alteracao
	 *            a validação veio do metodo de alteração?
	 */
	private void validarMembro(final Membro membro, Integer[] disponibilidades, final boolean alteracao) {
		Disponibilidade disponibilidadeReal = null;

		// validação disponibilidades, caso não tenha erro, adicione no membro
		if (disponibilidades != null && disponibilidades.length > 0) {
			for (Integer disponibilidade : disponibilidades) {
				try {
					disponibilidadeReal = Disponibilidade.getDisponibilidadeById(disponibilidade);
					membro.addDisponibilidade(disponibilidadeReal);
				} catch (IllegalArgumentException e) {
					validator.add(new ValidationMessage(Util.getMessage(localization, "disponibilidade.invalida"), "erro"));
				}
			}
		}

		// demais validações
		validator.checking(new Validations(localization.getBundle()) {
			{
				if (alteracao)
					that(dao.getById(membro.getId()) != null, "erro", "membro.invalido");
				that(Util.naoNuloComQtdMinimaCaracteres(membro.getNome(), 2), "erro", "nome.invalido");
				that(Util.naoNuloComQtdMinimaCaracteres(membro.getEmail(), 4), "erro", "email.invalido");
				that(membro.getDisponibilidades().size() > 0, "erro", "selecione.ao.menos.uma.disponibilidade");
			}
		});

		// caso tenha erro, forward..
		validator.onErrorForwardTo(this).index();
	}

}
