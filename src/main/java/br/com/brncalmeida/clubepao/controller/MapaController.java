package br.com.brncalmeida.clubepao.controller;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Controller com a finalidade de apresentar ao client funcionalidade e possiveis repostas para as dúvidas.
 * 
 * @author bruno.almeida
 *
 */
@Resource
public class MapaController {

	private Result result;

	/**
	 * Construtor default
	 * 
	 * @param result
	 *            reponse controlado pelo vraptor
	 */
	public MapaController(Result result) {
		this.result = result;
	}

	/**
	 * pagina incial para a guia de mapa e faq do site.
	 */
	@Get
	@Path("/mapa")
	public void index() {
		result.include("pagina_ativa", "mapa");
	}
}
