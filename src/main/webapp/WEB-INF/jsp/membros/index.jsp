<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<tiles:insertTemplate template="/WEB-INF/jsp/template.jsp">
	<tiles:putAttribute name="body">
		<style>
@media ( max-width : 480px) {
	.removido-responsivo {
		display: none;
	}
}
</style>

		<script>
			function alterar() {
				var lista_erros = new Array();
				$(".registro_membro").each(function() {
					if ($(this).find("input:checked").length == 0) {
						lista_erros.push($(this).find('.nome_membro').text());
					}
				});

				if (lista_erros.length > 0) {
					if (lista_erros.length > 1) {
						alert('<fmt:message key="selecione.ao.menos.uma.disponibilidade"/> <fmt:message key="para.os.membros"/>: ' + lista_erros);
					} else {
						alert('<fmt:message key="selecione.ao.menos.uma.disponibilidade"/> <fmt:message key="para.o.membro"/>: ' + lista_erros);
					}
					return false;
				}
				alert("alterando... ");
				//var json = {}; var membros = new Array(); var map = new Object(); var checks = $("input[name=variasDisponibilidades]:checkbox:checked").map(function() { return $(this).val(); }).each(function() { var campo = this.split("_"); if (map[campo[1]] == undefined) { map[campo[1]] = new Array(); } map[campo[1]].push(campo[0]); }); $.each(map, function(key, value) { membros.push({ 'membro.id' : parseInt(key) }); }); $.ajax({ data : membros, type : 'POST', dataType : 'json', url : "<c:url value="/membros/update" />", success : function(result) { alert(result.myMsg); }, error : function(result) { alert("error"); } });
			}

			// Função para habilitar os botões e colunas de edição
			function habilitar_coluna_edicao(bool) {
				if (bool) {
					if ($(".registro_membro").length > 0) {
						$('.edicao').each(function() {
							$(this).css('display', '');
						});
						$('.apoio_edicao').each(function() {
							$(this).css('display', 'none');
						});
						$('.remover-responsivo').addClass('removido-responsivo');
					}
				} else {
					$('.edicao').each(function() {
						$(this).css('display', 'none');
					});
					$('.apoio_edicao').each(function() {
						$(this).css('display', '');
					})
					$('.remover-responsivo').removeClass('removido-responsivo');
				}
			}
			$(document).ready(function() {
				<c:if test="${not empty down}">
				window.scrollTo(0, document.body.scrollHeight);
				habilitar_coluna_edicao(true);
				</c:if>

				<c:if test="${empty down}">
				habilitar_coluna_edicao(false);
				</c:if>

				$("#botao_add_um").click(function() {
					if ($('input[name=disponibilidades]:checked').length == 0) {
						alert('<fmt:message key="selecione.ao.menos.uma.disponibilidade"/>');
						return false;
					}
				});

				$("#botao_update").click(function() {
					alterar();
				});

				$("#botao_libera_edicao").click(function() {
					habilitar_coluna_edicao(true);
				});

				$("#botao_cancel").click(function() {
					habilitar_coluna_edicao(false);
				});

				$("#botao_upload").click(function() {
					if ($('input[name=file]').val().length > 0) {
						if ($(".registro_membro").length > 0 && $('input[name=sobrescrever]').is(':checked')) {
							return window.confirm('<fmt:message key="confirma.sobrescrever.membros.atuais"/>');
						}
					} else {
						alert('<fmt:message key="nenhum.arquivo.selecionado"/>');
						return false;
					}
				});

				$(".link_remover_membro").click(function() {
					return window.confirm('<fmt:message key="confirma.deletar.membro"/>');
				});

			});
		</script>

		<!--  painel de cadastro -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<fmt:message key="cadastro.novos.membros" />
			</div>

			<div class="panel-body">
				<div class="col-sm-6">
					<!--  painel cadastro unico -->
					<div class="panel panel-info">
						<div class="panel-heading">
							<fmt:message key="utilize.form" />
						</div>
						<div class="panel-body">
							<form id="cadastro_membros" action="<c:url value="/membros/add" />" method="post" class="buttonForm">
								<div class="row">
									<div class="form-group col-sm-6">
										<div class="input-group">
											<span class="input-group-addon"> <span class="glyphicon glyphicon-user"></span>
											</span> <input type="text" name="membro.nome" maxlength="20" class="form-control" placeholder="<fmt:message key="placeholder.nome.membro" />"
												required />
										</div>
									</div>
									<div class="form-group col-sm-6">
										<div class="input-group">
											<span class="input-group-addon">@</span> <input type="email" name="membro.email" maxlength="20" class="form-control"
												placeholder="<fmt:message key="email" />" required />
										</div>
									</div>
								</div>
								<div class="row">
									<div class="form-group col-sm-12">
										<c:forEach var="disponibilidade" items="${disponibilidades}">
											<label class="checkbox-inline"> <input type="checkbox" name="disponibilidades" value="${disponibilidade.id }"> <fmt:message
													key="${disponibilidade.message }" />
											</label>
										</c:forEach>
										<div class="error">
											<span></span>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<button type="submit" id="botao_add_um" class="btn btn-default navbar-right" title="<fmt:message key="cadastrar.membro" />">
											<span class="glyphicon glyphicon-cloud-upload"></span>
											<fmt:message key="cadastrar.membro" />
										</button>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<!--  painel cadastro massivo -->
				<div class="col-sm-6">
					<div class="panel panel-info">
						<div class="panel-heading">
							<fmt:message key="utilize.excel" />
						</div>

						<div class="panel-body">
							<form id="fileupload" action="<c:url value="/membros/add/all"/>" enctype="multipart/form-data" method="post" class="buttonForm">
								<div class="row">
									<div class="form-group col-sm-10">
										<input id="file" type="file" name="file" title="<fmt:message key="upload.planilha" />" required />
									</div>
								</div>
								<div class="row">
									<div class="form-group col-sm-12">
										<label class="checkbox-inline"><input type="checkbox" name="sobrescrever" class=""> <fmt:message
												key="sobrescrever.membros.atuais" /> </label><br />
										<!-- baixar planilha exemplo -->
										<a href="#" onclick="javascript:location.href='<c:url value="/membros/add/all/exemplo"/>'"
											title="<fmt:message
												key="planilha.exemplo" />"> <span class="glyphicon glyphicon-cloud-download espaco_lateral"
											title="<fmt:message key="planilha.exemplo" />"></span> <fmt:message key="planilha.exemplo" /></a>
										<!-- baixar planilha com membros atuais -->
										<a href="#" onclick="javascript:location.href='<c:url value="/membros/extract/all"/>'"
											title="<fmt:message
												key="planilha.completa" />"> <span class="glyphicon glyphicon-cloud-download espaco_lateral"
											title="<fmt:message key="planilha.completa" />"></span> <fmt:message key="planilha.completa" /></a>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-12">
										<button id="botao_upload" class="btn btn-default navbar-right" title="<fmt:message key="cadastrar.membro.massivo" />" type='submit'>
											<span class="glyphicon glyphicon-cloud-upload"></span>
											<fmt:message key="cadastrar.membro.massivo" />
										</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- fim campos upload massivo -->
				</div>
			</div>
		</div>



		<div class="panel panel-default print-area">
			<div class="panel-heading">
				<fmt:message key="descricao.table.membros" />
			</div>

			<table class="table table-striped table-hover table-condensed" id="tabela_membros">
				<thead>
					<tr>
						<th><fmt:message key="nome" /></th>
						<th class="remover-responsivo"><fmt:message key="email" /></th>
						<th class="apoio_edicao"><fmt:message key="disponibilidade" /></th>
						<th class="remove-print-area edicao"><fmt:message key="disponibilidade" /></th>
						<th class="remove-print-area edicao"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="membro" items="${membros}">
						<tr class="registro_membro">
							<td><div class="nome_membro">${membro.nome}</div></td>
							<td class="remover-responsivo">${membro.email}</td>
							<td class="apoio_edicao"><c:forEach var="disp" items="${membro.disponibilidades}">
						${disp.toString()} 
					</c:forEach></td>
							<td class="remove-print-area edicao alteracao"><input type="hidden" name="membros.id" value="${membro.id}" /> <small> <c:forEach
										var="disponibilidade" items="${disponibilidades}">
										<label class="checkbox-inline"> <input type="checkbox" name="variasDisponibilidades"
											title="<fmt:message key="${disponibilidade.message }_min" /> - ${membro.nome}" value="${disponibilidade.id }_${membro.id}"
											<c:forEach var="dispoMembro" items="${membro.disponibilidades}">
											  <c:if test="${disponibilidade eq dispoMembro}">
												checked
											  </c:if>
											</c:forEach>>
											<fmt:message key="${disponibilidade.message }_min" />
										</label>
									</c:forEach></small></td>
							<td class="remove-print-area edicao"><a class="link_remover_membro" href="<c:url value="/membro/remove/${membro.id}" />"
								title="<fmt:message key="remover" />"><span class="glyphicon glyphicon-trash"></span></a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="panel-footer remove-print-area">
				<!--  botão de impressão -->
				<a id="botao_impressao"> <span class="glyphicon glyphicon-print btn-lg" title="<fmt:message key="imprimir" />"></span></a>

				<div class="pull-right">
					<!--  orientação para editar membros -->
					<span class="apoio_edicao"> <fmt:message key="editar.membros" /> <span class="glyphicon glyphicon-arrow-right btn-lg"></span>
					</span>
					<!-- botão para habilitar funções de edição -->
					<a id="botao_libera_edicao" class="apoio_edicao" title="<fmt:message key="alterar" />"> <span class="glyphicon glyphicon-pencil btn-lg"></span></a>

					<!-- botão para cancelar funções de edição -->
					<a id="botao_cancel" class="edicao" title="<fmt:message key="cancelar" />"> <span class="glyphicon glyphicon-remove btn-lg"></span></a>

					<!-- botão submeter alterações -->
					<a id="botao_update" class="edicao" title="<fmt:message key="alterar" />"> <span class="glyphicon glyphicon-ok btn-lg"></span></a>
				</div>
			</div>
		</div>
	</tiles:putAttribute>
</tiles:insertTemplate>