<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:useBean id="now" class="java.util.Date" />

<tiles:insertTemplate template="/WEB-INF/jsp/template.jsp">
	<tiles:putAttribute name="body">
		<script>
			$(document).ready(function() {
				$("#link_planilha").click(function() {
					location.href = '<c:url value="/membros/add/all/exemplo"/>';
				});

			});
		</script>


		<!-- Informativos sobre o cadastro de membros -->
		<h3>
			<fmt:message key="menu.cadastro.membro" />
		</h3>
		<h4>
			<fmt:message key="utilize.form" />
		</h4>
		<p>
			<fmt:message key="sobre.inclusao.membro" />
		</p>
		<hr />
		<h4>
			<fmt:message key="utilize.excel" />
		</h4>
		<p>
			<fmt:message key="sobre.inclusao.massiva.part1" />
			<a id="link_planilha" title="<fmt:message key="planilha.exemplo" />"> <fmt:message key="aqui" /></a>.<br />
			<fmt:message key="sobre.inclusao.massiva.part2" />
		</p>

		<table class="table table-striped table-hover table-condensed">
			<thead>
				<tr>
					<th>.</th>
					<th>A</th>
					<th>B</th>
					<th>C</th>
					<th>D</th>
					<th>E</th>
					<th>F</th>
					<th>G</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>1</th>
					<td><fmt:message key="nome" /></td>
					<td><fmt:message key="email" /></td>
					<td><fmt:message key="segunda_min" /></td>
					<td><fmt:message key="terca_min" /></td>
					<td><fmt:message key="quarta_min" /></td>
					<td><fmt:message key="quinta_min" /></td>
					<td><fmt:message key="sexta_min" /></td>
				</tr>
				<tr>
					<th>2</th>
					<td>[<fmt:message key="nome.primeiro.membro" />]
					</td>
					<td>[<fmt:message key="email.primeiro.membro" />]
					</td>
					<td>x</td>
					<td></td>
					<td>x</td>
					<td></td>
					<td></td>
				</tr>
				<tr>
					<th>3
					<td>[<fmt:message key="nome.segundo.membro" />]
					</td>
					<td>[<fmt:message key="email.segundo.membro" />]
					</td>
					<td></td>
					<td>x</td>
					<td></td>
					<td></td>
					<td>x</td>
				</tr>
				<tr>
					<th>:</th>
					<td>:</td>
					<td>:</td>
					<td>:</td>
					<td>:</td>
					<td>:</td>
					<td>:</td>
					<td>:</td>
				</tr>
				<tr>
					<th>20</th>
					<td>[<fmt:message key="nome.ultimo.membro" />]
					</td>
					<td>[<fmt:message key="email.ultimo.membro" />]
					</td>
					<td>x</td>
					<td>x</td>
					<td></td>
					<td>x</td>
					<td>x</td>
				</tr>
			</tbody>
		</table>

		<p>
			<fmt:message key="sobre.inclusao.massiva.planilha" />
		</p>
		<p>
			<fmt:message key="sobre.inclusao.massiva.cadastro" />
		</p>
		<p>
			<fmt:message key="sobre.inclusao.maassiva.problemas" />
		</p>

		<hr />

		<h4>
			<fmt:message key="alterando.membros" />
		</h4>
		<p>
			<fmt:message key="sobre.alterar.membro.part1" />
			<a class="btn-lg"><span class="glyphicon glyphicon-pencil"></span></a>.<br />

			<fmt:message key="sobre.alterar.membro.part2" />

			<a class="btn-lg"><span class="glyphicon glyphicon-ok"></span></a>.<br />

			<fmt:message key="sobre.alterar.membro.part3" />

			<a class="btn-lg "><span class="glyphicon glyphicon-trash"></span></a>.

		</p>

		<!-- Informativo sobre criação da programação -->

		<hr />
		<hr />
		<h3>
			<fmt:message key="menu.gerar.controle" />
		</h3>
		<h4>
			<fmt:message key="gerando.programacao" />
		</h4>
		<p>
			<fmt:message key="sobre.gerando.programacao.part1" />
		</p>

		<p>
			<fmt:message key="sobre.gerando.programacao.part2" />
			<span class="sem_pao" style="color: red; font-weight: bold;"> <fmt:message key="error.sem.pao" />


			</span> <br />
		</p>

		<table class="table table-striped table-hover table-condensed">
			<thead>
				<tr>
					<th><fmt:message key="data" /></th>
					<th><fmt:message key="dia" /></th>
					<th><fmt:message key="membro" /></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>19/05/2014</td>
					<td>Seg</td>
					<td><span style="color: red; font-weight: bold;"><fmt:message key="error.sem.pao" /></span></td>
				</tr>
				<tr>
					<td>20/05/2014</td>
					<td>Ter</td>
					<td>[<fmt:message key="nome.membro.programado" />]
					</td>
				</tr>
			</tbody>
		</table>

		<p>
			<fmt:message key="sobre.gerando.programacao.part3" />
		</p>
		<ul>
			<li><fmt:message key="sobre.regra.ordenacao.part1" /></li>
			<li><fmt:message key="sobre.regra.ordenacao.part2" /></li>
		</ul>
		<hr />
		<hr />

		<h3>
			<fmt:message key="imprimindo.tabelas" />
		</h3>
		<p>
			<fmt:message key="sobre.imprimir.tabelas.part1" />
			<a> <span class="glyphicon glyphicon-print btn-lg"></span></a>.
			<fmt:message key="sobre.imprimir.tabelas.part2" />

		</p>

		<hr />
		<hr />

		<h3>
			<fmt:message key="demais.informacoes" />
		</h3>
		<p>
			<fmt:message key="sobre.demais.informacoes" />
		</p>

	</tiles:putAttribute>
</tiles:insertTemplate>